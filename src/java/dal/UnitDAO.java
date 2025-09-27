/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import context.DBContext;
import java.util.List;
import model.Unit;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author MinHeee
 */
public class UnitDAO extends DBContext {

    public List<Unit> getAll() {
        List<Unit> list = new ArrayList<>();
        try {
            String sql = "SELECT UnitID, Name, Type, Description FROM Unit ORDER BY UnitID";
            PreparedStatement stm = connection.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Unit u = new Unit();
                u.setUnitId(rs.getInt("UnitID"));
                u.setName(rs.getString("Name"));
                u.setType(rs.getString("Type"));
                u.setDescription(rs.getString("Description"));
                list.add(u);
            }
        } catch (SQLException ex) {
            Logger.getLogger(UnitDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public List<Unit> getAllUnits() {
        List<Unit> list = new ArrayList<>();
        try {
            String sql = "SELECT DISTINCT UnitID, Name FROM Unit";
            PreparedStatement stm = connection.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Unit u = new Unit();
                u.setUnitId(rs.getInt("UnitID"));
                u.setName(rs.getString("Name"));
                list.add(u);
            }
        } catch (SQLException ex) {
            Logger.getLogger(UnitDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public Unit getById(int id) {
        try {
            String sql = "SELECT UnitID, Name, Type, Description FROM Unit WHERE UnitID = ?";
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                return new Unit(
                        rs.getInt("UnitID"),
                        rs.getString("Name"),
                        rs.getString("Type"),
                        rs.getString("Description")
                );
            }
        } catch (SQLException ex) {
            Logger.getLogger(UnitDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public void insert(Unit unit) {
        try {
            String sql = "INSERT INTO Unit (Name, Type, Description) VALUES (?, ?, ?)";
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setString(1, unit.getName());
            stm.setString(2, unit.getType());
            stm.setString(3, unit.getDescription());
            stm.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(UnitDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void update(Unit unit) {
        try {
            String sql = "UPDATE Unit SET Name = ?, Type = ?, Description = ? WHERE UnitID = ?";
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setString(1, unit.getName());
            stm.setString(2, unit.getType());
            stm.setString(3, unit.getDescription());
            stm.setInt(4, unit.getUnitId());
            stm.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(UnitDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void delete(int id) {
        try {
            String sql = "DELETE FROM Unit WHERE UnitID = ?";
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, id);
            stm.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(UnitDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}