/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.sql.*;
import context.DBContext;
import java.util.ArrayList;
import java.util.List;
import model.Appointment;
import model.Car;
import model.CarMaintenance;
import model.User;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 *
 * @author MinHeee
 */
public class CarMaintenanceDAO extends DBContext {

    public List<CarMaintenance> getAllCarMaintenances() {
        List<CarMaintenance> list = new ArrayList<>();
        try {
            String sql = """
                SELECT 
                    cm.MaintenanceID,
                    a.AppointmentID,
                    u.FullName AS CustomerName,
                    c.LicensePlate,
                    c.Brand,
                    c.Model,
                    CONVERT(VARCHAR(10), cm.MaintenanceDate, 103) AS MaintenanceDate,
                    cm.Status,
                    tech.FullName AS TechnicianName
                FROM CarMaintenance cm
                LEFT JOIN Appointments a ON cm.AppointmentID = a.AppointmentID
                LEFT JOIN Cars c ON cm.CarID = c.CarID
                LEFT JOIN Users u ON c.OwnerID = u.UserID           -- khách hàng
                LEFT JOIN Users tech ON cm.AssignedTechnicianID = tech.UserID  -- kỹ thuật viên
                ORDER BY cm.MaintenanceDate DESC
            """;

            PreparedStatement stm = connection.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                CarMaintenance cm = new CarMaintenance();
                cm.setMaintenanceId(rs.getInt("MaintenanceID"));
                cm.setStatus(rs.getString("Status"));
                cm.setMaintenanceDate(rs.getString("MaintenanceDate"));

                // Appointment
                Appointment appointment = new Appointment();
                appointment.setAppointmentId(rs.getInt("AppointmentID"));
                cm.setAppointment(appointment);

                // Car
                Car car = new Car();
                car.setLicensePlate(rs.getString("LicensePlate"));
                car.setBrand(rs.getString("Brand"));
                car.setModel(rs.getString("Model"));

                // Chủ xe (khách hàng)
                User owner = new User();
                owner.setFullName(rs.getString("CustomerName"));
                car.setOwner(owner);
                cm.setCar(car);

                // Kỹ thuật viên (assignedTechnician)
                User technician = new User();
                String techName = rs.getString("TechnicianName");
                if (techName == null || techName.trim().isEmpty()) {
                    technician.setFullName("None");
                } else {
                    technician.setFullName(techName);
                }
                cm.setAssignedTechnician(technician);

                list.add(cm);
            }

        } catch (SQLException ex) {
            Logger.getLogger(CarMaintenanceDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return list;
    }

    public void updateStatus(int maintenanceId, String newStatus) {
        String sql = "UPDATE CarMaintenance SET Status = ?, AssignedTechnicianID = NULL WHERE MaintenanceID = ?";
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setString(1, newStatus);
            stm.setInt(2, maintenanceId);
            stm.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(CarMaintenanceDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public CarMaintenance getDetailServiceMaintenanceById(int maintenanceId) {
        String sql = """
        SELECT 
            m.MaintenanceID,
            a.AppointmentID,
            m.MaintenanceDate,
            m.Odometer,
            m.Status,
            m.Notes,
            m.AssignedTechnicianID,
            m.CompletedDate,
            u.FullName,
            u.Phone,
            u.Email,
            CONCAT(c.Brand, ' ', c.Model, ' - ', c.Color) AS CarInfo
        FROM Appointments a
        LEFT JOIN CarMaintenance m ON a.AppointmentID = m.AppointmentID
        LEFT JOIN Cars c ON a.CarID = c.CarID
        LEFT JOIN Users u ON c.OwnerID = u.UserID
        WHERE m.MaintenanceID = ?
    """;

        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, maintenanceId);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                CarMaintenance cm = new CarMaintenance();
                cm.setMaintenanceId(rs.getInt("MaintenanceID"));
                cm.setMaintenanceDate(rs.getString("MaintenanceDate"));
                cm.setOdometer(rs.getInt("Odometer"));
                cm.setStatus(rs.getString("Status"));
                cm.setNotes(rs.getString("Notes"));
                cm.setCompletedDate(rs.getString("CompletedDate"));

                User tech = new User();
                tech.setUserId(rs.getInt("AssignedTechnicianID"));
                cm.setAssignedTechnician(tech);

                Appointment ap = new Appointment();
                ap.setAppointmentId(rs.getInt("AppointmentID"));
                cm.setAppointment(ap);

                Car car = new Car();
                car.setBrand(rs.getString("CarInfo"));
                cm.setCar(car);

                User owner = new User();
                owner.setFullName(rs.getString("FullName"));
                owner.setPhone(rs.getString("Phone"));
                owner.setEmail(rs.getString("Email"));
                car.setOwner(owner);

                return cm;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public List<User> getTechnicians() {
        List<User> list = new ArrayList<>();
        String sql = """
        SELECT 
            u.UserID,
            u.FullName,
            u.Email,
            u.Phone,
            u.IsActive,
            r.RoleID,
            r.RoleName
        FROM Users u
        INNER JOIN Role r ON u.RoleID = r.RoleID
        WHERE u.RoleID = 3
          AND u.IsActive = 1
        ORDER BY u.FullName ASC
    """;

        try (PreparedStatement stm = connection.prepareStatement(sql); ResultSet rs = stm.executeQuery()) {

            while (rs.next()) {
                User u = new User();
                u.setUserId(rs.getInt("UserID"));
                u.setFullName(rs.getString("FullName"));
                u.setEmail(rs.getString("Email"));
                u.setPhone(rs.getString("Phone"));
                u.setIsActive(rs.getBoolean("IsActive"));

                // Gán role cho user
                model.Role r = new model.Role();
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

}
