package unit;

import dal.UnitDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import model.Unit;

public class AddUnit extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/view/ManagerUnit.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String name = request.getParameter("name");
        String type = request.getParameter("type");
        String description = request.getParameter("description");

        Unit unit = new Unit();
        unit.setName(name);
        unit.setType(type);
        unit.setDescription(description);

        UnitDAO dao = new UnitDAO();
        dao.insert(unit);

        response.sendRedirect(request.getContextPath() + "/ListUnit");
    }
}