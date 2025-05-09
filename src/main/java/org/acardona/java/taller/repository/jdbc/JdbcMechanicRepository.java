package org.acardona.java.taller.repository.jdbc;

import org.acardona.java.taller.Repositori.Repository;
import org.acardona.java.taller.domain.Mechanic;
import org.acardona.java.taller.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcMechanicRepository implements Repository<Mechanic> {

    @Override
    public Mechanic save(Mechanic mechanic) {
        if (mechanic == null || mechanic.getId() == null || mechanic.getName() == null) {
            throw new IllegalArgumentException("Mechanic and its required fields (id, name) cannot be null");
        }
        try (Connection conn = DatabaseConnection.getInstance()) {
            String sql = "INSERT INTO mechanics (id, name, phone, weekly_payment) VALUES (?, ?, ?, ?) " +
                    "ON DUPLICATE KEY UPDATE name = VALUES(name), phone = VALUES(phone), " +
                    "weekly_payment = VALUES(weekly_payment)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, mechanic.getId());
            stmt.setString(2, mechanic.getName());
            stmt.setString(3, mechanic.getPhone());
            stmt.setDouble(4, mechanic.getWeeklyPayment());
            stmt.executeUpdate();
            return mechanic;
        } catch (SQLException e) {
            throw new RuntimeException("Error saving mechanic: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Mechanic> findById(String id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        try (Connection conn = DatabaseConnection.getInstance()) {
            String sql = "SELECT * FROM mechanics WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Mechanic mechanic = new Mechanic(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getString("phone"),
                        rs.getDouble("weekly_payment")
                );
                return Optional.of(mechanic);
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Error finding mechanic: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Mechanic> findAll() {
        List<Mechanic> mechanics = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getInstance()) {
            String sql = "SELECT * FROM mechanics";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Mechanic mechanic = new Mechanic(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getString("phone"),
                        rs.getDouble("weekly_payment")
                );
                mechanics.add(mechanic);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding all mechanics: " + e.getMessage(), e);
        }
        return mechanics;
    }

    @Override
    public void delete(String id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        try (Connection conn = DatabaseConnection.getInstance()) {
            String sql = "DELETE FROM mechanics WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting mechanic: " + e.getMessage(), e);
        }
    }

    public double calculateWeeklyPayment(String mechanicId, LocalDateTime startDate, LocalDateTime endDate) {
        if (mechanicId == null || startDate == null || endDate == null) {
            throw new IllegalArgumentException("Mechanic ID, start date, and end date cannot be null");
        }
        try (Connection conn = DatabaseConnection.getInstance()) {
            String sql = "SELECT SUM(labor_cost * mechanic_labor_percentage / 100) as payment " +
                    "FROM repairs WHERE mechanic_id = ? AND start_date BETWEEN ? AND ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, mechanicId);
            stmt.setTimestamp(2, Timestamp.valueOf(startDate));
            stmt.setTimestamp(3, Timestamp.valueOf(endDate));
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("payment");
            }
            return 0.0;
        } catch (SQLException e) {
            throw new RuntimeException("Error calculating weekly payment: " + e.getMessage(), e);
        }
    }
}