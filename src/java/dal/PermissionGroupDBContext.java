package dal;

import Context.DBContext;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.PermissionGroup;

public class PermissionGroupDBContext extends DBContext {

    public List<PermissionGroup> getAll() {
        List<PermissionGroup> list = new ArrayList<>();
        try {
            String sql = "SELECT * from PermissionGroup";
            PreparedStatement stm = connection.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                PermissionGroup pg = new PermissionGroup();
                pg.setGroupID(rs.getInt("GroupID"));
                pg.setGroupName(rs.getString("GroupName"));
                pg.setDescription(rs.getString("Description"));
                list.add(pg);
            }
        } catch (SQLException ex) {
            Logger.getLogger(PermissionGroupDBContext.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public void insert(PermissionGroup pg) {
        try {
            String sql = "INSERT INTO PermissionGroup (GroupName, Description) VALUES (?, ?)";
            PreparedStatement stm = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stm.setString(1, pg.getGroupName());
            stm.setString(2, pg.getDescription());
            stm.executeUpdate();

            // Lấy ID vừa tạo (nếu cần)
            ResultSet rs = stm.getGeneratedKeys();
            if (rs.next()) {
                pg.setGroupID(rs.getInt(1));
            }
        } catch (SQLException ex) {
            Logger.getLogger(PermissionGroupDBContext.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
