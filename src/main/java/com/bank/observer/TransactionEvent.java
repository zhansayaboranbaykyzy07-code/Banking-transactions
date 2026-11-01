package com.bank.observer;

public record TransactionEvent(
        EventType type,
        String accountId,
        String branchId,
        double amount,
        String payload,
        long timestamp
) {
    public TransactionEvent {
        if (type == null || accountId == null || branchId == null) {
            throw new IllegalArgumentException("Type, accountId, and branchId cannot be null");
        }
    }
}