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
        String filterType = request.getParameter("type");

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


