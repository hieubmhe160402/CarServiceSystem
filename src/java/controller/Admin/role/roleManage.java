package controller.Admin.role;

import dal.RoleDAO;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Role;
import model.User;

@WebServlet(name = "roleManage", urlPatterns = {"/roleManage"})
public class roleManage extends HttpServlet {

    private static final int RECORDS_PER_PAGE = 5;
    private final RoleDAO roleDAO = new RoleDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        RoleDAO dao = new RoleDAO();

        // ✅ Kiểm tra session đăng nhập
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("view/CommonScreen/login.jsp");
            return;
        }

        // ✅ Kiểm tra quyền admin
        User user = (User) session.getAttribute("user");
        if (user == null || user.getRole() == null || !"Admin".equalsIgnoreCase(user.getRole().getRoleName())) {
            response.sendRedirect("noAccess.jsp");
            return;
        }

        // ✅ Lấy từ khóa tìm kiếm (nếu có)
        String keyword = request.getParameter("keyword");
        request.setAttribute("keyword", keyword);

        // ✅ Xử lý phân trang
        int page = 1;
        String pageParam = request.getParameter("page");
        if (pageParam != null) {
            try {
                page = Integer.parseInt(pageParam);
            } catch (NumberFormatException e) {
                page = 1;
            }
        }

        List<Role> roles;
        int totalRecords;

        if (keyword != null && !keyword.trim().isEmpty()) {
            roles = dao.searchRoles(keyword.trim());
            totalRecords = roles.size();
        } else {
            totalRecords = dao.getTotalRoles();
            roles = dao.getRolesByPage((page - 1) * RECORDS_PER_PAGE, RECORDS_PER_PAGE);
        }

        int totalPages = (int) Math.ceil(totalRecords * 1.0 / RECORDS_PER_PAGE);

        request.setAttribute("roles", roles);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);

        // ✅ Nếu có thông báo lỗi hoặc thành công
        String error = request.getParameter("error");
        String success = request.getParameter("success");
        if (error != null) request.setAttribute("error", error);
        if (success != null) request.setAttribute("success", success);

        request.getRequestDispatcher("view/role/manageRole.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("view/CommonScreen/login.jsp");
            return;
        }

        User user = (User) session.getAttribute("user");
        if (user == null || user.getRole() == null || !"Admin".equalsIgnoreCase(user.getRole().getRoleName())) {
            response.sendRedirect("noAccess.jsp");
            return;
        }

        String action = request.getParameter("action");
        if (action == null) action = "";

        switch (action) {
            case "insert":
                insertRole(request, response);
                break;
            case "update":
                updateRole(request, response);
                break;
            default:
                response.sendRedirect("roleManage");
                break;
        }
    }

    // ================== CÁC HÀM XỬ LÝ ===================

    private void insertRole(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        String name = request.getParameter("roleName");
        String desc = request.getParameter("description");

        if (name == null || name.trim().isEmpty()) {
            String msg = URLEncoder.encode("Tên vai trò không được để trống!", "UTF-8");
            response.sendRedirect("roleManage?error=" + msg);
            return;
        }

        List<Role> all = roleDAO.getAllRolesGara();
        for (Role r : all) {
            if (r.getRoleName().equalsIgnoreCase(name.trim())) {
                String msg = URLEncoder.encode("Tên vai trò đã tồn tại!", "UTF-8");
                response.sendRedirect("roleManage?error=" + msg);
                return;
            }
        }

        Role r = new Role();
        r.setRoleName(name.trim());
        r.setDescription(desc);
        roleDAO.insertRole(r);

        String success = URLEncoder.encode("Thêm vai trò thành công!", "UTF-8");
        response.sendRedirect("roleManage?success=" + success);
    }

    private void updateRole(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        int id = Integer.parseInt(request.getParameter("roleID"));
        String name = request.getParameter("roleName");
        String desc = request.getParameter("description");

        if (name == null || name.trim().isEmpty()) {
            String msg = URLEncoder.encode("Tên vai trò không được để trống!", "UTF-8");
            response.sendRedirect("roleManage?error=" + msg);
            return;
        }

        Role r = new Role(id, name.trim(), desc);
        roleDAO.updateRole(r);

        String success = URLEncoder.encode("Cập nhật vai trò thành công!", "UTF-8");
        response.sendRedirect("roleManage?success=" + success);
    }

    @Override
    public String getServletInfo() {
        return "Role management servlet with session-based access control and search";
    }
}
