package com.vmware.tanzu.demo.payments;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface PaymentChannels {

    String PAYMENTS = "payments";
    String CONFIRMATIONS = "confirmations";

    @Output(value = PAYMENTS)
    MessageChannel payments();

    @Input(value = CONFIRMATIONS)
    SubscribableChannel confirmations();

}
