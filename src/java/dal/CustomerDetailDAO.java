package dal;

import Context.DBContext;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.CustomerProfile;

public class CustomerDetailDAO extends DBContext {

    // Nếu DBContext đã có protected Connection connection thì KHÔNG cần khai báo conn nữa.

    public List<CustomerProfile> getAllCustomerProfiles() throws SQLException {
        List<CustomerProfile> list = new ArrayList<>();

        String sql = "SELECT u.UserID, u.FullName, u.Email, u.Phone, u.DateOfBirth, "
                   + "u.Male, u.Image, "
                   + "c.CarID, c.LicensePlate, c.Brand, c.Model, c.Year, c.Color, "
                   + "c.EngineNumber, c.ChassisNumber, c.PurchaseDate, "
                   + "c.LastMaintenanceDate, c.NextMaintenanceDate "
                   + "FROM Users u "
                   + "LEFT JOIN Cars c ON u.UserID = c.OwnerID "
                   + "WHERE u.RoleID = (SELECT RoleID FROM Role WHERE RoleName = 'Customer')";

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                CustomerProfile cp = new CustomerProfile();
                cp.setUserId(rs.getInt("UserID"));
                cp.setFullName(rs.getString("FullName"));
                cp.setEmail(rs.getString("Email"));
                cp.setPhone(rs.getString("Phone"));
                cp.setDateOfBirth(rs.getDate("DateOfBirth"));
                cp.setMale(rs.getBoolean("Male"));
                cp.setImage(rs.getString("Image"));

                cp.setCarId(rs.getInt("CarID"));
                cp.setLicensePlate(rs.getString("LicensePlate"));
                cp.setBrand(rs.getString("Brand"));
                cp.setModel(rs.getString("Model"));
                cp.setYear(rs.getInt("Year"));
                cp.setColor(rs.getString("Color"));
                cp.setEngineNumber(rs.getString("EngineNumber"));
                cp.setChassisNumber(rs.getString("ChassisNumber"));
                cp.setPurchaseDate(rs.getDate("PurchaseDate"));
                cp.setLastMaintenanceDate(rs.getDate("LastMaintenanceDate"));
                cp.setNextMaintenanceDate(rs.getDate("NextMaintenanceDate"));

                list.add(cp);
            }
        }
        return list;
    }

    public static void main(String[] args) {
        String url = "jdbc:sqlserver://localhost:1433;databaseName=CarServiceDB";
        String user = "sa";
        String password = "123456";

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            // Truyền connection thủ công cho DBContext
            CustomerDetailDAO dao = new CustomerDetailDAO();
            dao.connection = connection; // Gán kết nối vào DBContext

            List<CustomerProfile> customers = dao.getAllCustomerProfiles();
            for (CustomerProfile cp : customers) {
                System.out.println(cp);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
