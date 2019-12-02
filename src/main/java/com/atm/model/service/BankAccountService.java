package com.atm.model.service;

import com.atm.model.dto.account.BankAccount;

import java.util.List;

public interface BankAccountService {
    BankAccount authBankAccount(String accountNumber, String accountPassword);

    BankAccount getByNumber(String cardNumber);

    List<BankAccount> getByClient(Integer clientId);

    void updateBankAccount(BankAccount bankAccount);
}
