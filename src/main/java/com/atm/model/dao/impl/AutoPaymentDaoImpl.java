package com.atm.model.dao.impl;

import com.atm.model.HibernateUtil;
import com.atm.model.dao.AutoPaymentDao;
import com.atm.model.dto.account.BankAccount;
import com.atm.model.dto.account.service.AutoPayment;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;
import java.util.Properties;

public class AutoPaymentDaoImpl implements AutoPaymentDao {

    private Properties properties;
    private SessionFactory sessionFactory;

    public AutoPaymentDaoImpl(Properties properties) {
        this.properties = properties;
        this.sessionFactory = HibernateUtil.getSessionFactory();
    }

    @Override
    public List<AutoPayment> getBySender(Integer accountId) {
        Session session = this.sessionFactory.getCurrentSession();
        List<AutoPayment> autoPayments = (List<AutoPayment>) session.createQuery(properties.getProperty("getBySender")).list();
        return autoPayments;
    }

    @Override
    public void create(AutoPayment autoPayment) {
        Session session = this.sessionFactory.getCurrentSession();
        session.save(autoPayment);
    }

    @Override
    public AutoPayment findById(Integer entityId) {
        Session session = this.sessionFactory.getCurrentSession();
        AutoPayment autoPayment = session.get(AutoPayment.class, entityId);
        return autoPayment;
    }

    @Override
    public void update(AutoPayment autoPayment) {
        Session session = this.sessionFactory.getCurrentSession();
        session.update(autoPayment);
    }

    @Override
    public void delete(AutoPayment autoPayment) {
        Session session = this.sessionFactory.getCurrentSession();
        session.delete(autoPayment);
    }

    @Override
    public List<AutoPayment> findAll() {
        Session session = this.sessionFactory.getCurrentSession();
        List<AutoPayment> autoPayments = (List<AutoPayment>) session.createQuery(properties.getProperty("findAll")).list();
        return autoPayments;
    }
}
