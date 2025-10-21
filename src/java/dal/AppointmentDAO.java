package dal;

import context.DBContext;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
    
    
    
    public boolean updateAppointment(Appointment a) {
    String sql = "UPDATE Appointments SET "
            + "CarID = ?, "
            + "AppointmentDate = ?, "
            + "RequestedServices = ?, "
            + "RequestedPackageID = ?, "
            + "Status = ?, "
            + "Notes = ?, "
            + "ConfirmedBy = ?, "
            + "ConfirmedDate = ? "
            + "WHERE AppointmentID = ?";

    try (PreparedStatement ps = connection.prepareStatement(sql)) {

        ps.setInt(1, a.getCar().getCarId());
        ps.setString(2, a.getAppointmentDate());
        ps.setString(3, a.getRequestedServices());
        ps.setInt(4, a.getRequestedPackage().getPackageId());
        ps.setString(5, a.getStatus());
        ps.setString(6, a.getNotes());
        ps.setInt(7, a.getConfirmedBy() != null ? a.getConfirmedBy().getUserId() : 0); // tránh null
        ps.setTimestamp(8, new Timestamp(System.currentTimeMillis()));
        ps.setInt(9, a.getAppointmentId());

        return ps.executeUpdate() > 0;
    } catch (Exception e) {
        e.printStackTrace();
    }
    return false;
}

    // Method 1: Đếm tổng số records (có filter)
    public int getTotalRecords(int userId, String dateFilter, String packageFilter) {
        int total = 0;
        StringBuilder sql = new StringBuilder(
                "SELECT COUNT(*) FROM appointments a "
                + "LEFT JOIN maintenance_packages mp ON a.package_id = mp.package_id "
                + "WHERE a.user_id = ?"
        );

        // Thêm điều kiện filter
        if (dateFilter != null && !dateFilter.isEmpty()) {
            sql.append(" AND a.appointment_date = ?");
        }
        if (packageFilter != null && !packageFilter.isEmpty()) {
            sql.append(" AND mp.name LIKE ?");
        }

        try (PreparedStatement ps = connection.prepareStatement((sql).toString())) {

            int paramIndex = 1;
            ps.setInt(paramIndex++, userId);

            if (dateFilter != null && !dateFilter.isEmpty()) {
                ps.setString(paramIndex++, dateFilter);
            }
            if (packageFilter != null && !packageFilter.isEmpty()) {
                ps.setString(paramIndex++, "%" + packageFilter + "%");
            }

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                total = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return total;
    }

// Method 2: Lấy danh sách với LIMIT và OFFSET
    public List<Appointment> getAppointmentsPaginated(
            int userId, String dateFilter, String packageFilter,
            int offset, int limit) {

        List<Appointment> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
                "SELECT a.*, c.*, mp.*, u.* "
                + "FROM appointments a "
                + "LEFT JOIN cars c ON a.car_id = c.car_id "
                + "LEFT JOIN maintenance_packages mp ON a.package_id = mp.package_id "
                + "LEFT JOIN users u ON a.created_by = u.user_id "
                + "WHERE a.user_id = ?"
        );

        // Thêm điều kiện filter
        if (dateFilter != null && !dateFilter.isEmpty()) {
            sql.append(" AND a.appointment_date = ?");
        }
        if (packageFilter != null && !packageFilter.isEmpty()) {
            sql.append(" AND mp.name LIKE ?");
        }

        sql.append(" ORDER BY a.appointment_date DESC LIMIT ? OFFSET ?");

        try (PreparedStatement ps = connection.prepareStatement((sql).toString())) {

            int paramIndex = 1;
            ps.setInt(paramIndex++, userId);

            if (dateFilter != null && !dateFilter.isEmpty()) {
                ps.setString(paramIndex++, dateFilter);
            }
            if (packageFilter != null && !packageFilter.isEmpty()) {
                ps.setString(paramIndex++, "%" + packageFilter + "%");
            }

            ps.setInt(paramIndex++, limit);
            ps.setInt(paramIndex++, offset);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Appointment a = new Appointment();
                // Map ResultSet sang Object (như code cũ của bạn)
                // ...
                list.add(a);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // 1️⃣ Đếm tổng số records (không filter)
    public int getTotalRecordsByUserId(int userId) {
        int total = 0;
        String sql = "SELECT COUNT(*) FROM appointments WHERE user_id = ?";

        try (PreparedStatement ps = connection.prepareStatement((sql).toString())) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                total = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return total;
    }

// 2️⃣ Đếm tổng số records (có filter)
    public int getTotalRecordsWithFilter(int userId, String dateFilter, String packageFilter) {
        int total = 0;
        StringBuilder sql = new StringBuilder(
                "SELECT COUNT(*) FROM appointments a "
                + "LEFT JOIN maintenance_packages mp ON a.package_id = mp.package_id "
                + "WHERE a.user_id = ?"
        );

        if (dateFilter != null && !dateFilter.isEmpty()) {
            sql.append(" AND a.appointment_date = ?");
        }
        if (packageFilter != null && !packageFilter.isEmpty()) {
            sql.append(" AND mp.name LIKE ?");
        }

        try (PreparedStatement ps = connection.prepareStatement((sql).toString())) {

            int paramIndex = 1;
            ps.setInt(paramIndex++, userId);

            if (dateFilter != null && !dateFilter.isEmpty()) {
                ps.setString(paramIndex++, dateFilter);
            }
            if (packageFilter != null && !packageFilter.isEmpty()) {
                ps.setString(paramIndex++, "%" + packageFilter + "%");
            }

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                total = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return total;
    }

// 3️⃣ Lấy danh sách với phân trang (không filter)
    public List<Appointment> getAppointmentsByUserIdPaginated(int userId, int offset, int limit) {
        List<Appointment> list = new ArrayList<>();
        String sql = "SELECT a.*, c.*, mp.*, u.* "
                + "FROM appointments a "
                + "LEFT JOIN cars c ON a.car_id = c.car_id "
                + "LEFT JOIN maintenance_packages mp ON a.package_id = mp.package_id "
                + "LEFT JOIN users u ON a.created_by = u.user_id "
                + "WHERE a.user_id = ? "
                + "ORDER BY a.appointment_date DESC "
                + "LIMIT ? OFFSET ?";

        try (PreparedStatement ps = connection.prepareStatement((sql).toString())) {

            ps.setInt(1, userId);
            ps.setInt(2, limit);
            ps.setInt(3, offset);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                // Tạo object Appointment
                Appointment a = new Appointment();
                a.setAppointmentId(rs.getInt("appointment_id"));
                a.setAppointmentDate(rs.getDate("appointment_date").toString());
                a.setStatus(rs.getString("status"));
                a.setRequestedServices(rs.getString("requested_services"));
                a.setNotes(rs.getString("notes"));
                a.setCreatedDate(rs.getTimestamp("created_date").toString());

                // Tạo object Car
                Car car = new Car();
                car.setCarId(rs.getInt("car_id"));
                car.setBrand(rs.getString("brand"));
                car.setModel(rs.getString("model"));
                car.setLicensePlate(rs.getString("license_plate"));
                car.setYear(rs.getInt("year"));
                car.setColor(rs.getString("color"));
                a.setCar(car);

                // Tạo object MaintenancePackage
                MaintenancePackage mp = new MaintenancePackage();
                mp.setPackageId(rs.getInt("package_id"));
                mp.setName(rs.getString("name"));
                mp.setPackageCode(rs.getString("package_code"));
                mp.setDescription(rs.getString("description"));
                mp.setKilometerMilestone(rs.getInt("kilometer_milestone"));
                mp.setMonthMilestone(rs.getInt("month_milestone"));
                mp.setEstimatedDurationHours(rs.getBigDecimal("estimated_duration_hours"));
                mp.setBasePrice(rs.getBigDecimal("base_price"));
                mp.setDiscountPercent(rs.getBigDecimal("discount_percent"));
                mp.setFinalPrice(rs.getBigDecimal("final_price"));
                a.setRequestedPackage(mp);

                // Tạo object User (người tạo)
                User createdBy = new User();
                createdBy.setUserId(rs.getInt("user_id"));
                createdBy.setFullName(rs.getString("full_name"));
                createdBy.setEmail(rs.getString("email"));
                createdBy.setPhone(rs.getString("phone"));
                a.setCreatedBy(createdBy);

                list.add(a);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<Appointment> getAppointmentsByFilterPaginated(
            int userId, String dateFilter, String packageFilter, int offset, int limit) {

        List<Appointment> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
                "SELECT a.*, c.*, mp.*, u.* "
                + "FROM appointments a "
                + "LEFT JOIN cars c ON a.car_id = c.car_id "
                + "LEFT JOIN maintenance_packages mp ON a.package_id = mp.package_id "
                + "LEFT JOIN users u ON a.created_by = u.user_id "
                + "WHERE a.user_id = ?"
        );

        if (dateFilter != null && !dateFilter.isEmpty()) {
            sql.append(" AND a.appointment_date = ?");
        }
        if (packageFilter != null && !packageFilter.isEmpty()) {
            sql.append(" AND mp.name LIKE ?");
        }

        sql.append(" ORDER BY a.appointment_date DESC LIMIT ? OFFSET ?");

        try (PreparedStatement ps = connection.prepareStatement((sql).toString())) {

            int paramIndex = 1;
            ps.setInt(paramIndex++, userId);

            if (dateFilter != null && !dateFilter.isEmpty()) {
                ps.setString(paramIndex++, dateFilter);
            }
            if (packageFilter != null && !packageFilter.isEmpty()) {
                ps.setString(paramIndex++, "%" + packageFilter + "%");
            }

            ps.setInt(paramIndex++, limit);
            ps.setInt(paramIndex++, offset);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                // Tạo object Appointment
                Appointment a = new Appointment();
                a.setAppointmentId(rs.getInt("appointment_id"));
                a.setAppointmentDate(rs.getDate("appointment_date").toString());
                a.setStatus(rs.getString("status"));
                a.setRequestedServices(rs.getString("requested_services"));
                a.setNotes(rs.getString("notes"));
                a.setCreatedDate(rs.getTimestamp("created_date").toString());

                // Tạo object Car
                Car car = new Car();
                car.setCarId(rs.getInt("car_id"));
                car.setBrand(rs.getString("brand"));
                car.setModel(rs.getString("model"));
                car.setLicensePlate(rs.getString("license_plate"));
                car.setYear(rs.getInt("year"));
                car.setColor(rs.getString("color"));
                a.setCar(car);

                // Tạo object MaintenancePackage
                MaintenancePackage mp = new MaintenancePackage();
                mp.setPackageId(rs.getInt("package_id"));
                mp.setName(rs.getString("name"));
                mp.setPackageCode(rs.getString("package_code"));
                mp.setDescription(rs.getString("description"));
                mp.setKilometerMilestone(rs.getInt("kilometer_milestone"));
                mp.setMonthMilestone(rs.getInt("month_milestone"));
                mp.setEstimatedDurationHours(rs.getBigDecimal("estimated_duration_hours"));
                mp.setBasePrice(rs.getBigDecimal("base_price"));
                mp.setDiscountPercent(rs.getBigDecimal("discount_percent"));
                mp.setFinalPrice(rs.getBigDecimal("final_price"));
                a.setRequestedPackage(mp);

                // Tạo object User (người tạo)
                User createdBy = new User();
                createdBy.setUserId(rs.getInt("user_id"));
                createdBy.setFullName(rs.getString("full_name"));
                createdBy.setEmail(rs.getString("email"));
                createdBy.setPhone(rs.getString("phone"));
                a.setCreatedBy(createdBy);

                list.add(a);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public Appointment getAppointmentDetailById(int appointmentId) {
        Appointment appointment = null;
        String sql = """
        SELECT 
            a.AppointmentID,
            a.AppointmentDate,
            a.RequestedServices,
            a.Status,
            a.Notes,
            a.CreatedDate,
            c.CarID, c.LicensePlate, c.Brand, c.Model, c.Year, c.Color,
            mp.PackageID, mp.PackageCode, mp.Name AS PackageName, 
            mp.Description, mp.KilometerMilestone, mp.MonthMilestone,
            mp.BasePrice, mp.DiscountPercent, mp.FinalPrice, 
            mp.EstimatedDurationHours, mp.ApplicableBrands, mp.Image,
            u.UserID, u.FullName, u.Email, u.Phone
        FROM Appointments a
        LEFT JOIN Cars c ON a.CarID = c.CarID
        LEFT JOIN MaintenancePackage mp ON a.RequestedPackageID = mp.PackageID
        LEFT JOIN Users u ON a.CreatedBy = u.UserID
        WHERE a.AppointmentID = ?
    """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, appointmentId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                appointment = new Appointment();
                appointment.setAppointmentId(rs.getInt("AppointmentID"));
                appointment.setAppointmentDate(rs.getString("AppointmentDate"));
                appointment.setRequestedServices(rs.getString("RequestedServices"));
                appointment.setStatus(rs.getString("Status"));
                appointment.setNotes(rs.getString("Notes"));
                appointment.setCreatedDate(rs.getString("CreatedDate"));

                // Load thông tin xe đầy đủ
                if (rs.getInt("CarID") != 0) {
                    Car car = new Car();
                    car.setCarId(rs.getInt("CarID"));
                    car.setLicensePlate(rs.getString("LicensePlate"));
                    car.setBrand(rs.getString("Brand"));
                    car.setModel(rs.getString("Model"));
                    car.setYear(rs.getInt("Year"));
                    car.setColor(rs.getString("Color"));
                    appointment.setCar(car);
                }

                // Load thông tin gói bảo dưỡng đầy đủ
                if (rs.getInt("PackageID") != 0) {
                    MaintenancePackage pkg = new MaintenancePackage();
                    pkg.setPackageId(rs.getInt("PackageID"));
                    pkg.setPackageCode(rs.getString("PackageCode"));
                    pkg.setName(rs.getString("PackageName"));
                    pkg.setDescription(rs.getString("Description"));
                    pkg.setKilometerMilestone(rs.getInt("KilometerMilestone"));
                    pkg.setMonthMilestone(rs.getInt("MonthMilestone"));
                    pkg.setBasePrice(rs.getBigDecimal("BasePrice"));
                    pkg.setDiscountPercent(rs.getBigDecimal("DiscountPercent"));
                    pkg.setFinalPrice(rs.getBigDecimal("FinalPrice"));
                    pkg.setEstimatedDurationHours(rs.getBigDecimal("EstimatedDurationHours"));
                    pkg.setApplicableBrands(rs.getString("ApplicableBrands"));
                    pkg.setImage(rs.getString("Image"));
                    appointment.setRequestedPackage(pkg);
                }

                // Load thông tin người tạo
                if (rs.getInt("UserID") != 0) {
                    User user = new User();
                    user.setUserId(rs.getInt("UserID"));
                    user.setFullName(rs.getString("FullName"));
                    user.setEmail(rs.getString("Email"));
                    user.setPhone(rs.getString("Phone"));
                    appointment.setCreatedBy(user);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return appointment;
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
