<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*, model.MaintenancePackage, model.User, model.Car" %>
<%
    User user = (User) session.getAttribute("user");
    List<MaintenancePackage> list = (List<MaintenancePackage>) request.getAttribute("packageList");
    List<Car> carList = (List<Car>) request.getAttribute("carList");
    MaintenancePackage recommended = (MaintenancePackage) request.getAttribute("recommendedPackage");
    MaintenancePackage customPackage = (MaintenancePackage) request.getAttribute("customPackage");
    String selectedBrand = (String) request.getAttribute("selectedBrand");
    Integer currentKm = (Integer) request.getAttribute("currentKm");
    String errorMessage = (String) request.getAttribute("errorMessage");
    String successMessage = (String) request.getAttribute("successMessage");
    Boolean noSuitablePackage = (Boolean) request.getAttribute("noSuitablePackage");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Đặt lịch bảo dưỡng - Car Management</title>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">

    <style>
        /* ===== GIỮ NGUYÊN CSS CŨ ===== */
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
            position: fixed;
            height: 100vh;
            overflow-y: auto;
            z-index: 1000;
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
            margin-left: 260px;
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
        .section-header .btn {
            padding: 10px 20px;
            border-radius: 10px;
            font-weight: 600;
            transition: all 0.3s ease;
            border: 2px solid #d1d5db;
        }
        .section-header .btn:hover {
            background: #0f2340;
            color: white;
            border-color: #0f2340;
            transform: translateY(-2px);
            box-shadow: 0 4px 12px rgba(15, 35, 64, 0.2);
        }
        .alert {
            border-radius: 12px;
            border: none;
            padding: 16px 20px;
            margin-bottom: 25px;
            box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
            animation: slideIn 0.4s ease;
        }
        .alert-danger {
            background: linear-gradient(135deg, #fee2e2 0%, #fecaca 100%);
            color: #991b1b;
        }
        .alert-success {
            background: linear-gradient(135deg, #d1fae5 0%, #a7f3d0 100%);
            color: #065f46;
        }
        .alert-warning {
            background: linear-gradient(135deg, #fef3c7 0%, #fde68a 100%);
            color: #92400e;
        }
        .alert-info {
            background: linear-gradient(135deg, #dbeafe 0%, #bfdbfe 100%);
            color: #1e40af;
        }
        @keyframes slideIn {
            from {
                opacity: 0;
                transform: translateY(-10px);
            }
            to {
                opacity: 1;
                transform: translateY(0);
            }
        }
        .suggest-section {
            background: linear-gradient(135deg, #ffffff 0%, #f9fafb 100%);
            border-radius: 16px;
            padding: 30px;
            margin-bottom: 35px;
            box-shadow: 0 4px 16px rgba(0, 0, 0, 0.06);
            border: 1px solid #e5e7eb;
            position: relative;
            overflow: hidden;
        }
        .suggest-section::before {
            content: '';
            position: absolute;
            top: 0;
            left: 0;
            right: 0;
            height: 4px;
            background: linear-gradient(90deg, #16a34a, #10b981, #16a34a);
        }
        .suggest-section h4 {
            color: #0f2340;
            font-weight: 800;
            margin-bottom: 25px;
            font-size: 20px;
            display: flex;
            align-items: center;
            gap: 10px;
        }
        .suggest-section h4 i {
            color: #16a34a;
            font-size: 24px;
        }
        .suggest-section .form-label {
            color: #374151;
            font-weight: 600;
            margin-bottom: 10px;
            font-size: 15px;
        }
        .suggest-section .form-select,
        .suggest-section .form-control {
            border: 2px solid #e5e7eb;
            border-radius: 10px;
            padding: 12px 16px;
            font-size: 15px;
            transition: all 0.3s ease;
            background-color: white;
        }
        .suggest-section .form-select:focus,
        .suggest-section .form-control:focus {
            border-color: #16a34a;
            box-shadow: 0 0 0 4px rgba(22, 163, 74, 0.1);
            outline: none;
        }
        .btn-suggest {
            background: linear-gradient(135deg, #16a34a 0%, #15803d 100%);
            color: white;
            border: none;
            padding: 12px 28px;
            border-radius: 10px;
            font-weight: 700;
            font-size: 15px;
            transition: all 0.3s ease;
            box-shadow: 0 4px 12px rgba(22, 163, 74, 0.3);
            display: inline-flex;
            align-items: center;
            gap: 8px;
        }
        .btn-suggest:hover {
            background: linear-gradient(135deg, #15803d 0%, #166534 100%);
            transform: translateY(-2px);
            box-shadow: 0 6px 20px rgba(22, 163, 74, 0.4);
        }
        .btn-suggest:disabled {
            background: #9ca3af;
            cursor: not-allowed;
            box-shadow: none;
            transform: none;
        }
        .car-info {
            background: linear-gradient(135deg, #ecfdf5 0%, #d1fae5 100%);
            padding: 20px;
            border-radius: 12px;
            margin-top: 20px;
            border-left: 4px solid #16a34a;
            box-shadow: 0 2px 8px rgba(22, 163, 74, 0.1);
        }
        .car-info p {
            margin: 8px 0;
            color: #065f46;
            font-weight: 600;
        }
        .recommend-card {
            background: linear-gradient(135deg, #ecfdf5 0%, #d1fae5 100%);
            border-left: 6px solid #16a34a;
            border-radius: 16px;
            padding: 28px;
            margin-bottom: 35px;
            box-shadow: 0 6px 20px rgba(22, 163, 74, 0.15);
            position: relative;
            animation: slideIn 0.5s ease;
        }
        .recommend-card::before {
            content: '⭐';
            position: absolute;
            top: -15px;
            right: 30px;
            font-size: 40px;
            animation: pulse 2s infinite;
        }
        @keyframes pulse {
            0%, 100% {
                transform: scale(1);
            }
            50% {
                transform: scale(1.1);
            }
        }
        .recommend-card h4 {
            color: #065f46;
            font-weight: 800;
            font-size: 22px;
            margin-bottom: 15px;
        }
        .recommend-card p {
            color: #047857;
            margin: 8px 0;
            font-weight: 500;
        }
        
        /* Alert cho không có gói phù hợp */
        .no-suitable-alert {
            background: linear-gradient(135deg, #fef3c7 0%, #fde68a 100%);
            border-left: 6px solid #f59e0b;
            border-radius: 16px;
            padding: 28px;
            margin-bottom: 35px;
            box-shadow: 0 6px 20px rgba(245, 158, 11, 0.15);
            animation: slideIn 0.5s ease;
        }
        .no-suitable-alert h4 {
            color: #92400e;
            font-weight: 800;
            font-size: 20px;
            margin-bottom: 15px;
            display: flex;
            align-items: center;
            gap: 10px;
        }
        .no-suitable-alert h4 i {
            color: #f59e0b;
            font-size: 24px;
        }
        .no-suitable-alert p {
            color: #b45309;
            margin: 8px 0;
            font-weight: 500;
        }
        .no-suitable-alert .btn {
            margin-top: 15px;
        }
        
        .package-card {
            background: white;
            border-radius: 16px;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
            overflow: hidden;
            transition: all 0.4s cubic-bezier(0.175, 0.885, 0.32, 1.275);
            display: flex;
            flex-direction: column;
            height: 100%;
            border: 2px solid transparent;
        }
        .package-card:hover {
            transform: translateY(-8px) scale(1.02);
            box-shadow: 0 12px 28px rgba(0, 0, 0, 0.15);
            border-color: #16a34a;
        }
        .package-card img {
            width: 100%;
            height: 220px;
            object-fit: cover;
            transition: transform 0.4s ease;
        }
        .package-card:hover img {
            transform: scale(1.1);
        }
        .package-card .card-body {
            padding: 24px;
            display: flex;
            flex-direction: column;
            flex-grow: 1;
        }
        .package-card .card-body h5 {
            font-weight: 800;
            color: #0f2340;
            margin-bottom: 12px;
            font-size: 19px;
            height: 48px;
            overflow: hidden;
            display: -webkit-box;
            -webkit-line-clamp: 2;
            -webkit-box-orient: vertical;
            line-height: 1.3;
        }
        .package-card p {
            color: #4b5563;
            font-size: 14px;
            margin-bottom: 10px;
            line-height: 1.6;
        }
        .package-card .description {
            height: 66px;
            overflow: hidden;
            display: -webkit-box;
            -webkit-line-clamp: 3;
            -webkit-box-orient: vertical;
            margin-bottom: 15px;
            color: #6b7280;
        }
        .package-card .brand-text {
            height: 44px;
            overflow: hidden;
            display: -webkit-box;
            -webkit-line-clamp: 2;
            -webkit-box-orient: vertical;
            font-weight: 600;
            color: #374151;
        }
        .package-card .price {
            font-size: 22px;
            font-weight: 800;
            background: linear-gradient(135deg, #16a34a, #10b981);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
            margin-top: auto;
            margin-bottom: 18px;
        }
        .col-md-4 {
            display: flex;
            margin-bottom: 30px;
        }
        .btn-detail {
            background: linear-gradient(135deg, #0f2340 0%, #1e3a5f 100%);
            color: white;
            border: none;
            padding: 12px 20px;
            border-radius: 10px;
            font-weight: 700;
            font-size: 15px;
            transition: all 0.3s ease;
            box-shadow: 0 4px 12px rgba(15, 35, 64, 0.3);
            display: inline-flex;
            align-items: center;
            justify-content: center;
            gap: 8px;
        }
        .btn-detail:hover {
            background: linear-gradient(135deg, #1e3a5f 0%, #2d4a7c 100%);
            transform: translateY(-2px);
            box-shadow: 0 6px 20px rgba(15, 35, 64, 0.4);
            color: white;
        }
        .btn-book {
            background: linear-gradient(135deg, #16a34a 0%, #15803d 100%);
            color: white;
            border: none;
            padding: 10px 20px;
            border-radius: 10px;
            font-weight: 700;
            font-size: 14px;
            transition: all 0.3s ease;
            box-shadow: 0 4px 12px rgba(22, 163, 74, 0.3);
            margin-top: 10px;
            width: 100%;
        }
        .btn-book:hover {
            background: linear-gradient(135deg, #15803d 0%, #166534 100%);
            transform: translateY(-2px);
            box-shadow: 0 6px 20px rgba(22, 163, 74, 0.4);
        }
        form[action="bookingAppoitments"] input[type="text"] {
            padding: 12px 16px;
            width: 300px;
            border: 2px solid #e5e7eb;
            border-radius: 10px;
            font-size: 15px;
            transition: all 0.3s ease;
        }
        form[action="bookingAppoitments"] input[type="text"]:focus {
            border-color: #0f2340;
            box-shadow: 0 0 0 4px rgba(15, 35, 64, 0.1);
            outline: none;
        }
        form[action="bookingAppoitments"] button[type="submit"] {
            padding: 12px 24px;
            background: linear-gradient(135deg, #0f2340 0%, #1e3a5f 100%);
            color: white;
            border: none;
            border-radius: 10px;
            font-weight: 700;
            cursor: pointer;
            transition: all 0.3s ease;
            box-shadow: 0 4px 12px rgba(15, 35, 64, 0.3);
        }
        form[action="bookingAppoitments"] button[type="submit"]:hover {
            background: linear-gradient(135deg, #1e3a5f 0%, #2d4a7c 100%);
            transform: translateY(-2px);
            box-shadow: 0 6px 20px rgba(15, 35, 64, 0.4);
        }
        h4.mb-3 {
            color: #0f2340;
            font-weight: 800;
            font-size: 22px;
            margin-bottom: 25px !important;
            display: flex;
            align-items: center;
            gap: 10px;
            padding-bottom: 12px;
            border-bottom: 3px solid #e5e7eb;
        }
        h4.mb-3 i {
            color: #16a34a;
        }
        .modal-content {
            border-radius: 16px;
            border: none;
            box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
        }
        .modal-header {
            border-radius: 16px 16px 0 0;
            padding: 20px 28px;
            background: linear-gradient(135deg, #0f2340 0%, #1e3a5f 100%);
            color: white;
        }
        .modal-header.bg-success {
            background: linear-gradient(135deg, #16a34a 0%, #15803d 100%) !important;
        }
        .modal-body {
            padding: 28px;
        }
        .modal-body img {
            border-radius: 12px;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
        }
        .modal-footer {
            padding: 20px 28px;
            border-top: 2px solid #e5e7eb;
        }
        .modal-footer .btn-success {
            background: linear-gradient(135deg, #16a34a 0%, #15803d 100%);
            border: none;
            padding: 10px 24px;
            border-radius: 10px;
            font-weight: 700;
            box-shadow: 0 4px 12px rgba(22, 163, 74, 0.3);
            transition: all 0.3s ease;
        }
        .modal-footer .btn-success:hover {
            background: linear-gradient(135deg, #15803d 0%, #166534 100%);
            transform: translateY(-2px);
            box-shadow: 0 6px 20px rgba(22, 163, 74, 0.4);
        }
        /* ===== CSS CHO FORM ĐẶT LỊCH TÙY CHỌN ===== */
        .custom-appointment-section {
            background: white;
            border-radius: 16px;
            box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1);
            margin-bottom: 30px;
            overflow: hidden;
        }
        .custom-appointment-header {
            background: linear-gradient(135deg, #f59e0b, #d97706);
            color: white;
            padding: 20px 30px;
            border: none;
        }
        .custom-appointment-header h4 {
            margin: 0;
            font-weight: 700;
            display: flex;
            align-items: center;
            gap: 10px;
        }
        .custom-appointment-body {
            padding: 30px;
        }
        .custom-package-info {
            background: linear-gradient(135deg, #fef3c7, #fde68a);
            border: 2px solid #f59e0b;
            border-radius: 12px;
            padding: 20px;
            margin-bottom: 25px;
        }
        .custom-package-info h5 {
            color: #92400e;
            font-weight: 700;
            margin-bottom: 10px;
            display: flex;
            align-items: center;
            gap: 8px;
        }
        .custom-package-info h6 {
            color: #92400e;
            font-weight: 600;
            margin-bottom: 8px;
            display: flex;
            align-items: center;
            gap: 8px;
        }
        .custom-package-info p {
            color: #a16207;
            margin: 0;
            font-size: 14px;
        }
        
        /* ===== CSS CHO MODAL ĐẶT LỊCH TÙY CHỌN ===== */
        .modal-header.bg-warning {
            background: linear-gradient(135deg, #f59e0b, #d97706) !important;
            color: white !important;
        }
        .modal-header.bg-warning .btn-close {
            filter: invert(1);
        }
        .btn-warning {
            background: linear-gradient(135deg, #f59e0b, #d97706);
            border: none;
            color: white;
            font-weight: 600;
        }
        .btn-warning:hover {
            background: linear-gradient(135deg, #d97706, #b45309);
            color: white;
            transform: translateY(-2px);
            box-shadow: 0 8px 25px rgba(245, 158, 11, 0.3);
        }
        .required {
            color: #dc2626;
        }
        .form-text {
            font-size: 12px;
            color: #6b7280;
            margin-top: 4px;
        }

        @media (max-width: 768px) {
            .sidebar {
                position: relative;
                width: 100%;
                height: auto;
            }
            .main {
                margin-left: 0;
                padding: 20px;
            }
            .section-header h2 {
                font-size: 22px;
            }
            .package-card img {
                height: 180px;
            }
        }
    </style>
</head>
<body>
<div class="app">
    <!-- Sidebar -->
    <jsp:include page="/view/layout/sidebar.jsp"/>

    <!-- Main content -->
    <div class="main">
        <div class="section-header">
            <h2><i class="fas fa-tools"></i> Gói bảo dưỡng</h2>
            <div class="d-flex gap-2">
                <button type="button" class="btn btn-outline-secondary" onclick="window.location.href='bookingAppoitments'" title="Làm mới trang">
                    <i class="fas fa-redo"></i> Làm mới
                </button>
            </div>
        </div>

        <!-- ===== SUCCESS MESSAGE ===== -->
        <% if (successMessage != null) { %>
        <div class="alert alert-success alert-dismissible fade show" role="alert">
            <i class="fas fa-check-circle"></i> <%= successMessage %>
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
        <% } %>

        <!-- ===== BUTTON ĐẶT LỊCH TÙY CHỌN ===== -->
        <% if (customPackage != null) { %>
        <div class="text-center mb-4">
            <button type="button" class="btn btn-warning btn-lg" data-bs-toggle="modal" data-bs-target="#customAppointmentModal">
                <i class="fas fa-tools"></i>
                Đặt lịch sửa chữa tùy chọn
            </button>
            <p class="text-muted mt-2">
                <i class="fas fa-info-circle"></i>
                Mô tả chi tiết các dịch vụ cần sửa chữa, chúng tôi sẽ báo giá cụ thể sau khi kiểm tra xe
            </p>
        </div>
        <% } %>

        <!-- ===== ERROR MESSAGE ===== -->
        <% if (errorMessage != null) { %>
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
            <i class="fas fa-exclamation-circle"></i> <%= errorMessage %>
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
        <% } %>
        
        <% if (carList != null && !carList.isEmpty()) { %>
        <div class="suggest-section">
            <h4><i class="fas fa-magic"></i> Tìm gói phù hợp với xe của bạn</h4>
            <form action="bookingAppoitments" method="get">
                <div class="row">
                    <div class="col-md-6">
                        <label class="form-label"><strong>Chọn xe của bạn:</strong></label>
                        <select name="selectedCarId" class="form-select" id="carSelect">
                            <% for (Car car : carList) { 
                                Integer carKm = car.getCurrentOdometer();
                                String kmDisplay = (carKm != null && carKm > 0) ? carKm + " km" : "Chưa cập nhật";
                            %>
                            <option value="<%= car.getCarId() %>" 
                                    data-brand="<%= car.getBrand() %>" 
                                    data-km="<%= carKm != null ? carKm : 0 %>">
                                <%= car.getBrand() %> - <%= car.getModel() %> 
                                (Số km: <%= kmDisplay %>)
                            </option>
                            <% } %>
                        </select>
                    </div>
                    <div class="col-md-6 d-flex align-items-end gap-2">
                        <button type="submit" name="suggest" value="true" class="btn-suggest">
                            <i class="fas fa-lightbulb"></i> Gợi ý gói phù hợp
                        </button>
                    </div>
                </div>
                
                <% if (selectedBrand != null && currentKm != null) { %>
                <div class="car-info">
                    <p><strong>Xe đã chọn:</strong> <%= selectedBrand %></p>
                    <p><strong>Số km hiện tại:</strong> <%= currentKm %> km</p>
                </div>
                <% } %>
            </form>
        </div>
        <% } else { %>
        <div class="alert alert-warning">
            <i class="fas fa-exclamation-triangle"></i> 
            Bạn chưa có xe nào trong hệ thống. Vui lòng thêm xe trước khi sử dụng tính năng gợi ý.
        </div>
        <% } %>

        <!-- ===== THÔNG BÁO KHÔNG CÓ GÓI PHÙ HỢP ===== -->
        <% if (noSuitablePackage != null && noSuitablePackage) { %>
        <div class="no-suitable-alert">
            <h4><i class="fas fa-info-circle"></i> Không có gói phù hợp</h4>
            <p>Hiện tại chúng tôi chưa có gói bảo dưỡng phù hợp với số km hiện tại của xe <%= selectedBrand %> (Số km: <%= currentKm %>).</p>
            <p><strong>Đề xuất:</strong></p>
            <ul class="mb-0">
                <li>Xem tất cả các gói bảo dưỡng có sẵn bên dưới</li>
                <li>Liên hệ với chúng tôi để được tư vấn gói bảo dưỡng phù hợp</li>
                <li>Sử dụng dịch vụ đặt lịch tùy chọn cho các nhu cầu đặc biệt</li>
            </ul>
            <button type="button" class="btn btn-outline-warning mt-3" onclick="window.location.href='bookingAppoitments'">
                <i class="fas fa-times"></i> Đóng thông báo
            </button>
        </div>
        <% } %>

        <!-- ===== GÓI GỢI Ý HỢP LÝ ===== -->
        <% if (recommended != null) { %>
        <div class="recommend-card">
            <div class="d-flex justify-content-between align-items-start mb-3">
                <h4><i class="fas fa-lightbulb text-success"></i> Gợi ý cho bạn:</h4>
                <button type="button" class="btn btn-sm btn-outline-secondary" onclick="window.location.href='bookingAppoitments'" title="Ẩn gợi ý">
                    <i class="fas fa-times"></i>
                </button>
            </div>
            <p><strong><%= recommended.getName() %></strong> — Mốc <%= recommended.getKilometerMilestone() %> km</p>
            <p><small>Phù hợp với hãng: <%= recommended.getApplicableBrands() %></small></p>
            <button class="btn-detail" data-bs-toggle="modal" data-bs-target="#recommendModal">
                Xem chi tiết gói gợi ý
            </button>
        </div>

        <!-- Modal chi tiết gói gợi ý -->
        <div class="modal fade" id="recommendModal" tabindex="-1" aria-hidden="true">
            <div class="modal-dialog modal-lg modal-dialog-scrollable">
                <div class="modal-content">
                    <div class="modal-header bg-success text-white">
                        <h5 class="modal-title"><i class="fas fa-star"></i> <%= recommended.getName() %></h5>
                        <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body">
                        <img src="<%= recommended.getImage() != null && !recommended.getImage().isEmpty() ? recommended.getImage() : "https://tahico.com/wp-content/uploads/2021/04/goi-bao-duong-tiem-rua-va-cham-soc-xe-o-to-5.webp" %>"
                             class="img-fluid mb-3 rounded"
                             onerror="this.src='https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRBIgu6crRLG7VulmbB_ULqT3viUFF00BbB-A&s'">
                        <p><strong>Mã gói:</strong> <%= recommended.getPackageCode() %></p>
                        <p><strong>Mốc km:</strong> <%= recommended.getKilometerMilestone() %> km</p>
                        <p><strong>Thời gian dự kiến:</strong> <%= recommended.getEstimatedDurationHours() %> giờ</p>
                        <p><strong>Chi tiết:</strong></p>
                        <p><%= recommended.getDescription() %></p>
                        <hr>
                        <p><strong>Giá gốc:</strong> <%= String.format("%,.0f", recommended.getBasePrice()) %> VND</p>
                        <p><strong>Giảm giá:</strong> <%= recommended.getDiscountPercent() %>%</p>
                        <h5 class="text-success"><strong>Giá cuối:</strong> <%= String.format("%,.0f", recommended.getFinalPrice()) %> VND</h5>
                    </div>
                    <div class="modal-footer">
                        <button class="btn btn-success" data-bs-dismiss="modal" data-bs-toggle="modal" data-bs-target="#bookingModal<%= recommended.getPackageId() %>">
                            <i class="fas fa-calendar-check"></i> Đặt lịch ngay
                        </button>
                        <button class="btn btn-secondary" data-bs-dismiss="modal">Đóng</button>
                    </div>
                </div>
            </div>
        </div>

        <!-- Modal đặt lịch cho gói gợi ý -->
        <div class="modal fade" id="bookingModal<%= recommended.getPackageId() %>" tabindex="-1" aria-hidden="true">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header bg-success text-white">
                        <h5 class="modal-title"><i class="fas fa-calendar-plus"></i> Đặt lịch bảo dưỡng</h5>
                        <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
                    </div>
                    <form action="bookingAppoitments" method="post">
                        <div class="modal-body">
                            <input type="hidden" name="packageId" value="<%= recommended.getPackageId() %>">
                            
                            <div class="mb-3">
                                <label class="form-label"><strong>Gói dịch vụ:</strong></label>
                                <input type="text" class="form-control" value="<%= recommended.getName() %>" readonly>
                            </div>

                            <div class="mb-3">
                                <label class="form-label"><strong>Chọn xe: <span class="text-danger">*</span></strong></label>
                                <select name="carId" class="form-select" required>
                                    <option value="">-- Chọn xe của bạn --</option>
                                    <% for (Car car : carList) { %>
                                    <option value="<%= car.getCarId() %>">
                                        <%= car.getBrand() %> - <%= car.getModel() %> (<%= car.getLicensePlate() %>)
                                    </option>
                                    <% } %>
                                </select>
                            </div>

                            <div class="row">
                                <div class="col-md-6 mb-3">
                                    <label class="form-label"><strong>Ngày hẹn: <span class="text-danger">*</span></strong></label>
                                    <input type="date" name="appointmentDate" class="form-control" required min="<%= new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date()) %>">
                                </div>
                                <div class="col-md-6 mb-3">
                                    <label class="form-label"><strong>Giờ hẹn: <span class="text-danger">*</span></strong></label>
                                    <div class="d-flex gap-2 time-picker">
                                        <select class="form-select time-hour" aria-label="Giờ" required></select>
                                        <select class="form-select time-minute" aria-label="Phút" required></select>
                                        <select class="form-select time-ampm" aria-label="Buổi" required></select>
                                        <input type="hidden" name="appointmentTime" class="time-value">
                                    </div>
                                    <small class="text-muted">Giờ làm việc: 08:00 - 17:00</small>
                                </div>
                            </div>

                            <div class="mb-3">
                                <label class="form-label"><strong>Ghi chú thêm:</strong></label>
                                <textarea name="notes" class="form-control" rows="2" placeholder="Ghi chú đặc biệt (nếu có)..."></textarea>
                            </div>

                            <div class="alert alert-info">
                                <i class="fas fa-info-circle"></i> <strong>Lưu ý:</strong> Sau khi đặt lịch, chúng tôi sẽ liên hệ xác nhận trong vòng 24 giờ.
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="submit" class="btn btn-success">
                                <i class="fas fa-check-circle"></i> Xác nhận đặt lịch
                            </button>
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
        <% } %>

        <!-- ===== DANH SÁCH TẤT CẢ GÓI ===== -->
        <h4 class="mb-3"><i class="fas fa-list"></i> Tất cả gói bảo dưỡng</h4>
        
        <form action="bookingAppoitments" method="get" class="mb-3">
            <input type="text" name="searchBrand" placeholder="Nhập hãng xe (VD: Toyota, Honda...)" 
                   value="<%= request.getAttribute("searchBrand") != null ? request.getAttribute("searchBrand") : "" %>">
            <button type="submit">Tìm kiếm</button>
        </form>
        
        <div class="row">
            <%
                if (list != null && !list.isEmpty()) {
                    for (MaintenancePackage pkg : list) {
                        // Ẩn gói PKG-EMPTY khỏi danh sách combo
                        if (pkg.getPackageCode() != null && pkg.getPackageCode().equals("PKG-EMPTY")) {
                            continue;
                        }
            %>
            <div class="col-md-4 mb-4">
                <div class="package-card">
                    <img src="<%= pkg.getImage() != null && !pkg.getImage().isEmpty() ? pkg.getImage() : "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRBIgu6crRLG7VulmbB_ULqT3viUFF00BbB-A&s" %>" 
                         alt="<%= pkg.getName() %>"
                         onerror="this.src='https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRBIgu6crRLG7VulmbB_ULqT3viUFF00BbB-A&s'">
                    <div class="card-body">
                        <h5><%= pkg.getName() %></h5>
                        <p class="description"><%= pkg.getDescription() %></p>
                        <p class="brand-text"><strong>Áp dụng cho:</strong> <%= pkg.getApplicableBrands() %></p>
                        <p class="price"><%= String.format("%,.0f", pkg.getFinalPrice()) %> VND</p>
                        <button class="btn-detail w-100" data-bs-toggle="modal" data-bs-target="#detailModal<%= pkg.getPackageId() %>">
                            <i class="fas fa-info-circle"></i> Xem chi tiết
                        </button>
                        <% if (carList != null && !carList.isEmpty()) { %>
                        <button class="btn-book" data-bs-toggle="modal" data-bs-target="#bookingModal<%= pkg.getPackageId() %>">
                            <i class="fas fa-calendar-check"></i> Đặt lịch ngay
                        </button>
                        <% } %>
                    </div>
                </div>
            </div>

            <!-- Modal chi tiết -->
            <div class="modal fade" id="detailModal<%= pkg.getPackageId() %>" tabindex="-1" aria-hidden="true">
                <div class="modal-dialog modal-lg modal-dialog-scrollable">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title"><i class="fas fa-box"></i> <%= pkg.getName() %></h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                        </div>
                        <div class="modal-body">
                            <img src="<%= pkg.getImage() != null && !pkg.getImage().isEmpty() ? pkg.getImage() : "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRBIgu6crRLG7VulmbB_ULqT3viUFF00BbB-A&s" %>"
                                 class="img-fluid mb-3 rounded"
                                 onerror="this.src='https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRBIgu6crRLG7VulmbB_ULqT3viUFF00BbB-A&s'">
                            <p><strong>Mã gói:</strong> <%= pkg.getPackageCode() %></p>
                            <p><strong>Khoảng km:</strong> <%= pkg.getKilometerMilestone() != null ? pkg.getKilometerMilestone() : 0 %> km</p>
                            <p><strong>Mốc thời gian:</strong> <%= pkg.getMonthMilestone() != null ? pkg.getMonthMilestone() : 0 %> tháng</p>
                            <p><strong>Thời gian dự kiến:</strong> <%= pkg.getEstimatedDurationHours() != null ? pkg.getEstimatedDurationHours().toString() : "0" %> giờ</p>
                            <p><strong>Chi tiết:</strong></p>
                            <p><%= pkg.getDescription() %></p>
                            <hr>
                            <p><strong>Giá gốc:</strong> <%= String.format("%,.0f", pkg.getBasePrice()) %> VND</p>
                            <p><strong>Giảm giá:</strong> <%= pkg.getDiscountPercent() %>%</p>
                            <h5 class="text-success"><strong>Giá cuối:</strong> <%= String.format("%,.0f", pkg.getFinalPrice()) %> VND</h5>
                        </div>
                        <div class="modal-footer">
                            <% if (carList != null && !carList.isEmpty()) { %>
                            <button class="btn btn-success" data-bs-dismiss="modal" data-bs-toggle="modal" data-bs-target="#bookingModal<%= pkg.getPackageId() %>">
                                <i class="fas fa-calendar-check"></i> Đặt lịch ngay
                            </button>
                            <% } %>
                            <button class="btn btn-secondary" data-bs-dismiss="modal">Đóng</button>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Modal đặt lịch -->
            <% if (carList != null && !carList.isEmpty()) { %>
            <div class="modal fade" id="bookingModal<%= pkg.getPackageId() %>" tabindex="-1" aria-hidden="true">
                <div class="modal-dialog modal-lg">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title"><i class="fas fa-calendar-plus"></i> Đặt lịch bảo dưỡng</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                        </div>
                        <form action="bookingAppoitments" method="post">
                            <div class="modal-body">
                                <input type="hidden" name="packageId" value="<%= pkg.getPackageId() %>">
                                
                                <div class="mb-3">
                                    <label class="form-label"><strong>Gói dịch vụ:</strong></label>
                                    <input type="text" class="form-control" value="<%= pkg.getName() %>" readonly>
                                </div>

                                <div class="mb-3">
                                    <label class="form-label"><strong>Chọn xe: <span class="text-danger">*</span></strong></label>
                                    <select name="carId" class="form-select" required>
                                        <option value="">-- Chọn xe của bạn --</option>
                                        <% for (Car car : carList) { %>
                                        <option value="<%= car.getCarId() %>">
                                            <%= car.getBrand() %> - <%= car.getModel() %> (<%= car.getLicensePlate() %>)
                                        </option>
                                        <% } %>
                                    </select>
                                </div>

                                <div class="row">
                                    <div class="col-md-6 mb-3">
                                        <label class="form-label"><strong>Ngày hẹn: <span class="text-danger">*</span></strong></label>
                                        <input type="date" name="appointmentDate" class="form-control" required min="<%= new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date()) %>">
                                    </div>
                                    <div class="col-md-6 mb-3">
                                        <label class="form-label"><strong>Giờ hẹn: <span class="text-danger">*</span></strong></label>
                                        <div class="d-flex gap-2 time-picker">
                                            <select class="form-select time-hour" aria-label="Giờ" required></select>
                                            <select class="form-select time-minute" aria-label="Phút" required></select>
                                            <select class="form-select time-ampm" aria-label="Buổi" required></select>
                                            <input type="hidden" name="appointmentTime" class="time-value">
                                        </div>
                                        <small class="text-muted">Giờ làm việc: 08:00 - 17:00</small>
                                    </div>
                                </div>

                                <div class="mb-3">
                                    <label class="form-label"><strong>Ghi chú thêm:</strong></label>
                                    <textarea name="notes" class="form-control" rows="2" placeholder="Ghi chú đặc biệt (nếu có)..."></textarea>
                                </div>

                                <div class="alert alert-info">
                                    <i class="fas fa-info-circle"></i> <strong>Lưu ý:</strong> Sau khi đặt lịch, chúng tôi sẽ liên hệ xác nhận trong vòng 24 giờ.
                                </div>
                            </div>
                            <div class="modal-footer">
                                <button type="submit" class="btn btn-success">
                                    <i class="fas fa-check-circle"></i> Xác nhận đặt lịch
                                </button>
                                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
            <% } %>
            <%
                    }
                } else {
            %>
            <p class="text-center text-muted">Không có gói bảo dưỡng nào được hiển thị.</p>
            <% } %>
        </div>
    </div>

    <!-- ===== MODAL ĐẶT LỊCH TÙY CHỌN ===== -->
    <% if (customPackage != null) { %>
    <div class="modal fade" id="customAppointmentModal" tabindex="-1" aria-hidden="true">
        <div class="modal-dialog modal-lg">
            <div class="modal-content">
                <div class="modal-header bg-warning text-dark">
                    <h5 class="modal-title">
                        <i class="fas fa-tools"></i> 
                        Đặt lịch sửa chữa tùy chọn
                    </h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <form action="bookingAppoitments" method="post">
                    <div class="modal-body">
                        <input type="hidden" name="appointmentType" value="custom">
                        
                        <!-- Custom Package Info -->
                        <div class="custom-package-info mb-4">
                            <h6 class="mb-2">
                                <i class="fas fa-info-circle"></i>
                                <strong><%= customPackage.getName() %></strong>
                            </h6>
                            <p class="mb-0 text-muted">
                                <%= customPackage.getDescription() %>
                            </p>
                        </div>
                        
                        <div class="row">
                            <!-- Chọn xe -->
                            <div class="col-md-6 mb-3">
                                <label class="form-label">
                                    Chọn xe <span class="required">*</span>
                                </label>
                                <select name="carId" class="form-select" required>
                                    <option value="">-- Chọn xe của bạn --</option>
                                    <% if (carList != null) { %>
                                        <% for (Car car : carList) { %>
                                        <option value="<%= car.getCarId() %>">
                                            <%= car.getBrand() %> - <%= car.getModel() %> 
                                            (<%= car.getLicensePlate() %>)
                                        </option>
                                        <% } %>
                                    <% } %>
                                </select>
                            </div>

                            <!-- Ngày hẹn -->
                            <div class="col-md-6 mb-3">
                                <label class="form-label">
                                    Ngày hẹn <span class="required">*</span>
                                </label>
                                <input type="date" name="appointmentDate" class="form-control" required
                                       min="<%= java.time.LocalDate.now().toString() %>">
                            </div>
                        </div>

                        <div class="row">
                            <!-- Giờ hẹn -->
                            <div class="col-md-6 mb-3">
                                <label class="form-label">
                                    Giờ hẹn <span class="required">*</span>
                                </label>
                                <div class="d-flex gap-2 time-picker">
                                    <select class="form-select time-hour" aria-label="Giờ" required></select>
                                    <select class="form-select time-minute" aria-label="Phút" required></select>
                                    <select class="form-select time-ampm" aria-label="Buổi" required></select>
                                    <input type="hidden" name="appointmentTime" class="time-value">
                                </div>
                            </div>
                        </div>

                        <!-- Ghi chú thêm -->
                        <div class="mb-3">
                            <label class="form-label">Ghi chú thêm</label>
                            <textarea name="notes" class="form-control" rows="3"
                                      placeholder="Thêm thông tin khác về tình trạng xe hoặc yêu cầu đặc biệt..."></textarea>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="submit" class="btn btn-warning">
                            <i class="fas fa-calendar-check"></i>
                            Đặt lịch tùy chọn
                        </button>
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">
                            Hủy
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>
    <% } %>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script>
    // Auto-dismiss alerts after 5 seconds
    setTimeout(function() {
        var alerts = document.querySelectorAll('.alert');
        alerts.forEach(function(alert) {
            var bsAlert = new bootstrap.Alert(alert);
            bsAlert.close();
        });
    }, 5000);

    // Auto-focus vào trường đầu tiên khi modal hiển thị
    document.addEventListener('DOMContentLoaded', function() {
        const customModal = document.getElementById('customAppointmentModal');
        if (customModal) {
            customModal.addEventListener('shown.bs.modal', function() {
                const firstInput = customModal.querySelector('select[name="carId"]');
                if (firstInput) {
                    firstInput.focus();
                }
            });
        }
    });

    // Reset form khi modal đóng
    document.addEventListener('DOMContentLoaded', function() {
        const customModal = document.getElementById('customAppointmentModal');
        if (customModal) {
            customModal.addEventListener('hidden.bs.modal', function() {
                const form = customModal.querySelector('form');
                if (form) {
                    form.reset();
                }
            });
        }
    });

    // ===== TIME PICKER (HH, mm, SA/CH) for all .time-picker blocks =====
    document.addEventListener('DOMContentLoaded', function() {
        function initTimePicker(container) {
            if (!container) return;
            const hourSel = container.querySelector('.time-hour');
            const minuteSel = container.querySelector('.time-minute');
            const ampmSel = container.querySelector('.time-ampm');
            const hiddenValue = container.querySelector('.time-value');

            if (!hourSel || !minuteSel || !ampmSel || !hiddenValue) return;

            // Populate hours 01..12
            ['','01','02','03','04','05','06','07','08','09','10','11','12'].forEach((h, idx) => {
                const opt = document.createElement('option');
                opt.value = h;
                opt.textContent = idx === 0 ? '--' : h;
                hourSel.appendChild(opt);
            });
            // Populate minutes 00..59
            const minuteOptions = [''];
            for (let i = 0; i < 60; i++) minuteOptions.push(String(i).padStart(2,'0'));
            minuteOptions.forEach((m, idx) => {
                const opt = document.createElement('option');
                opt.value = m;
                opt.textContent = idx === 0 ? '--' : m;
                minuteSel.appendChild(opt);
            });
            // Populate AM/PM (SA/CH)
            [['','--'], ['AM','SA'], ['PM','CH']].forEach(pair => {
                const opt = document.createElement('option');
                opt.value = pair[0];
                opt.textContent = pair[1];
                ampmSel.appendChild(opt);
            });

            function to24h(hh12, mm, ampm) {
                if (!hh12 || !mm || !ampm) return '';
                let h = parseInt(hh12, 10);
                if (ampm === 'AM') {
                    if (h === 12) h = 0;
                } else if (ampm === 'PM') {
                    if (h !== 12) h += 12;
                }
                return String(h).padStart(2,'0') + ':' + mm;
            }

            function enforceBusinessHours() {
                // Business hours 08:00 - 17:00
                const v = to24h(hourSel.value, minuteSel.value, ampmSel.value);
                if (!v) { hiddenValue.value = ''; return; }
                // Disable invalid combinations by snapping
                const [H,M] = v.split(':').map(Number);
                let total = H*60 + M;
                const min = 8*60, max = 17*60; // inclusive endpoints
                if (total < min) total = min;
                if (total > max) total = max;
                const newH = Math.floor(total/60);
                const newM = total%60;
                // Reflect back to controls
                const newAmpm = newH < 12 ? 'AM' : 'PM';
                let h12 = newH % 12; if (h12 === 0) h12 = 12;
                hourSel.value = String(h12).padStart(2,'0');
                minuteSel.value = String(newM).padStart(2,'0');
                ampmSel.value = newAmpm;
                hiddenValue.value = String(newH).padStart(2,'0') + ':' + String(newM).padStart(2,'0');
            }

            [hourSel, minuteSel, ampmSel].forEach(sel => sel.addEventListener('change', enforceBusinessHours));
        }

        // Initialize for all visible time pickers (both combo and custom modal)
        document.querySelectorAll('.time-picker').forEach(initTimePicker);
    });
</script>
</body>
</html>