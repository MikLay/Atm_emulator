package com.atm.controller.handler;

import com.atm.controller.HttpServerAtm;
import com.atm.controller.JSONUtills;
import com.atm.model.HibernateUtil;
import com.atm.model.dto.account.BankAccount;
import com.atm.model.service.BankAccountService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import lombok.extern.log4j.Log4j;
import org.hibernate.Transaction;
import org.json.simple.JSONArray;

import java.io.IOException;
import java.util.List;

@Log4j
public class TransactionsHandler implements HttpHandler {

    BankAccountService bankAccountService;

    public TransactionsHandler(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    private static String createJSONTransactions(List<com.atm.model.dto.transaction.Transaction> accountTransactions, String cardNumber) {
        JSONArray jsonArray = new JSONArray();
        for (com.atm.model.dto.transaction.Transaction transaction : accountTransactions) {
            jsonArray.add(JSONUtills.createJSONTransaction(transaction, cardNumber));
        }
        return jsonArray.toString();
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        log.info("[Info] TransactionsHandler start with httpExchange: " + httpExchange);
        if (HttpServerAtm.addResponses(httpExchange)) return;


        String cardNumber = httpExchange.getPrincipal().getUsername();


        Transaction transactionUserAuth = HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();


        try {
            BankAccount bankAccount;
            bankAccount = bankAccountService.getByNumber(cardNumber);
            String response;
            if (bankAccount == null) {
                response = "Access denied";
                HttpServerAtm.writeAccesDeniedResponse(httpExchange, response);
            } else {
                List<com.atm.model.dto.transaction.Transaction> transactions = bankAccount.getAccountTransactionsFrom();
                transactions.addAll(bankAccount.getAccountTransactionsTo());
                response = createJSONTransactions(transactions, bankAccount.getAccountNumber());
                HttpServerAtm.writeSuccessResponse(httpExchange, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            HttpServerAtm.writeBadResponse(httpExchange, "Error");
        } finally {
            transactionUserAuth.commit();
        }
    }
}
