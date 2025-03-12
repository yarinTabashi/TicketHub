package com.yarin.customer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yarin.customer.customer.CustomerController;
import com.yarin.customer.customer.CustomerService;
import com.yarin.customer.dtos.CustomerRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(MockitoExtension.class)
public class CustomerControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Mock
    private CustomerService service;
    @InjectMocks
    private CustomerController controller;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final String CUSTOMER_ID = "123", FIRST_NAME = "Moshe", LAST_NAME = "mylast", EMAIL = "moshe@email.com", CITY = "Tel Aviv";

    @Test
    public void createCustomer_success(){
        CustomerRequest request = new CustomerRequest(CUSTOMER_ID, FIRST_NAME, LAST_NAME, EMAIL, CITY);
        Mockito.when(service.createCustomer(request)).thenReturn(CUSTOMER_ID);
        ResponseEntity<String> response = controller.createCustomer(request);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(CUSTOMER_ID, response.getBody());
        Mockito.verify(service).createCustomer(request);
    }

    @Test
    public void createCustomer_InvalidEmail_fails() throws Exception {
        String invalidEmail = "email";
        CustomerRequest request = new CustomerRequest(CUSTOMER_ID, FIRST_NAME, LAST_NAME, invalidEmail, CITY);

        mockMvc.perform(post("/api/v1/customer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest()) // Expect HTTP 400
                .andExpect(jsonPath("$.email").value("Customer Email is not a valid email address")); // Check validation message


        Mockito.when(service.createCustomer(request)).thenReturn(CUSTOMER_ID);
        ResponseEntity<String> response = controller.createCustomer(request);
    }
}
