/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.Admin;

import dal.RoleDAO;
import dal.UserDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.io.File;
import java.nio.file.Paths;
import java.util.List;
import model.Role;
import model.User;

/**
 *
 * @author MinHeee
 */
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024, // 1MB
        maxFileSize = 1024 * 1024 * 5, // 5MB
        maxRequestSize = 1024 * 1024 * 10 // 10MB
)

public class AddEmployees extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet AddEmployees</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet AddEmployees at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        RoleDAO rdb = new RoleDAO();
        List<Role> roles = rdb.getAllRole();
        request.setAttribute("roles", roles);
        request.getRequestDispatcher("/view/Admin/AddEmployees.jsp").forward(request, response);

    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        try {
            String code = request.getParameter("userCode");
            String fullName = request.getParameter("fullName");
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            String email = request.getParameter("email");
            String phone = request.getParameter("phone");
            String DOB = request.getParameter("DOB");
            boolean male = "1".equals(request.getParameter("male"));
            boolean isActive = Boolean.parseBoolean(request.getParameter("isActive"));
            int roleID = Integer.parseInt(request.getParameter("roleID"));

            Part imagePart = request.getPart("imageFile");
            String fileName = Paths.get(imagePart.getSubmittedFileName()).getFileName().toString();

            String uploadPath = getServletContext().getRealPath("/uploads");
            File uploadDir = new File(uploadPath);
            if (fileName != null && !fileName.isEmpty()) {
                imagePart.write(uploadPath + File.separator + fileName);
            }

            boolean hasError = false;
            UserDAO udb = new UserDAO();

            // Validate dữ liệu
            if (udb.existsByUserCode(code)) {
                request.setAttribute("errorUserCode", "User Code đã tồn tại, vui lòng nhập mã khác!");
                hasError = true;
            }

            if (udb.existsByUsername(username)) {
                request.setAttribute("errorUsername", "Username đã tồn tại, vui lòng nhập username khác!");
                hasError = true;
            }
            if (code == null || code.trim().isEmpty()) {
                request.setAttribute("errorUserCode", "Vui lòng nhập User Code");
                hasError = true;
            }
            if (fullName == null || fullName.trim().isEmpty()) {
                request.setAttribute("errorFullName", "Vui lòng nhập Full Name");
                hasError = true;
            }
            if (username == null || username.trim().isEmpty()) {
                request.setAttribute("errorUsername", "Vui lòng nhập Username");
                hasError = true;
            }
            if (password == null || password.trim().isEmpty()) {
                request.setAttribute("errorPassword", "Vui lòng nhập Password");
                hasError = true;
            } else if (!password.matches("^(?=[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*(),.?\":{}|<>]).{8,}$")) {
                request.setAttribute("errorPassword",
                        "Mật khẩu phải bắt đầu bằng chữ in hoa, có ít nhất 1 số, 1 ký tự đặc biệt và dài tối thiểu 8 ký tự");
                hasError = true;
            }
            if (email == null || email.trim().isEmpty()) {
                request.setAttribute("errorEmail", "Vui lòng nhập Email");
                hasError = true;
            } else if (!email.matches("^[A-Za-z0-9._%+-]+@gmail\\.com$")) {
                request.setAttribute("errorEmail", "Email phải đúng định dạng và là @gmail.com");
                hasError = true;
            }
            if (phone == null || phone.trim().isEmpty()) {
                request.setAttribute("errorPhone", "Vui lòng nhập SĐT");
                hasError = true;
            } else if (!phone.matches("\\d{10}")) {
                request.setAttribute("errorPhone", "SĐT phải gồm đúng 10 chữ số!");
                hasError = true;
            }
            if (DOB == null || DOB.trim().isEmpty()) {
                request.setAttribute("errorDOB", "Vui lòng nhập Ngày sinh");
                hasError = true;
            }

            if (udb.isEmailExist(email)) {
                RoleDAO rdb = new RoleDAO();

                request.setAttribute("errorEmail", "Email đã tồn tại, vui lòng nhập email khác!");
                request.setAttribute("roles", rdb.getAllRole());
                request.getRequestDispatcher("/view/Admin/AddEmployees.jsp").forward(request, response);
                return;
            }
            if (hasError) {
                RoleDAO rdb = new RoleDAO();
                request.setAttribute("roles", rdb.getAllRole());
                request.getRequestDispatcher("/view/Admin/AddEmployees.jsp").forward(request, response);
                return;
            }

            Role role = new Role();
            role.setRoleID(roleID);

            User user = new User();
            user.setUserCode(code);
            user.setFullName(fullName);
            user.setUserName(username);
            user.setPassword(password);
            user.setEmail(email);
            user.setPhone(phone);
            user.setDateOfBirth(DOB);
            user.setMale(male);
            user.setIsActive(isActive);
            user.setRole(role);
            user.setImage("uploads/" + fileName); //

            udb.insert(user);
            // Redirect
            response.sendRedirect("listEmployees");

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Lỗi khi thêm nhân viên: " + e.getMessage());
            request.getRequestDispatcher("/view/Admin/error.jsp").forward(request, response);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
