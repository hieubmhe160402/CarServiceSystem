/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller.Access;

import dal.UserDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;

/**
 *
 * @author MinHeee
 */
@WebServlet(name = "CustomerController", urlPatterns = {"/customerController"})
public class CustomerController extends HttpServlet {

    private UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        // Kiểm tra đăng nhập và quyền customer
        if (user == null) {
            response.sendRedirect("AuthController?action=login");
            return;
        }
        
        if (user.getRole() == null || !user.getRole().getRoleName().equals("CarOwner")) {
            response.sendRedirect("AuthController?action=login");
            return;
        }
        
        // Set thông tin user cho view
        request.setAttribute("currentUser", user);
        
        // Forward đến trang customer dashboard
        request.getRequestDispatcher("view/Cusview/Test.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
