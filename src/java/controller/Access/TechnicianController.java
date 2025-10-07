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
@WebServlet(name = "TechnicianController", urlPatterns = {"/technicianController"})
public class TechnicianController extends HttpServlet {

    private UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        // Kiểm tra đăng nhập và quyền technician
        if (user == null) {
            response.sendRedirect("AuthController?action=login");
            return;
        }
        
        if (user.getRole() == null || !user.getRole().getRoleName().equals("ServiceTechnician")) {
            response.sendRedirect("AuthController?action=login");
            return;
        }
        
        // Set thông tin user cho view
        request.setAttribute("currentUser", user);
        
        // Forward đến trang technician dashboard
        request.getRequestDispatcher("view/Technicianview/AssignedCars.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
