package com.vmware.tanzu.demo.payments;

import io.micrometer.core.instrument.Counter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class PaymentProcessingScheduler {

    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentProcessingScheduler.class);

    private static final long TEN_SECONDS = 10 * 1000;

    @Value("${tanzu.payment.rate:1}")
    private long paymentRate; // number of payments every 10 seconds

    private final PaymentProcessor paymentProcessor;
    private final Counter processedCounter;

    public PaymentProcessingScheduler(PaymentProcessor paymentProcessor, Counter processedCounter) {
        this.paymentProcessor = paymentProcessor;
        this.processedCounter = processedCounter;
    }

    @Scheduled(initialDelay = TEN_SECONDS, fixedRate = TEN_SECONDS)
    public void processPayments() throws InterruptedException {
        LOGGER.info("Processing Payments...");
        ThreadLocalRandom random = ThreadLocalRandom.current();

        for (int i = 0; i < paymentRate; i++) {
            String paymentId = "" + UUID.randomUUID();
            String originAccount = "" + random.nextInt(1000);
            String destinationAccount = "" + random.nextInt(1000);
            String amount = "€" + BigDecimal.valueOf(random.nextDouble(10)).setScale(2, RoundingMode.HALF_UP);

            Payment payment = new Payment(null, paymentId, originAccount, destinationAccount, amount, PaymentStatus.NOT_CONFIRMED);

            this.paymentProcessor.process(payment);
            this.processedCounter.increment();
        }

        LOGGER.info("Done!");
    }
}
