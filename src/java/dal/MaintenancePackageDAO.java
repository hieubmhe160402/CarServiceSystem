package dal;

import context.DBContext;
import java.io.IOException;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.sql.*;
import java.util.*;
import java.math.BigDecimal;
import model.MaintenancePackage;
import model.MaintenancePackageDetail;
import model.Product;
import model.User;

public class MaintenancePackageDAO extends DBContext {

    //DL
    public MaintenancePackage getRecommendedPackage(String brand, int currentKm) {
        String sql = """
        SELECT TOP 5 * 
        FROM MaintenancePackage 
        WHERE isActive = 1 
          AND (applicableBrands LIKE ? OR applicableBrands IS NULL OR applicableBrands = '')
          AND kilometerMilestone <= ?
        ORDER BY kilometerMilestone DESC
    """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, "%" + brand + "%");
            ps.setInt(2, currentKm);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    MaintenancePackage pkg = new MaintenancePackage();
                    pkg.setPackageId(rs.getInt("packageId"));
                    pkg.setPackageCode(rs.getString("packageCode"));
                    pkg.setName(rs.getString("name"));
                    pkg.setDescription(rs.getString("description"));
                    pkg.setKilometerMilestone(rs.getInt("kilometerMilestone"));
                    pkg.setMonthMilestone(rs.getInt("monthMilestone"));
                    pkg.setBasePrice(rs.getBigDecimal("basePrice"));
                    pkg.setDiscountPercent(rs.getBigDecimal("discountPercent"));
                    pkg.setFinalPrice(rs.getBigDecimal("finalPrice"));
                    pkg.setEstimatedDurationHours(rs.getBigDecimal("estimatedDurationHours"));
                    pkg.setApplicableBrands(rs.getString("applicableBrands"));
                    pkg.setImage(rs.getString("image"));
                    pkg.setDisplayOrder(rs.getInt("displayOrder"));
                    pkg.setIsActive(rs.getBoolean("isActive"));
                    pkg.setCreatedDate(rs.getString("createdDate"));
                    pkg.setCreatedBy(null);
                    return pkg;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    //DL
    public MaintenancePackage getPackageById(int id) {
        String sql = "SELECT * FROM MaintenancePackage WHERE PackageID = ? AND IsActive = 1";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    MaintenancePackage pkg = new MaintenancePackage();
                    // mapping các cột -> thuộc tính model (giữ nhất quán với các hàm khác)
                    pkg.setPackageId(rs.getInt("PackageID"));
                    pkg.setPackageCode(rs.getString("PackageCode"));
                    pkg.setName(rs.getString("Name"));
                    pkg.setDescription(rs.getString("Description"));
                    // Các cột kiểu int có thể là null trong DB -> dùng rs.getObject để kiểm tra null nếu cần
                    int km = rs.getInt("KilometerMilestone");
                    if (rs.wasNull()) {
                        pkg.setKilometerMilestone(null);
                    } else {
                        pkg.setKilometerMilestone(km);
                    }

                    int mo = rs.getInt("MonthMilestone");
                    if (rs.wasNull()) {
                        pkg.setMonthMilestone(null);
                    } else {
                        pkg.setMonthMilestone(mo);
                    }

                    pkg.setBasePrice(rs.getBigDecimal("BasePrice"));
                    pkg.setDiscountPercent(rs.getBigDecimal("DiscountPercent"));
                    // FinalPrice là computed column — vẫn có thể đọc bằng getBigDecimal
                    try {
                        pkg.setFinalPrice(rs.getBigDecimal("FinalPrice"));
                    } catch (Exception ex) {
                        pkg.setFinalPrice(null);
                    }
                    pkg.setEstimatedDurationHours(rs.getBigDecimal("EstimatedDurationHours"));
                    pkg.setApplicableBrands(rs.getString("ApplicableBrands"));
                    pkg.setImage(rs.getString("Image"));

                    int disp = rs.getInt("DisplayOrder");
                    if (rs.wasNull()) {
                        pkg.setDisplayOrder(null);
                    } else {
                        pkg.setDisplayOrder(disp);
                    }

                    pkg.setIsActive(rs.getBoolean("IsActive"));
                    // createdDate đọc dưới dạng String giống các hàm trước
                    pkg.setCreatedDate(rs.getString("CreatedDate"));

                    // CreatedBy là FK -> bạn giữ null (hoặc có thể load User bằng UserDAO nếu cần)
                    pkg.setCreatedBy(null);

                    return pkg;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //DL
    public List<MaintenancePackage> getAllActivePackages() {
        List<MaintenancePackage> list = new ArrayList<>();
        String sql = "SELECT * FROM MaintenancePackage WHERE isActive = 1 ORDER BY displayOrder";

        try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                MaintenancePackage pkg = new MaintenancePackage();
                pkg.setPackageId(rs.getInt("packageId"));
                pkg.setPackageCode(rs.getString("packageCode"));
                pkg.setName(rs.getString("name"));
                pkg.setDescription(rs.getString("description"));
                pkg.setKilometerMilestone(rs.getInt("kilometerMilestone"));
                pkg.setMonthMilestone(rs.getInt("monthMilestone"));
                pkg.setBasePrice(rs.getBigDecimal("basePrice"));
                pkg.setDiscountPercent(rs.getBigDecimal("discountPercent"));
                pkg.setFinalPrice(rs.getBigDecimal("finalPrice"));
                pkg.setEstimatedDurationHours(rs.getBigDecimal("estimatedDurationHours"));
                pkg.setApplicableBrands(rs.getString("applicableBrands"));
                pkg.setImage(rs.getString("image"));
                pkg.setDisplayOrder(rs.getInt("displayOrder"));
                pkg.setIsActive(rs.getBoolean("isActive"));
                pkg.setCreatedDate(rs.getString("createdDate"));
                pkg.setCreatedBy(null); // có thể dùng UserDAO để lấy sau nếu cần

                list.add(pkg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    //DL
    // ===== TÌM KIẾM GÓI THEO HÃNG XE =====
    public List<MaintenancePackage> searchPackagesByBrand(String brand) {
        List<MaintenancePackage> list = new ArrayList<>();
        String sql = """
        SELECT * FROM MaintenancePackage 
        WHERE IsActive = 1 
          AND (ApplicableBrands LIKE ? OR ApplicableBrands IS NULL OR ApplicableBrands = '')
        ORDER BY DisplayOrder
    """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, "%" + brand + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    MaintenancePackage pkg = new MaintenancePackage();
                    pkg.setPackageId(rs.getInt("PackageID"));
                    pkg.setPackageCode(rs.getString("PackageCode"));
                    pkg.setName(rs.getString("Name"));
                    pkg.setDescription(rs.getString("Description"));
                    pkg.setKilometerMilestone(rs.getInt("KilometerMilestone"));
                    pkg.setMonthMilestone(rs.getInt("MonthMilestone"));
                    pkg.setBasePrice(rs.getBigDecimal("BasePrice"));
                    pkg.setDiscountPercent(rs.getBigDecimal("DiscountPercent"));
                    pkg.setFinalPrice(rs.getBigDecimal("FinalPrice"));
                    pkg.setEstimatedDurationHours(rs.getBigDecimal("EstimatedDurationHours"));
                    pkg.setApplicableBrands(rs.getString("ApplicableBrands"));
                    pkg.setImage(rs.getString("Image"));
                    pkg.setDisplayOrder(rs.getInt("DisplayOrder"));
                    pkg.setIsActive(rs.getBoolean("IsActive"));
                    pkg.setCreatedDate(rs.getString("CreatedDate"));
                    pkg.setCreatedBy(null);
                    list.add(pkg);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    //DL
    // ===== LẤY GÓI THEO PACKAGE CODE =====
    public MaintenancePackage getPackageByCode(String packageCode) {
        String sql = "SELECT * FROM MaintenancePackage WHERE PackageCode = ? AND IsActive = 1";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, packageCode);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    MaintenancePackage pkg = new MaintenancePackage();
                    pkg.setPackageId(rs.getInt("PackageID"));
                    pkg.setPackageCode(rs.getString("PackageCode"));
                    pkg.setName(rs.getString("Name"));
                    pkg.setDescription(rs.getString("Description"));

                    int km = rs.getInt("KilometerMilestone");
                    if (rs.wasNull()) {
                        pkg.setKilometerMilestone(null);
                    } else {
                        pkg.setKilometerMilestone(km);
                    }

                    int mo = rs.getInt("MonthMilestone");
                    if (rs.wasNull()) {
                        pkg.setMonthMilestone(null);
                    } else {
                        pkg.setMonthMilestone(mo);
                    }

                    pkg.setBasePrice(rs.getBigDecimal("BasePrice"));
                    pkg.setDiscountPercent(rs.getBigDecimal("DiscountPercent"));

                    try {
                        pkg.setFinalPrice(rs.getBigDecimal("FinalPrice"));
                    } catch (Exception ex) {
                        pkg.setFinalPrice(null);
                    }

                    pkg.setEstimatedDurationHours(rs.getBigDecimal("EstimatedDurationHours"));
                    pkg.setApplicableBrands(rs.getString("ApplicableBrands"));
                    pkg.setImage(rs.getString("Image"));

                    int disp = rs.getInt("DisplayOrder");
                    if (rs.wasNull()) {
                        pkg.setDisplayOrder(null);
                    } else {
                        pkg.setDisplayOrder(disp);
                    }

                    pkg.setIsActive(rs.getBoolean("IsActive"));
                    pkg.setCreatedDate(rs.getString("CreatedDate"));
                    pkg.setCreatedBy(null);

                    return pkg;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<MaintenancePackageDetail> getAll() {
        List<MaintenancePackageDetail> list = new ArrayList<>();
        String sql = """
    SELECT 
            mp.PackageID,
            mp.PackageCode,
            mp.Name AS PackageName,
            mp.Description AS PackageDescription,
            mp.KilometerMilestone,
            mp.MonthMilestone,
            mp.BasePrice,
            mp.DiscountPercent, 
            mp.FinalPrice,
            mp.EstimatedDurationHours,
            mp.ApplicableBrands,
            mp.Image,
            mp.DisplayOrder,
            mp.IsActive,
            mp.CreatedDate,
            mp.CreatedBy,
            u.FullName AS CreatedByFullName,
            mpd.PackageDetailID,
            mpd.DisplayOrder AS DetailDisplayOrder,
            p.ProductID,
            p.Name AS ProductName
        FROM MaintenancePackageDetail mpd
        INNER JOIN MaintenancePackage mp ON mp.PackageID = mpd.PackageID
        INNER JOIN Product p ON p.ProductID = mpd.ProductID
        LEFT JOIN Users u ON mp.CreatedBy = u.UserID
        WHERE  mpd.IsRequired=1
        ORDER BY mp.PackageCode, mpd.DisplayOrder
""";
        try (PreparedStatement stm = connection.prepareStatement(sql); ResultSet rs = stm.executeQuery()) {
            while (rs.next()) {
                // Gói bảo dưỡng
                MaintenancePackage mp = new MaintenancePackage();
                mp.setPackageId(rs.getInt("PackageID"));
                mp.setPackageCode(rs.getString("PackageCode"));
                mp.setName(rs.getString("PackageName"));
                mp.setDescription(rs.getString("PackageDescription"));
                mp.setKilometerMilestone(rs.getInt("KilometerMilestone"));
                mp.setMonthMilestone(rs.getInt("MonthMilestone"));
                mp.setBasePrice(rs.getBigDecimal("BasePrice"));
                mp.setDiscountPercent(rs.getBigDecimal("DiscountPercent"));

                // Chuyển FinalPrice thành số nguyên
                BigDecimal finalPrice = rs.getBigDecimal("FinalPrice");
                if (finalPrice != null) {
                    mp.setFinalPrice(new BigDecimal(finalPrice.intValue()));
                }

                mp.setEstimatedDurationHours(rs.getBigDecimal("EstimatedDurationHours"));
                mp.setApplicableBrands(rs.getString("ApplicableBrands"));
                mp.setImage(rs.getString("Image"));
                mp.setDisplayOrder(rs.getInt("DisplayOrder"));
                mp.setIsActive(rs.getBoolean("IsActive"));
                mp.setCreatedDate(rs.getString("CreatedDate"));

                // Sản phẩm
                Product p = new Product();
                p.setProductId(rs.getInt("ProductID"));
                p.setName(rs.getString("ProductName"));

                User createdBy = new User();
                createdBy.setUserId(rs.getInt("CreatedBy"));
                createdBy.setFullName(rs.getString("CreatedByFullName"));
                mp.setCreatedBy(createdBy);

                // Chi tiết gói
                MaintenancePackageDetail detail = new MaintenancePackageDetail();
                detail.setPackageDetailId(rs.getInt("PackageDetailID"));
                detail.setDisplayOrder(rs.getInt("DetailDisplayOrder"));
                detail.setMaintenancePackage(mp);
                detail.setProduct(p);

                list.add(detail);
            }
        } catch (SQLException ex) {
            Logger.getLogger(MaintenancePackageDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public void updateStatus(int packageId, boolean status) {
        String sql = "UPDATE MaintenancePackage SET IsActive = ? WHERE PackageID = ?";
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setBoolean(1, status);
            stm.setInt(2, packageId);
            stm.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void insertPackage(MaintenancePackage mp) throws IOException {
        String sql = """
    INSERT INTO MaintenancePackage
    (PackageCode, Name, Description, KilometerMilestone, MonthMilestone,
     BasePrice, DiscountPercent, EstimatedDurationHours,
     ApplicableBrands, Image, DisplayOrder, IsActive, CreatedDate, CreatedBy)
    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, GETDATE(), ?)
""";

        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setString(1, mp.getPackageCode());
            stm.setString(2, mp.getName());
            stm.setString(3, mp.getDescription());
            stm.setInt(4, mp.getKilometerMilestone());
            stm.setInt(5, mp.getMonthMilestone());
            stm.setBigDecimal(6, mp.getBasePrice());
            stm.setBigDecimal(7, mp.getDiscountPercent());
            // ✅ FinalPrice không cần set vì là computed column
            stm.setBigDecimal(8, mp.getEstimatedDurationHours());
            stm.setString(9, mp.getApplicableBrands());
            stm.setString(10, mp.getImage());
            stm.setInt(11, mp.getDisplayOrder());
            stm.setBoolean(12, mp.isIsActive());
            stm.setInt(13, mp.getCreatedBy().getUserId());

            stm.executeUpdate();
        } catch (SQLException e) {
            throw new IOException("Lỗi thêm combo mới: " + e.getMessage());
        }
    }

    // ✅ NEW: Search packages by keyword and status
    public List<MaintenancePackageDetail> searchPackages(String keyword, String statusFilter) {
        List<MaintenancePackageDetail> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("""
            SELECT 
                mp.PackageID,
                mp.PackageCode,
                mp.Name AS PackageName,
                mp.Description AS PackageDescription,
                mp.KilometerMilestone,
                mp.MonthMilestone,
                mp.BasePrice,
                mp.DiscountPercent, 
                mp.FinalPrice,
                mp.EstimatedDurationHours,
                mp.ApplicableBrands,
                mp.Image,
                mp.DisplayOrder,
                mp.IsActive,
                mp.CreatedDate,
                mp.CreatedBy,
                u.FullName AS CreatedByFullName,
                mpd.PackageDetailID,
                mpd.DisplayOrder AS DetailDisplayOrder,
                p.ProductID,
                p.Name AS ProductName
            FROM MaintenancePackageDetail mpd
            INNER JOIN MaintenancePackage mp ON mp.PackageID = mpd.PackageID
            INNER JOIN Product p ON p.ProductID = mpd.ProductID
            LEFT JOIN Users u ON mp.CreatedBy = u.UserID
            WHERE (mp.PackageCode LIKE ? OR mp.Name LIKE ? OR mp.Description LIKE ?)
        """);

        if (statusFilter != null && !statusFilter.isEmpty()) {
            sql.append(" AND mp.IsActive = ?");
        }

        sql.append(" ORDER BY mp.PackageCode, mpd.DisplayOrder");

        try (PreparedStatement stm = connection.prepareStatement(sql.toString())) {
            String searchPattern = "%" + keyword + "%";
            stm.setString(1, searchPattern);
            stm.setString(2, searchPattern);
            stm.setString(3, searchPattern);

            if (statusFilter != null && !statusFilter.isEmpty()) {
                stm.setBoolean(4, Boolean.parseBoolean(statusFilter));
            }

            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    // Gói bảo dưỡng
                    MaintenancePackage mp = new MaintenancePackage();
                    mp.setPackageId(rs.getInt("PackageID"));
                    mp.setPackageCode(rs.getString("PackageCode"));
                    mp.setName(rs.getString("PackageName"));
                    mp.setDescription(rs.getString("PackageDescription"));
                    mp.setKilometerMilestone(rs.getInt("KilometerMilestone"));
                    mp.setMonthMilestone(rs.getInt("MonthMilestone"));
                    mp.setBasePrice(rs.getBigDecimal("BasePrice"));
                    mp.setDiscountPercent(rs.getBigDecimal("DiscountPercent"));
                    mp.setFinalPrice(rs.getBigDecimal("FinalPrice"));
                    mp.setEstimatedDurationHours(rs.getBigDecimal("EstimatedDurationHours"));
                    mp.setApplicableBrands(rs.getString("ApplicableBrands"));
                    mp.setImage(rs.getString("Image"));
                    mp.setDisplayOrder(rs.getInt("DisplayOrder"));
                    mp.setIsActive(rs.getBoolean("IsActive"));
                    mp.setCreatedDate(rs.getString("CreatedDate"));

                    // Sản phẩm
                    Product p = new Product();
                    p.setProductId(rs.getInt("ProductID"));
                    p.setName(rs.getString("ProductName"));

                    User createdBy = new User();
                    createdBy.setUserId(rs.getInt("CreatedBy"));
                    createdBy.setFullName(rs.getString("CreatedByFullName"));
                    mp.setCreatedBy(createdBy);

                    // Chi tiết gói
                    MaintenancePackageDetail detail = new MaintenancePackageDetail();
                    detail.setPackageDetailId(rs.getInt("PackageDetailID"));
                    detail.setDisplayOrder(rs.getInt("DetailDisplayOrder"));
                    detail.setMaintenancePackage(mp);
                    detail.setProduct(p);

                    list.add(detail);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(MaintenancePackageDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    // ✅ NEW: Get packages by status
    public List<MaintenancePackageDetail> getPackagesByStatus(String statusFilter) {
        List<MaintenancePackageDetail> list = new ArrayList<>();
        String sql = """
            SELECT 
                mp.PackageID,
                mp.PackageCode,
                mp.Name AS PackageName,
                mp.Description AS PackageDescription,
                mp.KilometerMilestone,
                mp.MonthMilestone,
                mp.BasePrice,
                mp.DiscountPercent, 
                mp.FinalPrice,
                mp.EstimatedDurationHours,
                mp.ApplicableBrands,
                mp.Image,
                mp.DisplayOrder,
                mp.IsActive,
                mp.CreatedDate,
                mp.CreatedBy,
                u.FullName AS CreatedByFullName,
                mpd.PackageDetailID,
                mpd.DisplayOrder AS DetailDisplayOrder,
                p.ProductID,
                p.Name AS ProductName
            FROM MaintenancePackageDetail mpd
            INNER JOIN MaintenancePackage mp ON mp.PackageID = mpd.PackageID
            INNER JOIN Product p ON p.ProductID = mpd.ProductID
            LEFT JOIN Users u ON mp.CreatedBy = u.UserID
            WHERE mp.IsActive = ?
            ORDER BY mp.PackageCode, mpd.DisplayOrder
        """;

        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setBoolean(1, Boolean.parseBoolean(statusFilter));
            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    // Gói bảo dưỡng
                    MaintenancePackage mp = new MaintenancePackage();
                    mp.setPackageId(rs.getInt("PackageID"));
                    mp.setPackageCode(rs.getString("PackageCode"));
                    mp.setName(rs.getString("PackageName"));
                    mp.setDescription(rs.getString("PackageDescription"));
                    mp.setKilometerMilestone(rs.getInt("KilometerMilestone"));
                    mp.setMonthMilestone(rs.getInt("MonthMilestone"));
                    mp.setBasePrice(rs.getBigDecimal("BasePrice"));
                    mp.setDiscountPercent(rs.getBigDecimal("DiscountPercent"));
                    mp.setFinalPrice(rs.getBigDecimal("FinalPrice"));
                    mp.setEstimatedDurationHours(rs.getBigDecimal("EstimatedDurationHours"));
                    mp.setApplicableBrands(rs.getString("ApplicableBrands"));
                    mp.setImage(rs.getString("Image"));
                    mp.setDisplayOrder(rs.getInt("DisplayOrder"));
                    mp.setIsActive(rs.getBoolean("IsActive"));
                    mp.setCreatedDate(rs.getString("CreatedDate"));

                    // Sản phẩm
                    Product p = new Product();
                    p.setProductId(rs.getInt("ProductID"));
                    p.setName(rs.getString("ProductName"));

                    User createdBy = new User();
                    createdBy.setUserId(rs.getInt("CreatedBy"));
                    createdBy.setFullName(rs.getString("CreatedByFullName"));
                    mp.setCreatedBy(createdBy);

                    // Chi tiết gói
                    MaintenancePackageDetail detail = new MaintenancePackageDetail();
                    detail.setPackageDetailId(rs.getInt("PackageDetailID"));
                    detail.setDisplayOrder(rs.getInt("DetailDisplayOrder"));
                    detail.setMaintenancePackage(mp);
                    detail.setProduct(p);

                    list.add(detail);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(MaintenancePackageDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    // ✅ NEW: Update package
    public void updatePackage(MaintenancePackage mp) throws IOException {
        // Build SQL dynamically based on whether image is being updated
        StringBuilder sql = new StringBuilder("""
    UPDATE MaintenancePackage
    SET PackageCode = ?, Name = ?, Description = ?, KilometerMilestone = ?, MonthMilestone = ?,
        BasePrice = ?, DiscountPercent = ?, EstimatedDurationHours = ?,
        ApplicableBrands = ?, DisplayOrder = ?, IsActive = ?
    """);

        // Add Image to SET clause if provided
        if (mp.getImage() != null && !mp.getImage().trim().isEmpty()) {
            sql.append(", Image = ?");
        }

        sql.append(" WHERE PackageID = ?");

        try (PreparedStatement stm = connection.prepareStatement(sql.toString())) {
            int paramIndex = 1;
            stm.setString(paramIndex++, mp.getPackageCode());
            stm.setString(paramIndex++, mp.getName());
            stm.setString(paramIndex++, mp.getDescription());
            stm.setInt(paramIndex++, mp.getKilometerMilestone());
            stm.setInt(paramIndex++, mp.getMonthMilestone());
            stm.setBigDecimal(paramIndex++, mp.getBasePrice());
            stm.setBigDecimal(paramIndex++, mp.getDiscountPercent());
            // ✅ FinalPrice không cần update vì là computed column
            stm.setBigDecimal(paramIndex++, mp.getEstimatedDurationHours());
            stm.setString(paramIndex++, mp.getApplicableBrands());
            stm.setInt(paramIndex++, mp.getDisplayOrder());
            stm.setBoolean(paramIndex++, mp.isIsActive());

            // Add image if provided
            if (mp.getImage() != null && !mp.getImage().trim().isEmpty()) {
                stm.setString(paramIndex++, mp.getImage());
            }

            stm.setInt(paramIndex++, mp.getPackageId());

            stm.executeUpdate();
        } catch (SQLException e) {
            throw new IOException("Lỗi cập nhật combo: " + e.getMessage());
        }
    }

}
