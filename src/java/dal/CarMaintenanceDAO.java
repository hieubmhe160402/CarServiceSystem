/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import context.DBContext;
import model.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO xử lý dữ liệu liên quan đến CarMaintenance và các bảng liên quan
 */
public class CarMaintenanceDAO extends DBContext {

    /**
     * Lấy danh sách maintenance theo UserID (cho người dùng đã đăng nhập)
     */
    public List<CarMaintenance> getMaintenanceByUserId(int userId) throws SQLException {
        List<CarMaintenance> list = new ArrayList<>();
        String sql = "SELECT " +
                "cm.MaintenanceID, cm.CarID, cm.AppointmentID, cm.MaintenanceDate, " +
                "cm.Odometer, cm.Status as MaintenanceStatus, cm.TotalAmount, " +
                "cm.DiscountAmount, cm.FinalAmount, cm.Notes, cm.CreatedBy as MaintenanceCreatedBy, " +
                "cm.AssignedTechnicianID, cm.CreatedDate as MaintenanceCreatedDate, cm.CompletedDate, " +
                "c.LicensePlate, c.Brand, c.Model, c.Year, c.Color, c.OwnerID, " +
                "a.AppointmentDate, a.Status as AppointmentStatus, a.RequestedPackageID, " +
                "a.CreatedBy as AppointmentCreatedBy, a.RequestedServices as AppointmentNotes, " +
                "owner.UserID as OwnerUserID, owner.FullName as OwnerName, owner.Phone as OwnerPhone, owner.Email as OwnerEmail, " +
                "creator.UserID as CreatorUserID, creator.FullName as CreatedByName, creator.Phone as CreatedByPhone, creator.Email as CreatedByEmail, " +
                "tech.UserID as TechUserID, tech.FullName as TechnicianName, " +
                "mp.PackageID, mp.Name as PackageName " +
                "FROM CarMaintenance cm " +
                "INNER JOIN Cars c ON cm.CarID = c.CarID " +
                "INNER JOIN Appointments a ON cm.AppointmentID = a.AppointmentID " +
                "INNER JOIN Users owner ON c.OwnerID = owner.UserID " +
                "INNER JOIN Users creator ON a.CreatedBy = creator.UserID " +
                "LEFT JOIN Users tech ON cm.AssignedTechnicianID = tech.UserID " +
                "LEFT JOIN MaintenancePackage mp ON a.RequestedPackageID = mp.PackageID " +
                "WHERE a.Status = 'CONFIRMED' AND c.OwnerID = ? " +
                "ORDER BY cm.MaintenanceDate DESC";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToCarMaintenance(rs));
                }
            }
        }
        return list;
    }

    /**
     * Lấy danh sách tất cả maintenance có trạng thái appointment CONFIRMED
     */
    public List<CarMaintenance> getAllConfirmedMaintenance() throws SQLException {
        List<CarMaintenance> list = new ArrayList<>();
        String sql = "SELECT " +
                "cm.MaintenanceID, cm.CarID, cm.AppointmentID, cm.MaintenanceDate, " +
                "cm.Odometer, cm.Status as MaintenanceStatus, cm.TotalAmount, " +
                "cm.DiscountAmount, cm.FinalAmount, cm.Notes, cm.CreatedBy as MaintenanceCreatedBy, " +
                "cm.AssignedTechnicianID, cm.CreatedDate as MaintenanceCreatedDate, cm.CompletedDate, " +
                "c.LicensePlate, c.Brand, c.Model, c.Year, c.Color, c.OwnerID, " +
                "a.AppointmentDate, a.Status as AppointmentStatus, a.RequestedPackageID, " +
                "a.CreatedBy as AppointmentCreatedBy, a.RequestedServices as AppointmentNotes, " +
                "owner.UserID as OwnerUserID, owner.FullName as OwnerName, owner.Phone as OwnerPhone, owner.Email as OwnerEmail, " +
                "creator.UserID as CreatorUserID, creator.FullName as CreatedByName, creator.Phone as CreatedByPhone, creator.Email as CreatedByEmail, " +
                "tech.UserID as TechUserID, tech.FullName as TechnicianName, " +
                "mp.PackageID, mp.Name as PackageName " +
                "FROM CarMaintenance cm " +
                "INNER JOIN Cars c ON cm.CarID = c.CarID " +
                "INNER JOIN Appointments a ON cm.AppointmentID = a.AppointmentID " +
                "INNER JOIN Users owner ON c.OwnerID = owner.UserID " +
                "INNER JOIN Users creator ON a.CreatedBy = creator.UserID " +
                "LEFT JOIN Users tech ON cm.AssignedTechnicianID = tech.UserID " +
                "LEFT JOIN MaintenancePackage mp ON a.RequestedPackageID = mp.PackageID " +
                "WHERE a.Status = 'CONFIRMED' " +
                "ORDER BY cm.MaintenanceDate DESC";

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapResultSetToCarMaintenance(rs));
            }
        }
        return list;
    }

    /**
     * Lấy chi tiết một maintenance theo ID
     */
    public CarMaintenance getMaintenanceByID(int maintenanceID) throws SQLException {
        String sql = "SELECT " +
                "cm.MaintenanceID, cm.CarID, cm.AppointmentID, cm.MaintenanceDate, " +
                "cm.Odometer, cm.Status as MaintenanceStatus, cm.TotalAmount, " +
                "cm.DiscountAmount, cm.FinalAmount, cm.Notes, cm.CreatedBy as MaintenanceCreatedBy, " +
                "cm.AssignedTechnicianID, cm.CreatedDate as MaintenanceCreatedDate, cm.CompletedDate, " +
                "c.LicensePlate, c.Brand, c.Model, c.Year, c.Color, c.OwnerID, " +
                "a.AppointmentDate, a.Status as AppointmentStatus, a.RequestedPackageID, " +
                "a.CreatedBy as AppointmentCreatedBy, a.RequestedServices as AppointmentNotes, " +
                "owner.UserID as OwnerUserID, owner.FullName as OwnerName, owner.Phone as OwnerPhone, owner.Email as OwnerEmail, " +
                "creator.UserID as CreatorUserID, creator.FullName as CreatedByName, creator.Phone as CreatedByPhone, creator.Email as CreatedByEmail, " +
                "tech.UserID as TechUserID, tech.FullName as TechnicianName, " +
                "mp.PackageID, mp.Name as PackageName " +
                "FROM CarMaintenance cm " +
                "INNER JOIN Cars c ON cm.CarID = c.CarID " +
                "INNER JOIN Appointments a ON cm.AppointmentID = a.AppointmentID " +
                "INNER JOIN Users owner ON c.OwnerID = owner.UserID " +
                "INNER JOIN Users creator ON a.CreatedBy = creator.UserID " +
                "LEFT JOIN Users tech ON cm.AssignedTechnicianID = tech.UserID " +
                "LEFT JOIN MaintenancePackage mp ON a.RequestedPackageID = mp.PackageID " +
                "WHERE cm.MaintenanceID = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, maintenanceID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToCarMaintenance(rs);
                }
            }
        }
        return null;
    }

    /**
     * Map dữ liệu ResultSet sang đối tượng CarMaintenance
     */
    private CarMaintenance mapResultSetToCarMaintenance(ResultSet rs) throws SQLException {
        CarMaintenance maintenance = new CarMaintenance();

        maintenance.setMaintenanceId(rs.getInt("MaintenanceID"));
        maintenance.setMaintenanceDate(rs.getString("MaintenanceDate"));
        maintenance.setOdometer(rs.getInt("Odometer"));
        maintenance.setStatus(rs.getString("MaintenanceStatus"));
        maintenance.setTotalAmount(rs.getBigDecimal("TotalAmount"));
        maintenance.setDiscountAmount(rs.getBigDecimal("DiscountAmount"));
        maintenance.setFinalAmount(rs.getBigDecimal("FinalAmount"));
        maintenance.setNotes(rs.getString("Notes"));
        maintenance.setCreatedDate(rs.getString("MaintenanceCreatedDate"));
        maintenance.setCompletedDate(rs.getString("CompletedDate"));

        // Car
        Car car = new Car();
        car.setCarId(rs.getInt("CarID"));
        car.setLicensePlate(rs.getString("LicensePlate"));
        car.setBrand(rs.getString("Brand"));
        car.setModel(rs.getString("Model"));
        car.setYear(rs.getInt("Year"));
        car.setColor(rs.getString("Color"));

        // Owner
        User owner = new User();
        owner.setUserId(rs.getInt("OwnerUserID"));
        owner.setFullName(rs.getString("OwnerName"));
        owner.setPhone(rs.getString("OwnerPhone"));
        owner.setEmail(rs.getString("OwnerEmail"));
        car.setOwner(owner);

        maintenance.setCar(car);

        // Appointment
        Appointment appointment = new Appointment();
        appointment.setAppointmentId(rs.getInt("AppointmentID"));
        appointment.setAppointmentDate(rs.getString("AppointmentDate"));
        appointment.setStatus(rs.getString("AppointmentStatus"));
        appointment.setNotes(rs.getString("AppointmentNotes"));
        appointment.setCar(car);

        // Creator
        User creator = new User();
        creator.setUserId(rs.getInt("CreatorUserID"));
        creator.setFullName(rs.getString("CreatedByName"));
        creator.setPhone(rs.getString("CreatedByPhone"));
        creator.setEmail(rs.getString("CreatedByEmail"));
        appointment.setCreatedBy(creator);
        maintenance.setCreatedBy(creator);

        // Package
        if (rs.getObject("PackageID") != null) {
            MaintenancePackage pkg = new MaintenancePackage();
            pkg.setPackageId(rs.getInt("PackageID"));
            pkg.setName(rs.getString("PackageName"));
            appointment.setRequestedPackage(pkg);
        }

        maintenance.setAppointment(appointment);

        // Technician
        if (rs.getObject("TechUserID") != null) {
            User technician = new User();
            technician.setUserId(rs.getInt("TechUserID"));
            technician.setFullName(rs.getString("TechnicianName"));
            maintenance.setAssignedTechnician(technician);
        }

        return maintenance;
    }
    public List<CarMaintenance> searchByLicensePlateAndUserId(String licensePlate, int userId) throws SQLException {
        List<CarMaintenance> list = new ArrayList<>();
        String sql = "SELECT " +
                "cm.MaintenanceID, cm.CarID, cm.AppointmentID, cm.MaintenanceDate, " +
                "cm.Odometer, cm.Status as MaintenanceStatus, cm.TotalAmount, " +
                "cm.DiscountAmount, cm.FinalAmount, cm.Notes, cm.CreatedBy as MaintenanceCreatedBy, " +
                "cm.AssignedTechnicianID, cm.CreatedDate as MaintenanceCreatedDate, cm.CompletedDate, " +
                "c.LicensePlate, c.Brand, c.Model, c.Year, c.Color, c.OwnerID, " +
                "a.AppointmentDate, a.Status as AppointmentStatus, a.RequestedPackageID, " +
                "a.CreatedBy as AppointmentCreatedBy, a.RequestedServices as AppointmentNotes, " +
                "owner.UserID as OwnerUserID, owner.FullName as OwnerName, owner.Phone as OwnerPhone, owner.Email as OwnerEmail, " +
                "creator.UserID as CreatorUserID, creator.FullName as CreatedByName, creator.Phone as CreatedByPhone, creator.Email as CreatedByEmail, " +
                "tech.UserID as TechUserID, tech.FullName as TechnicianName, " +
                "mp.PackageID, mp.Name as PackageName " +
                "FROM CarMaintenance cm " +
                "INNER JOIN Cars c ON cm.CarID = c.CarID " +
                "INNER JOIN Appointments a ON cm.AppointmentID = a.AppointmentID " +
                "INNER JOIN Users owner ON c.OwnerID = owner.UserID " +
                "INNER JOIN Users creator ON a.CreatedBy = creator.UserID " +
                "LEFT JOIN Users tech ON cm.AssignedTechnicianID = tech.UserID " +
                "LEFT JOIN MaintenancePackage mp ON a.RequestedPackageID = mp.PackageID " +
                "WHERE a.Status = 'CONFIRMED' AND c.OwnerID = ? AND c.LicensePlate LIKE ? " +
                "ORDER BY cm.MaintenanceDate DESC";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, "%" + licensePlate + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    CarMaintenance maintenance = mapResultSetToCarMaintenance(rs);
                    list.add(maintenance);
                }
            }
        }
        return list;
    }

    /**
     * 🔧 Lấy danh sách Service Details (dịch vụ bảo dưỡng)
     */
    public List<ServiceDetail> getServiceDetails(int maintenanceID) throws SQLException {
        List<ServiceDetail> list = new ArrayList<>();
        String sql = "SELECT sd.*, p.ProductID, p.Code as ProductCode, p.Name as ProductName, " +
                "p.Type as ProductType, p.Price as ProductPrice " +
                "FROM ServiceDetails sd " +
                "INNER JOIN Product p ON sd.ProductID = p.ProductID " +
                "WHERE sd.MaintenanceID = ? " +
                "ORDER BY sd.ServiceDetailID";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, maintenanceID);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ServiceDetail detail = new ServiceDetail();
                    detail.setServiceDetailId(rs.getInt("ServiceDetailID"));

                    CarMaintenance maintenance = new CarMaintenance();
                    maintenance.setMaintenanceId(maintenanceID);
                    detail.setMaintenance(maintenance);

                    Product product = new Product();
                    product.setProductId(rs.getInt("ProductID"));
                    product.setCode(rs.getString("ProductCode"));
                    product.setName(rs.getString("ProductName"));
                    product.setType(rs.getString("ProductType"));
                    product.setPrice(rs.getBigDecimal("ProductPrice"));
                    detail.setProduct(product);

                    detail.setQuantity(rs.getBigDecimal("Quantity"));
                    detail.setUnitPrice(rs.getBigDecimal("UnitPrice"));
                    detail.setTotalPrice(rs.getBigDecimal("TotalPrice"));
                    detail.setFromPackage(rs.getBoolean("FromPackage"));
                    detail.setNotes(rs.getString("Notes"));

                    list.add(detail);
                }
            }
        }
        return list;
    }

    /**
     * 🧩 Lấy danh sách Part Details (phụ tùng thay thế)
     */
    public List<ServicePartDetail> getPartDetails(int maintenanceID) throws SQLException {
        List<ServicePartDetail> list = new ArrayList<>();
        String sql = "SELECT spd.*, p.ProductID, p.Code as ProductCode, p.Name as ProductName, " +
                "p.Type as ProductType, p.Price as ProductPrice " +
                "FROM ServicePartDetails spd " +
                "INNER JOIN Product p ON spd.ProductID = p.ProductID " +
                "WHERE spd.MaintenanceID = ? " +
                "ORDER BY spd.ServicePartDetailID";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, maintenanceID);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ServicePartDetail detail = new ServicePartDetail();
                    detail.setServicePartDetailId(rs.getInt("ServicePartDetailID"));

                    CarMaintenance maintenance = new CarMaintenance();
                    maintenance.setMaintenanceId(maintenanceID);
                    detail.setMaintenance(maintenance);

                    Product product = new Product();
                    product.setProductId(rs.getInt("ProductID"));
                    product.setCode(rs.getString("ProductCode"));
                    product.setName(rs.getString("ProductName"));
                    product.setType(rs.getString("ProductType"));
                    product.setPrice(rs.getBigDecimal("ProductPrice"));
                    detail.setProduct(product);

                    detail.setQuantity(rs.getInt("Quantity"));
                    detail.setUnitPrice(rs.getBigDecimal("UnitPrice"));
                    detail.setTotalPrice(rs.getBigDecimal("TotalPrice"));
                    detail.setInstallationDate(rs.getString("InstallationDate"));
                    detail.setWarrantyExpireDate(rs.getString("WarrantyExpireDate"));
                    detail.setLotNumber(rs.getString("LotNumber"));
                    detail.setFromPackage(rs.getBoolean("FromPackage"));
                    detail.setNotes(rs.getString("Notes"));

                    list.add(detail);
                }
            }
        }
        return list;
    }
    
}