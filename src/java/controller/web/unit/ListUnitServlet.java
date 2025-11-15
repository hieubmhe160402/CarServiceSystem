package controller.web.unit;

import dal.UnitDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import model.Unit;

@WebServlet(name = "ListUnitServlet", urlPatterns = {"/units"})
public class ListUnitServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        UnitDAO unitDAO = new UnitDAO();

        int currentPage = 1;
        int pageSize = 8;
        String pageParam = request.getParameter("page");
        if (pageParam != null) {
            try {
                currentPage = Integer.parseInt(pageParam);
            } catch (NumberFormatException e) {
                currentPage = 1;
            }
        }
        
        // 🔴 Nếu có lỗi → KHÔNG filter, lấy từ attribute
        String filterType = (String) request.getAttribute("filterType");
        if (filterType == null) {
            filterType = request.getParameter("type");
        }

        List<Unit> unitList = unitDAO.getByPageAndType(currentPage, pageSize, filterType);
        int totalItems = unitDAO.count(filterType);
        int totalPages = (int) Math.ceil((double) totalItems / pageSize);

        request.setAttribute("unitList", unitList);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("filterType", filterType == null ? "" : filterType);

        request.getRequestDispatcher("view/unit/list-unit.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");
        UnitDAO dao = new UnitDAO();

        if ("add".equalsIgnoreCase(action)) {
            String name = request.getParameter("name");
            String type = request.getParameter("type");
            String description = request.getParameter("description");

            // ✅ Kiểm tra Name + Type trùng
            if (dao.isNameAndTypeExists(name, type)) {
                request.setAttribute("errorMsg", "⛔ Name '" + name + "' với Type '" + type + "' đã tồn tại!");
                request.setAttribute("showAddModal", true);
                request.setAttribute("formName", name);
                request.setAttribute("formType", type);
                request.setAttribute("formDescription", description);
                // 🔴 XÓA filter khi có lỗi
                request.setAttribute("filterType", "");
                doGet(request, response);
                return;
            }

            Unit unit = new Unit();
            unit.setName(name);
            unit.setType(type);
            unit.setDescription(description);

            boolean success = dao.add(unit);
            response.sendRedirect("units");
            return;
        } else if ("edit".equalsIgnoreCase(action)) {
            String idStr = request.getParameter("unitID");
            String name = request.getParameter("name");
            String type = request.getParameter("type");
            String description = request.getParameter("description");

            try {
                int id = Integer.parseInt(idStr);
                
                // ✅ Kiểm tra Unit có đang được sử dụng không
                if (dao.isUnitInUse(id)) {
                    request.setAttribute("errorMsg", "⛔ Unit này đang được sử dụng, không thể thay đổi!");
                    request.setAttribute("showEditModal", true);
                    request.setAttribute("editId", id);
                    request.setAttribute("formName", name);
                    request.setAttribute("formType", type);
                    request.setAttribute("formDescription", description);
                    request.setAttribute("filterType", "");
                    doGet(request, response);
                    return;
                }
                
                // ✅ Kiểm tra Name + Type trùng (trừ chính nó)
                if (dao.isNameAndTypeExistsExcept(name, type, id)) {
                    request.setAttribute("errorMsg", "⛔ Name '" + name + "' với Type '" + type + "' đã tồn tại!");
                    request.setAttribute("showEditModal", true);
                    request.setAttribute("editId", id);
                    request.setAttribute("formName", name);
                    request.setAttribute("formType", type);
                    request.setAttribute("formDescription", description);
                    // 🔴 XÓA filter khi có lỗi
                    request.setAttribute("filterType", "");
                    doGet(request, response);
                    return;
                }
                
                Unit unit = new Unit();
                unit.setUnitId(id);
                unit.setName(name);
                unit.setType(type);
                unit.setDescription(description);
                dao.update(unit);
            } catch (NumberFormatException e) {
                // ignore
            }
            response.sendRedirect("units");
            return;
        }
        response.sendRedirect("units");
    }
}


