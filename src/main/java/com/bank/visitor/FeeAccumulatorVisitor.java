package com.bank.visitor;

import com.bank.model.CheckingAccount;
import com.bank.model.CreditCard;
import com.bank.model.Loan;
import com.bank.model.SavingsAccount;

public class FeeAccumulatorVisitor implements BankVisitor<Double> {
    private double totalFees = 0.0;

    public double getTotalFees() {
        return totalFees;
    }

    @Override
    public Double visit(CheckingAccount account) {
        double fee = account.balance() * account.feeRate();
        totalFees += fee;
        return fee;
    }

    @Override
    public Double visit(SavingsAccount account) {
        return 0.0;
    }

    @Override
    public Double visit(CreditCard card) {
        double fee = card.isBlocked() ? 50.0 : 0.0;
        totalFees += fee;
        return fee;
    }

    @Override
    public Double visit(Loan loan) {
        double fee = loan.isDefaulted() ? 100.0 : 0.0;
        totalFees += fee;
        return fee;
    }
}