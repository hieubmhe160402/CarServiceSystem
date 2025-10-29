/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import context.DBContext;
import java.util.ArrayList;
import java.util.List;
import model.Supplier;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author MinHeee
 */
public class SupplierDAO extends DBContext {

    // Lấy tất cả Supplier
    public List<Supplier> getAllSuppliers() {
        List<Supplier> list = new ArrayList<>();
        String sql = "SELECT * FROM Supplier ORDER BY SupplierID DESC";
        try (PreparedStatement stm = connection.prepareStatement(sql); ResultSet rs = stm.executeQuery()) {
            while (rs.next()) {
                Supplier s = new Supplier(
                        rs.getInt("SupplierID"),
                        rs.getString("Name"),
                        rs.getString("Address"),
                        rs.getString("Phone"),
                        rs.getString("Email"),
                        rs.getString("Description"),
                        rs.getBoolean("IsActive"),
                        rs.getString("CreatedDate")
                );
                list.add(s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // Lấy Supplier theo ID
    public Supplier getSupplierById(int id) {
        String sql = "SELECT * FROM Supplier WHERE SupplierID = ?";
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                return new Supplier(
                        rs.getInt("SupplierID"),
                        rs.getString("Name"),
                        rs.getString("Address"),
                        rs.getString("Phone"),
                        rs.getString("Email"),
                        rs.getString("Description"),
                        rs.getBoolean("IsActive"),
                        rs.getString("CreatedDate")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Thêm mới Supplier
    public boolean addSupplier(Supplier s) {
        String sql = "INSERT INTO Supplier (Name, Address, Phone, Email, Description, IsActive, CreatedDate) VALUES (?, ?, ?, ?, ?, ?, GETDATE())";
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setString(1, s.getName());
            stm.setString(2, s.getAddress());
            stm.setString(3, s.getPhone());
            stm.setString(4, s.getEmail());
            stm.setString(5, s.getDescription());
            stm.setBoolean(6, s.isIsActive());
            return stm.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Cập nhật Supplier
    public boolean updateSupplier(Supplier s) {
        String sql = "UPDATE Supplier SET Name=?, Address=?, Phone=?, Email=?, Description=?, IsActive=? WHERE SupplierID=?";
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setString(1, s.getName());
            stm.setString(2, s.getAddress());
            stm.setString(3, s.getPhone());
            stm.setString(4, s.getEmail());
            stm.setString(5, s.getDescription());
            stm.setBoolean(6, s.isIsActive());
            stm.setInt(7, s.getSupplierId());
            return stm.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Supplier> getSuppliersByPage(int page, int pageSize) {
        List<Supplier> list = new ArrayList<>();
        String sql = "SELECT * FROM Supplier ORDER BY SupplierID OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, (page - 1) * pageSize);
            st.setInt(2, pageSize);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Supplier s = new Supplier();
                s.setSupplierId(rs.getInt("SupplierID"));
                s.setName(rs.getString("Name"));
                s.setAddress(rs.getString("Address"));
                s.setPhone(rs.getString("Phone"));
                s.setEmail(rs.getString("Email"));
                s.setDescription(rs.getString("Description"));
                s.setIsActive(rs.getBoolean("IsActive"));
                s.setCreatedDate(rs.getDate("CreatedDate") != null ? rs.getDate("CreatedDate").toString() : "");
                list.add(s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public int getTotalSuppliers() {
        String sql = "SELECT COUNT(*) FROM Supplier";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void updateStatus(int id, boolean status) {
        String sql = "UPDATE Supplier SET isActive = ? WHERE supplierId = ?";
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setBoolean(1, status);
            stm.setInt(2, id);
            stm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
