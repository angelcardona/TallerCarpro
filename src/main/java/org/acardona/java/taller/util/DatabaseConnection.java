package org.acardona.java.taller.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static  String url= "jdbc:mysql://localhost:3306/tallercarpro?useSSL=false&serverTimezone=UTC";
    private static String username="root";
    private static String password="simple123";
    private static Connection connection;


    public static Connection getInstance() throws SQLException {
        if (connection==null){
            connection = DriverManager.getConnection(url,username,password);
        }
        return connection;
    }


}
