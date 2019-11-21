package com.atm.model;

import lombok.extern.log4j.Log4j;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Log4j
public class ConnectionManager {

    private static Connection connection = null;
    private static DataSource dataSource = null;

    public static Connection getConnection() {
        log.info("Method getConnection start");
        try {
            dataSource = (DataSource) new InitialContext().lookup("jdbc/gymnetwork");
            connection = dataSource.getConnection();
        } catch (NamingException e) {
            log.error("getConnection caught NamingException");
            log.trace(e);
            e.printStackTrace();
        } catch (SQLException sqlE) {
            log.error("getConnection caught SQLException");
            log.trace(sqlE);
            sqlE.printStackTrace();
        }

        log.info("getConnection end; return: " + connection);
        return connection;
    }

    public static DataSource getModelConnection() {
        log.info("getModelConnection start");

        try {
            Context context = (Context) new InitialContext().lookup("java::comp/env");
            dataSource = (DataSource) context.lookup("jdbc/gymnetwork");
        } catch (NamingException e) {
            log.error("getModelConnection caught NamingException");
            log.trace(e);
            e.printStackTrace();
        }

        log.info("modelConnection end; return: " + dataSource);
        return dataSource;
    }


    public static void closeConnection() {
        log.info("closeConnection start");
        if (connection != null) {
            log.info("connection not null");
            try {
                connection.close();
            } catch (SQLException sqlE) {
                log.error("closeConnection caught SQLException ");
                log.trace(sqlE);
                sqlE.printStackTrace();
            }
        }
    }
}
