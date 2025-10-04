package dal;

import Context.DBContext;
import java.sql.*;
import java.util.*;
import model.Permission;

public class RolePermissionDAO extends DBContext {

    // Lấy danh sách quyền của 1 role
    public List<Permission> getPermissionsByRole(int roleId) {
        List<Permission> list = new ArrayList<>();
        String sql = "SELECT p.PermissionID, p.Name, p.URL, p.Description " +
                     "FROM RolePermission rp " +
                     "JOIN Permission p ON rp.PermissionID = p.PermissionID " +
                     "WHERE rp.RoleID = ?";

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, roleId);
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    Permission p = new Permission(
                            rs.getInt("PermissionID"),
                            rs.getString("Name"),
                            rs.getString("URL"),
                            rs.getString("Description"),
                            null
                    );
                    list.add(p);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Xóa hết quyền của 1 role
    public void deletePermissionsByRole(int roleId) {
        String sql = "DELETE FROM RolePermission WHERE RoleID = ?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, roleId);
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Thêm 1 quyền cho role
    public void addPermissionToRole(int roleId, int permissionId) {
        String sql = "INSERT INTO RolePermission (RoleID, PermissionID) VALUES (?, ?)";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, roleId);
            st.setInt(2, permissionId);
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
