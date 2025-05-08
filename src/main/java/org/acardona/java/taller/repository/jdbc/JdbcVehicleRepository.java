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
    @Override
    public Vehicle save(Vehicle vehicle) {
        try(Connection conn = DatabaseConnection.getInstance()){
            String sql="INSERT INTO vehicles (id,brand,model,year,license_plate,type,client_id) VALUES (?,?,?,?,?,?,?)" +
                    "ON DUPLICATE KEY UPDATE brand = VALUES (brand),model=VALUES (model),year=VALUES (year),license_plate=VALUES (license_plate)," +
                    "type = VALUES(type),client_id=VALUES(client_id)";
            PreparedStatement stmt=conn.prepareStatement(sql);
            stmt.setString(1,vehicle.getId());
            stmt.setString(2, vehicle.getBrand());
            stmt.setString(3, vehicle.getModel());
            stmt.setString(4, String.valueOf(vehicle.getYear()));
            stmt.setString(5, vehicle.getLicense_plate());
            stmt.setString(6, vehicle.getType());
            stmt.setString(7, vehicle.getClient().getId());
            stmt.executeUpdate();
            return vehicle;

        } catch (SQLException e) {
            throw new RuntimeException("Error saving vehicle",e);
        }
    }

    @Override
    public Optional<Vehicle> findById(String id) {
        try(Connection conn = DatabaseConnection.getInstance()){
            String sql = "SELECT v.*,c.id as client_id,c.name,c.email,c.phone,c.identification" +
                    "FROM vehicles v JOIN clients c ON v.client_id=c.id WHERE v.id = ?";
            PreparedStatement stmt=conn.prepareStatement(sql);
            stmt.setString(1,id);
            ResultSet rs=stmt.executeQuery();
            if(rs.next()){
                Client client = new Client(
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("identification")
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
    public List<Vehicle> findAll() {
        List<Vehicle> vehicles = new ArrayList<>();
        try(Connection conn = DatabaseConnection.getInstance()){
            String sql = "SELECT v.*,c.id as client_id,c.name,c.email,c.phone,c.identification" +
                    "FROM vehicles v JOIN clients c ON v.client_id=c.id";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                Client client = new Client(
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("identification")
                );
                client.setId(rs.getString("id"));
                Vehicle vehicle = new Vehicle(
                        rs.getString("brand"),
                        rs.getString("model"),
                        rs.getString("year"),
                        rs.getString("license_plate"),
                        rs.getString("type"),
                        client
                );
                vehicle.setId(rs.getString("id"));
                vehicles.add(vehicle);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding all vehicles",e);
        }
        return vehicles;
    }

    @Override
    public void delete(String id) {
        try(Connection conn=DatabaseConnection.getInstance()){
            String sql = "DELETE FROM vehicles WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1,"id");
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting vehicle",e);
        }

    }
    public  List<Vehicle> findByClientId(String clientId){
        List<Vehicle> vehicles = new ArrayList<>();
        try(Connection conn= DatabaseConnection.getInstance()){
            String sql = "SELECT v.*,c.id as client_id,c.name,c.email,c.phone,c.identification" +
                    "FROM vehicles v JOIN clients c ON v.client_id=c.id" +
                    "WHERE v.client_id =?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1,clientId);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                Client client = new Client(
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("identification")
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
                vehicles.add(vehicle);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return vehicles;
    }

    }

