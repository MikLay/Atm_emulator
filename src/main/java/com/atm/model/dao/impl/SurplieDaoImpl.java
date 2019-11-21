package com.atm.model.dao.impl;

import com.atm.model.HibernateUtil;
import com.atm.model.dao.SurplieDao;
import com.atm.model.dto.account.service.Surplie;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;
import java.util.Properties;

public class SurplieDaoImpl implements SurplieDao {

    private Properties properties;
    private SessionFactory sessionFactory;

    public SurplieDaoImpl(Properties properties) {
        this.properties = properties;
        this.sessionFactory = HibernateUtil.getSessionFactory();
    }


    @Override
    public List<Surplie> getBySender(Integer accountNumber) {
        Session session = this.sessionFactory.getCurrentSession();
        List<Surplie> surplies = (List<Surplie>) session.createQuery(properties.getProperty("findBySender")).list();
        return surplies;
    }

    @Override
    public void create(Surplie surplie) {
        Session session = this.sessionFactory.getCurrentSession();
        session.save(surplie);
    }

    @Override
    public Surplie findById(Integer entityId) {
        Session session = this.sessionFactory.getCurrentSession();
        Surplie surplie = session.get(Surplie.class, entityId);
        return surplie;
    }

    @Override
    public void update(Surplie surplie) {
        Session session = this.sessionFactory.getCurrentSession();
        session.update(surplie);

    }

    @Override
    public void delete(Surplie surplie) {
        Session session = this.sessionFactory.getCurrentSession();
        session.delete(surplie);
    }

    @Override
    public List<Surplie> findAll() {
        Session session = this.sessionFactory.getCurrentSession();
        List<Surplie> surplies = (List<Surplie>) session.createQuery(properties.getProperty("findAll")).list();
        return surplies;
    }
}
