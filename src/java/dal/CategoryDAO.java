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
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setString(1, category.getName());
            stm.setString(2, category.getType());
            stm.setString(3, category.getDescription());
            int rows = stm.executeUpdate();
            return rows > 0;
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

}
