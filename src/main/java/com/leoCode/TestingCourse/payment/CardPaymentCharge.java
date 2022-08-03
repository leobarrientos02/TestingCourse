package com.leoCode.TestingCourse.payment;

public class CardPaymentCharge {
    private final boolean isCardCharged;

    public CardPaymentCharge(boolean isCardCharged) {
        this.isCardCharged = isCardCharged;
    }

    public boolean isCardCharged() {
        return isCardCharged;
    }

    @Override
    public String toString() {
        return "CardPaymentCharge{" +
                "isCardCharged=" + isCardCharged +
                '}';
    }
}
