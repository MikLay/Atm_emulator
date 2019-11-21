package com.atm.model.dao.impl;

import com.atm.model.HibernateUtil;
import com.atm.model.dao.ClientDao;
import com.atm.model.dto.Client;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;
import java.util.Properties;

public class ClientDaoImpl implements ClientDao {
    private Properties properties;
    private SessionFactory sessionFactory;

    public ClientDaoImpl(Properties properties) {
        this.properties = properties;
        this.sessionFactory = HibernateUtil.getSessionFactory();
    }

    @Override
    public Client findByAccount(Integer accountNumber) {
        Session session = this.sessionFactory.getCurrentSession();
        Client client = (Client) session.createSQLQuery(properties.getProperty("findByAccount"))
                .setParameter("accountNumber", accountNumber).addEntity(Client.class);
        return client;
    }

    @Override
    public void create(Client client) {
        Session session = this.sessionFactory.getCurrentSession();
        session.save(client);
    }

    @Override
    public Client findById(Integer entityId) {
        Session session = this.sessionFactory.getCurrentSession();
        Client client = session.get(Client.class, entityId);
        return client;
    }

    @Override
    public void update(Client client) {
        Session session = this.sessionFactory.getCurrentSession();
        session.update(client);
    }

    @Override
    public void delete(Client client) {
        Session session = this.sessionFactory.getCurrentSession();
        session.delete(client);

    }

    @Override
    public List<Client> findAll() {
        Session session = this.sessionFactory.getCurrentSession();
        List<Client> clients = (List<Client>) session.createQuery(properties.getProperty("findAll")).list();
        return clients;
    }
}
