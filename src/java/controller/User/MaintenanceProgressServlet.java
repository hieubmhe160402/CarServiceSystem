package controller.User;

import dal.CarMaintenanceDAO;
import model.CarMaintenance;
import model.ServiceDetail;
import model.ServicePartDetail;
import model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "maintenanceProgress", urlPatterns = {"/maintenanceProgress"})
public class MaintenanceProgressServlet extends HttpServlet {
    
    // ==================== DOGET: HIỂN THỊ DANH SÁCH VÀ CHI TIẾT ====================
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        // Kiểm tra đăng nhập
        if (user == null) {
            response.sendRedirect("view/CommonScreen/login.jsp");
            return;
        }
        
        String action = request.getParameter("action");
        CarMaintenanceDAO dao = new CarMaintenanceDAO();
        
        try {
            if ("detail".equals(action)) {
                showDetail(request, response, dao, user.getUserId());
            } else if ("search".equals(action)) {
                searchMaintenance(request, response, dao, user.getUserId());
            } else {
                showList(request, response, dao, user.getUserId());
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "⚠️ Lỗi trong quá trình xử lý: " + e.getMessage());
            request.getRequestDispatcher("view/Customer/maintenanceProgress.jsp").forward(request, response);
        }
    }
    
    /**
     * Hiển thị danh sách maintenance của user
     */
    private void showList(HttpServletRequest request, HttpServletResponse response, 
            CarMaintenanceDAO dao, int userId) throws ServletException, IOException, SQLException {
        
        List<CarMaintenance> maintenanceList = dao.getMaintenanceByUserId(userId);
        
        // Tính tổng thành tiền cho mỗi maintenance (bao gồm dịch vụ và phụ tùng riêng)
        for (CarMaintenance maintenance : maintenanceList) {
            BigDecimal totalFinalAmount = calculateTotalFinalAmount(dao, maintenance);
            maintenance.setFinalAmount(totalFinalAmount);
        }
        
        request.setAttribute("maintenanceList", maintenanceList);
        request.getRequestDispatcher("view/Customer/maintenanceProgress.jsp").forward(request, response);
    }
    
    /**
     * Hiển thị chi tiết một maintenance
     */
    private void showDetail(HttpServletRequest request, HttpServletResponse response, 
            CarMaintenanceDAO dao, int userId) throws ServletException, IOException, SQLException {
        
        String idParam = request.getParameter("id");
        if (idParam == null || idParam.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/maintenanceProgress");
            return;
        }
        
        try {
            int maintenanceID = Integer.parseInt(idParam);
            
            CarMaintenance maintenance = dao.getMaintenanceByID(maintenanceID);
            if (maintenance == null) {
                request.setAttribute("errorMessage", "⚠️ Không tìm thấy thông tin bảo dưỡng");
                showList(request, response, dao, userId);
                return;
            }
            
            // Kiểm tra xem maintenance có thuộc về user hiện tại không
            if (maintenance.getCar().getOwner().getUserId() != userId) {
                request.setAttribute("errorMessage", "⚠️ Bạn không có quyền xem thông tin này");
                showList(request, response, dao, userId);
                return;
            }
            
            // Lấy chi tiết dịch vụ và phụ tùng
            List<ServiceDetail> serviceDetails = dao.getServiceDetails(maintenanceID);
            List<ServicePartDetail> partDetails = dao.getPartDetails(maintenanceID);
            
            request.setAttribute("maintenance", maintenance);
            request.setAttribute("serviceDetails", serviceDetails);
            request.setAttribute("partDetails", partDetails);
            
            request.getRequestDispatcher("view/Customer/maintenanceProgress.jsp")
                    .forward(request, response);
            
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/maintenanceProgress");
        }
    }
    
    /**
     * Tìm kiếm maintenance theo biển số xe
     */
    private void searchMaintenance(HttpServletRequest request, HttpServletResponse response, 
            CarMaintenanceDAO dao, int userId) throws ServletException, IOException, SQLException {
        
        String licensePlate = request.getParameter("licensePlate");
        
        if (licensePlate == null || licensePlate.trim().isEmpty()) {
            showList(request, response, dao, userId);
            return;
        }
        
        // Tìm kiếm theo biển số và userId
        List<CarMaintenance> maintenanceList = dao.searchByLicensePlateAndUserId(licensePlate, userId);
        
        // Tính tổng thành tiền cho mỗi maintenance
        for (CarMaintenance maintenance : maintenanceList) {
            BigDecimal totalFinalAmount = calculateTotalFinalAmount(dao, maintenance);
            maintenance.setFinalAmount(totalFinalAmount);
        }
        
        request.setAttribute("maintenanceList", maintenanceList);
        request.setAttribute("searchKeyword", licensePlate);
        request.getRequestDispatcher("view/Customer/maintenanceProgress.jsp").forward(request, response);
    }
    
    /**
     * Tính tổng thành tiền cuối cùng
     * = Thành tiền gói (đã giảm giá) + Dịch vụ riêng (không từ gói) + Phụ tùng riêng (không từ gói)
     */
    private BigDecimal calculateTotalFinalAmount(CarMaintenanceDAO dao, CarMaintenance maintenance) 
            throws SQLException {
        
        // Lấy thành tiền gói (đã trừ giảm giá)
        BigDecimal packageFinalAmount = maintenance.getFinalAmount();
        if (packageFinalAmount == null) {
            packageFinalAmount = BigDecimal.ZERO;
        }
        
        // Lấy danh sách dịch vụ và phụ tùng
        List<ServiceDetail> serviceDetails = dao.getServiceDetails(maintenance.getMaintenanceId());
        List<ServicePartDetail> partDetails = dao.getPartDetails(maintenance.getMaintenanceId());
        
        // Tính tổng dịch vụ KHÔNG từ gói
        BigDecimal serviceTotalAmount = BigDecimal.ZERO;
        for (ServiceDetail service : serviceDetails) {
            if (!service.isFromPackage() && service.getTotalPrice() != null) {
                serviceTotalAmount = serviceTotalAmount.add(service.getTotalPrice());
            }
        }
        
        // Tính tổng phụ tùng KHÔNG từ gói
        BigDecimal partTotalAmount = BigDecimal.ZERO;
        for (ServicePartDetail part : partDetails) {
            if (!part.isFromPackage() && part.getTotalPrice() != null) {
                partTotalAmount = partTotalAmount.add(part.getTotalPrice());
            }
        }
        
        // Tổng thành tiền = Gói (đã giảm giá) + Dịch vụ riêng + Phụ tùng riêng
        return packageFinalAmount.add(serviceTotalAmount).add(partTotalAmount);
    }
    
    // ==================== DOPOST: XỬ LÝ CÁC ACTION =================
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        doGet(request, response);
    }
    
    @Override
    public String getServletInfo() {
        return "Maintenance Progress tracking page for logged-in users";
    }
}