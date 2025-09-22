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
                u.setDateOfBirth(rs.getString("DateOfBirth"));
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

}
