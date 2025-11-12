/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import context.DBContext;
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

    private User mapUser(ResultSet rs) throws SQLException {
        Role role = new Role();
        role.setRoleID(rs.getInt("RoleID"));
        role.setRoleName(rs.getString("RoleName"));
        role.setDescription(rs.getString("Description"));

        User u = new User();
        u.setUserId(rs.getInt("UserID"));
        u.setUserCode(rs.getString("UserCode"));
        u.setFullName(rs.getString("FullName"));
        u.setUserName(rs.getString("Username"));
        u.setEmail(rs.getString("Email"));
        u.setPhone(rs.getString("Phone"));
        u.setMale(rs.getBoolean("Male"));
        Date dob = rs.getDate("DateOfBirth");
        u.setDateOfBirth(dob != null ? dob.toString() : null);
        u.setIsActive(rs.getBoolean("IsActive"));
        u.setRole(role);
        return u;
    }

    public int countUsers(String keyword, Integer roleId) {
        StringBuilder sql = new StringBuilder("""
                SELECT COUNT(*)
                FROM Users u
                JOIN Role r ON u.RoleID = r.RoleID
                WHERE r.RoleName <> 'Admin'
                """);
        List<Object> params = new ArrayList<>();

        if (keyword != null && !keyword.isEmpty()) {
            sql.append(" AND (u.FullName LIKE ? OR u.Email LIKE ?)");
            String like = "%" + keyword + "%";
            params.add(like);
            params.add(like);
        }

        if (roleId != null) {
            sql.append(" AND u.RoleID = ?");
            params.add(roleId);
        }

        try (PreparedStatement stm = connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                stm.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public List<User> getUsersWithPaging(String keyword, Integer roleId, int offset, int pageSize) {
        List<User> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("""
                SELECT u.UserID, u.UserCode, u.FullName, u.Username,
                       u.Email, u.Phone, u.Male, u.DateOfBirth, u.IsActive,
                       r.RoleID, r.RoleName, r.Description
                FROM Users u
                JOIN Role r ON u.RoleID = r.RoleID
                WHERE r.RoleName <> 'Admin'
                """);
        List<Object> params = new ArrayList<>();

        if (keyword != null && !keyword.isEmpty()) {
            sql.append(" AND (u.FullName LIKE ? OR u.Email LIKE ?)");
            String like = "%" + keyword + "%";
            params.add(like);
            params.add(like);
        }

        if (roleId != null) {
            sql.append(" AND u.RoleID = ?");
            params.add(roleId);
        }

        sql.append(" ORDER BY u.UserID OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");
        params.add(offset);
        params.add(pageSize);

        try (PreparedStatement stm = connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                stm.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    list.add(mapUser(rs));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public User getUserByUsername(String username) {
        String sql = "SELECT * FROM Users WHERE Username = ?";
        try (Connection conn = connection; PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                User u = new User();
                u.setUserId(rs.getInt("UserID"));
                u.setUserName(rs.getString("Username"));
                u.setFullName(rs.getString("FullName"));
                u.setEmail(rs.getString("Email"));
                // ... các trường khác
                return u;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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
    // for filter in list employeess

    public List<User> getByRole(int roleId) {
        List<User> list = new ArrayList<>();
        try {
            String sql = "SELECT u.UserID, u.UserCode, u.FullName, "
                    + "u.Email, u.Phone, u.Male, u.DateOfBirth, "
                    + "u.IsActive, r.RoleID, r.RoleName "
                    + "FROM Users u "
                    + "JOIN Role r ON u.RoleID = r.RoleID "
                    + "WHERE u.RoleID = ?";

            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, roleId);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                User u = new User();
                u.setUserId(rs.getInt("UserID"));
                u.setUserCode(rs.getString("UserCode"));
                u.setFullName(rs.getString("FullName"));
                u.setEmail(rs.getString("Email"));
                u.setPhone(rs.getString("Phone"));
                u.setMale(rs.getBoolean("Male"));

                Date dob = rs.getDate("DateOfBirth");
                u.setDateOfBirth(dob != null ? dob.toString() : null);

                u.setIsActive(rs.getBoolean("IsActive"));

                // Role
                Role r = new Role();
                r.setRoleID(rs.getInt("RoleID"));
                r.setRoleName(rs.getString("RoleName"));
                u.setRole(r);

                list.add(u);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // get 1 user
    public List<User> searchByEmail(String keyword) {
        List<User> list = new ArrayList<>();
        String sql = "SELECT u.*, r.RoleID, r.RoleName, r.Description "
                + "FROM Users u "
                + "JOIN Role r ON u.RoleID = r.RoleID "
                + "WHERE u.email LIKE ?";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            String searchValue = "%" + keyword + "%";
            st.setString(1, searchValue);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Role role = new Role(
                        rs.getInt("RoleID"),
                        rs.getString("RoleName"),
                        rs.getString("Description")
                );

                User u = new User(
                        rs.getInt("UserID"),
                        rs.getString("UserCode"),
                        rs.getString("FullName"),
                        rs.getString("UserName"),
                        rs.getString("Password"),
                        rs.getString("Email"),
                        rs.getString("Phone"),
                        rs.getString("Image"),
                        rs.getBoolean("Male"),
                        rs.getString("DateOfBirth"),
                        role,
                        rs.getBoolean("IsActive"),
                        rs.getString("CreatedDate"),
                        rs.getString("LastLoginDate")
                );
                list.add(u);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void updateStatus(int userId, boolean isActive) {
        String sql = "UPDATE Users SET IsActive = ? WHERE UserID = ?";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setBoolean(1, isActive);
            st.setInt(2, userId);
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //============================================================= Part Truong Login =========//
    public User login(String userName, String password) {
        String sql = "SELECT u.*, r.RoleID, r.RoleName, r.Description "
                + "FROM Users u "
                + "LEFT JOIN Role r ON u.RoleID = r.RoleID "
                + "WHERE u.Username = ? AND u.Password = ? AND u.IsActive = 1";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, userName);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("UserID"));
                user.setUserCode(rs.getString("UserCode"));
                user.setFullName(rs.getString("FullName"));
                user.setUserName(rs.getString("Username"));
                user.setPassword(rs.getString("Password"));
                user.setEmail(rs.getString("Email"));
                user.setPhone(rs.getString("Phone"));
                user.setImage(rs.getString("Image"));
                user.setMale(rs.getBoolean("Male"));
                user.setDateOfBirth(rs.getString("DateOfBirth"));
                user.setIsActive(rs.getBoolean("IsActive"));
                user.setCreatedDate(rs.getString("CreatedDate"));
                user.setLastLoginDate(rs.getString("LastLoginDate"));

                // Set Role
                Role role = new Role();
                role.setRoleID(rs.getInt("RoleID"));
                role.setRoleName(rs.getString("RoleName"));
                role.setDescription(rs.getString("Description"));
                user.setRole(role);

                return user;
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public boolean changePassword(int userId, String oldPassword, String newPassword) {
        String sql = "UPDATE Users SET Password = ? WHERE UserID = ? AND Password = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, newPassword);
            ps.setInt(2, userId);
            ps.setString(3, oldPassword);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public User getUserByEmail(String email) {
        String sql = "SELECT u.*, r.RoleID, r.RoleName, r.Description "
                + "FROM Users u "
                + "LEFT JOIN Role r ON u.RoleID = r.RoleID "
                + "WHERE u.Email = ? AND u.IsActive = 1";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, email);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("UserID"));
                user.setUserCode(rs.getString("UserCode"));
                user.setFullName(rs.getString("FullName"));
                user.setUserName(rs.getString("Username"));
                user.setPassword(rs.getString("Password"));
                user.setEmail(rs.getString("Email"));
                user.setPhone(rs.getString("Phone"));
                user.setImage(rs.getString("Image"));
                user.setMale(rs.getBoolean("Male"));
                user.setDateOfBirth(rs.getString("DateOfBirth"));
                user.setIsActive(rs.getBoolean("IsActive"));
                user.setCreatedDate(rs.getString("CreatedDate"));
                user.setLastLoginDate(rs.getString("LastLoginDate"));

                // Set Role
                Role role = new Role();
                role.setRoleID(rs.getInt("RoleID"));
                role.setRoleName(rs.getString("RoleName"));
                role.setDescription(rs.getString("Description"));
                user.setRole(role);

                return user;
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public boolean updatePassword(int userId, String newPassword) {
        String sql = "UPDATE Users SET Password = ? WHERE UserID = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, newPassword);
            ps.setInt(2, userId);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public void updateLastLogin(int userId) {
        String sql = "UPDATE Users SET LastLoginDate = GETDATE() WHERE UserID = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean registerCustomer(String userCode, String fullName, String userName, String password, String email, String phone, Boolean male, String dateOfBirth) {
        String sql = "INSERT INTO Users (UserCode, FullName, Username, Password, Email, Phone, Male, DateOfBirth, RoleID, IsActive, CreatedDate) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, 3, 1, GETDATE())"; // RoleID = 3 for Customer

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, userCode);
            ps.setString(2, fullName);
            ps.setString(3, userName);
            ps.setString(4, password);
            ps.setString(5, email);
            ps.setString(6, phone);
            ps.setBoolean(7, male);
            ps.setString(8, dateOfBirth);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public boolean checkUsernameExists(String userName) {
        String sql = "SELECT COUNT(*) FROM Users WHERE Username = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, userName);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public boolean checkEmailExists(String email) {
        String sql = "SELECT COUNT(*) FROM Users WHERE Email = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, email);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public String generateUserCode() {
        String sql = "SELECT MAX(CAST(SUBSTRING(UserCode, 3, LEN(UserCode)) AS INT)) FROM Users WHERE UserCode LIKE 'CU%'";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int maxNumber = rs.getInt(1);
                return "CU" + String.format("%03d", maxNumber + 1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "CU001"; // Default first customer code
    }

    public User getUserById(int userId) {
        String sql = "SELECT u.*, r.RoleID, r.RoleName, r.Description "
                + "FROM Users u "
                + "JOIN Role r ON u.RoleID = r.RoleID "
                + "WHERE u.UserID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Role role = new Role(
                        rs.getInt("RoleID"),
                        rs.getString("RoleName"),
                        rs.getString("Description")
                );

                User u = new User(
                        rs.getInt("UserID"),
                        rs.getString("UserCode"),
                        rs.getString("FullName"),
                        rs.getString("Username"),
                        rs.getString("Password"),
                        rs.getString("Email"),
                        rs.getString("Phone"),
                        rs.getString("Image"),
                        rs.getBoolean("Male"),
                        rs.getString("DateOfBirth"),
                        role,
                        rs.getBoolean("IsActive"),
                        rs.getString("CreatedDate"),
                        rs.getString("LastLoginDate")
                );
                return u;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static void main(String[] args) {
    UserDAO userDao = new UserDAO();

    // 👉 Thay bằng username thật trong database
    String testUsername = "hungowners";

    User user = userDao.getUserByUsername(testUsername);

    if (user != null) {
        System.out.println("===== Thông tin người dùng =====");
        System.out.println("UserID: " + user.getUserId());
        System.out.println("Username: " + user.getUserName());
        System.out.println("Full Name: " + user.getFullName());
        System.out.println("Email: " + user.getEmail());
    } else {
        System.out.println("Không tìm thấy user có username = " + testUsername);
    }
}

}
