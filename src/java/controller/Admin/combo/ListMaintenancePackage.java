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

        // Get filter and search parameters
        String statusFilter = request.getParameter("status");
        String keyword = request.getParameter("keyword");
        String reload = request.getParameter("reload");

        // ✅ Handle reload - clear all filters
        if ("true".equals(reload)) {
            statusFilter = null;
            keyword = null;
        }

        // Get filtered data
        List<MaintenancePackageDetail> listPackage;
        if (keyword != null && !keyword.trim().isEmpty()) {
            // Search functionality - có thể kết hợp với status filter
            listPackage = dao.searchPackages(keyword.trim(), statusFilter);
        } else if (statusFilter != null && !statusFilter.isEmpty()) {
            // Filter by status only
            listPackage = dao.getPackagesByStatus(statusFilter);
        } else {
            // Get all packages
            listPackage = dao.getAll();
        }

        // Set attributes for JSP
        request.setAttribute("listPackage", listPackage);
        request.setAttribute("currentStatus", statusFilter);
        request.setAttribute("currentKeyword", keyword);
        request.setAttribute("totalCount", listPackage.size());

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
            if ("updateCombo".equals(action)) {
                try {
                    // Lấy dữ liệu từ form update
                    int packageId = Integer.parseInt(request.getParameter("packageId"));
                    String packageCode = request.getParameter("packageCode");
                    String name = request.getParameter("name");
                    String description = request.getParameter("description");
                    int kilometerMilestone = Integer.parseInt(request.getParameter("kilometerMilestone"));
                    int monthMilestone = Integer.parseInt(request.getParameter("monthMilestone"));

                    // ✅ Validation PackageCode format (should already be valid from existing data)
                    if (packageCode == null || !packageCode.trim().toUpperCase().startsWith("PKG-")) {
                        request.setAttribute("error", "Package Code phải bắt đầu bằng 'PKG-'");
                        request.setAttribute("listPackage", dao.getAll());
                        request.getRequestDispatcher("/view/Admin/MaintenancePackage.jsp").forward(request, response);
                        return;
                    }

                    // ✅ Định dạng số: chỉ thay đổi dấu phẩy (,) thành dấu chấm (.)
                    String rawBasePrice = request.getParameter("basePrice");
                    String rawDiscountPercent = request.getParameter("discountPercent");
                    String rawEstimatedDuration = request.getParameter("estimatedDurationHours");

                    // Validate không null
                    if (rawBasePrice == null || rawDiscountPercent == null || rawEstimatedDuration == null) {
                        request.setAttribute("error", "Lỗi: Thiếu thông tin bắt buộc!");
                        request.setAttribute("listPackage", dao.getAll());
                        request.getRequestDispatcher("/view/Admin/MaintenancePackage.jsp").forward(request, response);
                        return;
                    }

                    rawBasePrice = rawBasePrice.trim().replace(",", ".");
                    rawDiscountPercent = rawDiscountPercent.trim().replace(",", ".");
                    rawEstimatedDuration = rawEstimatedDuration.trim().replace(",", ".");

                    BigDecimal basePrice = new BigDecimal(rawBasePrice);
                    BigDecimal discountPercent = new BigDecimal(rawDiscountPercent);
                    BigDecimal estimatedDurationHours = new BigDecimal(rawEstimatedDuration);

                    // ✅ DEBUG: Log giá trị trước khi lưu
                    System.out.println("===== DEBUG UPDATE COMBO =====");
                    System.out.println("discountPercent input: " + request.getParameter("discountPercent"));
                    System.out.println("discountPercent processed: " + rawDiscountPercent);
                    System.out.println("discountPercent BigDecimal: " + discountPercent);
                    System.out.println("===============================");

                    String applicableBrand = request.getParameter("applicableBrand");

                    // ✅ Validate displayOrder (optional field)
                    String displayOrderParam = request.getParameter("displayOrder");
                    int displayOrder = 1; // Default value
                    if (displayOrderParam != null && !displayOrderParam.trim().isEmpty()) {
                        try {
                            displayOrder = Integer.parseInt(displayOrderParam);
                        } catch (NumberFormatException e) {
                            request.setAttribute("error", "Thứ tự hiển thị không hợp lệ!");
                            request.setAttribute("listPackage", dao.getAll());
                            request.getRequestDispatcher("/view/Admin/MaintenancePackage.jsp").forward(request, response);
                            return;
                        }
                    }

                    boolean isActive = Boolean.parseBoolean(request.getParameter("isActive"));

                    // ✅ Validate createdBy parameter
                    String createdByParam = request.getParameter("createdBy");
                    if (createdByParam == null || createdByParam.trim().isEmpty()) {
                        request.setAttribute("error", "Lỗi: Không xác định được người tạo. Vui lòng đăng nhập lại!");
                        request.setAttribute("listPackage", dao.getAll());
                        request.getRequestDispatcher("/view/Admin/MaintenancePackage.jsp").forward(request, response);
                        return;
                    }
                    int createdById = Integer.parseInt(createdByParam);

                    // Xử lý ảnh upload (optional)
                    Part imagePart = request.getPart("image");
                    String imagePath = null;

                    if (imagePart != null && imagePart.getSubmittedFileName() != null
                            && !imagePart.getSubmittedFileName().isEmpty()) {
                        String fileName = Paths.get(imagePart.getSubmittedFileName()).getFileName().toString();
                        String uploadPath = request.getServletContext().getRealPath("/uploads");
                        File uploadDir = new File(uploadPath);
                        if (!uploadDir.exists()) {
                            uploadDir.mkdir();
                        }
                        imagePath = "uploads/" + fileName;
                        imagePart.write(uploadPath + File.separator + fileName);
                    }

                    // Tạo đối tượng MaintenancePackage
                    MaintenancePackage mp = new MaintenancePackage();
                    mp.setPackageId(packageId);
                    mp.setPackageCode(packageCode.trim().toUpperCase());
                    mp.setName(name);
                    mp.setDescription(description);
                    mp.setKilometerMilestone(kilometerMilestone);
                    mp.setMonthMilestone(monthMilestone);
                    mp.setBasePrice(basePrice);
                    mp.setDiscountPercent(discountPercent);
                    // ✅ FinalPrice sẽ được tính tự động bởi computed column trong DB
                    mp.setEstimatedDurationHours(estimatedDurationHours);
                    mp.setApplicableBrands(applicableBrand);
                    if (imagePath != null) {
                        mp.setImage(imagePath);
                    }
                    mp.setDisplayOrder(displayOrder);
                    mp.setIsActive(isActive);

                    User createdBy = new User();
                    createdBy.setUserId(createdById);
                    mp.setCreatedBy(createdBy);

                    dao.updatePackage(mp);

                    request.setAttribute("success", "Cập nhật combo thành công!");
                    request.setAttribute("listPackage", dao.getAll());
                    request.getRequestDispatcher("/view/Admin/MaintenancePackage.jsp").forward(request, response);
                    return;

                } catch (Exception e) {
                    e.printStackTrace();
                    request.setAttribute("error", "Lỗi cập nhật combo: " + e.getMessage());
                    request.setAttribute("listPackage", dao.getAll());
                    request.getRequestDispatcher("/view/Admin/MaintenancePackage.jsp").forward(request, response);
                }
            }
            if ("addCombo".equals(action)) {
                try {
                    // Lấy dữ liệu từ form
                    String packageCode = request.getParameter("packageCode");
                    String name = request.getParameter("name");
                    String description = request.getParameter("description");
                    int kilometerMilestone = Integer.parseInt(request.getParameter("kilometerMilestone"));
                    int monthMilestone = Integer.parseInt(request.getParameter("monthMilestone"));

                    // ✅ Validation PackageCode format
                    if (packageCode == null || !packageCode.trim().toUpperCase().startsWith("PKG-")) {
                        request.setAttribute("error", "Package Code phải bắt đầu bằng 'PKG-'");
                        request.getRequestDispatcher("/view/Admin/MaintenancePackage.jsp").forward(request, response);
                        return;
                    }

                    // ✅ Định dạng số: chỉ thay đổi dấu phẩy (,) thành dấu chấm (.)
                    String rawBasePrice = request.getParameter("basePrice");
                    String rawDiscountPercent = request.getParameter("discountPercent");
                    String rawEstimatedDuration = request.getParameter("estimatedDurationHours");

                    // Validate không null
                    if (rawBasePrice == null || rawDiscountPercent == null || rawEstimatedDuration == null) {
                        request.setAttribute("error", "Lỗi: Thiếu thông tin bắt buộc!");
                        request.setAttribute("listPackage", dao.getAll());
                        request.getRequestDispatcher("/view/Admin/MaintenancePackage.jsp").forward(request, response);
                        return;
                    }

                    rawBasePrice = rawBasePrice.trim().replace(",", ".");
                    rawDiscountPercent = rawDiscountPercent.trim().replace(",", ".");
                    rawEstimatedDuration = rawEstimatedDuration.trim().replace(",", ".");

                    BigDecimal basePrice = new BigDecimal(rawBasePrice);
                    BigDecimal discountPercent = new BigDecimal(rawDiscountPercent);
                    BigDecimal estimatedDurationHours = new BigDecimal(rawEstimatedDuration);

                    String applicableBrand = request.getParameter("applicableBrand");

                    // ✅ Validate displayOrder (optional field)
                    String displayOrderParam = request.getParameter("displayOrder");
                    int displayOrder = 1; // Default value
                    if (displayOrderParam != null && !displayOrderParam.trim().isEmpty()) {
                        try {
                            displayOrder = Integer.parseInt(displayOrderParam);
                        } catch (NumberFormatException e) {
                            request.setAttribute("error", "Thứ tự hiển thị không hợp lệ!");
                            request.setAttribute("listPackage", dao.getAll());
                            request.getRequestDispatcher("/view/Admin/MaintenancePackage.jsp").forward(request, response);
                            return;
                        }
                    }

                    boolean isActive = Boolean.parseBoolean(request.getParameter("isActive"));

                    // ✅ Validate createdBy parameter
                    String createdByParam = request.getParameter("createdBy");
                    if (createdByParam == null || createdByParam.trim().isEmpty()) {
                        request.setAttribute("error", "Lỗi: Không xác định được người tạo. Vui lòng đăng nhập lại!");
                        request.setAttribute("listPackage", dao.getAll());
                        request.getRequestDispatcher("/view/Admin/MaintenancePackage.jsp").forward(request, response);
                        return;
                    }
                    int createdById = Integer.parseInt(createdByParam);

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
                    String imagePath = "";
                    Part imagePart = request.getPart("image");
                    if (imagePart != null && imagePart.getSubmittedFileName() != null
                            && !imagePart.getSubmittedFileName().isEmpty()) {
                        String fileName = Paths.get(imagePart.getSubmittedFileName()).getFileName().toString();
                        String uploadPath = request.getServletContext().getRealPath("/uploads");
                        File uploadDir = new File(uploadPath);
                        if (!uploadDir.exists()) {
                            uploadDir.mkdir();
                        }
                        imagePath = "uploads/" + fileName;
                        imagePart.write(uploadPath + File.separator + fileName);
                    }

                    // Tạo đối tượng MaintenancePackage
                    MaintenancePackage mp = new MaintenancePackage();
                    mp.setPackageCode(packageCode.trim().toUpperCase()); // Normalize package code
                    mp.setName(name);
                    mp.setDescription(description);
                    mp.setKilometerMilestone(kilometerMilestone);
                    mp.setMonthMilestone(monthMilestone);
                    mp.setBasePrice(basePrice);
                    mp.setDiscountPercent(discountPercent);
                    // ✅ FinalPrice sẽ được tính tự động bởi computed column trong DB
                    mp.setEstimatedDurationHours(estimatedDurationHours);
                    mp.setApplicableBrands(applicableBrand);
                    mp.setImage(imagePath);
                    mp.setDisplayOrder(displayOrder);
                    mp.setIsActive(isActive);

                    User createdBy = new User();
                    createdBy.setUserId(createdById);
                    mp.setCreatedBy(createdBy);

                    dao.insertPackage(mp);

                    request.setAttribute("success", "Thêm combo thành công!");
                    request.setAttribute("listPackage", dao.getAll());
                    request.getRequestDispatcher("/view/Admin/MaintenancePackage.jsp").forward(request, response);
                    return;

                } catch (Exception e) {
                    e.printStackTrace();
                    request.setAttribute("error", "Lỗi xử lý yêu cầu: " + e.getMessage());
                    request.setAttribute("listPackage", dao.getAll());
                    request.getRequestDispatcher("/view/Admin/MaintenancePackage.jsp").forward(request, response);
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
