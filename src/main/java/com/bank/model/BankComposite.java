package com.bank.model;

import com.bank.visitor.BankVisitor;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class BankComposite implements BankElement {
    private final String id;
    private final List<BankElement> children = new CopyOnWriteArrayList<>();

    public BankComposite(String id) {
        this.id = id;
    }

    public void add(BankElement element) {
        children.add(element);
    }

    public List<BankElement> getChildren() {
        return children;
    }

    @Override
    public <R> R accept(BankVisitor<R> visitor) {
        for (BankElement child : children) {
            child.accept(visitor);
        }
        return null;
    }

    @Override
    public String getId() {
        return id;
    }
}