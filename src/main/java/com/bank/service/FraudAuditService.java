package com.bank.service;

import com.bank.model.BankComposite;
import com.bank.observer.EventRegistry;
import com.bank.observer.EventType;
import com.bank.observer.Observer;
import com.bank.observer.TransactionEvent;
import com.bank.visitor.RiskFilterVisitor;

import java.util.Map;

public class FraudAuditService implements Observer<TransactionEvent> {
    private final Map<String, BankComposite> branchMap;
    private final EventRegistry outputRegistry;

    public FraudAuditService(Map<String, BankComposite> branchMap, EventRegistry outputRegistry) {
        this.branchMap = branchMap;
        this.outputRegistry = outputRegistry;
    }

    @Override
    public void onEvent(TransactionEvent event) {
        // Когда срабатывает событие подозрения на фрод или превышение лимита:
        if (event.type() == EventType.FRAUD_SUSPECTED || event.type() == EventType.LIMIT_EXCEEDED) {
            BankComposite targetBranch = branchMap.get(event.branchId());
            if (targetBranch != null) {
                // 1. Запуск Visitor ТОЛЬКО на скомпрометированном поддереве отделения
                RiskFilterVisitor visitor = new RiskFilterVisitor();
                targetBranch.accept(visitor);

                // 2. Результат проверки отправляется обратно в систему как новое событие-отчет
                TransactionEvent reportEvent = new TransactionEvent(
                        EventType.AGGREGATED_AUDIT_REPORT,
                        event.accountId(),
                        event.branchId(),
                        event.amount(),
                        "Flagged account count: " + visitor.getFlaggedAccounts().size(),
                        System.currentTimeMillis()
                );
                outputRegistry.notifyListeners(reportEvent);
            }
        }
    }
}