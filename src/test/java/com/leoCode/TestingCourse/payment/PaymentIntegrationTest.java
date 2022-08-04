package com.leoCode.TestingCourse.payment;

import com.leoCode.TestingCourse.customer.Customer;
import com.leoCode.TestingCourse.customer.CustomerRegistrationRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leoCode.TestingCourse.customer.CustomerRegistrationService;
import com.leoCode.TestingCourse.customer.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/* @SpringBootTest: Makes sure that whenever we run any test in this class,
    the entire application runs.

    MockMvc: allows us to test our restful api controllers, we use the @AutoConfigureMockMvc
    annotation and @Autowired private MockMvc MvcName is used to configure;

    -MockMvcRequestBuilders is used to test HttpMethod in this case put.
    -put("url-path")
    -contentType(): the content type being sent in this case JSON
    -content(): the content we are sending
 */
@SpringBootTest
@AutoConfigureMockMvc
public class PaymentIntegrationTest {
    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void itShouldCreatePaymentSuccessfully() throws Exception {
        // Given a customer
        UUID customerId = UUID.randomUUID();
        Customer customer = new Customer(customerId, "James", "+447000000000");

        CustomerRegistrationRequest customerRegistrationRequest = new CustomerRegistrationRequest(customer);

        // Register
        ResultActions customerRegResultActions = mockMvc.perform(post("/api/v1/customer-registration")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectToJson(customerRegistrationRequest))));

        // Then both customer registration and payment requests are 200 status code
        customerRegResultActions.andExpect(status().isOk());
    }

    private String objectToJson(Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            fail("Failed to convert object to json");
            return null;
        }
    }
}
