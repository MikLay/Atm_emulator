package com.atm.model.service.impl;

import com.atm.model.dao.SurplieDao;
import com.atm.model.dto.account.BankAccount;
import com.atm.model.dto.account.service.Surplie;
import com.atm.model.service.SurplierService;

import java.util.List;

public class SurplierServiceImpl implements SurplierService {

    private SurplieDao surplieDao;

    public SurplierServiceImpl(SurplieDao surplieDao) {
        this.surplieDao = surplieDao;
    }

    @Override
    public Surplie getById(Integer surplierId) {
        return null;
    }

    @Override
    public List<Surplie> getByAccount(Integer accountId) {
        return null;
    }

    @Override
    public void createSurplie(BankAccount to, BankAccount from, Long amount) {
        surplieDao.create(Surplie.builder().from(from).to(to).surplieLimit(amount).build());
    }

    @Override
    public void updateSurplier(Integer id, BankAccount to, BankAccount from, Long amount) {
        surplieDao.create(Surplie.builder().surplierId(id).from(from).to(to).surplieLimit(amount).build());
    }

    @Override
    public void deleteSurplier(Integer id, BankAccount to, BankAccount from, Long amount) {
        surplieDao.delete(Surplie.builder().surplierId(id).from(from).to(to).surplieLimit(amount).build());
    }
}
