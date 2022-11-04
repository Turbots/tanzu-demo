package com.vmware.tanzu.demo.payments;

import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.ThreadLocalRandom;

@Service
public class PaymentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentService.class);

    private final PaymentRepository paymentRepository;
    private final PaymentMessaging paymentMessaging;

    public PaymentService(PaymentRepository paymentRepository, PaymentMessaging paymentMessaging) {
        this.paymentRepository = paymentRepository;
        this.paymentMessaging = paymentMessaging;
    }

    @Transactional
    public void process(@NotNull final Payment payment) throws InterruptedException {
        LOGGER.info("Processing payment [{}] from [{}] to [{}] ({})", payment.getPaymentId(), payment.getOriginAccount(), payment.getDestinationAccount(), payment.getAmount());

        this.paymentRepository.save(payment);
        LOGGER.debug("Payment stored in database as [{}]", PaymentStatus.NOT_CONFIRMED);

        this.registerWithMainframe(payment);
        this.paymentMessaging.sendPaymentMessage(payment);
    }

    @Transactional
    public void confirmPayment(String paymentId) {
        LOGGER.debug("Confirming payment {} in database", paymentId);
        this.paymentRepository.confirmPayment(paymentId, PaymentStatus.CONFIRMED);
    }

    private void registerWithMainframe(final Payment payment) throws InterruptedException {
        LOGGER.debug("Registering payment {} in mainframe", payment.getPaymentId());

        Thread.sleep(ThreadLocalRandom.current().nextLong(100));

        LOGGER.debug("Done!");
    }
}
