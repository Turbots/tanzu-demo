package io.pivotal.examples.b2b.b2baccounts;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.math.BigDecimal;

@RedisHash("accounts")
public class Account {

    public Account(String number, BigDecimal balance) {
        this.number = number;
        this.balance = balance;
    }

    @Id
    private String number;
    private BigDecimal balance;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
