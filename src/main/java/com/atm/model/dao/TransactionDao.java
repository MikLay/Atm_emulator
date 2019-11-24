package com.atm.model.dao;

import com.atm.model.dto.transaction.Transaction;

import java.util.List;

public interface TransactionDao extends Dao<Transaction, Integer> {
    List<Transaction> findByAccountReceiver(Integer accountNumber);

    List<Transaction> findByAccountSender(Integer accountSender);

    Integer createWithoutId(Transaction transaction);
}
