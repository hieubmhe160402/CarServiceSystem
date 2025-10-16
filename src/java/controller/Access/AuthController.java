/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller.Access;

import dal.TokenForgetPasswordDAO;
import dal.UserDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.TokenForgetPassword;
import util.EmailUtil;
import util.EmailUtil.SmtpConfig;
import model.User;

/**
 *
 * @author MinHeee
 */
@WebServlet(name = "AuthController", urlPatterns = {"/authController"})
public class AuthController extends HttpServlet {

    private UserDAO userDAO = new UserDAO();
    private TokenForgetPasswordDAO tokenDAO = new TokenForgetPasswordDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action == null) {
            request.getRequestDispatcher("view/CommonScreen/login.jsp").forward(request, response);
        } else {
            switch (action) {
                case "login":
                    request.getRequestDispatcher("view/CommonScreen/login.jsp").forward(request, response);
                    break;
                case "changePassword":
                    request.getRequestDispatcher("view/CommonScreen/changePassword.jsp").forward(request, response);
                    break;
                case "resetPassword":
                    request.getRequestDispatcher("view/CommonScreen/resetPassword.jsp").forward(request, response);
                    break;
                case "newPassword":
                    String token = request.getParameter("token");
                    if (token != null && !token.isEmpty()) {
                        TokenForgetPassword tokenObj = tokenDAO.getTokenByToken(token);
                        if (tokenObj != null) {
                            request.setAttribute("token", token);
                            request.getRequestDispatcher("view/CommonScreen/newPassword.jsp").forward(request, response);
                        } else {
                            request.setAttribute("error", "Token không hợp lệ hoặc đã hết hạn!");
                            request.getRequestDispatcher("view/CommonScreen/resetPassword.jsp").forward(request, response);
                        }
                    } else {
                        request.getRequestDispatcher("view/CommonScreen/resetPassword.jsp").forward(request, response);
                    }
                    break;
                case "logout":
                    HttpSession session = request.getSession();
                    session.invalidate();
                    response.sendRedirect("authController?action=login");
                    break;
                case "home":
                    HttpSession sessionHome = request.getSession();
                    User currentUser = (User) sessionHome.getAttribute("user");
                    if (currentUser == null) {
                        response.sendRedirect("authController?action=login");
                        return;
                    }

                    redirectToHomeByRole(currentUser, response);
                    break;
                default:
                    request.getRequestDispatcher("view/CommonScreen/login.jsp").forward(request, response);
                    break;
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        switch (action) {
            case "login":
                handleLogin(request, response);
                break;
            case "changePassword":
                handleChangePassword(request, response);
                break;
            case "resetPassword":
                handleResetPassword(request, response);
                break;
            case "newPassword":
                handleNewPassword(request, response);
                break;
            default:
                request.getRequestDispatcher("view/CommonScreen/login.jsp").forward(request, response);
                break;
        }
    }

    private void handleLogin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String userName = request.getParameter("userName");
        String password = request.getParameter("password");

        if (userName == null || password == null || userName.isEmpty() || password.isEmpty()) {
            request.setAttribute("error", "Vui lòng nhập đầy đủ thông tin!");
            request.getRequestDispatcher("view/CommonScreen/login.jsp").forward(request, response);
            return;
        }

        User user = userDAO.login(userName, password);

        if (user != null) {
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            userDAO.updateLastLogin(user.getUserId());

            // Redirect based on user role
            String roleName = user.getRole().getRoleName();
            switch (roleName) {
                case "Admin":
                    response.sendRedirect("adminController");
                    break;
                case "Staff":
                    response.sendRedirect("staffController");
                    break;
                case "Accountant":
                    response.sendRedirect("accountantController");
                    break;
                case "ServiceTechnician":
                    response.sendRedirect("technicianController");
                    break;
                case "CarOwner":
                    response.sendRedirect("customerController");
                    break;
                default:
                    // Nếu role không khớp, quay về login
                    request.setAttribute("error", "Role người dùng không hợp lệ!");
                    request.getRequestDispatcher("view/CommonScreen/login.jsp").forward(request, response);
                    break;
            }
        } else {
            request.setAttribute("error", "Tên đăng nhập hoặc mật khẩu không đúng!");
            request.getRequestDispatcher("view/CommonScreen/login.jsp").forward(request, response);
        }
    }

    private void handleChangePassword(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("authController?action=login");
            return;
        }

        String oldPassword = request.getParameter("oldPassword");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        if (oldPassword == null || newPassword == null || confirmPassword == null
                || oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            request.setAttribute("error", "Vui lòng nhập đầy đủ thông tin!");
            request.getRequestDispatcher("view/CommonScreen/changePassword.jsp").forward(request, response);
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            request.setAttribute("error", "Mật khẩu mới và xác nhận mật khẩu không khớp!");
            request.getRequestDispatcher("view/CommonScreen/changePassword.jsp").forward(request, response);
            return;
        }

        if (newPassword.length() < 6) {
            request.setAttribute("error", "Mật khẩu mới phải có ít nhất 6 ký tự!");
            request.getRequestDispatcher("view/CommonScreen/changePassword.jsp").forward(request, response);
            return;
        }

        boolean success = userDAO.changePassword(user.getUserId(), oldPassword, newPassword);

        if (success) {
            request.setAttribute("success", "Đổi mật khẩu thành công!");
            request.getRequestDispatcher("view/CommonScreen/changePassword.jsp").forward(request, response);
        } else {
            request.setAttribute("error", "Mật khẩu cũ không đúng!");
            request.getRequestDispatcher("view/CommonScreen/changePassword.jsp").forward(request, response);
        }
    }

    private void handleResetPassword(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("email");

        if (email == null || email.isEmpty()) {
            request.setAttribute("error", "Vui lòng nhập email!");
            request.getRequestDispatcher("view/CommonScreen/resetPassword.jsp").forward(request, response);
            return;
        }

        User user = userDAO.getUserByEmail(email);

        if (user != null) {
            String token = tokenDAO.createToken(user.getUserId());
            if (token != null) {
                // Build reset URL
                String baseUrl = request.getRequestURL().toString().replace(request.getRequestURI(), request.getContextPath());
                String resetUrl = baseUrl + "/authController?action=newPassword&token=" + token;

                // Load SMTP config from context params
                SmtpConfig cfg = new SmtpConfig();
                cfg.host = getServletContext().getInitParameter("mail.host");
                cfg.port = Integer.parseInt(getServletContext().getInitParameter("mail.port"));
                cfg.starttls = Boolean.parseBoolean(getServletContext().getInitParameter("mail.starttls"));
                cfg.ssl = Boolean.parseBoolean(getServletContext().getInitParameter("mail.ssl"));
                cfg.username = getServletContext().getInitParameter("mail.username");
                cfg.password = getServletContext().getInitParameter("mail.password");
                cfg.from = getServletContext().getInitParameter("mail.from");

                String subject = "[Car Service] Đặt lại mật khẩu";
                String html = EmailUtil.buildResetPasswordEmail(user.getFullName(), resetUrl, token);

                try {
                    EmailUtil.sendEmail(cfg, user.getEmail(), subject, html);
                    request.setAttribute("success", "Email đặt lại mật khẩu đã được gửi! Vui lòng kiểm tra hộp thư.");
                } catch (Exception ex) {
                    ex.printStackTrace();

                    // fallback: hiển thị token để test trong môi trường dev
                    request.setAttribute("success", "Không gửi được email trong môi trường hiện tại. Token: " + token);
                    request.setAttribute("token", token);
                }
            } else {
                request.setAttribute("error", "Có lỗi xảy ra khi tạo token!");
            }
        } else {
            request.setAttribute("error", "Email không tồn tại trong hệ thống!");
        }

        request.getRequestDispatcher("view/CommonScreen/resetPassword.jsp").forward(request, response);
    }

    private void handleNewPassword(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String token = request.getParameter("token");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        if (token == null || newPassword == null || confirmPassword == null
                || token.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            request.setAttribute("error", "Vui lòng nhập đầy đủ thông tin!");
            request.setAttribute("token", token);
            request.getRequestDispatcher("view/CommonScreen/newPassword.jsp").forward(request, response);
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            request.setAttribute("error", "Mật khẩu mới và xác nhận mật khẩu không khớp!");
            request.setAttribute("token", token);
            request.getRequestDispatcher("view/CommonScreen/newPassword.jsp").forward(request, response);
            return;
        }

        if (newPassword.length() < 6) {
            request.setAttribute("error", "Mật khẩu mới phải có ít nhất 6 ký tự!");
            request.setAttribute("token", token);
            request.getRequestDispatcher("view/CommonScreen/newPassword.jsp").forward(request, response);
            return;
        }

        TokenForgetPassword tokenObj = tokenDAO.getTokenByToken(token);

        if (tokenObj != null) {
            boolean success = userDAO.updatePassword(tokenObj.getUser().getUserId(), newPassword);

            if (success) {
                tokenDAO.markTokenAsUsed(token);
                request.setAttribute("success", "Đặt lại mật khẩu thành công! Vui lòng đăng nhập lại.");
                request.getRequestDispatcher("view/CommonScreen/login.jsp").forward(request, response);
            } else {
                request.setAttribute("error", "Có lỗi xảy ra khi cập nhật mật khẩu!");
                request.setAttribute("token", token);
                request.getRequestDispatcher("view/CommonScreen/newPassword.jsp").forward(request, response);
            }
        } else {
            request.setAttribute("error", "Token không hợp lệ hoặc đã hết hạn!");
            request.getRequestDispatcher("view/CommonScreen/resetPassword.jsp").forward(request, response);
        }
    }
//Button return home 

    private void redirectToHomeByRole(User user, HttpServletResponse response) throws IOException {
        String roleName = user.getRole().getRoleName();

        switch (roleName) {
            case "Admin":
                response.sendRedirect("adminController");
                break;
            case "Staff":
                response.sendRedirect("staffController");
                break;
            case "Accountant":
                response.sendRedirect("accountantController");
                break;
            case "ServiceTechnician":
                response.sendRedirect("technicianController");
                break;
            case "CarOwner":
                response.sendRedirect("customerController");
                break;
            default:
                response.sendRedirect("authController?action=login");
                break;
        }

    }
}
