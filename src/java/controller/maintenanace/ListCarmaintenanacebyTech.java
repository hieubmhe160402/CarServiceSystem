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
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import model.CarMaintenance;
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

        String action = request.getParameter("action");
        CarMaintenanaceByTechDAO dao = new CarMaintenanaceByTechDAO();
        
        // Xử lý action detail - load dữ liệu chi tiết
        if ("detail".equals(action)) {
            String maintenanceIdParam = request.getParameter("maintenanceId");
            if (maintenanceIdParam != null) {
                try {
                    int maintenanceId = Integer.parseInt(maintenanceIdParam);
                    CarMaintenance detail = dao.getDetailServiceMaintenanceById(maintenanceId);
                    List<Map<String, Object>> products = dao.getMaintenanceProducts(maintenanceId);
                    
                    request.setAttribute("detail", detail);
                    request.setAttribute("products", products);
                    request.setAttribute("maintenanceId", maintenanceId);
                } catch (NumberFormatException e) {
                    // Ignore
                }
            }
        }
        
        // Xử lý action addService/addPart - load danh sách sản phẩm để hiển thị trong popup
        if ("addService".equals(action) || "addPart".equals(action)) {
            String maintenanceIdParam = request.getParameter("maintenanceId");
            if (maintenanceIdParam != null) {
                try {
                    int maintenanceId = Integer.parseInt(maintenanceIdParam);
                    String productType = "addService".equals(action) ? "SERVICE" : "PART";
                    List<Map<String, Object>> productList = dao.getProductsByType(productType);
                    
                    // Load lại detail và products để hiển thị modal
                    CarMaintenance detail = dao.getDetailServiceMaintenanceById(maintenanceId);
                    List<Map<String, Object>> products = dao.getMaintenanceProducts(maintenanceId);
                    
                    request.setAttribute("detail", detail);
                    request.setAttribute("products", products);
                    request.setAttribute("maintenanceId", maintenanceId);
                    request.setAttribute("productList", productList);
                    request.setAttribute("addType", action); // "addService" hoặc "addPart"
                } catch (NumberFormatException e) {
                    // Ignore
                }
            }
        }

        // Lấy technician ID từ user đã đăng nhập
        int technicianId = user.getUserId();

        // Chỉ tạo dao mới nếu chưa có (trường hợp không vào action nào)
        if (dao == null) {
            dao = new CarMaintenanaceByTechDAO();
        }
        
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
            } else if ("addService".equals(action)) {
                // Xử lý thêm dịch vụ
                String productIdParam = request.getParameter("productId");
                String quantityParam = request.getParameter("quantity");
                String unitPriceParam = request.getParameter("unitPrice");
                String notes = request.getParameter("notes");
                
                if (productIdParam != null && quantityParam != null && unitPriceParam != null) {
                    int productId = Integer.parseInt(productIdParam);
                    BigDecimal quantity = new BigDecimal(quantityParam);
                    BigDecimal unitPrice = new BigDecimal(unitPriceParam);
                    success = dao.addServiceDetail(maintenanceId, productId, quantity, unitPrice, notes);
                }
                
                // Redirect về trang detail để hiển thị lại
                response.sendRedirect("listcarmaintenanacebytech?action=detail&maintenanceId=" + maintenanceId);
                return;
            } else if ("addPart".equals(action)) {
                // Xử lý thêm linh kiện
                String productIdParam = request.getParameter("productId");
                String quantityParam = request.getParameter("quantity");
                String unitPriceParam = request.getParameter("unitPrice");
                String notes = request.getParameter("notes");
                
                if (productIdParam != null && quantityParam != null) {
                    int productId = Integer.parseInt(productIdParam);
                    int quantity = Integer.parseInt(quantityParam);

                    BigDecimal unitPrice;
                    if (unitPriceParam != null && !unitPriceParam.trim().isEmpty()) {
                        unitPrice = new BigDecimal(unitPriceParam);
                    } else {
                        unitPrice = dao.getProductPriceById(productId);
                    }

                    if (unitPrice != null) {
                        success = dao.addServicePartDetail(maintenanceId, productId, quantity, unitPrice, notes);
                    }
                }
                
                // Redirect về trang detail để hiển thị lại
                response.sendRedirect("listcarmaintenanacebytech?action=detail&maintenanceId=" + maintenanceId);
                return;
            } else if ("deleteService".equals(action)) {
                // Xử lý xóa dịch vụ
                String serviceDetailIdParam = request.getParameter("serviceDetailId");
                if (serviceDetailIdParam != null) {
                    int serviceDetailId = Integer.parseInt(serviceDetailIdParam);
                    success = dao.deleteServiceDetail(serviceDetailId, maintenanceId);
                }
                response.sendRedirect("listcarmaintenanacebytech?action=detail&maintenanceId=" + maintenanceId);
                return;
            } else if ("deletePart".equals(action)) {
                // Xử lý xóa linh kiện
                String servicePartDetailIdParam = request.getParameter("servicePartDetailId");
                if (servicePartDetailIdParam != null) {
                    int servicePartDetailId = Integer.parseInt(servicePartDetailIdParam);
                    success = dao.deleteServicePartDetail(servicePartDetailId, maintenanceId);
                }
                response.sendRedirect("listcarmaintenanacebytech?action=detail&maintenanceId=" + maintenanceId);
                return;
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
