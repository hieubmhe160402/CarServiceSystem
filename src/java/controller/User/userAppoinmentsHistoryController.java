package controller.User;

import dal.AppointmentDAO;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Appointment;
import model.User;

@WebServlet(name = "userAppoinmentsHistoryController", urlPatterns = {"/userAppoinmentsHistoryController"})
public class userAppoinmentsHistoryController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("view/CommonScreen/login.jsp");
            return;
        }

        int userId = user.getUserId();
        String dateFilter = request.getParameter("dateFilter");
        String packageFilter = request.getParameter("packageFilter");
        String appointmentIdParam = request.getParameter("appointmentId"); // 👈 khi user chọn 1 lịch cụ thể để xem chi tiết

        AppointmentDAO dao = new AppointmentDAO();

        // --- 1️⃣ Nếu có id lịch hẹn -> Lấy chi tiết 1 lịch hẹn ---
        if (appointmentIdParam != null && !appointmentIdParam.isEmpty()) {
            try {
                int appointmentId = Integer.parseInt(appointmentIdParam);
                Appointment appointmentDetail = dao.getAppointmentDetailById(appointmentId); // dùng đúng query SELECT bạn gửi ở trên

                if (appointmentDetail != null) {
                    request.setAttribute("appointmentDetail", appointmentDetail);
                } else {
                    request.setAttribute("errorMessage", "Không tìm thấy lịch hẹn với ID " + appointmentId);
                }

            } catch (NumberFormatException e) {
                request.setAttribute("errorMessage", "ID không hợp lệ.");
            }
        }

        // --- 2️⃣ Lấy danh sách lịch hẹn (lọc hoặc toàn bộ) ---
        List<Appointment> list;
        if ((dateFilter != null && !dateFilter.isEmpty()) ||
            (packageFilter != null && !packageFilter.isEmpty())) {
            list = dao.getAppointmentsByFilter(userId, dateFilter, packageFilter);
        } else {
            list = dao.getAppointmentsByUserId(userId);
        }

        // --- 3️⃣ Truyền dữ liệu sang JSP ---
        request.setAttribute("appointmentList", list);
        request.setAttribute("dateFilter", dateFilter);
        request.setAttribute("packageFilter", packageFilter);

        // Nếu có detail, JSP có thể hiển thị phần chi tiết riêng
        request.getRequestDispatcher("view/Customer/userAppointmentHistory.jsp").forward(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Hiển thị lịch sử lịch hẹn của người dùng, có thể lọc theo ngày, gói, và xem chi tiết từng lịch.";
    }
}