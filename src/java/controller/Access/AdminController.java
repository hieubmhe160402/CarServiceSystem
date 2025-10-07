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
 *vie
 * @author MinHeee
 */
@WebServlet(name = "AdminController", urlPatterns = {"/adminController"})

public class AdminController extends HttpServlet {

    private UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        // Kiểm tra đăng nhập và quyền admin
        if (user == null) {
            response.sendRedirect("authController?action=login");
            return;
        }

        if (user.getRole() == null || !user.getRole().getRoleName().equals("Admin")) {
            response.sendRedirect("authController?action=login");
            return;
        }

        // Set thông tin user cho view
        request.setAttribute("currentUser", user);

        // Forward đến trang admin dashboard
        request.getRequestDispatcher("view/Admin/HomePageForAdmin.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
