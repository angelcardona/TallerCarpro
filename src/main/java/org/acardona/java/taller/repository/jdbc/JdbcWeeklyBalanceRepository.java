package org.acardona.java.taller.repository.jdbc;

import org.acardona.java.taller.Repositori.Repository;
import org.acardona.java.taller.domain.WeeklyBalance;
import org.acardona.java.taller.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class JdbcWeeklyBalanceRepository implements Repository {
    @Override
    public WeeklyBalance save(WeeklyBalance weeklyBalance) {
        try (Connection conn = DatabaseConnection.getInstance()) {
            String sql = "INSERT INTO weekly_balances (id, start_date, end_date, total_income, cash_income, " +
                    "card_income, transfer_income, total_expenses, total_spare_parts_profit, total_labor_profit, balance) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " +
                    "ON DUPLICATE KEY UPDATE start_date = VALUES(start_date), end_date = VALUES(end_date), " +
                    "total_income = VALUES(total_income), cash_income = VALUES(cash_income), " +
                    "card_income = VALUES(card_income), transfer_income = VALUES(transfer_income), " +
                    "total_expenses = VALUES(total_expenses), total_spare_parts_profit = VALUES(total_spare_parts_profit), " +
                    "total_labor_profit = VALUES(total_labor_profit), balance = VALUES(balance)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, weeklyBalance.getId());
            stmt.setTimestamp(2, Timestamp.valueOf(weeklyBalance.getStartDate()));
            stmt.setTimestamp(3, Timestamp.valueOf(weeklyBalance.getEndDate()));
            stmt.setDouble(4, weeklyBalance.getTotalIncome());
            stmt.setDouble(5, weeklyBalance.getCashIncome());
            stmt.setDouble(6, weeklyBalance.getCardIncome());
            stmt.setDouble(7, weeklyBalance.getTransferIncome());
            stmt.setDouble(8, weeklyBalance.getTotalExpenses());
            stmt.setDouble(9, weeklyBalance.getTotalSparePartsProfit());
            stmt.setDouble(10, weeklyBalance.getTotalLaborProfit());
            stmt.setDouble(11, weeklyBalance.getBalance());
            stmt.executeUpdate();
            return weeklyBalance;
        } catch (SQLException e) {
            throw new RuntimeException("Error saving weekly balance", e);
        }
    }

    @Override
    public Optional<WeeklyBalance> findById(String id) {
        try (Connection conn = DatabaseConnection.getInstance()) {
            String sql = "SELECT * FROM weekly_balances WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                WeeklyBalance weeklyBalance = new WeeklyBalance(
                        rs.getTimestamp("start_date").toLocalDateTime(),
                        rs.getTimestamp("end_date").toLocalDateTime(),
                        rs.getDouble("total_income"),
                        rs.getDouble("cash_income"),
                        rs.getDouble("card_income"),
                        rs.getDouble("transfer_income"),
                        rs.getDouble("total_expenses"),
                        rs.getDouble("total_spare_parts_profit"),
                        rs.getDouble("total_labor_profit")
                );
                weeklyBalance.setId(rs.getString("id"));
                weeklyBalance.setBalance(rs.getDouble("balance"));
                return Optional.of(weeklyBalance);
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Error finding weekly balance", e);
        }
    }

    @Override
    public List <WeeklyBalance>findAll() {
        List<WeeklyBalance> balances = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getInstance()) {
            String sql = "SELECT * FROM weekly_balances";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                WeeklyBalance weeklyBalance = new WeeklyBalance(
                        rs.getTimestamp("start_date").toLocalDateTime(),
                        rs.getTimestamp("end_date").toLocalDateTime(),
                        rs.getDouble("total_income"),
                        rs.getDouble("cash_income"),
                        rs.getDouble("card_income"),
                        rs.getDouble("transfer_income"),
                        rs.getDouble("total_expenses"),
                        rs.getDouble("total_spare_parts_profit"),
                        rs.getDouble("total_labor_profit")
                );
                weeklyBalance.setId(rs.getString("id"));
                weeklyBalance.setBalance(rs.getDouble("balance"));
                balances.add(weeklyBalance);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding all weekly balances", e);
        }
        return balances;
    }

    @Override
    public void delete(String id) {
        try (Connection conn = DatabaseConnection.getInstance()) {
            String sql = "DELETE FROM weekly_balances WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting weekly balance", e);
        }
    }

    }
}
