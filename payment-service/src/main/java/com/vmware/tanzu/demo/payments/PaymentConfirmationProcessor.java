package com.vmware.tanzu.demo.payments;

import io.micrometer.core.instrument.Counter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Service
public class PaymentConfirmationProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentConfirmationProcessor.class);

    private final Counter confirmationCounter;
    private final PaymentService paymentService;

    public PaymentConfirmationProcessor(Counter confirmationCounter, PaymentService paymentService) {
        this.confirmationCounter = confirmationCounter;
        this.paymentService = paymentService;
    }

    @Bean
    public Consumer<PaymentConfirmation> confirmation() {
        return confirmation -> {
            LOGGER.info("[Confirmation] - Payment [{}]", confirmation.getPaymentId());
            this.paymentService.confirmPayment(confirmation.getPaymentId());
            this.confirmationCounter.increment();
        };
    }
}
