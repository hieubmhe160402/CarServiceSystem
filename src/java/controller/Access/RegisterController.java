/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller.Access;

import dal.UserDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 *
 * @author MinHeee
 */
public class RegisterController extends HttpServlet {

    private UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("view/CommonScreen/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        handleRegister(request, response);
    }

    private void handleRegister(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Lấy thông tin từ form
        String fullName = request.getParameter("fullName");
        String userName = request.getParameter("userName");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String gender = request.getParameter("gender");
        String dateOfBirth = request.getParameter("dateOfBirth");
        
        // Validation
        if (fullName == null || fullName.trim().isEmpty()) {
            request.setAttribute("error", "Vui lòng nhập họ tên!");
            request.getRequestDispatcher("view/CommonScreen/register.jsp").forward(request, response);
            return;
        }
        
        if (userName == null || userName.trim().isEmpty()) {
            request.setAttribute("error", "Vui lòng nhập tên đăng nhập!");
            request.getRequestDispatcher("view/CommonScreen/register.jsp").forward(request, response);
            return;
        }
        
        if (password == null || password.trim().isEmpty()) {
            request.setAttribute("error", "Vui lòng nhập mật khẩu!");
            request.getRequestDispatcher("view/CommonScreen/register.jsp").forward(request, response);
            return;
        }
        
        if (confirmPassword == null || confirmPassword.trim().isEmpty()) {
            request.setAttribute("error", "Vui lòng xác nhận mật khẩu!");
            request.getRequestDispatcher("view/CommonScreen/register.jsp").forward(request, response);
            return;
        }
        
        if (email == null || email.trim().isEmpty()) {
            request.setAttribute("error", "Vui lòng nhập email!");
            request.getRequestDispatcher("view/CommonScreen/register.jsp").forward(request, response);
            return;
        }
        
        if (phone == null || phone.trim().isEmpty()) {
            request.setAttribute("error", "Vui lòng nhập số điện thoại!");
            request.getRequestDispatcher("view/CommonScreen/register.jsp").forward(request, response);
            return;
        }
        
        if (gender == null || gender.trim().isEmpty()) {
            request.setAttribute("error", "Vui lòng chọn giới tính!");
            request.getRequestDispatcher("view/CommonScreen/register.jsp").forward(request, response);
            return;
        }
        
        if (dateOfBirth == null || dateOfBirth.trim().isEmpty()) {
            request.setAttribute("error", "Vui lòng nhập ngày sinh!");
            request.getRequestDispatcher("view/CommonScreen/register.jsp").forward(request, response);
            return;
        }
        
        // Kiểm tra mật khẩu khớp
        if (!password.equals(confirmPassword)) {
            request.setAttribute("error", "Mật khẩu và xác nhận mật khẩu không khớp!");
            request.getRequestDispatcher("view/CommonScreen/register.jsp").forward(request, response);
            return;
        }
        
        // Kiểm tra độ dài mật khẩu
        if (password.length() < 6) {
            request.setAttribute("error", "Mật khẩu phải có ít nhất 6 ký tự!");
            request.getRequestDispatcher("view/CommonScreen/register.jsp").forward(request, response);
            return;
        }
        
        // Kiểm tra định dạng email
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            request.setAttribute("error", "Email không hợp lệ!");
            request.getRequestDispatcher("view/CommonScreen/register.jsp").forward(request, response);
            return;
        }
        
        // Kiểm tra username đã tồn tại
        if (userDAO.checkUsernameExists(userName)) {
            request.setAttribute("error", "Tên đăng nhập đã tồn tại!");
            request.getRequestDispatcher("view/CommonScreen/register.jsp").forward(request, response);
            return;
        }
        
        // Kiểm tra email đã tồn tại
        if (userDAO.checkEmailExists(email)) {
            request.setAttribute("error", "Email đã được sử dụng!");
            request.getRequestDispatcher("view/CommonScreen/register.jsp").forward(request, response);
            return;
        }
        
        // Tạo user code tự động
        String userCode = userDAO.generateUserCode();
        
        // Chuyển đổi giới tính
        Boolean male = gender.equals("male");
        
        // Thực hiện đăng ký
        boolean success = userDAO.registerCustomer(userCode, fullName, userName, password, email, phone, male, dateOfBirth);
        
        if (success) {
            request.setAttribute("success", "Đăng ký thành công! Bạn có thể đăng nhập ngay bây giờ.");
            request.setAttribute("registeredUsername", userName);
            request.getRequestDispatcher("view/CommonScreen/register.jsp").forward(request, response);
        } else {
            request.setAttribute("error", "Có lỗi xảy ra khi đăng ký. Vui lòng thử lại!");
            request.getRequestDispatcher("view/CommonScreen/register.jsp").forward(request, response);
        }
    }
}
