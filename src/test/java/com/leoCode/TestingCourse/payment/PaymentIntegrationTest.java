package com.leoCode.TestingCourse.payment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leoCode.TestingCourse.customer.Customer;
import com.leoCode.TestingCourse.customer.CustomerRegistrationRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Objects;
import java.util.UUID;

import static org.assertj.core.api.Fail.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/* @SpringBootTest: Makes sure that whenever we run any test in this class,
    the entire application runs.

    MockMvc: allows us to test our restful api controllers, we use the @AutoConfigureMockMvc
    annotation and @Autowired private MockMvc MvcName is used to configure;
 */
@SpringBootTest
@AutoConfigureMockMvc
public class PaymentIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void itShouldCreatePaymentSuccessfully() throws Exception {
        // Given
        UUID id = UUID.randomUUID();
        String name = "Leonel Barrientos";
        String phoneNumber = "0000000";
        Customer customer = new Customer(id, name, phoneNumber);

        /*
            -MockMvcRequestBuilders is used to test HttpMethod in this case put.
            -put("url-path")
            -contentType(): the content type being sent in this case JSON
            -content(): the content we are sending
         */
        CustomerRegistrationRequest customerRegistrationRequest = new CustomerRegistrationRequest(customer);
        ResultActions customerRegResultAction = mockMvc.perform(put("/api/v1/customer-registration")
                .contentType(MediaType.APPLICATION_JSON)
                        .content(Objects.requireNonNull(objectToJson(customerRegistrationRequest))) // requireNotNull() is used to enforce non null values
                );
        System.out.println(customerRegResultAction);

        // When
        // Then
        // Checking if the status code is OK
        customerRegResultAction.andExpect(status().isOk());
    }

    private String objectToJson(Object object) {
        try{
            /* The ObjectMapper class is used to convert an object to string JSON */
            return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
           fail("Failed to convert object to JSON");
           return null;
        }
    }
}
