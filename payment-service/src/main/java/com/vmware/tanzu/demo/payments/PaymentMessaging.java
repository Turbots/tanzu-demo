package com.vmware.tanzu.demo.payments;

import jakarta.validation.constraints.NotNull;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class PaymentMessaging {

    private final StreamBridge streamBridge;

    public PaymentMessaging(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    @Async
    public void sendPaymentMessage(@NotNull final Payment payment) {
        this.streamBridge.send("payments", new GenericMessage<>(payment));
    }
}
