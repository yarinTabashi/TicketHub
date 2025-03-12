package com.yarin.customer;

import com.yarin.customer.customer.Customer;
import com.yarin.customer.customer.CustomerRepository;
import com.yarin.customer.customer.CustomerService;
import com.yarin.customer.dtos.CustomerMapper;
import com.yarin.customer.dtos.CustomerRequest;
import com.yarin.customer.dtos.CustomerResponse;
import com.yarin.customer.exceptions.CustomerNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {
    @Mock
    private CustomerRepository repository;
    @Mock
    private CustomerMapper mapper;
    @InjectMocks
    private CustomerService service;
    private static final String CUSTOMER_ID = "123", FIRST_NAME = "Moshe", LAST_NAME = "mylast", EMAIL = "moshe@email.com", CITY = "Tel Aviv";

    @Test
    public void createCustomer_validInput_success(){
        CustomerRequest request = Mockito.mock(CustomerRequest.class);
        Customer mappedCustomer = new Customer(CUSTOMER_ID, null, null, null, null);

        Mockito.when(mapper.toCustomer(request)).thenReturn(mappedCustomer);
        Mockito.when(repository.save(mappedCustomer)).thenReturn(mappedCustomer);

        String result = service.createCustomer(request);
        Assertions.assertEquals(CUSTOMER_ID, result);
    }

    @Test
    public void updateCustomer_notFound_fail(){
        // Given
        CustomerRequest request = new CustomerRequest(CUSTOMER_ID, FIRST_NAME, LAST_NAME, EMAIL, "Tel Aviv");
        Mockito.when(repository.findById(CUSTOMER_ID)).thenReturn(Optional.empty());

        Throwable thrown = Assertions.assertThrows(CustomerNotFoundException.class, () -> {
            service.updateCustomer(request);
        });

        Assertions.assertEquals(
                String.format("Cannot update customer. The customer with the ID: %s isn't found.",CUSTOMER_ID),
                thrown.getMessage()
        );
    }

    @Test
    public void findById_success(){
        Customer customer = new Customer(CUSTOMER_ID, null, null, null, null);
        CustomerResponse expectedResponse = new CustomerResponse(CUSTOMER_ID, null, null, null, null);

        Mockito.when(repository.findById(CUSTOMER_ID)).thenReturn(Optional.of(customer));
        Mockito.when(mapper.fromCustomer(customer)).thenReturn(expectedResponse); // Mock the mapper

        CustomerResponse response = service.findById(CUSTOMER_ID);
        Assertions.assertEquals(CUSTOMER_ID, response.id());
    }

    @Test
    public void findById_fails(){
        Mockito.when(repository.findById(CUSTOMER_ID)).thenReturn(Optional.empty());

        Throwable thrown = Assertions.assertThrows(CustomerNotFoundException.class, () ->{
           service.findById(CUSTOMER_ID) ;
        });
        Assertions.assertEquals
                (String.format("No customer found with the provided ID: %s",CUSTOMER_ID),
                thrown.getMessage());
    }

    @Test
    public void existsById_success(){
        Customer customer = new Customer(CUSTOMER_ID, null, null, null, null);
        Mockito.when(repository.findById(CUSTOMER_ID)).thenReturn(Optional.of(customer));
        boolean result = service.existsById(CUSTOMER_ID);
        Assertions.assertTrue(result);
    }


    @Test
    public void existsById_fails(){
        Mockito.when(repository.findById(CUSTOMER_ID)).thenReturn(Optional.empty());
        boolean result = service.existsById(CUSTOMER_ID);
        Assertions.assertFalse(result);
    }

    @Test
    public void deleteCustomer_success(){
        service.deleteCustomer(CUSTOMER_ID);
        Mockito.verify(repository).deleteById(CUSTOMER_ID);
    }
}
