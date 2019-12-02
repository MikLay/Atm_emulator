package com.atm.model.service.impl;

import com.atm.model.dao.ATMDao;
import com.atm.model.dto.ATM;
import com.atm.model.service.ATMService;

public class ATMServiceImpl implements ATMService {

    private ATMDao atmDao;

    public ATMServiceImpl(ATMDao atmDao) {
        this.atmDao = atmDao;
    }

    @Override
    public ATM getById(Integer clientId) {
        return atmDao.findById(clientId);
    }

    @Override
    public void updateATM(ATM atm) {
        atmDao.update(atm);
    }
}
