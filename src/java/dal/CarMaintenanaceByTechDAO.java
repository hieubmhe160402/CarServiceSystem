/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import context.DBContext;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author nxtru
 */
public class CarMaintenanaceByTechDAO extends DBContext {

    public List<Map<String, Object>> getCarMaintenanceByTechnicianId(int technicianId) {
        List<Map<String, Object>> list = new ArrayList<>();
        String sql = """
            SELECT 
                cm.MaintenanceID,
                CONCAT(c.Brand, ' ', c.Model, ' - ', c.LicensePlate) AS CarInfo,
                mp.PackageCode,
                mp.Name AS PackageName,
                cm.Odometer,
                cm.Status,
                cm.Notes,
                cm.CreatedDate
            FROM CarMaintenance cm
            JOIN Users u 
                ON cm.AssignedTechnicianID = u.UserID
            JOIN Cars c 
                ON cm.CarID = c.CarID
            LEFT JOIN Appointments a 
                ON cm.AppointmentID = a.AppointmentID
            LEFT JOIN MaintenancePackage mp 
                ON a.RequestedPackageID = mp.PackageID
            WHERE 
                cm.AssignedTechnicianID = ?
                AND u.IsActive = 1
            ORDER BY cm.CreatedDate DESC
        """;

        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, technicianId);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                Map<String, Object> map = new HashMap<>();
                map.put("maintenanceID", rs.getInt("MaintenanceID"));
                map.put("carInfo", rs.getString("CarInfo"));

                String packageCode = rs.getString("PackageCode");
                map.put("packageCode", packageCode != null ? packageCode : "-");

                String packageName = rs.getString("PackageName");
                map.put("packageName", packageName != null ? packageName : "-");

                map.put("odometer", rs.getInt("Odometer"));
                map.put("status", rs.getString("Status"));

                String notes = rs.getString("Notes");
                map.put("notes", notes != null ? notes : "-");

                Timestamp createdDate = rs.getTimestamp("CreatedDate");
                map.put("createdDate", createdDate != null ? createdDate.toString() : "-");

                list.add(map);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CarMaintenanaceByTechDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return list;
    }

    public boolean updateStatus(int maintenanceId, String newStatus) {
        String sql;
        if ("COMPLETED".equals(newStatus) || "CANCELLED".equals(newStatus)) {
            // Khi COMPLETED hoặc CANCELLED thì cập nhật cả CompletedDate
            sql = "UPDATE CarMaintenance SET Status = ?, CompletedDate = GETDATE() WHERE MaintenanceID = ?";
        } else {
            // Các status khác thì chỉ update status
            sql = "UPDATE CarMaintenance SET Status = ?, CompletedDate = GETDATE() WHERE MaintenanceID = ?";
        }

        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setString(1, newStatus);
            stm.setInt(2, maintenanceId);
            int rowsAffected = stm.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException ex) {
            Logger.getLogger(CarMaintenanaceByTechDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
}
