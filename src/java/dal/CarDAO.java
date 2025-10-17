package dal;

import context.DBContext;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Car;
import model.User;

public class CarDAO extends DBContext {

    // Lấy danh sách xe theo UserID
    public List<Car> getCarsByUserId(int userId) {
        List<Car> list = new ArrayList<>();
        String sql = "SELECT CarID, Brand, LicensePlate, Color FROM Cars WHERE OwnerId = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Car car = new Car();
                car.setCarId(rs.getInt("CarID"));
                car.setBrand(rs.getString("Brand"));
                car.setLicensePlate(rs.getString("LicensePlate"));
                car.setColor(rs.getString("Color"));
                list.add(car);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // Thêm xe mới
    public boolean insertCar(Car car) {
        String sql = "INSERT INTO Cars (LicensePlate, Brand, Model, Year, Color, EngineNumber, "
                + "ChassisNumber, OwnerID, PurchaseDate, CreatedDate) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, GETDATE())";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, car.getLicensePlate());
            ps.setString(2, car.getBrand());
            ps.setString(3, car.getModel());
            ps.setInt(4, car.getYear());
            ps.setString(5, car.getColor());
            ps.setString(6, car.getEngineNumber());
            ps.setString(7, car.getChassisNumber());
            ps.setInt(8, car.getOwner().getUserId());
            ps.setString(9, car.getPurchaseDate());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // ✅ Cập nhật thông tin xe
    public boolean updateCar(Car car) {
        String sql = "UPDATE Cars SET LicensePlate=?, Brand=?, Model=?, Year=?, Color=?, "
                   + "EngineNumber=?, ChassisNumber=?, PurchaseDate=? WHERE CarID=?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, car.getLicensePlate());
            ps.setString(2, car.getBrand());
            ps.setString(3, car.getModel());
            ps.setInt(4, car.getYear());
            ps.setString(5, car.getColor());
            ps.setString(6, car.getEngineNumber());
            ps.setString(7, car.getChassisNumber());
            ps.setString(8, car.getPurchaseDate());
            ps.setInt(9, car.getCarId());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // ✅ Xóa xe
    public boolean deleteCar(int carId) {
        String sql = "DELETE FROM Cars WHERE CarID = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, carId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // ✅ Lấy thông tin xe theo CarID (dùng cho xem chi tiết hoặc edit)
    public Car getCarById(int carId) {
        String sql = "SELECT * FROM Cars WHERE CarID = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, carId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Car car = new Car();
                car.setCarId(rs.getInt("CarID"));
                car.setLicensePlate(rs.getString("LicensePlate"));
                car.setBrand(rs.getString("Brand"));
                car.setModel(rs.getString("Model"));
                car.setYear(rs.getInt("Year"));
                car.setColor(rs.getString("Color"));
                car.setEngineNumber(rs.getString("EngineNumber"));
                car.setChassisNumber(rs.getString("ChassisNumber"));
                car.setPurchaseDate(rs.getString("PurchaseDate"));
                return car;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // ✅ Tìm kiếm xe theo từ khóa (ví dụ trong thanh search)
    public List<Car> searchCarsByKeyword(int ownerId, String keyword) {
        List<Car> list = new ArrayList<>();
        String sql = "SELECT CarID, Brand, LicensePlate, Color "
                   + "FROM Cars WHERE OwnerID = ? AND "
                   + "(Brand LIKE ? OR LicensePlate LIKE ? OR Color LIKE ?)";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, ownerId);
            ps.setString(2, "%" + keyword + "%");
            ps.setString(3, "%" + keyword + "%");
            ps.setString(4, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Car car = new Car();
                car.setCarId(rs.getInt("CarID"));
                car.setBrand(rs.getString("Brand"));
                car.setLicensePlate(rs.getString("LicensePlate"));
                car.setColor(rs.getString("Color"));
                list.add(car);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // Test main
    public static void main(String[] args) {
        CarDAO dao = new CarDAO();
        int userId = 13;

        System.out.println("Tìm kiếm từ khóa 'Toyota':");
        List<Car> result = dao.searchCarsByKeyword(userId, "Toyota");
        for (Car c : result) {
            System.out.println(c.getBrand() + " - " + c.getLicensePlate());
        }
    }
}
