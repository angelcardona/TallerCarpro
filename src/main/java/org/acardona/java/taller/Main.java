package org.acardona.java.taller;

import org.acardona.java.taller.util.DatabaseConnection;

import java.sql.*;

public class Main {
    public static void main(String[] args) {
        try (Connection conn = DatabaseConnection.getInstance();
             Statement stmt = conn.createStatement();
             ResultSet result = stmt.executeQuery("SELECT * FROM prueba")) {

            while (result.next()) {
                System.out.println(result.getString("nombre"));
                System.out.println("|");
                System.out.println(result.getString("ciudad"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
