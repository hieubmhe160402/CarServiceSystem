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
import model.Product;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Unit;

/**
 *
 * @author MinHeee
 */
public class ProductDAO extends DBContext {

    public List<Product> getAll() {
        List<Product> list = new ArrayList<>();
        try {
            String sql = "SELECT p.ProductID, p.Code, p.Name AS ProductName, p.Type, p.Price, "
                    + "p.Description, p.Image, p.UnitID, p.CategoryID, "
                    + "p.WarrantyPeriodMonths, p.MinStockLevel, p.EstimatedDurationHours, "
                    + "p.IsActive, p.CreatedDate, "
                    + "c.CategoryID, c.Name AS CategoryName, c.Type AS CategoryType, c.Description AS CategoryDescription, "
                    + "u.UnitID, u.Name AS UnitName, u.Type AS UnitType, u.Description AS UnitDescription "
                    + "FROM Product p "
                    + "LEFT JOIN Category c ON p.CategoryID = c.CategoryID "
                    + "LEFT JOIN Unit u ON p.UnitID = u.UnitID "
                    + "ORDER BY p.ProductID";

            PreparedStatement stm = connection.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                // map Category
                Category cat = null;
                int catId = rs.getInt("CategoryID");
                if (catId > 0) {
                    cat = new Category(
                            rs.getInt("CategoryID"),
                            rs.getString("CategoryName"),
                            rs.getString("CategoryType"),
                            rs.getString("CategoryDescription")
                    );
                }

                // map Unit
                Unit unit = null;
                int unitId = rs.getInt("UnitID");
                if (unitId > 0) {
                    unit = new Unit(
                            rs.getInt("UnitID"),
                            rs.getString("UnitName"),
                            rs.getString("UnitType"),
                            rs.getString("UnitDescription")
                    );
                }

                // map Product
                Product p = new Product();
                p.setProductId(rs.getInt("ProductID"));
                p.setCode(rs.getString("Code"));
                p.setName(rs.getString("ProductName"));
                p.setType(rs.getString("Type"));
                p.setPrice(rs.getBigDecimal("Price"));
                p.setDescription(rs.getString("Description"));
                p.setImage(rs.getString("Image"));
                p.setUnit(unit);
                p.setCategory(cat);
                p.setWarrantyPeriodMonths(rs.getInt("WarrantyPeriodMonths"));
                p.setMinStockLevel(rs.getInt("MinStockLevel"));
                p.setEstimatedDurationHours(rs.getBigDecimal("EstimatedDurationHours"));
                p.setIsActive(rs.getBoolean("IsActive"));
                p.setCreatedDate(rs.getString("CreatedDate"));

                list.add(p);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public void insertProduct(Product p) {
        try {
            String sql = "INSERT INTO Product "
                    + "(Code, Name, Type, Price, Description, Image, "
                    + "CategoryID, UnitID, WarrantyPeriodMonths, MinStockLevel, "
                    + "EstimatedDurationHours, IsActive, CreatedDate) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, GETDATE())";

            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setString(1, p.getCode());
            stm.setString(2, p.getName());
            stm.setString(3, p.getType());
            stm.setBigDecimal(4, p.getPrice());
            stm.setString(5, p.getDescription());
            stm.setString(6, p.getImage());
            stm.setInt(7, p.getCategory() != null ? p.getCategory().getCategoryId() : 0);
            stm.setInt(8, p.getUnit() != null ? p.getUnit().getUnitId() : 0);
            stm.setInt(9, p.getWarrantyPeriodMonths());
            stm.setInt(10, p.getMinStockLevel());
            stm.setBigDecimal(11, p.getEstimatedDurationHours());
            stm.setBoolean(12, p.isIsActive());

            stm.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(ProductDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void delete(int id) {
        try {
            String sql = "DELETE FROM Product WHERE ProductID = ?";
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, id);
            stm.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(ProductDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Product getById(int id) {
        try {
            String sql = "SELECT p.ProductID, p.Code, p.Name AS ProductName, p.Type, p.Price, "
                    + "p.Description, p.Image, p.UnitID, p.CategoryID, "
                    + "p.WarrantyPeriodMonths, p.MinStockLevel, p.EstimatedDurationHours, "
                    + "p.IsActive, p.CreatedDate, "
                    + "c.CategoryID, c.Name AS CategoryName, c.Type AS CategoryType, c.Description AS CategoryDescription, "
                    + "u.UnitID, u.Name AS UnitName, u.Type AS UnitType, u.Description AS UnitDescription "
                    + "FROM Product p "
                    + "LEFT JOIN Category c ON p.CategoryID = c.CategoryID "
                    + "LEFT JOIN Unit u ON p.UnitID = u.UnitID "
                    + "WHERE p.ProductID = ?";
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                Category cat = null;
                int catId = rs.getInt("CategoryID");
                if (catId > 0) {
                    cat = new Category(
                            rs.getInt("CategoryID"),
                            rs.getString("CategoryName"),
                            rs.getString("CategoryType"),
                            rs.getString("CategoryDescription")
                    );
                }

                Unit unit = null;
                int unitId = rs.getInt("UnitID");
                if (unitId > 0) {
                    unit = new Unit(
                            rs.getInt("UnitID"),
                            rs.getString("UnitName"),
                            rs.getString("UnitType"),
                            rs.getString("UnitDescription")
                    );
                }

                Product p = new Product();
                p.setProductId(rs.getInt("ProductID"));
                p.setCode(rs.getString("Code"));
                p.setName(rs.getString("ProductName"));
                p.setType(rs.getString("Type"));
                p.setPrice(rs.getBigDecimal("Price"));
                p.setDescription(rs.getString("Description"));
                p.setImage(rs.getString("Image"));
                p.setUnit(unit);
                p.setCategory(cat);
                p.setWarrantyPeriodMonths(rs.getInt("WarrantyPeriodMonths"));
                p.setMinStockLevel(rs.getInt("MinStockLevel"));
                p.setEstimatedDurationHours(rs.getBigDecimal("EstimatedDurationHours"));
                p.setIsActive(rs.getBoolean("IsActive"));
                p.setCreatedDate(rs.getString("CreatedDate"));
                return p;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public void update(Product p) {
        try {
            String sql = "UPDATE Product SET Code=?, Name=?, Type=?, Price=?, Description=?, Image=?, "
                    + "CategoryID=?, UnitID=?, WarrantyPeriodMonths=?, MinStockLevel=?, EstimatedDurationHours=?, IsActive=? "
                    + "WHERE ProductID=?";
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setString(1, p.getCode());
            stm.setString(2, p.getName());
            stm.setString(3, p.getType());
            stm.setBigDecimal(4, p.getPrice());
            stm.setString(5, p.getDescription());
            stm.setString(6, p.getImage());
            stm.setInt(7, p.getCategory() != null ? p.getCategory().getCategoryId() : 0);
            stm.setInt(8, p.getUnit() != null ? p.getUnit().getUnitId() : 0);
            stm.setInt(9, p.getWarrantyPeriodMonths());
            stm.setInt(10, p.getMinStockLevel());
            stm.setBigDecimal(11, p.getEstimatedDurationHours());
            stm.setBoolean(12, p.isIsActive());
            stm.setInt(13, p.getProductId());

            stm.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(ProductDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}