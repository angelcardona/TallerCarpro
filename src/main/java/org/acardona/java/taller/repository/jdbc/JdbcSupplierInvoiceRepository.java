package org.acardona.java.taller.repository.jdbc;

import org.acardona.java.taller.Repositori.Repository;
import org.acardona.java.taller.domain.Client;
import org.acardona.java.taller.domain.Supplier;
import org.acardona.java.taller.domain.SupplierInvoice;
import org.acardona.java.taller.domain.Vehicle;
import org.acardona.java.taller.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcSupplierInvoiceRepository implements Repository <SupplierInvoice> {
    private final Repository<Supplier> supplierRepository;
    private final Repository<Vehicle> vehicleRepository;

    public JdbcSupplierInvoiceRepository(Repository<Supplier> supplierRepository, Repository<Vehicle> vehicleRepository) {
        this.supplierRepository = supplierRepository;
        this.vehicleRepository = vehicleRepository;
    }


    @Override
    public SupplierInvoice save(SupplierInvoice supplierInvoice) {
        try(Connection conn = DatabaseConnection.getInstance()){
            String sql="INSERT INTO supplier_invoices (id,date,supplier_id,vehicle_id,total,description) VALUES (?,?,?,?,?,?)" +
                    "ON DUPLICATE KEY UPDATE name = VALUES (name),email=VALUES (email),phone=VALUES (phone),identification=VALUES (identification)";
            PreparedStatement stmt=conn.prepareStatement(sql);
            stmt.setString(1,supplierInvoice.getId());
            stmt.setTimestamp(2,Timestamp.valueOf(supplierInvoice.getDate()));
            stmt.setString(3,supplierInvoice.getSupplier().getId());
            stmt.setString(4,supplierInvoice.getVehicle().getId() != null ? supplierInvoice.getVehicle().getId() : null);
            stmt.setDouble(5, supplierInvoice.getTotal());
            stmt.setString(6,supplierInvoice.getDescription());
            stmt.setBoolean(7, supplierInvoice.isPaid());
            stmt.executeUpdate();
            return supplierInvoice;

        } catch (SQLException e) {
            throw new RuntimeException("Error saving supplierInvoice",e);
        }
    }

    @Override
    public Optional<SupplierInvoice> findById(String id) {
        try (Connection conn = DatabaseConnection.getInstance()) {
            String sql = "SELECT si.*, s.id as supplier_id, s.name as supplier_name, s.contact, s.email, " +
                    "v.id as vehicle_id, v.brand, v.model, v.year, v.license_plate, v.type " +
                    "FROM supplier_invoices si " +
                    "JOIN suppliers s ON si.supplier_id = s.id " +
                    "LEFT JOIN vehicles v ON si.vehicle_id = v.id WHERE si.id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Supplier supplier = new Supplier(
                        rs.getString("supplier_name"),
                        rs.getString("contact"),
                        rs.getString("email")
                );
                supplier.setId(rs.getString("supplier_id"));
                Vehicle vehicle = null;
                if (rs.getString("vehicle_id") != null) {
                    vehicle = new Vehicle(
                            rs.getString("brand"),
                            rs.getString("model"),
                            rs.getInt("year"),
                            rs.getString("license_plate"),
                            rs.getString("type"),
                            null
                    );
                    vehicle.setId(rs.getString("vehicle_id"));
                }
                SupplierInvoice supplierInvoice = new SupplierInvoice(
                        rs.getTimestamp("date").toLocalDateTime(),
                        supplier,
                        vehicle,
                        rs.getDouble("total"),
                        rs.getString("description")
                );
                supplierInvoice.setId(rs.getString("id"));
                supplierInvoice.setPaid(rs.getBoolean("paid"));
                return Optional.of(supplierInvoice);
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Error finding supplier invoice", e);
        }
    }

    @Override
    public List<SupplierInvoice> findAll() {
        List<SupplierInvoice> supplierInvoices = new ArrayList<>();
        try(Connection conn = DatabaseConnection.getInstance()){
            String sql = "SELECT si.*,s.id as supplier_id,s.name as supplier_name" +
                    "s.contact,s.email," +
                    "v.id a vehicle_id,v.brand,v.model,v.year,v.license_plate,v.type" +
                    "FROM supplier_invoices s ON si.supplier_id=s.d" +
                    "LEFT JOIN vehicles v ON si.vehicle_id=vid";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                Supplier supplier = new Supplier(
                        rs.getString("supplier_name"),
                        rs.getString("contact"),
                        rs.getString("email")
                );
                supplier.setId(rs.getString("supplier_id"));
                Vehicle vehicle = null;
                if(rs.getString("vehicle_id") !=null){
                    vehicle= new Vehicle(
                        rs.getString("brand"),
                        rs.getString("model"),
                        rs.getInt("year"),
                        rs.getString("license_plate"),
                        rs.getString("type"),
                        null
                    );
                    vehicle.setId(rs.getString("vehicle_id"));
                }
                SupplierInvoice supplierInvoice=new SupplierInvoice(
                        rs.getTimestamp("date").toLocalDateTime(),
                        supplier,
                        vehicle,
                        rs.getDouble("total"),
                        rs.getString("description")
                );
                supplierInvoice.setId("id");
                supplierInvoice.setPaid(rs.getBoolean("paid"));
                supplierInvoices.add(supplierInvoice);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding all supplier invoices",e);
        }
        return supplierInvoices;
    }

    @Override
    public void delete(String id) {
        try(Connection conn=DatabaseConnection.getInstance()){
            String sql = "DELETE FROM supplier_invoices WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1,"id");
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting supplier invoice",e);
        }

    }
}
