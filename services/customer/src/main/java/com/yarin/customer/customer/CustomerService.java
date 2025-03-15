package com.yarin.customer.customer;

import com.yarin.customer.dtos.CustomerMapper;
import com.yarin.customer.dtos.CustomerRequest;
import com.yarin.customer.dtos.CustomerResponse;
import com.yarin.customer.exceptions.CustomerNotFoundException;
import com.yarin.customer.kafka.UserRegisteredEvent;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.apache.commons.lang.StringUtils;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final KafkaTemplate<String, UserRegisteredEvent> kafkaTemplate;
    private final CustomerRepository repository;
    private final CustomerMapper mapper;

    public String createCustomer(CustomerRequest request) {
        Customer customer = this.repository.save(mapper.toCustomer(request));

        UserRegisteredEvent event = new UserRegisteredEvent(
                customer.getId(),
                customer.getEmail(),
                String.format("%s %s", customer.getFirstname(), customer.getLastname())
        );
        try{
            kafkaTemplate.send(new ProducerRecord<>("customer-events", event));
        }
        catch (KafkaException e){
            System.err.println("Failed to send message to Kafka: " + e.getMessage());
        }
        return customer.getId();
    }

    public void updateCustomer(CustomerRequest request) {
        var customer = this.repository.findById(request.id())
                .orElseThrow(() -> new CustomerNotFoundException(
                        String.format("Cannot update customer. The customer with the ID: %s isn't found.", request.id())
                ));
        updateCustomerData(customer, request);
        this.repository.save(customer);
    }

    private void updateCustomerData(Customer customer, CustomerRequest request) {
        if (StringUtils.isNotBlank(request.firstname())) {
            customer.setFirstname(request.firstname());
        }
        if (StringUtils.isNotBlank(request.email())) {
            customer.setEmail(request.email());
        }
        if (request.city() != null) {
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
        this.repository.deleteById(id);
    }
}