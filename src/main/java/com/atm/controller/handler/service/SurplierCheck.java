package com.atm.controller.handler.service;

import com.atm.controller.DateTimeUtils;
import com.atm.controller.HttpServerAtm;
import com.atm.controller.JSONUtills;
import com.atm.model.HibernateUtil;
import com.atm.model.dto.account.BankAccount;

import com.sun.net.httpserver.HttpsExchange;
import org.hibernate.Transaction;
import com.atm.model.service.BankAccountService;
import com.atm.model.service.TransactionService;

import javax.xml.ws.spi.http.HttpHandler;
import java.io.IOException;

public class SurplierCheck {
    private BankAccountService bankAccountService;
    private TransactionService transactionService;

    public SurplierCheck(BankAccountService bankAccountService, TransactionService transactionService) {
        this.bankAccountService = bankAccountService;
        this.transactionService = transactionService;
    }

    public void checkForSurplies(BankAccount bankAccount) throws IOException {

        if (bankAccount.getSupplier() == null ||
                bankAccount.getAccountAmount() < bankAccount.getSupplier().getSurplieLimit() ||
                bankAccount.getSupplier().getFrom().getAccountType().equalsIgnoreCase("DEPOSIT")) {
            return;
        }

        BankAccount bankAccountReceiver = bankAccount.getSupplier().getTo();

        Long amount = bankAccount.getAccountAmount() - bankAccount.getSupplier().getSurplieLimit();

        transactionService.createTransaction("TRANSFER", amount, true, 0L, bankAccount, bankAccountReceiver, null, DateTimeUtils.getServerLocalTime().toString());


        Long senderAmount = bankAccount.getAccountAmount();
        Long receiverAmount = bankAccountReceiver.getAccountAmount();

        bankAccount.setAccountAmount(senderAmount - amount);
        bankAccountReceiver.setAccountAmount(receiverAmount + amount);

        bankAccountService.updateBankAccount(bankAccountReceiver);
        bankAccountService.updateBankAccount(bankAccount);

    }

}
