package controller.web.unit;

import dal.UnitDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
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
                
                // ✅ Kiểm tra Unit có đang được sử dụng không
                if (dao.isUnitInUse(id)) {
                    HttpSession session = request.getSession();
                    session.setAttribute("errorMsg", "⛔ Unit này đang được sử dụng, không thể xóa!");
                } else {
                    boolean success = dao.delete(id);
                    if (!success) {
                        HttpSession session = request.getSession();
                        session.setAttribute("errorMsg", "❌ Không thể xóa Unit này.");
                    }
                }
            } catch (NumberFormatException e) {
                // ignore invalid id
            }
        }
        response.sendRedirect("units");
    }
}


