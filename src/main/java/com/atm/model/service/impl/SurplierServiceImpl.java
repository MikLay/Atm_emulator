package com.atm.model.service.impl;

import com.atm.model.dto.account.BankAccount;
import com.atm.model.dto.account.service.Surplie;
import com.atm.model.service.SurplierService;

import java.util.List;

public class SurplierServiceImpl implements SurplierService {
    @Override
    public Surplie getById(Integer surplierId) {
        return null;
    }

    @Override
    public List<Surplie> getByAccount(Integer accountId) {
        return null;
    }

    @Override
    public Integer createSurplie(BankAccount to, BankAccount from, Long amount) {
        return null;
    }
}
