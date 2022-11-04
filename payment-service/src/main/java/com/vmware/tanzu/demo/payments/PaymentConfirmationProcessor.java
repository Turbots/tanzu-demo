package com.vmware.tanzu.demo.payments;

import io.micrometer.core.instrument.Counter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Consumer;

@Service
public class PaymentConfirmationProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentConfirmationProcessor.class);

    private final PaymentRepository paymentRepository;
    private final Counter confirmationCounter;

    public PaymentConfirmationProcessor(PaymentRepository paymentRepository, Counter confirmationCounter) {
        this.paymentRepository = paymentRepository;
        this.confirmationCounter = confirmationCounter;
    }

    @Bean
    @Transactional
    public Consumer<PaymentConfirmation> confirmation() {
        return confirmation -> {
            LOGGER.info("[Confirmation] - Payment [{}]", confirmation.getPaymentId());
            this.paymentRepository.confirmPayment(confirmation.getPaymentId(), PaymentStatus.CONFIRMED);
            this.confirmationCounter.increment();
        };
    }
}
