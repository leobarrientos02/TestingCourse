package com.leoCode.TestingCourse.payment;

import com.leoCode.TestingCourse.customer.Customer;
import com.leoCode.TestingCourse.customer.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

/*
    AssertThatThrownBy() is used to ensure that an exception is being
    thrown when it is expected.
    @Mock is used with MockitoAnnotations.openMocks(this) to mock classes
    that are needed to test the class.
 */
class PaymentServiceTest {

    @Mock private CustomerRepository customerRepository;
    @Mock private PaymentRepository paymentRepository;
    @Mock private CardPaymentCharger cardPaymentCharger;

    private PaymentService underTest;
    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        underTest = new PaymentService(customerRepository, paymentRepository, cardPaymentCharger);
    }
    @Test
    void itShouldChargeCardSuccessfully() {
        // Given
        UUID customerId = UUID.randomUUID();
        // ... Customer exist
        given(customerRepository.findById(customerId)).willReturn(Optional.of(mock(Customer.class)));

        // ... Payment request
        PaymentRequest request = new PaymentRequest(
                new Payment(
                        null,
                        null,
                        new BigDecimal("100.00"),
                        Currency.USD,
                        "card123xx",
                        "Donation"
                )
        );

        // ... Card is charged successfully
        given(cardPaymentCharger.chargeCard(
                request.getPayment().getSource(),
                request.getPayment().getAmount(),
                request.getPayment().getCurrency(),
                request.getPayment().getDescription()
        )).willReturn(new CardPaymentCharge(true));

        // When
        underTest.chargeCard(customerId, request);

        // Then
        ArgumentCaptor<Payment> paymentArgumentCaptor =
                ArgumentCaptor.forClass(Payment.class);

        then(paymentRepository).should().save(paymentArgumentCaptor.capture());

        Payment paymentArgumentCaptorValue = paymentArgumentCaptor.getValue();
        assertThat(paymentArgumentCaptorValue).isEqualTo(request.getPayment());
    }

    @Test
    void itShouldThrowExceptionWhenCardIsNotCharged() {
        // Given
        UUID customerId = UUID.randomUUID();
        // ... Customer exist
        given(customerRepository.findById(customerId)).willReturn(Optional.of(mock(Customer.class)));

        // ... Payment request
        PaymentRequest request = new PaymentRequest(
                new Payment(
                        null,
                        null,
                        new BigDecimal("100.00"),
                        Currency.USD,
                        "card123xx",
                        "Donation"
                )
        );

        // ... Card is charged successfully
        given(cardPaymentCharger.chargeCard(
                request.getPayment().getSource(),
                request.getPayment().getAmount(),
                request.getPayment().getCurrency(),
                request.getPayment().getDescription()
        )).willReturn(new CardPaymentCharge(false)); // Charge is false

        // When
        // Then
        assertThatThrownBy(() -> underTest.chargeCard(customerId, request))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Card " + request.getPayment().getSource() + " was not charged");

        // ... No interactions with paymentRepository
        then(paymentRepository).shouldHaveNoInteractions();
    }

    @Test
    void itShouldNotChargeCardAndThrowExceptionWhenCurrencyNotSupported() {
        // Given
        UUID customerId = UUID.randomUUID();
        // ... Customer exist
        given(customerRepository.findById(customerId)).willReturn(Optional.of(mock(Customer.class)));

        // ... Payment request with unsupported Currency
        PaymentRequest request = new PaymentRequest(
                new Payment(
                        null,
                        null,
                        new BigDecimal("100.00"),
                        Currency.EUR, // Not in the Array contained in the PaymentService Class
                        "card123xx",
                        "Donation"
                )
        );

        // When
        assertThatThrownBy(() -> underTest.chargeCard(customerId, request))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining(
                        "Currency " + request.getPayment().getCurrency() + " is not supported");

        // Then

        // ... No interactions with cardPaymentCharger
        then(cardPaymentCharger).shouldHaveNoInteractions();

        // ... No interactions with paymentRepository
        then(paymentRepository).shouldHaveNoInteractions();
    }

    @Test
    void itShouldNotChargeAndThrowExceptionWhenCustomerIsNotFound() {
        // Given
        UUID customerId = UUID.randomUUID();

        // When customer is not found in DB
        given(customerRepository.findById(customerId)).willReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> underTest.chargeCard(customerId, new PaymentRequest(new Payment())))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining(
                        "Customer with the id " + customerId + " was not found");

        // ... No interactions with cardPaymentCharger
        then(cardPaymentCharger).shouldHaveNoInteractions();

        // ... No interactions with paymentRepository
        then(paymentRepository).shouldHaveNoInteractions();

    }
}