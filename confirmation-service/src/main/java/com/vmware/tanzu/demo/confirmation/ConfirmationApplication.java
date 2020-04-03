package io.pivotal.examples.b2b.confirmation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;

@SpringBootApplication
@EnableBinding(PaymentChannels.class)
public class B2bConfirmationApplication {

    public static void main(String[] args) {
        SpringApplication.run(B2bConfirmationApplication.class, args);
    }
}
