package com.atm.model.service;

import com.atm.model.dto.account.BankAccount;
import com.atm.model.dto.account.service.AutoPayment;

import java.sql.Date;
import java.util.List;

public interface AutoPaymentService {
    AutoPayment getById(Integer autoPaymentId);

    List<AutoPayment> getBySender(Integer senderId);

    Integer createAutoPayment(Long autoPaymentAmount, Date autoPaymentStart, Date autoPaymentEnd, Integer autoPaymentFrequency, Boolean autoPaymentIsFixed, BankAccount paymentFrom, BankAccount paymentTo);

    void deleteAutoPayment(AutoPayment autoPayment);
}
