package com.vmware.tanzu.demo.accounts;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.math.BigDecimal;

@Entity
public class Account {

    public Account() {
    }

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
