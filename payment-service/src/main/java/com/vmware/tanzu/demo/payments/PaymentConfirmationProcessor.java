package com.vmware.tanzu.demo.payments;

import io.micrometer.core.instrument.Counter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentConfirmationProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentConfirmationProcessor.class);

    private final PaymentRepository paymentRepository;
    private final Counter confirmationCounter;

    public PaymentConfirmationProcessor(PaymentRepository paymentRepository, Counter confirmationCounter) {
        this.paymentRepository = paymentRepository;
        this.confirmationCounter = confirmationCounter;
    }

    @Transactional
    @StreamListener(PaymentChannels.CONFIRMATIONS)
    public void confirmation(PaymentConfirmation paymentConfirmation) {
        LOGGER.info("[Confirmation] - Payment [{}]", paymentConfirmation.getPaymentId());

        this.paymentRepository.confirmPayment(paymentConfirmation.getPaymentId(), PaymentStatus.CONFIRMED);
        this.confirmationCounter.increment();
    }
}
