package com.vmware.tanzu.demo.accounts;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.function.Consumer;

@Service
public class PaymentProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentProcessor.class);

    private final AccountRepository accountRepository;
    private final PaymentService paymentService;

    public PaymentProcessor(AccountRepository accountRepository, PaymentService paymentService) {
        this.accountRepository = accountRepository;
        this.paymentService = paymentService;
    }

    @Bean
    public Consumer<PaymentConfirmation> confirmation() {
        return confirmation -> {
            LOGGER.info("Payment Confirmation [{}]", confirmation.getPaymentId());

            Payment payment = this.paymentService.getPayment(confirmation.getPaymentId());

            if (payment != null) {
                LOGGER.info("Processing Payment [{}] with amount [{}]", payment.getPaymentId(), payment.getAmount());
                Optional<Account> origin = this.accountRepository.findById(payment.getOriginAccount());
                Optional<Account> destination = this.accountRepository.findById(payment.getDestinationAccount());

                Account originAccount = origin.orElse(new Account(payment.getOriginAccount(), BigDecimal.ZERO));
                Account destinationAccount = destination.orElse(new Account(payment.getDestinationAccount(), BigDecimal.ZERO));

                BigDecimal credit = new BigDecimal(payment.getAmount().replace('€', '0'));
                BigDecimal debit = credit.multiply(new BigDecimal(-1));

                destinationAccount.setBalance(destinationAccount.getBalance().add(credit));
                originAccount.setBalance(originAccount.getBalance().add(debit));

                accountRepository.save(originAccount);
                accountRepository.save(destinationAccount);
            }
        };
    }
}
