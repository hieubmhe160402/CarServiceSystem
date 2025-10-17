package dal;

import context.DBContext;
import java.util.ArrayList;
import java.util.List;
import model.Product;
import java.sql.*;
import model.MaintenancePackage;
import model.MaintenancePackageDetail;

public class MaintenancePackageDetailDAO extends DBContext {

    // ... (các hàm khác giữ nguyên)
    public List<Product> getAllProductIdAndName() {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT ProductID, Name FROM Product ORDER BY Name";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Product p = new Product();
                p.setProductId(rs.getInt("ProductID"));
                p.setName(rs.getString("Name"));
                list.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<MaintenancePackageDetail> getAllPackageDetails() {
        List<MaintenancePackageDetail> list = new ArrayList<>();
        String sql = """
        SELECT 
            mpd.PackageDetailID, mp.PackageCode, p.ProductID, p.Name AS ProductName,
            mpd.Quantity, mpd.IsRequired, mpd.DisplayOrder, mpd.Notes
        FROM MaintenancePackageDetail AS mpd
        INNER JOIN MaintenancePackage AS mp ON mp.PackageID = mpd.PackageID
        INNER JOIN Product AS p ON p.ProductID = mpd.ProductID
        ORDER BY mp.PackageCode, mpd.DisplayOrder;
        """;
        try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                // ... (code bên trong giữ nguyên)
                MaintenancePackageDetail detail = new MaintenancePackageDetail();
                MaintenancePackage pkg = new MaintenancePackage();
                pkg.setPackageCode(rs.getString("PackageCode"));
                detail.setMaintenancePackage(pkg);
                Product product = new Product();
                product.setProductId(rs.getInt("ProductID"));
                product.setName(rs.getString("ProductName"));
                detail.setProduct(product);
                detail.setPackageDetailId(rs.getInt("PackageDetailID"));
                detail.setQuantity(rs.getBigDecimal("Quantity"));
                detail.setIsRequired(rs.getBoolean("IsRequired"));
                detail.setDisplayOrder(rs.getInt("DisplayOrder"));
                detail.setNotes(rs.getString("Notes"));
                list.add(detail);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean updatePackageDetail(MaintenancePackageDetail detail) {
        // ... (hàm này giữ nguyên)
        String sql = "UPDATE MaintenancePackageDetail SET ProductID = ?, Quantity = ?, DisplayOrder = ?, Notes = ? WHERE PackageDetailID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, detail.getProduct().getProductId());
            ps.setBigDecimal(2, detail.getQuantity());
            ps.setInt(3, detail.getDisplayOrder());
            ps.setString(4, detail.getNotes());
            ps.setInt(5, detail.getPackageDetailId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean toggleStatus(int packageDetailId) {
        // ... (hàm này giữ nguyên)
        String sql = "UPDATE MaintenancePackageDetail SET IsRequired = CASE WHEN IsRequired = 1 THEN 0 ELSE 1 END WHERE PackageDetailID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, packageDetailId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // **** HÀM SEARCH ĐÃ ĐƯỢC SỬA LẠI ****
    public List<MaintenancePackageDetail> searchPackageDetails(String productName, String status) {
        List<MaintenancePackageDetail> list = new ArrayList<>();

        StringBuilder sql = new StringBuilder("""
        SELECT 
            mpd.PackageDetailID, mp.PackageCode, p.ProductID, p.Name AS ProductName,
            mpd.Quantity, mpd.IsRequired, mpd.DisplayOrder, mpd.Notes
        FROM MaintenancePackageDetail AS mpd
        INNER JOIN MaintenancePackage AS mp ON mp.PackageID = mpd.PackageID
        INNER JOIN Product AS p ON p.ProductID = mpd.ProductID
        WHERE 1=1
        """);

        List<Object> params = new ArrayList<>();

        if (productName != null && !productName.trim().isEmpty()) {
            sql.append(" AND p.Name LIKE ?");
            params.add("%" + productName.trim() + "%");
        }

        // THAY ĐỔI TẠI ĐÂY: So sánh với "1" và "0" thay vì "active"/"inactive"
        if (status != null && !status.isEmpty()) {
            sql.append(" AND mpd.IsRequired = ?");
            params.add(status);
        }

        sql.append(" ORDER BY mp.PackageCode, mpd.DisplayOrder");

        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    MaintenancePackageDetail detail = new MaintenancePackageDetail();
                    //... (code bên trong giữ nguyên)
                    MaintenancePackage pkg = new MaintenancePackage();
                    pkg.setPackageCode(rs.getString("PackageCode"));
                    detail.setMaintenancePackage(pkg);
                    Product product = new Product();
                    product.setProductId(rs.getInt("ProductID"));
                    product.setName(rs.getString("ProductName"));
                    detail.setProduct(product);
                    detail.setPackageDetailId(rs.getInt("PackageDetailID"));
                    detail.setQuantity(rs.getBigDecimal("Quantity"));
                    detail.setIsRequired(rs.getBoolean("IsRequired"));
                    detail.setDisplayOrder(rs.getInt("DisplayOrder"));
                    detail.setNotes(rs.getString("Notes"));
                    list.add(detail);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public int countPackageDetails(String productName, String status) {
        StringBuilder sql = new StringBuilder("""
            SELECT COUNT(*)
            FROM MaintenancePackageDetail AS mpd
            INNER JOIN Product AS p ON p.ProductID = mpd.ProductID
            WHERE 1=1
        """);

        List<Object> params = new ArrayList<>();

        if (productName != null && !productName.trim().isEmpty()) {
            sql.append(" AND p.Name LIKE ?");
            params.add("%" + productName.trim() + "%");
        }
        if (status != null && !status.isEmpty()) {
            sql.append(" AND mpd.IsRequired = ?");
            params.add(status);
        }

        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<MaintenancePackageDetail> searchPackageDetails(String productName, String status, int offset, int limit) {
        List<MaintenancePackageDetail> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("""
            SELECT 
                mpd.PackageDetailID, mp.PackageCode, p.ProductID, p.Name AS ProductName,
                mpd.Quantity, mpd.IsRequired, mpd.DisplayOrder, mpd.Notes
            FROM MaintenancePackageDetail AS mpd
            INNER JOIN MaintenancePackage AS mp ON mp.PackageID = mpd.PackageID
            INNER JOIN Product AS p ON p.ProductID = mpd.ProductID
            WHERE 1=1
        """);

        List<Object> params = new ArrayList<>();

        if (productName != null && !productName.trim().isEmpty()) {
            sql.append(" AND p.Name LIKE ?");
            params.add("%" + productName.trim() + "%");
        }
        if (status != null && !status.isEmpty()) {
            sql.append(" AND mpd.IsRequired = ?");
            params.add(status);
        }

        sql.append(" ORDER BY mp.PackageCode, mpd.DisplayOrder OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");
        params.add(offset);
        params.add(limit);

        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    //... (code map dữ liệu giữ nguyên)
                    MaintenancePackageDetail detail = new MaintenancePackageDetail();
                    MaintenancePackage pkg = new MaintenancePackage();
                    pkg.setPackageCode(rs.getString("PackageCode"));
                    detail.setMaintenancePackage(pkg);
                    Product product = new Product();
                    product.setProductId(rs.getInt("ProductID"));
                    product.setName(rs.getString("ProductName"));
                    detail.setProduct(product);
                    detail.setPackageDetailId(rs.getInt("PackageDetailID"));
                    detail.setQuantity(rs.getBigDecimal("Quantity"));
                    detail.setIsRequired(rs.getBoolean("IsRequired"));
                    detail.setDisplayOrder(rs.getInt("DisplayOrder"));
                    detail.setNotes(rs.getString("Notes"));
                    list.add(detail);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean addPackageDetail(MaintenancePackageDetail detail) {
        String sql = "INSERT INTO MaintenancePackageDetail (PackageID, ProductID, Quantity, IsRequired, DisplayOrder, Notes) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, detail.getMaintenancePackage().getPackageId());
            ps.setInt(2, detail.getProduct().getProductId());
            ps.setBigDecimal(3, detail.getQuantity());
            ps.setBoolean(4, detail.isIsRequired());
            ps.setInt(5, detail.getDisplayOrder());
            ps.setString(6, detail.getNotes());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // **** HÀM MỚI ĐỂ KIỂM TRA TRÙNG LẶP SẢN PHẨM TRONG GÓI ****
    public boolean isDetailExist(int packageId, int productId) {
        String sql = "SELECT COUNT(*) FROM MaintenancePackageDetail WHERE PackageID = ? AND ProductID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, packageId);
            ps.setInt(2, productId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // **** HÀM MỚI ĐỂ LẤY DANH SÁCH CÁC GÓI CHA ****
    public List<MaintenancePackage> getAllPackages() {
        List<MaintenancePackage> list = new ArrayList<>();
        String sql = "SELECT PackageID, PackageCode, Name FROM MaintenancePackage WHERE IsActive = 1 ORDER BY Name";
        try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                MaintenancePackage p = new MaintenancePackage();
                p.setPackageId(rs.getInt("PackageID"));
                p.setPackageCode(rs.getString("PackageCode"));
                p.setName(rs.getString("Name")); // Giả sử model có setPackageName
                list.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public MaintenancePackage getPackageById(int packageId) {
        String sql = "SELECT PackageID, PackageCode, Name, Description, IsActive FROM MaintenancePackage WHERE PackageID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, packageId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    MaintenancePackage pkg = new MaintenancePackage();
                    pkg.setPackageId(rs.getInt("PackageID"));
                    pkg.setPackageCode(rs.getString("PackageCode"));
                    pkg.setName(rs.getString("Name"));
                    pkg.setDescription(rs.getString("Description"));
                    pkg.setIsActive(rs.getBoolean("IsActive"));
                    return pkg;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
