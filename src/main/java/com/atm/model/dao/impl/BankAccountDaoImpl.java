package com.atm.model.dao.impl;

import com.atm.model.HibernateUtil;
import com.atm.model.dao.BankAccountDao;
import com.atm.model.dto.account.BankAccount;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;
import java.util.Properties;

public class BankAccountDaoImpl implements BankAccountDao {
    private Properties properties;
    private SessionFactory sessionFactory;

    public BankAccountDaoImpl(Properties properties) {
        this.properties = properties;
        this.sessionFactory = HibernateUtil.getSessionFactory();
    }

    @Override
    public void create(BankAccount bankAccount) {
        Session session = this.sessionFactory.getCurrentSession();
        session.save(bankAccount);
    }

    @Override
    public BankAccount findById(String entityId) {
        Session session = this.sessionFactory.getCurrentSession();
        return session.get(BankAccount.class, entityId);
    }

    @Override
    public void update(BankAccount bankAccount) {
        Session session = this.sessionFactory.getCurrentSession();
        session.update(bankAccount);
    }

    @Override
    public void delete(BankAccount bankAccount) {
        Session session = this.sessionFactory.getCurrentSession();
        session.delete(bankAccount);
    }

    @Override
    public List<BankAccount> findAll() {
        Session session = this.sessionFactory.getCurrentSession();
        List<BankAccount> bankAccounts = (List<BankAccount>) session.createQuery(properties.getProperty("findAll")).list();
        return bankAccounts;
    }
}
