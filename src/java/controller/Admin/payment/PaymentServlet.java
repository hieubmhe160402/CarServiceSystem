/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.Admin.payment;

import dal.PaymentDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import model.CarMaintenance;

/**
 *
 * @author phamp
 */
@WebServlet(name = "PaymentServlet", urlPatterns = {"/payments"})
public class PaymentServlet extends HttpServlet {

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
            out.println("<title>Servlet PaymentServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet PaymentServlet at " + request.getContextPath() + "</h1>");
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
        try {
            PaymentDAO paymentDAO = new PaymentDAO();
            
            // Lấy tham số search và filter
            String search = request.getParameter("search");
            String status = request.getParameter("status");
            String action = request.getParameter("action");
            
            String searchParam = (search != null) ? search.trim() : "";
            String statusParam = (status != null) ? status : "";
            
            List<Map<String, Object>> payments = paymentDAO.getAllPaymentTransactions(searchParam, statusParam);
            
            request.setAttribute("payments", payments);
            request.setAttribute("searchKeyword", searchParam);
            request.setAttribute("selectedStatus", statusParam);

            if ("details".equals(action)) {
                try {
                    int transactionId = Integer.parseInt(request.getParameter("transactionId"));
                    Map<String, Object> paymentDetail = paymentDAO.getPaymentTransactionDetails(transactionId);
                    if (paymentDetail != null) {
                        request.setAttribute("paymentDetail", paymentDetail);

                        Object maintenanceIdObj = paymentDetail.get("maintenanceId");
                        if (maintenanceIdObj instanceof Number) {
                            int maintenanceId = ((Number) maintenanceIdObj).intValue();

                            CarMaintenance detail = paymentDAO.getMaintenanceDetailForPayment(maintenanceId);
                            if (detail != null) {
                                request.setAttribute("detail", detail);
                                request.setAttribute("products", paymentDAO.getMaintenanceProductsForPayment(maintenanceId));
                            }
                        }
                    }
                } catch (NumberFormatException ex) {
                    request.setAttribute("error", "Mã giao dịch không hợp lệ.");
                }
            }
            
            // Xử lý thông báo từ session
            HttpSession session = request.getSession();
            String successMessage = (String) session.getAttribute("successMessage");
            String errorMessage = (String) session.getAttribute("errorMessage");
            
            if (successMessage != null) {
                request.setAttribute("success", successMessage);
                session.removeAttribute("successMessage");
            }
            
            if (errorMessage != null) {
                request.setAttribute("error", errorMessage);
                session.removeAttribute("errorMessage");
            }
            
            request.getRequestDispatcher("/view/Admin/ListAllPayment.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Đã xảy ra lỗi khi tải dữ liệu: " + e.getMessage());
            request.getRequestDispatcher("/view/Admin/ListAllPayment.jsp").forward(request, response);
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
        try {
            PaymentDAO paymentDAO = new PaymentDAO();
            String action = request.getParameter("action");
            
            // Xử lý action "markAsDone" - Đã nhận tiền
            if ("markAsDone".equals(action)) {
                try {
                    int transactionId = Integer.parseInt(request.getParameter("transactionId"));
                    boolean success = paymentDAO.updatePaymentStatus(transactionId, "DONE");
                    
                    // Lấy lại các tham số search và filter để giữ lại khi redirect
                    String search = request.getParameter("search");
                    String status = request.getParameter("status");
                    
                    HttpSession session = request.getSession();
                    if (success) {
                        session.setAttribute("successMessage", "Đã cập nhật trạng thái thành công");
                    } else {
                        session.setAttribute("errorMessage", "Không thể cập nhật trạng thái");
                    }
                    
                    // Tạo URL redirect với các tham số search và filter
                    StringBuilder redirectUrl = new StringBuilder("payments");
                    if (search != null && !search.trim().isEmpty()) {
                        redirectUrl.append("?search=").append(java.net.URLEncoder.encode(search.trim(), "UTF-8"));
                        if (status != null && !status.trim().isEmpty()) {
                            redirectUrl.append("&status=").append(status);
                        }
                    } else if (status != null && !status.trim().isEmpty()) {
                        redirectUrl.append("?status=").append(status);
                    }
                    
                    response.sendRedirect(redirectUrl.toString());
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                    HttpSession session = request.getSession();
                    session.setAttribute("errorMessage", "Đã xảy ra lỗi: " + e.getMessage());
                    response.sendRedirect("payments");
                    return;
                }
            }
            
            doGet(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            HttpSession session = request.getSession();
            session.setAttribute("errorMessage", "Đã xảy ra lỗi: " + e.getMessage());
            response.sendRedirect("payments");
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
