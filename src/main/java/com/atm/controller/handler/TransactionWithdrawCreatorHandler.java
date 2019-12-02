package com.atm.controller.handler;

import com.atm.controller.Constants;
import com.atm.controller.DateTimeUtils;
import com.atm.controller.HttpServerAtm;
import com.atm.controller.JSONUtills;
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
public class TransactionWithdrawCreatorHandler implements HttpHandler {

    private BankAccountService bankAccountService;
    private TransactionService transactionService;
    private ATMService atmService;

    public TransactionWithdrawCreatorHandler(BankAccountService bankAccountService, TransactionService transactionService, ATMService atmService) {
        this.bankAccountService = bankAccountService;
        this.transactionService = transactionService;
        this.atmService = atmService;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        log.info("[Info] TransactionWithdrawCreatorHandler start with httpExchange: " + httpExchange);

        if (HttpServerAtm.addResponses(httpExchange)) return;


        Map<String, String> params = HttpServerAtm.queryToMap(httpExchange.getRequestURI().getQuery());

        BankAccount bankAccountSender = null;
        ATM atmReceiver = null;
        Integer transaction_real_id = null;
        com.atm.model.dto.transaction.Transaction transaction;


        Transaction getObjectsTransaction = HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
        try {

            String cardNumber = httpExchange.getPrincipal().getUsername();
            atmReceiver = atmService.getById(Integer.valueOf(params.get("atmId")));
            bankAccountSender = bankAccountService.getByNumber(cardNumber);

        } catch (Exception e) {
            e.printStackTrace();
            HttpServerAtm.writeBadResponse(httpExchange, "Error Excused");
        } finally {
            getObjectsTransaction.commit();
        }


        Transaction createWithdrowTransaction = HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
        try {
            String response;
            if (atmReceiver == null) {
                response = "Wrong atm id";
                HttpServerAtm.writeBadResponse(httpExchange, response);
            } else if (bankAccountSender.getAccountType().equalsIgnoreCase("DEPOSIT")) {
                response = "It is not allowed to create transaction from deposit account";
                HttpServerAtm.writeDataErroResponse(httpExchange, response, 400);
            } else {
                Long amount = Long.valueOf(params.get("amount"));
                long commission = (long) countCommission(bankAccountSender.getAccountType(), amount);
                if (!transactionAbility(bankAccountSender, amount, commission)) {
                    response = "Not enough money";
                    HttpServerAtm.writeDataErroResponse(httpExchange, response, 400);
                } else if (atmReceiver.getAtmAmount() < amount) {
                    response = "Sorry not enough money on ATM";
                    HttpServerAtm.writeDataErroResponse(httpExchange, response, 400);
                } else {
                    transaction_real_id = transactionService.createTransaction("WITHDRAWAL", amount, false, commission, bankAccountSender, null, atmReceiver, DateTimeUtils.getServerLocalTime().toString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            HttpServerAtm.writeBadResponse(httpExchange, "Error Excused");
        } finally {
            createWithdrowTransaction.commit();
        }

        Transaction updateTransTransaction = HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();

        try {
            if (transaction_real_id != null) {

                transaction = transactionService.getTransactionById(transaction_real_id);

                Long senderAmount = bankAccountSender.getAccountAmount();
                Long atmAmount = atmReceiver.getAtmAmount();

                bankAccountSender.setAccountAmount(senderAmount - transaction.getTransactionAmount() - transaction.getTransactionCommission());
                atmReceiver.setAtmAmount(atmAmount - transaction.getTransactionAmount());
                atmService.updateATM(atmReceiver);
                bankAccountService.updateBankAccount(bankAccountSender);

                transaction.setTransactionStatus(true);
                transactionService.updateTransaction(transaction);
                String response = JSONUtills.createJSONTransaction(transaction, bankAccountSender.getAccountNumber()).toString();
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
                return amount * Constants.WITHDRAW_MAIN;
            }
            case "CREDIT": {
                return amount * Constants.WITHDRAW_CREDIT;
            }
            default:
                return 0d;
        }
    }

    private boolean transactionAbility(BankAccount bankAccountSender, Long amount, double commission) {
        switch (bankAccountSender.getAccountType()) {
            case "MAIN": {
                return bankAccountSender.getAccountAmount() - amount - commission >= 0;
            }
            case "CREDIT": {
                CreditAccount creditAccount = (CreditAccount) bankAccountSender;
                return bankAccountSender.getAccountAmount() - amount - commission >= -creditAccount.getAccountCreditLimit();

            }
            default:
                return false;
        }
    }
}
