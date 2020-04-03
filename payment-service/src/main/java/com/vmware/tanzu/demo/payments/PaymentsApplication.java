package io.pivotal.examples.b2b.payments;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class B2bPaymentsApplication {

    public static void main(String[] args) {
        SpringApplication.run(B2bPaymentsApplication.class, args);
    }
}
