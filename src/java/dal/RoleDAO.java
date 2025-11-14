/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import context.DBContext;
import java.sql.*;
import java.sql.SQLException;
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
    //List All Roles 

    public List<Role> getAllRole() {
        List<Role> list = new ArrayList<>();
        try {
            String sql = "SELECT * FROM Role WHERE RoleName <> 'Admin'";
            PreparedStatement stm = connection.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Role r = new Role();
                r.setRoleID(rs.getInt("roleID"));
                r.setRoleName(rs.getString("roleName"));
                r.setDescription(rs.getString("description"));
                list.add(r);
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    //  CREATE
    public boolean insertRole(Role role) {
        String sql = "INSERT INTO Role (RoleName, Description) VALUES (?, ?)";
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setString(1, role.getRoleName());
            stm.setString(2, role.getDescription());
            return stm.executeUpdate() > 0;
        } catch (SQLException ex) {
            Logger.getLogger(RoleDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    //  READ (All)
    public List<Role> getAllRolesGara() {
        List<Role> list = new ArrayList<>();
        String sql = "SELECT * FROM Role";
        try (PreparedStatement stm = connection.prepareStatement(sql); ResultSet rs = stm.executeQuery()) {

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

    //  READ (By ID)
    public Role getRoleById(int id) {
        String sql = "SELECT * FROM Role WHERE RoleID = ?";
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                Role r = new Role();
                r.setRoleID(rs.getInt("RoleID"));
                r.setRoleName(rs.getString("RoleName"));
                r.setDescription(rs.getString("Description"));
                return r;
            }
        } catch (SQLException ex) {
            Logger.getLogger(RoleDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    //  UPDATE
    public boolean updateRole(Role role) {
        String sql = "UPDATE Role SET RoleName = ?, Description = ? WHERE RoleID = ?";
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setString(1, role.getRoleName());
            stm.setString(2, role.getDescription());
            stm.setInt(3, role.getRoleID());
            return stm.executeUpdate() > 0;
        } catch (SQLException ex) {
            Logger.getLogger(RoleDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    //  SEARCH
    public List<Role> searchRoles(String keyword) {
        List<Role> list = new ArrayList<>();
        String sql = "SELECT * FROM Role WHERE RoleName LIKE ? OR Description LIKE ?";
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            String kw = "%" + keyword + "%";
            stm.setString(1, kw);
            stm.setString(2, kw);
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
    
         public int getTotalRoles() {
        String sql = "SELECT COUNT(*) FROM Role WHERE RoleName <> 'Admin'";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(RoleDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    //  Lấy danh sách Role theo trang (offset, limit)
    public List<Role> getRolesByPage(int start, int total) {
        List<Role> list = new ArrayList<>();
        String sql = "SELECT * FROM Role WHERE RoleName <> 'Admin' ORDER BY RoleID OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, start);
            ps.setInt(2, total);
            ResultSet rs = ps.executeQuery();
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
    public static void main(String[] args) {
        RoleDAO dao = new RoleDAO();
        List<Role> roles = dao.getAllRole();

        if (roles.isEmpty()) {
            System.out.println("Không có dữ liệu hoặc lỗi truy vấn!");
        } else {
            for (Role r : roles) {
                System.out.println("ID: " + r.getRoleID()
                        + ", Name: " + r.getRoleName()
                        + ", Description: " + r.getDescription());
            }
        }
    }
}
