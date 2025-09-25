/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import Context.DBContext;
import java.util.List;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Role;
import model.User;

/**
 *
 * @author MinHeee
 */
public class UserDAO extends DBContext {

    public List<User> getAll() {
        List<User> list = new ArrayList<>();
        try {
            String sql = "SELECT u.UserID, u.UserCode, u.FullName, "
                    + "u.Email, u.Phone, u.Male, u.DateOfBirth, "
                    + "u.IsActive, r.RoleName "
                    + "FROM Users u "
                    + "JOIN Role r ON u.RoleID = r.RoleID "
                    + "WHERE r.RoleName <> 'Admin'";
            PreparedStatement stm = connection.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                User u = new User();
                u.setUserId(rs.getInt("UserID"));
                u.setUserCode(rs.getString("UserCode"));
                u.setFullName(rs.getString("FullName"));
                u.setEmail(rs.getString("Email"));
                u.setPhone(rs.getString("Phone"));
                u.setMale(rs.getBoolean("Male"));
                Date dob = rs.getDate("dateOfBirth");
                u.setDateOfBirth(dob != null ? dob.toString() : null);
                u.setIsActive(rs.getBoolean("IsActive"));

                // Thêm Role
                Role r = new Role();

                r.setRoleName(rs.getString("RoleName"));
                u.setRole(r);

                list.add(u);
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public User UpdateEmployees(User u) {
        try {
            String sql = "UPDATE Users "
                    + "SET userCode = ?, "
                    + "    fullName = ?, "
                    + "    email = ?, "
                    + "    phone = ?, "
                    + "    male = ?, "
                    + "    dateOfBirth = ?, "
                    + "    isActive = ?, "
                    + "    roleID = ? "
                    + "WHERE userId = ?";
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setString(1, u.getUserCode());
            stm.setString(2, u.getFullName());
            stm.setString(3, u.getEmail());
            stm.setString(4, u.getPhone());
            stm.setBoolean(5, u.getMale());
            stm.setString(6, u.getDateOfBirth());
            stm.setBoolean(7, u.isIsActive());
            stm.setInt(8, u.getRole().getRoleID());
            stm.setInt(9, u.getUserId());

            stm.executeUpdate();

        } catch (Exception ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public void deleteUser(int userId) {
        try {
            String sql = "DELETE From Users WHERE UserID= ? ";
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, userId);
            stm.executeUpdate();
        } catch (Exception ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);

        }
    }

    public void insert(User user) {
        try {
            String sql = "INSERT INTO [dbo].[Users] "
                    + "([UserCode], [FullName], [Username], [Password], [Email], "
                    + "[Phone], [Image], [Male], [DateOfBirth], [RoleID], [IsActive]) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setString(1, user.getUserCode());
            stm.setString(2, user.getFullName());
            stm.setString(3, user.getUserName());
            stm.setString(4, user.getPassword());
            stm.setString(5, user.getEmail());
            stm.setString(6, user.getPhone());
            stm.setString(7, user.getImage());
            stm.setBoolean(8, user.getMale() != null && user.getMale());
            stm.setString(9, user.getDateOfBirth());
            stm.setInt(10, user.getRole().getRoleID());
            stm.setBoolean(11, user.isIsActive());
            stm.executeUpdate();
        } catch (Exception e) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public boolean isEmailExist(String email) {
        try {
            String sql = "SELECT 1 FROM Users WHERE Email = ?";
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setString(1, email);
            ResultSet rs = stm.executeQuery();
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean existsByUserCode(String userCode) {
        String sql = "SELECT UserCode FROM Users WHERE UserCode = ?";
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setString(1, userCode);
            ResultSet rs = stm.executeQuery();
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean existsByUsername(String username) {
        String sql = "SELECT Username FROM Users WHERE Username = ?";
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setString(1, username);
            ResultSet rs = stm.executeQuery();
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
