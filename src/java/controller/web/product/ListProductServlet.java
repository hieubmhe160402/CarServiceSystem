/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.web.product;

import dal.CategoryDAO;
import dal.ProductDAO;
import dal.UnitDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.List;
import model.Category;
import model.Product;
import model.Unit;

/**
 *
 * @author LEGION
 */
@WebServlet(name = "ListProductServlet", urlPatterns = {"/products"})
public class ListProductServlet extends HttpServlet {

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
            out.println("<title>Servlet ListProductServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ListProductServlet at " + request.getContextPath() + "</h1>");
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
        CategoryDAO categoryDAO = new CategoryDAO();
        UnitDAO unitDAO = new UnitDAO();
        ProductDAO productDAO = new ProductDAO();

        // Lấy parameters
        String pageStr = request.getParameter("page");
        String categoryFilter = request.getParameter("category");
        String statusFilter = request.getParameter("status");

        // Xử lý phân trang
        int currentPage = 1;
        int recordsPerPage = 8;

        if (pageStr != null && !pageStr.isEmpty()) {
            try {
                currentPage = Integer.parseInt(pageStr);
                if (currentPage < 1) {
                    currentPage = 1;
                }
            } catch (NumberFormatException e) {
                currentPage = 1;
            }
        }

        // Lấy danh sách category và unit
        List<Category> categorys = categoryDAO.getByType("PART");
        List<Unit> units = unitDAO.getAllUnits();

        // Lấy tổng số products theo filter
        int totalRecords = productDAO.countProductsByFilter(categoryFilter, statusFilter);
        int totalPages = (int) Math.ceil((double) totalRecords / recordsPerPage);

        // Đảm bảo currentPage không vượt quá totalPages
        if (currentPage > totalPages && totalPages > 0) {
            currentPage = totalPages;
        }

        // Lấy danh sách products có phân trang
        int offset = (currentPage - 1) * recordsPerPage;
        List<Product> products = productDAO.getProductsByPartTypeWithPaging(
                categoryFilter, statusFilter, offset, recordsPerPage);

        // Set attributes
        request.setAttribute("categoryList", categorys);
        request.setAttribute("unitList", units);
        request.setAttribute("productList", products);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("filterCategory", categoryFilter != null ? categoryFilter : "");
        request.setAttribute("filterStatus", statusFilter != null ? statusFilter : "");

        request.getRequestDispatcher("view/product/page-list-product.jsp").forward(request, response);
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
        response.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");
        ProductDAO productDAO = new ProductDAO();

        try {
            if ("add".equals(action)) {
                // Thêm mới sản phẩm
                addProduct(request, response, productDAO);
            } else if ("edit".equals(action)) {
                // Cập nhật sản phẩm
                editProduct(request, response, productDAO);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
            response.sendRedirect("product");
        }
    }

    private void addProduct(HttpServletRequest request, HttpServletResponse response, ProductDAO productDAO)
            throws ServletException, IOException {

        // Lấy dữ liệu từ form
        String code = request.getParameter("code");
        String name = request.getParameter("name");
        String type = request.getParameter("type"); // PART
        String priceStr = request.getParameter("price");
        String description = request.getParameter("description");
        String image = request.getParameter("image");
        String unitIdStr = request.getParameter("unitId");
        String categoryIdStr = request.getParameter("categoryId");
        String warrantyStr = request.getParameter("warrantyPeriodMonths");
        String minStockStr = request.getParameter("minStockLevel");
        String isActiveStr = request.getParameter("isActive");

        // Validate dữ liệu
        if (code == null || code.trim().isEmpty()
                || name == null || name.trim().isEmpty()
                || priceStr == null || unitIdStr == null || categoryIdStr == null) {
            request.getSession().setAttribute("error", "Vui lòng điền đầy đủ thông tin bắt buộc!");
            response.sendRedirect("product");
            return;
        }

        // Kiểm tra mã sản phẩm đã tồn tại chưa
        if (productDAO.isProductCodeExist(code)) {
            request.getSession().setAttribute("error", "Mã sản phẩm đã tồn tại!");
            response.sendRedirect("product");
            return;
        }

        try {
            // Tạo đối tượng Product
            Product product = new Product();
            product.setCode(code.trim());
            product.setName(name.trim());
            product.setType(type);
            product.setPrice(new BigDecimal(priceStr));
            product.setDescription(description != null ? description.trim() : "");
            product.setImage(image != null ? image.trim() : "");

            // Set Unit
            Unit unit = new Unit();
            unit.setUnitId(Integer.parseInt(unitIdStr));
            product.setUnit(unit);

            // Set Category
            Category category = new Category();
            category.setCategoryId(Integer.parseInt(categoryIdStr));
            product.setCategory(category);

            product.setWarrantyPeriodMonths(warrantyStr != null ? Integer.parseInt(warrantyStr) : 0);
            product.setMinStockLevel(minStockStr != null ? Integer.parseInt(minStockStr) : 0);
            product.setIsActive("1".equals(isActiveStr));

            // Thêm vào database
            boolean success = productDAO.addProduct(product);

            if (success) {
                request.getSession().setAttribute("success", "Thêm sản phẩm thành công!");
            } else {
                request.getSession().setAttribute("error", "Thêm sản phẩm thất bại!");
            }

        } catch (NumberFormatException e) {
            request.getSession().setAttribute("error", "Dữ liệu số không hợp lệ!");
        } catch (Exception e) {
            request.getSession().setAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
        }

        response.sendRedirect("product");
    }

    private void editProduct(HttpServletRequest request, HttpServletResponse response, ProductDAO productDAO)
            throws ServletException, IOException {

        // Lấy dữ liệu từ form
        String productIdStr = request.getParameter("productID");
        String code = request.getParameter("code");
        String name = request.getParameter("name");
        String type = request.getParameter("type"); // PART
        String priceStr = request.getParameter("price");
        String description = request.getParameter("description");
        String image = request.getParameter("image");
        String unitIdStr = request.getParameter("unitId");
        String categoryIdStr = request.getParameter("categoryId");
        String warrantyStr = request.getParameter("warrantyPeriodMonths");
        String minStockStr = request.getParameter("minStockLevel");
        String isActiveStr = request.getParameter("isActive");

        // Validate dữ liệu
        if (productIdStr == null || code == null || code.trim().isEmpty()
                || name == null || name.trim().isEmpty()
                || priceStr == null || unitIdStr == null || categoryIdStr == null) {
            request.getSession().setAttribute("error", "Vui lòng điền đầy đủ thông tin bắt buộc!");
            response.sendRedirect("product");
            return;
        }

        try {
            int productId = Integer.parseInt(productIdStr);

            // Kiểm tra mã sản phẩm đã tồn tại chưa (trừ chính nó)
            if (productDAO.isProductCodeExistForUpdate(code, productId)) {
                request.getSession().setAttribute("error", "Mã sản phẩm đã tồn tại!");
                response.sendRedirect("product");
                return;
            }

            // Tạo đối tượng Product
            Product product = new Product();
            product.setProductId(productId);
            product.setCode(code.trim());
            product.setName(name.trim());
            product.setType(type);
            product.setPrice(new BigDecimal(priceStr));
            product.setDescription(description != null ? description.trim() : "");
            product.setImage(image != null ? image.trim() : "");

            // Set Unit
            Unit unit = new Unit();
            unit.setUnitId(Integer.parseInt(unitIdStr));
            product.setUnit(unit);

            // Set Category
            Category category = new Category();
            category.setCategoryId(Integer.parseInt(categoryIdStr));
            product.setCategory(category);

            product.setWarrantyPeriodMonths(warrantyStr != null ? Integer.parseInt(warrantyStr) : 0);
            product.setMinStockLevel(minStockStr != null ? Integer.parseInt(minStockStr) : 0);
            product.setIsActive("1".equals(isActiveStr));

            // Cập nhật vào database
            boolean success = productDAO.updateProduct(product);

            if (success) {
                request.getSession().setAttribute("success", "Cập nhật sản phẩm thành công!");
            } else {
                request.getSession().setAttribute("error", "Cập nhật sản phẩm thất bại!");
            }

        } catch (NumberFormatException e) {
            request.getSession().setAttribute("error", "Dữ liệu số không hợp lệ!");
        } catch (Exception e) {
            request.getSession().setAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
        }

        response.sendRedirect("product");
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
