package org.acardona.java.taller.repository.jdbc;

import org.acardona.java.taller.Repositori.Repository;
import org.acardona.java.taller.domain.Client;
import org.acardona.java.taller.domain.Vehicle;
import org.acardona.java.taller.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcVehicleRepository implements Repository<Vehicle> {

    private final Repository<Client> clientRepository;

    public JdbcVehicleRepository(Repository<Client> clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public Vehicle save(Vehicle vehicle) {
        if (vehicle == null || vehicle.getId() == null || vehicle.getClient() == null) {
            throw new IllegalArgumentException("Vehicle, its ID, and client cannot be null");
        }
        try (Connection conn = DatabaseConnection.getInstance()) {
            String sql = "INSERT INTO vehicles (id, brand, model, year, license_plate, type, client_id) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?) " +
                    "ON DUPLICATE KEY UPDATE brand = VALUES(brand), model = VALUES(model), year = VALUES(year), " +
                    "license_plate = VALUES(license_plate), type = VALUES(type), client_id = VALUES(client_id)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, vehicle.getId());
            stmt.setString(2, vehicle.getBrand());
            stmt.setString(3, vehicle.getModel());
            stmt.setInt(4, vehicle.getYear());
            stmt.setString(5, vehicle.getLicensePlate());
            stmt.setString(6, vehicle.getType());
            stmt.setString(7, vehicle.getClient().getId());
            stmt.executeUpdate();
            return vehicle;
        } catch (SQLException e) {
            throw new RuntimeException("Error saving vehicle: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Vehicle> findById(String id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        try (Connection conn = DatabaseConnection.getInstance()) {
            String sql = "SELECT v.*, c.id as client_id, c.name, c.email, c.phone, c.identification " +
                    "FROM vehicles v JOIN clients c ON v.client_id = c.id WHERE v.id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Client client = new Client(
                        rs.getString("client_id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("identification")
                );
                Vehicle vehicle = new Vehicle(
                        rs.getString("id"),
                        rs.getString("brand"),
                        rs.getString("model"),
                        rs.getInt("year"),
                        rs.getString("license_plate"),
                        rs.getString("type"),
                        client
                );
                return Optional.of(vehicle);
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Error finding vehicle: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Vehicle> findAll() {
        List<Vehicle> vehicles = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getInstance()) {
            String sql = "SELECT v.*, c.id as client_id, c.name, c.email, c.phone, c.identification " +
                    "FROM vehicles v JOIN clients c ON v.client_id = c.id";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Client client = new Client(
                        rs.getString("client_id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("identification")
                );
                Vehicle vehicle = new Vehicle(
                        rs.getString("id"),
                        rs.getString("brand"),
                        rs.getString("model"),
                        rs.getInt("year"),
                        rs.getString("license_plate"),
                        rs.getString("type"),
                        client
                );
                vehicles.add(vehicle);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding all vehicles: " + e.getMessage(), e);
        }
        return vehicles;
    }

    @Override
    public void delete(String id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        try (Connection conn = DatabaseConnection.getInstance()) {
            String sql = "DELETE FROM vehicles WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting vehicle: " + e.getMessage(), e);
        }
    }

    public List<Vehicle> findByClientId(String clientId) {
        if (clientId == null) {
            throw new IllegalArgumentException("Client ID cannot be null");
        }
        List<Vehicle> vehicles = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getInstance()) {
            String sql = "SELECT v.*, c.id as client_id, c.name, c.email, c.phone, c.identification " +
                    "FROM vehicles v JOIN clients c ON v.client_id = c.id WHERE v.client_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, clientId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Client client = new Client(
                        rs.getString("client_id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("identification")
                );
                Vehicle vehicle = new Vehicle(
                        rs.getString("id"),
                        rs.getString("brand"),
                        rs.getString("model"),
                        rs.getInt("year"),
                        rs.getString("license_plate"),
                        rs.getString("type"),
                        client
                );
                vehicles.add(vehicle);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding vehicles by client ID: " + e.getMessage(), e);
        }
        return vehicles;
    }
}