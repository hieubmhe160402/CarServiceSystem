/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.web.supplierproduct;

import dal.SupplierProductDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import model.Product;
import model.Supplier;
import model.SupplierProduct;

/**
 *
 * @author MinHeee
 */
@WebServlet(name = "SupplierProductServlet", urlPatterns = {"/supplierproduct"})
public class SupplierProductServlet extends HttpServlet {

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
            out.println("<title>Servlet SupplierProductServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet SupplierProductServlet at " + request.getContextPath() + "</h1>");
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

        SupplierProductDAO dao = new SupplierProductDAO();

        // --- Phân trang ---
        int page = 1; // trang mặc định
        int pageSize = 10; // số dòng mỗi trang

        // Lấy page từ request nếu có
        String pageParam = request.getParameter("page");
        if (pageParam != null) {
            try {
                page = Integer.parseInt(pageParam);
            } catch (NumberFormatException e) {
                page = 1;
            }
        }

        // Đếm tổng số bản ghi
        int totalRecords = dao.getTotalSupplierProductCount();
        int totalPages = (int) Math.ceil((double) totalRecords / pageSize);

        // Lấy danh sách SupplierProduct theo trang
        List<SupplierProduct> supplierProducts = dao.getByPage(page, pageSize);

        // Lấy các danh sách phụ khác
        List<Supplier> suppliers = dao.getAllSuppliers();
        List<String> supplierNames = dao.getAllSupplierNames();
        List<String> productNames = dao.getAllProductNamesByTypePart();

        // Gửi dữ liệu sang JSP
        request.setAttribute("supplierProducts", supplierProducts);
        request.setAttribute("suppliers", suppliers);
        request.setAttribute("supplierNames", supplierNames);
        request.setAttribute("productNames", productNames);

        // Thêm thông tin phân trang
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);

        request.getRequestDispatcher("/view/supplierproduct/page-list-supplierproduct.jsp")
                .forward(request, response);
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
        SupplierProductDAO dao = new SupplierProductDAO();
        String action = request.getParameter("action");

        if ("add".equalsIgnoreCase(action)) {
            String supplierName = request.getParameter("supplierName");
            String productName = request.getParameter("productName");
            int deliveryDuration = Integer.parseInt(request.getParameter("deliveryDuration"));
            java.math.BigDecimal estimatedPrice = new java.math.BigDecimal(request.getParameter("estimatedPrice"));
            String policies = request.getParameter("policies");
            boolean isActive = Boolean.parseBoolean(request.getParameter("isActive"));

            System.out.println("DEBUG - supplierName: " + supplierName);
            System.out.println("DEBUG - productName: " + productName);

            Integer supplierId = dao.getSupplierIdByName(supplierName);
            Integer productId = dao.getProductIdByName(productName);

            System.out.println("DEBUG - supplierId: " + supplierId);
            System.out.println("DEBUG - productId: " + productId);

            if (supplierId != null && productId != null) {
                // Kiểm tra xem đã tồn tại chưa
                if (dao.exists(supplierId, productId)) {
                    request.setAttribute("errorMessage", "Nhà cung cấp và sản phẩm này đã tồn tại!");
                    doGet(request, response);
                    return;
                }

                Supplier s = new Supplier();
                s.setSupplierId(supplierId);

                Product p = new Product();
                p.setProductId(productId);

                SupplierProduct sp = new SupplierProduct();
                sp.setSupplier(s);
                sp.setProduct(p);
                sp.setDeliveryDuration(deliveryDuration);
                sp.setEstimatedPrice(estimatedPrice);
                sp.setPolicies(policies);
                sp.setIsActive(isActive);

                boolean success = dao.insert(sp);
                if (!success) {
                    request.setAttribute("errorMessage", "Không thể lưu dữ liệu. Vui lòng thử lại!");
                    doGet(request, response);
                    return;
                }
            } else {
                request.setAttribute("errorMessage", "Không tìm thấy nhà cung cấp hoặc sản phẩm!");
                doGet(request, response);
                return;
            }
            response.sendRedirect("supplierproduct");

        } else if ("update".equalsIgnoreCase(action)) {
            // Lấy dữ liệu từ form
            String supplierName = request.getParameter("supplierName");
            String productName = request.getParameter("productName");
            int deliveryDuration = Integer.parseInt(request.getParameter("deliveryDuration"));
            java.math.BigDecimal estimatedPrice = new java.math.BigDecimal(request.getParameter("estimatedPrice"));
            String policies = request.getParameter("policies");
            boolean isActive = Boolean.parseBoolean(request.getParameter("isActive"));

            // Lấy ID cũ từ hidden fields (để so sánh)
            Integer oldSupplierId = null;
            Integer oldProductId = null;

            String supplierIdParam = request.getParameter("supplierId");
            String productIdParam = request.getParameter("productId");

            if (supplierIdParam != null && !supplierIdParam.trim().isEmpty()) {
                try {
                    oldSupplierId = Integer.parseInt(supplierIdParam);
                } catch (NumberFormatException e) {
                    System.out.println("DEBUG - Invalid supplierId from hidden field: " + supplierIdParam);
                }
            }

            if (productIdParam != null && !productIdParam.trim().isEmpty()) {
                try {
                    oldProductId = Integer.parseInt(productIdParam);
                } catch (NumberFormatException e) {
                    System.out.println("DEBUG - Invalid productId from hidden field: " + productIdParam);
                }
            }

            // Lấy ID mới từ tên được chọn trong dropdown
            Integer newSupplierId = null;
            Integer newProductId = null;

            if (supplierName != null && !supplierName.trim().isEmpty()) {
                newSupplierId = dao.getSupplierIdByName(supplierName);
            }
            if (productName != null && !productName.trim().isEmpty()) {
                newProductId = dao.getProductIdByName(productName);
            }

            // Kiểm tra đã có đủ ID chưa
            if (newSupplierId == null || newProductId == null) {
                request.setAttribute("errorMessage", "Không tìm thấy Supplier ID hoặc Product ID!");
                doGet(request, response);
                return;
            }

            if (oldSupplierId == null || oldProductId == null) {
                request.setAttribute("errorMessage", "Không tìm thấy thông tin bản ghi cũ để cập nhật!");
                doGet(request, response);
                return;
            }

            // Kiểm tra xem Supplier/Product có thay đổi không
            boolean supplierChanged = !oldSupplierId.equals(newSupplierId);
            boolean productChanged = !oldProductId.equals(newProductId);

            if (supplierChanged || productChanged) {
                // Supplier hoặc Product đã thay đổi
                // Kiểm tra xem cặp (newSupplierId, newProductId) đã tồn tại chưa
                // (có thể là một bản ghi khác, không phải bản ghi hiện tại đang chỉnh sửa)
                if (dao.exists(newSupplierId, newProductId)) {
                    request.setAttribute("errorMessage", "Nhà cung cấp và sản phẩm này đã tồn tại!");
                    doGet(request, response);
                    return;
                }

                // Xóa bản ghi cũ và tạo mới với thông tin mới
                dao.delete(oldSupplierId, oldProductId);

                Supplier s = new Supplier();
                s.setSupplierId(newSupplierId);

                Product p = new Product();
                p.setProductId(newProductId);

                SupplierProduct sp = new SupplierProduct();
                sp.setSupplier(s);
                sp.setProduct(p);
                sp.setDeliveryDuration(deliveryDuration);
                sp.setEstimatedPrice(estimatedPrice);
                sp.setPolicies(policies);
                sp.setIsActive(isActive);

                // Tạo bản ghi mới
                boolean success = dao.insert(sp);
                if (!success) {
                    request.setAttribute("errorMessage", "Không thể cập nhật dữ liệu. Vui lòng thử lại!");
                    doGet(request, response);
                    return;
                }
            } else {
                // Supplier và Product không thay đổi, chỉ cập nhật các trường khác
                Supplier s = new Supplier();
                s.setSupplierId(newSupplierId);

                Product p = new Product();
                p.setProductId(newProductId);

                SupplierProduct sp = new SupplierProduct();
                sp.setSupplier(s);
                sp.setProduct(p);
                sp.setDeliveryDuration(deliveryDuration);
                sp.setEstimatedPrice(estimatedPrice);
                sp.setPolicies(policies);
                sp.setIsActive(isActive);

                // Gọi DAO cập nhật
                dao.update(sp);
            }

            // Quay lại trang danh sách
            response.sendRedirect("supplierproduct");
        } else if ("updateStatus".equals(action)) {
            int supplierId = Integer.parseInt(request.getParameter("supplierId"));
            int productId = Integer.parseInt(request.getParameter("productId"));
            boolean newStatus = Boolean.parseBoolean(request.getParameter("status"));
            dao.updateStatus(supplierId, productId, newStatus);

            response.sendRedirect("supplierproduct");
        } else {
            doGet(request, response);
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
