/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import context.DBContext;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.Appointment;
import model.Car;
import model.CarMaintenance;
import model.User;

/**
 *
 * @author phamp
 */
public class PaymentDAO extends DBContext {

    public List<Map<String, Object>> getAllPaymentTransactions(String search, String status) {
        List<Map<String, Object>> list = new ArrayList<>();
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT ");
            sql.append("    pt.TransactionID, ");
            sql.append("    pt.MaintenanceID, ");
            sql.append("    c.LicensePlate, ");
            sql.append("    uCustomer.FullName AS CustomerName, ");
            sql.append("    pt.Amount AS PaidAmount, ");
            sql.append("    pt.PaymentMethod, ");
            sql.append("    CONVERT(VARCHAR(19), pt.PaymentDate, 120) AS PaymentDate, ");
            sql.append("    pt.Status, ");
            sql.append("    pt.TransactionReference, ");
            sql.append("    pt.Notes, ");
            sql.append("    uStaff.FullName AS ProcessedBy ");
            sql.append("FROM PaymentTransactions pt ");
            sql.append("INNER JOIN CarMaintenance cm ON pt.MaintenanceID = cm.MaintenanceID ");
            sql.append("INNER JOIN Cars c ON cm.CarID = c.CarID ");
            sql.append("INNER JOIN Users uCustomer ON c.OwnerID = uCustomer.UserID ");
            sql.append("LEFT JOIN Users uStaff ON pt.ProcessedBy = uStaff.UserID ");
            sql.append("WHERE 1=1 ");

            List<Object> params = new ArrayList<>();

            // Filter theo trạng thái
            if (status != null && !status.trim().isEmpty()) {
                sql.append(" AND pt.Status = ? ");
                params.add(status.trim());
            }

            // Search theo biển số xe
            if (search != null && !search.trim().isEmpty()) {
                sql.append(" AND c.LicensePlate LIKE ? ");
                params.add("%" + search.trim() + "%");
            }

            sql.append("ORDER BY pt.PaymentDate DESC ");

            PreparedStatement stm = connection.prepareStatement(sql.toString());

            // Set parameters
            for (int i = 0; i < params.size(); i++) {
                stm.setObject(i + 1, params.get(i));
            }

            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                Map<String, Object> payment = new HashMap<>();
                payment.put("transactionId", rs.getInt("TransactionID"));
                payment.put("maintenanceId", rs.getInt("MaintenanceID"));
                payment.put("licensePlate", rs.getString("LicensePlate"));
                payment.put("customerName", rs.getString("CustomerName"));
                payment.put("paidAmount", rs.getBigDecimal("PaidAmount"));
                payment.put("paymentMethod", rs.getString("PaymentMethod"));
                payment.put("paymentDate", rs.getString("PaymentDate"));
                payment.put("status", rs.getString("Status"));
                // Xử lý null values
                String transactionRef = rs.getString("TransactionReference");
                payment.put("transactionReference", transactionRef != null ? transactionRef : "");
                String notes = rs.getString("Notes");
                payment.put("notes", notes != null ? notes : "");
                String processedBy = rs.getString("ProcessedBy");
                payment.put("processedBy", processedBy != null ? processedBy : "");

                list.add(payment);
            }

            rs.close();
            stm.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return list;
    }

    public Map<String, Object> getPaymentTransactionDetails(int transactionId) {
        Map<String, Object> payment = null;
        try {
            String sql = "SELECT "
                    + "    pt.TransactionID, "
                    + "    pt.MaintenanceID, "
                    + "    c.LicensePlate, "
                    + "    uCustomer.FullName AS CustomerName, "
                    + "    pt.Amount AS PaidAmount, "
                    + "    pt.PaymentMethod, "
                    + "    CONVERT(VARCHAR(19), pt.PaymentDate, 120) AS PaymentDate, "
                    + "    pt.Status, "
                    + "    pt.TransactionReference, "
                    + "    pt.Notes, "
                    + "    uStaff.FullName AS ProcessedBy "
                    + "FROM PaymentTransactions pt "
                    + "INNER JOIN CarMaintenance cm ON pt.MaintenanceID = cm.MaintenanceID "
                    + "INNER JOIN Cars c ON cm.CarID = c.CarID "
                    + "INNER JOIN Users uCustomer ON c.OwnerID = uCustomer.UserID "
                    + "LEFT JOIN Users uStaff ON pt.ProcessedBy = uStaff.UserID "
                    + "WHERE pt.TransactionID = ?";

            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, transactionId);

            ResultSet rs = stm.executeQuery();

            if (rs.next()) {
                payment = new HashMap<>();
                payment.put("transactionId", rs.getInt("TransactionID"));
                payment.put("maintenanceId", rs.getInt("MaintenanceID"));
                payment.put("licensePlate", rs.getString("LicensePlate"));
                payment.put("customerName", rs.getString("CustomerName"));
                payment.put("paidAmount", rs.getBigDecimal("PaidAmount"));
                payment.put("paymentMethod", rs.getString("PaymentMethod"));
                payment.put("paymentDate", rs.getString("PaymentDate"));
                payment.put("status", rs.getString("Status"));
                // Xử lý null values
                String transactionRef = rs.getString("TransactionReference");
                payment.put("transactionReference", transactionRef != null ? transactionRef : "");
                String notes = rs.getString("Notes");
                payment.put("notes", notes != null ? notes : "");
                String processedBy = rs.getString("ProcessedBy");
                payment.put("processedBy", processedBy != null ? processedBy : "");
            }

            rs.close();
            stm.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return payment;
    }

    public CarMaintenance getMaintenanceDetailForPayment(int maintenanceId) {
        String sql = """
                SELECT TOP 1
                    pt.TransactionID,
                    pt.PaymentMethod,
                    pt.ProcessedBy,
                    pt.Amount            AS PaidAmount,
                    pt.Status            AS PaymentStatus,
                    pt.PaymentDate,

                    cm.MaintenanceID,
                    cm.AppointmentID,
                    cm.MaintenanceDate,
                    cm.Odometer,
                    cm.Status,
                    cm.Notes,
                    cm.AssignedTechnicianID,
                    cm.CompletedDate,
                    cm.TotalAmount,
                    cm.DiscountAmount,
                    cm.FinalAmount,

                    u.UserID             AS CustomerID,
                    u.FullName           AS CustomerName,
                    u.Phone              AS CustomerPhone,
                    u.Email              AS CustomerEmail,

                    tech.UserID          AS TechnicianID,
                    tech.FullName        AS TechnicianName,
                    tech.Phone           AS TechnicianPhone,
                    tech.Email           AS TechnicianEmail,

                    c.CarID,
                    c.LicensePlate,
                    c.Brand,
                    c.Model,
                    c.Color
                FROM PaymentTransactions pt
                JOIN CarMaintenance cm ON pt.MaintenanceID = cm.MaintenanceID
                LEFT JOIN Appointments a ON cm.AppointmentID = a.AppointmentID
                LEFT JOIN Cars c ON cm.CarID = c.CarID
                LEFT JOIN Users u ON c.OwnerID = u.UserID
                LEFT JOIN Users tech ON cm.AssignedTechnicianID = tech.UserID
                WHERE cm.MaintenanceID = ?
                ORDER BY pt.PaymentDate DESC, pt.TransactionID DESC
                """;

        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, maintenanceId);
            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    CarMaintenance cm = new CarMaintenance();
                    cm.setMaintenanceId(rs.getInt("MaintenanceID"));
                    cm.setMaintenanceDate(rs.getString("MaintenanceDate"));
                    cm.setOdometer(rs.getInt("Odometer"));
                    cm.setStatus(rs.getString("Status"));
                    cm.setNotes(rs.getString("Notes"));
                    cm.setCompletedDate(rs.getString("CompletedDate"));

                    BigDecimal totalAmount = rs.getBigDecimal("TotalAmount");
                    BigDecimal discountAmount = rs.getBigDecimal("DiscountAmount");
                    BigDecimal finalAmount = rs.getBigDecimal("FinalAmount");
                    cm.setTotalAmount(totalAmount != null ? totalAmount : BigDecimal.ZERO);
                    cm.setDiscountAmount(discountAmount != null ? discountAmount : BigDecimal.ZERO);
                    cm.setFinalAmount(finalAmount != null ? finalAmount : BigDecimal.ZERO);

                    Appointment appointment = new Appointment();
                    appointment.setAppointmentId(rs.getInt("AppointmentID"));
                    cm.setAppointment(appointment);

                    Car car = new Car();
                    car.setCarId(rs.getInt("CarID"));
                    car.setLicensePlate(rs.getString("LicensePlate"));
                    car.setBrand(rs.getString("Brand"));
                    car.setModel(rs.getString("Model"));
                    car.setColor(rs.getString("Color"));
                    cm.setCar(car);

                    User owner = new User();
                    owner.setUserId(rs.getInt("CustomerID"));
                    owner.setFullName(rs.getString("CustomerName"));
                    owner.setPhone(rs.getString("CustomerPhone"));
                    owner.setEmail(rs.getString("CustomerEmail"));
                    car.setOwner(owner);

                    User technician = new User();
                    String techName = rs.getString("TechnicianName");
                    if (techName == null || techName.trim().isEmpty()) {
                        technician.setFullName("Chưa chọn");
                        technician.setPhone("-");
                        technician.setEmail("-");
                    } else {
                        technician.setUserId(rs.getInt("TechnicianID"));
                        technician.setFullName(techName);
                        technician.setPhone(rs.getString("TechnicianPhone"));
                        technician.setEmail(rs.getString("TechnicianEmail"));
                    }
                    cm.setAssignedTechnician(technician);

                    return cm;
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public List<Map<String, Object>> getMaintenanceProductsForPayment(int maintenanceId) {
        List<Map<String, Object>> list = new ArrayList<>();

        try {
            String packageHeaderSql = """
                    SELECT 
                        mp.PackageCode,
                        mp.Name       AS PackageName,
                        mp.BasePrice,
                        mp.DiscountPercent,
                        mp.FinalPrice
                    FROM MaintenancePackageUsage mpu
                    JOIN MaintenancePackage mp ON mpu.PackageID = mp.PackageID
                    WHERE mpu.MaintenanceID = ?
                    ORDER BY mp.PackageCode
                    """;
            try (PreparedStatement stm = connection.prepareStatement(packageHeaderSql)) {
                stm.setInt(1, maintenanceId);
                try (ResultSet rs = stm.executeQuery()) {
                    while (rs.next()) {
                        Map<String, Object> header = new HashMap<>();
                        header.put("packageCode", rs.getString("PackageCode"));
                        header.put("productName", rs.getString("PackageName"));
                        header.put("quantity", BigDecimal.ONE);
                        header.put("basePrice", rs.getDouble("BasePrice"));
                        header.put("discountPercent", rs.getDouble("DiscountPercent"));
                        header.put("finalPrice", rs.getDouble("FinalPrice"));
                        header.put("itemType", "Dịch vụ combo");
                        list.add(header);
                    }
                }
            }

            String packageItemsSql = """
                    SELECT 
                        mp.PackageCode,
                        p.Name        AS ProductName,
                        mpd.Quantity
                    FROM MaintenancePackageUsage mpu
                    JOIN MaintenancePackage mp      ON mpu.PackageID = mp.PackageID
                    JOIN MaintenancePackageDetail mpd ON mp.PackageID = mpd.PackageID
                    JOIN Product p ON mpd.ProductID = p.ProductID
                    WHERE mpu.MaintenanceID = ?
                    ORDER BY mp.PackageCode, mpd.DisplayOrder
                    """;
            try (PreparedStatement stm = connection.prepareStatement(packageItemsSql)) {
                stm.setInt(1, maintenanceId);
                try (ResultSet rs = stm.executeQuery()) {
                    while (rs.next()) {
                        Map<String, Object> item = new HashMap<>();
                        item.put("packageCode", rs.getString("PackageCode"));
                        item.put("productName", rs.getString("ProductName"));
                        item.put("quantity", rs.getBigDecimal("Quantity"));
                        item.put("basePrice", null);
                        item.put("discountPercent", null);
                        item.put("finalPrice", null);
                        item.put("itemType", "Dịch vụ combo");
                        list.add(item);
                    }
                }
            }

            String sqlService = """
                    SELECT 
                        p.Code,
                        p.Name AS ProductName,
                        sd.Quantity,
                        sd.UnitPrice AS BasePrice,
                        0 AS DiscountPercent,
                        sd.TotalPrice AS FinalPrice
                    FROM ServiceDetails sd
                    JOIN Product p ON sd.ProductID = p.ProductID
                    WHERE sd.MaintenanceID = ?
                      AND (sd.Notes IS NULL OR (sd.Notes NOT LIKE 'Từ gói %' AND sd.Notes NOT LIKE '%[ĐÃ XÓA]%'))
                """;
            try (PreparedStatement stm = connection.prepareStatement(sqlService)) {
                stm.setInt(1, maintenanceId);
                try (ResultSet rs = stm.executeQuery()) {
                    while (rs.next()) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("packageCode", rs.getString("Code"));
                        map.put("productName", rs.getString("ProductName"));
                        map.put("quantity", rs.getBigDecimal("Quantity"));
                        map.put("basePrice", rs.getDouble("BasePrice"));
                        map.put("discountPercent", rs.getDouble("DiscountPercent"));
                        map.put("finalPrice", rs.getDouble("FinalPrice"));
                        map.put("itemType", "Dịch vụ lẻ");
                        list.add(map);
                    }
                }
            }

            String sqlPart = """
                    SELECT 
                        p.Code,
                        p.Name AS ProductName,
                        spd.Quantity,
                        spd.UnitPrice AS BasePrice,
                        0 AS DiscountPercent,
                        spd.TotalPrice AS FinalPrice
                    FROM ServicePartDetails spd
                    JOIN Product p ON spd.ProductID = p.ProductID
                    WHERE spd.MaintenanceID = ?
                      AND (spd.Notes IS NULL OR (spd.Notes NOT LIKE 'Từ gói %' AND spd.Notes NOT LIKE '%[ĐÃ XÓA]%'))
                """;
            try (PreparedStatement stm = connection.prepareStatement(sqlPart)) {
                stm.setInt(1, maintenanceId);
                try (ResultSet rs = stm.executeQuery()) {
                    while (rs.next()) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("packageCode", rs.getString("Code"));
                        map.put("productName", rs.getString("ProductName"));
                        map.put("quantity", rs.getBigDecimal("Quantity"));
                        map.put("basePrice", rs.getDouble("BasePrice"));
                        map.put("discountPercent", rs.getDouble("DiscountPercent"));
                        map.put("finalPrice", rs.getDouble("FinalPrice"));
                        map.put("itemType", "Phụ tùng thay thế");
                        list.add(map);
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
}
