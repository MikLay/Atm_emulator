package com.atm.model.service;

import com.atm.model.dto.account.BankAccount;
import com.atm.model.dto.account.service.Surplie;

import java.util.List;

public interface SurplierService {
    Surplie getById(Integer surplierId);

    List<Surplie> getByAccount(Integer accountId);

    void createSurplie(BankAccount to, BankAccount from, Long amount);

    void updateSurplier(Integer id, BankAccount to, BankAccount from, Long amount);

    void deleteSurplier(Integer id, BankAccount to, BankAccount from, Long amount);
}

