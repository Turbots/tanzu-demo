package com.vmware.tanzu.demo.payments;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PaymentController {

    private final PaymentRepository paymentRepository;

    public PaymentController(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @GetMapping
    @RequestMapping("/")
    public List<Payment> getPayments() {
        return this.paymentRepository.findTop50ByOrderByIdDesc();
    }

    @GetMapping
    @RequestMapping("/{paymentId}")
    public Payment getPayment(@PathVariable("paymentId") String paymentId) {
        return this.paymentRepository.findOneByPaymentId(paymentId);
    }
}
