package org.acardona.java.taller.repository.jdbc;

import org.acardona.java.taller.Repositori.Repository;
import org.acardona.java.taller.domain.Mechanic;
import org.acardona.java.taller.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class JdbcMechanicRepository implements Repository<Mechanic> {
    @Override
    public Mechanic save(Mechanic mechanic) {
        try(Connection conn = DatabaseConnection.getInstance()){
            String sql="INSERT INTO clients (id,name,email,weekly_payment) VALUES (?,?,?,?)" +
                    "ON DUPLICATE KEY UPDATE name = VALUES (name),email=VALUES (email),weekly_payment=VALUES(weekly_payment)";
            PreparedStatement stmt=conn.prepareStatement(sql);
            stmt.setString(1,client.getId());
            stmt.setString(2,client.getName());
            stmt.setString(3,client.getEmail());
            stmt.setString(4,client.getPhone());
            stmt.setString(5, client.getIdentification());
            stmt.executeUpdate();
            return client;

        } catch (SQLException e) {
            throw new RuntimeException("Error saving client",e);
        }
    }

    @Override
    public Optional<Mechanic> findById(String id) {
        return Optional.empty();
    }

    @Override
    public List<Mechanic> findAll() {
        return List.of();
    }

    @Override
    public void delete(String id) {

    }
}
