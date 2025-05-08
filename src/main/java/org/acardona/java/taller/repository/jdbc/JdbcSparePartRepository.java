package org.acardona.java.taller.repository.jdbc;

import org.acardona.java.taller.Repositori.Repository;
import org.acardona.java.taller.domain.Client;
import org.acardona.java.taller.domain.SparePart;
import org.acardona.java.taller.domain.SupplierInvoice;
import org.acardona.java.taller.domain.Vehicle;
import org.acardona.java.taller.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcSparePartRepository implements Repository<SparePart> {
    private final Repository<SupplierInvoice> supplierInvoiceRepository;

    public JdbcSparePartRepository(Repository<SupplierInvoice> supplierInvoiceRepository) {
        this.supplierInvoiceRepository = supplierInvoiceRepository;
    }

    @Override
    public SparePart save(SparePart sparePart) {
        try(Connection conn = DatabaseConnection.getInstance()){
            String sql="INSERT INTO spare_parts (id,name,description,cost,profit_percentage,supplier_invoice_id) VALUES (?,?,?,?,?,?)" +
                    "ON DUPLICATE KEY UPDATE name = VALUES (name),description=VALUES (description),cost=VALUES (cost)," +
                    ",profit_percentage=VALUES (profit_percentage)" +
                    "supplier_invoice_id=VALUES(supplier_invoice_id)";
            PreparedStatement stmt=conn.prepareStatement(sql);
            stmt.setString(1,sparePart.getId());
            stmt.setString(2,sparePart.getName());
            stmt.setString(3,sparePart.getDescription());
            stmt.setDouble(4,sparePart.getCost());
            stmt.setDouble(5, sparePart.getProfitPercentage());
            stmt.setString(6,sparePart.getSupplierInvoice() !=null ? sparePart.getSupplierInvoice().getId(): null);
            stmt.executeUpdate();
            return sparePart;

        } catch (SQLException e) {
            throw new RuntimeException("Error saving Spare Part",e);
        }
    }

    @Override
    public Optional<SparePart> findById(String id) {
        try(Connection conn = DatabaseConnection.getInstance()){
            String sql = "SELECT sp.*,si.id as supplier_id,si.date,si.supplier_id,si.vehicle_id" +
                    ",si.total,si.description,si.paid" +
                    "FROM spare_parts sp," +
                    "LEFT JOIN supplier_invoices si ON sp.supplier_invoice_id=si.id" +
                    "WHERE sp.id= ?";
            PreparedStatement stmt=conn.prepareStatement(sql);
            stmt.setString(1,id);
            ResultSet rs=stmt.executeQuery();
            if(rs.next()){
                SupplierInvoice supplierInvoice=null;
                if(rs.getString("si_id") != null){
                    supplierInvoice=new SupplierInvoice(
                            rs.getTimestamp("date").toLocalDateTime(),
                            supplierInvoiceRepository.findById(rs.getString("supplier_id"))
                                    .orElseThrow(()->new RuntimeException("supplier not found")).getSupplier(),
                            rs.getString("vehicle_id") != null?
                                    supplierInvoiceRepository.findById(rs.getString("vehicle_id"))
                                            .map(SupplierInvoice::getVehicle)
                                            .orElse(null):null,
                            rs.getDouble("total"),
                            rs.getString("description")
                    );
                    supplierInvoice.setId(rs.getString("si_id"));
                    supplierInvoice.setPaid(rs.getBoolean("paid"));
                }
                SparePart sparePart= new SparePart(
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getDouble("profit_percentage"),
                        supplierInvoice
                );
                sparePart.setId(rs.getString("id"));
                return Optional.of(sparePart);
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Error Finding client",e);
        }
    }

    @Override
    public List<SparePart> findAll() {
        List<SparePart> spareParts = new ArrayList<>();
        try(Connection conn = DatabaseConnection.getInstance()){
            String sql = "SELECT sp.*,si.id as supplier_id,si.date,si.supplier_id,si.vehicle_id" +
                    ",si.total,si.description,si.paid" +
                    "FROM spare_parts sp," +
                    "LEFT JOIN supplier_invoices si ON sp.supplier_invoice_id=si.id" +
                    "WHERE sp.id= ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                SupplierInvoice supplierInvoice=null;
                if(rs.getString("si_id") !=null){
                    supplierInvoice=new SupplierInvoice(
                            rs.getTimestamp("date").toLocalDateTime(),
                            supplierInvoiceRepository.findById(rs.getString("supplier_id"))
                                    .orElseThrow(()->new RuntimeException("supplier not found")).getSupplier(),
                            rs.getString("vehicle_id") != null?
                                    supplierInvoiceRepository.findById(rs.getString("vehicle_id"))
                                            .map(SupplierInvoice::getVehicle)
                                            .orElse(null):null,
                            rs.getDouble("total"),
                            rs.getString("description")
                    );
                    supplierInvoice.setId(rs.getString("si_id"));
                    supplierInvoice.setPaid(rs.getBoolean("paid"));
                }
                SparePart sparePart = new SparePart(
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getDouble("profit_percentage"),
                        supplierInvoice
                );
                sparePart.setId(rs.getString("id"));
                spareParts.add(sparePart);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding all vehicles",e);
        }
        return spareParts;
    }



    @Override
    public void delete(String id) {
        try(Connection conn=DatabaseConnection.getInstance()){
            String sql = "DELETE FROM spare_parts WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1,"id");
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting spare_parts",e);
        }

    }
}
