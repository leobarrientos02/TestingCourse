package com.leoCode.TestingCourse.customer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest(
        properties = {
                "spring.jpa.properties.javax.persistence.validation.mode=none"
        }
) // properties = {} :tells the program to check annotation on entity's columns
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository underTest;

    @Test
    void itShouldSaveCustomer(){
        // Given
        UUID id = UUID.randomUUID();
        Customer customer = new Customer(id, "Leonel Barrientos", "123-456-7890");

        // When
        underTest.save(customer);

        // Thens
        Optional<Customer> optionalCustomer = underTest.findById(id);
        assertThat(optionalCustomer)
                .isPresent()
                .hasValueSatisfying(c -> {
                    assertThat(c).isEqualTo(customer);
                });
    }

    @Test
    void itShouldSelectCustomerByPhoneNumber(){
        // Given
        UUID id = UUID.randomUUID();
        String name = "Leonel Barrientos";
        String phoneNumber = "123-456-7890";
        Customer customer = new Customer(id, name, phoneNumber);

        // When
        underTest.save(customer);

        // Then
        Optional<Customer> optionalCustomer = underTest.selectCustomerByPhoneNumber(phoneNumber);
        assertThat(optionalCustomer).isPresent()
                .hasValueSatisfying(c -> {
                    assertThat(c).isEqualTo(customer);
                });
    }
    @Test
    void itShouldNotSelectCustomerByPhoneNumberWhenNumberDoesNotExist(){
        // Given
        String phoneNumber = "123-456-7890";

        // When
        Optional<Customer> optionalCustomer = underTest.selectCustomerByPhoneNumber(phoneNumber);

        // Then
        assertThat(optionalCustomer).isNotPresent();
    }

    @Test
    void itShouldSelectCustomerByName() {
        // Given
        UUID id = UUID.randomUUID();
        String name = "Leonel Barrientos";
        String phoneNumber = "123-456-7890";
        Customer customer = new Customer(id, name, phoneNumber);

        // When
        underTest.save(customer);

        // Then
        Optional<Customer> optionalCustomer = underTest.selectCustomerByName(name);
        assertThat(optionalCustomer).isPresent()
                .hasValueSatisfying(c -> {
                    assertThat(c).isEqualTo(customer);
                });
    }

    @Test
    void itShouldNotSelectCustomerByNameIfNameDoesNotExist() {
        // Given
        String name = "Leonel Barrientos";

        // When
        Optional<Customer> optionalCustomer = underTest.selectCustomerByName(name);

        // Then
        assertThat(optionalCustomer).isNotPresent();
    }
    @Test
    void itShouldNotSaveCustomerWhenNameIsNull() {
        // Given
        UUID id = UUID.randomUUID();
        String phoneNumber = "123-456-7890";
        Customer customer = new Customer(id, null, phoneNumber);

        // When
        // Then
        assertThatThrownBy(() -> underTest.save(customer))
                .hasMessageContaining("not-null property references a null or transient value : com.leoCode.TestingCourse.customer.Customer.name")
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void itShouldNotSaveCustomerWhenPhoneNumberIsNull() {
        // Given
        UUID id = UUID.randomUUID();
        String name = "Leonel Barrientos";
        Customer customer = new Customer(id, name, null);

        // When
        // Then
        assertThatThrownBy(() -> underTest.save(customer))
                .hasMessageContaining("not-null property references a null or transient value : com.leoCode.TestingCourse.customer.Customer.phoneNumber")
                .isInstanceOf(DataIntegrityViolationException.class);
    }

}