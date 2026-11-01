package com.bank.visitor;

import com.bank.model.CheckingAccount;
import com.bank.model.CreditCard;
import com.bank.model.Loan;
import com.bank.model.SavingsAccount;

public class InterestCalculationVisitor implements BankVisitor<Double> {
    private double totalInterest = 0.0;

    public double getTotalInterest() {
        return totalInterest;
    }

    @Override
    public Double visit(CheckingAccount account) {
        return totalInterest;
    }

    @Override
    public Double visit(SavingsAccount account) {
        double interest = account.balance() * (account.interestRate() / 100.0);
        totalInterest += interest;
        return interest;
    }

    @Override
    public Double visit(CreditCard card) {
        double interest = card.debt() * 0.18;
        totalInterest += interest;
        return interest;
    }

    @Override
    public Double visit(Loan loan) {
        double interest = loan.principal() * (loan.interestRate() / 100.0);
        totalInterest += interest;
        return interest;
    }
}