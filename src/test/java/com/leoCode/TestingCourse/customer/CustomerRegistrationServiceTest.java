package com.leoCode.TestingCourse.customer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

class CustomerRegistrationServiceTest {
    @Mock
    private CustomerRepository customerRepository;
    @Captor
    private ArgumentCaptor<Customer> customerArgumentCaptor;
    private CustomerRegistrationService underTest;
    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        underTest = new CustomerRegistrationService(customerRepository);
    }
    @Test
    void itShouldSaveNewCustomer() {
        // Given
        UUID id = UUID.randomUUID();
        String name = "Leonel Barrientos";
        String phoneNumber = "0000";
        Customer customer = new Customer(id, name, phoneNumber);

        // ... a request
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(customer);

        // ... an empty optional is returned
        given(customerRepository.selectCustomerByPhoneNumber(phoneNumber))
                .willReturn(Optional.empty());

        // When
        underTest.registerNewCustomer(request);

        // Then
        then(customerRepository).should().save(customerArgumentCaptor.capture()); // customerArgumentCaptor.capture() = capturing the customer
        Customer customerArgumentValue = customerArgumentCaptor.getValue();
        assertThat(customerArgumentValue).isEqualTo(customer);
    }

    @Test
    void itShouldSaveNewCustomerWhenIdIsNull() {
        // Given a phone number and a customer
        String phoneNumber = "000099";
        Customer customer = new Customer(null, "Maryam", phoneNumber);

        // ... a request
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(customer);

        // ... No customer with phone number passed
        given(customerRepository.selectCustomerByPhoneNumber(phoneNumber))
                .willReturn(Optional.empty());

        // When
        underTest.registerNewCustomer(request);

        // Then
        then(customerRepository).should().save(customerArgumentCaptor.capture());
        Customer customerArgumentCaptorValue = customerArgumentCaptor.getValue();
        assertThat(customerArgumentCaptorValue)
                .isEqualToIgnoringGivenFields(customer, "id");
        assertThat(customerArgumentCaptorValue.getId()).isNotNull();
    }

    @Test
    void itShouldNotSaveCustomerWhenCustomerExists() {
        // Given
        UUID id = UUID.randomUUID();
        String name = "Leonel Barrientos";
        String phoneNumber = "0000";
        Customer customer = new Customer(id, name, phoneNumber);

        // ... a request
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(customer);

        // ... an existing customer is returned
        given(customerRepository.selectCustomerByPhoneNumber(phoneNumber))
                .willReturn(Optional.of(customer));

        // When
        underTest.registerNewCustomer(request);

        // Then
        then(customerRepository).should(never()).save(any(Customer.class));
    }

    @Test
    void itShouldThrowExceptionWhenPhoneNumberIsTaken() {
        // Given
        UUID id = UUID.randomUUID();
        String name = "Leonel Barrientos";
        String phoneNumber = "0000";
        Customer customer = new Customer(id, name, phoneNumber);
        Customer customerTwo = new Customer(id, "Jose Barrientos", phoneNumber);

        // ... a request
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(customer);

        // ... Customer found with phone number
        given(customerRepository.selectCustomerByPhoneNumber(phoneNumber))
                .willReturn(Optional.of(customerTwo));

        // When
        // Then
        assertThatThrownBy(() -> underTest.registerNewCustomer(request))
                .isInstanceOf(IllegalStateException.class)
                        .hasMessageContaining(String.format("Phone number %s is taken",phoneNumber));

        // Finally
        then(customerRepository).should(never()).save(any(Customer.class));
    }

}