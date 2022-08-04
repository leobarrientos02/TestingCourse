package com.leoCode.TestingCourse.payment;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static com.leoCode.TestingCourse.payment.Currency.*;
import static org.assertj.core.api.Assertions.assertThat;

/*
    @DataJpaTest: this annotation is used to test our repositories to see if
    we are successfully getting the data we queried.
 */
@DataJpaTest(
        properties = {
                "spring.jpa.properties.javax.persistence.validation.mode=none"
        }
)
class PaymentRepositoryTest {

    @Autowired
    private PaymentRepository underTest;

    @Test
    void itShouldInsertPayment() {
        // Given
        Long paymentId = 1L;
        UUID customerId = UUID.randomUUID();
        BigDecimal amount = new BigDecimal("10.00");
        String source = "Paypal";
        String description = "Donation";
        Payment payment = new Payment(paymentId, customerId,amount, USD, source, description);

        // When
        underTest.save(payment);

        // Then
        Optional<Payment> optionalPayment = underTest.findById(paymentId);
        assertThat(optionalPayment)
                .isPresent()
                .hasValueSatisfying(p -> {
                    assertThat(p).isEqualTo(payment);
                });
    }
}