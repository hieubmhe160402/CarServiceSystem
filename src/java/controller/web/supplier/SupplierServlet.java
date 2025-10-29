/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.web.supplier;

import dal.SupplierDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import model.Supplier;

/**
 *
 * @author MinHeee
 */
@WebServlet(name = "SupplierServlet", urlPatterns = {"/supplier"})
public class SupplierServlet extends HttpServlet {

    private SupplierDAO dao = new SupplierDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }

        switch (action) {
            case "addForm":
                request.getRequestDispatcher("view/supplier/page-list-supplier.jsp").forward(request, response);
                break;
            case "editForm":
                int id = Integer.parseInt(request.getParameter("id"));
                Supplier s = dao.getSupplierById(id);
                request.setAttribute("supplier", s);
                request.getRequestDispatcher("view/supplier/page-list-supplier.jsp").forward(request, response);
                break;
            default:
                int page = 1;
                int pageSize = 10;

                String pageParam = request.getParameter("page");
                if (pageParam != null) {
                    try {
                        page = Integer.parseInt(pageParam);
                    } catch (NumberFormatException e) {
                        page = 1;
                    }
                }

                int totalSuppliers = dao.getTotalSuppliers();
                int totalPages = (int) Math.ceil((double) totalSuppliers / pageSize);

                List<Supplier> list = dao.getSuppliersByPage(page, pageSize);

                request.setAttribute("suppliers", list);
                request.setAttribute("currentPage", page);
                request.setAttribute("totalPages", totalPages);

                request.getRequestDispatcher("view/supplier/page-list-supplier.jsp").forward(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        String currentPage = request.getParameter("currentPage");
        if (currentPage == null || currentPage.isEmpty()) {
            currentPage = "1";
        }

        StringBuilder errorMsg = new StringBuilder();

        if ("add".equals(action)) {
            Supplier s = new Supplier();
            s.setName(request.getParameter("name"));
            s.setAddress(request.getParameter("address"));
            s.setPhone(request.getParameter("phone"));
            s.setEmail(request.getParameter("email"));
            s.setDescription(request.getParameter("description"));
            s.setIsActive(Boolean.parseBoolean(request.getParameter("isActive")));

            String phone = s.getPhone();
            String email = s.getEmail();

            if (phone == null || !phone.matches("\\d{10}")) {
                errorMsg.append("❌ Số điện thoại phải gồm đúng 10 chữ số!<br>");
            }

            if (email == null || !email.endsWith("@gmail.com")) {
                errorMsg.append("❌ Email phải có đuôi @gmail.com!<br>");
            }

            if (errorMsg.length() > 0) {
                List<Supplier> list = dao.getSuppliersByPage(Integer.parseInt(currentPage), 10);
                request.setAttribute("suppliers", list);
                request.setAttribute("supplier", s);
                request.setAttribute("errorMessage", errorMsg.toString());
                request.setAttribute("showModal", true);
                request.setAttribute("currentPage", Integer.parseInt(currentPage));
                request.setAttribute("totalPages",
                        (int) Math.ceil((double) dao.getTotalSuppliers() / 10));
                request.getRequestDispatcher("view/supplier/page-list-supplier.jsp").forward(request, response);
                return;
            }

            dao.addSupplier(s);
            response.sendRedirect("supplier?page=" + currentPage);

        } else if ("edit".equals(action)) {
            Supplier s = new Supplier();
            s.setSupplierId(Integer.parseInt(request.getParameter("supplierId")));
            s.setName(request.getParameter("name"));
            s.setAddress(request.getParameter("address"));
            s.setPhone(request.getParameter("phone"));
            s.setEmail(request.getParameter("email"));
            s.setDescription(request.getParameter("description"));
            s.setIsActive(Boolean.parseBoolean(request.getParameter("isActive")));

            String phone = s.getPhone();
            String email = s.getEmail();

            if (phone == null || !phone.matches("\\d{10}")) {
                errorMsg.append("❌ Số điện thoại phải gồm đúng 10 chữ số!<br>");
            }

            if (email == null || !email.endsWith("@gmail.com")) {
                errorMsg.append("❌ Email phải có đuôi @gmail.com!<br>");
            }

            if (errorMsg.length() > 0) {
                List<Supplier> list = dao.getSuppliersByPage(Integer.parseInt(currentPage), 10);
                request.setAttribute("suppliers", list);
                request.setAttribute("errorMessage", errorMsg.toString());
                request.setAttribute("supplier", s);
                request.setAttribute("showModal", true);
                request.setAttribute("currentPage", Integer.parseInt(currentPage));
                request.setAttribute("totalPages",
                        (int) Math.ceil((double) dao.getTotalSuppliers() / 10));
                request.getRequestDispatcher("view/supplier/page-list-supplier.jsp").forward(request, response);
                return;
            }

            dao.updateSupplier(s);
            response.sendRedirect("supplier?page=" + currentPage);

        } else if ("updateStatus".equals(action)) {
            int id = Integer.parseInt(request.getParameter("id"));
            boolean status = Boolean.parseBoolean(request.getParameter("status"));
            dao.updateStatus(id, status);
            response.sendRedirect("supplier?page=" + currentPage);
        }
    }

}
