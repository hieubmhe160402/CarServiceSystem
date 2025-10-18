package dal;

import context.DBContext;
import java.sql.*;
import java.util.*;
import java.math.BigDecimal;
import model.MaintenancePackage;
import model.User;

public class MaintenancePackageDAO extends DBContext {

    
    public MaintenancePackage getRecommendedPackage(String brand, int currentKm) {
    String sql = """
        SELECT TOP 5 * 
        FROM MaintenancePackage 
        WHERE isActive = 1 
          AND (applicableBrands LIKE ? OR applicableBrands IS NULL OR applicableBrands = '')
          AND kilometerMilestone <= ?
        ORDER BY kilometerMilestone DESC
    """;

    try (Connection conn = connection;
         PreparedStatement ps = conn.prepareStatement(sql)) {

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
    
    
    
   public List<MaintenancePackage> getAllActivePackages() {
    List<MaintenancePackage> list = new ArrayList<>();
    String sql = "SELECT * FROM MaintenancePackage WHERE isActive = 1 ORDER BY displayOrder";

    try (Connection conn = connection;
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

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
   
   // ===== TÌM KIẾM GÓI THEO HÃNG XE =====
    public List<MaintenancePackage> searchPackagesByBrand(String brand) {
    List<MaintenancePackage> list = new ArrayList<>();
    String sql = """
        SELECT * FROM MaintenancePackage 
        WHERE IsActive = 1 
          AND (ApplicableBrands LIKE ? OR ApplicableBrands IS NULL OR ApplicableBrands = '')
        ORDER BY DisplayOrder
    """;

    try (Connection conn = connection;
         PreparedStatement ps = conn.prepareStatement(sql)) {

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

   
    public static void main(String[] args) {
    MaintenancePackageDAO dao = new MaintenancePackageDAO();
    List<MaintenancePackage> packages = dao.getAllActivePackages();

    if (packages == null || packages.isEmpty()) {
        System.out.println("⚠️ Không có gói bảo dưỡng nào đang hoạt động!");
    } else {
        System.out.println("=== Danh sách gói bảo dưỡng đang hoạt động ===");
        for (MaintenancePackage pkg : packages) {
            System.out.println(pkg.getPackageId() + " - " + pkg.getName() + " (" + pkg.getFinalPrice() + " VND)");
        }
        System.out.println("Tổng số gói: " + packages.size());
    }
}
    
}
