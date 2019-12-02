package com.atm.controller.handler;

import com.atm.controller.DateTimeUtils;
import com.atm.controller.HttpServerAtm;
import com.atm.model.HibernateUtil;
import com.atm.model.dto.account.BankAccount;
import com.atm.model.service.BankAccountService;
import com.atm.model.service.SurplierService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import lombok.extern.log4j.Log4j;
import org.hibernate.Transaction;

import java.io.IOException;
import java.util.Map;

@Log4j
public class SurplierHandler implements HttpHandler {

    private BankAccountService bankAccountService;
    private SurplierService surplierService;

    public SurplierHandler(BankAccountService bankAccountService, SurplierService surplierService) {
        this.bankAccountService = bankAccountService;
        this.surplierService = surplierService;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        log.info("[Info] SurplierHandler start with httpExchange: " + httpExchange);

        if (HttpServerAtm.addResponses(httpExchange)) return;

        Map<String, String> params = HttpServerAtm.queryToMap(httpExchange.getRequestURI().getQuery());

        BankAccount bankAccountSender = null;
        BankAccount bankAccountReceiver = null;
        Transaction getObjectsTransaction = HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
        try {
            String cardNumber = httpExchange.getPrincipal().getUsername();
            bankAccountSender = bankAccountService.getByNumber(cardNumber);
            bankAccountReceiver = bankAccountService.getByNumber(params.get("supplierNumber"));

        } catch (Exception e) {
            e.printStackTrace();
            HttpServerAtm.writeBadResponse(httpExchange, "Error Excused");
        } finally {
            getObjectsTransaction.commit();
        }


        Transaction createTransTransaction = HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
        try {
            String response;
            if (bankAccountSender == null) {
                response = "Error";
                HttpServerAtm.writeBadResponse(httpExchange, response);
            } else if (bankAccountSender.getAccountType().equalsIgnoreCase("DEPOSIT")) {
                response = "It is not allowed to create supplier for Deposit account";
                HttpServerAtm.writeDataErroResponse(httpExchange, response, 400);
            } else {
                Long amount = Long.valueOf(params.get("amount"));
                if (bankAccountSender.getSupplier() == null) {
                    if (bankAccountReceiver == null) {
                        response = "No such Supplier Card Number";
                        HttpServerAtm.writeDataErroResponse(httpExchange, response, 400);
                    } else if (bankAccountReceiver.getAccountType().equalsIgnoreCase("DEPOSIT")) {
                        response = "No permission for creating supplier as Deposit account";
                        HttpServerAtm.writeDataErroResponse(httpExchange, response, 400);
                    } else if (bankAccountReceiver.getAccountNumber().equalsIgnoreCase(bankAccountSender.getAccountNumber())) {
                        response = "You are not allowed to use the same card as Supplier";
                        HttpServerAtm.writeDataErroResponse(httpExchange, response, 400);
                    } else {
                        surplierService.createSurplie(bankAccountSender, bankAccountReceiver, amount);
                    }
                } else {
                    if (bankAccountReceiver == null) {
                        surplierService.deleteSurplier(bankAccountSender.getSupplier().getSurplierId(), bankAccountSender, bankAccountSender.getSupplier().getTo(), bankAccountSender.getSupplier().getSurplieLimit());
                    } else {
                        surplierService.updateSurplier(bankAccountSender.getSupplier().getSurplierId(), bankAccountSender, bankAccountReceiver, amount);
                    }
                }
                HttpServerAtm.writeSuccessResponse(httpExchange, "OK");
            }
        } catch (Exception e) {
            e.printStackTrace();
            HttpServerAtm.writeBadResponse(httpExchange, "Error Excused");
        } finally {
            createTransTransaction.commit();
        }


    }
}
