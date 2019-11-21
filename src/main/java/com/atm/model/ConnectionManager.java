package com.atm.model;

import org.apache.log4j.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionManager {
    private static final Logger LOGGER = Logger.getLogger(ConnectionManager.class.getSimpleName());

    private static Connection connection = null;
    private static DataSource dataSource = null;

    public static Connection getConnection() {
        LOGGER.info("Method getConnection start");
        try {
            dataSource = (DataSource) new InitialContext().lookup("jdbc/gymnetwork");
            connection = dataSource.getConnection();
        } catch (NamingException e) {
            LOGGER.error("getConnection caught NamingException");
            LOGGER.trace(e);
            e.printStackTrace();
        } catch (SQLException sqlE) {
            LOGGER.error("getConnection caught SQLException");
            LOGGER.trace(sqlE);
            sqlE.printStackTrace();
        }

        LOGGER.info("getConnection end; return: " + connection);
        return connection;
    }

    public static DataSource getModelConnection() {
        LOGGER.info("getModelConnection start");

        try {
            Context context = (Context) new InitialContext().lookup("java::comp/env");
            dataSource = (DataSource) context.lookup("jdbc/gymnetwork");
        } catch (NamingException e) {
            LOGGER.error("getModelConnection caught NamingException");
            LOGGER.trace(e);
            e.printStackTrace();
        }

        LOGGER.info("modelConnection end; return: " + dataSource);
        return dataSource;
    }


    public static void closeConnection() {
        LOGGER.info("closeConnection start");
        if (connection != null) {
            LOGGER.info("connection not null");
            try {
                connection.close();
            } catch (SQLException sqlE) {
                LOGGER.error("closeConnection caught SQLException ");
                LOGGER.trace(sqlE);
                sqlE.printStackTrace();
            }
        }
    }
}
