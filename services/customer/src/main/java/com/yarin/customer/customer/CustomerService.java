package com.yarin.customer.customer;

import com.yarin.customer.dtos.CustomerMapper;
import com.yarin.customer.dtos.CustomerRequest;
import com.yarin.customer.dtos.CustomerResponse;
import com.yarin.customer.exceptions.CustomerNotFoundException;
import com.yarin.customer.kafka.CustomerEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.apache.commons.lang.StringUtils;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerService {
    private final KafkaTemplate<String, CustomerEvent> kafkaTemplate;
    private final CustomerRepository repository;
    private final CustomerMapper mapper;

    public String createCustomer(CustomerRequest request) {
        Customer customer = this.repository.save(mapper.toCustomer(request));
        sendCustomerEvent(customer, CustomerEvent.CustomerEventType.REGISTERED);
        log.info("Customer created: {}", customer);
        return customer.getId();
    }

    public void updateCustomer(CustomerRequest request) {
        var customer = this.repository.findById(request.id())
                .orElseThrow(() -> new CustomerNotFoundException(
                        String.format("Cannot update customer. The customer with the ID: %s isn't found.", request.id())
                ));
        updateCustomerData(customer, request);
        sendCustomerEvent(customer, CustomerEvent.CustomerEventType.UPDATED);
        this.repository.save(customer);
        log.info("Customer updated: {}", customer);
    }

    private void updateCustomerData(Customer customer, CustomerRequest request) {
        if (StringUtils.isNotBlank(request.firstname())) {
            customer.setFirstname(request.firstname());
        }
        if (StringUtils.isNotBlank(request.lastname())) {
            customer.setLastname(request.lastname());
        }
        if (StringUtils.isNotBlank(request.email())) {
            customer.setEmail(request.email());
        }
        if (StringUtils.isNotBlank(request.city())) {
            customer.setCity(request.city());
        }
    }

    public CustomerResponse findById(String id) {
        return this.repository.findById(id)
                .map(mapper::fromCustomer)
                .orElseThrow(() -> new CustomerNotFoundException(String.format("No customer found with the provided ID: %s", id)));
    }

    public boolean existsById(String id) {
        return this.repository.findById(id)
                .isPresent();
    }

    public void deleteCustomer(String id) {
        var customer = this.repository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(
                        String.format("Cannot delete customer. The customer with the ID: %s isn't found.", id)
                ));
        this.repository.deleteById(id);
        sendCustomerEvent(customer, CustomerEvent.CustomerEventType.DELETED);
        log.info("Customer deleted: {}", customer);
    }

    private void sendCustomerEvent(Customer customer, CustomerEvent.CustomerEventType eventType){
        log.info("Sending customer event: {} for customer: {}", eventType,  customer.getId());
        CustomerEvent event = new CustomerEvent(
                customer.getId(),
                customer.getEmail(),
                String.format("%s %s", customer.getFirstname(), customer.getLastname()),
                eventType
        );
        kafkaTemplate.send(new ProducerRecord<>("customer-events", event));
    }
}