package controller.web.unit;

import dal.UnitDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "DeleteUnitServlet", urlPatterns = {"/delete-unit"})
public class DeleteUnitServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idParam = request.getParameter("id");
        if (idParam != null) {
            try {
                int id = Integer.parseInt(idParam);
                UnitDAO dao = new UnitDAO();
                dao.delete(id);
            } catch (NumberFormatException e) {
                // ignore invalid id
            }
        }
        response.sendRedirect("units");
    }
}


