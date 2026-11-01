package com.bank.visitor;

import com.bank.model.CheckingAccount;
import com.bank.model.CreditCard;
import com.bank.model.Loan;
import com.bank.model.SavingsAccount;

import java.util.ArrayList;
import java.util.List;

public class RiskFilterVisitor implements BankVisitor<List<String>> {
    private final List<String> flaggedAccounts = new ArrayList<>();

    public List<String> getFlaggedAccounts() {
        return flaggedAccounts;
    }

    @Override
    public List<String> visit(CheckingAccount account) {
        if (account.balance() < 0) {
            flaggedAccounts.add(account.getId());
        }
        return flaggedAccounts;
    }

    @Override
    public List<String> visit(SavingsAccount account) {
        return flaggedAccounts;
    }

    @Override
    public List<String> visit(CreditCard card) {
        if (card.isBlocked() || card.debt() > card.limit()) {
            flaggedAccounts.add(card.getId());
        }
        return flaggedAccounts;
    }

    @Override
    public List<String> visit(Loan loan) {
        if (loan.isDefaulted()) {
            flaggedAccounts.add(loan.getId());
        }
        return flaggedAccounts;
    }
}