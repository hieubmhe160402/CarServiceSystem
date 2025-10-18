package controller.User;

import dal.MaintenancePackageDAO;
import dal.CarDAO;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.MaintenancePackage;
import model.User;
import model.Car;

@WebServlet(name = "bookingAppoitments", urlPatterns = {"/bookingAppoitments"})
public class bookingAppoitments extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        // Nếu người dùng chưa đăng nhập
        if (user == null) {
            response.sendRedirect("view/CommonScreen/login.jsp");
            return;
        }

        MaintenancePackageDAO packageDao = new MaintenancePackageDAO();
        CarDAO carDao = new CarDAO();

        List<MaintenancePackage> packageList = null;
        List<Car> carList = null;
        MaintenancePackage recommendedPackage = null;

        // ===== LẤY DANH SÁCH XE NGƯỜI DÙNG =====
        carList = carDao.getCarsByUserIdWithOwnerInfo(user.getUserId());
        request.setAttribute("carList", carList);

        // ===== XỬ LÝ TÌM KIẾM THEO HÃNG =====
        String searchBrand = request.getParameter("searchBrand");
        if (searchBrand != null && !searchBrand.trim().isEmpty()) {
            packageList = packageDao.searchPackagesByBrand(searchBrand.trim());
            request.setAttribute("searchBrand", searchBrand);
        }

        // ===== XỬ LÝ GỢI Ý GÓI =====
        String suggest = request.getParameter("suggest");
        String selectedCarIdStr = request.getParameter("selectedCarId");

        if ("true".equalsIgnoreCase(suggest) && carList != null && !carList.isEmpty()) {
            Car selectedCar = null;

            if (selectedCarIdStr != null && !selectedCarIdStr.isEmpty()) {
                try {
                    int selectedCarId = Integer.parseInt(selectedCarIdStr);
                    for (Car car : carList) {
                        if (car.getCarId() == selectedCarId) {
                            selectedCar = car;
                            break;
                        }
                    }
                } catch (NumberFormatException e) {
                    selectedCar = carList.get(0);
                }
            } else {
                selectedCar = carList.get(0);
            }

            if (selectedCar != null) {
                String brand = selectedCar.getBrand();
                Integer currentKm = selectedCar.getCurrentOdometer();

                if (currentKm == null) currentKm = 0;

                recommendedPackage = packageDao.getRecommendedPackage(brand, currentKm);

                request.setAttribute("selectedBrand", brand);
                request.setAttribute("currentKm", currentKm);
                request.setAttribute("recommendedPackage", recommendedPackage);
            }
        }

        // Nếu không tìm kiếm thì hiển thị toàn bộ
        if (packageList == null) {
            packageList = packageDao.getAllActivePackages();
        }

        request.setAttribute("packageList", packageList);
        request.getRequestDispatcher("view/Customer/bookingAppointments.jsp").forward(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Booking appointments page showing maintenance packages with search and smart recommendation";
    }
}
