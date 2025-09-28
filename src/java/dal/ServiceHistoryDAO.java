package dal;

import Context.DBContext;
import model.ServiceHistory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceHistoryDAO extends DBContext {

    public List<ServiceHistory> getAllServiceHistories() throws SQLException {
        List<ServiceHistory> list = new ArrayList<>();

        String sql = "SELECT u.UserID, u.FullName AS CustomerName, c.LicensePlate, " +
                     "cm.MaintenanceID, cm.MaintenanceDate, p.Name AS ServiceName, " +
                     "sd.Quantity, sd.UnitPrice, sd.TotalPrice " +
                     "FROM Users u " +
                     "JOIN Cars c ON u.UserID = c.OwnerID " +
                     "JOIN CarMaintenance cm ON c.CarID = cm.CarID " +
                     "JOIN ServiceDetails sd ON cm.MaintenanceID = sd.MaintenanceID " +
                     "JOIN Product p ON sd.ProductID = p.ProductID " +
                     "WHERE u.RoleID = (SELECT RoleID FROM Role WHERE RoleName = 'Customer') " +
                     "AND p.Type = 'SERVICE' " +
                     "ORDER BY u.UserID, cm.MaintenanceDate DESC";

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ServiceHistory sh = new ServiceHistory();
                sh.setUserId(rs.getInt("UserID"));
                sh.setCustomerName(rs.getString("CustomerName"));
                sh.setLicensePlate(rs.getString("LicensePlate"));
                sh.setMaintenanceId(rs.getInt("MaintenanceID"));
                sh.setMaintenanceDate(rs.getDate("MaintenanceDate"));
                sh.setServiceName(rs.getString("ServiceName"));
                sh.setQuantity(rs.getInt("Quantity"));
                sh.setUnitPrice(rs.getBigDecimal("UnitPrice"));
                sh.setTotalPrice(rs.getBigDecimal("TotalPrice"));

                list.add(sh);
            }
        }
        return list;
    }
}
