/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import Context.DBContext;
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
}
