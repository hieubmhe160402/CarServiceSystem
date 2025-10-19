<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="model.Appointment"%>
<%
    List<Appointment> appointments = (List<Appointment>) request.getAttribute("appointmentList");
    String dateFilter = (String) request.getAttribute("dateFilter");
    String packageFilter = (String) request.getAttribute("packageFilter");
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
            height: 100vh;
        }
        .sidebar {
            width: 260px;
            background: linear-gradient(180deg, #0f2340, #0b1830);
            color: white;
            padding: 28px 18px;
            display: flex;
            flex-direction: column;
            box-shadow: 4px 0 12px rgba(0, 0, 0, 0.1);
        }
        .brand {
            font-weight: 800;
            font-size: 18px;
            letter-spacing: 1px;
            margin-bottom: 22px;
        }
        .nav {
            display: flex;
            flex-direction: column;
            gap: 8px;
        }
        .nav a {
            color: rgba(255, 255, 255, 0.9);
            text-decoration: none;
            padding: 10px 12px;
            border-radius: 10px;
            display: flex;
            align-items: center;
            gap: 12px;
            transition: all 0.3s ease;
        }
        .nav a.active,
        .nav a:hover {
            background: rgba(255, 255, 255, 0.15);
            transform: translateX(4px);
        }
        .nav a i {
            width: 20px;
            text-align: center;
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

            font-weight: 700;
            padding: 18px 16px;
            border: none;
            font-size: 14px;
            text-transform: uppercase;
            letter-spacing: 0.5px;
        }
        .table tbody tr {
            transition: all 0.3s ease;
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
        .table tbody td .mobile-label {
            display: none;
            font-weight: 700;
            color: #6b7280;
            margin-bottom: 5px;
            font-size: 12px;
        }
        @media (max-width: 768px) {
            .table thead {
                display: none;
            }
            .table tbody td {
                display: block;
                text-align: left;
                padding: 10px 16px;
                border: none;
            }
            .table tbody td .mobile-label {
                display: block;
            }
            .table tbody tr {
                display: block;
                margin-bottom: 20px;
                border: 1px solid #e5e7eb;
                border-radius: 12px;
                overflow: hidden;
                background: white;
            }
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
        .table tbody tr {
            animation: fadeIn 0.5s ease;
        }
        @media (max-width: 768px) {
            .app {
                flex-direction: column;
            }
            .sidebar {
                width: 100%;
                padding: 20px;
            }
            .main {
                padding: 20px;
            }
            .section-header h2 {
                font-size: 22px;
            }
            .table-responsive {
                font-size: 12px;
            }
        }
    </style>
</head>
<body>
<div class="app">
    <!-- Sidebar -->
    <div class="sidebar">
        <div class="brand">🚗 CAR MANAGEMENT</div>
        <nav class="nav">
            <a href="userProfileController"><i class="fas fa-user"></i>Profile</a>
            <a href="#"><i class="fas fa-car"></i>Xe của tôi</a>
            <a href="bookingAppoitments"><i class="fas fa-calendar-check"></i>Đặt lịch bảo dưỡng</a>
            <a href="userAppoinmentsHistoryController" class="active"><i class="fas fa-history"></i>Lịch sử</a>
            <a href="#"><i class="fas fa-cog"></i>Cài đặt</a>
            <a href="logout" style="margin-top: auto;"><i class="fas fa-sign-out-alt"></i>Đăng xuất</a>
        </nav>
    </div>

    <!-- Main content -->
    <div class="main">
        <div class="section-header">
            <h2><i class="fas fa-history"></i> Lịch sử lịch hẹn</h2>
        </div>

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
                            <th><i class="fas fa-tools"></i> Dịch vụ yêu cầu</th>
                            <th><i class="fas fa-box"></i> Gói bảo dưỡng</th>
                            <th><i class="fas fa-info-circle"></i> Trạng thái</th>
                            <th><i class="fas fa-sticky-note"></i> Ghi chú</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% if (appointments != null && !appointments.isEmpty()) {
                            for (Appointment ap : appointments) { 
                                String statusClass = "";
                                String statusText = ap.getStatus();
                                
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
                            <td data-label="ID">
                                <div class="mobile-label">AppointmentID:</div>
                                <strong>#<%= ap.getAppointmentId() %></strong>
                            </td>
                            <td data-label="Ngày hẹn">
                                <div class="mobile-label">Ngày hẹn:</div>
                                <i class="fas fa-calendar text-primary"></i> <%= ap.getAppointmentDate() %>
                            </td>
                            <td data-label="Dịch vụ yêu cầu">
                                <div class="mobile-label">Dịch vụ yêu cầu:</div>
                                <%= ap.getRequestedServices() != null ? ap.getRequestedServices() : "N/A" %>
                            </td>
                            <td data-label="Gói bảo dưỡng">
                                <div class="mobile-label">Gói bảo dưỡng:</div>
                                <% if (ap.getRequestedPackage() != null) { %>
                                    <i class="fas fa-box text-success"></i> <%= ap.getRequestedPackage().getName() %>
                                <% } else { %>
                                    <span class="text-muted">N/A</span>
                                <% } %>
                            </td>
                            <td data-label="Trạng thái">
                                <div class="mobile-label">Trạng thái:</div>
                                <span class="status-badge <%= statusClass %>">
                                    <%= statusText != null ? statusText : "N/A" %>
                                </span>
                            </td>
                            <td data-label="Ghi chú">
                                <div class="mobile-label">Ghi chú:</div>
                                <%= ap.getNotes() != null ? ap.getNotes() : "-" %>
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
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>