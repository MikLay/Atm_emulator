package com.atm.model.service.impl;

import com.atm.model.dao.TransactionDao;
import com.atm.model.dto.ATM;
import com.atm.model.dto.account.BankAccount;
import com.atm.model.dto.transaction.Transaction;
import com.atm.model.service.TransactionService;

import java.util.List;

public class TransactionServiceImpl implements TransactionService {

    private TransactionDao transactionDao;

    public TransactionServiceImpl(TransactionDao transactionDao) {
        this.transactionDao = transactionDao;
    }

    @Override
    public Transaction getTransactionById(Integer transactionId) {
        return transactionDao.findById(transactionId);
    }

    @Override
    public List<Transaction> getTransactionsByAccountFrom(Integer transactionIdFrom) {
        return null;
    }

    @Override
    public List<Transaction> getTransactionByAccountTo(Integer transactionIdTo) {
        return null;
    }

    @Override
    public Integer createTransaction(String transactionType, Long transactionAmount, Boolean transactionStatus, Long transactionCommission, BankAccount from, BankAccount to, ATM atm, String dateTime) {
        return transactionDao.createWithoutId(Transaction.builder().transactionAmount(transactionAmount).transactionCommission(transactionCommission).transactionStatus(transactionStatus).transactionType(transactionType).bankAccountFrom(from).bankAccountTo(to).transactionDateTime(dateTime).build());
    }

    @Override
    public void updateTransaction(Transaction transaction) {
        transactionDao.update(transaction);
    }
}
