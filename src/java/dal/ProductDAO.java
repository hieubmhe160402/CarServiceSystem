package dal;

import Context.DBContext;
import java.sql.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Category;
import model.Product;
import model.Unit;

public class ProductDAO extends DBContext {

    public int countProductsByFilter(String categoryFilter, String statusFilter) {
        String sql = "SELECT COUNT(*) FROM Product WHERE type = 'PART'";

        if (categoryFilter != null && !categoryFilter.isEmpty()) {
            sql += " AND CategoryID = ?";
        }
        if (statusFilter != null && !statusFilter.isEmpty()) {
            sql += " AND IsActive = ?";
        }

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            int paramIndex = 1;
            if (categoryFilter != null && !categoryFilter.isEmpty()) {
                ps.setInt(paramIndex++, Integer.parseInt(categoryFilter));
            }
            if (statusFilter != null && !statusFilter.isEmpty()) {
                ps.setInt(paramIndex++, Integer.parseInt(statusFilter));
            }

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

// Method 2: Lấy danh sách products có phân trang
    public List<Product> getProductsByPartTypeWithPaging(String categoryFilter, String statusFilter,
            int offset, int limit) {
        List<Product> products = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT p.ProductID, p.Code, p.Name, p.Type, p.Price, p.Description, ");
        sql.append("p.Image, p.UnitID, p.CategoryID, p.WarrantyPeriodMonths, ");
        sql.append("p.MinStockLevel, p.IsActive, c.Name as CategoryName, u.Name as UnitName ");
        sql.append("FROM Product p ");
        sql.append("LEFT JOIN Category c ON p.CategoryID = c.CategoryID ");
        sql.append("LEFT JOIN Unit u ON p.UnitID = u.UnitID ");
        sql.append("WHERE p.Type = 'PART'");

        if (categoryFilter != null && !categoryFilter.isEmpty()) {
            sql.append(" AND p.CategoryID = ?");
        }
        if (statusFilter != null && !statusFilter.isEmpty()) {
            sql.append(" AND p.IsActive = ?");
        }

        sql.append(" ORDER BY p.ProductID DESC");
        sql.append(" OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");

        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {

            int paramIndex = 1;
            if (categoryFilter != null && !categoryFilter.isEmpty()) {
                ps.setInt(paramIndex++, Integer.parseInt(categoryFilter));
            }
            if (statusFilter != null && !statusFilter.isEmpty()) {
                ps.setInt(paramIndex++, Integer.parseInt(statusFilter));
            }
            ps.setInt(paramIndex++, offset);
            ps.setInt(paramIndex++, limit);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Product product = new Product();
                product.setProductId(rs.getInt("ProductID"));
                product.setCode(rs.getString("Code"));
                product.setName(rs.getString("Name"));
                product.setType(rs.getString("Type"));
                product.setPrice(rs.getBigDecimal("Price"));
                product.setDescription(rs.getString("Description"));
                product.setImage(rs.getString("Image"));
                product.setWarrantyPeriodMonths(rs.getInt("WarrantyPeriodMonths"));
                product.setMinStockLevel(rs.getInt("MinStockLevel"));
                product.setIsActive(rs.getBoolean("IsActive"));

                // Set Unit
                Unit unit = new Unit();
                unit.setUnitId(rs.getInt("UnitID"));
                unit.setName(rs.getString("UnitName"));
                product.setUnit(unit);

                // Set Category
                Category category = new Category();
                category.setCategoryId(rs.getInt("CategoryID"));
                category.setName(rs.getString("CategoryName"));
                product.setCategory(category);

                products.add(product);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return products;
    }

    public boolean isProductCodeExist(String code) {
        String sql = "SELECT COUNT(*) FROM Product WHERE Code = ?";
        try ( PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, code);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

// Kiểm tra mã sản phẩm đã tồn tại cho update (trừ chính nó)
    public boolean isProductCodeExistForUpdate(String code, int productId) {
        String sql = "SELECT COUNT(*) FROM Product WHERE Code = ? AND ProductID != ?";
        try ( PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, code);
            ps.setInt(2, productId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

// Thêm sản phẩm mới
    public boolean addProduct(Product product) {
        String sql = "INSERT INTO Product (Code, Name, Type, Price, Description, Image, "
                + "UnitID, CategoryID, WarrantyPeriodMonths, MinStockLevel, IsActive, CreatedDate) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, GETDATE())";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, product.getCode());
            ps.setString(2, product.getName());
            ps.setString(3, product.getType());
            ps.setBigDecimal(4, product.getPrice());
            ps.setString(5, product.getDescription());
            ps.setString(6, product.getImage());
            ps.setInt(7, product.getUnit().getUnitId());
            ps.setInt(8, product.getCategory().getCategoryId());
            ps.setInt(9, product.getWarrantyPeriodMonths());
            ps.setInt(10, product.getMinStockLevel());
            ps.setBoolean(11, product.isIsActive());

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

// Cập nhật sản phẩm
    public boolean updateProduct(Product product) {
        String sql = "UPDATE Product SET Code = ?, Name = ?, Type = ?, Price = ?, "
                + "Description = ?, Image = ?, UnitID = ?, CategoryID = ?, "
                + "WarrantyPeriodMonths = ?, MinStockLevel = ?, IsActive = ? "
                + "WHERE ProductID = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, product.getCode());
            ps.setString(2, product.getName());
            ps.setString(3, product.getType());
            ps.setBigDecimal(4, product.getPrice());
            ps.setString(5, product.getDescription());
            ps.setString(6, product.getImage());
            ps.setInt(7, product.getUnit().getUnitId());
            ps.setInt(8, product.getCategory().getCategoryId());
            ps.setInt(9, product.getWarrantyPeriodMonths());
            ps.setInt(10, product.getMinStockLevel());
            ps.setBoolean(11, product.isIsActive());
            ps.setInt(12, product.getProductId());

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

// Xóa sản phẩm
    public boolean deleteProduct(int productId) {
        String sql = "DELETE FROM Product WHERE ProductID = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, productId);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
