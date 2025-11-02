/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.maintenanace;

import dal.CarMaintenanaceByTechDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import model.User;

/**
 *
 * @author nxtru
 */
@WebServlet(name = "ListCarmaintenanacebyTech", urlPatterns = {"/listcarmaintenanacebytech"})
public class ListCarmaintenanacebyTech extends HttpServlet {

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
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        // Kiểm tra đăng nhập
        if (user == null) {
            response.sendRedirect("authController?action=login");
            return;
        }

        // Lấy technician ID từ user đã đăng nhập
        int technicianId = user.getUserId();

        CarMaintenanaceByTechDAO dao = new CarMaintenanaceByTechDAO();
        List<Map<String, Object>> maintenanceList = dao.getCarMaintenanceByTechnicianId(technicianId);

        request.setAttribute("maintenanceList", maintenanceList);
        request.getRequestDispatcher("view/Technicianview/managerCarmaintenanaceByTech.jsp").forward(request, response);
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
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        // Kiểm tra đăng nhập
        if (user == null) {
            response.sendRedirect("authController?action=login");
            return;
        }

        String action = request.getParameter("action");
        String maintenanceIdParam = request.getParameter("maintenanceId");

        if (action == null || maintenanceIdParam == null) {
            response.sendRedirect("listcarmaintenanacebytech");
            return;
        }

        try {
            int maintenanceId = Integer.parseInt(maintenanceIdParam);       
            CarMaintenanaceByTechDAO dao = new CarMaintenanaceByTechDAO();
            boolean success = false;

            if ("cancel".equals(action)) {
                success = dao.updateStatus(maintenanceId, "CANCELLED");
            } else if ("complete".equals(action)) {
                success = dao.updateStatus(maintenanceId, "COMPLETED");
            }

            // Redirect về trang danh sách sau khi update
            response.sendRedirect("listcarmaintenanacebytech");

        } catch (NumberFormatException e) {
            response.sendRedirect("listcarmaintenanacebytech");
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "List Car Maintenance by Technician";
    }
}
