package com.bank.model;

import com.bank.visitor.BankVisitor;

public record CreditCard(String id, double debt, double limit, boolean isBlocked) implements BankElement {
    @Override
    public <R> R accept(BankVisitor<R> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String getId() {
        return id;
    }
}