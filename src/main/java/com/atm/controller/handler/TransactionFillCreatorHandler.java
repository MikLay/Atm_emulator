package com.atm.controller.handler;

import com.atm.controller.Constants;
import com.atm.controller.DateTimeUtils;
import com.atm.controller.HttpServerAtm;
import com.atm.controller.JSONUtills;
import com.atm.controller.handler.service.SurplierCheck;
import com.atm.model.HibernateUtil;
import com.atm.model.dto.ATM;
import com.atm.model.dto.account.BankAccount;
import com.atm.model.dto.account.CreditAccount;
import com.atm.model.service.ATMService;
import com.atm.model.service.BankAccountService;
import com.atm.model.service.TransactionService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import lombok.extern.log4j.Log4j;
import org.hibernate.Transaction;

import java.io.IOException;
import java.util.Map;

@Log4j
public class TransactionFillCreatorHandler implements HttpHandler {

    private BankAccountService bankAccountService;
    private TransactionService transactionService;
    private ATMService atmService;
    private SurplierCheck surplierCheck;

    public TransactionFillCreatorHandler(BankAccountService bankAccountService, TransactionService transactionService, ATMService atmService) {
        this.bankAccountService = bankAccountService;
        this.transactionService = transactionService;
        this.atmService = atmService;
        this.surplierCheck = new SurplierCheck(bankAccountService, transactionService);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        log.info("[Info] TransactionFillCreatorHandler start with httpExchange: " + httpExchange);
        if (HttpServerAtm.addResponses(httpExchange)) return;

        Map<String, String> params = HttpServerAtm.queryToMap(httpExchange.getRequestURI().getQuery());

        BankAccount bankAccountReceiver = null;
        ATM atmSender = null;
        Integer transaction_real_id = null;
        com.atm.model.dto.transaction.Transaction transaction;


        Transaction getObjectsTransaction = HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
        try {

            String cardNumber = httpExchange.getPrincipal().getUsername();
            atmSender = atmService.getById(Integer.valueOf(params.get("atmId")));
            bankAccountReceiver = bankAccountService.getByNumber(cardNumber);

        } catch (Exception e) {
            e.printStackTrace();
            HttpServerAtm.writeBadResponse(httpExchange, "Error Excused");
        } finally {
            getObjectsTransaction.commit();
        }


        Transaction createWithdrawTransaction = HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
        try {
            String response;
            if (atmSender == null) {
                response = "Wrong atm id";
                HttpServerAtm.writeBadResponse(httpExchange, response);
            } else if (bankAccountReceiver.getAccountType().equalsIgnoreCase("DEPOSIT")) {
                response = "It is not allowed to create transaction to deposit account";
                HttpServerAtm.writeDataErroResponse(httpExchange, response, 400);
            } else {
                Long amount = Long.valueOf(params.get("amount"));
                long commission = (long) countCommission(bankAccountReceiver.getAccountType(), amount);
                if (atmSender.getAtmAmount() < amount) {
                    response = "Sorry not enough money on ATM";
                    HttpServerAtm.writeDataErroResponse(httpExchange, response, 400);
                } else {
                    transaction_real_id = transactionService.createTransaction("FILL", amount, false, commission, null, bankAccountReceiver, atmSender, DateTimeUtils.getServerLocalTime().toString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            HttpServerAtm.writeBadResponse(httpExchange, "Error Excused");
        } finally {
            createWithdrawTransaction.commit();
        }

        Transaction updateTransTransaction = HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();

        try {
            if (transaction_real_id != null) {

                transaction = transactionService.getTransactionById(transaction_real_id);

                Long receiverAmount = bankAccountReceiver.getAccountAmount();
                Long atmAmount = atmSender.getAtmAmount();

                bankAccountReceiver.setAccountAmount(receiverAmount + transaction.getTransactionAmount() - transaction.getTransactionCommission());
                atmSender.setAtmAmount(atmAmount + transaction.getTransactionAmount());
                atmService.updateATM(atmSender);
                bankAccountService.updateBankAccount(bankAccountReceiver);

                transaction.setTransactionStatus(true);
                transactionService.updateTransaction(transaction);
                String response = JSONUtills.createJSONTransaction(transaction, bankAccountReceiver.getAccountNumber()).toString();
                try {
                    surplierCheck.checkForSurplies(bankAccountReceiver);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                HttpServerAtm.writeSuccessResponse(httpExchange, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            HttpServerAtm.writeBadResponse(httpExchange, "Error Excused");
        } finally {
            updateTransTransaction.commit();
        }

    }


    private double countCommission(String typeBankAccountSender, Long amount) {
        switch (typeBankAccountSender) {
            case "MAIN": {
                return amount * Constants.FILL_MAIN;
            }
            case "CREDIT": {
                return amount * Constants.FILL_CREDIT;
            }
            default:
                return 0d;
        }
    }
}
