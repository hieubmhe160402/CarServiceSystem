/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import Context.DBContext;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Role;

/**
 *
 * @author MinHeee
 */
public class RoleDAO extends DBContext {

    // Lấy danh sách Role (loại bỏ Admin)
    public List<Role> getAllRole() {
        List<Role> list = new ArrayList<>();
        try {
            String sql = "SELECT * FROM Role WHERE RoleName <> 'Admin'";
            PreparedStatement stm = connection.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Role r = new Role();
                r.setRoleID(rs.getInt("RoleID"));
                r.setRoleName(rs.getString("RoleName"));
                r.setDescription(rs.getString("Description"));
                
                list.add(r);
            }
        } catch (SQLException ex) {
            Logger.getLogger(RoleDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    // Lấy toàn bộ role
    public List<Role> getAllRole1() {
        List<Role> list = new ArrayList<>();
        String sql = "SELECT * FROM Role";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Role r = new Role();
                r.setRoleID(rs.getInt("RoleID"));
                r.setRoleName(rs.getString("RoleName"));
                r.setDescription(rs.getString("Description"));
                
                list.add(r);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

   // Thêm role mới
public void insertRole(Role role) {
    String sql = "INSERT INTO Role (RoleName, Description) VALUES (?, ?)";
    try (PreparedStatement st = connection.prepareStatement(sql)) {
        st.setString(1, role.getRoleName());
        st.setString(2, role.getDescription());
        st.executeUpdate();
    } catch (Exception e) {
        e.printStackTrace();
    }
}


    // Cập nhật trạng thái (Active/Inactive)
    public void updateStatus(int roleId, boolean newStatus) {
        String sql = "UPDATE Role SET Status = ? WHERE RoleID = ?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setBoolean(1, newStatus);
            st.setInt(2, roleId);
            st.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Toggle status nhanh (đảo ngược trạng thái)
    public void toggleStatus(int roleId) {
        String sql = "UPDATE Role SET Status = CASE WHEN Status = 1 THEN 0 ELSE 1 END WHERE RoleID = ?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, roleId);
            st.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Xóa role
    public void deleteRole(int roleId) {
        String sql = "DELETE FROM Role WHERE RoleID = ?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, roleId);
            st.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
