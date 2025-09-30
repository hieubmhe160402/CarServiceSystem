package dal;

import Context.DBContext;
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
}
