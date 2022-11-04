package com.vmware.tanzu.demo.confirmation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;

@Service
public class ConfirmationProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfirmationProcessor.class);

    @Bean
    public Function<Payment, PaymentConfirmation> confirm() {
        return payment -> {
            LOGGER.info("Payment [{}] received. Sending out confirmation", payment.getPaymentId());

            try {
                Thread.sleep(ThreadLocalRandom.current().nextLong(100));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            return new PaymentConfirmation(payment.getPaymentId());
        };
    }
}
