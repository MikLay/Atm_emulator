package com.atm.model.service.impl;

import com.atm.model.PasswordUtils;
import com.atm.model.dao.BankAccountDao;
import com.atm.model.dto.account.BankAccount;
import com.atm.model.service.BankAccountService;

import java.util.List;

public class BankAccountServiceImpl implements BankAccountService {

    private BankAccountDao accountDao;

    public BankAccountServiceImpl(BankAccountDao accountDao) {
        this.accountDao = accountDao;
    }

    @Override
    public BankAccount authBankAccount(String accountNumber, String accountPassword) {
        BankAccount bankAccount = null;

        try {
            bankAccount = accountDao.findById(accountNumber);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (bankAccount != null) {
            if (PasswordUtils.verifyUserPassword(accountPassword, bankAccount.getAccountPassword(), bankAccount.getAccountSalt())) {
                return bankAccount;
            }
        }
        return null;
    }

    @Override
    public BankAccount getByNumber(String cardNumber) {
        BankAccount bankAccount = null;

        try {
            bankAccount = accountDao.findById(cardNumber);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bankAccount;
    }


    @Override
    public List<BankAccount> getByClient(Integer clientId) {
        //TODO: Eddit Dao
        return null;
    }

    @Override
    public void updateBankAccount(BankAccount bankAccount) {
        accountDao.update(bankAccount);
    }


}
