/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import context.DBContext;
import java.util.ArrayList;
import java.util.List;
import model.SupplierProduct;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.Product;
import model.Supplier;

/**
 *
 * @author MinHeee
 */
public class SupplierProductDAO extends DBContext {

    public List<SupplierProduct> getAll() {
        List<SupplierProduct> list = new ArrayList<>();
        String sql = """
        SELECT sp.SupplierID, sp.ProductID, sp.DeliveryDuration,
               sp.EstimatedPrice, sp.Policies, sp.IsActive,
               s.Name AS SupplierName, s.Email AS SupplierEmail,
               p.Name AS ProductName
        FROM Supplier_Product sp
        JOIN Supplier s ON sp.SupplierID = s.SupplierID
        JOIN Product p ON sp.ProductID = p.ProductID
    """;

        try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Supplier supplier = new Supplier();
                supplier.setSupplierId(rs.getInt("SupplierID"));
                supplier.setName(rs.getString("SupplierName"));
                supplier.setEmail(rs.getString("SupplierEmail"));

                Product product = new Product();
                product.setProductId(rs.getInt("ProductID"));
                product.setName(rs.getString("ProductName")); // ✅ fix alias

                SupplierProduct sp = new SupplierProduct();
                sp.setSupplier(supplier);
                sp.setProduct(product);
                sp.setDeliveryDuration(rs.getInt("DeliveryDuration"));
                sp.setEstimatedPrice(rs.getBigDecimal("EstimatedPrice"));
                sp.setPolicies(rs.getString("Policies"));
                sp.setIsActive(rs.getBoolean("IsActive"));

                list.add(sp);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean insert(SupplierProduct sp) {
        String sql = """
        INSERT INTO Supplier_Product (SupplierID, ProductID, DeliveryDuration, EstimatedPrice, Policies, IsActive)
        VALUES (?, ?, ?, ?, ?, ?)
    """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, sp.getSupplier().getSupplierId());
            ps.setInt(2, sp.getProduct().getProductId());
            ps.setInt(3, sp.getDeliveryDuration());
            ps.setBigDecimal(4, sp.getEstimatedPrice());
            ps.setString(5, sp.getPolicies());
            ps.setBoolean(6, sp.isIsActive()); // ✅ lấy từ form thay vì gán cố định
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean exists(int supplierId, int productId) {
        String sql = "SELECT COUNT(*) FROM Supplier_Product WHERE SupplierID = ? AND ProductID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, supplierId);
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

    public void update(SupplierProduct sp) {
        String sql = """
        UPDATE Supplier_Product
        SET DeliveryDuration = ?, EstimatedPrice = ?, Policies = ?, IsActive = ?
        WHERE SupplierID = ? AND ProductID = ?
    """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, sp.getDeliveryDuration());
            ps.setBigDecimal(2, sp.getEstimatedPrice());
            ps.setString(3, sp.getPolicies());
            ps.setBoolean(4, sp.isIsActive());
            ps.setInt(5, sp.getSupplier().getSupplierId());
            ps.setInt(6, sp.getProduct().getProductId());

            System.out.println("DEBUG DAO UPDATE - Executing UPDATE with:");
            System.out.println("  SupplierID: " + sp.getSupplier().getSupplierId());
            System.out.println("  ProductID: " + sp.getProduct().getProductId());
            System.out.println("  DeliveryDuration: " + sp.getDeliveryDuration());
            System.out.println("  EstimatedPrice: " + sp.getEstimatedPrice());
            System.out.println("  Policies: " + sp.getPolicies());
            System.out.println("  IsActive: " + sp.isIsActive());

            int rowsAffected = ps.executeUpdate();
            System.out.println("DEBUG DAO UPDATE - Rows affected: " + rowsAffected);
        } catch (SQLException e) {
            System.out.println("DEBUG DAO UPDATE - Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public List<Supplier> getAllSuppliers() {
        List<Supplier> list = new ArrayList<>();
        String sql = "SELECT SupplierID, Name FROM Supplier WHERE IsActive = 1";
        try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Supplier s = new Supplier();
                s.setSupplierId(rs.getInt("SupplierID"));
                s.setName(rs.getString("Name"));
                list.add(s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<String> getAllSupplierNames() {
        List<String> names = new ArrayList<>();
        String sql = "SELECT Name FROM Supplier WHERE IsActive = 1"; // chỉ lấy những supplier đang hoạt động

        try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                names.add(rs.getString("Name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return names;
    }

    public List<String> getAllProductNamesByTypePart() {
        List<String> names = new ArrayList<>();
        String sql = "SELECT Name FROM Product WHERE Type = 'PART' AND IsActive = 1";

        try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String productName = rs.getString("Name");
                names.add(productName);
                System.out.println("DEBUG - Available product: '" + productName + "'");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return names;
    }

    public Integer getSupplierIdByName(String name) {
        String sql = "SELECT SupplierID FROM Supplier WHERE LTRIM(RTRIM(Name)) = LTRIM(RTRIM(?))";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("SupplierID");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Integer getProductIdByName(String name) {
        String sql = "SELECT ProductID FROM Product WHERE LTRIM(RTRIM(Name)) = LTRIM(RTRIM(?))";
        System.out.println("DEBUG DAO - Searching for product: '" + name + "'");
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int productId = rs.getInt("ProductID");
                    System.out.println("DEBUG DAO - Found productId: " + productId);
                    return productId;
                } else {
                    System.out.println("DEBUG DAO - Product not found!");
                }
            }
        } catch (SQLException e) {
            System.out.println("DEBUG DAO - Error: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public void updateStatus(int supplierId, int productId, boolean isActive) {
        String sql = "UPDATE Supplier_Product SET IsActive = ? WHERE SupplierID = ? AND ProductID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setBoolean(1, isActive);
            ps.setInt(2, supplierId);
            ps.setInt(3, productId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean delete(int supplierId, int productId) {
        String sql = "DELETE FROM Supplier_Product WHERE SupplierID = ? AND ProductID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, supplierId);
            ps.setInt(2, productId);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    // Đếm tổng số bản ghi

    public int getTotalSupplierProductCount() {
        String sql = "SELECT COUNT(*) FROM Supplier_Product";
        try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

// Lấy danh sách theo trang
    public List<SupplierProduct> getByPage(int page, int pageSize) {
        List<SupplierProduct> list = new ArrayList<>();
        String sql = """
        SELECT sp.SupplierID, sp.ProductID, sp.DeliveryDuration,
               sp.EstimatedPrice, sp.Policies, sp.IsActive,
               s.Name AS SupplierName, s.Email AS SupplierEmail,
               p.Name AS ProductName
        FROM Supplier_Product sp
        JOIN Supplier s ON sp.SupplierID = s.SupplierID
        JOIN Product p ON sp.ProductID = p.ProductID
        ORDER BY sp.SupplierID, sp.ProductID
        OFFSET ? ROWS FETCH NEXT ? ROWS ONLY
    """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, (page - 1) * pageSize);
            ps.setInt(2, pageSize);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Supplier supplier = new Supplier();
                    supplier.setSupplierId(rs.getInt("SupplierID"));
                    supplier.setName(rs.getString("SupplierName"));
                    supplier.setEmail(rs.getString("SupplierEmail"));

                    Product product = new Product();
                    product.setProductId(rs.getInt("ProductID"));
                    product.setName(rs.getString("ProductName"));

                    SupplierProduct sp = new SupplierProduct();
                    sp.setSupplier(supplier);
                    sp.setProduct(product);
                    sp.setDeliveryDuration(rs.getInt("DeliveryDuration"));
                    sp.setEstimatedPrice(rs.getBigDecimal("EstimatedPrice"));
                    sp.setPolicies(rs.getString("Policies"));
                    sp.setIsActive(rs.getBoolean("IsActive"));

                    list.add(sp);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

}
