package com.vmware.tanzu.demo.payments;

import javax.persistence.*;

@Entity
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String paymentId;
    private String originAccount;
    private String destinationAccount;
    private String amount;

    @Enumerated(value = EnumType.STRING)
    private PaymentStatus paymentStatus = PaymentStatus.NOT_CONFIRMED;

    public Payment() {
    }

    public Payment(Long id, String paymentId, String originAccount, String destinationAccount, String amount, PaymentStatus paymentStatus) {
        this.id = id;
        this.paymentId = paymentId;
        this.originAccount = originAccount;
        this.destinationAccount = destinationAccount;
        this.amount = amount;
        this.paymentStatus = paymentStatus;
    }

    public Long getId() {
        return id;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public String getOriginAccount() {
        return originAccount;
    }

    public String getDestinationAccount() {
        return destinationAccount;
    }

    public String getAmount() {
        return amount;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }
}
