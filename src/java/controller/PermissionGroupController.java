/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dal.PermissionGroupDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import model.PermissionGroup;

/**
 *
 * @author MinHeee
 */
public class PermissionGroupController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        PermissionGroupDAO db = new PermissionGroupDAO();
        List<PermissionGroup> groups = db.getAll();

        request.setAttribute("groups", groups);
        // forward bằng đường dẫn tuyệt đối (từ context root)
        request.getRequestDispatcher("/view/Test.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // quan trọng: set encoding trước khi đọc param nếu có tiếng Việt
        request.setCharacterEncoding("UTF-8");

        String groupName = request.getParameter("groupName");
        String description = request.getParameter("description");

        if (groupName != null && !groupName.trim().isEmpty()) {
            PermissionGroup pg = new PermissionGroup();
            pg.setGroupName(groupName);
            pg.setDescription(description);

            PermissionGroupDAO db = new PermissionGroupDAO();
            db.insert(pg);
        }

        // sau khi insert, gọi lại doGet để load lại danh sách và forward Test.jsp
        doGet(request, response);
    }
}