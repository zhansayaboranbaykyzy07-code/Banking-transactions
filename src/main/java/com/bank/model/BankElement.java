package com.bank.model;

import com.bank.visitor.BankVisitor;

public interface BankElement {
    <R> R accept(BankVisitor<R> visitor);
    String getId();
}