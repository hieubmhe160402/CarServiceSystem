/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.maintenanace;

import dal.CarMaintenanceDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import model.CarMaintenance;
import model.User;

/**
 *
 * @author MinHeee
 */
@WebServlet(name = "ListCarmaintenance", urlPatterns = {"/listCarmaintenance"})
public class ListCarmaintenance extends HttpServlet {

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
            out.println("<title>Servlet ListCarmaintenance</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ListCarmaintenance at " + request.getContextPath() + "</h1>");
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

        CarMaintenanceDAO dao = new CarMaintenanceDAO();
        String action = request.getParameter("action");

        try {
            // ✅ Nếu bấm "Chọn" (mở danh sách nhân viên)
            if ("assign".equals(action)) {
                int maintenanceId = Integer.parseInt(request.getParameter("maintenanceId"));
                CarMaintenance detail = dao.getDetailServiceMaintenanceById(maintenanceId);
                List<User> technicians = dao.getTechnicians();
                List<CarMaintenance> maintenances = dao.getAllCarMaintenances();
                List<Map<String, Object>> products = dao.getMaintenanceProducts(maintenanceId);
                request.setAttribute("products", products);

                request.setAttribute("maintenances", maintenances);
                request.setAttribute("detail", detail);
                request.setAttribute("technicians", technicians);
                request.setAttribute("openModal", true);
                request.getRequestDispatcher("/view/carmaintenance/managerCarmaintenanace.jsp")
                        .forward(request, response);
                return;
            }

            // ✅ Nếu không có action (hiển thị danh sách chung)
            List<CarMaintenance> maintenances = dao.getAllCarMaintenances();
            request.setAttribute("maintenances", maintenances);
            request.getRequestDispatcher("/view/carmaintenance/managerCarmaintenanace.jsp")
                    .forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Đã xảy ra lỗi khi tải dữ liệu: " + e.getMessage());
            request.getRequestDispatcher("/view/carmaintenance/managerCarmaintenanace.jsp")
                    .forward(request, response);
        }
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
        CarMaintenanceDAO dao = new CarMaintenanceDAO();

        if ("cancel".equals(action)) {
            int maintenanceId = Integer.parseInt(request.getParameter("maintenanceId"));
            dao.updateStatus(maintenanceId, "CANCELLED");
            response.sendRedirect("listCarmaintenance");
            return;
        }
        if ("confirmAssign".equals(action) || "assignTechnician".equals(action)) {
            int maintenanceId = Integer.parseInt(request.getParameter("maintenanceId"));
            int technicianId = Integer.parseInt(request.getParameter("technicianId"));
            dao.assignTechnician(maintenanceId, technicianId);
            response.sendRedirect("listCarmaintenance");
            return;
        }

        // Sau khi update xong, load lại danh sách
        List<CarMaintenance> list = dao.getAllCarMaintenances();
        request.setAttribute("maintenances", list);
        request.getRequestDispatcher("/view/carmaintenance/managerCarmaintenanace.jsp").forward(request, response);
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
