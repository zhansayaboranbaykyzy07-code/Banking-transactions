package com.bank.model;

import com.bank.visitor.BankVisitor;

public record CheckingAccount(String id, double balance, double feeRate) implements BankElement {
    @Override
    public <R> R accept(BankVisitor<R> visitor) {
        return visitor.visit(this); // Double dispatch
    }

    @Override
    public String getId() {
        return id;
    }
}