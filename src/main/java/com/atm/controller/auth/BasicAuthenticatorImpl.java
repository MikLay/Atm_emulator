package com.atm.controller.auth;

import com.atm.controller.HttpServerAtm;
import com.atm.model.HibernateUtil;
import com.atm.model.dto.account.BankAccount;
import com.atm.model.service.BankAccountService;
import com.sun.net.httpserver.*;
import org.hibernate.Transaction;

import java.io.IOException;
import java.util.Base64;

public class BasicAuthenticatorImpl extends BasicAuthenticator {

    private BankAccountService bankAccountService;

    public BasicAuthenticatorImpl(String s, BankAccountService bankAccountService) {
        super(s);
        this.bankAccountService = bankAccountService;
    }

    @Override
    public Authenticator.Result authenticate(HttpExchange var1) {
        Headers var2 = var1.getRequestHeaders();
        String var3 = var2.getFirst("Authorization");
        if (var3 == null) {
            Headers var11 = var1.getResponseHeaders();
            var11.set("WWW-Authenticate", "Basic realm=\"" + this.realm + "\"");
            try {
                if (HttpServerAtm.addResponses(var1)) return null;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return new Retry(405);
        } else {
            int var4 = var3.indexOf(32);
            if (var4 != -1 && var3.substring(0, var4).equals("Basic")) {
                byte[] var5 = Base64.getDecoder().decode(var3.substring(var4 + 1));
                String var6 = new String(var5);
                int var7 = var6.indexOf(58);
                String var8 = var6.substring(0, var7);
                String var9 = var6.substring(var7 + 1);
                System.out.println("ins");
                if (this.checkCredentials(var8, var9)) {
                    return new Success(new HttpPrincipal(var8, this.realm));
                } else {
                    Headers var10 = var1.getResponseHeaders();
                    var10.set("WWW-Authenticate", "Basic realm=\"" + this.realm + "\"");
                    return new Failure(401);
                }
            } else {
                System.out.println("out");
                return new Failure(401);
            }
        }
    }

    @Override
    public boolean checkCredentials(String cardNumber, String code) {
        Transaction transactionUserAuth = HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
        try {
            BankAccount bankAccount;
            bankAccount = bankAccountService.authBankAccount(cardNumber, code);
            return bankAccount != null;
        } catch (Exception e) {
            return false;
        } finally {
            transactionUserAuth.commit();
        }
    }

}
