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

    // Thêm xe mới (đã bao gồm CurrentOdometer)
public boolean insertCar(Car car) {
    String sql = "INSERT INTO Cars (LicensePlate, Brand, Model, Year, Color, EngineNumber, "
               + "ChassisNumber, OwnerID, PurchaseDate, CurrentOdometer, CreatedDate) "
               + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, GETDATE())";
    try (PreparedStatement ps = connection.prepareStatement(sql)) {
        ps.setString(1, car.getLicensePlate());
        ps.setString(2, car.getBrand());
        ps.setString(3, car.getModel());
        ps.setInt(4, car.getYear());
        ps.setString(5, car.getColor());
        ps.setString(6, car.getEngineNumber());
        ps.setString(7, car.getChassisNumber());
        ps.setInt(8, car.getOwner().getUserId());
        ps.setString(9, car.getPurchaseDate());

        // currentOdometer có thể là null -> setNull nếu null
        Integer odo = car.getCurrentOdometer();
        if (odo != null) {
            ps.setInt(10, odo);
        } else {
            ps.setNull(10, java.sql.Types.INTEGER);
        }

        return ps.executeUpdate() > 0;
    } catch (Exception e) {
        e.printStackTrace();
    }
    return false;
}


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
    
    // Thêm phương thức này vào CarDAO.java

public Car getCarByIdWithOwner(int carId) {
    String sql = "SELECT c.*, u.UserId, u.Username, u.FullName, u.Email " +
                 "FROM Cars c " +
                 "LEFT JOIN Users u ON c.UserId = u.UserId " +
                 "WHERE c.CarId = ?";
    
    try (PreparedStatement ps = connection.prepareStatement(sql)) {
        ps.setInt(1, carId);
        ResultSet rs = ps.executeQuery();
        
        if (rs.next()) {
            Car car = new Car();
            car.setCarId(rs.getInt("CarId"));
            car.setBrand(rs.getString("Brand"));
            car.setModel(rs.getString("Model"));
            car.setYear(rs.getInt("Year"));
            car.setLicensePlate(rs.getString("LicensePlate"));
            car.setColor(rs.getString("Color"));
            car.setEngineNumber(rs.getString("EngineNumber"));
            car.setChassisNumber(rs.getString("ChassisNumber"));
            car.setCurrentOdometer(rs.getObject("CurrentOdometer") != null ? rs.getInt("CurrentOdometer") : null);
            car.setPurchaseDate(rs.getString("PurchaseDate"));
            car.setLastMaintenanceDate(rs.getString("LastMaintenanceDate"));
            car.setNextMaintenanceDate(rs.getString("NextMaintenanceDate"));
            car.setCreatedDate(rs.getString("CreatedDate"));
            
            // Load thông tin Owner
            User owner = new User();
            owner.setUserId(rs.getInt("UserId"));
            owner.setUserName(rs.getString("Username"));
            owner.setFullName(rs.getString("FullName"));
            owner.setEmail(rs.getString("Email"));
            
            car.setOwner(owner);
            
            return car;
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return null;
}
    
    public Car getCarByUserCodeAndCarId(String userCode, int carId) throws Exception {
    String sql = "SELECT c.CarID, c.LicensePlate, c.Brand, c.Model, c.Year, c.Color, " +
                 "c.EngineNumber, c.ChassisNumber, c.OwnerID, c.PurchaseDate, " +
                 "c.LastMaintenanceDate, c.NextMaintenanceDate, c.CurrentOdometer, c.CreatedDate, " +
                 "u.UserID, u.UserCode, u.FullName, u.Email, u.Phone, u.Image " +
                 "FROM Cars c " +
                 "INNER JOIN Users u ON c.OwnerID = u.UserID " +
                 "WHERE u.UserCode = ? AND c.CarID = ?";
    try (
         PreparedStatement ps = connection.prepareStatement(sql)) {
        ps.setString(1, userCode);
        ps.setInt(2, carId);
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                Car car = new Car();
                // gán dữ liệu xe
                car.setCarId(rs.getInt("CarID"));
                car.setLicensePlate(rs.getString("LicensePlate"));
                car.setBrand(rs.getString("Brand"));
                car.setModel(rs.getString("Model"));
                car.setYear(rs.getInt("Year"));
                car.setColor(rs.getString("Color"));
                car.setEngineNumber(rs.getString("EngineNumber"));
                car.setChassisNumber(rs.getString("ChassisNumber"));

                java.sql.Date purchaseDate = rs.getDate("PurchaseDate");
                car.setPurchaseDate(purchaseDate != null ? purchaseDate.toString() : null);
                java.sql.Date lastMaintenanceDate = rs.getDate("LastMaintenanceDate");
                car.setLastMaintenanceDate(lastMaintenanceDate != null ? lastMaintenanceDate.toString() : null);
                java.sql.Date nextMaintenanceDate = rs.getDate("NextMaintenanceDate");
                car.setNextMaintenanceDate(nextMaintenanceDate != null ? nextMaintenanceDate.toString() : null);
                java.sql.Date createdDate = rs.getDate("CreatedDate");
                car.setCreatedDate(createdDate != null ? createdDate.toString() : null);

                Integer currentOdometer = rs.getObject("CurrentOdometer") != null ? rs.getInt("CurrentOdometer") : 0;
                car.setCurrentOdometer(currentOdometer);

                // gán owner
                User owner = new User();
                owner.setUserId(rs.getInt("UserID"));
                owner.setUserCode(rs.getString("UserCode"));
                owner.setFullName(rs.getString("FullName"));
                owner.setEmail(rs.getString("Email"));
                owner.setPhone(rs.getString("Phone"));
                owner.setImage(rs.getString("Image"));
                car.setOwner(owner);

                return car;
            }
        }
    }
    return null;
}

    public Car getCarById(int carId) throws Exception {
        String sql = "SELECT c.CarID, c.LicensePlate, c.Brand, c.Model, c.Year, c.Color, " +
                     "c.EngineNumber, c.ChassisNumber, c.OwnerID, c.PurchaseDate, " +
                     "c.LastMaintenanceDate, c.NextMaintenanceDate, c.CurrentOdometer, c.CreatedDate, " +
                     "u.UserID AS OwnerUserID, u.FullName AS OwnerName, u.Email AS OwnerEmail, " +
                     "u.Phone AS OwnerPhone, u.Image AS OwnerImage " +
                     "FROM Cars c " +
                     "LEFT JOIN Users u ON c.OwnerID = u.UserID " +
                     "WHERE c.CarID = ?";

        try (
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, carId);
            try (ResultSet rs = ps.executeQuery()) {
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
                    car.setPurchaseDate(rs.getDate("PurchaseDate").toString());
                    car.setLastMaintenanceDate(rs.getDate("LastMaintenanceDate").toString());
                    car.setNextMaintenanceDate(rs.getDate("NextMaintenanceDate").toString());
                    car.setCurrentOdometer(rs.getInt("CurrentOdometer"));
                    car.setCreatedDate(rs.getDate("CreatedDate").toString());

                    // 🧩 Gắn thông tin chủ xe (User)
                    User owner = new User();
                    owner.setUserId(rs.getInt("OwnerUserID"));
                    owner.setFullName(rs.getString("OwnerName"));
                    owner.setEmail(rs.getString("OwnerEmail"));
                    owner.setPhone(rs.getString("OwnerPhone"));
                    owner.setImage(rs.getString("OwnerImage"));
                    car.setOwner(owner);

                    return car;
                }
            }
        }
        return null;
    }
    
    
    public List<Car> getCarsByUserIdWithOwnerInfo(int userId) {
        List<Car> list = new ArrayList<>();
        String sql = "SELECT c.CarID, c.LicensePlate, c.Brand, c.Model, c.Year, c.Color, "
                + "c.EngineNumber, c.ChassisNumber, c.PurchaseDate, "
                + "c.LastMaintenanceDate, c.NextMaintenanceDate, c.CreatedDate, c.CurrentOdometer, "
                + "u.UserID, u.FullName, u.Email, u.Phone, u.Male, u.DateOfBirth, u.Image "
                + "FROM Cars c "
                + "INNER JOIN Users u ON c.OwnerId = u.UserID "
                + "WHERE c.OwnerId = ?";

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                // Lấy thông tin xe
                int carId = rs.getInt("CarID");
                String licensePlate = rs.getString("LicensePlate");
                String brand = rs.getString("Brand");
                String model = rs.getString("Model");
                int year = rs.getInt("Year");
                String color = rs.getString("Color");
                String engineNumber = rs.getString("EngineNumber");
                String chassisNumber = rs.getString("ChassisNumber");
                String purchaseDate = rs.getString("PurchaseDate");
                String lastMaintenanceDate = rs.getString("LastMaintenanceDate");
                String nextMaintenanceDate = rs.getString("NextMaintenanceDate");
                String createdDate = rs.getString("CreatedDate");
                int currentOdometer = rs.getInt("CurrentOdometer");
                if (rs.wasNull()) {
                    currentOdometer = 0; // hoặc null nếu dùng Integer
                }

                // Tạo User object với thông tin đầy đủ
                User owner = new User();
                owner.setUserId(rs.getInt("UserID"));
                owner.setFullName(rs.getString("FullName"));
                owner.setEmail(rs.getString("Email"));
                owner.setPhone(rs.getString("Phone"));
                owner.setMale(rs.getBoolean("Male"));
                owner.setDateOfBirth(rs.getString("DateOfBirth"));
                owner.setImage(rs.getString("Image"));

                // Sử dụng constructor đầy đủ
                Car car = new Car(
                        carId,
                        licensePlate,
                        brand,
                        model,
                        year,
                        color,
                        engineNumber,
                        chassisNumber,
                        owner,
                        purchaseDate,
                        lastMaintenanceDate,
                        nextMaintenanceDate,
                        createdDate,
                        currentOdometer
                );

                list.add(car);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

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

    public boolean updateCarByUser(Car car, int userId) {
        String sql = "UPDATE Cars SET LicensePlate=?, Brand=?, Model=?, Year=?, Color=?, "
                + "EngineNumber=?, ChassisNumber=?, PurchaseDate=? "
                + "WHERE CarID=? AND OwnerID=?";
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
            ps.setInt(10, userId); // đảm bảo xe thuộc user này

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public List<Car> getCarsByOwnerId(int ownerId) {
    List<Car> cars = new ArrayList<>();
    String sql = "SELECT CarID, LicensePlate, Brand, Model, Year, Color, OwnerID " +
                 "FROM Cars WHERE OwnerID = ?";
    try {
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, ownerId);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Car car = new Car();
            car.setCarId(rs.getInt("CarID"));
            car.setLicensePlate(rs.getString("LicensePlate"));
            car.setBrand(rs.getString("Brand"));
            car.setModel(rs.getString("Model"));
            car.setYear(rs.getInt("Year"));
            car.setColor(rs.getString("Color"));

            // Set owner
            User owner = new User();
            owner.setUserId(rs.getInt("OwnerID"));
            car.setOwner(owner);

            cars.add(car);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return cars;
}
    public static void main(String[] args) {
        CarDAO dao = new CarDAO();

        // Test phiên bản 1
        List<Car> cars1 = dao.getCarsByUserId(13);
        System.out.println(" Version 1: " + cars1.size() + " cars");
        cars1.forEach(c -> System.out.println("   - " + c.getBrand()
                + " " + c.getModel()
                + " (" + c.getYear() + ")"));

        // Test phiên bản 2
        List<Car> cars2 = dao.getCarsByUserIdWithOwnerInfo(13);
        System.out.println("\n Version 2: " + cars2.size() + " cars");
        cars2.forEach(c -> System.out.println("   - " + c.getBrand()
                + " | Owner: " + c.getOwner().getFullName()));
    }

    
}
