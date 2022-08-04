package com.leoCode.TestingCourse.payment.stripe;

import com.leoCode.TestingCourse.payment.CardPaymentCharge;
import com.leoCode.TestingCourse.payment.CardPaymentCharger;
import com.leoCode.TestingCourse.payment.Currency;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@ConditionalOnProperty(
        value = "stripe.enabled",
        havingValue = "false"
)
public class MockStripeService implements CardPaymentCharger {
    @Override
    public CardPaymentCharge chargeCard(String cardSource,
                                        BigDecimal amount,
                                        Currency currency,
                                        String description) {
        return new CardPaymentCharge(true);
    }
}
