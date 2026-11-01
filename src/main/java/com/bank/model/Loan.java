package com.bank.model;

import com.bank.visitor.BankVisitor;

public record Loan(String id, double principal, double interestRate, boolean isDefaulted) implements BankElement {
    @Override
    public <R> R accept(BankVisitor<R> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String getId() {
        return id;
    }
}