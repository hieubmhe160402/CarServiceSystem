/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dal.PermissionDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import model.Permission;

/**
 *
 * @author Admin
 */


    @WebServlet(name = "RolePermissionServlet", urlPatterns = {"/rolePermission"})
public class RolePermissionController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int roleId = Integer.parseInt(request.getParameter("roleId"));
        PermissionDAO dao = new PermissionDAO();

        List<Permission> allPermissions = dao.getAllPermissions();
        List<Permission> rolePermissions = dao.getPermissionsByRole(roleId);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        StringBuilder json = new StringBuilder("{");
        json.append("\"allPermissions\":[");
        for (int i = 0; i < allPermissions.size(); i++) {
            Permission p = allPermissions.get(i);
            json.append("{\"permissionID\":").append(p.getPermissionID())
                .append(",\"name\":\"").append(p.getName()).append("\"}");
            if (i < allPermissions.size() - 1) json.append(",");
        }
        json.append("],\"rolePermissions\":[");
        for (int i = 0; i < rolePermissions.size(); i++) {
            Permission p = rolePermissions.get(i);
            json.append("{\"permissionID\":").append(p.getPermissionID())
                .append(",\"name\":\"").append(p.getName()).append("\"}");
            if (i < rolePermissions.size() - 1) json.append(",");
        }
        json.append("]}");

        response.getWriter().write(json.toString());
    }

    @Override
protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

    int roleId = Integer.parseInt(request.getParameter("roleId"));
    String[] selectedPermissions = request.getParameterValues("permissionIds"); // ✅ trùng với JSP

    PermissionDAO dao = new PermissionDAO();
    dao.updateRolePermissions(roleId, selectedPermissions);

    response.setStatus(HttpServletResponse.SC_OK);
}

}



