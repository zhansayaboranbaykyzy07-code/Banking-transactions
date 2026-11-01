package org.example;

import com.bank.model.*;
import com.bank.observer.*;
import com.bank.service.FraudAuditService;
import com.bank.visitor.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Banking System Initialization ===");

        // 1. Построение дерева (Composite)
        BankComposite headOffice = new BankComposite("HeadOffice");
        BankComposite branchAstana = new BankComposite("Branch-Astana");

        CheckingAccount acc1 = new CheckingAccount("ACC-001", 1500.0, 0.02);
        SavingsAccount acc2 = new SavingsAccount("ACC-002", 5000.0, 5.0);
        CreditCard card1 = new CreditCard("CARD-001", 1200.0, 1000.0, true);
        Loan loan1 = new Loan("LOAN-001", 10000.0, 12.0, false);

        branchAstana.add(acc1);
        branchAstana.add(acc2);
        branchAstana.add(card1);
        branchAstana.add(loan1);
        headOffice.add(branchAstana);

        Map<String, BankComposite> branchMap = new HashMap<>();
        branchMap.put("Branch-Astana", branchAstana);

        // 2. Инициализация Observer & Registry
        EventRegistry systemRegistry = new EventRegistry();
        FraudAuditService auditService = new FraudAuditService(branchMap, systemRegistry);

        // Подписка на события
        systemRegistry.subscribe(EventType.FRAUD_SUSPECTED, auditService);
        systemRegistry.subscribe(EventType.AGGREGATED_AUDIT_REPORT, event ->
                System.out.println("[AUDIT LOG] Received report for branch: " + event.branchId() + " -> " + event.payload())
        );

        // 3. Запуск Visitor'ов
        System.out.println("\n--- Running Visitors ---");
        InterestCalculationVisitor interestVisitor = new InterestCalculationVisitor();
        headOffice.accept(interestVisitor);
        System.out.printf("Total Expected Interest: %.2f$\n", interestVisitor.getTotalInterest());

        JsonExportVisitor jsonVisitor = new JsonExportVisitor();
        headOffice.accept(jsonVisitor);
        System.out.println("JSON Export Output:\n" + jsonVisitor.getJsonOutput());

        // 4. Симуляция потока событий (Concurrency - Ch. 13)
        System.out.println("\n--- Simulating Transaction Stream ---");
        ExecutorService executor = Executors.newFixedThreadPool(3);

        executor.submit(() -> systemRegistry.notifyListeners(
                new TransactionEvent(EventType.TRANSACTION_CREATED, "ACC-001", "Branch-Astana", 200.0, "ATM Withdrawal", System.currentTimeMillis())
        ));

        executor.submit(() -> systemRegistry.notifyListeners(
                new TransactionEvent(EventType.FRAUD_SUSPECTED, "CARD-001", "Branch-Astana", 5000.0, "Suspicious international transfer", System.currentTimeMillis())
        ));

        executor.shutdown();
        executor.awaitTermination(3, TimeUnit.SECONDS);
        System.out.println("\n=== System finished execution gracefully ===");
    }
}