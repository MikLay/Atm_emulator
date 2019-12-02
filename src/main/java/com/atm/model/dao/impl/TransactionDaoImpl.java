package com.atm.model.dao.impl;

import com.atm.model.HibernateUtil;
import com.atm.model.dao.TransactionDao;
import com.atm.model.dto.account.service.Surplie;
import com.atm.model.dto.transaction.Transaction;
import lombok.extern.log4j.Log4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;
import java.util.Properties;

@Log4j
public class TransactionDaoImpl implements TransactionDao {
    private Properties properties;
    private SessionFactory sessionFactory;

    public TransactionDaoImpl(Properties properties) {
        this.properties = properties;
        this.sessionFactory = HibernateUtil.getSessionFactory();
    }

    @Override
    public List<Transaction> findByAccountReceiver(Integer accountNumber) {
        Session session = this.sessionFactory.getCurrentSession();
        List<Transaction> transactions = (List<Transaction>) session.createQuery(properties.getProperty("findByReceiver")).list();
        return transactions;
    }

    @Override
    public List<Transaction> findByAccountSender(Integer accountSender) {
        Session session = this.sessionFactory.getCurrentSession();
        List<Transaction> transactions = (List<Transaction>) session.createQuery(properties.getProperty("findBySender")).list();
        return transactions;
    }

    @Override
    public Integer createWithoutId(Transaction transaction) {
        log.info("[Info] Create start with parameters: transaction: " + transaction);
        System.out.println(transaction.toString());
        Session session = this.sessionFactory.getCurrentSession();
        return (Integer) session.save(transaction);
    }

    @Override
    public void create(Transaction transaction) {
        log.info("create start with parameters: transaction: " + transaction);
        Session session = this.sessionFactory.getCurrentSession();
        session.save(transaction);
        log.info("create end");
    }

    @Override
    public Transaction findById(Integer entityId) {
        log.info("findById start with parameters: transaction: " + entityId);
        Session session = this.sessionFactory.getCurrentSession();
        Transaction transaction = session.get(Transaction.class, entityId);
        log.info("findById end");
        return transaction;
    }

    @Override
    public void update(Transaction transaction) {
        Session session = this.sessionFactory.getCurrentSession();
        session.update(transaction);

    }

    @Override
    public void delete(Transaction transaction) {
        Session session = this.sessionFactory.getCurrentSession();
        session.delete(transaction);
    }

    @Override
    public List<Transaction> findAll() {
        Session session = this.sessionFactory.getCurrentSession();
        List<Transaction> transactions = (List<Transaction>) session.createQuery(properties.getProperty("findAll")).list();
        return transactions;
    }
}
