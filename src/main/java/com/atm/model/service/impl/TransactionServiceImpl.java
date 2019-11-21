package com.atm.model.service.impl;

import com.atm.model.dto.ATM;
import com.atm.model.dto.account.BankAccount;
import com.atm.model.dto.transaction.Transaction;
import com.atm.model.service.TransactionService;

import java.util.List;

public class TransactionServiceImpl implements TransactionService {
    @Override
    public Transaction getTransactionById(Integer transactionId) {
        return null;
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
    public Integer createTransaction(String transactionType, Long transactionAmount, Boolean transactionStatus, Long transactionCommission, BankAccount from, BankAccount to, ATM atm) {
        return null;
    }
}
