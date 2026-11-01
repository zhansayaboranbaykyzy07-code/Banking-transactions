package com.bank.model;

import com.bank.visitor.BankVisitor;

public record SavingsAccount(String id, double balance, double interestRate) implements BankElement {
    @Override
    public <R> R accept(BankVisitor<R> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String getId() {
        return id;
    }
}