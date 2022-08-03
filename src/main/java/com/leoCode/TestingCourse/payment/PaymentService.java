package com.leoCode.TestingCourse.payment;

import com.leoCode.TestingCourse.customer.Customer;
import com.leoCode.TestingCourse.customer.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.leoCode.TestingCourse.payment.Currency.*;

@Service
public class PaymentService {

    private static final List<Currency> acceptedCurrencies = List.of(USD,GBP);
    private final CustomerRepository customerRepository;
    private final PaymentRepository paymentRepository;
    private final CardPaymentCharger cardPaymentCharger;

    @Autowired
    public PaymentService(CustomerRepository customerRepository, PaymentRepository paymentRepository, CardPaymentCharger cardPaymentCharger) {
        this.customerRepository = customerRepository;
        this.paymentRepository = paymentRepository;
        this.cardPaymentCharger = cardPaymentCharger;
    }

    public void chargeCard(UUID customerId, PaymentRequest request){
        // 1. Does customer exist if not throw
        Optional<Customer> optionalCustomer = customerRepository.findById(customerId);
        if(optionalCustomer.isEmpty()){
            throw new IllegalStateException(
                    String.format("Customer with the id %s was not found",customerId));
        }
        // 2. Do we support the currency if not throw
        boolean isCurrencySupported = acceptedCurrencies.stream()
                .anyMatch(c -> c.equals(request.getPayment().getCurrency()));
        if(!isCurrencySupported){
            throw new IllegalStateException(
                    String.format("Currency %s is not supported",
                            request.getPayment().getCurrency())
            );
        }

        // 3. Charge card
        CardPaymentCharge cardPaymentCharge = cardPaymentCharger.chargeCard(
                request.getPayment().getSource(),
                request.getPayment().getAmount(),
                request.getPayment().getCurrency(),
                request.getPayment().getDescription()
        );

        // 4. If not charged throw
        if(!cardPaymentCharge.isCardCharged()){
            throw new IllegalStateException(
                    String.format("Card %s was not charged",
                            request.getPayment().getSource())
            );
        }

        // 5. Insert payment
        request.getPayment().setCustomerId(customerId);
        paymentRepository.save(request.getPayment());

        // 6. TODO: sens sms
    }
}
