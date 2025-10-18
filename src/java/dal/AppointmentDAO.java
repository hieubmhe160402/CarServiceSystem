package dal;

import context.DBContext;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import model.Appointment;
import model.Car;
import model.MaintenancePackage;
import model.User;

public class AppointmentDAO extends DBContext {

    public boolean insertAppointment(Appointment a) {
        String sql = "INSERT INTO Appointments "
                + "(CarId, AppointmentDate, RequestedServices, Status, Notes, CreatedBy, CreatedDate, RequestedPackageID) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, a.getCar().getCarId());
            ps.setString(2, a.getAppointmentDate());
            ps.setString(3, a.getRequestedServices());
            ps.setString(4, a.getStatus());
            ps.setString(5, a.getNotes());
            ps.setInt(6, a.getCreatedBy().getUserId());
            ps.setTimestamp(7, new Timestamp(System.currentTimeMillis()));
            ps.setInt(8, a.getRequestedPackage().getPackageId());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public static void main(String[] args) {
        AppointmentDAO dao = new AppointmentDAO();

        // --- Tạo các đối tượng cần thiết ---
        // 1️⃣ Giả lập User tạo lịch hẹn
        User creator = new User();
        creator.setUserId(13); // 👈 ID user có sẵn trong bảng Users

        // 2️⃣ Giả lập xe của user
        Car car = new Car();
        car.setCarId(10); // 👈 ID xe có trong bảng Cars

        // 3️⃣ Giả lập gói bảo dưỡng
        MaintenancePackage pkg = new MaintenancePackage();
        pkg.setPackageId(10); // 👈 ID gói bảo dưỡng có sẵn trong MaintenancePackage

        // 4️⃣ Tạo đối tượng Appointment
        Appointment ap = new Appointment();
        ap.setCar(car);
        ap.setAppointmentDate("2025-10-20 10:30:00"); // 👈 Thời gian hẹn
        ap.setRequestedServices("Thay dầu, kiểm tra phanh"); // mô tả dịch vụ
        ap.setStatus("Pending"); // trạng thái
        ap.setNotes("Khách hàng muốn lấy xe trong ngày");
        ap.setCreatedBy(creator);
        ap.setRequestedPackage(pkg);

        // --- Gọi DAO để insert ---
        boolean success = dao.insertAppointment(ap);

        if (success) {
            System.out.println("✅ Thêm lịch hẹn thành công!");
        } else {
            System.out.println("❌ Thêm lịch hẹn thất bại!");
        }
    }
    
    public List<Appointment> getAppointmentsByUserId(int userId) {
        List<Appointment> list = new ArrayList<>();
        String sql = """
            SELECT 
                a.AppointmentID,
                a.AppointmentDate,
                a.RequestedServices,
                a.Status,
                a.Notes,
                a.CreatedDate,
                c.CarID, c.LicensePlate, c.Brand, c.Model,
                mp.PackageID, mp.Name AS PackageName, mp.FinalPrice
            FROM Appointments a
            INNER JOIN Cars c ON a.CarID = c.CarID
            LEFT JOIN MaintenancePackage mp ON a.RequestedPackageID = mp.PackageID
            WHERE c.OwnerID = ?
            ORDER BY a.AppointmentDate DESC
        """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Appointment ap = new Appointment();

                Car car = new Car();
                car.setCarId(rs.getInt("CarID"));
                car.setLicensePlate(rs.getString("LicensePlate"));
                car.setBrand(rs.getString("Brand"));
                car.setModel(rs.getString("Model"));

                MaintenancePackage pkg = new MaintenancePackage();
                pkg.setPackageId(rs.getInt("PackageID"));
                pkg.setName(rs.getString("PackageName"));
                pkg.setFinalPrice(rs.getBigDecimal("FinalPrice"));

                ap.setAppointmentId(rs.getInt("AppointmentID"));
                ap.setAppointmentDate(rs.getString("AppointmentDate"));
                ap.setRequestedServices(rs.getString("RequestedServices"));
                ap.setStatus(rs.getString("Status"));
                ap.setNotes(rs.getString("Notes"));
                ap.setCreatedDate(rs.getTimestamp("CreatedDate").toString());
                ap.setCar(car);
                ap.setRequestedPackage(pkg);

                list.add(ap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    
    public List<Appointment> getAppointmentsByFilter(int userId, String dateFilter, String packageFilter) {
    List<Appointment> list = new ArrayList<>();
    String sql = "SELECT * FROM Appointments WHERE CreatedBy = ?";

    if (dateFilter != null && !dateFilter.isEmpty()) {
        sql += " AND CAST(AppointmentDate AS DATE) = ?";
    }
    if (packageFilter != null && !packageFilter.isEmpty()) {
        sql += " AND RequestedPackage IN (SELECT PackageID FROM MaintenancePackage WHERE PackageName LIKE ?)";
    }

    try (PreparedStatement ps = connection.prepareStatement(sql)) {
        int index = 1;
        ps.setInt(index++, userId);
        if (dateFilter != null && !dateFilter.isEmpty()) {
            ps.setString(index++, dateFilter);
        }
        if (packageFilter != null && !packageFilter.isEmpty()) {
            ps.setString(index++, "%" + packageFilter + "%");
        }

        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Appointment ap = new Appointment();
            ap.setAppointmentId(rs.getInt("AppointmentID"));
            ap.setAppointmentDate(rs.getString("AppointmentDate"));
            ap.setRequestedServices(rs.getString("RequestedServices"));
            ap.setStatus(rs.getString("Status"));
            ap.setNotes(rs.getString("Notes"));
            list.add(ap);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return list;
}

}
