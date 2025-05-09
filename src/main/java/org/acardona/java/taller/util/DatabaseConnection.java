package org.acardona.java.taller.util;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {
    private static final String URL;
    private static final String USERNAME;
    private static final String PASSWORD;
    private static volatile Connection connection;

    static {
        Properties props = new Properties();
        try {
            props.load(DatabaseConnection.class.getClassLoader().getResourceAsStream("application.properties"));
            URL = props.getProperty("db.url");
            USERNAME = props.getProperty("db.username");
            PASSWORD = props.getProperty("db.password");
        } catch (IOException e) {
            throw new RuntimeException("Error loading database properties: " + e.getMessage(), e);
        }
    }

    public static Connection getInstance() throws SQLException {
        if (connection == null || connection.isClosed()) {
            synchronized (DatabaseConnection.class) {
                if (connection == null || connection.isClosed()) {
                    connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                }
            }
        }
        return connection;
    }
}