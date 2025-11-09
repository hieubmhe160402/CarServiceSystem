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
        // Cập nhật tổng tiền trước khi lấy chi tiết
        getMaintenanceFinalAmount(maintenanceId);

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
                    		ISNULL(m.FinalAmount, 0) AS FinalAmount,
                    
                            -- ✅ Tính TotalAmount trực tiếp
                            (
                                SELECT 
                                    ISNULL(SUM(DISTINCT mpu.AppliedPrice), 0)
                                  + ISNULL(SUM(DISTINCT sd.TotalPrice), 0)
                                  + ISNULL(SUM(DISTINCT spd.TotalPrice), 0)
                                FROM CarMaintenance cm
                                LEFT JOIN MaintenancePackageUsage mpu ON cm.MaintenanceID = mpu.MaintenanceID
                                LEFT JOIN ServiceDetails sd ON cm.MaintenanceID = sd.MaintenanceID
                                    AND (sd.Notes IS NULL OR (sd.Notes NOT LIKE '%[ĐÃ XÓA]%' AND sd.Notes NOT LIKE 'Từ gói %'))
                                LEFT JOIN ServicePartDetails spd ON cm.MaintenanceID = spd.MaintenanceID
                                    AND (spd.Notes IS NULL OR (spd.Notes NOT LIKE '%[ĐÃ XÓA]%' AND spd.Notes NOT LIKE 'Từ gói %'))
                                WHERE cm.MaintenanceID = m.MaintenanceID
                            ) AS TotalAmount,
                            m.CreatedBy,
                            creator.UserID AS CreatedByID,
                            creator.FullName AS CreatedByName,
                            u.UserID AS CustomerID,
                            u.FullName AS CustomerName,
                            u.Phone AS CustomerPhone,
                            u.Email AS CustomerEmail,
                            tech.UserID AS TechnicianID,
                            tech.FullName AS TechnicianName,
                            tech.Phone AS TechnicianPhone,
                            tech.Email AS TechnicianEmail,
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
                BigDecimal totalAmount = rs.getBigDecimal("TotalAmount");
                if (totalAmount != null) {
                    cm.setFinalAmount(totalAmount);
                } else {
                    cm.setFinalAmount(rs.getBigDecimal("FinalAmount"));
                }
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
            // Lưu packageCode từ Appointment để loại trừ khi xử lý gói combo đã thêm
            String appointmentPackageCode = null;

            // 🔹 Nếu có gói combo từ Appointment → lấy thông tin gói và sản phẩm
            if (packageId != null) {
                // Lấy thông tin gói combo trước
                String sqlPackageInfo = """
                        SELECT PackageCode, Name, BasePrice, DiscountPercent, FinalPrice
                        FROM MaintenancePackage
                        WHERE PackageID = ?
                    """;
                String packageCode = null;
                String packageName = null;
                Double packageBasePrice = null;
                Double packageDiscountPercent = null;
                Double packageFinalPrice = null;

                try (PreparedStatement stm = connection.prepareStatement(sqlPackageInfo)) {
                    stm.setInt(1, packageId);
                    ResultSet rs = stm.executeQuery();
                    if (rs.next()) {
                        packageCode = rs.getString("PackageCode");
                        appointmentPackageCode = packageCode; // Lưu để loại trừ
                        packageName = rs.getString("Name");
                        packageBasePrice = rs.getDouble("BasePrice");
                        packageDiscountPercent = rs.getDouble("DiscountPercent");
                        packageFinalPrice = rs.getDouble("FinalPrice");
                    }
                }

                // Thêm entry header cho gói combo
                if (packageCode != null) {
                    Map<String, Object> headerMap = new HashMap<>();
                    headerMap.put("packageCode", packageCode);
                    headerMap.put("productName", packageName);
                    headerMap.put("quantity", new BigDecimal(1));
                    headerMap.put("basePrice", packageBasePrice);
                    headerMap.put("discountPercent", packageDiscountPercent);
                    headerMap.put("finalPrice", packageFinalPrice);
                    headerMap.put("itemType", "Dịch vụ combo");
                    list.add(headerMap);
                }

                // Lấy các sản phẩm trong gói
                String sqlCombo = """
                        SELECT 
                            p.Name AS ProductName,
                            mpd.Quantity
                        FROM MaintenancePackage mp
                        JOIN MaintenancePackageDetail mpd ON mp.PackageID = mpd.PackageID
                        JOIN Product p ON mpd.ProductID = p.ProductID
                        WHERE mp.PackageID = ?
                        ORDER BY mpd.DisplayOrder
                    """;
                try (PreparedStatement stm = connection.prepareStatement(sqlCombo)) {
                    stm.setInt(1, packageId);
                    ResultSet rs = stm.executeQuery();
                    while (rs.next()) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("packageCode", packageCode);
                        map.put("productName", rs.getString("ProductName"));
                        map.put("quantity", rs.getBigDecimal("Quantity"));
                        map.put("basePrice", null);
                        map.put("discountPercent", null);
                        map.put("finalPrice", null);
                        map.put("itemType", "Dịch vụ combo");
                        list.add(map);
                    }
                }
            }

            // 🔹 Lấy các gói combo đã thêm (có Notes bắt đầu "Từ gói ")
            // Tìm các packageCode từ Notes của ServiceDetails/ServicePartDetails
            Map<String, String> addedPackageCodes = new HashMap<>();
            String sqlFindAddedPackages = """
                    SELECT DISTINCT LTRIM(RTRIM(REPLACE(sd.Notes, 'Từ gói ', ''))) AS PackageCode
                    FROM ServiceDetails sd
                    WHERE sd.MaintenanceID = ? AND sd.Notes LIKE 'Từ gói %'
                    UNION
                    SELECT DISTINCT LTRIM(RTRIM(REPLACE(spd.Notes, 'Từ gói ', ''))) AS PackageCode
                    FROM ServicePartDetails spd
                    WHERE spd.MaintenanceID = ? AND spd.Notes LIKE 'Từ gói %'
                """;
            try (PreparedStatement stm = connection.prepareStatement(sqlFindAddedPackages)) {
                stm.setInt(1, maintenanceId);
                stm.setInt(2, maintenanceId);
                ResultSet rs = stm.executeQuery();
                while (rs.next()) {
                    String pkgCode = rs.getString("PackageCode");
                    if (pkgCode != null && !pkgCode.trim().isEmpty()) {
                        addedPackageCodes.put(pkgCode.trim(), pkgCode.trim());
                    }
                }
            }

            // Với mỗi packageCode đã tìm được, lấy thông tin gói và tất cả sản phẩm từ MaintenancePackageDetail
            // Loại trừ packageCode từ Appointment (đã xử lý ở trên)
            for (String packageCode : addedPackageCodes.keySet()) {
                // Bỏ qua nếu đã được xử lý từ Appointment
                if (appointmentPackageCode != null && appointmentPackageCode.equals(packageCode)) {
                    continue;
                }
                // Lấy thông tin gói từ MaintenancePackage
                String sqlPackageInfo = """
                        SELECT PackageCode, Name, BasePrice, DiscountPercent, FinalPrice, PackageID
                        FROM MaintenancePackage
                        WHERE PackageCode = ?
                    """;
                try (PreparedStatement stm = connection.prepareStatement(sqlPackageInfo)) {
                    stm.setString(1, packageCode);
                    ResultSet rs = stm.executeQuery();
                    if (rs.next()) {
                        Integer pkgId = rs.getInt("PackageID");

                        // Entry header cho gói
                        Map<String, Object> headerMap = new HashMap<>();
                        headerMap.put("packageCode", rs.getString("PackageCode"));
                        headerMap.put("productName", rs.getString("Name"));
                        headerMap.put("quantity", new BigDecimal(1));
                        headerMap.put("basePrice", rs.getDouble("BasePrice"));
                        headerMap.put("discountPercent", rs.getDouble("DiscountPercent"));
                        headerMap.put("finalPrice", rs.getDouble("FinalPrice"));
                        headerMap.put("itemType", "Dịch vụ combo");
                        list.add(headerMap);

                        // Lấy TẤT CẢ sản phẩm trong gói từ MaintenancePackageDetail (để hiển thị đầy đủ)
                        String sqlPackageProducts = """
                                SELECT 
                                    p.Name AS ProductName,
                                    mpd.Quantity
                                FROM MaintenancePackageDetail mpd
                                JOIN Product p ON mpd.ProductID = p.ProductID
                                WHERE mpd.PackageID = ?
                                ORDER BY mpd.DisplayOrder
                            """;
                        try (PreparedStatement stmProducts = connection.prepareStatement(sqlPackageProducts)) {
                            stmProducts.setInt(1, pkgId);
                            ResultSet rsProducts = stmProducts.executeQuery();
                            while (rsProducts.next()) {
                                Map<String, Object> itemMap = new HashMap<>();
                                itemMap.put("packageCode", packageCode);
                                itemMap.put("productName", rsProducts.getString("ProductName"));
                                itemMap.put("quantity", rsProducts.getBigDecimal("Quantity"));
                                itemMap.put("basePrice", null);
                                itemMap.put("discountPercent", null);
                                itemMap.put("finalPrice", null);
                                itemMap.put("itemType", "Dịch vụ combo");
                                list.add(itemMap);
                            }
                        }
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(CarMaintenanaceByTechDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            // 🔹 Dịch vụ lẻ (không phải từ gói)
            String sqlService = """
                    SELECT 
                        sd.ServiceDetailID,
                        p.Code,
                        p.Name AS ProductName,
                        sd.Quantity,
                        sd.UnitPrice AS BasePrice,
                        0 AS DiscountPercent,
                        sd.TotalPrice AS FinalPrice
                    FROM ServiceDetails sd
                    JOIN Product p ON sd.ProductID = p.ProductID
                    WHERE sd.MaintenanceID = ? 
                        AND (sd.Notes IS NULL OR sd.Notes NOT LIKE 'Từ gói %')
                        AND (sd.Notes IS NULL OR sd.Notes NOT LIKE '%[ĐÃ XÓA]%')
                """;
            try (PreparedStatement stm = connection.prepareStatement(sqlService)) {
                stm.setInt(1, maintenanceId);
                ResultSet rs = stm.executeQuery();
                while (rs.next()) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("serviceDetailId", rs.getInt("ServiceDetailID"));
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

            // 🔹 Phụ tùng lẻ (không phải từ gói)
            String sqlPart = """
                    SELECT 
                        spd.ServicePartDetailID,
                        p.Code,
                        p.Name AS ProductName,
                        spd.Quantity,
                        spd.UnitPrice AS BasePrice,
                        0 AS DiscountPercent,
                        spd.TotalPrice AS FinalPrice
                    FROM ServicePartDetails spd
                    JOIN Product p ON spd.ProductID = p.ProductID
                    WHERE spd.MaintenanceID = ?
                        AND (spd.Notes IS NULL OR spd.Notes NOT LIKE 'Từ gói %')
                        AND (spd.Notes IS NULL OR spd.Notes NOT LIKE '%[ĐÃ XÓA]%')
                """;
            try (PreparedStatement stm = connection.prepareStatement(sqlPart)) {
                stm.setInt(1, maintenanceId);
                ResultSet rs = stm.executeQuery();
                while (rs.next()) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("servicePartDetailId", rs.getInt("ServicePartDetailID"));
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

    /**
     * Lấy danh sách sản phẩm theo loại (SERVICE hoặc PART)
     */
    public List<Map<String, Object>> getProductsByType(String type) {
        List<Map<String, Object>> list = new ArrayList<>();
        String sql = """
            SELECT ProductID, Code, Name, Price, Type
            FROM Product
            WHERE Type = ? AND IsActive = 1
            ORDER BY Name
        """;

        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setString(1, type);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                Map<String, Object> map = new HashMap<>();
                map.put("productId", rs.getInt("ProductID"));
                map.put("code", rs.getString("Code"));
                map.put("name", rs.getString("Name"));
                map.put("price", rs.getBigDecimal("Price"));
                map.put("type", rs.getString("Type"));
                list.add(map);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CarMaintenanaceByTechDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return list;
    }

    /**
     * Lấy đơn giá của sản phẩm theo ProductID
     */
    public BigDecimal getProductPriceById(int productId) {
        String sql = """
            SELECT Price
            FROM Product
            WHERE ProductID = ? AND IsActive = 1
        """;

        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, productId);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                return rs.getBigDecimal("Price");
            }
        } catch (SQLException ex) {
            Logger.getLogger(CarMaintenanaceByTechDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Lấy danh sách gói combo đang hoạt động
     */
    public List<Map<String, Object>> getActivePackages() {
        List<Map<String, Object>> list = new ArrayList<>();
        String sql = """
            SELECT PackageID, PackageCode, Name, FinalPrice
            FROM MaintenancePackage
            WHERE IsActive = 1
            ORDER BY Name
        """;
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Map<String, Object> map = new HashMap<>();
                map.put("packageId", rs.getInt("PackageID"));
                map.put("packageCode", rs.getString("PackageCode"));
                map.put("name", rs.getString("Name"));
                map.put("finalPrice", rs.getBigDecimal("FinalPrice"));
                list.add(map);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CarMaintenanaceByTechDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    /**
     * Thêm toàn bộ chi tiết gói combo vào phiếu bảo dưỡng
     */
    public boolean addPackageToMaintenance(int maintenanceId, int packageId) {
        String getCodeSql = "SELECT PackageCode FROM MaintenancePackage WHERE PackageID = ?";
        String sql = """
            SELECT mpd.ProductID, mpd.Quantity, p.Type, p.Price
            FROM MaintenancePackageDetail mpd
            JOIN Product p ON mpd.ProductID = p.ProductID
            WHERE mpd.PackageID = ?
        """;

        String insertService = """
            INSERT INTO ServiceDetails (MaintenanceID, ProductID, Quantity, UnitPrice, TotalPrice, Notes, FromPackage)
            VALUES (?, ?, ?, ?, ?, ?, 1)
        """;
        String insertPart = """
            INSERT INTO ServicePartDetails (MaintenanceID, ProductID, Quantity, UnitPrice, TotalPrice, Notes, FromPackage, InstallationDate)
            VALUES (?, ?, ?, ?, ?, ?, 1, GETDATE())
        """;

        String packageCode = null;
        try (PreparedStatement codeStm = connection.prepareStatement(getCodeSql)) {
            codeStm.setInt(1, packageId);
            ResultSet crs = codeStm.executeQuery();
            if (crs.next()) {
                packageCode = crs.getString("PackageCode");
            }
        } catch (SQLException ex) {
            Logger.getLogger(CarMaintenanaceByTechDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        boolean anyInserted = false;
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, packageId);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                int productId = rs.getInt("ProductID");
                BigDecimal quantity = rs.getBigDecimal("Quantity");
                BigDecimal price = rs.getBigDecimal("Price");
                String type = rs.getString("Type");
                String note = packageCode != null ? ("Từ gói " + packageCode) : "Từ gói";

                if ("SERVICE".equalsIgnoreCase(type)) {
                    BigDecimal total = quantity.multiply(price);
                    try (PreparedStatement is = connection.prepareStatement(insertService)) {
                        is.setInt(1, maintenanceId);
                        is.setInt(2, productId);
                        is.setBigDecimal(3, quantity);
                        is.setBigDecimal(4, price);
                        is.setBigDecimal(5, total);
                        is.setString(6, note);
                        anyInserted |= is.executeUpdate() > 0;
                    }
                } else if ("PART".equalsIgnoreCase(type)) {
                    int qtyInt = quantity.setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
                    BigDecimal total = price.multiply(new BigDecimal(qtyInt));
                    try (PreparedStatement ip = connection.prepareStatement(insertPart)) {
                        ip.setInt(1, maintenanceId);
                        ip.setInt(2, productId);
                        ip.setInt(3, qtyInt);
                        ip.setBigDecimal(4, price);
                        ip.setBigDecimal(5, total);
                        ip.setString(6, note);
                        anyInserted |= ip.executeUpdate() > 0;
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(CarMaintenanaceByTechDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

        if (anyInserted) {
            getMaintenanceFinalAmount(maintenanceId);
        }
        return anyInserted;
    }

    /**
     * Thêm dịch vụ vào ServiceDetails
     */
    public boolean addServiceDetail(int maintenanceId, int productId, BigDecimal quantity, BigDecimal unitPrice, String notes) {
        String sql = """
            INSERT INTO ServiceDetails (MaintenanceID, ProductID, Quantity, UnitPrice, TotalPrice, Notes, FromPackage)
            VALUES (?, ?, ?, ?, ?, ?, 0)
        """;

        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            BigDecimal totalPrice = quantity.multiply(unitPrice);

            stm.setInt(1, maintenanceId);
            stm.setInt(2, productId);
            stm.setBigDecimal(3, quantity);
            stm.setBigDecimal(4, unitPrice);
            stm.setBigDecimal(5, totalPrice);
            stm.setString(6, notes != null ? notes : "");

            int rowsAffected = stm.executeUpdate();

            // Cập nhật FinalAmount trong CarMaintenance
            if (rowsAffected > 0) {
                // Không update DB — chỉ đọc lại tổng
                BigDecimal newTotal = getMaintenanceFinalAmount(maintenanceId);
                System.out.println("FinalAmount mới: " + newTotal);
            }

            return rowsAffected > 0;
        } catch (SQLException ex) {
            Logger.getLogger(CarMaintenanaceByTechDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    /**
     * Thêm linh kiện vào ServicePartDetails
     */
    public boolean addServicePartDetail(int maintenanceId, int productId, int quantity, BigDecimal unitPrice, String notes) {
        String sql = """
            INSERT INTO ServicePartDetails (MaintenanceID, ProductID, Quantity, UnitPrice, TotalPrice, Notes, FromPackage, InstallationDate)
            VALUES (?, ?, ?, ?, ?, ?, 0, GETDATE())
        """;

        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            BigDecimal totalPrice = unitPrice.multiply(new BigDecimal(quantity));

            stm.setInt(1, maintenanceId);
            stm.setInt(2, productId);
            stm.setInt(3, quantity);
            stm.setBigDecimal(4, unitPrice);
            stm.setBigDecimal(5, totalPrice);
            stm.setString(6, notes != null ? notes : "");

            int rowsAffected = stm.executeUpdate();

            // Cập nhật FinalAmount trong CarMaintenance
            if (rowsAffected > 0) {
                BigDecimal newTotal = getMaintenanceFinalAmount(maintenanceId);
                System.out.println(">>> FinalAmount sau khi thêm linh kiện: " + newTotal);
            }

            return rowsAffected > 0;
        } catch (SQLException ex) {
            Logger.getLogger(CarMaintenanaceByTechDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    /**
     * Xóa dịch vụ (soft delete - đánh dấu trong Notes)
     */
    public boolean deleteServiceDetail(int serviceDetailId, int maintenanceId) {
        String sql = "DELETE FROM ServiceDetails WHERE ServiceDetailID = ? AND MaintenanceID = ?";

        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, serviceDetailId);
            stm.setInt(2, maintenanceId);
            int rowsAffected = stm.executeUpdate();

            if (rowsAffected > 0) {
                BigDecimal newTotal = getMaintenanceFinalAmount(maintenanceId);
                System.out.println(">>> FinalAmount sau khi xóa dịch vụ: " + newTotal);
            }

            return rowsAffected > 0;
        } catch (SQLException ex) {
            Logger.getLogger(CarMaintenanaceByTechDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    /**
     * Xóa linh kiện (soft delete - đánh dấu trong Notes)
     */
    public boolean deleteServicePartDetail(int servicePartDetailId, int maintenanceId) {
        String sql = "DELETE FROM ServicePartDetails WHERE ServicePartDetailID = ? AND MaintenanceID = ?";

        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, servicePartDetailId);
            stm.setInt(2, maintenanceId);
            int rowsAffected = stm.executeUpdate();

            if (rowsAffected > 0) {
                BigDecimal newTotal = getMaintenanceFinalAmount(maintenanceId);
                System.out.println(">>> FinalAmount sau khi xóa dịch vụ: " + newTotal);
            }

            return rowsAffected > 0;
        } catch (SQLException ex) {
            Logger.getLogger(CarMaintenanaceByTechDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    /**
     * Cập nhật FinalAmount trong CarMaintenance: TotalAmount = PackageAmount
     * (từ MaintenancePackageUsage) + ServiceAmount (dịch vụ lẻ, không từ gói) +
     * PartAmount (linh kiện lẻ, không từ gói)
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
            Logger.getLogger(CarMaintenanaceByTechDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return BigDecimal.ZERO;
    }

}
