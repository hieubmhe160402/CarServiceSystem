/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import context.DBContext;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 *
 * @author MinHeee
 */
public class DashboardDAO extends DBContext {

    public int getTotalCustomers() {
        int total = 0;
        String sql = "SELECT COUNT(*) AS TotalCustomers FROM Users WHERE RoleID = 4";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                total = rs.getInt("TotalCustomers");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return total;
    }

    public int getTotalCarsProcessing() {
        int total = 0;

        String sql = "SELECT COUNT(*) AS TotalCarsProcessing "
                + "FROM CarMaintenance "
                + "WHERE Status = N'PROCESSING'";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                total = rs.getInt("TotalCarsProcessing");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return total;
    }

    public double getTotalRevenueToday() {
        double total = 0.0;

        String sql = "SELECT ISNULL(SUM(Amount), 0) AS TotalRevenueToday "
                + "FROM PaymentTransactions "
                + "WHERE Status = 'DONE' "
                + "AND CAST(PaymentDate AS DATE) = CAST(GETDATE() AS DATE)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                total = rs.getDouble("TotalRevenueToday");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return total;
    }

    public double getTotalRevenueThisMonth() {
        double total = 0.0;

        String sql = "SELECT ISNULL(SUM(Amount), 0) AS TotalRevenueThisMonth "
                + "FROM PaymentTransactions "
                + "WHERE Status = 'DONE' "
                + "AND MONTH(PaymentDate) = MONTH(GETDATE()) "
                + "AND YEAR(PaymentDate) = YEAR(GETDATE())";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                total = rs.getDouble("TotalRevenueThisMonth");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return total;
    }

    public List<Map<String, Object>> getPopularServices(int topN) {
        List<Map<String, Object>> serviceList = new ArrayList<>();
        String sql = "SELECT TOP (?) p.Name AS ServiceName, COUNT(sd.ServiceDetailID) AS UsageCount "
                + "FROM ServiceDetails sd "
                + "JOIN Product p ON sd.ProductID = p.ProductID "
                + "WHERE p.Type = N'SERVICE' "
                + "GROUP BY p.ProductID, p.Name "
                + "ORDER BY UsageCount DESC";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, topN);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                // Dùng key "serviceName" và "usageCount"
                row.put("serviceName", rs.getString("ServiceName"));
                row.put("usageCount", rs.getInt("UsageCount"));
                serviceList.add(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return serviceList;
    }
    // 2. Hàm khách hàng mới nhất

    public List<Map<String, Object>> getRecentCustomers(int limit) {
        List<Map<String, Object>> customers = new ArrayList<>();
        String sql = "SELECT TOP (?) FullName, Phone "
                + "FROM Users "
                + "WHERE RoleID = 4 "
                + "ORDER BY CreatedDate DESC";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, limit);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                // Dùng key "fullName" và "phone" (giống hệt tên thuộc tính DTO)
                row.put("fullName", rs.getString("FullName"));
                row.put("phone", rs.getString("Phone"));
                customers.add(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return customers;
    }

// 3. Hàm lịch hẹn gần đây
    public List<Map<String, Object>> getRecentAppointments(int limit) {
        List<Map<String, Object>> appointments = new ArrayList<>();

        // SỬA ĐỔI SQL:
        // 1. JOIN thêm bảng MaintenancePackage (mp) để lấy tên gói.
        // 2. SELECT tên gói (mp.Name) thay vì RequestedServices.
        // 3. Thêm WHERE để lọc các Appointment có gói (IS NOT NULL) và khác 6.
        String sql = "SELECT TOP (?) u.FullName, mp.Name AS PackageName "
                + "FROM Appointments a "
                + "JOIN Users u ON a.CreatedBy = u.UserID "
                + "JOIN MaintenancePackage mp ON a.RequestedPackageID = mp.PackageID "
                + "WHERE a.RequestedPackageID IS NOT NULL AND a.RequestedPackageID != 6 "
                + "ORDER BY a.CreatedDate DESC";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, limit);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();

                // Key "customerName" vẫn lấy FullName
                row.put("customerName", rs.getString("FullName"));

                // SỬA ĐỔI: Key "requestedServices" giờ sẽ chứa tên của gói (PackageName)
                row.put("requestedServices", rs.getString("PackageName"));

                appointments.add(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return appointments;
    }

    public Map<Integer, Double> getMonthlyRevenueByYear(int year) {
        Map<Integer, Double> revenueMap = new HashMap<>();

        String sql = "SELECT "
                + "    MONTH(CompletedDate) AS RevenueMonth, "
                + "    SUM(FinalAmount) AS TotalRevenue "
                + "FROM "
                + "    CarMaintenance "
                + "WHERE "
                + "    YEAR(CompletedDate) = ? " // Lọc theo năm
                + "    AND CompletedDate IS NOT NULL "
                + "GROUP BY "
                + "    MONTH(CompletedDate) " // Nhóm theo tháng
                + "ORDER BY "
                + "    RevenueMonth";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, year);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                // Key là tháng (1-12), Value là tổng tiền
                revenueMap.put(rs.getInt("RevenueMonth"), rs.getDouble("TotalRevenue"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return revenueMap;
    }
}
