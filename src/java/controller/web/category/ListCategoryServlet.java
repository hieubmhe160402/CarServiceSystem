/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.web.category;

import dal.CategoryDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import model.Category;

/**
 *
 * @author LEGION
 */
@WebServlet(name = "ListCategoryServlet", urlPatterns = {"/category"})
public class ListCategoryServlet extends HttpServlet {

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
            out.println("<title>Servlet ListCategoryServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ListCategoryServlet at " + request.getContextPath() + "</h1>");
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
        CategoryDAO categoryDAO = new CategoryDAO();

        // Lấy page và filter từ request
        int currentPage = 1;
        int pageSize = 7;
        String pageParam = request.getParameter("page");
        if (pageParam != null) {
            currentPage = Integer.parseInt(pageParam);
        }
        String filterType = request.getParameter("type"); // lấy filter từ dropdown

        // Lấy danh sách phân trang + lọc
        List<Category> categoryList = categoryDAO.getByPageAndType(currentPage, pageSize, filterType);
        int totalItems = categoryDAO.count(filterType);
        int totalPages = (int) Math.ceil((double) totalItems / pageSize);

        request.setAttribute("categoryList", categoryList);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("filterType", filterType == null ? "" : filterType);

        request.getRequestDispatcher("view/category/list-category.jsp").forward(request, response);
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
        response.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");
        CategoryDAO dao = new CategoryDAO();

        if ("add".equalsIgnoreCase(action)) {
            // Thêm mới
            String name = request.getParameter("name");
            String type = request.getParameter("type");
            String description = request.getParameter("description");

            Category category = new Category();
            category.setName(name);
            category.setType(type);
            category.setDescription(description);

            boolean success = dao.add(category);

            if (success) {
                response.sendRedirect("category");
            } else {
                request.setAttribute("errorMsg", "Không thể thêm Category!");
                request.getRequestDispatcher("view/category/list-category.jsp").forward(request, response);
            }

        } else if ("edit".equalsIgnoreCase(action)) {
            // Sửa
            int id = Integer.parseInt(request.getParameter("categoryID"));
            String name = request.getParameter("name");
            String type = request.getParameter("type");
            String description = request.getParameter("description");

            Category category = new Category();
            category.setCategoryId(id);
            category.setName(name);
            category.setType(type);
            category.setDescription(description);

            boolean success = dao.update(category);

            if (success) {
                response.sendRedirect("category");
            } else {
                request.setAttribute("errorMsg", "Không thể cập nhật Category!");
                request.getRequestDispatcher("view/category/list-category.jsp").forward(request, response);
            }

        } else if ("delete".equalsIgnoreCase(action)) {
            // Xóa
            int id = Integer.parseInt(request.getParameter("id"));
            boolean success = dao.delete(id);

            response.setContentType("application/json");
            
            if (success) {
                response.sendRedirect("category");
            } else {
                request.setAttribute("errorMsg", "Không thể xóa Category!");
                request.getRequestDispatcher("view/category/list-category.jsp").forward(request, response);
            }
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
