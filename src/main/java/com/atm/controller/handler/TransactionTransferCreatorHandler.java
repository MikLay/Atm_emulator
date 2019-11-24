package com.atm.controller.handler;

import com.atm.controller.Constants;
import com.atm.controller.DateTimeUtils;
import com.atm.controller.HttpServerAtm;
import com.atm.model.HibernateUtil;
import com.atm.model.dto.account.BankAccount;
import com.atm.model.dto.account.CreditAccount;
import com.atm.model.service.BankAccountService;
import com.atm.model.service.TransactionService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import lombok.extern.log4j.Log4j;
import org.hibernate.Transaction;

import java.io.IOException;
import java.util.Map;

@Log4j
public class TransactionTransferCreatorHandler implements HttpHandler {

    BankAccountService bankAccountService;
    TransactionService transactionService;

    public TransactionTransferCreatorHandler(BankAccountService bankAccountService, TransactionService transactionService) {
        this.bankAccountService = bankAccountService;
        this.transactionService = transactionService;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        log.info("[Info] TransactionTransferCreatorHandler start with httpExchange: " + httpExchange);


        Map<String, String> params = HttpServerAtm.queryToMap(httpExchange.getRequestURI().getQuery());
        BankAccount bankAccountSender = null;
        BankAccount bankAccountReceiver = null;
        Integer transaction_real_id = null;
        com.atm.model.dto.transaction.Transaction transaction = null;


        Transaction getAccountsTransaction = HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
        try {

            String cardNumber = httpExchange.getPrincipal().getUsername();
            bankAccountSender = bankAccountService.getByNumber(cardNumber);
            bankAccountReceiver = bankAccountService.getByNumber(params.get("receiverNumber"));

        } catch (Exception e) {
            e.printStackTrace();
            HttpServerAtm.writeBadResponse(httpExchange, "Error Excused");
        } finally {
            getAccountsTransaction.commit();
        }


        Transaction createTransTransaction = HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
        try {
            String response;
            if (bankAccountSender == null) {
                response = "Error";
                HttpServerAtm.writeBadResponse(httpExchange, response);
            } else if (bankAccountSender.getAccountType().equalsIgnoreCase("DEPOSIT")) {
                response = "It is not allowed to create transaction from deposit account";
                HttpServerAtm.writeDataErroResponse(httpExchange, response, 400);
            } else if (bankAccountReceiver == null) {
                response = "No such Card Receiver";
                HttpServerAtm.writeDataErroResponse(httpExchange, response, 400);
            } else if (bankAccountReceiver.getAccountType().equalsIgnoreCase("DEPOSIT")) {
                response = "No permission for creating transaction to deposit account";
                HttpServerAtm.writeDataErroResponse(httpExchange, response, 400);
            } else {
                Long amount = Long.valueOf(params.get("amount"));
                long commission = (long) countCommission(bankAccountSender.getAccountType(), bankAccountReceiver.getAccountType(), amount);
                if (amount < 99) {
                    response = "Wrong sum";
                    HttpServerAtm.writeDataErroResponse(httpExchange, response, 400);
                } else if (!transactionAbility(bankAccountSender, amount, commission)) {
                    response = "Not enough money";
                    HttpServerAtm.writeDataErroResponse(httpExchange, response, 400);
                } else {
                    transaction_real_id = transactionService.createTransaction("TRANSFER", amount, false, commission, bankAccountSender, bankAccountReceiver, null, DateTimeUtils.getServerLocalTime().toString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            HttpServerAtm.writeBadResponse(httpExchange, "Error Excused");
        } finally {
            createTransTransaction.commit();
        }

        Transaction updateTransTransaction = HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();

        try {
            if (transaction_real_id != null) {

                transaction = transactionService.getTransactionById(transaction_real_id);

                System.out.println(transaction_real_id);
                Long senderAmount = bankAccountSender.getAccountAmount();
                Long receiverAmount = bankAccountReceiver.getAccountAmount();
                bankAccountSender.setAccountAmount(senderAmount - transaction.getTransactionAmount() - transaction.getTransactionCommission());
                bankAccountReceiver.setAccountAmount(receiverAmount + transaction.getTransactionAmount());
                transaction.setTransactionStatus(true);
                transactionService.updateTransaction(transaction);
                HttpServerAtm.writeSuccessResponse(httpExchange, "OK");
            }
        } catch (Exception e) {
            e.printStackTrace();
            HttpServerAtm.writeBadResponse(httpExchange, "Error Excused");
        } finally {
            updateTransTransaction.commit();
        }

    }


    private double countCommission(String typeBankAccountSender, String typeBankAccountReceiver, Long amount) {
        switch (typeBankAccountSender) {
            case "MAIN": {
                switch (typeBankAccountReceiver) {
                    case "MAIN": {
                        return amount * Constants.TRANSFER_MAIN_MAIN;
                    }
                    case "CREDIT":
                    default: {
                        return amount * Constants.TRANSFER_MAIN_CREDIT;
                    }
                }
            }
            case "CREDIT": {
                switch (typeBankAccountReceiver) {
                    case "MAIN": {
                        return amount * Constants.TRANSFER_CREDIT_MAIN;
                    }
                    case "CREDIT":
                    default: {
                        return amount * Constants.TRANSFER_CREDIT_CREDIT;
                    }
                }
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
                return bankAccountSender.getAccountAmount() - amount - commission >= creditAccount.getAccountCreditLimit();
            }
            default:
                return false;
        }
    }
}
