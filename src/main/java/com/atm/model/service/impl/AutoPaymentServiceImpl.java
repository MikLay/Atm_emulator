package com.atm.model.service.impl;

import com.atm.model.dto.account.BankAccount;
import com.atm.model.dto.account.service.AutoPayment;
import com.atm.model.service.AutoPaymentService;

import java.sql.Date;
import java.util.List;

public class AutoPaymentServiceImpl implements AutoPaymentService {
    @Override
    public AutoPayment getById(Integer autoPaymentId) {
        return null;
    }

    @Override
    public List<AutoPayment> getBySender(Integer senderId) {
        return null;
    }

    @Override
    public Integer createAutoPayment(Long autoPaymentAmount, Date autoPaymentStart, Date autoPaymentEnd, Integer autoPaymentFrequency, Boolean autoPaymentIsFixed, BankAccount paymentFrom, BankAccount paymentTo) {
        return null;
    }

    @Override
    public void deleteAutoPayment(AutoPayment autoPayment) {

    }
}
