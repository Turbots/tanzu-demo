package io.pivotal.examples.b2b.confirmation;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface PaymentChannels {

    String PAYMENTS = "payments";
    String CONFIRMATIONS = "confirmations";

    @Input(PAYMENTS)
    SubscribableChannel payments();

    @Output(CONFIRMATIONS)
    MessageChannel confirmations();
}
