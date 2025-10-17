package controller.adminCombo;

import dal.MaintenancePackageDetailDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.List;
import model.MaintenancePackage;
import model.MaintenancePackageDetail;
import model.Product;

@WebServlet(name = "ManagerPackage", urlPatterns = {"/managerPackage"})
public class ManagerPackage extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        MaintenancePackageDetailDAO dao = new MaintenancePackageDetailDAO();

        String productName = request.getParameter("productName");
        String status = request.getParameter("status");
        String pageStr = request.getParameter("page");
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

        int totalRecords = dao.countPackageDetails(productName, status);
        int totalPages = (int) Math.ceil((double) totalRecords / recordsPerPage);

        if (currentPage > totalPages && totalPages > 0) {
            currentPage = totalPages;
        }

        int offset = (currentPage - 1) * recordsPerPage;

        List<MaintenancePackageDetail> details = dao.searchPackageDetails(productName, status, offset, recordsPerPage);
        List<Product> products = dao.getAllProductIdAndName();
        List<MaintenancePackage> packages = dao.getAllPackages();

        request.setAttribute("details", details);
        request.setAttribute("products", products);
        request.setAttribute("packages", packages);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", totalPages);

        request.getRequestDispatcher("/view/Admin/ManagePackage.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        MaintenancePackageDetailDAO dao = new MaintenancePackageDetailDAO();

        if ("add".equals(action)) {
            addPackageDetail(request, response, dao);
            return;
        }

        if ("toggleStatus".equals(action)) {
            int id = Integer.parseInt(request.getParameter("id"));
            if (dao.toggleStatus(id)) {
                request.getSession().setAttribute("success", "Thay đổi trạng thái thành công!");
            } else {
                request.getSession().setAttribute("error", "Thay đổi trạng thái thất bại!");
            }
            response.sendRedirect("managerPackage");
            return;
        }

        if ("update".equals(action)) {
            updatePackageDetail(request, response, dao);
        }
    }

    private void addPackageDetail(HttpServletRequest request, HttpServletResponse response, MaintenancePackageDetailDAO dao) throws IOException {
        try {
            int packageId = Integer.parseInt(request.getParameter("packageId"));
            int productId = Integer.parseInt(request.getParameter("productId"));
            BigDecimal quantity = new BigDecimal(request.getParameter("quantity"));
            int displayOrder = Integer.parseInt(request.getParameter("displayOrder"));
            String notes = request.getParameter("notes");

            // Kiểm tra trùng lặp
            if (dao.isDetailExist(packageId, productId)) {
                request.getSession().setAttribute("error", "Sản phẩm này đã tồn tại trong gói!");
            } else {
                MaintenancePackage pkg = dao.getPackageById(packageId); // ✅ lấy đầy đủ package (có code)
                Product product = new Product();
                product.setProductId(productId);

                MaintenancePackageDetail detail = new MaintenancePackageDetail();
                detail.setMaintenancePackage(pkg);
                detail.setProduct(product);
                detail.setQuantity(quantity);
                detail.setDisplayOrder(displayOrder);
                detail.setNotes(notes);
                detail.setIsRequired(true); // mặc định hoạt động

                if (dao.addPackageDetail(detail)) {
                    request.getSession().setAttribute("success", "Thêm chi tiết gói " + pkg.getPackageCode() + " thành công!");
                } else {
                    request.getSession().setAttribute("error", "Thêm chi tiết gói thất bại!");
                }
            }
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("error", "Dữ liệu không hợp lệ!");
        }
        response.sendRedirect("managerPackage");
    }

    private void updatePackageDetail(HttpServletRequest request, HttpServletResponse response, MaintenancePackageDetailDAO dao) throws IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            int productId = Integer.parseInt(request.getParameter("productId"));
            BigDecimal quantity = new BigDecimal(request.getParameter("quantity"));
            int displayOrder = Integer.parseInt(request.getParameter("displayOrder"));
            String notes = request.getParameter("notes");

            MaintenancePackageDetail detail = new MaintenancePackageDetail();
            Product product = new Product();
            product.setProductId(productId);

            detail.setPackageDetailId(id);
            detail.setProduct(product);
            detail.setQuantity(quantity);
            detail.setDisplayOrder(displayOrder);
            detail.setNotes(notes);

            if (dao.updatePackageDetail(detail)) {
                request.getSession().setAttribute("success", "Cập nhật thành công!");
            } else {
                request.getSession().setAttribute("error", "Cập nhật thất bại!");
            }
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("error", "Dữ liệu không hợp lệ!");
        }
        response.sendRedirect("managerPackage");
    }
}
