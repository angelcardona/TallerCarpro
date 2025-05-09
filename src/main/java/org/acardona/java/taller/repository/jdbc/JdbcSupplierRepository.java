package org.acardona.java.taller.repository.jdbc;

import org.acardona.java.taller.Repositori.Repository;
import org.acardona.java.taller.domain.Client;
import org.acardona.java.taller.domain.Supplier;
import org.acardona.java.taller.domain.Vehicle;
import org.acardona.java.taller.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public abstract class JdbcSupplierRepository implements Repository {
    @Override
    public Supplier save(Supplier supplier) {
        try(Connection conn = DatabaseConnection.getInstance()){
            String sql="INSERT INTO suppliers(id,name,contact,email)" +
                    "VALUES (?,?,?,?)" +
                    "ON DUPLICATE KEY UPDATE name=VALUES(name),contact=VALUES(contact),email=VALUES(contact)";
            PreparedStatement stmt=conn.prepareStatement(sql);
            stmt.setString(1,supplier.getId());
            stmt.setString(2, supplier.getName());
            stmt.setString(3, supplier.getEmail());
            stmt.setString(4, supplier.getContact());

            stmt.executeUpdate();
            return supplier;

        } catch (SQLException e) {
            throw new RuntimeException("Error saving Supplier",e);
        }
    }

    @Override
    public Optional <Supplier> findById(String id) {
        try(Connection conn = DatabaseConnection.getInstance()){
            String sql = "SELECT * FROM suppliers WHERE id = ?" ;
            PreparedStatement stmt=conn.prepareStatement(sql);
            stmt.setString(1,id);
            ResultSet rs=stmt.executeQuery();
            if(rs.next()){
                Client client = new Client(
                        rs.getString("name"),
                        rs.getString("contact"),
                        rs.getString("email")

                );
                client.setId(rs.getString("client_id"));
                Vehicle vehicle = new Vehicle(
                        rs.getString("brand"),
                        rs.getString("model"),
                        rs.getString("year"),
                        rs.getString("license_plate"),
                        rs.getString("type"),
                        client
                );
                vehicle.setId(rs.getString("id"));
                return Optional.of(vehicle);
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Error Finding vehicle",e);
        }

    }

    @Override
    public List findAll() {
        return List.of();
    }

    @Override
    public void delete(String id) {

    }
}
