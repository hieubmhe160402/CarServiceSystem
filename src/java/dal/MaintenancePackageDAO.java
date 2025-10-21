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
                if (rs.wasNull()) pkg.setKilometerMilestone(null);
                else pkg.setKilometerMilestone(km);

                int mo = rs.getInt("MonthMilestone");
                if (rs.wasNull()) pkg.setMonthMilestone(null);
                else pkg.setMonthMilestone(mo);

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
                if (rs.wasNull()) pkg.setDisplayOrder(null);
                else pkg.setDisplayOrder(disp);

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
    
   public List<MaintenancePackage> getAllActivePackages() {
    List<MaintenancePackage> list = new ArrayList<>();
    String sql = "SELECT * FROM MaintenancePackage WHERE isActive = 1 ORDER BY displayOrder";

    try (PreparedStatement ps = connection.prepareStatement(sql);
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

   public static void main(String[] args) {
    MaintenancePackageDAO dao = new MaintenancePackageDAO();
    
    int testId = 1; // 👈 đổi ID gói bảo dưỡng bạn muốn test
    MaintenancePackage pkg = dao.getPackageById(testId);

    if (pkg != null) {
        System.out.println("===== Gói bảo dưỡng tìm thấy =====");
        System.out.println("ID: " + pkg.getPackageId());
        System.out.println("Tên gói: " + pkg.getName());
        System.out.println("Mô tả: " + pkg.getDescription());
        System.out.println("KM mốc: " + pkg.getKilometerMilestone());
        System.out.println("Tháng mốc: " + pkg.getMonthMilestone());
        System.out.println("Giá gốc: " + pkg.getBasePrice());
        System.out.println("Giảm giá: " + pkg.getDiscountPercent());
        System.out.println("Giá cuối: " + pkg.getFinalPrice());
        System.out.println("Hãng áp dụng: " + pkg.getApplicableBrands());
        System.out.println("Ảnh: " + pkg.getImage());
        System.out.println("Ngày tạo: " + pkg.getCreatedDate());
        System.out.println("Trạng thái: " + (pkg.isIsActive() ? "Hoạt động" : "Ngừng"));
    } else {
        System.out.println("❌ Không tìm thấy gói nào với ID = " + testId);
    }
}
  
    
}
