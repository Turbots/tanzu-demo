package io.pivotal.examples.b2b.payments;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class PaymentProcessingScheduler {

    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentProcessingScheduler.class);

    private static final long EVERY_TEN_MINUTES = 10 * 60 * 1000;
    private static final long TEN_SECONDS = 10 * 1000;

    private PaymentProcessor paymentProcessor;

    public PaymentProcessingScheduler(PaymentProcessor paymentProcessor) {
        this.paymentProcessor = paymentProcessor;
    }

    @Scheduled(initialDelay = TEN_SECONDS, fixedRate = EVERY_TEN_MINUTES)
    public void processPayments() throws IOException, InterruptedException {
        LOGGER.info("Processing Payments...");

        Path path = Paths.get("payments.csv");

        BufferedReader reader = Files.newBufferedReader(path);
        Iterable<CSVRecord> records = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(reader);

        for (CSVRecord record : records) {
            String paymentId = record.get("paymentId");
            String originAccount = record.get("originAccount");
            String destinationAccount = record.get("destinationAccount");
            String amount = record.get("amount");

            Payment payment = new Payment(null, paymentId, originAccount, destinationAccount, amount, PaymentStatus.NOT_CONFIRMED);

            this.paymentProcessor.process(payment);
        }

        LOGGER.info("Done!");
    }
}
