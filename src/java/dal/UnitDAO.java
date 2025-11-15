package dal;

import context.DBContext;
import java.util.ArrayList;
import java.util.List;
import model.Unit;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UnitDAO extends DBContext {

    public List<Unit> getAllUnits() {
        List<Unit> list = new ArrayList<>();
        String sql = "SELECT * FROM Unit";

        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                Unit unit = new Unit();
                unit.setUnitId(rs.getInt("UnitID"));
                unit.setName(rs.getString("Name"));
                unit.setType(rs.getString("Type"));
                unit.setDescription(rs.getString("Description"));

                list.add(unit);
            }
        } catch (SQLException ex) {
            Logger.getLogger(UnitDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return list;
    }

    public Unit getById(int id) {
        String sql = "SELECT UnitID, Name, Type, Description FROM Unit WHERE UnitID = ?";
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, id);
            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    Unit unit = new Unit();
                    unit.setUnitId(rs.getInt("UnitID"));
                    unit.setName(rs.getString("Name"));
                    unit.setType(rs.getString("Type"));
                    unit.setDescription(rs.getString("Description"));
                    return unit;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(UnitDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public boolean add(Unit unit) {
        String sql = "INSERT INTO Unit (Name, Type, Description) VALUES (?, ?, ?)";
        try (PreparedStatement stm = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stm.setString(1, unit.getName());
            stm.setString(2, unit.getType());
            stm.setString(3, unit.getDescription());
            int result = stm.executeUpdate();
            
            // Lấy ID được generate
            if (result > 0) {
                try (ResultSet rs = stm.getGeneratedKeys()) {
                    if (rs.next()) {
                        unit.setUnitId(rs.getInt(1));
                    }
                }
            }
            return result > 0;
        } catch (SQLException ex) {
            Logger.getLogger(UnitDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean update(Unit unit) {
        String sql = "UPDATE Unit SET Name = ?, Type = ?, Description = ? WHERE UnitID = ?";
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setString(1, unit.getName());
            stm.setString(2, unit.getType());
            stm.setString(3, unit.getDescription());
            stm.setInt(4, unit.getUnitId());
            return stm.executeUpdate() > 0;
        } catch (SQLException ex) {
            Logger.getLogger(UnitDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    // ✅ Kiểm tra Name + Type trùng (khi Add)
    public boolean isNameAndTypeExists(String name, String type) {
        String sql = "SELECT COUNT(1) FROM Unit WHERE Name = ? AND Type = ?";
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setString(1, name);
            stm.setString(2, type);
            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(UnitDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    // ✅ Kiểm tra Name + Type trùng (trừ chính nó - khi Update)
    public boolean isNameAndTypeExistsExcept(String name, String type, int unitId) {
        String sql = "SELECT COUNT(1) FROM Unit WHERE Name = ? AND Type = ? AND UnitID <> ?";
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setString(1, name);
            stm.setString(2, type);
            stm.setInt(3, unitId);
            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(UnitDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM Unit WHERE UnitID = ?";
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, id);
            return stm.executeUpdate() > 0;
        } catch (SQLException ex) {
            Logger.getLogger(UnitDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    // ✅ Kiểm tra Unit có đang được sử dụng không (trong Product, MaintenancePackageDetail, etc.)
    public boolean isUnitInUse(int unitId) {
        String sql = "SELECT COUNT(1) FROM Product WHERE UnitID = ? " +
                     "UNION ALL SELECT COUNT(1) FROM MaintenancePackageDetail WHERE UnitID = ? " +
                     "UNION ALL SELECT COUNT(1) FROM InventoryLot WHERE UnitID = ?";
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, unitId);
            stm.setInt(2, unitId);
            stm.setInt(3, unitId);
            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    if (rs.getInt(1) > 0) {
                        return true;
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(UnitDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public int count(String typeFilter) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(1) FROM Unit WHERE 1=1");
        if (typeFilter != null && !typeFilter.isEmpty()) {
            sql.append(" AND Type = ?");
        }
        try (PreparedStatement stm = connection.prepareStatement(sql.toString())) {
            if (typeFilter != null && !typeFilter.isEmpty()) {
                stm.setString(1, typeFilter);
            }
            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(UnitDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public List<Unit> getByPageAndType(int page, int pageSize, String typeFilter) {
        List<Unit> list = new ArrayList<>();
        int offset = (page - 1) * pageSize;
        StringBuilder sql = new StringBuilder("SELECT UnitID, Name, Type, Description FROM Unit WHERE 1=1");
        if (typeFilter != null && !typeFilter.isEmpty()) {
            sql.append(" AND Type = ?");
        }
        sql.append(" ORDER BY UnitID OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");

        try (PreparedStatement stm = connection.prepareStatement(sql.toString())) {
            int paramIndex = 1;
            if (typeFilter != null && !typeFilter.isEmpty()) {
                stm.setString(paramIndex++, typeFilter);
            }
            stm.setInt(paramIndex++, offset);
            stm.setInt(paramIndex, pageSize);

            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    Unit unit = new Unit();
                    unit.setUnitId(rs.getInt("UnitID"));
                    unit.setName(rs.getString("Name"));
                    unit.setType(rs.getString("Type"));
                    unit.setDescription(rs.getString("Description"));
                    list.add(unit);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(UnitDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }
}
