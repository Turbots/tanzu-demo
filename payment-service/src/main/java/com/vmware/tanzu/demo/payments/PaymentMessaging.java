package com.vmware.tanzu.demo.payments;

import org.springframework.messaging.support.GenericMessage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;

@Service
public class PaymentMessaging {

    private final PaymentChannels paymentChannels;

    public PaymentMessaging(PaymentChannels paymentChannels) {
        this.paymentChannels = paymentChannels;
    }

    @Async
    public void sendPaymentMessage(@NotNull final Payment payment) {
        this.paymentChannels.payments().send(new GenericMessage<>(payment));
    }
}
