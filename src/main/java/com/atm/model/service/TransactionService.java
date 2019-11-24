package com.atm.model.service;

import com.atm.model.dto.ATM;
import com.atm.model.dto.account.BankAccount;
import com.atm.model.dto.transaction.Transaction;

import java.util.List;

public interface TransactionService {

    Transaction getTransactionById(Integer transactionId);

    List<Transaction> getTransactionsByAccountFrom(Integer transactionIdFrom);

    List<Transaction> getTransactionByAccountTo(Integer transactionIdTo);

    Integer createTransaction(String transactionType, Long transactionAmount, Boolean transactionStatus,
                              Long transactionCommission, BankAccount from, BankAccount to, ATM atm, String dateTime);

    void updateTransaction(Transaction transaction);
}
