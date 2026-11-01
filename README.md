# Assignment 6: Observer and Visitor Patterns
**Topic:** Variant 11 — Banking Transactions  
**Tech Stack:** Java 17, JUnit 5

---

## 1. Architectural Decisions

### Observer Pattern (Push-based)
* **Push Model Choice:** Data sources push immutable `TransactionEvent` records directly into `Observer.onEvent(event)`. This guarantees thread safety and snapshot isolation without forcing listeners to query state back from the subject.
* **Thread-Safe Registry:** Listeners are stored in `ConcurrentHashMap<EventType, CopyOnWriteArrayList<Observer<TransactionEvent>>>`. This allows registering or unregistering listeners at runtime (even within event handlers) without causing `ConcurrentModificationException` or blocking event emission.

### Visitor Pattern & Integration
* **Traversal Strategy:** Composite elements (`BankComposite`) control recursive tree traversal while visitors focus purely on business computations.
* **Double Dispatch:** Elements implement `accept(BankVisitor<R> visitor)` to invoke `visitor.visit(this)`, resolving concrete target types at runtime.
* **Integration Link:** `FraudAuditService` acts as an `Observer`. When receiving `FRAUD_SUSPECTED` or `LIMIT_EXCEEDED` events, it executes `RiskFilterVisitor` **only on the impacted branch subtree** and publishes an `AGGREGATED_AUDIT_REPORT` back into the system.

---

## 2. Structural UML Diagram

```mermaid
classDiagram
    class BankElement {
        <<interface>>
        +accept(visitor)
        +getId() String
    }
    class BankComposite {
        -List~BankElement~ children
        +add(element)
        +accept(visitor)
    }
    class CheckingAccount {
        -double balance
        -double feeRate
    }
    class CreditCard {
        -double debt
        -double limit
        -boolean isBlocked
    }
    class BankVisitor~R~ {
        <<interface>>
        +visit(CheckingAccount)
        +visit(SavingsAccount)
        +visit(CreditCard)
        +visit(Loan)
    }
    class RiskFilterVisitor {
        -List~String~ flaggedAccounts
        +getFlaggedAccounts()
    }

    BankElement <|.. BankComposite
    BankElement <|.. CheckingAccount
    BankElement <|.. CreditCard
    BankComposite o-- BankElement
    BankVisitor <|.. RiskFilterVisitor