package org.acardona.java.taller.repository.jdbc;

import org.acardona.java.taller.Repositori.Repository;
import org.acardona.java.taller.domain.Client;
import org.acardona.java.taller.domain.Invoice;
import org.acardona.java.taller.domain.PaymentMethod;
import org.acardona.java.taller.domain.Vehicle;
import org.acardona.java.taller.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcInvoiceRepository implements Repository<Invoice> {
    private final Repository<Client> clientRepository;
    private final Repository<Vehicle> vehicleRepository;

    public JdbcInvoiceRepository(Repository<Client> clientRepository, Repository<Vehicle> vehicleRepository) {
        this.clientRepository = clientRepository;
        this.vehicleRepository = vehicleRepository;
    }

    @Override
    public Invoice save(Invoice invoice) {
        if (invoice == null || invoice.getId() == null || invoice.getClient() == null || invoice.getVehicle() == null) {
            throw new IllegalArgumentException("Invoice, its ID, client, and vehicle cannot be null");
        }
        try (Connection conn = DatabaseConnection.getInstance()) {
            String sql = "INSERT INTO invoices (id, date, client_id, vehicle_id, labor_cost, spare_parts_cost, " +
                    "other_costs, total, is_vehicle_delivered, payment_method) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " +
                    "ON DUPLICATE KEY UPDATE date = VALUES(date), client_id = VALUES(client_id), " +
                    "vehicle_id = VALUES(vehicle_id), labor_cost = VALUES(labor_cost), " +
                    "spare_parts_cost = VALUES(spare_parts_cost), other_costs = VALUES(other_costs), " +
                    "total = VALUES(total), is_vehicle_delivered = VALUES(is_vehicle_delivered), " +
                    "payment_method = VALUES(payment_method)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, invoice.getId());
            stmt.setTimestamp(2, Timestamp.valueOf(invoice.getDate()));
            stmt.setString(3, invoice.getClient().getId());
            stmt.setString(4, invoice.getVehicle().getId());
            stmt.setDouble(5, invoice.getLaborCost());
            stmt.setDouble(6, invoice.getSparePartsCost());
            stmt.setDouble(7, invoice.getOtherCosts());
            stmt.setDouble(8, invoice.getTotal());
            stmt.setBoolean(9, invoice.isVehicleDelivered());
            stmt.setString(10, invoice.getPaymentMethod().name());
            stmt.executeUpdate();
            return invoice;
        } catch (SQLException e) {
            throw new RuntimeException("Error saving invoice: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Invoice> findById(String id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        try (Connection conn = DatabaseConnection.getInstance()) {
            String sql = "SELECT i.*, c.id as client_id, c.name, c.email, c.phone, c.identification, " +
                    "v.id as vehicle_id, v.brand, v.model, v.year, v.license_plate, v.type " +
                    "FROM invoices i " +
                    "JOIN clients c ON i.client_id = c.id " +
                    "JOIN vehicles v ON i.vehicle_id = v.id WHERE i.id = ?";
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
                        rs.getString("vehicle_id"),
                        rs.getString("brand"),
                        rs.getString("model"),
                        rs.getInt("year"),
                        rs.getString("license_plate"),
                        rs.getString("type"),
                        client
                );
                Invoice invoice = new Invoice(
                        rs.getString("id"),
                        rs.getTimestamp("date").toLocalDateTime(),
                        client,
                        vehicle,
                        rs.getDouble("labor_cost"),
                        rs.getDouble("spare_parts_cost"),
                        rs.getDouble("other_costs"),
                        rs.getDouble("total"),
                        rs.getBoolean("is_vehicle_delivered"),
                        PaymentMethod.valueOf(rs.getString("payment_method"))
                );
                return Optional.of(invoice);
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Error finding invoice: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Invoice> findAll() {
        List<Invoice> invoices = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getInstance()) {
            String sql = "SELECT i.*, c.id as client_id, c.name, c.email, c.phone, c.identification, " +
                    "v.id as vehicle_id, v.brand, v.model, v.year, v.license_plate, v.type " +
                    "FROM invoices i " +
                    "JOIN clients c ON i.client_id = c.id " +
                    "JOIN vehicles v ON i.vehicle_id = v.id";
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
                        rs.getString("vehicle_id"),
                        rs.getString("brand"),
                        rs.getString("model"),
                        rs.getInt("year"),
                        rs.getString("license_plate"),
                        rs.getString("type"),
                        client
                );
                Invoice invoice = new Invoice(
                        rs.getString("id"),
                        rs.getTimestamp("date").toLocalDateTime(),
                        client,
                        vehicle,
                        rs.getDouble("labor_cost"),
                        rs.getDouble("spare_parts_cost"),
                        rs.getDouble("other_costs"),
                        rs.getDouble("total"),
                        rs.getBoolean("is_vehicle_delivered"),
                        PaymentMethod.valueOf(rs.getString("payment_method"))
                );
                invoices.add(invoice);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding all invoices: " + e.getMessage(), e);
        }
        return invoices;
    }

    @Override
    public void delete(String id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        try (Connection conn = DatabaseConnection.getInstance()) {
            String sql = "DELETE FROM invoices WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting invoice: " + e.getMessage(), e);
        }
    }
}