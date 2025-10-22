<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="model.Appointment" %>
<%@ page import="model.Car" %>
<%@ page import="model.MaintenancePackage" %>
<%@ page import="model.User" %>
<%
    List<Appointment> appointments = (List<Appointment>) request.getAttribute("appointmentList");
    Appointment appointmentDetail = (Appointment) request.getAttribute("appointmentDetail");
    String errorMessage = (String) request.getAttribute("errorMessage");
    String dateFilter = (String) request.getAttribute("dateFilter");
    String packageFilter = (String) request.getAttribute("packageFilter");
    
    // Pagination parameters
    Integer currentPage = (Integer) request.getAttribute("currentPage");
    Integer totalPages = (Integer) request.getAttribute("totalPages");
    Integer totalRecords = (Integer) request.getAttribute("totalRecords");
    
    if (currentPage == null) currentPage = 1;
    if (totalPages == null) totalPages = 1;
    if (totalRecords == null) totalRecords = 0;
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Lịch sử lịch hẹn - Car Management</title>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
    margin: 0;
    font-family: 'Inter', 'Segoe UI', Roboto, Arial, sans-serif;
    background-color: #f5f7fb;
    color: #111827;
}

.app {
    display: flex;
    min-height: 100vh;
}

.main {
    flex: 1;
    padding: 40px;
    overflow-y: auto;
    background: linear-gradient(135deg, #f5f7fb 0%, #e8ecf4 100%);
}

.section-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 35px;
    padding-bottom: 20px;
    border-bottom: 3px solid #e5e7eb;
}

.section-header h2 {
    font-size: 28px;
    font-weight: 800;
    color: #0f2340;
    display: flex;
    align-items: center;
    gap: 12px;
    margin: 0;
}

.section-header h2 i {
    color: #16a34a;
}

.alert {
    border-radius: 12px;
    border: none;
    padding: 16px 20px;
    margin-bottom: 25px;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
    animation: slideDown 0.4s ease;
}

.alert-danger {
    background: linear-gradient(135deg, #fee2e2 0%, #fecaca 100%);
    color: #991b1b;
}

@keyframes slideDown {
    from {
        opacity: 0;
        transform: translateY(-20px);
    }
    to {
        opacity: 1;
        transform: translateY(0);
    }
}

.filter-card {
    background: white;
    border-radius: 16px;
    padding: 28px;
    margin-bottom: 30px;
    box-shadow: 0 4px 16px rgba(0, 0, 0, 0.06);
    border: 1px solid #e5e7eb;
}

.filter-card h4 {
    color: #0f2340;
    font-weight: 700;
    margin-bottom: 20px;
    font-size: 18px;
    display: flex;
    align-items: center;
    gap: 10px;
}

.filter-card h4 i {
    color: #16a34a;
}

.filter-card .form-label {
    color: #374151;
    font-weight: 600;
    margin-bottom: 8px;
    font-size: 14px;
}

.filter-card .form-control {
    border: 2px solid #e5e7eb;
    border-radius: 10px;
    padding: 10px 14px;
    font-size: 14px;
    transition: all 0.3s ease;
}

.filter-card .form-control:focus {
    border-color: #16a34a;
    box-shadow: 0 0 0 4px rgba(22, 163, 74, 0.1);
    outline: none;
}

.btn-search {
    background: linear-gradient(135deg, #16a34a 0%, #15803d 100%);
    color: white;
    border: none;
    padding: 10px 24px;
    border-radius: 10px;
    font-weight: 700;
    font-size: 14px;
    transition: all 0.3s ease;
    box-shadow: 0 4px 12px rgba(22, 163, 74, 0.3);
}

.btn-search:hover {
    background: linear-gradient(135deg, #15803d 0%, #166534 100%);
    transform: translateY(-2px);
    box-shadow: 0 6px 20px rgba(22, 163, 74, 0.4);
    color: white;
}

.btn-reset {
    background: white;
    color: #374151;
    border: 2px solid #e5e7eb;
    padding: 10px 24px;
    border-radius: 10px;
    font-weight: 700;
    font-size: 14px;
    transition: all 0.3s ease;
    text-decoration: none;
    display: inline-block;
}

.btn-reset:hover {
    background: #f3f4f6;
    border-color: #d1d5db;
    transform: translateY(-2px);
}

.table-card {
    background: white;
    border-radius: 16px;
    padding: 0;
    box-shadow: 0 4px 16px rgba(0, 0, 0, 0.06);
    border: 1px solid #e5e7eb;
    overflow: hidden;
}

.table {
    margin: 0;
}

.table thead {
    background: linear-gradient(135deg, #0f2340 0%, #1e3a5f 100%);
}

.table thead th {
    color: black;
    font-weight: 700;
    padding: 18px 16px;
    border: none;
    font-size: 14px;
    text-transform: uppercase;
    letter-spacing: 0.5px;
}

.table tbody tr {
    transition: all 0.3s ease;
    animation: fadeIn 0.5s ease;
}

.table tbody tr:hover {
    background-color: #f9fafb;
    transform: scale(1.01);
}

.table tbody td {
    padding: 16px;
    vertical-align: middle;
    border-bottom: 1px solid #f3f4f6;
    font-size: 14px;
    color: #374151;
}

.btn-view {
    background: linear-gradient(135deg, #0f2340 0%, #1e3a5f 100%);
    color: white;
    border: none;
    padding: 8px 16px;
    border-radius: 8px;
    font-weight: 600;
    font-size: 13px;
    transition: all 0.3s ease;
    text-decoration: none;
    display: inline-flex;
    align-items: center;
    gap: 6px;
}

.btn-view:hover {
    background: linear-gradient(135deg, #1e3a5f 0%, #2d4a7c 100%);
    transform: translateY(-2px);
    box-shadow: 0 4px 12px rgba(15, 35, 64, 0.3);
    color: white;
}

.status-badge {
    padding: 6px 14px;
    border-radius: 20px;
    font-weight: 600;
    font-size: 13px;
    display: inline-block;
}

.status-pending {
    background: linear-gradient(135deg, #fef3c7 0%, #fde68a 100%);
    color: #92400e;
}

.status-confirmed {
    background: linear-gradient(135deg, #dbeafe 0%, #bfdbfe 100%);
    color: #1e40af;
}

.status-completed {
    background: linear-gradient(135deg, #d1fae5 0%, #a7f3d0 100%);
    color: #065f46;
}

.status-cancelled {
    background: linear-gradient(135deg, #fee2e2 0%, #fecaca 100%);
    color: #991b1b;
}

.empty-state {
    text-align: center;
    padding: 60px 20px;
    color: #9ca3af;
}

.empty-state i {
    font-size: 64px;
    margin-bottom: 20px;
    opacity: 0.5;
}

.empty-state h4 {
    color: #6b7280;
    font-weight: 600;
    margin-bottom: 10px;
}

.empty-state p {
    color: #9ca3af;
}

/* Pagination Styles */
.pagination-container {
    background: white;
    border-radius: 0 0 16px 16px;
    padding: 20px 28px;
    border-top: 2px solid #e5e7eb;
    display: flex;
    justify-content: space-between;
    align-items: center;
    flex-wrap: wrap;
    gap: 15px;
}

.pagination-info {
    color: #6b7280;
    font-size: 14px;
    font-weight: 500;
}

.pagination-info strong {
    color: #0f2340;
    font-weight: 700;
}

.pagination {
    margin: 0;
    gap: 6px;
}

.pagination .page-item .page-link {
    border: 2px solid #e5e7eb;
    color: #374151;
    font-weight: 600;
    padding: 8px 14px;
    border-radius: 8px;
    transition: all 0.3s ease;
    margin: 0 2px;
}

.pagination .page-item .page-link:hover {
    background: #f3f4f6;
    border-color: #16a34a;
    color: #16a34a;
    transform: translateY(-2px);
}

.pagination .page-item.active .page-link {
    background: linear-gradient(135deg, #16a34a 0%, #15803d 100%);
    border-color: #16a34a;
    color: white;
    box-shadow: 0 4px 12px rgba(22, 163, 74, 0.3);
}

.pagination .page-item.disabled .page-link {
    background: #f9fafb;
    border-color: #e5e7eb;
    color: #9ca3af;
}

/* Modal Styles */
.modal-content {
    border-radius: 16px;
    border: none;
    box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
}

.modal-header {
    border-radius: 16px 16px 0 0;
    padding: 24px 28px;
    background: linear-gradient(135deg, #0f2340 0%, #1e3a5f 100%);
    color: white;
    border-bottom: none;
}

.modal-header .btn-close {
    filter: brightness(0) invert(1);
    opacity: 0.8;
}

.modal-header .btn-close:hover {
    opacity: 1;
}

.modal-body {
    padding: 28px;
}

.detail-row {
    padding: 16px 0;
    border-bottom: 1px solid #e5e7eb;
    display: flex;
    align-items: flex-start;
}

.detail-row:last-child {
    border-bottom: none;
}

/* Ẩn các detail-row không có nội dung (bị comment) */
.detail-row:empty {
    display: none;
    padding: 0;
    border: none;
}

.detail-label {
    font-weight: 700;
    color: #6b7280;
    min-width: 180px;
    font-size: 14px;
    display: flex;
    align-items: center;
    gap: 8px;
}

.detail-label i {
    color: #16a34a;
    width: 20px;
}

.detail-value {
    color: #374151;
    font-size: 14px;
    flex: 1;
    font-weight: 500;
}

.detail-value strong {
    color: #0f2340;
}

.modal-footer {
    padding: 20px 28px;
    border-top: 2px solid #e5e7eb;
}

@keyframes fadeIn {
    from {
        opacity: 0;
        transform: translateY(20px);
    }
    to {
        opacity: 1;
        transform: translateY(0);
    }
}

@media (max-width: 768px) {
    .app {
        flex-direction: column;
    }
    
    .main {
        padding: 20px;
    }
    
    .section-header h2 {
        font-size: 22px;
    }
    
    .table thead {
        display: none;
    }
    
    .table tbody td {
        display: block;
        text-align: left;
        padding: 10px 16px;
        border: none;
    }
    
    .table tbody tr {
        display: block;
        margin-bottom: 20px;
        border: 1px solid #e5e7eb;
        border-radius: 12px;
        overflow: hidden;
        background: white;
    }
    
    .detail-row {
        flex-direction: column;
    }
    
    .detail-label {
        min-width: 100%;
        margin-bottom: 8px;
    }
    
    .pagination-container {
        flex-direction: column;
        text-align: center;
    }
    
    .pagination {
        flex-wrap: wrap;
        justify-content: center;
    }
}
    </style>
</head>
<body>
<div class="app">
    <!-- Include Sidebar -->
    <jsp:include page="/view/layout/sidebar.jsp"/>

    <!-- Main content -->
    <div class="main">
        <div class="section-header">
            <h2><i class="fas fa-history"></i> Lịch sử lịch hẹn của bạn</h2>
        </div>

        <% if (errorMessage != null) { %>
            <div class="alert alert-danger">
                <i class="fas fa-exclamation-circle"></i> <%= errorMessage %>
            </div>
        <% } %>

        <!-- Filter Card -->
        <div class="filter-card">
            <h4><i class="fas fa-filter"></i> Bộ lọc tìm kiếm</h4>
            <form action="userAppoinmentsHistoryController" method="get">
                <div class="row">
                    <div class="col-md-5">
                        <label class="form-label">Ngày hẹn:</label>
                        <input type="date" name="dateFilter" class="form-control" 
                               value="<%= dateFilter == null ? "" : dateFilter %>">
                    </div>
                    <div class="col-md-5">
                        <label class="form-label">Gói bảo dưỡng:</label>
                        <input type="text" name="packageFilter" class="form-control" 
                               placeholder="Nhập tên gói..." 
                               value="<%= packageFilter == null ? "" : packageFilter %>">
                    </div>
                    <div class="col-md-2 d-flex align-items-end gap-2">
                        <button type="submit" class="btn-search w-100">
                            <i class="fas fa-search"></i> Tìm
                        </button>
                    </div>
                </div>
                <div class="row mt-3">
                    <div class="col-md-12">
                        <a href="userAppoinmentsHistoryController" class="btn-reset">
                            <i class="fas fa-redo"></i> Đặt lại bộ lọc
                        </a>
                    </div>
                </div>
            </form>
        </div>

        <!-- Table Card -->
        <div class="table-card">
            <div class="table-responsive">
                <table class="table">
                    <thead>
                        <tr>
                            <th><i class="fas fa-hashtag"></i> ID</th>
                            <th><i class="fas fa-calendar-alt"></i> Ngày hẹn</th>
                            <th><i class="fas fa-car"></i> Xe</th>
                            <th><i class="fas fa-box"></i> Gói bảo dưỡng</th>
                            <th><i class="fas fa-info-circle"></i> Trạng thái</th>
                            <th><i class="fas fa-eye"></i> Hành động</th>
                        </tr>
                    </thead>
                    <tbody>
                    <% if (appointments != null && !appointments.isEmpty()) {
                        for (Appointment a : appointments) { 
                            String statusClass = "";
                            String statusText = a.getStatus();
                            
                            if (statusText != null) {
                                if (statusText.toLowerCase().contains("pending") || statusText.toLowerCase().contains("chờ")) {
                                    statusClass = "status-pending";
                                } else if (statusText.toLowerCase().contains("confirmed") || statusText.toLowerCase().contains("xác nhận")) {
                                    statusClass = "status-confirmed";
                                } else if (statusText.toLowerCase().contains("completed") || statusText.toLowerCase().contains("hoàn thành")) {
                                    statusClass = "status-completed";
                                } else if (statusText.toLowerCase().contains("cancelled") || statusText.toLowerCase().contains("hủy")) {
                                    statusClass = "status-cancelled";
                                }
                            }
                    %>
                        <tr>
                            <td><strong>#<%= a.getAppointmentId() %></strong></td>
                            <td>
                                <i class="fas fa-calendar text-primary"></i> 
                                <%= a.getAppointmentDate() %>
                            </td>
                            <td>
                                <% if (a.getCar() != null) { %>
                                    <i class="fas fa-car text-success"></i>
                                    <%= a.getCar().getBrand() %> - <%= a.getCar().getLicensePlate() %>
                                <% } else { %>
                                    <span class="text-muted">N/A</span>
                                <% } %>
                            </td>
                            <td>
                                <% if (a.getRequestedPackage() != null) { %>
                                    <i class="fas fa-box text-success"></i>
                                    <%= a.getRequestedPackage().getName() %>
                                <% } else { %>
                                    <span class="text-muted">Không có</span>
                                <% } %>
                            </td>
                            <td>
                                <span class="status-badge <%= statusClass %>">
                                    <%= statusText != null ? statusText : "N/A" %>
                                </span>
                            </td>
                            <td>
                                <a href="userAppoinmentsHistoryController?appointmentId=<%= a.getAppointmentId() %>" 
                                   class="btn-view">
                                    <i class="fas fa-eye"></i> Xem chi tiết
                                </a>
                            </td>
                        </tr>
                    <% } 
                    } else { %>
                        <tr>
                            <td colspan="6">
                                <div class="empty-state">
                                    <i class="fas fa-calendar-times"></i>
                                    <h4>Không có lịch hẹn nào</h4>
                                    <p>Bạn chưa có lịch hẹn bảo dưỡng nào trong hệ thống.</p>
                                </div>
                            </td>
                        </tr>
                    <% } %>
                    </tbody>
                </table>
            </div>
            
            <!-- Pagination -->
            <% if (appointments != null && !appointments.isEmpty() && totalPages > 1) { %>
            <div class="pagination-container">
                <div class="pagination-info">
                    Hiển thị trang <strong><%= currentPage %></strong> / <strong><%= totalPages %></strong> 
                    (Tổng <strong><%= totalRecords %></strong> lịch hẹn)
                </div>
                <nav>
                    <ul class="pagination">
                        <li class="page-item <%= currentPage == 1 ? "disabled" : "" %>">
                            <a class="page-link" href="?page=<%= currentPage - 1 %><%= dateFilter != null ? "&dateFilter=" + dateFilter : "" %><%= packageFilter != null ? "&packageFilter=" + packageFilter : "" %>">
                                <i class="fas fa-chevron-left"></i> Trước
                            </a>
                        </li>
                        
                        <%
                            int startPage = Math.max(1, currentPage - 2);
                            int endPage = Math.min(totalPages, currentPage + 2);
                            
                            if (startPage > 1) {
                        %>
                            <li class="page-item">
                                <a class="page-link" href="?page=1<%= dateFilter != null ? "&dateFilter=" + dateFilter : "" %><%= packageFilter != null ? "&packageFilter=" + packageFilter : "" %>">1</a>
                            </li>
                            <% if (startPage > 2) { %>
                                <li class="page-item disabled"><span class="page-link">...</span></li>
                            <% } %>
                        <% } %>
                        
                        <% for (int i = startPage; i <= endPage; i++) { %>
                            <li class="page-item <%= i == currentPage ? "active" : "" %>">
                                <a class="page-link" href="?page=<%= i %><%= dateFilter != null ? "&dateFilter=" + dateFilter : "" %><%= packageFilter != null ? "&packageFilter=" + packageFilter : "" %>"><%= i %></a>
                            </li>
                        <% } %>
                        
                        <%
                            if (endPage < totalPages) {
                                if (endPage < totalPages - 1) {
                        %>
                                <li class="page-item disabled"><span class="page-link">...</span></li>
                        <%      } %>
                            <li class="page-item">
                                <a class="page-link" href="?page=<%= totalPages %><%= dateFilter != null ? "&dateFilter=" + dateFilter : "" %><%= packageFilter != null ? "&packageFilter=" + packageFilter : "" %>"><%= totalPages %></a>
                            </li>
                        <% } %>
                        
                        <li class="page-item <%= currentPage == totalPages ? "disabled" : "" %>">
                            <a class="page-link" href="?page=<%= currentPage + 1 %><%= dateFilter != null ? "&dateFilter=" + dateFilter : "" %><%= packageFilter != null ? "&packageFilter=" + packageFilter : "" %>">
                                Sau <i class="fas fa-chevron-right"></i>
                            </a>
                        </li>
                    </ul>
                </nav>
            </div>
            <% } %>
        </div>
    </div>
</div>

<!-- Modal Chi tiết lịch hẹn -->
<% if (appointmentDetail != null) { 
    String statusClass = "";
    String statusText = appointmentDetail.getStatus();
    
    if (statusText != null) {
        if (statusText.toLowerCase().contains("pending") || statusText.toLowerCase().contains("chờ")) {
            statusClass = "status-pending";
        } else if (statusText.toLowerCase().contains("confirmed") || statusText.toLowerCase().contains("xác nhận")) {
            statusClass = "status-confirmed";
        } else if (statusText.toLowerCase().contains("completed") || statusText.toLowerCase().contains("hoàn thành")) {
            statusClass = "status-completed";
        } else if (statusText.toLowerCase().contains("cancelled") || statusText.toLowerCase().contains("hủy")) {
            statusClass = "status-cancelled";
        }
    }
%>
<div class="modal fade show" id="detailModal" tabindex="-1" style="display:block; background:rgba(0,0,0,0.5);" aria-modal="true">
    <div class="modal-dialog modal-lg modal-dialog-scrollable">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">
                    <i class="fas fa-file-alt"></i> Chi tiết lịch hẹn #<%= appointmentDetail.getAppointmentId() %>
                </h5>
                <a href="userAppoinmentsHistoryController" class="btn-close"></a>
            </div>
            <div class="modal-body">
                <div class="detail-row">
                    <div class="detail-label">
                        <i class="fas fa-hashtag"></i> Mã lịch hẹn:
                    </div>
                    <div class="detail-value">
                        <strong>#<%= appointmentDetail.getAppointmentId() %></strong>
                    </div>
                </div>
                
                <div class="detail-row">
                    <div class="detail-label">
                        <i class="fas fa-calendar-alt"></i> Ngày hẹn:
                    </div>
                    <div class="detail-value">
                        <%= appointmentDetail.getAppointmentDate() %>
                    </div>
                </div>
                
                <div class="detail-row">
                    <div class="detail-label">
                        <i class="fas fa-info-circle"></i> Trạng thái:
                    </div>
                    <div class="detail-value">
                        <span class="status-badge <%= statusClass %>">
                            <%= statusText != null ? statusText : "N/A" %>
                        </span>
                    </div>
                </div>
                
                
                
                <div class="detail-row">
                    <div class="detail-label">
                        <i class="fas fa-sticky-note"></i> Ghi chú:
                    </div>
                    <div class="detail-value">
                        <%= appointmentDetail.getNotes() != null && !appointmentDetail.getNotes().isEmpty() ? 
                            appointmentDetail.getNotes() : "Không có ghi chú" %>
                    </div>
                </div>

                <% if (appointmentDetail.getCar() != null) { %>
                <div class="detail-row">
                    <div class="detail-label">
                        <i class="fas fa-car"></i> Thông tin xe:
                    </div>
                    <div class="detail-value">
                        <strong><%= appointmentDetail.getCar().getBrand() %> 
                        <%= appointmentDetail.getCar().getModel() != null ? appointmentDetail.getCar().getModel() : "" %></strong><br>
                        Biển số: <%= appointmentDetail.getCar().getLicensePlate() %><br>
                        <% if (appointmentDetail.getCar().getYear() > 0) { %>
                        Năm sản xuất: <%= appointmentDetail.getCar().getYear() %><br>
                        <% } %>
                        <% if (appointmentDetail.getCar().getColor() != null) { %>
                        Màu sắc: <%= appointmentDetail.getCar().getColor() %>
                        <% } %>
                    </div>
                </div>
                <% } %>

                <% if (appointmentDetail.getRequestedPackage() != null) { %>
                <div class="detail-row">
                    <div class="detail-label">
                        <i class="fas fa-box"></i> Gói bảo dưỡng:
                    </div>
                    <div class="detail-value">
                        <strong><%= appointmentDetail.getRequestedPackage().getName() %></strong><br>
                        <% if (appointmentDetail.getRequestedPackage().getPackageCode() != null) { %>
                        Mã gói: <%= appointmentDetail.getRequestedPackage().getPackageCode() %><br>
                        <% } %>
                        <% if (appointmentDetail.getRequestedPackage().getDescription() != null && !appointmentDetail.getRequestedPackage().getDescription().isEmpty()) { %>
                        Mô tả: <%= appointmentDetail.getRequestedPackage().getDescription() %><br>
                        <% } %>
                        <% if (appointmentDetail.getRequestedPackage().getKilometerMilestone() != null) { %>
                        Mốc km: <%= appointmentDetail.getRequestedPackage().getKilometerMilestone() %> km<br>
                        <% } %>
                        <% if (appointmentDetail.getRequestedPackage().getMonthMilestone() != null) { %>
                        Mốc tháng: <%= appointmentDetail.getRequestedPackage().getMonthMilestone() %> tháng<br>
                        <% } %>
                        <% if (appointmentDetail.getRequestedPackage().getEstimatedDurationHours() != null) { %>
                        Thời gian dự kiến: <%= appointmentDetail.getRequestedPackage().getEstimatedDurationHours() %> giờ<br>
                        <% } %>
                        <% if (appointmentDetail.getRequestedPackage().getBasePrice() != null) { %>
                        Giá gốc: <%= String.format("%,.0f", appointmentDetail.getRequestedPackage().getBasePrice()) %> VND<br>
                        <% } %>
                        <% if (appointmentDetail.getRequestedPackage().getDiscountPercent() != null && appointmentDetail.getRequestedPackage().getDiscountPercent().compareTo(java.math.BigDecimal.ZERO) > 0) { %>
                        Giảm giá: <%= appointmentDetail.getRequestedPackage().getDiscountPercent() %>%<br>
                        <% } %>
                        <% if (appointmentDetail.getRequestedPackage().getFinalPrice() != null) { %>
                        <span class="text-success fw-bold">
                            Giá cuối: <%= String.format("%,.0f", appointmentDetail.getRequestedPackage().getFinalPrice()) %> VND
                        </span>
                        <% } %>
                    </div>
                </div>
                <% } %>

                <% if (appointmentDetail.getCreatedBy() != null) { %>
                <div class="detail-row">
                    <div class="detail-label">
                        <i class="fas fa-user"></i> Người tạo lịch:
                    </div>
                    <div class="detail-value">
                        <strong><%= appointmentDetail.getCreatedBy().getFullName() %></strong><br>
                        <% if (appointmentDetail.getCreatedBy().getEmail() != null) { %>
                        Email: <%= appointmentDetail.getCreatedBy().getEmail() %><br>
                        <% } %>
                        <% if (appointmentDetail.getCreatedBy().getPhone() != null) { %>
                        Điện thoại: <%= appointmentDetail.getCreatedBy().getPhone() %>
                        <% } %>
                    </div>
                </div>
                <% } %>
                
                <% if (appointmentDetail.getCreatedDate() != null) { %>
                <div class="detail-row">
                    <div class="detail-label">
                        <i class="fas fa-clock"></i> Ngày tạo:
                    </div>
                    <div class="detail-value">
                        <%= appointmentDetail.getCreatedDate() %>
                    </div>
                </div>
                <% } %>
            </div>
            <div class="modal-footer">
                <a href="userAppoinmentsHistoryController" class="btn btn-secondary">
                    <i class="fas fa-times"></i> Đóng
                </a>
            </div>
        </div>
    </div>
</div>
<% } %>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>