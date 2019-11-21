package com.atm.model;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class QueriesManager {

    public static Properties getProperties(String entity) {

        String pathToProperties = "queries/" + entity + "/queries.properties";
        ClassLoader loader = Thread.currentThread().getContextClassLoader();

        Properties properties = new Properties();

        try (InputStream resourceStream = loader.getResourceAsStream(pathToProperties)) {
            properties.load(resourceStream);
        } catch (FileNotFoundException fileEx) {
            fileEx.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return properties;
    }
}
