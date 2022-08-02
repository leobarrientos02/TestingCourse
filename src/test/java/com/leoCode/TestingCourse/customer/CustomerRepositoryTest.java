package com.leoCode.TestingCourse.customer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository underTest;

    @Test
    void itShouldSaveCustomer(){
        // Given
        UUID id = UUID.randomUUID();
        Customer customer = new Customer(id, "Leonel Barrientos", "516-960-8086");

        // When
        underTest.save(customer);

        // Then
        Optional<Customer> optionalCustomer = underTest.findById(id);
        assertThat(optionalCustomer)
                .isPresent()
                .hasValueSatisfying(c -> {
                    assertThat(c.getId()).isEqualTo(id);
                    assertThat(c.getName()).isEqualTo("Leonel Barrientos");
                    assertThat(c.getPhoneNumber()).isEqualTo("516-960-8086");
                });
    }

    @Test
    void itShouldSelectCustomerByPhoneNumber(){
        // Given
        UUID id = UUID.randomUUID();
        String name = "Leonel Barrientos";
        String phoneNumber = "0000";
        Customer customer = new Customer(id, name, phoneNumber);

        // When
        underTest.save(customer);

        // Then
        Optional<Customer> optionalCustomer = underTest.selectCustomerByPhoneNumber(phoneNumber);
        assertThat(optionalCustomer).isPresent()
                .hasValueSatisfying(c -> {
                    assertThat(c.getId()).isEqualTo(id);
                    assertThat(c.getName()).isEqualTo(name);
                    assertThat(c.getPhoneNumber()).isEqualTo(phoneNumber);
                });
    }
    @Test
    void itShouldNotSelectCustomerByPhoneNumberWhenNumberDoesNotExist(){
        // Given
        String phoneNumber = "0000";

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
        String phoneNumber = "0000";
        Customer customer = new Customer(id, name, phoneNumber);

        // When
        underTest.save(customer);

        // Then
        Optional<Customer> optionalCustomer = underTest.selectCustomerByName(name);
        assertThat(optionalCustomer).isPresent()
                .hasValueSatisfying(c -> {
                    assertThat(c.getId()).isEqualTo(id);
                    assertThat(c.getName()).isEqualTo(name);
                    assertThat(c.getPhoneNumber()).isEqualTo(phoneNumber);
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
        String phoneNumber = "0000";
        Customer customer = new Customer(id, null, phoneNumber);

        // When
        // Then
        assertThatThrownBy(() -> underTest.save(customer))
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
                .isInstanceOf(DataIntegrityViolationException.class);
    }

}