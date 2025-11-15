package dal;

import context.DBContext;
import model.Category;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CategoryDAO extends DBContext {

    public boolean add(Category category) {
        String sql = "INSERT INTO Category (Name, Type, Description) VALUES (?, ?, ?)";
        try (PreparedStatement stm = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stm.setString(1, category.getName());
            stm.setString(2, category.getType());
            stm.setString(3, category.getDescription());
            int result = stm.executeUpdate();
            
            // Lấy ID được generate
            if (result > 0) {
                try (ResultSet rs = stm.getGeneratedKeys()) {
                    if (rs.next()) {
                        category.setCategoryId(rs.getInt(1));
                    }
                }
            }
            return result > 0;
        } catch (SQLException ex) {
            Logger.getLogger(CategoryDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    // READ: Lấy tất cả Category
    public List<Category> getAll() {
        List<Category> list = new ArrayList<>();
        String sql = "SELECT * FROM Category";
        try (PreparedStatement stm = connection.prepareStatement(sql); ResultSet rs = stm.executeQuery()) {

            while (rs.next()) {
                Category category = new Category();
                category.setCategoryId(rs.getInt("CategoryID"));
                category.setName(rs.getString("Name"));
                category.setType(rs.getString("Type"));
                category.setDescription(rs.getString("Description"));
                list.add(category);
            }

        } catch (SQLException ex) {
            Logger.getLogger(CategoryDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public List<Category> getByPageAndType(int page, int pageSize, String type) {
        List<Category> list = new ArrayList<>();
        String sql = "SELECT * FROM Category "
                + (type != null && !type.isEmpty() ? "WHERE Type = ? " : "")
                + "ORDER BY CategoryID "
                + "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            int index = 1;
            if (type != null && !type.isEmpty()) {
                stm.setString(index++, type);
            }
            stm.setInt(index++, (page - 1) * pageSize);
            stm.setInt(index, pageSize);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Category cat = new Category();
                cat.setCategoryId(rs.getInt("CategoryID"));
                cat.setName(rs.getString("Name"));
                cat.setType(rs.getString("Type"));
                cat.setDescription(rs.getString("Description"));
                list.add(cat);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CategoryDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

// Lấy tổng số category (để tính tổng trang)
    public int count(String type) {
        String sql = "SELECT COUNT(*) FROM Category "
                + (type != null && !type.isEmpty() ? "WHERE Type = ?" : "");
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            if (type != null && !type.isEmpty()) {
                stm.setString(1, type);
            }
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CategoryDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    // READ: Lấy Category theo ID
    public Category getById(int id) {
        String sql = "SELECT * FROM Category WHERE CategoryID = ?";
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                Category category = new Category();
                category.setCategoryId(rs.getInt("CategoryID"));
                category.setName(rs.getString("Name"));
                category.setType(rs.getString("Type"));
                category.setDescription(rs.getString("Description"));
                return category;
            }
        } catch (SQLException ex) {
            Logger.getLogger(CategoryDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    // UPDATE: Cập nhật Category
    public boolean update(Category category) {
        String sql = "UPDATE Category SET Name = ?, Type = ?, Description = ? WHERE CategoryID = ?";
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setString(1, category.getName());
            stm.setString(2, category.getType());
            stm.setString(3, category.getDescription());
            stm.setInt(4, category.getCategoryId());
            int rows = stm.executeUpdate();
            return rows > 0;
        } catch (SQLException ex) {
            Logger.getLogger(CategoryDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    // DELETE: Xóa Category
    public boolean delete(int id) {
        String sql = "DELETE FROM Category WHERE CategoryID = ?";
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, id);
            int rows = stm.executeUpdate();
            return rows > 0;
        } catch (SQLException ex) {
            Logger.getLogger(CategoryDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    public List<Category> getByType(String type) {
        List<Category> list = new ArrayList<>();
        String sql = "SELECT * FROM Category WHERE Type = ?";

        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setString(1, type);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                Category cat = new Category();
                cat.setCategoryId(rs.getInt("CategoryID"));
                cat.setName(rs.getString("Name"));
                cat.setType(rs.getString("Type"));
                cat.setDescription(rs.getString("Description"));

                list.add(cat);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CategoryDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return list;
    }

    // ✅ Kiểm tra Name + Type trùng (khi Add)
    public boolean isNameAndTypeExists(String name, String type) {
        String sql = "SELECT COUNT(1) FROM Category WHERE Name = ? AND Type = ?";
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setString(1, name);
            stm.setString(2, type);
            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(CategoryDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    // ✅ Kiểm tra Name + Type trùng (trừ chính nó - khi Update)
    public boolean isNameAndTypeExistsExcept(String name, String type, int categoryId) {
        String sql = "SELECT COUNT(1) FROM Category WHERE Name = ? AND Type = ? AND CategoryID <> ?";
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setString(1, name);
            stm.setString(2, type);
            stm.setInt(3, categoryId);
            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(CategoryDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    // ✅ Kiểm tra Category có đang được sử dụng không (trong Product, MaintenancePackageDetail, etc.)
    public boolean isCategoryInUse(int categoryId) {
        String sql = "SELECT COUNT(1) FROM Product WHERE CategoryID = ? " +
                     "UNION ALL SELECT COUNT(1) FROM MaintenancePackageDetail WHERE CategoryID = ?";
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, categoryId);
            stm.setInt(2, categoryId);
            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    if (rs.getInt(1) > 0) {
                        return true;
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(CategoryDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

}
