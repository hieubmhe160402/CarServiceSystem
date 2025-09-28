package controller;

import dal.ServiceHistoryDAO;
import model.ServiceHistory;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/serviceHistory")
public class ServiceHistoryController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ServiceHistoryDAO dao = new ServiceHistoryDAO();
        try {
            List<ServiceHistory> histories = dao.getAllServiceHistories();
            request.setAttribute("histories", histories);

            request.getRequestDispatcher("serviceHistory.jsp").forward(request, response);

        } catch (SQLException e) {
            throw new ServletException("Error retrieving service history", e);
        }
    }
}
