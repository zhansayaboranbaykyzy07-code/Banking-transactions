package com.bank.visitor;

import com.bank.model.CheckingAccount;
import com.bank.model.CreditCard;
import com.bank.model.Loan;
import com.bank.model.SavingsAccount;

public class JsonExportVisitor implements BankVisitor<String> {
    private final StringBuilder json = new StringBuilder();
    private boolean first = true;

    public String getJsonOutput() {
        return "[\n" + json.toString() + "\n]";
    }

    private void appendComma() {
        if (!first) {
            json.append(",\n");
        }
        first = false;
    }

    @Override
    public String visit(CheckingAccount account) {
        appendComma();
        json.append(String.format("  {\"type\": \"CheckingAccount\", \"id\": \"%s\", \"balance\": %.2f}", account.getId(), account.balance()));
        return json.toString();
    }

    @Override
    public String visit(SavingsAccount account) {
        appendComma();
        json.append(String.format("  {\"type\": \"SavingsAccount\", \"id\": \"%s\", \"balance\": %.2f}", account.getId(), account.balance()));
        return json.toString();
    }

    @Override
    public String visit(CreditCard card) {
        appendComma();
        json.append(String.format("  {\"type\": \"CreditCard\", \"id\": \"%s\", \"debt\": %.2f, \"blocked\": %b}", card.getId(), card.debt(), card.isBlocked()));
        return json.toString();
    }

    @Override
    public String visit(Loan loan) {
        appendComma();
        json.append(String.format("  {\"type\": \"Loan\", \"id\": \"%s\", \"principal\": %.2f, \"defaulted\": %b}", loan.getId(), loan.principal(), loan.isDefaulted()));
        return json.toString();
    }
}