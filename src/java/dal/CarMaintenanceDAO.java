/*
         * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
         * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.sql.*;
import context.DBContext;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.Appointment;
import model.Car;
import model.CarMaintenance;
import model.User;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 *
 * @author MinHeee
 */
public class CarMaintenanceDAO extends DBContext {

    public void updateStatus(int maintenanceId, String newStatus) {
        String sql = "UPDATE CarMaintenance SET Status = ?, AssignedTechnicianID = NULL WHERE MaintenanceID = ?";
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setString(1, newStatus);
            stm.setInt(2, maintenanceId);
            stm.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(CarMaintenanceDAO.class.getName()).log(Level.SEVERE, null, ex);
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

                return cm;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public List<User> getTechnicians() {
        List<User> list = new ArrayList<>();
        String sql = """
                SELECT 
                    u.UserID,
                    u.FullName,
                    u.Email,
                    u.Phone,
                    u.IsActive,
                    r.RoleID,
                    r.RoleName
                FROM Users u
                INNER JOIN Role r ON u.RoleID = r.RoleID
                WHERE u.RoleID = 3
                  AND u.IsActive = 1
                ORDER BY u.FullName ASC
            """;

        try (PreparedStatement stm = connection.prepareStatement(sql); ResultSet rs = stm.executeQuery()) {

            while (rs.next()) {
                User u = new User();
                u.setUserId(rs.getInt("UserID"));
                u.setFullName(rs.getString("FullName"));
                u.setEmail(rs.getString("Email"));
                u.setPhone(rs.getString("Phone"));
                u.setIsActive(rs.getBoolean("IsActive"));

                // Gán role cho user
                model.Role r = new model.Role();
                r.setRoleID(rs.getInt("RoleID"));
                r.setRoleName(rs.getString("RoleName"));
                u.setRole(r);

                list.add(u);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public void assignTechnician(int maintenanceId, int technicianId) {
        String sql = """
                UPDATE CarMaintenance
                SET AssignedTechnicianID = ?, Status = 'PROCESSING'
                WHERE MaintenanceID = ?
            """;
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, technicianId);
            stm.setInt(2, maintenanceId);
            int rows = stm.executeUpdate();
            System.out.println(">>> assignTechnician updated rows = " + rows);
            System.out.println(">>> technicianId = " + technicianId + ", maintenanceId = " + maintenanceId);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
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
            e.printStackTrace();
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
            e.printStackTrace();
        }

        return list;
    }

    public int countMaintenanceRecords(String status, String search) {
        int count = 0;
        try {
            StringBuilder sql = new StringBuilder("""
                        SELECT COUNT(*) AS Total
                        FROM CarMaintenance cm
                        LEFT JOIN Cars c ON cm.CarID = c.CarID
                        LEFT JOIN Users u ON c.OwnerID = u.UserID
                        WHERE 1=1
                    """);

            List<Object> params = new ArrayList<>();

            if (status != null && !status.isEmpty()) {
                sql.append(" AND cm.Status = ? ");
                params.add(status);
            }

            if (search != null && !search.isEmpty()) {
                sql.append(" AND u.FullName LIKE ? ");
                params.add("%" + search + "%");
            }

            PreparedStatement stm = connection.prepareStatement(sql.toString());
            for (int i = 0; i < params.size(); i++) {
                stm.setObject(i + 1, params.get(i));
            }

            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                count = rs.getInt("Total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    // Lấy danh sách bảo dưỡng có phân trang
    public List<CarMaintenance> getAllCarMaintenances(String status, String search, int pageIndex, int pageSize) {
        List<CarMaintenance> list = new ArrayList<>();
        try {
            StringBuilder sql = new StringBuilder("""
                        SELECT 
                            cm.MaintenanceID,
                            a.AppointmentID,
                            u.FullName AS CustomerName,
                            c.LicensePlate,
                            c.Brand,
                            c.Model,
                            CONVERT(VARCHAR(10), cm.MaintenanceDate, 103) AS MaintenanceDate,
                            cm.Status,
                            tech.FullName AS TechnicianName
                        FROM CarMaintenance cm
                        LEFT JOIN Appointments a ON cm.AppointmentID = a.AppointmentID
                        LEFT JOIN Cars c ON cm.CarID = c.CarID
                        LEFT JOIN Users u ON c.OwnerID = u.UserID
                        LEFT JOIN Users tech ON cm.AssignedTechnicianID = tech.UserID
                        WHERE 1=1
                    """);

            List<Object> params = new ArrayList<>();

            if (status != null && !status.isEmpty()) {
                sql.append(" AND cm.Status = ? ");
                params.add(status);
            }

            if (search != null && !search.isEmpty()) {
                sql.append(" AND u.FullName LIKE ? ");
                params.add("%" + search + "%");
            }

            sql.append(" ORDER BY cm.MaintenanceDate DESC ");
            sql.append(" OFFSET ? ROWS FETCH NEXT ? ROWS ONLY "); // SQL Server style pagination

            int offset = (pageIndex - 1) * pageSize;
            params.add(offset);
            params.add(pageSize);

            PreparedStatement stm = connection.prepareStatement(sql.toString());
            for (int i = 0; i < params.size(); i++) {
                stm.setObject(i + 1, params.get(i));
            }

            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                CarMaintenance cm = new CarMaintenance();
                cm.setMaintenanceId(rs.getInt("MaintenanceID"));
                cm.setStatus(rs.getString("Status"));
                cm.setMaintenanceDate(rs.getString("MaintenanceDate"));

                Appointment appointment = new Appointment();
                appointment.setAppointmentId(rs.getInt("AppointmentID"));
                cm.setAppointment(appointment);

                Car car = new Car();
                car.setLicensePlate(rs.getString("LicensePlate"));
                car.setBrand(rs.getString("Brand"));
                car.setModel(rs.getString("Model"));

                User owner = new User();
                owner.setFullName(rs.getString("CustomerName"));
                car.setOwner(owner);
                cm.setCar(car);

                User technician = new User();
                String techName = rs.getString("TechnicianName");
                technician.setFullName((techName == null || techName.trim().isEmpty()) ? "None" : techName);
                cm.setAssignedTechnician(technician);

                list.add(cm);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return list;
    }

    /**
     * 🔹 Tính tổng chi phí (FinalAmount) của một phiếu bảo dưỡng Gồm: Giá gói +
     * dịch vụ lẻ + linh kiện thay thế (loại bỏ dòng đã xóa hoặc thuộc gói)
     */
    public BigDecimal getMaintenanceFinalAmount(int maintenanceId) {
        String sql = """
            SELECT 
                ISNULL(SUM(DISTINCT mpu.AppliedPrice), 0) AS PackageAmount,
                ISNULL(SUM(DISTINCT sd.TotalPrice), 0) AS ServiceAmount,
                ISNULL(SUM(DISTINCT spd.TotalPrice), 0) AS PartAmount,
                (ISNULL(SUM(DISTINCT mpu.AppliedPrice), 0)
                 + ISNULL(SUM(DISTINCT sd.TotalPrice), 0)
                 + ISNULL(SUM(DISTINCT spd.TotalPrice), 0)) AS TotalAmount
            FROM CarMaintenance cm
            LEFT JOIN MaintenancePackageUsage mpu 
                ON cm.MaintenanceID = mpu.MaintenanceID
            LEFT JOIN ServiceDetails sd 
                ON cm.MaintenanceID = sd.MaintenanceID
                AND (sd.Notes IS NULL OR (sd.Notes NOT LIKE '%[ĐÃ XÓA]%' AND sd.Notes NOT LIKE 'Từ gói %'))
            LEFT JOIN ServicePartDetails spd 
                ON cm.MaintenanceID = spd.MaintenanceID
                AND (spd.Notes IS NULL OR (spd.Notes NOT LIKE '%[ĐÃ XÓA]%' AND spd.Notes NOT LIKE 'Từ gói %'))
            WHERE cm.MaintenanceID = ?
            GROUP BY cm.MaintenanceID
        """;

        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, maintenanceId);
            ResultSet rs = stm.executeQuery();

            if (rs.next()) {
                BigDecimal totalAmount = rs.getBigDecimal("TotalAmount");
                return totalAmount != null ? totalAmount : BigDecimal.ZERO;
            }
        } catch (SQLException ex) {
            Logger.getLogger(CarMaintenanceDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return BigDecimal.ZERO;
    }

}
