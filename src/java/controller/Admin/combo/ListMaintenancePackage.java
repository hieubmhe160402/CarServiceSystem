/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.Admin.combo;

import dal.MaintenancePackageDAO;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Paths;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.io.PrintWriter;
import model.MaintenancePackageDetail;
import model.MaintenancePackage;
import model.User;

/**
 *
 * @author nxtru
 */
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 10, // 10MB
        maxRequestSize = 1024 * 1024 * 50 // 50MB
)
@WebServlet(name = "ListMaintenancePackage", urlPatterns = {"/maintenancePackage"})
public class ListMaintenancePackage extends HttpServlet {

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
            out.println("<title>Servlet ListMaintenancePackage</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ListMaintenancePackage at " + request.getContextPath() + "</h1>");
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
        MaintenancePackageDAO dao = new MaintenancePackageDAO();
        request.setAttribute("listPackage", dao.getAll());
        request.getRequestDispatcher("/view/Admin/MaintenancePackage.jsp").forward(request, response);

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
        String action = request.getParameter("action");
        MaintenancePackageDAO dao = new MaintenancePackageDAO();
        try {
            if ("changeStatus".equals(action)) {
                int packageId = Integer.parseInt(request.getParameter("packageId"));
                boolean newStatus = Boolean.parseBoolean(request.getParameter("status"));
                dao.updateStatus(packageId, newStatus);
                response.sendRedirect("maintenancePackage");
                return;
            }
            if ("addCombo".equals(action)) {
                try {
                    // Lấy dữ liệu từ form
                    String packageCode = request.getParameter("packageCode");
                    String name = request.getParameter("name");
                    String description = request.getParameter("description");
                    int kilometerMilestone = Integer.parseInt(request.getParameter("kilometerMilestone"));
                    int monthMilestone = Integer.parseInt(request.getParameter("monthMilestone"));

                    // ✅ Sửa định dạng số để tránh lỗi SQL numeric overflow
                    String basePriceStr = request.getParameter("basePrice").replace(".", "").replace(",", ".");
                    String discountPercentStr = request.getParameter("discountPercent").replace(".", "").replace(",", ".");
                    String estimatedDurationStr = request.getParameter("estimatedDurationHours").replace(".", "").replace(",", ".");

                    String rawBasePrice = request.getParameter("basePrice").trim();
                    rawBasePrice = rawBasePrice.replace(".", "").replace(",", ".");
                    BigDecimal basePrice = new BigDecimal(rawBasePrice);
                    BigDecimal discountPercent = new BigDecimal(discountPercentStr);
                    BigDecimal estimatedDurationHours = new BigDecimal(estimatedDurationStr);

                    String applicableBrand = request.getParameter("applicableBrand");
                    int displayOrder = Integer.parseInt(request.getParameter("displayOrder"));
                    boolean isActive = Boolean.parseBoolean(request.getParameter("isActive"));
                    int createdById = Integer.parseInt(request.getParameter("createdBy"));

                    System.out.println("===== DEBUG ADD COMBO =====");
                    System.out.println("packageCode: " + packageCode);
                    System.out.println("name: " + name);
                    System.out.println("kilometerMilestone: " + kilometerMilestone);
                    System.out.println("monthMilestone: " + monthMilestone);
                    System.out.println("basePrice: " + basePrice);
                    System.out.println("discountPercent: " + discountPercent);
                    System.out.println("estimatedDurationHours: " + estimatedDurationHours);
                    System.out.println("applicableBrand: " + applicableBrand);
                    System.out.println("displayOrder: " + displayOrder);
                    System.out.println("isActive: " + isActive);
                    System.out.println("createdById: " + createdById);
                    System.out.println("============================");

                    // Xử lý ảnh upload
                    Part imagePart = request.getPart("image");
                    String fileName = Paths.get(imagePart.getSubmittedFileName()).getFileName().toString();
                    String uploadPath = request.getServletContext().getRealPath("/uploads");
                    File uploadDir = new File(uploadPath);
                    if (!uploadDir.exists()) {
                        uploadDir.mkdir();
                    }

                    String imagePath = "uploads/" + fileName;
                    if (!fileName.isEmpty()) {
                        imagePart.write(uploadPath + File.separator + fileName);
                    }

                    // Tạo đối tượng MaintenancePackage
                    MaintenancePackage mp = new MaintenancePackage();
                    mp.setPackageCode(packageCode);
                    mp.setName(name);
                    mp.setDescription(description);
                    mp.setKilometerMilestone(kilometerMilestone);
                    mp.setMonthMilestone(monthMilestone);
                    mp.setBasePrice(basePrice);
                    mp.setDiscountPercent(discountPercent);
                    mp.setEstimatedDurationHours(estimatedDurationHours);
                    mp.setApplicableBrands(applicableBrand);
                    mp.setImage(imagePath);
                    mp.setDisplayOrder(displayOrder);
                    mp.setIsActive(isActive);

                    User createdBy = new User();
                    createdBy.setUserId(createdById);
                    mp.setCreatedBy(createdBy);

                    dao.insertPackage(mp);

                    response.sendRedirect("maintenancePackage");
                    return;

                } catch (Exception e) {
                    e.printStackTrace();
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                            "Lỗi xử lý yêu cầu: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Lỗi xử lý yêu cầu: " + e.getMessage());
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
