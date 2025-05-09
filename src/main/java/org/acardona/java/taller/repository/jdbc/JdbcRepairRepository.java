package org.acardona.java.taller.repository.jdbc;

import org.acardona.java.taller.Repositori.Repository;
import org.acardona.java.taller.domain.*;
import org.acardona.java.taller.util.DatabaseConnection;

import java.sql.*;
import java.util.List;
import java.util.Optional;

public abstract class JdbcRepairRepository implements Repository {
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
        try(Connection conn = DatabaseConnection.getInstance()){
            String sql="INSERT INTO repairs(id,repair_type,description," +
                    "labor_cost,mechanic_labor_percentage,status,vehicle_id,mechanic_id,start_date)" +
                    "VALUES (?,?,?,?,?,?,?)" +
                    "ON DUPLICATE KEY UPDATE repair_type=VALUES(repair_type),description=VALUES(description),labor_cost=VALUES(labor_cost)," +
                    "mechanic_labor_percentage=VALUES(mechanic_labor_percentage),status=VALUES(status),vehicle_id=VALUES(vehicle_id)," +
                    "mechanic_id=VALUES(mechanic_id),start_date=VALUES(start_date)";
            PreparedStatement stmt=conn.prepareStatement(sql);
            stmt.setString(1,repair.getId());
            stmt.setString(2, repair.getRepairType());
            stmt.setString(3, repair.getDescription());
            stmt.setDouble(5, repair.getLaborCost());
            stmt.setDouble(6, repair.getMechanic_labor_percentage());
            stmt.setString(7, repair.getStatus());
            stmt.setString(8, repair.getVehicle().getId());
            stmt.setString(9, repair.getMechanic().getId());
            stmt.setTimestamp(9, Timestamp.valueOf(repair.getStart_date()));
            stmt.executeUpdate();
            return repair;
            String deleteSql="DELETE FROM repair_spare_parts WHERE repair_id=?";
            PreparedStatement deleteStmt=conn.prepareStatement(deleteSql);
            deleteStmt.setString(1,repair.getId());
            deleteStmt.executeUpdate();

            String insertSql="INSERT INTO repair_spare_parts(repair_id,spare_part) VALUES(??)";
            PreparedStatement insertStmt=conn.prepareStatement(insertSql);
            for(SparePart sp :repair.getSpareParts()){
                insertStmt.setString(1,repair.getId());
                insertStmt.setString(2,sp.getId());
                insertStmt.executeUpdate();
            }
            return repair;

        } catch (SQLException e) {
            throw new RuntimeException("Error saving Repair",e);
        }

    }

    @Override
    public Optional <Repair>findById(String id) {
        try(Connection conn = DatabaseConnection.getInstance()){
            String sql = "SELECT r.*,v.id as vehicle_id,v.brand,v.model,v.year,v.license_plate,v.type" +
                    ",m.id as mechanic_id,n.name as mechanic_name,m.email" +
                    "FROM repairs r," +
                    "JOIN vehicles v ON r.vehicle_id=v.id" +
                    "JOIN mechanics m ON r.mechanic_id=m.id" +
                    "WHERE r.id= ?";
            PreparedStatement stmt=conn.prepareStatement(sql);
            stmt.setString(1,id);
            ResultSet rs=stmt.executeQuery();
            if(rs.next()){
                Optional<Vehicle> vehicle=vehicleRepository.findById(rs.getString(("Vehicle_id"))
                        .describeConstable().orElseThrow(() -> new RuntimeException("Vehicle not Found")));
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
                repair.setStart_date(rs.getTimestamp("start_date").toLocalDateTime());
                //Charging Spare

                String sparePartSql="SELECT sp.*,si.id as si.id,si.date,si.supplier_id," +
                        "FROM spare_parts sp" +
                        "JOIN repair_spare_parts rsp ON sp.id =rsp.spare_part_id," +
                        "LEFT JOIN supplier_invoices si ON sp.supplier_invoice_id=si.id" +
                        "WHERE rsp.repair_id=?";
                PreparedStatement sparePartsStmt= conn.prepareStatement(sparePartSql);
                sparePartsStmt.setString(1,id);
                ResultSet sparePartsRs=sparePartsStmt.executeQuery();
                while (sparePartsRs.next()){
                    SupplierInvoice supplierInvoice = null;
                    if (sparePartsRs.getString("si_id")!= null){
                        sparePartsRs.getTimestamp("date").toLocalDateTime(),
                        supplierInvoiceRepository.findById(sparePartsRs.getString("supplier_id")).
                                orElseThrow(() -> new RuntimeException("Supplier not found")),
                        sparePartsRs.getString("vehicle_id")!= null ?
                                supplierInvoiceRepository.findById(sparePartsRs.getString("vehicle_id"))
                                        .map(SupplierInvoice::getVehicle)
                                        .orElse(null) : null,
                        sparePartsRs.getDouble("total"),
                        sparePartsRs.getString("description")
                        );
                        SparePart sparePart=new SparePart(
                                rs.getString("name"),
                                rs.getString("description"),
                                rs.getDouble("profit_percentage"),
                                supplierInvoice
                        );
                        sparePart.setId((sparePartsRs.getString("id")));
                        repair.addSparePart(sparePart);
                }
                return  Optional.of(repair);




        }

        @Override
    public List findAll() {
        return List.of();
    }

    @Override
    public void delete(String id) {

    }
}
