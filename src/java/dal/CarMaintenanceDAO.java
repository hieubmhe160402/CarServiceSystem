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

}
