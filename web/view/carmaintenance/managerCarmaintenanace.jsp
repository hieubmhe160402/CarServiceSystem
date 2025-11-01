<%-- 
    Document   : managerCarmaintenanace
    Created on : Oct 23, 2025, 4:19:29 PM
    Author     : MinHeee
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8" />
        <title>Quản lý lịch bảo dưỡng</title>
        <style>
            * {
                margin: 0;
                padding: 0;
                box-sizing: border-box;
            }
            body {
                font-family: Inter, "Segoe UI", Roboto, Arial, sans-serif;
                background: #f5f7fb;
                color: #111827;
            }
            .app {
                display: flex;
                height: 100vh;
            }

            /* --- SIDEBAR --- */
            /* --- SIDEBAR --- */
            .sidebar {
                width: 260px;
                background: linear-gradient(180deg,#0f2340,#0b1830);
                color:#fff;
                padding:28px 18px;
                display:flex;
                flex-direction:column;
            }

            /* --- Main Content --- */
            .main {
                flex: 1;
                padding: 24px 32px;
                overflow: auto;
            }
            .container {
                background: #fff;
                border-radius: 8px;
                padding: 25px 30px;
                box-shadow: 0 2px 6px rgba(0, 0, 0, 0.05);
            }
            h2 {
                font-size: 22px;
                font-weight: 600;
                color: #222;
                margin-bottom: 20px;
            }

            /* --- TOP BAR --- */
            .top-bar {
                display: flex;
                justify-content: space-between;
                align-items: center;
                margin-bottom: 16px;
                flex-wrap: wrap;
                gap: 10px;
            }
            .search-box {
                padding: 8px 12px;
                border: 1px solid #d1d5db;
                border-radius: 6px;
                width: 300px;
                font-size: 14px;
            }
            .search-box:focus {
                outline: none;
                border-color: #2563eb;
                box-shadow: 0 0 0 2px rgba(37,99,235,0.2);
            }

            /* --- BUTTONS --- */
            .btn {
                border: none;
                border-radius: 6px;
                cursor: pointer;
                font-size: 14px;
                padding: 8px 16px;
                font-weight: 500;
                transition: all 0.25s;
            }
            .btn-add {
                background: #16a34a;
                color: #fff;
            }
            .btn-add:hover {
                background: #15803d;
            }
            .btn-edit {
                background: #3b82f6;
                color: white;
                padding: 6px 12px;
                margin-right: 5px;
            }
            .btn-edit:hover {
                background: #2563eb;
            }
            .btn-delete {
                background: #ef4444;
                color: white;
                padding: 6px 12px;
            }
            .btn-delete:hover {
                background: #dc2626;
            }
            .btn-cancel {
                background: #6c757d;
                color: white;
            }
            .btn-save {
                background: #28a745;
                color: white;
            }

            /* --- TABLE --- */
            table {
                width: 100%;
                border-collapse: collapse;
                margin-top: 15px;
            }
            th, td {
                padding: 12px;
                text-align: left;
                border-bottom: 1px solid #e5e7eb;
            }
            th {
                background: #f8fafc;
                color: #374151;
                font-weight: 600;
                font-size: 14px;
            }
            td {
                font-size: 14px;
                vertical-align: middle;
            }
            tr:hover {
                background: #f9fafb;
            }
            .description-cell {
                max-width: 260px;
                overflow: hidden;
                text-overflow: ellipsis;
                white-space: nowrap;
            }

            /* --- PAGINATION --- */
            .pagination {
                display: flex;
                justify-content: center;
                align-items: center;
                gap: 6px;
                margin-top: 25px;
            }
            .pagination a {
                padding: 8px 12px;
                border: 1px solid #ddd;
                border-radius: 6px;
                color: #333;
                text-decoration: none;
                font-size: 14px;
                transition: all 0.2s;
            }
            .pagination a:hover {
                background: #2563eb;
                color: white;
                border-color: #2563eb;
            }
            .pagination .active {
                background: #2563eb;
                color: white;
                border-color: #2563eb;
                font-weight: 600;
            }

            /* --- MODAL --- (giữ nguyên layout hiện tại) */
            .modal {
                display: none;
                position: fixed;
                z-index: 1000;
                left: 0;
                top: 0;
                width: 100%;
                height: 100%;
                background: rgba(0,0,0,0.5);
                overflow-y: auto;
            }
            .modal-content {
                background: white;
                margin: 5% auto;
                padding: 30px;
                width: 500px;
                border-radius: 10px;
                position: relative;
                box-shadow: 0 5px 20px rgba(0,0,0,0.15);
            }
            .close {
                position: absolute;
                right: 15px;
                top: 10px;
                font-size: 26px;
                cursor: pointer;
                color: #aaa;
            }
            .close:hover {
                color: #000;
            }
            .form-group label {
                font-weight: 600;
                margin-bottom: 5px;
                color: #222;
                display: block;
            }
            .form-group input,
            .form-group textarea,
            .form-group select {
                width: 100%;
                padding: 8px;
                border: 1px solid #ddd;
                border-radius: 6px;
                font-size: 14px;
            }
            .form-actions {
                display: flex;
                justify-content: flex-end;
                gap: 10px;
                margin-top: 20px;
            }

            /* --- DELETE MODAL --- */
            #deleteModal .modal-content {
                width: 400px;
                text-align: center;
            }
            #deleteModal h3 {
                margin-bottom: 15px;
                color: #222;
            }
            #deleteModal p {
                margin-bottom: 20px;
                font-size: 15px;
                color: #444;
            }
            .status-filter {
                padding: 6px 10px;
                border-radius: 5px;
                border: 1px solid #ccc;
                font-size: 14px;
            }
            .btn-reload {
                background: #0ea5e9;
                color: white;
                border: none;
                border-radius: 6px;
                padding: 8px 14px;
                cursor: pointer;
                transition: background 0.2s;
            }
            .btn-reload:hover {
                background: #0284c7;
            }
            .btn-primary {
                background: #3b82f6; /* xanh dương */
                color: #fff;
            }
            .btn-primary:hover {
                background: #2563eb;
            }

            .btn-danger {
                background: #ef4444; /* đỏ */
                color: #fff;
            }
            .btn-danger:hover {
                background: #dc2626;
            }
            .modal {
                display: none;
                position: fixed;
                z-index: 2000;
                left: 0;
                top: 0;
                width: 100%;
                height: 100%;
                background: rgba(0,0,0,0.5);
                overflow-y: auto;
            }

            /* Hộp nội dung chính */
            .modal-content {
                background: #fff;
                margin: 3% auto;
                border-radius: 10px;
                width: 75%;
                max-width: 1000px;
                box-shadow: 0 5px 25px rgba(0,0,0,0.25);
                animation: fadeIn 0.3s ease-in-out;
            }

            /* Header của phiếu */
            .modal-header {
                display: flex;
                justify-content: space-between;
                align-items: center;
                background: #e6f0ff;
                padding: 15px 25px;
                border-radius: 10px 10px 0 0;
                border-bottom: 1px solid #cbd5e1;
            }
            .modal-header h2 {
                font-size: 20px;
                font-weight: 600;
                color: #1e3a8a;
            }
            .status-badge {
                display: inline-block;
                padding: 6px 12px;
                border-radius: 6px;
                font-weight: bold;
                font-size: 13px;
                text-transform: uppercase;
            }

            /* Trạng thái WAITING - nền vàng nhạt */
            .status-badge.waiting {
                background-color: #fff9e6;
                color: #b38b00;
                border: 1px solid #ffe58f;
            }

            /* Trạng thái PROCESSING - nền xanh nhạt + viền */
            .status-badge.processing {
                background-color: #e6f0ff;
                color: #004085;
                border: 1px solid #b3d1ff;
            }
            .close {
                font-size: 26px;
                cursor: pointer;
                color: #6b7280;
            }
            .close:hover {
                color: #000;
            }

            /* Nội dung body */
            .modal-body {
                padding: 10px;
            }
            .grid {
                display: grid;
                grid-template-columns: repeat(3, 1fr);
                gap: 20px;
                margin-bottom: 20px;
            }
            .form-group label {
                font-weight: 600;
                margin-bottom: 6px;
                color: #111827;
                display: block;
            }
            .form-group input, .form-group textarea, .form-group select {
                width: 100%;
                padding: 8px 10px;
                border: 1px solid #d1d5db;
                border-radius: 6px;
                font-size: 14px;
            }
            .form-group.full {
                grid-column: 1 / 4;
            }

            /* Nút chọn */
            .btn-blue {
                background: #3b82f6;
                color: #fff;
                border: none;
                border-radius: 6px;
                padding: 6px 14px;
                font-size: 14px;
                cursor: pointer;
            }
            .btn-blue:hover {
                background: #2563eb;
            }

            /* Animation */
            @keyframes fadeIn {
                from {
                    opacity: 0;
                    transform: translateY(-10px);
                }
                to {
                    opacity: 1;
                    transform: translateY(0);
                }
            }
            .technician-grid {
                grid-template-columns: 1fr 2fr; /* Bên trái nhỏ hơn bên phải */
                align-items: flex-start;
                gap: 20px;
            }

            .btn-gray {
                background-color: #999;
                color: #fff;
                padding: 6px 14px;
                border: none;
                border-radius: 4px;
                cursor: pointer;
            }

            .info-box {
                background: #f2f2f2;
                padding: 20px 15px;
                border-radius: 4px;
                display: flex;
                justify-content: space-between;
                flex-wrap: wrap;
                font-size: 14px;
            }
        </style>
    </head>
    <body>
        <div class="app">
            <!-- Sidebar -->
            <jsp:include page="/view/layout/sidebar.jsp"/>

            <!-- Main -->
            <div class="main">
                <div class="container">
                    <h2>Quản lý lịch bảo dưỡng</h2>

                    <div class="top-bar">
                        <form action="listCarmaintenance" method="get" style="display:flex; align-items:center; gap:10px;">
                            <input type="text" name="search" placeholder="Tìm kiếm theo tên khách hàng..." 
                                   value="${searchKeyword}" class="search-box">

                            <select name="status" class="status-filter">
                                <option value="">Tất cả</option>
                                <option value="WAITING" ${selectedStatus == 'WAITING' ? 'selected' : ''}>WAITING</option>
                                <option value="PROCESSING" ${selectedStatus == 'PROCESSING' ? 'selected' : ''}>PROCESSING</option>
                                <option value="COMPLETE" ${selectedStatus == 'COMPLETE' ? 'selected' : ''}>COMPLETE</option>
                                <option value="CANCELLED" ${selectedStatus == 'CANCELLED' ? 'selected' : ''}>CANCELLED</option>
                            </select>
                            </select>
                            <button type="button" class="btn btn-reload" onclick="window.location.href = 'listCarmaintenance'">🔁 Tải lại</button>
                        </form>
                    </div>

                    <table class="table table-bordered">
                        <thead>
                            <tr>
                                <th>Mã bảo dưỡng</th>
                                <th>Mã lịch hẹn</th>
                                <th>Khách hàng</th>
                                <th>Biển số xe</th>
                                <th>Hãng xe</th>
                                <th>Ngày bảo dưỡng</th>
                                <th>Trạng thái</th>
                                <th>Kỹ thuật viên</th>
                                <th>Hành động</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:choose>
                                <c:when test="${not empty maintenances}">
                                    <c:forEach items="${maintenances}" var="m">
                                        <tr>
                                            <td>${m.maintenanceId}</td>
                                            <td>${m.appointment.appointmentId}</td>
                                            <td>${m.car.owner.fullName}</td>
                                            <td>${m.car.licensePlate}</td>
                                            <td>${m.car.brand} ${m.car.model}</td>
                                            <td>${m.maintenanceDate}</td>
                                            <td>${m.status}</td>

                                            <td>
                                                <c:choose>
                                                    <c:when test="${m.assignedTechnician.fullName eq 'None'}">
                                                        <span class="text-muted">None</span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        ${m.assignedTechnician.fullName}
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>

                                            <!-- Cột hành động -->
                                            <td class="text-center">
                                                <c:choose>
                                                    <c:when test="${m.status eq 'CANCELLED'}">
                                                        <span class="text-muted">Đã hủy</span>
                                                    </c:when>
                                                    <c:when test="${m.status eq 'COMPLETE'}">
                                                        <form method="get" action="listCarmaintenance" style="display:inline;">
                                                            <input type="hidden" name="action" value="assign" />
                                                            <input type="hidden" name="maintenanceId" value="${m.maintenanceId}" />
                                                            <button type="submit" class="btn btn-primary btn-sm">Details</button>
                                                        </form>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <!-- Gửi request đến servlet để hiển thị chi tiết -->
                                                        <form method="get" action="listCarmaintenance" style="display:inline;">
                                                            <input type="hidden" name="action" value="assign" />
                                                            <input type="hidden" name="maintenanceId" value="${m.maintenanceId}" />
                                                            <button type="submit" class="btn btn-primary btn-sm">Assign</button>
                                                        </form>

                                                        <!-- Nút cancel vẫn giữ nguyên -->
                                                        <form method="post" action="listCarmaintenance" style="display:inline;">
                                                            <input type="hidden" name="action" value="cancel" />
                                                            <input type="hidden" name="maintenanceId" value="${m.maintenanceId}" />
                                                            <button type="submit" class="btn btn-danger btn-sm">Cancel</button>
                                                        </form>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>

                                        </tr>
                                    </c:forEach>
                                </c:when>

                                <c:otherwise>
                                    <tr>
                                        <td colspan="9" class="text-center text-muted">Không có dữ liệu bảo dưỡng.</td>
                                    </tr>
                                </c:otherwise>
                            </c:choose>
                        </tbody>
                    </table>
                    <!-- POPUP PHIẾU DỊCH VỤ -->
                    <c:if test="${not empty detail}">
                        <div id="serviceModal" class="modal" style="display:block;">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <h2>Phiếu dịch vụ</h2>
                                    <span class="status-badge
                                          ${detail.status eq 'WAITING' ? 'waiting' : 
                                            (detail.status eq 'PROCESSING' ? 'processing' : '')}">
                                              ${detail.status}
                                          </span>
                                          <a href="listCarmaintenance" class="close">&times;</a>
                                    </div>

                                    <div class="modal-body">
                                        <div class="grid">
                                            <div class="form-group">
                                                <label>Mã phiếu dịch vụ</label>
                                                <input type="text" value="${detail.maintenanceId}" readonly />
                                            </div>
                                            <div class="form-group">
                                                <label>Mã lịch hẹn</label>
                                                <input type="text" value="${detail.appointment.appointmentId}" readonly />
                                            </div>
                                            <div class="form-group">
                                                <label>Ngày bảo trì</label>
                                                <input type="text" value="${detail.maintenanceDate}" readonly />
                                            </div>
                                        </div>

                                        <div class="grid">
                                            <div class="form-group">
                                                <label>Tên khách hàng</label>
                                                <input type="text" value="${detail.car.owner.fullName}" readonly />
                                            </div>
                                            <div class="form-group">
                                                <label>Số điện thoại</label>
                                                <input type="text" value="${detail.car.owner.phone}" readonly />
                                            </div>
                                            <div class="form-group">
                                                <label>Email</label>
                                                <input type="text" value="${detail.car.owner.email}" readonly />
                                            </div>
                                        </div>

                                        <div class="grid">
                                            <div class="form-group">
                                                <label>Thông tin xe</label>
                                                <input type="text" value="${detail.car.brand}" readonly />
                                            </div>
                                            <div class="form-group">
                                                <label>Odometer</label>
                                                <input type="text" value="${detail.odometer}" readonly />
                                            </div>
                                            <div class="form-group">
                                                <label>Kỹ thuật viên</label>
                                                <input type="text" value="${detail.assignedTechnician.userId}" readonly />
                                            </div>
                                        </div>

                                        <div class="form-group full">
                                            <label>Ghi chú</label>
                                            <textarea readonly>${detail.notes}</textarea>
                                        </div>


                                        <div class="grid technician-grid">
                                            <!-- Cột chọn kỹ thuật viên -->
                                            <div class="form-group">
                                                <label>Thông tin nhân viên sửa chữa</label>
                                                <p style="margin: 4px 0;">Chọn nhân viên sửa chữa *</p>
                                                <div style="display: flex; gap: 10px;">
                                                    <!-- Nút mở popup -->
                                                    <button id="btnSelectTech" class="btn btn-blue" type="button"
                                                            onclick="document.getElementById('technicianModal').style.display = 'block'">
                                                        Chọn
                                                    </button>
                                                    <!--information when staff picking up--> 
                                                    <div id="technicianModal" class="modal" style="display:none;">
                                                        <div class="modal-content" style="width: 500px; padding: 20px;">
                                                            <h4>Chọn kỹ thuật viên</h4>

                                                            <table class="table table-bordered" style="width:100%;">
                                                                <thead>
                                                                    <tr>
                                                                        <th>Tên</th>
                                                                        <th>Điện thoại</th>
                                                                        <th>Email</th>
                                                                        <th>Chọn</th>
                                                                    </tr>
                                                                </thead>
                                                                <tbody>
                                                                    <c:forEach var="t" items="${technicians}">
                                                                        <tr>
                                                                            <td>${t.fullName}</td>
                                                                            <td>${t.phone}</td>
                                                                            <td>${t.email}</td>
                                                                            <td style="text-align:center;">
                                                                                <form action="listCarmaintenance" method="post" style="display:inline;">
                                                                                    <input type="hidden" name="action" value="confirmAssign" />
                                                                                    <input type="hidden" name="maintenanceId" value="${detail.maintenanceId}" />
                                                                                    <input type="hidden" name="technicianId" value="${t.userId}" />
                                                                                    <button type="submit" class="btn btn-blue">Chọn</button>
                                                                                </form>
                                                                            </td>
                                                                        </tr>
                                                                    </c:forEach>
                                                                </tbody>
                                                            </table>

                                                            <div style="text-align:right; margin-top:10px;">
                                                                <button class="btn btn-gray" type="button"
                                                                        onclick="document.getElementById('technicianModal').style.display = 'none'">
                                                                    Đóng
                                                                </button>
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <button id="btnResetTech" class="btn btn-gray" type="button" style="display:none;"
                                                            onclick="resetTechnician()">
                                                        Chọn lại nhân viên
                                                    </button>
                                                </div>
                                            </div>

                                            <!-- Cột hiển thị thông tin kỹ thuật viên -->
                                            <div class="form-group tech-info-box">
                                                <label>Thông tin kỹ thuật viên</label>
                                                <div class="info-box">
                                                    <span><strong>Tên:</strong> ${detail.assignedTechnician.fullName}</span>
                                                    <span><strong>Điện thoại:</strong> ${detail.assignedTechnician.phone}</span>
                                                    <span><strong>Email:</strong> ${detail.assignedTechnician.email}</span>
                                                </div>
                                            </div>

                                            <!-- Modal chọn kỹ thuật viên -->
                                            <div id="technicianModal" class="modal" style="
                                                 display:none;
                                                 position: fixed;
                                                 z-index: 1000;
                                                 left: 0; top: 0;
                                                 width: 100%; height: 100%;
                                                 background: rgba(0,0,0,0.5);">

                                                <div class="modal-content" style="
                                                     background: white;
                                                     margin: 8% auto;
                                                     padding: 20px;
                                                     width: 700px;
                                                     border-radius: 8px;
                                                     box-shadow: 0 2px 10px rgba(0,0,0,0.2);">

                                                    <h3 style="text-align:center; margin-bottom: 15px;">Danh sách nhân viên sửa xe</h3>

                                                    <table border="1" cellspacing="0" cellpadding="8" 
                                                           style="width:100%; border-collapse:collapse; text-align:left;">
                                                        <thead style="background:#f5f5f5;">
                                                            <tr>
                                                                <th>ID</th>
                                                                <th>Tên nhân viên</th>
                                                                <th>Email</th>
                                                                <th>Số điện thoại</th>
                                                                <th>Trạng thái</th>
                                                                <th>Chức vụ</th>
                                                                <th>Hành động</th>
                                                            </tr>
                                                        </thead>
                                                        <tbody>
                                                            <c:forEach var="t" items="${technicians}">
                                                                <tr>
                                                                    <td>${t.userId}</td>
                                                                    <td>${t.fullName}</td>
                                                                    <td>${t.email}</td>
                                                                    <td>${t.phone}</td>
                                                                    <td>
                                                                        <c:choose>
                                                                            <c:when test="${t.isActive}">Hoạt động</c:when>
                                                                            <c:otherwise>Ngưng hoạt động</c:otherwise>
                                                                        </c:choose>
                                                                    </td>
                                                                    <td>${t.role.roleName}</td>
                                                                    <td style="text-align:center;">
                                                                        <form action="listCarmaintenance" method="post" style="margin:0;">
                                                                            <input type="hidden" name="action" value="confirmAssign" />
                                                                            <input type="hidden" name="maintenanceId" value="${detail.maintenanceId}" />
                                                                            <input type="hidden" name="technicianId" value="${t.userId}" />
                                                                            <button type="submit" 
                                                                                    class="btn btn-primary btn-sm"
                                                                                    style="background:#3B82F6; color:white; border:none; padding:5px 10px; border-radius:4px;">
                                                                                Chọn
                                                                            </button>
                                                                        </form>
                                                                    </td>
                                                                </tr>
                                                            </c:forEach>
                                                        </tbody>
                                                    </table>

                                                    <div style="text-align:right; margin-top:15px;">
                                                        <button type="button" class="btn btn-gray"
                                                                style="background:#ccc; border:none; padding:6px 12px; border-radius:4px;"
                                                                onclick="document.getElementById('technicianModal').style.display = 'none'">
                                                            Đóng
                                                        </button>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <!-- ===================== DANH SÁCH DỊCH VỤ SỬ DỤNG ===================== -->
                                    <div class="form-group full">
                                        <h3 style="margin-bottom: 10px;">Danh sách dịch vụ sử dụng</h3>
                                        <table border="1" cellspacing="0" cellpadding="8"
                                               style="width:100%; border-collapse:collapse; text-align:left;">
                                            <thead style="background:#f5f5f5;">
                                                <tr>
                                                    <th>Mã SP</th>
                                                    <th>Tên linh kiện/Dịch vụ</th>
                                                    <th>Số lượng</th>
                                                    <th>Đơn giá</th>
                                                    <th>Giảm giá</th>
                                                    <th>Thành tiền</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <c:choose>
                                                    <c:when test="${not empty products}">
                                                        <c:set var="prevPackage" value="" />
                                                        <c:set var="rowCount" value="0" />

                                                        <c:forEach var="item" items="${products}" varStatus="loop">
                                                            <!-- Khi gặp packageCode mới, đếm số hàng có cùng mã -->
                                                            <c:if test="${item.packageCode ne prevPackage}">
                                                                <c:set var="rowCount" value="0" />
                                                                <c:forEach var="inner" items="${products}">
                                                                    <c:if test="${inner.packageCode eq item.packageCode}">
                                                                        <c:set var="rowCount" value="${rowCount + 1}" />
                                                                    </c:if>
                                                                </c:forEach>
                                                            </c:if>

                                                            <tr>
                                                                <!-- Mã SP -->
                                                                <c:if test="${item.packageCode ne prevPackage}">
                                                                    <td rowspan="${rowCount}">${item.packageCode}</td>
                                                                </c:if>

                                                                <!-- Tên sản phẩm -->
                                                                <td>${item.productName}</td>
                                                                <td>${item.quantity}</td>

                                                                <!-- Các cột giá chỉ hiển thị 1 lần / nhóm -->
                                                                <c:if test="${item.packageCode ne prevPackage}">
                                                                    <td rowspan="${rowCount}">
                                                                        <fmt:formatNumber value="${item.basePrice}" type="number" groupingUsed="true"/> VND
                                                                    </td>
                                                                    <td rowspan="${rowCount}">
                                                                        ${item.discountPercent}%
                                                                    </td>
                                                                    <td rowspan="${rowCount}">
                                                                        <fmt:formatNumber value="${item.finalPrice}" type="number" groupingUsed="true"/> VND
                                                                    </td>
                                                                </c:if>
                                                            </tr>

                                                            <c:set var="prevPackage" value="${item.packageCode}" />
                                                        </c:forEach>
                                                    </c:when>

                                                    <c:otherwise>
                                                        <tr>
                                                            <td colspan="6" style="text-align:center;">Không có sản phẩm nào trong phiếu bảo dưỡng này</td>
                                                        </tr>
                                                    </c:otherwise>
                                                </c:choose>
                                            </tbody>
                                        </table>

                                        <div style="text-align:right; margin-top:10px; font-weight:bold;">
                                            Tổng tiền:
                                            <fmt:formatNumber value="${detail.finalAmount}" type="number" groupingUsed="true"/> VND
                                        </div>

                                        <div style="text-align:right; margin-top:30px;">
                                            <a href="listCarmaintenance" class="btn btn-danger" 
                                               style="background-color:#dc3545; border:none; padding:8px 18px; border-radius:6px; color:white;">
                                                Đóng
                                            </a>
                                        </div>
                                    </div>

                                </div>
                            </div>
                        </c:if>
                        <div class="pagination">
                            <c:if test="${currentPage > 1}">
                                <a href="listCarmaintenance?page=${currentPage - 1}">« Trước</a>
                            </c:if>

                            <c:forEach begin="1" end="${totalPages}" var="i">
                                <a href="listCarmaintenance?page=${i}" class="${i == currentPage ? 'active' : ''}">${i}</a>
                            </c:forEach>

                            <c:if test="${currentPage < totalPages}">
                                <a href="listCarmaintenance?page=${currentPage + 1}">Sau »</a>
                            </c:if>
                        </div>
                    </div>
                </div>
            </div>

            <script>
                window.onclick = function (event) {
                    const addModal = document.getElementById("addModal");
                    const detailModal = document.getElementById("detailModal");
                    if (event.target === addModal)
                        addModal.style.display = "none";
                    if (event.target === detailModal)
                        detailModal.style.display = "none";
                };
                document.getElementById('ownerSelect').addEventListener('change', function () {
                    const selectedOwner = this.value;
                    const carOptions = document.querySelectorAll('#carSelect option[data-owner]');
                    document.getElementById('carSelect').value = "";
                    carOptions.forEach(opt => {
                        opt.style.display = (opt.getAttribute('data-owner') === selectedOwner) ? 'block' : 'none';
                    });
                });

                function closeServiceModal() {
                    document.getElementById('serviceModal').style.display = 'none';
                }
                function chooseTechnician() {
                    alert("Popup chọn kỹ thuật viên sẽ hiển thị ở đây!");
                }
                window.onclick = function (e) {
                    const modal = document.getElementById('serviceModal');
                    if (e.target === modal)
                        closeServiceModal();
                }
                function openAssignModal(maintenanceId) {
                    // Lấy modal
                    const modal = document.getElementById("serviceModal");
                    modal.style.display = "block";

                    // 🟦 Nếu bạn muốn test nhanh mà chưa có servlet
                    // thì ta sẽ tạm gán dữ liệu demo (sau này có thể fetch từ server bằng AJAX)
                    document.getElementById("serviceId").value = "SRV-" + maintenanceId;
                    document.getElementById("createdDate").value = "2025-10-28";
                    document.getElementById("createdBy").value = "Admin";
                    document.getElementById("customerName").value = "Nguyễn Văn A";
                    document.getElementById("phone").value = "0901234567";
                    document.getElementById("email").value = "vana@gmail.com";
                    document.getElementById("carBrand").value = "Toyota Camry";
                    document.getElementById("odometer").value = "45,000 km";
                    document.getElementById("licensePlate").value = "51H-123.45";
                    document.getElementById("maintenanceDate").value = "2025-10-30";
                    document.getElementById("technician").value = "Chưa chọn";
                }
            </script>
            <c:if test="${openModal}">
                <script>
                    const serviceModal = new bootstrap.Modal(document.getElementById('serviceModal'));
                    serviceModal.show();
                </script>
            </c:if>

            <script>
                function selectTechnician(id, name, phone, email) {
                    // Cập nhật thông tin hiển thị
                    document.getElementById('techName').textContent = name;
                    document.getElementById('techPhone').textContent = phone;
                    document.getElementById('techEmail').textContent = email;

                    // Cập nhật input ẩn (nếu có)
                    const inputTechId = document.getElementById('technicianId');
                    if (inputTechId)
                        inputTechId.value = id;

                    // Ẩn popup
                    document.getElementById('technicianModal').style.display = 'none';

                    // Ẩn nút "Chọn", hiện nút "Chọn lại"
                    document.getElementById('btnSelectTech').style.display = 'none';
                    document.getElementById('btnResetTech').style.display = 'inline-block';
                }
                function resetTechnician() {
                    // Xóa thông tin kỹ thuật viên
                    document.getElementById('techName').textContent = 'Chưa chọn';
                    document.getElementById('techPhone').textContent = '-';
                    document.getElementById('techEmail').textContent = '-';

                    // Reset input hidden
                    const inputTechId = document.getElementById('technicianId');
                    if (inputTechId)
                        inputTechId.value = '';

                    // Hiện lại nút "Chọn", ẩn nút "Chọn lại"
                    document.getElementById('btnSelectTech').style.display = 'inline-block';
                    document.getElementById('btnResetTech').style.display = 'none';
                }
            </script>
            <script>
                document.addEventListener("DOMContentLoaded", function () {
                    const statusSelect = document.querySelector("select[name='status']");
                    const searchInput = document.querySelector("input[name='search']");

                    // 🔹 Khi chọn trạng thái → submit form tự động
                    statusSelect.addEventListener("change", function () {
                        this.form.submit();
                    });

                    // 🔹 Khi gõ tìm kiếm → submit sau 0.6s không gõ nữa
                    let typingTimer;
                    searchInput.addEventListener("input", function () {
                        clearTimeout(typingTimer);
                        typingTimer = setTimeout(() => this.form.submit(), 1500);
                    });
                });
            </script>
        </body>
    </html>
