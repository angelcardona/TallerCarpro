package org.acardona.java.taller.repository.jdbc;

import org.acardona.java.taller.Repositori.Repository;
import org.acardona.java.taller.domain.Client;
import org.acardona.java.taller.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcClientRepository implements Repository<Client> {

    @Override
    public Client save(Client client) {
        if (client == null || client.getId() == null || client.getName() == null || client.getIdentification() == null) {
            throw new IllegalArgumentException("Client and its required fields (id, name, identification) cannot be null");
        }
        try (Connection conn = DatabaseConnection.getInstance()) {
            String sql = "INSERT INTO clients (id, name, email, phone, identification) VALUES (?, ?, ?, ?, ?) " +
                    "ON DUPLICATE KEY UPDATE name = VALUES(name), email = VALUES(email), " +
                    "phone = VALUES(phone), identification = VALUES(identification)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, client.getId());
            stmt.setString(2, client.getName());
            stmt.setString(3, client.getEmail());
            stmt.setString(4, client.getPhone());
            stmt.setString(5, client.getIdentification());
            stmt.executeUpdate();
            return client;
        } catch (SQLException e) {
            throw new RuntimeException("Error saving client: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Client> findById(String id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        try (Connection conn = DatabaseConnection.getInstance()) {
            String sql = "SELECT * FROM clients WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Client client = new Client(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("identification")
                );
                return Optional.of(client);
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Error finding client: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Client> findAll() {
        List<Client> clients = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getInstance()) {
            String sql = "SELECT * FROM clients";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Client client = new Client(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("identification")
                );
                clients.add(client);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding all clients: " + e.getMessage(), e);
        }
        return clients;
    }

    @Override
    public void delete(String id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        try (Connection conn = DatabaseConnection.getInstance()) {
            String sql = "DELETE FROM clients WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting client: " + e.getMessage(), e);
        }
    }
}
