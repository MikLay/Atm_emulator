package com.atm.model.dao.impl;

import com.atm.model.HibernateUtil;
import com.atm.model.dao.ATMDao;
import com.atm.model.dto.ATM;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;
import java.util.Properties;

public class ATMDaoImpl implements ATMDao {
    private Properties properties;
    private SessionFactory sessionFactory;

    public ATMDaoImpl(Properties properties) {
        this.properties = properties;
        this.sessionFactory = HibernateUtil.getSessionFactory();
    }


    @Override
    public void create(ATM atm) {
        Session session = this.sessionFactory.getCurrentSession();
        session.save(atm);
    }

    @Override
    public ATM findById(Integer entityId) {
        Session session = this.sessionFactory.getCurrentSession();
        ATM atm = session.get(ATM.class, entityId);
        return atm;
    }

    @Override
    public void update(ATM atm) {
        Session session = this.sessionFactory.getCurrentSession();
        session.update(atm);
    }

    @Override
    public void delete(ATM atm) {
        Session session = this.sessionFactory.getCurrentSession();
        session.delete(atm);
    }

    @Override
    public List<ATM> findAll() {
        Session session = this.sessionFactory.getCurrentSession();
        List<ATM> atms = (List<ATM>) session.createQuery(properties.getProperty("findAll")).list();
        return atms;
    }
}
