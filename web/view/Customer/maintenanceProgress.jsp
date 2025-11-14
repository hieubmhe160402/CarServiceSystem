<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Theo dõi tiến độ sửa chữa</title>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        
        body {
            font-family: 'Inter', 'Segoe UI', Roboto, Arial, sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
        }
        
        .app {
            display: flex;
            height: 100vh;
        }
        
        /* Sidebar Styles - Giống trang bookingAppoitments */
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
        
        /* Main Container - Adjusted for sidebar */
        .container {
            flex: 1;
            margin-left: 260px;
            padding: 40px;
            overflow-y: auto;
        }
        
        /* Detail View Styles */
        .detail-view {
            display: none;
        }
        
        .detail-view.active {
            display: block;
        }
        
        .list-view.hidden {
            display: none;
        }
        
        .back-btn {
            display: inline-flex;
            align-items: center;
            gap: 8px;
            padding: 12px 24px;
            background: white;
            color: #667eea;
            text-decoration: none;
            border-radius: 8px;
            font-weight: 600;
            margin-bottom: 20px;
            transition: all 0.3s;
            box-shadow: 0 4px 15px rgba(0,0,0,0.1);
            border: none;
            cursor: pointer;
        }
        
        .back-btn:hover {
            transform: translateX(-5px);
            box-shadow: 0 6px 20px rgba(0,0,0,0.15);
        }
        
        .header {
            background: white;
            padding: 30px;
            border-radius: 15px;
            box-shadow: 0 10px 30px rgba(0,0,0,0.2);
            margin-bottom: 30px;
        }
        
        .header h1 {
            color: #333;
            font-size: 32px;
            margin-bottom: 20px;
        }
        
        .search-box {
            display: flex;
            gap: 10px;
            margin-top: 20px;
        }
        
        .search-box input {
            flex: 1;
            padding: 12px 20px;
            border: 2px solid #ddd;
            border-radius: 8px;
            font-size: 16px;
            transition: all 0.3s;
        }
        
        .search-box input:focus {
            outline: none;
            border-color: #667eea;
            box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
        }
        
        .btn {
            padding: 12px 30px;
            border: none;
            border-radius: 8px;
            font-size: 16px;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.3s;
            text-decoration: none;
            display: inline-block;
        }
        
        .btn-primary {
            background: #667eea;
            color: white;
        }
        
        .btn-primary:hover {
            background: #5568d3;
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(102, 126, 234, 0.4);
        }
        
        .btn-secondary {
            background: #6b7280;
            color: white;
        }
        
        .btn-secondary:hover {
            background: #4b5563;
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(107, 114, 128, 0.4);
        }
        
        .btn i {
            margin-right: 5px;
        }
        
        .maintenance-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(400px, 1fr));
            gap: 25px;
            margin-top: 20px;
        }
        
        .maintenance-card {
            background: white;
            border-radius: 15px;
            padding: 25px;
            box-shadow: 0 10px 30px rgba(0,0,0,0.1);
            transition: all 0.3s;
            cursor: pointer;
        }
        
        .maintenance-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 15px 40px rgba(0,0,0,0.2);
        }
        
        .card-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 20px;
            padding-bottom: 15px;
            border-bottom: 2px solid #f0f0f0;
        }
        
        .license-plate {
            font-size: 24px;
            font-weight: bold;
            color: #333;
        }
        
        .car-info {
            color: #666;
            font-size: 14px;
            margin-top: 5px;
        }
        
        .status-badge {
            padding: 8px 16px;
            border-radius: 20px;
            font-size: 13px;
            font-weight: 600;
            text-transform: uppercase;
        }
        
        .status-confirmed {
            background: #e3f2fd;
            color: #1976d2;
        }
        
        .status-processing {
            background: #fff3e0;
            color: #f57c00;
        }
        
        .status-completed {
            background: #e8f5e9;
            color: #388e3c;
        }
        
        .status-cancelled {
            background: #ffebee;
            color: #d32f2f;
        }
        
        .progress-timeline {
            margin: 25px 0;
            position: relative;
        }
        
        .timeline-steps {
            display: flex;
            justify-content: space-between;
            position: relative;
        }
        
        .timeline-line {
            position: absolute;
            top: 20px;
            left: 0;
            right: 0;
            height: 4px;
            background: #e0e0e0;
            z-index: 1;
        }
        
        .timeline-line-progress {
            position: absolute;
            top: 0;
            left: 0;
            height: 100%;
            background: linear-gradient(90deg, #667eea 0%, #764ba2 100%);
            transition: width 0.5s ease;
        }
        
        .timeline-step {
            display: flex;
            flex-direction: column;
            align-items: center;
            position: relative;
            z-index: 2;
            flex: 1;
        }
        
        .step-circle {
            width: 40px;
            height: 40px;
            border-radius: 50%;
            background: #e0e0e0;
            display: flex;
            align-items: center;
            justify-content: center;
            font-weight: bold;
            color: white;
            margin-bottom: 8px;
            transition: all 0.3s;
        }
        
        .step-circle.active {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            box-shadow: 0 5px 15px rgba(102, 126, 234, 0.4);
            transform: scale(1.1);
        }
        
        .step-label {
            font-size: 12px;
            color: #666;
            text-align: center;
            font-weight: 600;
        }
        
        .step-label.active {
            color: #667eea;
        }
        
        .card-info {
            margin: 20px 0;
        }
        
        .info-row {
            display: flex;
            justify-content: space-between;
            padding: 8px 0;
            border-bottom: 1px solid #f0f0f0;
        }
        
        .info-label {
            color: #666;
            font-size: 14px;
        }
        
        .info-value {
            color: #333;
            font-weight: 600;
            font-size: 14px;
        }
        
        .amount {
            font-size: 20px;
            color: #667eea;
            font-weight: bold;
            margin-top: 15px;
            text-align: right;
        }
        
        .no-data {
            text-align: center;
            padding: 60px 20px;
            background: white;
            border-radius: 15px;
            box-shadow: 0 10px 30px rgba(0,0,0,0.1);
        }
        
        .no-data-icon {
            font-size: 64px;
            color: #ddd;
            margin-bottom: 20px;
        }
        
        .no-data-text {
            color: #666;
            font-size: 18px;
        }
        
        /* Detail Page Styles */
        .detail-card {
            background: white;
            border-radius: 15px;
            padding: 30px;
            box-shadow: 0 10px 30px rgba(0,0,0,0.2);
            margin-bottom: 25px;
        }
        
        .card-title {
            font-size: 24px;
            color: #333;
            margin-bottom: 20px;
            padding-bottom: 15px;
            border-bottom: 3px solid #667eea;
            display: flex;
            align-items: center;
            gap: 10px;
        }
        
        .progress-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 30px;
        }
        
        .car-title {
            font-size: 32px;
            font-weight: bold;
            color: #333;
        }
        
        .step-date {
            font-size: 12px;
            color: #999;
            margin-top: 5px;
        }
        
        .info-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
            gap: 20px;
            margin: 20px 0;
        }
        
        .info-item {
            padding: 15px;
            background: #f8f9fa;
            border-radius: 10px;
            border-left: 4px solid #667eea;
        }
        
        .info-item .info-label {
            color: #666;
            font-size: 13px;
            margin-bottom: 5px;
            text-transform: uppercase;
            letter-spacing: 0.5px;
        }
        
        .info-item .info-value {
            color: #333;
            font-size: 16px;
            font-weight: 600;
        }
        
        .table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }
        
        .table th {
            background: #667eea;
            color: white;
            padding: 15px;
            text-align: left;
            font-weight: 600;
            text-transform: uppercase;
            font-size: 13px;
        }
        
        .table td {
            padding: 15px;
            border-bottom: 1px solid #e0e0e0;
        }
        
        .table tbody tr:hover {
            background: #f8f9fa;
        }
        
        .badge {
            padding: 5px 12px;
            border-radius: 12px;
            font-size: 12px;
            font-weight: 600;
        }
        
        .badge-service {
            background: #e3f2fd;
            color: #1976d2;
        }
        
        .badge-part {
            background: #f3e5f5;
            color: #7b1fa2;
        }
        
        .badge-package {
            background: #e8f5e9;
            color: #388e3c;
        }
        
        .summary {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 25px;
            border-radius: 15px;
            margin-top: 30px;
        }
        
        .summary-row {
            display: flex;
            justify-content: space-between;
            padding: 10px 0;
            font-size: 16px;
        }
        
        .summary-row.total {
            border-top: 2px solid rgba(255,255,255,0.3);
            margin-top: 10px;
            padding-top: 15px;
            font-size: 24px;
            font-weight: bold;
        }
        
        .empty-message {
            text-align: center;
            padding: 40px;
            color: #999;
            font-style: italic;
        }
        
        .notes-box {
            background: #fff9e6;
            border-left: 4px solid #ffc107;
            padding: 15px;
            border-radius: 8px;
            margin-top: 20px;
        }
        
        .notes-label {
            font-weight: 600;
            color: #f57c00;
            margin-bottom: 8px;
        }
        
        .notes-content {
            color: #666;
            line-height: 1.6;
        }
        
        @media (max-width: 768px) {
            .sidebar {
                position: relative;
                width: 100%;
                height: auto;
            }
            .container {
                margin-left: 0;
                padding: 20px;
            }
            .maintenance-grid {
                grid-template-columns: 1fr;
            }
        }
    </style>
</head>
<body>
<div class="app">
    <!-- Sidebar -->
    <jsp:include page="/view/layout/sidebar.jsp"/>
    
    <!-- Main content -->
    <div class="container">
        <!-- LIST VIEW -->
        <div class="list-view <c:if test='${not empty maintenance}'>hidden</c:if>">
            <div class="header">
                <h1>🚗 Theo dõi tiến độ sửa chữa của tôi</h1>
                <p style="color: #666; margin-top: 10px;">Xin chào, <strong>${sessionScope.user.fullName}</strong>!</p>
                <form action="${pageContext.request.contextPath}/maintenanceProgress" method="get" class="search-box">
                    <input type="hidden" name="action" value="search">
                    <input type="text" name="licensePlate" placeholder="Tìm kiếm theo biển số xe của bạn..." 
                           value="${searchKeyword}">
                    <button type="submit" class="btn btn-primary">
                        <i class="fas fa-search"></i> Tìm kiếm
                    </button>
                    <c:if test="${not empty searchKeyword}">
                        <button type="button" class="btn btn-secondary" onclick="window.location.href='${pageContext.request.contextPath}/maintenanceProgress'">
                            <i class="fas fa-redo"></i> Làm mới
                        </button>
                    </c:if>
                </form>
            </div>
            
            <c:choose>
                <c:when test="${empty maintenanceList}">
                    <div class="no-data">
                        <div class="no-data-icon">📋</div>
                        <div class="no-data-text">
                            <c:choose>
                                <c:when test="${not empty searchKeyword}">
                                    Không tìm thấy xe nào với biển số "${searchKeyword}"
                                </c:when>
                                <c:otherwise>
                                    Bạn chưa có đơn bảo dưỡng nào được xác nhận
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="maintenance-grid">
                        <c:forEach items="${maintenanceList}" var="item">
                            <div class="maintenance-card" 
                                 onclick="window.location.href='${pageContext.request.contextPath}/maintenanceProgress?action=detail&id=${item.maintenanceId}'">
                                
                                <div class="card-header">
                                    <div>
                                        <div class="license-plate">${item.car.licensePlate}</div>
                                        <div class="car-info">${item.car.brand} ${item.car.model}</div>
                                    </div>
                                    <span class="status-badge status-${item.status.toLowerCase()}">
                                        <c:choose>
                                            <c:when test="${item.status == 'PROCESSING'}">Đang xử lý</c:when>
                                            <c:when test="${item.status == 'COMPLETED'}">Hoàn thành</c:when>
                                            <c:when test="${item.status == 'CANCELLED'}">Đã hủy</c:when>
                                            <c:otherwise>${item.status}</c:otherwise>
                                        </c:choose>
                                    </span>
                                </div>
                                
                                <div class="progress-timeline">
                                    <div class="timeline-steps">
                                        <div class="timeline-line">
                                            <div class="timeline-line-progress" style="width: 
                                                <c:choose>
                                                    <c:when test="${item.status == 'PROCESSING'}">50%</c:when>
                                                    <c:when test="${item.status == 'COMPLETED'}">100%</c:when>
                                                    <c:when test="${item.status == 'CANCELLED'}">100%</c:when>
                                                    <c:otherwise>0%</c:otherwise>
                                                </c:choose>
                                            "></div>
                                        </div>
                                        
                                        <div class="timeline-step">
                                            <div class="step-circle active">✓</div>
                                            <div class="step-label active">Đã xác nhận</div>
                                        </div>
                                        
                                        <div class="timeline-step">
                                            <div class="step-circle ${item.status == 'PROCESSING' || item.status == 'COMPLETED' ? 'active' : ''}">
                                                ${item.status == 'PROCESSING' || item.status == 'COMPLETED' ? '✓' : '2'}
                                            </div>
                                            <div class="step-label ${item.status == 'PROCESSING' || item.status == 'COMPLETED' ? 'active' : ''}">
                                                Đang xử lý
                                            </div>
                                        </div>
                                        
                                        <div class="timeline-step">
                                            <div class="step-circle ${item.status == 'COMPLETED' ? 'active' : ''}">
                                                ${item.status == 'COMPLETED' ? '✓' : '3'}
                                            </div>
                                            <div class="step-label ${item.status == 'COMPLETED' ? 'active' : ''}">
                                                Hoàn thành
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                
                                <div class="card-info">
                                    <div class="info-row">
                                        <span class="info-label">Ngày đặt lịch:</span>
                                        <span class="info-value">${item.appointment.appointmentDate}</span>
                                    </div>
                                    <div class="info-row">
                                        <span class="info-label">Ngày bảo dưỡng:</span>
                                        <span class="info-value">${item.maintenanceDate}</span>
                                    </div>
                                    <c:if test="${not empty item.assignedTechnician}">
                                        <div class="info-row">
                                            <span class="info-label">Kỹ thuật viên:</span>
                                            <span class="info-value">${item.assignedTechnician.fullName}</span>
                                        </div>
                                    </c:if>
                                    <c:if test="${not empty item.appointment.requestedPackage}">
                                        <div class="info-row">
                                            <span class="info-label">Gói bảo dưỡng:</span>
                                            <span class="info-value">${item.appointment.requestedPackage.name}</span>
                                        </div>
                                    </c:if>
                                </div>
                                
                                <div class="amount">
                                    <fmt:formatNumber value="${item.finalAmount}" type="number" maxFractionDigits="0" groupingUsed="true" />₫
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
        
        <!-- DETAIL VIEW -->
        <c:if test="${not empty maintenance}">
            <div class="detail-view active">
                <button onclick="window.location.href='${pageContext.request.contextPath}/maintenanceProgress'" class="back-btn">
                    ← Quay lại danh sách
                </button>
                
                <div class="detail-card">
                    <div class="progress-header">
                        <div>
                            <div class="car-title">${maintenance.car.licensePlate}</div>
                            <div style="color: #666; margin-top: 5px;">
                                ${maintenance.car.brand} ${maintenance.car.model} | Số KM: <fmt:formatNumber value="${maintenance.odometer}" groupingUsed="true" /> km
                            </div>
                        </div>
                        <span class="status-badge status-${maintenance.status.toLowerCase()}">
                            <c:choose>
                                <c:when test="${maintenance.status == 'PROCESSING'}">Đang xử lý</c:when>
                                <c:when test="${maintenance.status == 'COMPLETED'}">Hoàn thành</c:when>
                                <c:when test="${maintenance.status == 'CANCELLED'}">Đã hủy</c:when>
                                <c:otherwise>${maintenance.status}</c:otherwise>
                            </c:choose>
                        </span>
                    </div>
                    
                    <div class="progress-timeline">
                        <div class="timeline-steps">
                            <div class="timeline-line">
                                <div class="timeline-line-progress" style="width: 
                                    <c:choose>
                                        <c:when test="${maintenance.status == 'PROCESSING'}">50%</c:when>
                                        <c:when test="${maintenance.status == 'COMPLETED'}">100%</c:when>
                                        <c:when test="${maintenance.status == 'CANCELLED'}">100%</c:when>
                                        <c:otherwise>0%</c:otherwise>
                                    </c:choose>
                                "></div>
                            </div>
                            
                            <div class="timeline-step">
                                <div class="step-circle active">✓</div>
                                <div class="step-label active">Đã xác nhận</div>
                                <div class="step-date">${maintenance.appointment.appointmentDate}</div>
                            </div>
                            
                            <div class="timeline-step">
                                <div class="step-circle ${maintenance.status == 'PROCESSING' || maintenance.status == 'COMPLETED' ? 'active' : ''}">
                                    ${maintenance.status == 'PROCESSING' || maintenance.status == 'COMPLETED' ? '✓' : '⚙'}
                                </div>
                                <div class="step-label ${maintenance.status == 'PROCESSING' || maintenance.status == 'COMPLETED' ? 'active' : ''}">
                                    Đang xử lý
                                </div>
                                <c:if test="${not empty maintenance.createdDate}">
                                    <div class="step-date">${maintenance.createdDate}</div>
                                </c:if>
                            </div>
                            
                            <div class="timeline-step">
                                <div class="step-circle ${maintenance.status == 'COMPLETED' ? 'active' : ''}">
                                    ${maintenance.status == 'COMPLETED' ? '✓' : '🏁'}
                                </div>
                                <div class="step-label ${maintenance.status == 'COMPLETED' ? 'active' : ''}">
                                    Hoàn thành
                                </div>
                                <c:if test="${not empty maintenance.completedDate}">
                                    <div class="step-date">${maintenance.completedDate}</div>
                                </c:if>
                            </div>
                        </div>
                    </div>
                </div>
                
                <div class="detail-card">
                    <div class="card-title">📋 Thông tin chi tiết</div>
                    
                    <div class="info-grid">
                        <div class="info-item">
                            <div class="info-label">Chủ xe</div>
                            <div class="info-value">${maintenance.car.owner.fullName}</div>
                            <div style="font-size: 13px; color: #999; margin-top: 5px;">
                                📞 ${maintenance.car.owner.phone}
                            </div>
                        </div>
                        
                        <div class="info-item">
                            <div class="info-label">Người đặt lịch</div>
                            <div class="info-value">${maintenance.appointment.createdBy.fullName}</div>
                            <div style="font-size: 13px; color: #999; margin-top: 5px;">
                                📞 ${maintenance.appointment.createdBy.phone}
                            </div>
                        </div>
                        
                        <c:if test="${not empty maintenance.assignedTechnician}">
                            <div class="info-item">
                                <div class="info-label">Kỹ thuật viên</div>
                                <div class="info-value">${maintenance.assignedTechnician.fullName}</div>
                            </div>
                        </c:if>
                        
                        <c:if test="${not empty maintenance.appointment.requestedPackage}">
                            <div class="info-item">
                                <div class="info-label">Gói bảo dưỡng</div>
                                <div class="info-value">${maintenance.appointment.requestedPackage.name}</div>
                            </div>
                        </c:if>
                    </div>
                    
                    <c:if test="${not empty maintenance.notes}">
                        <div class="notes-box">
                            <div class="notes-label">📝 Ghi chú:</div>
                            <div class="notes-content">${maintenance.notes}</div>
                        </div>
                    </c:if>
                </div>
                
                
<!-- Tính tổng tiền dịch vụ KHÔNG từ gói (dịch vụ riêng) -->
<c:set var="serviceTotalAmount" value="0" />
<c:set var="serviceFromPackageAmount" value="0" />
<c:forEach items="${serviceDetails}" var="service">
    <c:choose>
        <c:when test="${service.fromPackage}">
            <c:set var="serviceFromPackageAmount" value="${serviceFromPackageAmount + service.totalPrice}" />
        </c:when>
        <c:otherwise>
            <c:set var="serviceTotalAmount" value="${serviceTotalAmount + service.totalPrice}" />
        </c:otherwise>
    </c:choose>
</c:forEach>

<!-- Tính tổng tiền phụ tùng KHÔNG từ gói (phụ tùng riêng) -->
<c:set var="partTotalAmount" value="0" />
<c:set var="partFromPackageAmount" value="0" />
<c:forEach items="${partDetails}" var="part">
    <c:choose>
        <c:when test="${part.fromPackage}">
            <c:set var="partFromPackageAmount" value="${partFromPackageAmount + part.totalPrice}" />
        </c:when>
        <c:otherwise>
            <c:set var="partTotalAmount" value="${partTotalAmount + part.totalPrice}" />
        </c:otherwise>
    </c:choose>
</c:forEach>

<!-- Tính tổng tiền dịch vụ KHÔNG từ gói (dịch vụ riêng) -->
<c:set var="serviceTotalAmount" value="0" />
<c:set var="serviceFromPackageAmount" value="0" />
<c:forEach items="${serviceDetails}" var="service">
    <c:choose>
        <c:when test="${service.fromPackage}">
            <c:set var="serviceFromPackageAmount" value="${serviceFromPackageAmount + service.totalPrice}" />
        </c:when>
        <c:otherwise>
            <c:set var="serviceTotalAmount" value="${serviceTotalAmount + service.totalPrice}" />
        </c:otherwise>
    </c:choose>
</c:forEach>

<!-- Tính tổng tiền phụ tùng KHÔNG từ gói (phụ tùng riêng) -->
<c:set var="partTotalAmount" value="0" />
<c:set var="partFromPackageAmount" value="0" />
<c:forEach items="${partDetails}" var="part">
    <c:choose>
        <c:when test="${part.fromPackage}">
            <c:set var="partFromPackageAmount" value="${partFromPackageAmount + part.totalPrice}" />
        </c:when>
        <c:otherwise>
            <c:set var="partTotalAmount" value="${partTotalAmount + part.totalPrice}" />
        </c:otherwise>
    </c:choose>
</c:forEach>


<!-- Tính tổng tiền dịch vụ KHÔNG từ gói (dịch vụ riêng) -->
<c:set var="serviceTotalAmount" value="0" />
<c:set var="serviceFromPackageAmount" value="0" />
<c:forEach items="${serviceDetails}" var="service">
    <c:choose>
        <c:when test="${service.fromPackage}">
            <c:set var="serviceFromPackageAmount" value="${serviceFromPackageAmount + service.totalPrice}" />
        </c:when>
        <c:otherwise>
            <c:set var="serviceTotalAmount" value="${serviceTotalAmount + service.totalPrice}" />
        </c:otherwise>
    </c:choose>
</c:forEach>

<!-- Tính tổng tiền phụ tùng KHÔNG từ gói (phụ tùng riêng) -->
<c:set var="partTotalAmount" value="0" />
<c:set var="partFromPackageAmount" value="0" />
<c:forEach items="${partDetails}" var="part">
    <c:choose>
        <c:when test="${part.fromPackage}">
            <c:set var="partFromPackageAmount" value="${partFromPackageAmount + part.totalPrice}" />
        </c:when>
        <c:otherwise>
            <c:set var="partTotalAmount" value="${partTotalAmount + part.totalPrice}" />
        </c:otherwise>
    </c:choose>
</c:forEach>

<!-- Tính tổng tiền dịch vụ KHÔNG từ gói (dịch vụ riêng) -->
<c:set var="serviceTotalAmount" value="0" />
<c:set var="serviceFromPackageAmount" value="0" />
<c:forEach items="${serviceDetails}" var="service">
    <c:choose>
        <c:when test="${service.fromPackage}">
            <c:set var="serviceFromPackageAmount" value="${serviceFromPackageAmount + service.totalPrice}" />
        </c:when>
        <c:otherwise>
            <c:set var="serviceTotalAmount" value="${serviceTotalAmount + service.totalPrice}" />
        </c:otherwise>
    </c:choose>
</c:forEach>

<!-- Tính tổng tiền phụ tùng KHÔNG từ gói (phụ tùng riêng) -->
<c:set var="partTotalAmount" value="0" />
<c:set var="partFromPackageAmount" value="0" />
<c:forEach items="${partDetails}" var="part">
    <c:choose>
        <c:when test="${part.fromPackage}">
            <c:set var="partFromPackageAmount" value="${partFromPackageAmount + part.totalPrice}" />
        </c:when>
        <c:otherwise>
            <c:set var="partTotalAmount" value="${partTotalAmount + part.totalPrice}" />
        </c:otherwise>
    </c:choose>
</c:forEach>

<!-- Tính tổng tiền dịch vụ KHÔNG từ gói (dịch vụ riêng) -->
<c:set var="serviceTotalAmount" value="0" />
<c:set var="serviceFromPackageAmount" value="0" />
<c:forEach items="${serviceDetails}" var="service">
    <c:choose>
        <c:when test="${service.fromPackage}">
            <c:set var="serviceFromPackageAmount" value="${serviceFromPackageAmount + service.totalPrice}" />
        </c:when>
        <c:otherwise>
            <c:set var="serviceTotalAmount" value="${serviceTotalAmount + service.totalPrice}" />
        </c:otherwise>
    </c:choose>
</c:forEach>

<!-- Tính tổng tiền phụ tùng KHÔNG từ gói (phụ tùng riêng) -->
<c:set var="partTotalAmount" value="0" />
<c:set var="partFromPackageAmount" value="0" />
<c:forEach items="${partDetails}" var="part">
    <c:choose>
        <c:when test="${part.fromPackage}">
            <c:set var="partFromPackageAmount" value="${partFromPackageAmount + part.totalPrice}" />
        </c:when>
        <c:otherwise>
            <c:set var="partTotalAmount" value="${partTotalAmount + part.totalPrice}" />
        </c:otherwise>
    </c:choose>
</c:forEach>

<!-- Tính tổng tiền thêm (dịch vụ + phụ tùng riêng) -->
<c:set var="additionalAmount" value="${serviceTotalAmount + partTotalAmount}" />

<!-- Tính tổng cộng cuối cùng = maintenance.finalAmount + dịch vụ/phụ tùng riêng (KHÔNG trừ giảm giá) -->
<c:set var="calculatedTotalAmount" value="${maintenance.totalAmount + additionalAmount}" />
<c:set var="calculatedFinalAmount" value="${maintenance.finalAmount + additionalAmount}" />

<div class="detail-card">
    <div class="card-title">🔧 Chi tiết dịch vụ</div>
    
    <c:choose>
        <c:when test="${empty serviceDetails}">
            <div class="empty-message">Chưa có dịch vụ nào được thực hiện</div>
        </c:when>
        <c:otherwise>
            <table class="table">
                <thead>
                    <tr>
                        <th>Mã</th>
                        <th>Tên dịch vụ</th>
                        <th>Số lượng</th>
                        <th>Đơn giá</th>
                        <th>Thành tiền</th>
                        <th>Ghi chú</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${serviceDetails}" var="service">
                        <tr>
                            <td>
                                ${service.product.code}
                                <c:if test="${service.fromPackage}">
                                    <br><span class="badge badge-package">Từ gói</span>
                                </c:if>
                            </td>
                            <td>
                                <strong>${service.product.name}</strong>
                                <br><span class="badge badge-service">${service.product.type}</span>
                            </td>
                            <td><fmt:formatNumber value="${service.quantity}" maxFractionDigits="2" /></td>
                            <td><fmt:formatNumber value="${service.unitPrice}" type="number" maxFractionDigits="0" groupingUsed="true" />₫</td>
                            <td><strong><fmt:formatNumber value="${service.totalPrice}" type="number" maxFractionDigits="0" groupingUsed="true" />₫</strong></td>
                            <td>${service.notes}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:otherwise>
    </c:choose>
</div>

<div class="detail-card">
    <div class="card-title">⚙️ Chi tiết phụ tùng</div>
    
    <c:choose>
        <c:when test="${empty partDetails}">
            <div class="empty-message">Chưa có phụ tùng nào được thay thế</div>
        </c:when>
        <c:otherwise>
            <table class="table">
                <thead>
                    <tr>
                        <th>Mã</th>
                        <th>Tên phụ tùng</th>
                        <th>Số lượng</th>
                        <th>Đơn giá</th>
                        <th>Thành tiền</th>
                        <th>Ngày lắp đặt</th>
                        <th>Bảo hành đến</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${partDetails}" var="part">
                        <tr>
                            <td>
                                ${part.product.code}
                                <c:if test="${part.fromPackage}">
                                    <br><span class="badge badge-package">Từ gói</span>
                                </c:if>
                            </td>
                            <td>
                                <strong>${part.product.name}</strong>
                                <br><span class="badge badge-part">${part.product.type}</span>
                            </td>
                            <td>${part.quantity}</td>
                            <td><fmt:formatNumber value="${part.unitPrice}" type="number" maxFractionDigits="0" groupingUsed="true" />₫</td>
                            <td><strong><fmt:formatNumber value="${part.totalPrice}" type="number" maxFractionDigits="0" groupingUsed="true" />₫</strong></td>
                            <td>${part.installationDate}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${not empty part.warrantyExpireDate}">
                                        ${part.warrantyExpireDate}
                                    </c:when>
                                    <c:otherwise>-</c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:otherwise>
    </c:choose>
</div>

<div class="detail-card">
    <div class="summary">
        <div class="summary-row">
            <span>Tổng tiền gói package:</span>
            <span><fmt:formatNumber value="${maintenance.totalAmount}" type="number" maxFractionDigits="0" groupingUsed="true" />₫</span>
        </div>
        <div class="summary-row">
            <span>Giảm giá gói:</span>
            <span>- <fmt:formatNumber value="${maintenance.discountAmount}" type="number" maxFractionDigits="0" groupingUsed="true" />₫</span>
        </div>
        <div class="summary-row">
            <span>Thành tiền gói sau giảm giá:</span>
            <span><fmt:formatNumber value="${maintenance.finalAmount}" type="number" maxFractionDigits="0" groupingUsed="true" />₫</span>
        </div>
        <div class="summary-row" style="border-top: 1px dashed #ddd; margin-top: 10px; padding-top: 10px;">
            <span>Dịch vụ riêng (ngoài gói):</span>
            <span><fmt:formatNumber value="${serviceTotalAmount}" type="number" maxFractionDigits="0" groupingUsed="true" />₫</span>
        </div>
        <div class="summary-row">
            <span>Phụ tùng riêng (ngoài gói):</span>
            <span><fmt:formatNumber value="${partTotalAmount}" type="number" maxFractionDigits="0" groupingUsed="true" />₫</span>
        </div>
        <div class="summary-row total" style="border-top: 2px solid #333; margin-top: 10px; padding-top: 10px;">
            <span><strong>Tổng thành tiền thanh toán:</strong></span>
            <span><strong><fmt:formatNumber value="${calculatedFinalAmount}" type="number" maxFractionDigits="0" groupingUsed="true" />₫</strong></span>
        </div>
    </div>
</div>
            </div>
        </c:if>
    </div>
</div>
</body>
</html>