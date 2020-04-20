package com.vmware.tanzu.demo.accounts;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PaymentService {

    private final RestTemplate restTemplate;

    @Value("${payments.host:localhost:8080}")
    private String paymentsHost;

    public PaymentService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Payment getPayment(final String paymentId) {
        return this.restTemplate.getForObject("http://" + paymentsHost + "/{paymentId}", Payment.class, paymentId);
    }

    public Payment[] getPayments() {
        return this.restTemplate.getForObject("http://" + paymentsHost + "/", Payment[].class);
    }
}
