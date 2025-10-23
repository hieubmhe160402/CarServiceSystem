/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.sql.*;

import context.DBContext;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import model.Appointment;
import model.Car;
import model.User;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.MaintenancePackage;

/**
 *
 * @author MinHeee
 */
public class AppointmentDAO extends DBContext {

    public List<Appointment> getAllAppointments() {
        List<Appointment> list = new ArrayList<>();
        try {
            String sql = """
            SELECT 
                a.AppointmentID,
                a.CarID,
                a.AppointmentDate,
                a.RequestedPackageID,
                a.Status,
                a.Notes,
                a.CreatedBy,
                a.CreatedDate,
                a.ConfirmedBy,
                a.ConfirmedDate,
                c.Brand,
                c.Model,
                c.LicensePlate,
                u.FullName AS CreatedByName,
                mp.PackageCode,
                mp.Name AS PackageName
            FROM Appointments a
            JOIN Cars c ON a.CarID = c.CarID
            JOIN Users u ON a.CreatedBy = u.UserID
            LEFT JOIN MaintenancePackage mp ON a.RequestedPackageID = mp.PackageID
            ORDER BY a.AppointmentDate 
        """;

            PreparedStatement stm = connection.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                Appointment a = new Appointment();
                a.setAppointmentId(rs.getInt("AppointmentID"));
                a.setAppointmentDate(rs.getString("AppointmentDate"));
                a.setStatus(rs.getString("Status"));
                a.setNotes(rs.getString("Notes"));
                a.setCreatedDate(rs.getString("CreatedDate"));
                a.setConfirmedDate(rs.getString("ConfirmedDate"));

                // Car
                Car car = new Car();
                car.setCarId(rs.getInt("CarID"));
                car.setBrand(rs.getString("Brand"));
                car.setModel(rs.getString("Model"));
                car.setLicensePlate(rs.getString("LicensePlate"));
                a.setCar(car);

                // CreatedBy
                User createdBy = new User();
                createdBy.setUserId(rs.getInt("CreatedBy"));
                createdBy.setFullName(rs.getString("CreatedByName"));
                a.setCreatedBy(createdBy);

                // Package
                MaintenancePackage pkg = new MaintenancePackage();
                pkg.setPackageId(rs.getInt("RequestedPackageID"));
                pkg.setPackageCode(rs.getString("PackageCode"));
                pkg.setName(rs.getString("PackageName"));
                a.setRequestedPackage(pkg);

                list.add(a);
            }

        } catch (SQLException ex) {
            Logger.getLogger(AppointmentDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return list;
    }

    public void confirmAppointment(int appointmentId, User confirmedBy) {
        String sql = """
        UPDATE Appointments
        SET Status = 'CONFIRMED',
            ConfirmedBy = ?, 
            ConfirmedDate = GETDATE()
        WHERE AppointmentID = ?
    """;

        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, confirmedBy.getUserId());
            stm.setInt(2, appointmentId);
            stm.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(AppointmentDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<Appointment> getAppointmentsByStatus(String status) {
        List<Appointment> list = new ArrayList<>();
        try {
            String sql = """
            SELECT 
                a.AppointmentID,
                a.CarID,
                a.AppointmentDate,
                a.RequestedPackageID,
                a.Status,
                a.Notes,
                a.CreatedBy,
                a.CreatedDate,
                a.ConfirmedBy,
                a.ConfirmedDate,
                c.Brand,
                c.Model,
                c.LicensePlate,
                u.FullName AS CreatedByName,
                mp.PackageCode,
                mp.Name AS PackageName
            FROM Appointments a
            JOIN Cars c ON a.CarID = c.CarID
            JOIN Users u ON a.CreatedBy = u.UserID
            LEFT JOIN MaintenancePackage mp ON a.RequestedPackageID = mp.PackageID
        """;

            if (status != null && !status.isEmpty()) {
                sql += " WHERE a.Status = ?";
            }

            sql += " ORDER BY a.AppointmentDate DESC";

            PreparedStatement stm = connection.prepareStatement(sql);

            if (status != null && !status.isEmpty()) {
                stm.setString(1, status);
            }

            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                Appointment a = new Appointment();
                a.setAppointmentId(rs.getInt("AppointmentID"));
                a.setAppointmentDate(rs.getString("AppointmentDate"));
                a.setStatus(rs.getString("Status"));
                a.setNotes(rs.getString("Notes"));
                a.setCreatedDate(rs.getString("CreatedDate"));
                a.setConfirmedDate(rs.getString("ConfirmedDate"));

                // Car
                Car car = new Car();
                car.setCarId(rs.getInt("CarID"));
                car.setBrand(rs.getString("Brand"));
                car.setModel(rs.getString("Model"));
                car.setLicensePlate(rs.getString("LicensePlate"));
                a.setCar(car);

                // CreatedBy
                User createdBy = new User();
                createdBy.setUserId(rs.getInt("CreatedBy"));
                createdBy.setFullName(rs.getString("CreatedByName"));
                a.setCreatedBy(createdBy);

                // Package
                MaintenancePackage pkg = new MaintenancePackage();
                pkg.setPackageId(rs.getInt("RequestedPackageID"));
                pkg.setPackageCode(rs.getString("PackageCode"));
                pkg.setName(rs.getString("PackageName"));
                a.setRequestedPackage(pkg);

                list.add(a);
            }

        } catch (SQLException ex) {
            Logger.getLogger(AppointmentDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return list;
    }

    public void cancelAppointment(int appointmentId) {
        try {
            String sql = """
            UPDATE Appointments
            SET Status = 'CANCELLED'
            WHERE AppointmentID = ?
        """;

            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, appointmentId);
            stm.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(AppointmentDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<Car> getAllCustomerCars() {
        List<Car> list = new ArrayList<>();
        try {
            String sql = """
            SELECT
                u.UserID,
                u.FullName,
                c.CarID,
                c.Brand,
                c.Model,
                c.LicensePlate
            FROM
                Users AS u
            INNER JOIN
                Cars AS c ON u.UserID = c.OwnerID
            WHERE
                u.RoleID = 4
            ORDER BY
                u.FullName, c.Brand, c.Model
        """;

            PreparedStatement stm = connection.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                Car car = new Car();
                car.setCarId(rs.getInt("CarID"));
                car.setBrand(rs.getString("Brand"));
                car.setModel(rs.getString("Model"));
                car.setLicensePlate(rs.getString("LicensePlate"));

                User owner = new User();
                owner.setUserId(rs.getInt("UserID"));
                owner.setFullName(rs.getString("FullName"));
                car.setOwner(owner);

                list.add(car);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return list;
    }

    public List<MaintenancePackage> getAllPackages() {
        List<MaintenancePackage> list = new ArrayList<>();
        try {
            String sql = """
             SELECT PackageID, PackageCode, Name, IsActive
             FROM MaintenancePackage
             WHERE IsActive = 1
             ORDER BY PackageCode
             """;
            PreparedStatement stm = connection.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                MaintenancePackage p = new MaintenancePackage();
                p.setPackageId(rs.getInt("PackageID"));
                p.setPackageCode(rs.getString("PackageCode"));
                p.setName(rs.getString("Name"));
                list.add(p);
            }

        } catch (SQLException ex) {
            Logger.getLogger(AppointmentDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public void addAppointment(Appointment appointment) {
        String sql = """
        INSERT INTO Appointments
        (CarID, AppointmentDate, RequestedPackageID, Status, Notes, CreatedBy, CreatedDate)
        VALUES (?, ?, ?, 'PENDING', ?, ?, GETDATE())
    """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, appointment.getCar().getCarId());
            ps.setString(2, appointment.getAppointmentDate().replace("T", " "));
            ps.setInt(3, appointment.getRequestedPackage().getPackageId());
            ps.setString(4, appointment.getNotes());

            // ✅ CreatedBy giờ sẽ là người đang đăng nhập (loginUser)
            ps.setInt(5, appointment.getCreatedBy().getUserId());

            int rows = ps.executeUpdate();
            System.out.println("✅ Appointment inserted successfully. Rows affected: " + rows);

        } catch (SQLException e) {
            System.out.println("❌ SQL ERROR (addAppointment): " + e.getMessage());
            e.printStackTrace();
        }
    }

    public boolean isAppointmentTimeConflict(int carId, LocalDateTime newAppointmentTime) {
        String sql = "SELECT COUNT(*) FROM Appointments "
                + "WHERE CarID = ? AND "
                + "ABS(DATEDIFF(MINUTE, AppointmentDate, ?)) < 60"; // kiểm tra trong 1 tiếng trùng

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, carId);
            ps.setTimestamp(2, Timestamp.valueOf(newAppointmentTime));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // nếu có bản ghi => trùng lịch
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isCarAlreadyBookedOnDate(int carId, LocalDateTime appointmentDateTime) {
        String sql = """
        SELECT COUNT(*) 
        FROM Appointments
        WHERE CarID = ? 
          AND CAST(AppointmentDate AS DATE) = CAST(? AS DATE)
          AND Status NOT IN ('CANCELLED', 'COMPLETED')
    """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, carId);
            ps.setTimestamp(2, Timestamp.valueOf(appointmentDateTime));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // Có ít nhất 1 lịch cùng ngày
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Appointment> getAppointmentsFiltered(String status, Integer packageId) {
        List<Appointment> list = new ArrayList<>();
        try {
            String sql = """
            SELECT 
                a.AppointmentID,
                a.CarID,
                a.AppointmentDate,
                a.RequestedPackageID,
                a.Status,
                a.Notes,
                a.CreatedBy,
                a.CreatedDate,
                a.ConfirmedBy,
                a.ConfirmedDate,
                c.Brand,
                c.Model,
                c.LicensePlate,
                u.FullName AS CreatedByName,
                mp.PackageCode,
                mp.Name AS PackageName
            FROM Appointments a
            JOIN Cars c ON a.CarID = c.CarID
            JOIN Users u ON a.CreatedBy = u.UserID
            LEFT JOIN MaintenancePackage mp ON a.RequestedPackageID = mp.PackageID
            WHERE 1=1
        """;

            // ✅ thêm điều kiện động
            List<Object> params = new ArrayList<>();
            if (status != null && !status.isEmpty()) {
                sql += " AND a.Status = ?";
                params.add(status);
            }
            if (packageId != null) {
                sql += " AND a.RequestedPackageID = ?";
                params.add(packageId);
            }

            sql += " ORDER BY a.AppointmentDate DESC";

            PreparedStatement stm = connection.prepareStatement(sql);

            // set parameter theo thứ tự
            for (int i = 0; i < params.size(); i++) {
                stm.setObject(i + 1, params.get(i));
            }

            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                Appointment a = new Appointment();
                a.setAppointmentId(rs.getInt("AppointmentID"));
                a.setAppointmentDate(rs.getString("AppointmentDate"));
                a.setStatus(rs.getString("Status"));
                a.setNotes(rs.getString("Notes"));
                a.setCreatedDate(rs.getString("CreatedDate"));
                a.setConfirmedDate(rs.getString("ConfirmedDate"));

                // Car
                Car car = new Car();
                car.setCarId(rs.getInt("CarID"));
                car.setBrand(rs.getString("Brand"));
                car.setModel(rs.getString("Model"));
                car.setLicensePlate(rs.getString("LicensePlate"));
                a.setCar(car);

                // CreatedBy
                User createdBy = new User();
                createdBy.setUserId(rs.getInt("CreatedBy"));
                createdBy.setFullName(rs.getString("CreatedByName"));
                a.setCreatedBy(createdBy);

                // Package
                MaintenancePackage pkg = new MaintenancePackage();
                pkg.setPackageId(rs.getInt("RequestedPackageID"));
                pkg.setPackageCode(rs.getString("PackageCode"));
                pkg.setName(rs.getString("PackageName"));
                a.setRequestedPackage(pkg);

                list.add(a);
            }

        } catch (SQLException ex) {
            Logger.getLogger(AppointmentDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return list;
    }

    public Appointment getAppointmentById(int appointmentId) {
        Appointment appointment = null;
        String sql = """
        SELECT 
            a.AppointmentID, 
            u.FullName, 
            u.Email, 
            u.Phone, 
            u.DateOfBirth, 
            u.Male, 
            c.Brand, 
            c.Model, 
            a.Status, 
            a.Notes, 
            a.ConfirmedDate,
            a.AppointmentDate
        FROM Appointments a
        JOIN Cars c ON a.CarID = c.CarID
        JOIN Users u ON a.CreatedBy = u.UserID
        WHERE a.AppointmentID = ?
    """;

        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, appointmentId);
            ResultSet rs = stm.executeQuery();

            if (rs.next()) {
                appointment = new Appointment();
                appointment.setAppointmentId(rs.getInt("AppointmentID"));
                appointment.setStatus(rs.getString("Status"));
                appointment.setNotes(rs.getString("Notes"));
                appointment.setAppointmentDate(rs.getString("AppointmentDate"));
                appointment.setConfirmedDate(rs.getString("ConfirmedDate"));

                // ✅ User (CreatedBy)
                User user = new User();
                user.setFullName(rs.getString("FullName"));
                user.setEmail(rs.getString("Email"));
                user.setPhone(rs.getString("Phone"));
                user.setDateOfBirth(rs.getString("DateOfBirth"));
                user.setMale(rs.getBoolean("Male"));
                appointment.setCreatedBy(user);

                // ✅ Car
                Car car = new Car();
                car.setBrand(rs.getString("Brand"));
                car.setModel(rs.getString("Model"));
                appointment.setCar(car);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return appointment;
    }
    //split  page 

    public List<Appointment> getAppointmentsByPage(int page, int pageSize) {
        List<Appointment> list = new ArrayList<>();
        String sql = """
        SELECT 
            a.AppointmentID,
            a.CarID,
            a.AppointmentDate,
            a.RequestedPackageID,
            a.Status,
            a.Notes,
            a.CreatedBy,
            a.CreatedDate,
            a.ConfirmedBy,
            a.ConfirmedDate,
            c.Brand,
            c.Model,
            c.LicensePlate,
            u.FullName AS CreatedByName,
            mp.PackageCode,
            mp.Name AS PackageName
        FROM Appointments a
        JOIN Cars c ON a.CarID = c.CarID
        JOIN Users u ON a.CreatedBy = u.UserID
        LEFT JOIN MaintenancePackage mp ON a.RequestedPackageID = mp.PackageID
        ORDER BY a.AppointmentDate DESC
        OFFSET ? ROWS FETCH NEXT ? ROWS ONLY
    """;

        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, (page - 1) * pageSize);
            stm.setInt(2, pageSize);

            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Appointment a = new Appointment();
                a.setAppointmentId(rs.getInt("AppointmentID"));
                a.setAppointmentDate(rs.getString("AppointmentDate"));
                a.setStatus(rs.getString("Status"));
                a.setNotes(rs.getString("Notes"));
                a.setCreatedDate(rs.getString("CreatedDate"));
                a.setConfirmedDate(rs.getString("ConfirmedDate"));

                // Car
                Car car = new Car();
                car.setCarId(rs.getInt("CarID"));
                car.setBrand(rs.getString("Brand"));
                car.setModel(rs.getString("Model"));
                car.setLicensePlate(rs.getString("LicensePlate"));
                a.setCar(car);

                // CreatedBy
                User createdBy = new User();
                createdBy.setUserId(rs.getInt("CreatedBy"));
                createdBy.setFullName(rs.getString("CreatedByName"));
                a.setCreatedBy(createdBy);

                // Package
                MaintenancePackage pkg = new MaintenancePackage();
                pkg.setPackageId(rs.getInt("RequestedPackageID"));
                pkg.setPackageCode(rs.getString("PackageCode"));
                pkg.setName(rs.getString("PackageName"));
                a.setRequestedPackage(pkg);

                list.add(a);
            }

        } catch (SQLException ex) {
            Logger.getLogger(AppointmentDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return list;
    }

    //tinh tong so trang 
    public int getTotalAppointments() {
        String sql = "SELECT COUNT(*) FROM Appointments";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void confirmAppointmentAndCreateMaintenance(int appointmentId, User confirmedBy) {
        String updateSql = """
        UPDATE Appointments
        SET Status = 'CONFIRMED',
            ConfirmedBy = ?,
            ConfirmedDate = GETDATE()
        WHERE AppointmentID = ?
    """;

        // ✅ Insert CarMaintenance, có xử lý trường hợp có hoặc không có RequestedPackageID
        String insertMaintenanceSql = """
        INSERT INTO CarMaintenance
        (CarID, AppointmentID, MaintenanceDate, Odometer, Status,
         TotalAmount, DiscountAmount, Notes, CreatedBy, AssignedTechnicianID, CreatedDate)
        OUTPUT INSERTED.MaintenanceID
        SELECT
            a.CarID,
            a.AppointmentID,
            GETDATE(),             -- MaintenanceDate
            c.CurrentOdometer,     -- Lấy từ Cars
            'WAITING',             -- Mặc định
            ISNULL(mp.BasePrice, 0),                         -- ✅ TotalAmount
            ISNULL(mp.BasePrice - mp.FinalPrice, 0),         -- ✅ DiscountAmount
            a.Notes,
            a.ConfirmedBy,         -- Người xác nhận
            NULL,                  -- Technician (chưa gán)
            GETDATE()
        FROM Appointments a
        JOIN Cars c ON a.CarID = c.CarID
        LEFT JOIN MaintenancePackage mp ON a.RequestedPackageID = mp.PackageID  -- ✅ Nếu có gói bảo dưỡng thì lấy giá
        WHERE a.AppointmentID = ?
    """;

        // ✅ Nếu có RequestedPackageID → thêm MaintenancePackageUsage
        String insertPackageUsageSql = """
        INSERT INTO MaintenancePackageUsage (MaintenanceID, PackageID, AppliedPrice, DiscountAmount, AppliedDate)
        SELECT ?, a.RequestedPackageID, mp.FinalPrice, (mp.BasePrice - mp.FinalPrice), GETDATE()
        FROM Appointments a
        JOIN MaintenancePackage mp ON mp.PackageID = a.RequestedPackageID
        WHERE a.AppointmentID = ? AND a.RequestedPackageID IS NOT NULL
    """;

        try {
            connection.setAutoCommit(false);

            // 1️⃣ Cập nhật Appointment
            try (PreparedStatement psUpd = connection.prepareStatement(updateSql)) {
                psUpd.setInt(1, confirmedBy.getUserId());
                psUpd.setInt(2, appointmentId);
                psUpd.executeUpdate();
            }

            // 2️⃣ Thêm CarMaintenance
            Integer newMaintenanceId = null;
            try (PreparedStatement psIns = connection.prepareStatement(insertMaintenanceSql)) {
                psIns.setInt(1, appointmentId);
                try (ResultSet rs = psIns.executeQuery()) {
                    if (rs.next()) {
                        newMaintenanceId = rs.getInt(1);
                    }
                }
            }

            // 3️⃣ Nếu có combo → thêm MaintenancePackageUsage
            if (newMaintenanceId != null) {
                try (PreparedStatement psPkg = connection.prepareStatement(insertPackageUsageSql)) {
                    psPkg.setInt(1, newMaintenanceId);
                    psPkg.setInt(2, appointmentId);
                    psPkg.executeUpdate();
                }
            }

            connection.commit();
            System.out.println("✅ Appointment confirmed and CarMaintenance created. MaintenanceID=" + newMaintenanceId);

        } catch (SQLException ex) {
            try {
                connection.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            ex.printStackTrace();
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
