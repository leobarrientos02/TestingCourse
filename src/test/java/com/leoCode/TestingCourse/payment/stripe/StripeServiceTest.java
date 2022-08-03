package com.leoCode.TestingCourse.payment.stripe;

import com.leoCode.TestingCourse.payment.Currency;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class StripeServiceTest {

    private StripeService underTest;

    @BeforeEach
    void setUp() {
        underTest = new StripeService();
    }

    @Test
    void itShouldChargeCard() {
        // Given
        String cardSource = "0x0x0x";
        BigDecimal amount = new BigDecimal("10.00");
        Currency usd  = Currency.USD;
        String description = "Donation";

        // When
        underTest.chargeCard(cardSource, amount, usd, description);
        // Then
    }
}