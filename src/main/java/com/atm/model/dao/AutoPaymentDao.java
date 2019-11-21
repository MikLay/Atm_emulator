package com.atm.model.dao;

import com.atm.model.dto.account.service.AutoPayment;

import java.util.List;

public interface AutoPaymentDao extends Dao<AutoPayment, Integer> {
    List<AutoPayment> getBySender(Integer accountId);
}
