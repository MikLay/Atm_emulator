package com.atm.controller.handler;

import com.atm.controller.HttpServerAtm;
import com.atm.controller.JSONUtills;
import com.atm.model.HibernateUtil;
import com.atm.model.PasswordUtils;
import com.atm.model.dto.account.BankAccount;
import com.atm.model.dto.account.CreditAccount;
import com.atm.model.dto.account.DepositAccount;
import com.atm.model.service.BankAccountService;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import lombok.extern.log4j.Log4j;
import org.hibernate.Transaction;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.Arrays;

@Log4j
public class AuthHandler implements HttpHandler {
    BankAccountService bankAccountService;

    public AuthHandler(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    private static String createJSONAccount(BankAccount bankAccount) {
        JSONObject jsonAccount = JSONUtills.createJSONBankAccount(bankAccount);
        switch (bankAccount.getAccountType()) {
            case "MAIN":
                jsonAccount.put("type", "MAIN");
                break;
            case "DEPOSIT":
                DepositAccount depositAccount = (DepositAccount) bankAccount;
                jsonAccount.put("type", "DEPOSIT");
                jsonAccount.put("depositPercent", depositAccount.getDepositAccountPercent());
                break;
            default:
            case "CREDIT":
                CreditAccount creditAccount = (CreditAccount) bankAccount;
                jsonAccount.put("type", "CREDIT");
                jsonAccount.put("creditLimit", creditAccount.getAccountCreditLimit());
        }
        return jsonAccount.toString();
    }


    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        log.info("handle start with httpExchange: " + httpExchange);

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
                response = createJSONAccount(bankAccount);
                HttpServerAtm.writeSuccessResponse(httpExchange, response);
            }
        } catch (Exception e) {
            HttpServerAtm.writeBadResponse(httpExchange, "Error");
        } finally {
            transactionUserAuth.commit();
        }
    }
}
