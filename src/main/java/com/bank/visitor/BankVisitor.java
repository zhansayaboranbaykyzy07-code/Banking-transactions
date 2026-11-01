package com.bank.visitor;

import com.bank.model.CheckingAccount;
import com.bank.model.CreditCard;
import com.bank.model.Loan;
import com.bank.model.SavingsAccount;

public interface BankVisitor<R> {
    R visit(CheckingAccount account);
    R visit(SavingsAccount account);
    R visit(CreditCard card);
    R visit(Loan loan);
}