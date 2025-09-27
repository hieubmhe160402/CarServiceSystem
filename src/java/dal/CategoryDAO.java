/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import context.DBContext;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Category;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author MinHeee
 */
public class CategoryDAO extends DBContext {

    // Lấy tất cả Category
    public List<Category> getAll() {
        List<Category> list = new ArrayList<>();
        try {
            String sql = "SELECT CategoryID, Name, Type, Description FROM Category";
            PreparedStatement stm = connection.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Category c = new Category();
                c.setCategoryId(rs.getInt("CategoryID"));
                c.setName(rs.getString("Name"));
                c.setType(rs.getString("Type"));
                c.setDescription(rs.getString("Description"));
                list.add(c);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CategoryDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    // Tìm Category theo ID
    public Category findById(int id) {
        try {
            String sql = "SELECT CategoryID, Name, Type, Description FROM Category WHERE CategoryID = ?";
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                return new Category(
                        rs.getInt("CategoryID"),
                        rs.getString("Name"),
                        rs.getString("Type"),
                        rs.getString("Description")
                );
            }
        } catch (SQLException ex) {
            Logger.getLogger(CategoryDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    // Thêm mới Category
    public void insert(Category c) {
        try {
            String sql = "INSERT INTO Category (Name, Type, Description) VALUES (?, ?, ?)";
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setString(1, c.getName());
            stm.setString(2, c.getType());
            stm.setString(3, c.getDescription());
            stm.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(CategoryDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Cập nhật Category
    public void update(Category c) {
        try {
            String sql = "UPDATE Category SET Name = ?, Type = ?, Description = ? WHERE CategoryID = ?";
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setString(1, c.getName());
            stm.setString(2, c.getType());
            stm.setString(3, c.getDescription());
            stm.setInt(4, c.getCategoryId());
            stm.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(CategoryDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Category getById(int id) {
        try {
            String sql = "SELECT CategoryID, Name, Type, Description FROM Category WHERE CategoryID = ?";
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                return new Category(
                        rs.getInt("CategoryID"),
                        rs.getString("Name"),
                        rs.getString("Type"),
                        rs.getString("Description")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Xóa Category
    public void delete(int id) {
        try {
            String sql = "DELETE FROM Category WHERE CategoryID = ?";
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, id);
            stm.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(CategoryDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<String> getAllType() {
        List<String> list = new ArrayList<>();
        try {
            String sql = "SELECT DISTINCT Type FROM Category WHERE Type IN ('Service', 'Part')";
            PreparedStatement stm = connection.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                list.add(rs.getString("Type"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(CategoryDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public List<String> getAllNameByType(String type) {
        List<String> list = new ArrayList<>();
        try {
            String sql = "SELECT Name FROM Category WHERE Type = ?";
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setString(1, type);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                list.add(rs.getString("Name"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(CategoryDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    // Thêm mới: tìm theo tên để lấy CategoryID cho Add/Update Product
    public Category findByName(String name) {
        try {
            String sql = "SELECT CategoryID, Name, Type, Description FROM Category WHERE Name = ?";
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setString(1, name);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                return new Category(
                        rs.getInt("CategoryID"),
                        rs.getString("Name"),
                        rs.getString("Type"),
                        rs.getString("Description")
                );
            }
        } catch (SQLException ex) {
            Logger.getLogger(CategoryDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}