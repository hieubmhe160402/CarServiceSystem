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

        AppointmentDAO dao = new AppointmentDAO();
        List<Appointment> list;

        // Nếu có lọc thì lấy theo điều kiện
        if ((dateFilter != null && !dateFilter.isEmpty()) || 
            (packageFilter != null && !packageFilter.isEmpty())) {
            list = dao.getAppointmentsByFilter(userId, dateFilter, packageFilter);
        } else {
            list = dao.getAppointmentsByUserId(userId);
        }

        request.setAttribute("appointmentList", list);
        request.setAttribute("dateFilter", dateFilter);
        request.setAttribute("packageFilter", packageFilter);
        request.getRequestDispatcher("view/Customer/userAppointmentHistory.jsp").forward(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Hiển thị lịch sử lịch hẹn của người dùng, có thể lọc theo ngày và gói.";
    }
}