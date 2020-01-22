package io.pivotal.examples.b2b.payments;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class PaymentDataGenerator implements ApplicationRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentDataGenerator.class);

    @Override
    public void run(ApplicationArguments args) throws Exception {
        LOGGER.info("Generating payments.csv file...");

        Path path = Paths.get("payments.csv");
        ThreadLocalRandom random = ThreadLocalRandom.current();
        try (final CSVPrinter printer = CSVFormat.RFC4180.withHeader("paymentId", "originAccount", "destinationAccount", "amount").print(path, StandardCharsets.UTF_8);) {
            int limit = 1_000;
            for (int i = 1; i <= limit; i++) {
                UUID id = UUID.randomUUID();
                String originAccount = "NL" + random.nextLong(100000000000000L);
                String destinationAccount = "NL" + random.nextLong(100000000000000L);
                String amount = "€" + BigDecimal.valueOf(random.nextDouble(1000000)).setScale(2, RoundingMode.HALF_UP);

                printer.printRecord(id.toString(), originAccount, destinationAccount, amount);
            }
        } catch (IOException e) {
            LOGGER.error("Could not write CSV file payments.csv", e);
        }
    }
}
