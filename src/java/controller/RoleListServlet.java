package controller;

import dal.RoleDAO;
import model.Role;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;


public class RoleListServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RoleDAO dao = new RoleDAO();
        List<Role> list = dao.getAllRole1();

        request.setAttribute("listRole", list);
        request.getRequestDispatcher("roleList.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        RoleDAO dao = new RoleDAO();

        try {
            if ("add".equals(action)) {
                // Lấy dữ liệu từ popup add form
                String roleName = request.getParameter("roleName");
                String description = request.getParameter("description");

                Role role = new Role();
                role.setRoleName(roleName);
                role.setDescription(description);

                dao.insertRole(role);

            } else if ("delete".equals(action)) {
                int roleId = Integer.parseInt(request.getParameter("roleId"));
                dao.deleteRole(roleId);

            } else if ("toggle".equals(action)) {
                int roleId = Integer.parseInt(request.getParameter("roleId"));
                boolean status = Boolean.parseBoolean(request.getParameter("status"));
                dao.updateStatus(roleId, !status);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Sau khi xử lý thì load lại list
        response.sendRedirect("rolelist");
    }
}
