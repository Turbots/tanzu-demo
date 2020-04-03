package com.vmware.tanzu.demo.payments;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class PaymentProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentProcessor.class);

    private final PaymentRepository paymentRepository;
    private final PaymentMessaging paymentMessaging;

    public PaymentProcessor(PaymentRepository paymentRepository, PaymentMessaging paymentMessaging) {
        this.paymentRepository = paymentRepository;
        this.paymentMessaging = paymentMessaging;
    }

    @Transactional
    public void process(@NotNull final Payment payment) throws InterruptedException {
        LOGGER.info("Processing payment [{}] from [{}] to [{}] ({})", payment.getPaymentId(), payment.getOriginAccount(), payment.getDestinationAccount(), payment.getAmount());

        this.paymentRepository.save(payment);
        LOGGER.debug("Payment stored in database as [{}]", PaymentStatus.NOT_CONFIRMED);

        this.transact(payment);
        LOGGER.debug("Payment transacted");

        this.paymentMessaging.sendPaymentMessage(payment);
    }

    private void transact(final Payment payment) throws InterruptedException {
        LOGGER.debug("Performing transaction...");

        Thread.sleep(ThreadLocalRandom.current().nextLong(100));

        LOGGER.debug("Done!");
    }
}
