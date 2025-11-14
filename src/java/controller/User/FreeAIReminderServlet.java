package controller.User;

import service.FreeAIService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import model.Car;
import dal.CarDAO;
import model.User;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

@WebServlet("/freeAiReminder")
public class FreeAIReminderServlet extends HttpServlet {

    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/view/CommonScreen/login.jsp");
            return;
        }

        try {
            CarDAO carDAO = new CarDAO();
            List<Car> carList = carDAO.getCarsByUserIdWithOwnerInfo(user.getUserId());
            request.setAttribute("carList", carList);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "❌ Lỗi hệ thống: " + e.getMessage());
        }

        request.getRequestDispatcher("/view/Customer/aiReminder.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=UTF-8");
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        CarDAO carDAO = new CarDAO();

        if (user == null) {
            JsonObject error = new JsonObject();
            error.addProperty("error", "❌ Vui lòng đăng nhập trước");
            out.write(gson.toJson(error));
            return;
        }

        String action = request.getParameter("action");

        try {
            if ("chat".equals(action)) {
                String userMessage = request.getParameter("message");
                String carIdParam = request.getParameter("carId");

                if (userMessage == null || userMessage.trim().isEmpty()) {
                    JsonObject error = new JsonObject();
                    error.addProperty("error", "Tin nhắn không được để trống");
                    out.write(gson.toJson(error));
                    return;
                }

                System.out.println("DEBUG carIdParam=" + carIdParam);

                String aiResponse;

                if (carIdParam != null && !carIdParam.trim().isEmpty()) {
                    try {
                        int carId = Integer.parseInt(carIdParam);
                        Car car = carDAO.getCarByUserCodeAndCarId(user.getUserCode(), carId);

                        if (car != null) {
                            // Lấy thông tin xe từ DB
                            String carBrand = car.getBrand();
                            String carModel = car.getModel();
                            String carOdometer = car.getCurrentOdometer() != null
                                    ? String.valueOf(car.getCurrentOdometer())
                                    : "0";
                            String carLastMaintenance = car.getLastMaintenanceDate() != null
                                    ? car.getLastMaintenanceDate()
                                    : "Chưa có dữ liệu";

                            System.out.println("DEBUG brand=" + carBrand + ", model=" + carModel + ", odometer=" + carOdometer);

                            // Tạo context đầy đủ cho AI
                            String carContext = String.format(
                                    "Thông tin xe:\n- Hãng: %s\n- Model: %s\n- Số km hiện tại: %s km\n- Bảo dưỡng gần nhất: %s\n\nCâu hỏi: %s",
                                    carBrand, carModel, carOdometer, carLastMaintenance, userMessage.trim()
                            );

                            // Gọi AI với context đầy đủ
                            aiResponse = FreeAIService.chatWithAI(carContext, carBrand, carModel, carOdometer, carLastMaintenance);
                        } else {
                            aiResponse = "❌ Không tìm thấy thông tin xe. Vui lòng chọn lại xe.";
                        }
                    } catch (NumberFormatException e) {
                        aiResponse = "❌ ID xe không hợp lệ.";
                    }
                } else {
                    // Nếu không có xe được chọn, gọi AI bình thường
                    aiResponse = FreeAIService.chatWithGemini(userMessage.trim());
                }

                JsonObject jsonResponse = new JsonObject();
                jsonResponse.addProperty("response", aiResponse);
                jsonResponse.addProperty("success", true);
                out.write(gson.toJson(jsonResponse));

            } else {
                JsonObject error = new JsonObject();
                error.addProperty("error", "Action không hợp lệ");
                out.write(gson.toJson(error));
            }
        } catch (Exception e) {
            e.printStackTrace();
            JsonObject error = new JsonObject();
            error.addProperty("error", "Lỗi khi xử lý yêu cầu: " + e.getMessage());
            error.addProperty("success", false);
            out.write(gson.toJson(error));
        } finally {
            out.flush();
        }
    }
}
