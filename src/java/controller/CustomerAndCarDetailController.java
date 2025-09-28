package controller;

import dal.CustomerDetailDAO;
import model.CustomerProfile;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/customers")
public class CustomerAndCarDetailController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        CustomerDetailDAO dao = new CustomerDetailDAO();
        try {
            List<CustomerProfile> customers = dao.getAllCustomerProfiles();

            // Đẩy dữ liệu sang JSP
            request.setAttribute("customers", customers);
                request.getRequestDispatcher("CustomerAndCarDetailController.jsp").forward(request, response);

        } catch (SQLException e) {
            throw new ServletException("Error retrieving customer data", e);
        }
    }
}
