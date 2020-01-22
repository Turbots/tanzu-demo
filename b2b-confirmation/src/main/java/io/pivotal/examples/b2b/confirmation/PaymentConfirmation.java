package io.pivotal.examples.b2b.confirmation;

import java.time.LocalDateTime;

public class PaymentConfirmation {

    private String paymentId;
    private LocalDateTime instant;

    public PaymentConfirmation(String paymentId) {
        this.paymentId = paymentId;
        this.instant = LocalDateTime.now();
    }

    public String getPaymentId() {
        return paymentId;
    }

    public LocalDateTime getInstant() {
        return instant;
    }
}
