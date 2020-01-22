package io.pivotal.examples.b2b.payments;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PaymentController {

    private final PaymentRepository paymentRepository;

    public PaymentController(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @RequestMapping("/api/payments")
    public List<Payment> getPayments() {
        return this.paymentRepository.findAll();
    }
}
