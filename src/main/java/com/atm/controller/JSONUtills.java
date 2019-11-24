package com.atm.controller;

import com.atm.model.dto.ATM;
import com.atm.model.dto.Client;
import com.atm.model.dto.account.BankAccount;
import com.atm.model.dto.account.CreditAccount;
import com.atm.model.dto.account.service.Surplie;
import com.atm.model.dto.transaction.Transaction;
import org.json.simple.JSONObject;

public class JSONUtills {

    public static JSONObject createJSONBankAccount(BankAccount bankAccount) {
        JSONObject jsonObjectBankAccount = new JSONObject();
        jsonObjectBankAccount.put("number", bankAccount.getAccountNumber());
        jsonObjectBankAccount.put("start_date", bankAccount.getAccountStartDate());
        jsonObjectBankAccount.put("end_date", bankAccount.getAccountEndDate());
        jsonObjectBankAccount.put("amount", bankAccount.getAccountAmount());
        jsonObjectBankAccount.put("client", createJSONClient(bankAccount.getAccount_client()));
        jsonObjectBankAccount.put("surplier",
                bankAccount.getSupplier() != null ?
                        createJSONSurplie(bankAccount.getSupplier()) : null);
        return jsonObjectBankAccount;
    }

    public static JSONObject createJSONSurplie(Surplie surplie) {
        JSONObject jsonObjectSurplie = new JSONObject();
        jsonObjectSurplie.put("id", surplie.getSurplierId());
        jsonObjectSurplie.put("limit", surplie.getSurplieLimit());
        jsonObjectSurplie.put("toNumber", surplie.getTo().getAccountNumber());
        return jsonObjectSurplie;
    }

    public static JSONObject createJSONClient(Client client) {
        JSONObject jsonObjectClient = new JSONObject();
        jsonObjectClient.put("id", client.getClientId());
        jsonObjectClient.put("firstname", client.getClientFirstName());
        jsonObjectClient.put("lastname", client.getClientLastName());
        jsonObjectClient.put("birthdate", client.getClientBirthDate());
        jsonObjectClient.put("phone", client.getClientPhoneNumber());
        jsonObjectClient.put("email", client.getClientEmail());
        return jsonObjectClient;
    }

    public static JSONObject createJSONTransaction(Transaction transaction, String cardNumber) {
        JSONObject jsonObjectTransaction = new JSONObject();
        jsonObjectTransaction.put("id", transaction.getTransactionId());
        jsonObjectTransaction.put("status", transaction.getTransactionStatus());
        jsonObjectTransaction.put("commission", transaction.getTransactionCommission());
        jsonObjectTransaction.put("dateTime", transaction.getTransactionDateTime());
        switch (transaction.getTransactionType()) {
            case "WITHDRAWAL":
            case "FILL": {
                jsonObjectTransaction.put("type", transaction.getTransactionType());
                jsonObjectTransaction.put("source", transaction.getAtm().getAtmAddress());
                jsonObjectTransaction.put("amount", (transaction.getTransactionType()
                        .equalsIgnoreCase("FILL") ? "+" : "-") + transaction.getTransactionAmount());
                break;
            }

            case "TRANSFER":
            default: {
                jsonObjectTransaction.put("type", transaction.getTransactionType());
                BankAccount bankAccount;
                if (transaction.getBankAccountFrom().getAccountNumber().equals(cardNumber)) {
                    jsonObjectTransaction.put("amount", "-" + transaction.getTransactionAmount());
                    bankAccount = transaction.getBankAccountTo();
                } else {
                    jsonObjectTransaction.put("amount", "+" + transaction.getTransactionAmount());
                    bankAccount = transaction.getBankAccountFrom();
                }
                Client client = bankAccount.getAccount_client();
                jsonObjectTransaction.put("source", client.getClientFirstName() + " " + client.getClientLastName() + " " + bankAccount.getAccountNumber());
                break;
            }
        }
        jsonObjectTransaction.put("commission", transaction.getTransactionCommission());
        return jsonObjectTransaction;
    }
}
