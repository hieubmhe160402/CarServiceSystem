package dal;

import Context.DBContext;
import java.sql.*;
import java.util.*;
import model.Permission;
import model.PermissionGroup;

public class PermissionDAO extends DBContext {

    // Lấy toàn bộ Permission
    public List<Permission> getAllPermissions() {
        List<Permission> list = new ArrayList<>();
        String sql = "SELECT p.*, pg.GroupID, pg.GroupName, pg.Description as GroupDesc " +
                     "FROM Permission p " +
                     "LEFT JOIN PermissionGroup pg ON p.GroupID = pg.GroupID";
        try (PreparedStatement st = connection.prepareStatement(sql);
             ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
                PermissionGroup pg = new PermissionGroup(
                        rs.getInt("GroupID"),
                        rs.getString("GroupName"),
                        rs.getString("GroupDesc")
                );
                Permission p = new Permission(
                        rs.getInt("PermissionID"),
                        rs.getString("Name"),
                        rs.getString("URL"),
                        rs.getString("Description"),
                        pg
                );
                list.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Lấy Permission theo RoleID
    public List<Permission> getPermissionsByRole(int roleId) {
        List<Permission> list = new ArrayList<>();
        String sql = "SELECT p.*, pg.GroupID, pg.GroupName, pg.Description as GroupDesc " +
                     "FROM RolePermission rp " +
                     "INNER JOIN Permission p ON rp.PermissionID = p.PermissionID " +
                     "LEFT JOIN PermissionGroup pg ON p.GroupID = pg.GroupID " +
                     "WHERE rp.RoleID = ?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, roleId);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                PermissionGroup pg = new PermissionGroup(
                        rs.getInt("GroupID"),
                        rs.getString("GroupName"),
                        rs.getString("GroupDesc")
                );
                Permission p = new Permission(
                        rs.getInt("PermissionID"),
                        rs.getString("Name"),
                        rs.getString("URL"),
                        rs.getString("Description"),
                        pg
                );
                list.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void updateRolePermissions(int roleId, String[] permissionIds) {
        String deleteSql = "DELETE FROM RolePermission WHERE RoleID = ?";
        String insertSql = "INSERT INTO RolePermission (RoleID, PermissionID) VALUES (?, ?)";

        try {
            connection.setAutoCommit(false); // bắt đầu transaction

            // Xóa quyền cũ
            try (PreparedStatement st = connection.prepareStatement(deleteSql)) {
                st.setInt(1, roleId);
                st.executeUpdate();
            }

            // Thêm quyền mới
            if (permissionIds != null) {
                try (PreparedStatement st = connection.prepareStatement(insertSql)) {
                    for (String pid : permissionIds) {
                        st.setInt(1, roleId);
                        st.setInt(2, Integer.parseInt(pid));
                        st.addBatch();
                    }
                    st.executeBatch();
                }
            }

            connection.commit(); // xác nhận
        } catch (SQLException e) {
            try { connection.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            e.printStackTrace();
        } finally {
            try { connection.setAutoCommit(true); } catch (SQLException e) { e.printStackTrace(); }
        }
    }
}
