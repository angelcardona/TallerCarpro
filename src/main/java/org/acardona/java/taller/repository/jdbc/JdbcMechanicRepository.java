package org.acardona.java.taller.repository.jdbc;

import org.acardona.java.taller.Repositori.Repository;
import org.acardona.java.taller.domain.Client;
import org.acardona.java.taller.domain.Mechanic;
import org.acardona.java.taller.util.DatabaseConnection;

import javax.xml.transform.Result;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcMechanicRepository implements Repository<Mechanic> {
    @Override
    public Mechanic save(Mechanic mechanic) {
        try(Connection conn = DatabaseConnection.getInstance()){
            String sql="INSERT INTO mechanics (id,name,phone,weekly_payment) VALUES (?,?,?,?)" +
                    "ON DUPLICATE KEY UPDATE name = VALUES (name),phone=VALUES (phone),weekly_payment=VALUES(weekly_payment)";
            PreparedStatement stmt=conn.prepareStatement(sql);
            stmt.setString(1,mechanic.getId());
            stmt.setString(2,mechanic.getName());
            stmt.setString(3,mechanic.getPhone());
            stmt.executeUpdate();
            return mechanic;

        } catch (SQLException e) {
            throw new RuntimeException("Error saving Mechanic",e);
        }
    }
    @Override
    public Optional<Mechanic> findById(String id) {
        try(Connection conn = DatabaseConnection.getInstance()){
            String sql = "SELECT * FROM mechanics WHERE id=?";
            PreparedStatement stmt=conn.prepareStatement(sql);
            stmt.setString(1,id);
            ResultSet rs=stmt.executeQuery();
            if(rs.next()){
                Mechanic mechanic = new Mechanic(
                        rs.getString("name"),
                        rs.getString("phone")
                );
                mechanic.setId(rs.getString("id"));
                return Optional.of(mechanic);
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Error Finding mechanic",e);
        }
    }

    @Override
    public List<Mechanic> findAll() {
        List<Mechanic> mechanics = new ArrayList<>();
        try(Connection conn = DatabaseConnection.getInstance()){
            String sql = "SELECT * FROM mechanics";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                Mechanic mechanic = new Mechanic(
                        rs.getString("name"),
                        rs.getString("phone")

                );
                mechanic.setId(rs.getString("id"));
                mechanics.add(mechanic);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding all mechanics",e);
        }
        return mechanics;
    }

    @Override
    public void delete(String id) {
        try(Connection conn=DatabaseConnection.getInstance()){
            String sql = "DELETE FROM mechanics WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1,"id");
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    public double calulateWeeklyPayment(String mechanicId, LocalDateTime startDate,LocalDateTime endDate){
        try(Connection conn= DatabaseConnection.getInstance()){
            String sql = "SELECT SUM (labor_cost * mechanic_labor_percentage / 100) as payment" +
                    "FROM repairs WHERE mechanic_id=? and start_date BETWEEEN ? AND ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1,mechanicId);
            stmt.setTimestamp(2, Timestamp.valueOf(startDate));
            stmt.setTimestamp(3,Timestamp.valueOf(endDate));
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                return rs.getDouble("payment");
            }
            return 0.0;


        } catch (SQLException e) {
            throw new RuntimeException("Error calculating weekly payment",e);
        }
    }
}
