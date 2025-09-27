package controller.Product;

import dal.CategoryDAO;
import dal.ProductDAO;
import dal.UnitDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import model.Category;
import model.Product;
import model.Unit;

public class UpdateProduct extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idStr = request.getParameter("id");
        if (idStr == null || idStr.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/ListProduct");
            return;
        }
        
        int id = Integer.parseInt(idStr);

        ProductDAO dao = new ProductDAO();
        Product p = dao.getById(id);

        if (p == null) {
            response.sendRedirect(request.getContextPath() + "/ListProduct");
            return;
        }

        CategoryDAO cdao = new CategoryDAO();
        UnitDAO udao = new UnitDAO();

        List<String> types = cdao.getAllType();
        String selectedType = p.getType();
        List<String> names = cdao.getAllNameByType(selectedType);
        List<Unit> units = udao.getAllUnits();

        request.setAttribute("product", p);
        request.setAttribute("types", types);
        request.setAttribute("selectedType", selectedType);
        request.setAttribute("names", names);
        request.setAttribute("units", units);

        request.getRequestDispatcher("/view/AddProduct.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String idStr = request.getParameter("id");
        if (idStr == null || idStr.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/ListProduct");
            return;
        }
        
        int id = Integer.parseInt(idStr);

        String code = request.getParameter("code");
        String name = request.getParameter("name");
        String type = request.getParameter("type");
        String priceStr = request.getParameter("price");
        String description = request.getParameter("description");
        String image = request.getParameter("image");
        String categoryName = request.getParameter("categoryName");
        String unitIdStr = request.getParameter("unitId");
        String warrantyStr = request.getParameter("warrantyPeriodMonths");
        String minStockStr = request.getParameter("minStockLevel");
        String estDurStr = request.getParameter("estimatedDurationHours");
        boolean isActive = request.getParameter("isActive") != null;

        BigDecimal price = priceStr == null || priceStr.isEmpty() ? BigDecimal.ZERO : new BigDecimal(priceStr);
        int unitId = unitIdStr == null || unitIdStr.isEmpty() ? 0 : Integer.parseInt(unitIdStr);
        int warrantyMonths = warrantyStr == null || warrantyStr.isEmpty() ? 0 : Integer.parseInt(warrantyStr);
        int minStock = minStockStr == null || minStockStr.isEmpty() ? 0 : Integer.parseInt(minStockStr);
        BigDecimal estHours = estDurStr == null || estDurStr.isEmpty() ? BigDecimal.ZERO : new BigDecimal(estDurStr);

        CategoryDAO cdao = new CategoryDAO();
        Category cat = cdao.findByName(categoryName);

        Unit u = new Unit();
        u.setUnitId(unitId);

        Product p = new Product();
        p.setProductId(id); // QUAN TRỌNG: Set ID để UPDATE
        p.setCode(code);
        p.setName(name);
        p.setType(type);
        p.setPrice(price);
        p.setDescription(description);
        p.setImage(image);
        p.setCategory(cat);
        p.setUnit(u);
        p.setWarrantyPeriodMonths(warrantyMonths);
        p.setMinStockLevel(minStock);
        p.setEstimatedDurationHours(estHours);
        p.setIsActive(isActive);

        ProductDAO dao = new ProductDAO();
        dao.update(p); // Gọi UPDATE

        response.sendRedirect(request.getContextPath() + "/ListProduct");
    }
}
