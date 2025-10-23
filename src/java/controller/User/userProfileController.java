package controller.User;

import dal.UserDAO;
import dal.CarDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import model.User;
import model.Car;

@WebServlet(name = "userProfileController", urlPatterns = {"/userProfileController"})
public class userProfileController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("user");

        if (currentUser == null) {
            response.sendRedirect("view/CommonScreen/login.jsp");
            return;
        }

        UserDAO userDao = new UserDAO();
        User user = userDao.getUserById(currentUser.getUserId());
        request.setAttribute("user", user);

        CarDAO carDao = new CarDAO();
        List<Car> cars;

        // Tìm kiếm nếu có từ khóa
        String search = request.getParameter("search");
        if (search != null && !search.trim().isEmpty()) {
            cars = carDao.searchCarsByKeyword(user.getUserId(), search);
            request.setAttribute("search", search);
        } else {
            cars = carDao.getCarsByUserIdWithOwnerInfo(user.getUserId());
        }

        request.setAttribute("cars", cars);
        request.getRequestDispatcher("view/Customer/userProfile.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            response.sendRedirect("view/CommonScreen/login.jsp");
            return;
        }

        CarDAO carDao = new CarDAO();
        String action = request.getParameter("action");

        try {
            if ("add".equals(action)) {
                // Thêm mới
                Car car = new Car();
                car.setLicensePlate(request.getParameter("licensePlate"));
                car.setBrand(request.getParameter("brand"));
                car.setModel(request.getParameter("model"));
                car.setYear(Integer.parseInt(request.getParameter("year")));
                car.setColor(request.getParameter("color"));
                car.setEngineNumber(request.getParameter("engineNumber"));
                car.setChassisNumber(request.getParameter("chassisNumber"));
                car.setPurchaseDate(request.getParameter("purchaseDate"));
                car.setCurrentOdometer(Integer.parseInt(request.getParameter("currentOdometer")));
                car.setOwner(currentUser);

                boolean success = carDao.insertCar(car);
                request.setAttribute("message", success ? " Thêm xe thành công!" : "❌ Thêm xe thất bại!");

            } else if ("update".equals(action)) {
                // Cập nhật
                Car car = new Car();
                car.setCarId(Integer.parseInt(request.getParameter("carId")));
                car.setLicensePlate(request.getParameter("licensePlate"));
                car.setBrand(request.getParameter("brand"));
                car.setModel(request.getParameter("model"));
                car.setYear(Integer.parseInt(request.getParameter("year")));
                car.setColor(request.getParameter("color"));
                car.setEngineNumber(request.getParameter("engineNumber"));
                car.setChassisNumber(request.getParameter("chassisNumber"));
                car.setPurchaseDate(request.getParameter("purchaseDate"));
                car.setCurrentOdometer(Integer.parseInt(request.getParameter("currentOdometer")));
                boolean success = carDao.updateCar(car);
                request.setAttribute("message", success ? " Cập nhật thành công!" : "❌ Cập nhật thất bại!");

            } else if ("delete".equals(action)) {
                // Xóa
                int carId = Integer.parseInt(request.getParameter("carId"));
                boolean success = carDao.deleteCar(carId);
                request.setAttribute("message", success ? "🗑️ Xóa xe thành công!" : "❌ Không thể xóa xe!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("message", "⚠️ Lỗi xử lý dữ liệu!");
        }
        
        if ("updateProfile".equals(action)) {
        // Lấy thông tin từ form update
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        boolean male = Boolean.parseBoolean(request.getParameter("male"));
        String dateOfBirth = request.getParameter("dateOfBirth");

        // Gán lại giá trị mới
        currentUser.setFullName(fullName);
        currentUser.setEmail(email);
        currentUser.setPhone(phone);
        currentUser.setMale(male);
        currentUser.setDateOfBirth(dateOfBirth);

        // Gọi DAO update
        UserDAO userDao = new UserDAO();
        userDao.UpdateEmployees(currentUser);

        session.setAttribute("user", currentUser);
        request.setAttribute("message", " Cập nhật thông tin thành công!");
    } else {
        // Thêm xe (hàm cũ bạn có)
        doAddCar(request, response, currentUser);
    }


        doGet(request, response);
    }
    
    private void doAddCar(HttpServletRequest request, HttpServletResponse response, User currentUser) {
    String licensePlate = request.getParameter("licensePlate");
    String brand = request.getParameter("brand");
    String model = request.getParameter("model");
    String yearStr = request.getParameter("year");
    String color = request.getParameter("color");
    String engineNumber = request.getParameter("engineNumber");
    String chassisNumber = request.getParameter("chassisNumber");
    String purchaseDate = request.getParameter("purchaseDate");

    try {
        int year = Integer.parseInt(yearStr);
        Car car = new Car();
        car.setLicensePlate(licensePlate);
        car.setBrand(brand);
        car.setModel(model);
        car.setYear(year);
        car.setColor(color);
        car.setEngineNumber(engineNumber);
        car.setChassisNumber(chassisNumber);
        car.setPurchaseDate(purchaseDate);
        car.setOwner(currentUser);

        CarDAO carDao = new CarDAO();
        boolean success = carDao.insertCar(car);

        if (success) {
            request.setAttribute("message", " Thêm xe thành công!");
        } else {
            request.setAttribute("message", "❌ Thêm xe thất bại!");
        }

    } catch (Exception e) {
        e.printStackTrace();
        request.setAttribute("message", "❌ Dữ liệu không hợp lệ!");
    }
}

}
