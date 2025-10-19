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

    String sql = "SELECT a.*, mp.PackageID, mp.Name AS PackageName "
               + "FROM Appointments a "
               + "LEFT JOIN MaintenancePackage mp ON a.RequestedPackageID = mp.PackageID "
               + "WHERE a.CreatedBy = ?";

    if (dateFilter != null && !dateFilter.isEmpty()) {
        sql += " AND CAST(a.AppointmentDate AS DATE) = ?";
    }
    if (packageFilter != null && !packageFilter.isEmpty()) {
        sql += " AND mp.Name LIKE ?";
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

            // Nếu có gói bảo dưỡng
            if (rs.getInt("PackageID") != 0) {
                model.MaintenancePackage pkg = new model.MaintenancePackage();
                pkg.setPackageId(rs.getInt("PackageID"));
                pkg.setName(rs.getString("PackageName"));
                ap.setRequestedPackage(pkg);
            }

            list.add(ap);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return list;
}

    
public static void main(String[] args) {
    AppointmentDAO dao = new AppointmentDAO();

    // 🔹 Giả lập thông tin người dùng đang đăng nhập
    int userId = 13; // ID người dùng có sẵn trong bảng Users

    // 🔹 Bộ lọc (bạn có thể thay đổi để test)
    String dateFilter = "2025-10-20";     // hoặc để null nếu không muốn lọc theo ngày
    String packageFilter = "Bảo dưỡng";   // hoặc để null nếu không muốn lọc theo tên gói

    // 🔹 Gọi hàm lấy danh sách lịch hẹn
    List<Appointment> list = dao.getAppointmentsByFilter(userId, dateFilter, packageFilter);

    // 🔹 In kết quả ra console
    if (list.isEmpty()) {
        System.out.println("❌ Không tìm thấy lịch hẹn nào khớp với bộ lọc!");
    } else {
        System.out.println("✅ Danh sách lịch hẹn của UserID " + userId + ":");
        for (Appointment ap : list) {
            System.out.println("--------------------------------------");
            System.out.println("AppointmentID: " + ap.getAppointmentId());
            System.out.println("Ngày hẹn: " + ap.getAppointmentDate());
            System.out.println("Dịch vụ yêu cầu: " + ap.getRequestedServices());
            System.out.println("Trạng thái: " + ap.getStatus());
            System.out.println("Ghi chú: " + ap.getNotes());
        }
    }
}

}

