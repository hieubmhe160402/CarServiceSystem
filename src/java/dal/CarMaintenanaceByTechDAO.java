/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import context.DBContext;
import java.sql.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Appointment;
import model.Car;
import model.CarMaintenance;
import model.User;

/**
 *
 * @author nxtru
 */
public class CarMaintenanaceByTechDAO extends DBContext {

    public List<Map<String, Object>> getCarMaintenanceByTechnicianId(int technicianId) {
        List<Map<String, Object>> list = new ArrayList<>();
        String sql = """
            SELECT 
                cm.MaintenanceID,
                CONCAT(c.Brand, ' ', c.Model, ' - ', c.LicensePlate) AS CarInfo,
                mp.PackageCode,
                mp.Name AS PackageName,
                cm.Odometer,
                cm.Status,
                cm.Notes,
                cm.CreatedDate
            FROM CarMaintenance cm
            JOIN Users u 
                ON cm.AssignedTechnicianID = u.UserID
            JOIN Cars c 
                ON cm.CarID = c.CarID
            LEFT JOIN Appointments a 
                ON cm.AppointmentID = a.AppointmentID
            LEFT JOIN MaintenancePackage mp 
                ON a.RequestedPackageID = mp.PackageID
            WHERE 
                cm.AssignedTechnicianID = ?
                AND u.IsActive = 1
            ORDER BY cm.CreatedDate DESC
        """;

        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, technicianId);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                Map<String, Object> map = new HashMap<>();
                map.put("maintenanceID", rs.getInt("MaintenanceID"));
                map.put("carInfo", rs.getString("CarInfo"));

                String packageCode = rs.getString("PackageCode");
                map.put("packageCode", packageCode != null ? packageCode : "-");

                String packageName = rs.getString("PackageName");
                map.put("packageName", packageName != null ? packageName : "-");

                map.put("odometer", rs.getInt("Odometer"));
                map.put("status", rs.getString("Status"));

                String notes = rs.getString("Notes");
                map.put("notes", notes != null ? notes : "-");

                Timestamp createdDate = rs.getTimestamp("CreatedDate");
                map.put("createdDate", createdDate != null ? createdDate.toString() : "-");

                list.add(map);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CarMaintenanaceByTechDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return list;
    }

    public boolean updateStatus(int maintenanceId, String newStatus) {
        String sql;
        if ("COMPLETED".equals(newStatus) || "CANCELLED".equals(newStatus)) {
            // Khi COMPLETED hoặc CANCELLED thì cập nhật cả CompletedDate
            sql = "UPDATE CarMaintenance SET Status = ?, CompletedDate = GETDATE() WHERE MaintenanceID = ?";
        } else {
            // Các status khác thì chỉ update status
            sql = "UPDATE CarMaintenance SET Status = ?, CompletedDate = GETDATE() WHERE MaintenanceID = ?";
        }

        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setString(1, newStatus);
            stm.setInt(2, maintenanceId);
            int rowsAffected = stm.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException ex) {
            Logger.getLogger(CarMaintenanaceByTechDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public CarMaintenance getDetailServiceMaintenanceById(int maintenanceId) {
        String sql = """
                SELECT 
                    m.MaintenanceID,
                    m.AppointmentID,
                    m.MaintenanceDate,
                    m.Odometer,
                    m.Status,
                    m.Notes,
                    m.AssignedTechnicianID,
                    m.CompletedDate,
                    m.FinalAmount,
                    m.CreatedBy,

                    -- Thông tin người tạo
                    creator.UserID AS CreatedByID,
                    creator.FullName AS CreatedByName,

                    -- Thông tin khách hàng (chủ xe)
                    u.UserID AS CustomerID,
                    u.FullName AS CustomerName,
                    u.Phone AS CustomerPhone,
                    u.Email AS CustomerEmail,

                    -- Thông tin kỹ thuật viên
                    tech.UserID AS TechnicianID,
                    tech.FullName AS TechnicianName,
                    tech.Phone AS TechnicianPhone,
                    tech.Email AS TechnicianEmail,

                    -- Thông tin xe
                    c.CarID,
                    c.LicensePlate,
                    c.Brand,
                    c.Model,
                    c.Color,
                    CONCAT(c.Brand, ' ', c.Model, ' - ', c.Color) AS CarInfo

                FROM CarMaintenance m
                LEFT JOIN Appointments a ON m.AppointmentID = a.AppointmentID
                LEFT JOIN Cars c ON a.CarID = c.CarID
                LEFT JOIN Users u ON c.OwnerID = u.UserID
                LEFT JOIN Users tech ON m.AssignedTechnicianID = tech.UserID
                LEFT JOIN Users creator ON m.CreatedBy = creator.UserID
                WHERE m.MaintenanceID = ?
            """;

        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, maintenanceId);
            ResultSet rs = stm.executeQuery();

            if (rs.next()) {
                CarMaintenance cm = new CarMaintenance();
                cm.setMaintenanceId(rs.getInt("MaintenanceID"));
                cm.setMaintenanceDate(rs.getString("MaintenanceDate"));
                cm.setOdometer(rs.getInt("Odometer"));
                cm.setStatus(rs.getString("Status"));
                cm.setFinalAmount(rs.getBigDecimal("FinalAmount"));
                cm.setNotes(rs.getString("Notes"));
                cm.setCompletedDate(rs.getString("CompletedDate"));

                // 🔹 Appointment
                Appointment ap = new Appointment();
                ap.setAppointmentId(rs.getInt("AppointmentID"));
                cm.setAppointment(ap);

                // 🔹 Car
                Car car = new Car();
                car.setCarId(rs.getInt("CarID"));
                car.setLicensePlate(rs.getString("LicensePlate"));
                car.setBrand(rs.getString("Brand"));
                car.setModel(rs.getString("Model"));
                car.setColor(rs.getString("Color"));
                cm.setCar(car);

                // 🔹 Owner (Customer)
                User owner = new User();
                owner.setUserId(rs.getInt("CustomerID"));
                owner.setFullName(rs.getString("CustomerName"));
                owner.setPhone(rs.getString("CustomerPhone"));
                owner.setEmail(rs.getString("CustomerEmail"));
                car.setOwner(owner);

                // 🔹 Technician (Assigned Technician)
                User tech = new User();
                String techName = rs.getString("TechnicianName");
                if (techName == null || techName.trim().isEmpty()) {
                    tech.setFullName("Chưa chọn");
                    tech.setPhone("-");
                    tech.setEmail("-");
                } else {
                    tech.setUserId(rs.getInt("TechnicianID"));
                    tech.setFullName(techName);
                    tech.setPhone(rs.getString("TechnicianPhone"));
                    tech.setEmail(rs.getString("TechnicianEmail"));
                }
                cm.setAssignedTechnician(tech);

                // 🔹 CreatedBy (Người tạo)
                User creator = new User();
                String creatorName = rs.getString("CreatedByName");
                if (creatorName == null || creatorName.trim().isEmpty()) {
                    creator.setFullName("-");
                } else {
                    creator.setUserId(rs.getInt("CreatedByID"));
                    creator.setFullName(creatorName);
                }
                cm.setCreatedBy(creator);

                return cm;
            }
        } catch (SQLException ex) {
            Logger.getLogger(CarMaintenanaceByTechDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    public List<Map<String, Object>> getMaintenanceProducts(int maintenanceId) {
        List<Map<String, Object>> list = new ArrayList<>();

        // ✅ Lấy PackageID nếu có
        Integer packageId = null;
        String checkPackageSql = """
                SELECT RequestedPackageID 
                FROM Appointments a
                JOIN CarMaintenance m ON a.AppointmentID = m.AppointmentID
                WHERE m.MaintenanceID = ?
            """;
        try (PreparedStatement ps = connection.prepareStatement(checkPackageSql)) {
            ps.setInt(1, maintenanceId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                packageId = rs.getInt("RequestedPackageID");
                if (rs.wasNull()) {
                    packageId = null;
                }
            }
        } catch (SQLException e) {
            Logger.getLogger(CarMaintenanaceByTechDAO.class.getName()).log(Level.SEVERE, null, e);
        }

        try {
            // 🔹 Nếu có gói combo → lấy sản phẩm từ MaintenancePackageDetail
            if (packageId != null) {
                String sqlCombo = """
                        SELECT 
                            mp.PackageCode,
                            p.Name AS ProductName,
                            mpd.Quantity,
                            mp.BasePrice,
                            mp.DiscountPercent,
                            mp.FinalPrice
                        FROM MaintenancePackage mp
                        JOIN MaintenancePackageDetail mpd ON mp.PackageID = mpd.PackageID
                        JOIN Product p ON mpd.ProductID = p.ProductID
                        WHERE mp.PackageID = ?
                    """;
                try (PreparedStatement stm = connection.prepareStatement(sqlCombo)) {
                    stm.setInt(1, packageId);
                    ResultSet rs = stm.executeQuery();
                    while (rs.next()) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("packageCode", rs.getString("PackageCode"));
                        map.put("productName", rs.getString("ProductName"));
                        map.put("quantity", rs.getBigDecimal("Quantity"));
                        map.put("basePrice", rs.getDouble("BasePrice"));
                        map.put("discountPercent", rs.getDouble("DiscountPercent"));
                        map.put("finalPrice", rs.getDouble("FinalPrice"));
                        map.put("itemType", "Dịch vụ combo");
                        list.add(map);
                    }
                }
            }

            // 🔹 Dịch vụ lẻ
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
                """;
            try (PreparedStatement stm = connection.prepareStatement(sqlService)) {
                stm.setInt(1, maintenanceId);
                ResultSet rs = stm.executeQuery();
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

            // 🔹 Phụ tùng lẻ
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
                """;
            try (PreparedStatement stm = connection.prepareStatement(sqlPart)) {
                stm.setInt(1, maintenanceId);
                ResultSet rs = stm.executeQuery();
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

        } catch (SQLException e) {
            Logger.getLogger(CarMaintenanaceByTechDAO.class.getName()).log(Level.SEVERE, null, e);
        }

        return list;
    }
}
