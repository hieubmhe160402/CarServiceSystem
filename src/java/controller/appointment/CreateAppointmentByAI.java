/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.appointment;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;
import dal.AppointmentDAO;
import dal.AppointmentByAI;
import model.Car;
import service.FreeAIService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 *
 * @author MinHeee
 */
@WebServlet(name = "CreateAppointmentByAI", urlPatterns = {"/createAppointmentByAI"})
public class CreateAppointmentByAI extends HttpServlet {

    private final Gson gson = new Gson();

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet CreateAppointmentByAI</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet CreateAppointmentByAI at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Lấy danh sách xe để hiển thị trong dropdown của AI widget
        try {
            AppointmentDAO dao = new AppointmentDAO();
            List<Car> carList = dao.getAllCustomerCars();
            request.setAttribute("carList", carList);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Không thể tải danh sách xe: " + e.getMessage());
        }

        // Chuyển tiếp/nhúng tới JSP widget.
        // Khi servlet được gọi thông qua <jsp:include>, không nên forward (gây IllegalStateException)
        // Thay vào đó include nội dung JSP vào response hiện tại.
        request.getRequestDispatcher("/view/appointment/aiCreateAppointment.jsp").include(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=UTF-8");
        PrintWriter out = response.getWriter();

        String action = request.getParameter("action");

        try {
            if ("quickBook".equals(action)) {
                // Quick booking: create appointment in 2 hours for selected car
                String carIdStr = request.getParameter("carId");

                if (carIdStr == null || carIdStr.isEmpty()) {
                    JsonObject error = new JsonObject();
                    error.addProperty("error", "Vui lòng chọn xe");
                    error.addProperty("success", false);
                    out.write(gson.toJson(error));
                    return;
                }

                int carId = Integer.parseInt(carIdStr);

                // Get current user from session
                HttpSession session = request.getSession();
                User currentUser = (User) session.getAttribute("user");

                if (currentUser == null) {
                    JsonObject error = new JsonObject();
                    error.addProperty("error", "Vui lòng đăng nhập");
                    error.addProperty("success", false);
                    out.write(gson.toJson(error));
                    return;
                }

                // Build a prompt for Gemini to generate a short appointment note/confirmation
                AppointmentByAI appointmentDAO = new AppointmentByAI();
                // Try to fetch car info to include in prompt
                String carInfoText = "";
                try {
                    for (model.Car c : appointmentDAO.getCarsWithCustomerInfo()) {
                        if (c.getCarId() == carId) {
                            carInfoText = c.getBrand() + " (Owner: " + (c.getOwner() != null ? c.getOwner().getFullName() : "") + ")";
                            break;
                        }
                    }
                } catch (Exception ex) {
                    // ignore, we'll still call Gemini with basic info
                }

                String prompt = "Khách hàng muốn đặt lịch cho xe: " + carInfoText + "."
                        + " HÃY TRẢ VỀ MỘT CÂU RẤT NGẮN BẰNG TIẾNG VIỆT (1 câu duy nhất), bắt đầu với '2 tiếng nữa' và chỉ là nội dung ghi chú, ví dụ: '2 tiếng nữa Anh/Chị A đến là được'."
                        + " KHÔNG GIẢI THÍCH, KHÔNG DÀNH DÒNG.";

                String aiNotes = null;
                try {
                    aiNotes = FreeAIService.chatWithGemini(prompt);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                // Post-process AI output to guarantee a very short single-line note
                if (aiNotes != null) {
                    aiNotes = aiNotes.trim().replaceAll("\\s+", " ");
                    // Take only first sentence (up to first punctuation .!? or newline)
                    int endIdx = aiNotes.indexOf('\n');
                    if (endIdx == -1) {
                        int dot = aiNotes.indexOf('.');
                        int excl = aiNotes.indexOf('!');
                        int ques = aiNotes.indexOf('?');
                        int firstPunc = -1;
                        if (dot != -1) {
                            firstPunc = dot;
                        }
                        if (firstPunc == -1 || (excl != -1 && excl < firstPunc)) {
                            firstPunc = excl != -1 ? excl : firstPunc;
                        }
                        if (firstPunc == -1 || (ques != -1 && ques < firstPunc)) {
                            firstPunc = ques != -1 ? ques : firstPunc;
                        }
                        if (firstPunc != -1) {
                            aiNotes = aiNotes.substring(0, firstPunc + 1).trim();
                        }
                    } else {
                        aiNotes = aiNotes.substring(0, endIdx).trim();
                    }

                    // Ensure it starts with '2 tiếng'
                    String lower = aiNotes.toLowerCase();
                    if (!lower.startsWith("2 tiếng") && !lower.startsWith("2h") && !lower.startsWith("2 giờ")) {
                        aiNotes = "2 tiếng nữa, " + aiNotes;
                    }

                    // Truncate to reasonable length
                    if (aiNotes.length() > 120) {
                        aiNotes = aiNotes.substring(0, 120).trim();
                    }
                }

                // Fallback if AI failed or produced empty text
                if (aiNotes == null || aiNotes.isEmpty()) {
                    aiNotes = "2 tiếng nữa, Anh/Chị đến là được.";
                }

                boolean success = appointmentDAO.createAppointmentInTwoHours(carId, currentUser.getUserId(), aiNotes);

                JsonObject jsonResponse = new JsonObject();
                if (success) {
                    jsonResponse.addProperty("response", "✅ Lịch hẹn đã được tạo thành công cho 2 tiếng nữa!");
                    jsonResponse.addProperty("aiNotes", aiNotes == null ? "" : aiNotes);
                    jsonResponse.addProperty("success", true);
                } else {
                    jsonResponse.addProperty("response", "❌ Không thể tạo lịch hẹn. Vui lòng thử lại.");
                    jsonResponse.addProperty("aiNotes", aiNotes == null ? "" : aiNotes);
                    jsonResponse.addProperty("success", false);
                }
                out.write(gson.toJson(jsonResponse));
            } else if ("getPrices".equals(action)) {
                // Lấy danh sách giá từ database
                AppointmentByAI appointmentDAO = new AppointmentByAI();
                List<Map<String, Object>> packages = appointmentDAO.getActivePackagePrices();

                if (packages != null && !packages.isEmpty()) {
                    // Xây dựng danh sách giá để gửi cho AI
                    StringBuilder priceListForDB = new StringBuilder("💰 Danh sách giá bảo dưỡng hiện tại:\n\n");
                    StringBuilder priceListForAI = new StringBuilder("Danh sách các gói bảo dưỡng:\n");

                    for (Map<String, Object> pkg : packages) {
                        String packageCode = (String) pkg.get("packageCode");
                        double basePrice = (Double) pkg.get("basePrice");
                        double finalPrice = (Double) pkg.get("finalPrice");

                        priceListForDB.append(String.format(
                                "📦 %s\n  Giá gốc: %,.0f VNĐ\n  Giá cuối cùng: %,.0f VNĐ\n\n",
                                packageCode, basePrice, finalPrice
                        ));

                        priceListForAI.append(String.format(
                                "- %s: Giá gốc %,.0f VNĐ, Giá cuối cùng %,.0f VNĐ\n",
                                packageCode, basePrice, finalPrice
                        ));
                    }

                    // Gọi AI để phân tích và gợi ý
                    String aiPrompt = priceListForAI.toString()
                            + "\n\nHãy giải thích các gói bảo dưỡng này, so sánh giá cả, và gợi ý gói phù hợp cho khách hàng. "
                            + "Hãy trả lời bằng tiếng Việt, súc tích và dễ hiểu.";

                    String aiAnalysis = FreeAIService.chatWithGemini(aiPrompt);

                    JsonObject jsonResponse = new JsonObject();
                    jsonResponse.addProperty("priceList", priceListForDB.toString());
                    jsonResponse.addProperty("aiAnalysis", aiAnalysis);
                    jsonResponse.addProperty("response", priceListForDB.toString() + "\n\n🤖 Phân tích từ AI:\n" + aiAnalysis);
                    jsonResponse.addProperty("success", true);
                    out.write(gson.toJson(jsonResponse));
                } else {
                    JsonObject jsonResponse = new JsonObject();
                    jsonResponse.addProperty("response", "❌ Không có gói bảo dưỡng nào khả dụng.");
                    jsonResponse.addProperty("success", true);
                    out.write(gson.toJson(jsonResponse));
                }
            } else {
                processRequest(request, response);
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

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
