package io.pivotal.examples.b2b.payments;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentConfirmationProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentConfirmationProcessor.class);

    private final PaymentRepository paymentRepository;

    public PaymentConfirmationProcessor(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Transactional
    @StreamListener(PaymentChannels.CONFIRMATIONS)
    public void confirmation(PaymentConfirmation paymentConfirmation) {
        LOGGER.info("Payment Confirmation - payment [{}]", paymentConfirmation.getPaymentId());

        this.paymentRepository.confirmPayment(paymentConfirmation.getPaymentId(), PaymentStatus.CONFIRMED);
    }
}
