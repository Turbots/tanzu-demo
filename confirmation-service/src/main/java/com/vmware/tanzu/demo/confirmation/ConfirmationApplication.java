package com.vmware.tanzu.demo.confirmation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;

@SpringBootApplication
@EnableBinding(PaymentChannels.class)
public class ConfirmationApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConfirmationApplication.class, args);
    }
}
