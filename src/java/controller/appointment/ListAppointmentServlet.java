/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.appointment;

import dal.AppointmentDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import model.Appointment;
import model.Car;
import model.MaintenancePackage;
import model.User;

/**
 *
 * @author MinHeee
 */
@WebServlet(name = "ListAppointmentServlet", urlPatterns = {"/listAppointmentServlet"})
public class ListAppointmentServlet extends HttpServlet {

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
            out.println("<title>Servlet ListAppointmentServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ListAppointmentServlet at " + request.getContextPath() + "</h1>");
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

        String status = request.getParameter("status");
        String packageIdStr = request.getParameter("packageId");
        String pageStr = request.getParameter("page"); // ✅ lấy số trang hiện tại

        AppointmentDAO dao = new AppointmentDAO();

        List<Car> cars = dao.getAllCustomerCars();
        List<MaintenancePackage> packages = dao.getAllPackages();

        List<Appointment> list;

        // 🔴 Nếu có flag noFilter (từ lỗi validation) → KHÔNG filter
        boolean noFilter = request.getAttribute("noFilter") != null
                && (boolean) request.getAttribute("noFilter");

        // ✅ Nếu có filter theo status hoặc package (và không phải lỗi validation)
        if (!noFilter && ((status != null && !status.isEmpty()) || (packageIdStr != null && !packageIdStr.isEmpty()))) {
            Integer packageId = (packageIdStr != null && !packageIdStr.isEmpty())
                    ? Integer.parseInt(packageIdStr)
                    : null;

            list = dao.getAppointmentsFiltered(status, packageId);
        } else {
            list = dao.getAllAppointments();
        }

        // ✅ Phân trang (10 dòng mỗi trang)
        int pageSize = 10;
        int page = 1; // trang mặc định

        if (pageStr != null) {
            try {
                page = Integer.parseInt(pageStr);
            } catch (NumberFormatException e) {
                page = 1;
            }
        }

        int totalAppointments = list.size();
        int totalPages = (int) Math.ceil((double) totalAppointments / pageSize);

        // ✅ Giới hạn trang hợp lệ
        if (page < 1) {
            page = 1;
        }
        if (page > totalPages && totalPages > 0) {
            page = totalPages;
        }

        int start = (page - 1) * pageSize;
        int end = Math.min(start + pageSize, totalAppointments);

        List<Appointment> pagedAppointments = list.subList(start, end);

        // ✅ Gửi dữ liệu sang JSP
        request.setAttribute("cars", cars);
        request.setAttribute("appointments", pagedAppointments);

        // 🔴 Nếu có flag noFilter (từ lỗi validation) → KHÔNG hiển thị filter
        if (noFilter) {
            request.setAttribute("selectedStatus", null);
            request.setAttribute("selectedPackageId", null);
        } else {
            request.setAttribute("selectedStatus", status);
            request.setAttribute("selectedPackageId", packageIdStr);
        }

        request.setAttribute("packages", packages);

        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);

        request.getRequestDispatcher("view/appointment/manageAppointment.jsp").forward(request, response);
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

        String action = request.getParameter("action");
        AppointmentDAO dao = new AppointmentDAO();

        try {
            if ("cancel".equals(action)) {
                int appointmentId = Integer.parseInt(request.getParameter("appointmentId"));
                dao.cancelAppointment(appointmentId);

            } else if ("confirm".equals(action)) {
                int appointmentId = Integer.parseInt(request.getParameter("appointmentId"));

                HttpSession session = request.getSession();
                User loginUser = (User) session.getAttribute("user");

                if (loginUser == null) {
                    response.sendRedirect("authController?action=login");
                    return;
                }

                // ✅ Gọi hàm vừa confirm Appointment vừa tạo CarMaintenance
                dao.confirmAppointmentAndCreateMaintenance(appointmentId, loginUser);

                // ✅ Có thể thêm message hiển thị (nếu muốn)
                request.getSession().setAttribute("successMessage", "Đã xác nhận lịch hẹn và tạo phiếu bảo dưỡng thành công!");
            } else if ("view".equals(action)) {
                int appointmentId = Integer.parseInt(request.getParameter("appointmentId"));
                Appointment appointment = dao.getAppointmentById(appointmentId); // cần viết hàm này trong DAO

                request.setAttribute("appointmentDetail", appointment);
                request.setAttribute("showDetailModal", true);

                // Gọi lại doGet để nạp danh sách và hiển thị popup
                doGet(request, response);
                return;
            } else if ("add".equals(action)) {
                int carId = Integer.parseInt(request.getParameter("carId"));
                int packageId = Integer.parseInt(request.getParameter("packageId"));
                String appointmentDateStr = request.getParameter("appointmentDate");
                String notes = request.getParameter("notes");

                HttpSession session = request.getSession();
                User loginUser = (User) session.getAttribute("user");

                if (loginUser == null) {
                    response.sendRedirect("authController?action=login");
                    return;
                }

                try {
                    LocalDateTime appointmentDateTime = LocalDateTime.parse(appointmentDateStr);
                    LocalDateTime now = LocalDateTime.now();

                    LocalTime time = appointmentDateTime.toLocalTime();
                    LocalTime openTime = LocalTime.of(9, 0);
                    LocalTime closeTime = LocalTime.of(20, 0);

                    // ✅ Điều kiện 1: Không được đặt trong quá khứ
                    if (appointmentDateTime.isBefore(now)) {
                        request.setAttribute("errorTimeMessage", "⛔ Không thể đặt lịch trong quá khứ!");
                    } // ✅ Điều kiện 2: Phải cách hiện tại ít nhất 1 tiếng
                    else if (appointmentDateTime.isBefore(now.plusHours(1))) {
                        request.setAttribute("errorTimeMessage", "⛔ Thời gian đặt lịch phải cách hiện tại ít nhất 1 tiếng!");
                    } // ✅ Điều kiện 3: Chỉ được đặt trong 7 ngày tới
                    else if (appointmentDateTime.isAfter(now.plusDays(7))) {
                        request.setAttribute("errorTimeMessage", "⛔ Chỉ được đặt lịch trong vòng 7 ngày tới!");
                    } // ✅ Điều kiện 4: Giờ mở cửa
                    else if (time.isBefore(openTime) || time.isAfter(closeTime)) {
                        request.setAttribute("errorTimeMessage", "⛔ Chỉ có thể đặt lịch trong khoảng từ 9:00 sáng đến 8:00 tối.");
                    } else if (dao.isCarAlreadyBookedOnDate(carId, appointmentDateTime)) {
                        request.setAttribute("errorTimeMessage", "⛔ Xe này đã có lịch hẹn trong ngày này rồi!");
                    }
                    if (dao.isAppointmentTimeConflict(carId, appointmentDateTime)) {
                        request.setAttribute("errorTimeMessage", "🚫 Xe này đã có lịch hẹn trong khoảng thời gian gần đó!");
                    }

                    // Nếu có lỗi thì quay lại form (KHÔNG FILTER)
                    if (request.getAttribute("errorTimeMessage") != null) {
                        // Giữ lại dữ liệu form
                        request.setAttribute("showAddModal", true);
                        request.setAttribute("selectedCarId", carId);
                        // 🔴 XÓA selectedPackageId để dropdown bảo dưỡng reset về mặc định
                        request.setAttribute("selectedPackageId", null);
                        request.setAttribute("enteredDate", appointmentDateStr);
                        request.setAttribute("enteredNotes", notes);

                        // 🔴 Set flag để doGet() biết không filter
                        request.setAttribute("noFilter", true);

                        // Gọi lại doGet để nạp lại dữ liệu danh sách (show all, KHÔNG filter)
                        doGet(request, response);
                        return;
                    }

                    // ✅ Nếu hợp lệ -> thêm lịch hẹn
                    Appointment appointment = new Appointment();
                    Car car = new Car();
                    car.setCarId(carId);
                    appointment.setCar(car);

                    MaintenancePackage pkg = new MaintenancePackage();
                    pkg.setPackageId(packageId);
                    appointment.setRequestedPackage(pkg);

                    appointment.setAppointmentDate(appointmentDateStr);
                    appointment.setNotes(notes);
                    appointment.setStatus("PENDING");
                    appointment.setCreatedBy(loginUser);
                    dao.addAppointment(appointment);

                } catch (DateTimeParseException e) {
                    request.setAttribute("errorTimeMessage", "❌ Định dạng ngày giờ không hợp lệ.");
                    request.setAttribute("showAddModal", true);
                    doGet(request, response);
                    return;
                }
            }

            // ✅ Quay lại trang danh sách (KHÔNG giữ filter cũ, show all appointments)
            request.getSession().setAttribute("successMessage", "Đã thêm lịch hẹn thành công!");
            response.sendRedirect("listAppointmentServlet");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Đã xảy ra lỗi khi xử lý yêu cầu: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
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
