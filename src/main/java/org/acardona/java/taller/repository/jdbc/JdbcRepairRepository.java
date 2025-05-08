package org.acardona.java.taller.repository.jdbc;

import org.acardona.java.taller.domain.*;
import org.acardona.java.taller.repositori.Repository;
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

public class JdbcRepairRepository implements Repository<Repair> {
    private final Repository<Vehicle> vehicleRepository;
    private final Repository<Mechanic> mechanicRepository;
    private final Repository<SparePart> sparePartRepository;
    private final Repository<SupplierInvoice> supplierInvoiceRepository;

    public JdbcRepairRepository(Repository<Vehicle> vehicleRepository, Repository<Mechanic> mechanicRepository,
                                Repository<SparePart> sparePartRepository, Repository<SupplierInvoice> supplierInvoiceRepository) {
        this.vehicleRepository = vehicleRepository;
        this.mechanicRepository = mechanicRepository;
        this.sparePartRepository = sparePartRepository;
        this.supplierInvoiceRepository = supplierInvoiceRepository;
    }

    @Override
    public Repair save(Repair repair) {
        try (Connection conn = DatabaseConnection.getInstance()) {
            // Guardar repair
            String sql = "INSERT INTO repairs (id, repair_type, description, labor_cost, mechanic_labor_percentage, " +
                    "status, vehicle_id, mechanic_id, start_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) " +
                    "ON DUPLICATE KEY UPDATE repair_type = VALUES(repair_type), description = VALUES(description), " +
                    "labor_cost = VALUES(labor_cost), mechanic_labor_percentage = VALUES(mechanic_labor_percentage), " +
                    "status = VALUES(status), vehicle_id = VALUES(vehicle_id), mechanic_id = VALUES(mechanic_id), " +
                    "start_date = VALUES(start_date)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, repair.getId());
            stmt.setString(2, repair.getRepairType());
            stmt.setString(3, repair.getDescription());
            stmt.setDouble(4, repair.getLaborCost());
            stmt.setDouble(5, repair.getMechanicLaborPercentage());
            stmt.setString(6, repair.getStatus());
            stmt.setString(7, repair.getVehicle().getId());
            stmt.setString(8, repair.getMechanic().getId());
            stmt.setTimestamp(9, Timestamp.valueOf(repair.getStartDate()));
            stmt.executeUpdate();

            // Actualizar repair_spare_parts
            String deleteSql = "DELETE FROM repair_spare_parts WHERE repair_id = ?";
            PreparedStatement deleteStmt = conn.prepareStatement(deleteSql);
            deleteStmt.setString(1, repair.getId());
            deleteStmt.executeUpdate();

            String insertSql = "INSERT INTO repair_spare_parts (repair_id, spare_part_id) VALUES (?, ?)";
            PreparedStatement insertStmt = conn.prepareStatement(insertSql);
            for (SparePart sp : repair.getSpareParts()) {
                insertStmt.setString(1, repair.getId());
                insertStmt.setString(2, sp.getId());
                insertStmt.executeUpdate();
            }

            return repair;
        } catch (SQLException e) {
            throw new RuntimeException("Error saving repair", e);
        }
    }

    @Override
    public Optional<Repair> findById(String id) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT r.*, v.id as vehicle_id, v.brand, v.model, v.year, v.license_plate, v.type, " +
                    "m.id as mechanic_id, m.name as mechanic_name, m.email " +
                    "FROM repairs r " +
                    "JOIN vehicles v ON r.vehicle_id = v.id " +
                    "JOIN mechanics m ON r.mechanic_id = m.id WHERE r.id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Vehicle vehicle = vehicleRepository.findById(rs.getString("vehicle_id"))
                        .orElseThrow(() -> new RuntimeException("Vehicle not found"));
                Mechanic mechanic = new Mechanic(
                        rs.getString("mechanic_name"),
                        rs.getString("email")
                );
                mechanic.setId(rs.getString("mechanic_id"));
                Repair repair = new Repair(
                        rs.getString("repair_type"),
                        rs.getString("description"),
                        rs.getDouble("labor_cost"),
                        rs.getDouble("mechanic_labor_percentage"),
                        rs.getString("status"),
                        vehicle,
                        mechanic
                );
                repair.setId(rs.getString("id"));
                repair.setStartDate(rs.getTimestamp("start_date").toLocalDateTime());

                // Cargar repuestos
                String sparePartsSql = "SELECT sp.*, si.id as si_id, si.date, si.supplier_id, si.vehicle_id, si.total, si.description, si.paid " +
                        "FROM spare_parts sp " +
                        "JOIN repair_spare_parts rsp ON sp.id = rsp.spare_part_id " +
                        "LEFT JOIN supplier_invoices si ON sp.supplier_invoice_id = si.id " +
                        "WHERE rsp.repair_id = ?";
                PreparedStatement sparePartsStmt = conn.prepareStatement(sparePartsSql);
                sparePartsStmt.setString(1, id);
                ResultSet sparePartsRs = sparePartsStmt.executeQuery();
                while (sparePartsRs.next()) {
                    SupplierInvoice supplierInvoice = null;
                    if (sparePartsRs.getString("si_id") != null) {
                        supplierInvoice = new SupplierInvoice(
                                sparePartsRs.getTimestamp("date").toLocalDateTime(),
                                supplierInvoiceRepository.findById(sparePartsRs.getString("supplier_id"))
                                        .orElseThrow(() -> new RuntimeException("Supplier not found")),
                                sparePartsRs.getString("vehicle_id") != null ?
                                        supplierInvoiceRepository.findById(sparePartsRs.getString("vehicle_id"))
                                                .map(si -> si.getVehicle())
                                                .orElse(null) : null,
                                sparePartsRs.getDouble("total"),
                                sparePartsRs.getString("description")
                        );
                        supplierInvoice.setId(sparePartsRs.getString("si_id"));
                        supplierInvoice.setPaid(sparePartsRs.getBoolean("paid"));
                    }
                    SparePart sparePart = new SparePart(
                            sparePartsRs.getString("name"),
                            sparePartsRs.getString("description"),
                            sparePartsRs.getDouble("cost"),
                            sparePartsRs.getDouble("profit_percentage"),
                            supplierInvoice
                    );
                    sparePart.setId(sparePartsRs.getString("id"));
                    repair.addSparePart(sparePart);
                }
                return Optional.of(repair);
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Error finding repair", e);
        }
    }

    @Override
    public List<Repair> findAll() {
        List<Repair> repairs = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT r.*, v.id as vehicle_id, v.brand, v.model, v.year, v.license_plate, v.type, " +
                    "m.id as mechanic_id, m.name as mechanic_name, m.email " +
                    "FROM repairs r " +
                    "JOIN vehicles v ON r.vehicle_id = v.id " +
                    "JOIN mechanics m ON r.mechanic_id = m.id";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Vehicle vehicle = vehicleRepository.findById(rs.getString("vehicle_id"))
                        .orElseThrow(() -> new RuntimeException("Vehicle not found"));
                Mechanic mechanic = new Mechanic(
                        rs.getString("mechanic_name"),
                        rs.getString("email")
                );
                mechanic.setId(rs.getString("mechanic_id"));
                Repair repair = new Repair(
                        rs.getString("repair_type"),
                        rs.getString("description"),
                        rs.getDouble("labor_cost"),
                        rs.getDouble("mechanic_labor_percentage"),
                        rs.getString("status"),
                        vehicle,
                        mechanic
                );
                repair.setId(rs.getString("id"));
                repair.setStartDate(rs.getTimestamp("start_date").toLocalDateTime());

                // Cargar repuestos
                String sparePartsSql = "SELECT sp.*, si.id as si_id, si.date, si.supplier_id, si.vehicle_id, si.total, si.description, si.paid " +
                        "FROM spare_parts sp " +
                        "JOIN repair_spare_parts rsp ON sp.id = rsp.spare_part_id " +
                        "LEFT JOIN supplier_invoices si ON sp.supplier_invoice_id = si.id " +
                        "WHERE rsp.repair_id = ?";
                PreparedStatement sparePartsStmt = conn.prepareStatement(sparePartsSql);
                sparePartsStmt.setString(1, repair.getId());
                ResultSet sparePartsRs = sparePartsStmt.executeQuery();
                while (sparePartsRs.next()) {
                    SupplierInvoice supplierInvoice = null;
                    if (sparePartsRs.getString("si_id") != null) {
                        supplierInvoice = new SupplierInvoice(
                                sparePartsRs.getTimestamp("date").toLocalDateTime(),
                                supplierInvoiceRepository.findById(sparePartsRs.getString("supplier_id"))
                                        .orElseThrow(() -> new RuntimeException("Supplier not found")),
                                sparePartsRs.getString("vehicle_id") != null ?
                                        supplierInvoiceRepository.findById(sparePartsRs.getString("vehicle_id"))
                                                .map(si -> si.getVehicle())
                                                .orElse(null) : null,
                                sparePartsRs.getDouble("total"),
                                sparePartsRs.getString("description")
                        );
                        supplierInvoice.setId(sparePartsRs.getString("si_id"));
                        supplierInvoice.setPaid(sparePartsRs.getBoolean("paid"));
                    }
                    SparePart sparePart = new SparePart(
                            sparePartsRs.getString("name"),
                            sparePartsRs.getString("description"),
                            sparePartsRs.getDouble("cost"),
                            sparePartsRs.getDouble("profit_percentage"),
                            supplierInvoice
                    );
                    sparePart.setId(sparePartsRs.getString("id"));
                    repair.addSparePart(sparePart);
                }
                repairs.add(repair);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding all repairs", e);
        }
        return repairs;
    }

    @Override
    public void delete(String id) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String deleteSparePartsSql = "DELETE FROM repair_spare_parts WHERE repair_id = ?";
            PreparedStatement deleteSparePartsStmt = conn.prepareStatement(deleteSparePartsSql);
            deleteSparePartsStmt.setString(1, id);
            deleteSparePartsStmt.executeUpdate();

            String deleteSql = "DELETE FROM repairs WHERE id = ?";
            PreparedStatement deleteStmt = conn.prepareStatement(deleteSql);
            deleteStmt.setString(1, id);
            deleteStmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting repair", e);
        }
    }
}
