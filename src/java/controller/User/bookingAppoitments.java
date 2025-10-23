package controller.User;

import dal.MaintenancePackageDAO;
import dal.CarDAO;
import dal.AppointmentDAO;
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
import model.Appointment;
    
@WebServlet(name = "bookingAppoitments", urlPatterns = {"/bookingAppoitments"})
public class bookingAppoitments extends HttpServlet {

    // ==================== DOGET: HIỂN THỊ DANH SÁCH, GỢI Ý, TÌM KIẾM ====================
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("view/CommonScreen/login.jsp");
            return;
        }

        MaintenancePackageDAO packageDao = new MaintenancePackageDAO();
        CarDAO carDao = new CarDAO();

        List<MaintenancePackage> packageList = null;
        List<Car> carList = carDao.getCarsByUserIdWithOwnerInfo(user.getUserId());
        request.setAttribute("carList", carList);

        // ====== TÌM KIẾM THEO HÃNG XE ======
        String searchBrand = request.getParameter("searchBrand");
        if (searchBrand != null && !searchBrand.trim().isEmpty()) {
            packageList = packageDao.searchPackagesByBrand(searchBrand.trim());
            request.setAttribute("searchBrand", searchBrand);
        }

        // ====== GỢI Ý GÓI BẢO DƯỠNG ======
        String suggest = request.getParameter("suggest");
        String selectedCarIdStr = request.getParameter("selectedCarId");
        MaintenancePackage recommendedPackage = null;

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

        // ====== HIỂN THỊ TẤT CẢ GÓI NẾU KHÔNG TÌM ======
        if (packageList == null) {
            packageList = packageDao.getAllActivePackages();
        }

        // ====== LẤY GÓI TÙY CHỌN PKG-EMPTY ======
        MaintenancePackage customPackage = packageDao.getPackageByCode("PKG-EMPTY");
        request.setAttribute("customPackage", customPackage);

        request.setAttribute("packageList", packageList);
        request.getRequestDispatcher("view/Customer/bookingAppointments.jsp").forward(request, response);
    }

    // ==================== DOPOST: XỬ LÝ ĐẶT LỊCH HẸN ====================
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("view/CommonScreen/login.jsp");
            return;
        }

        try {
            // ====== KIỂM TRA LOẠI ĐẶT LỊCH ======
            String appointmentType = request.getParameter("appointmentType");
            
            // Nếu là đặt lịch tùy chọn
            if ("custom".equals(appointmentType)) {
                handleCustomAppointment(request, response, user);
                return;
            }
            
            // Lấy dữ liệu từ form đặt lịch thường
            int carId = Integer.parseInt(request.getParameter("carId"));
            int packageId = Integer.parseInt(request.getParameter("packageId"));
            String appointmentDate = request.getParameter("appointmentDate");
            String appointmentTime = request.getParameter("appointmentTime");
            
            String notes = request.getParameter("notes");

            // Kiểm tra dữ liệu bắt buộc
            if (appointmentDate == null || appointmentDate.trim().isEmpty()) {
                request.setAttribute("errorMessage", "Vui lòng chọn ngày hẹn!");
                doGet(request, response);
                return;
            }

            if (appointmentTime == null || appointmentTime.trim().isEmpty()) {
                request.setAttribute("errorMessage", "Vui lòng chọn giờ hẹn!");
                doGet(request, response);
                return;
            }

            // Ghép ngày và giờ thành datetime
            String fullDateTime = appointmentDate + " " + appointmentTime + ":00";

            // Lấy Package theo ID
            MaintenancePackageDAO packageDao = new MaintenancePackageDAO();
            MaintenancePackage selectedPackage = packageDao.getPackageById(packageId);

            if (selectedPackage == null) {
                request.setAttribute("errorMessage", "Gói bảo dưỡng không tồn tại!");
                doGet(request, response);
                return;
            }

            // Kiểm tra xe có thuộc về user không
            CarDAO carDao = new CarDAO();
            List<Car> userCars = carDao.getCarsByUserIdWithOwnerInfo(user.getUserId());
            
            Car selectedCar = null;
            for (Car car : userCars) {
                if (car.getCarId() == carId) {
                    selectedCar = car;
                    break;
                }
            }
            
            if (selectedCar == null) {
                request.setAttribute("errorMessage", "Xe không tồn tại hoặc không thuộc về bạn!");
                doGet(request, response);
                return;
            }

            // ====== KIỂM TRA GÓI CÓ PHÙ HỢP VỚI HÃNG XE KHÔNG ======
            if (!isPackageCompatibleWithCar(selectedPackage, selectedCar)) {
                request.setAttribute("errorMessage", 
                    "⚠️ Gói bảo dưỡng \"" + selectedPackage.getName() + 
                    "\" không phù hợp với xe " + selectedCar.getBrand() + " của bạn! " +
                    "Gói này chỉ áp dụng cho: " + selectedPackage.getApplicableBrands());
                doGet(request, response);
                return;
            }

            // Tạo đối tượng Appointment
            Appointment appointment = new Appointment();
            appointment.setCar(selectedCar);
            appointment.setAppointmentDate(fullDateTime);
//            appointment.setRequestedServices(requestedServices != null ? requestedServices : "");
            appointment.setNotes(notes != null ? notes : "");
            appointment.setRequestedPackage(selectedPackage);
            appointment.setCreatedBy(user);
            appointment.setStatus("Pending"); // chờ xác nhận

            // Lưu vào DB
            AppointmentDAO appointmentDAO = new AppointmentDAO();
            boolean success = appointmentDAO.insertAppointment(appointment);

            if (success) {
                request.setAttribute("successMessage", " Đặt lịch hẹn thành công! Chúng tôi sẽ liên hệ với bạn sớm.");
            } else {
                request.setAttribute("errorMessage", "❌ Đặt lịch thất bại, vui lòng thử lại!");
            }

        } catch (NumberFormatException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "⚠️ Dữ liệu không hợp lệ!");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "⚠️ Lỗi trong quá trình đặt lịch: " + e.getMessage());
        }

        doGet(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Booking appointments page with brand search, suggestion, and appointment creation";
    }

    // ==================== KIỂM TRA GÓI PHÙ HỢP VỚI XE ====================
    /**
     * Kiểm tra gói bảo dưỡng có phù hợp với hãng xe không
     * @param pkg Gói bảo dưỡng
     * @param car Xe cần kiểm tra
     * @return true nếu phù hợp, false nếu không phù hợp
     */
    private boolean isPackageCompatibleWithCar(MaintenancePackage pkg, Car car) {
        if (pkg == null || car == null) {
            return false;
        }

        String applicableBrands = pkg.getApplicableBrands();
        String carBrand = car.getBrand();

        // Nếu không có thông tin ApplicableBrands -> cho phép tất cả
        if (applicableBrands == null || applicableBrands.trim().isEmpty()) {
            return true;
        }

        // Nếu có "All" hoặc "Tất cả" -> cho phép tất cả hãng
        if (applicableBrands.toLowerCase().contains("all") || 
            applicableBrands.toLowerCase().contains("tất cả")) {
            return true;
        }

        // Kiểm tra carBrand có trong danh sách ApplicableBrands không
        // Xử lý nhiều format: "Toyota, Honda, BMW" hoặc "Toyota; Honda; BMW"
        String[] brandArray = applicableBrands.split("[,;]+");
        
        for (String brand : brandArray) {
            brand = brand.trim().toLowerCase();
            if (carBrand != null && carBrand.toLowerCase().contains(brand)) {
                return true;
            }
            if (brand.contains(carBrand.toLowerCase())) {
                return true;
            }
        }

        return false;
    }

    // ==================== XỬ LÝ ĐẶT LỊCH TÙY CHỌN ====================
    /**
     * Xử lý đặt lịch hẹn tùy chọn với gói PKG-EMPTY
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @param user User hiện tại
     */
    private void handleCustomAppointment(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {
        
        try {
            // Lấy dữ liệu từ form đặt lịch tùy chọn
            int carId = Integer.parseInt(request.getParameter("carId"));
            String appointmentDate = request.getParameter("appointmentDate");
            String appointmentTime = request.getParameter("appointmentTime");
            // customServices bị bỏ qua theo yêu cầu nghiệp vụ mới
            String customServices = null;
            String notes = request.getParameter("notes");

            // Kiểm tra dữ liệu bắt buộc
            if (appointmentDate == null || appointmentDate.trim().isEmpty()) {
                request.setAttribute("errorMessage", "Vui lòng chọn ngày hẹn!");
                doGet(request, response);
                return;
            }

            if (appointmentTime == null || appointmentTime.trim().isEmpty()) {
                request.setAttribute("errorMessage", "Vui lòng chọn giờ hẹn!");
                doGet(request, response);
                return;
            }

            // BỎ QUA kiểm tra mô tả dịch vụ tùy chọn

            // Ghép ngày và giờ thành datetime
            String fullDateTime = appointmentDate + " " + appointmentTime + ":00";

            // Kiểm tra xe có thuộc về user không
            CarDAO carDao = new CarDAO();
            List<Car> userCars = carDao.getCarsByUserIdWithOwnerInfo(user.getUserId());
            
            Car selectedCar = null;
            for (Car car : userCars) {
                if (car.getCarId() == carId) {
                    selectedCar = car;
                    break;
                }
            }
            
            if (selectedCar == null) {
                request.setAttribute("errorMessage", "Xe không tồn tại hoặc không thuộc về bạn!");
                doGet(request, response);
                return;
            }

            // Tạo lịch hẹn tùy chọn với gói PKG-EMPTY
            AppointmentDAO appointmentDAO = new AppointmentDAO();
            boolean success = appointmentDAO.createCustomAppointmentWithPackageCode(
                carId, 
                fullDateTime, 
                customServices, 
                notes != null ? notes.trim() : "", 
                user.getUserId(), 
                "PKG-EMPTY"
            );

            if (success) {
                request.setAttribute("successMessage", 
                    "✅ Đặt lịch hẹn tùy chọn thành công! " +
                    "Chúng tôi sẽ liên hệ với bạn để xác nhận chi tiết dịch vụ và báo giá.");
            } else {
                request.setAttribute("errorMessage", "Có lỗi xảy ra khi đặt lịch tùy chọn. Vui lòng thử lại!");
            }

        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Dữ liệu không hợp lệ!");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Có lỗi xảy ra. Vui lòng thử lại!");
        }

        doGet(request, response);
    }
}