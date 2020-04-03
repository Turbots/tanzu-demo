package com.vmware.tanzu.demo.confirmation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

@Service
public class ConfirmationProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfirmationProcessor.class);

    @StreamListener(PaymentChannels.PAYMENTS)
    @SendTo(PaymentChannels.CONFIRMATIONS)
    private PaymentConfirmation confirm(Payment payment) throws InterruptedException {
        LOGGER.info("Payment [{}] received. Sending out confirmation", payment.getPaymentId());

        Thread.sleep(ThreadLocalRandom.current().nextLong(100));

        return new PaymentConfirmation(payment.getPaymentId());
    }
}
