/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.Admin.dashboard;

import dal.DashboardDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author MinHeee
 */
@WebServlet(name = "dashboardServlet", urlPatterns = {"/dashboard"})
public class dashboardServlet extends HttpServlet {

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
            out.println("<title>Servlet dashboardServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet dashboardServlet at " + request.getContextPath() + "</h1>");
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

        DashboardDAO dd = new DashboardDAO();

        // --- 1. Dữ liệu CARD (Code gốc của bạn - Giữ nguyên) ---
        int totalCustomers = dd.getTotalCustomers();
        int totalCarsProcessing = dd.getTotalCarsProcessing();
        double totalRevenueToday = dd.getTotalRevenueToday();
        double totalRevenueThisMonth = dd.getTotalRevenueThisMonth();

        request.setAttribute("totalCustomers", totalCustomers);
        request.setAttribute("totalCarsProcessing", totalCarsProcessing);
        request.setAttribute("totalRevenueToday", totalRevenueToday);
        request.setAttribute("totalRevenueThisMonth", totalRevenueThisMonth);

        // --- 1.5. (PHẦN MỚI ĐƯỢC THÊM VÀO) ---
        // --- Dữ liệu cho BIỂU ĐỒ DOANH THU (Bar Chart) ---
        int currentYear = java.time.LocalDate.now().getYear();
        Map<Integer, Double> revenueMap = dd.getMonthlyRevenueByYear(currentYear);

        // Tạo 1 list 12 tháng, tất cả ban đầu là 0.0
        List<Double> monthlyRevenueData = new ArrayList<>(java.util.Collections.nCopies(12, 0.0));

        // Cập nhật doanh thu cho những tháng có dữ liệu
        for (Map.Entry<Integer, Double> entry : revenueMap.entrySet()) {
            monthlyRevenueData.set(entry.getKey() - 1, entry.getValue()); // Set vào index 0-11
        }
        // Convert List<Double> thành JSON array string
        StringBuilder revenueJson = new StringBuilder("[");
        for (int i = 0; i < monthlyRevenueData.size(); i++) {
            if (i > 0) revenueJson.append(",");
            revenueJson.append(monthlyRevenueData.get(i));
        }
        revenueJson.append("]");
        request.setAttribute("monthlyRevenueData", revenueJson.toString());
        // --- KẾT THÚC PHẦN THÊM MỚI ---

        // --- 2. Dữ liệu BIỂU ĐỒ DỊCH VỤ (Code gốc của bạn - Giữ nguyên) ---
        List<Map<String, Object>> popularServices = dd.getPopularServices(5);

        // (Phần xử lý thêm vào)
        List<String> serviceLabels = new ArrayList<>();
        List<Integer> serviceCounts = new ArrayList<>();
        for (Map<String, Object> row : popularServices) {
            serviceLabels.add((String) row.get("serviceName"));
            serviceCounts.add((Integer) row.get("usageCount"));
        }
        // Convert labels thành JSON array string với escape cho các ký tự đặc biệt
        StringBuilder labelsJson = new StringBuilder("[");
        for (int i = 0; i < serviceLabels.size(); i++) {
            if (i > 0) labelsJson.append(",");
            String label = serviceLabels.get(i).replace("\"", "\\\"").replace("\n", "\\n");
            labelsJson.append("\"").append(label).append("\"");
        }
        labelsJson.append("]");
        
        // Convert counts thành JSON array string
        StringBuilder countsJson = new StringBuilder("[");
        for (int i = 0; i < serviceCounts.size(); i++) {
            if (i > 0) countsJson.append(",");
            countsJson.append(serviceCounts.get(i));
        }
        countsJson.append("]");
        
        request.setAttribute("popularServiceLabels", labelsJson.toString());
        request.setAttribute("popularServiceData", countsJson.toString());

        // --- 3. Dữ liệu BẢNG (Code gốc của bạn - Giữ nguyên) ---
        List<Map<String, Object>> recentCustomers = dd.getRecentCustomers(5);
        List<Map<String, Object>> recentAppointments = dd.getRecentAppointments(5);

        request.setAttribute("recentCustomers", recentCustomers);
        request.setAttribute("recentAppointments", recentAppointments);

        // --- 4. Chuyển tiếp (Code gốc của bạn - Giữ nguyên) ---
        request.getRequestDispatcher("/view/Admin/HomePageForAdmin.jsp").forward(request, response);
    }

    /**
     * @Override protected void doGet(HttpServletRequest request,
     * HttpServletResponse response) throws ServletException, IOException {
     * DashboardDAO dd = new DashboardDAO(); int totalCustomers =
     * dd.getTotalCustomers(); request.setAttribute("totalCustomers",
     * totalCustomers);
     * request.getRequestDispatcher("/view/Admin/HomePageForAdmin.jsp").forward(request,
     * response);
     *
     * }
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
        processRequest(request, response);
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
