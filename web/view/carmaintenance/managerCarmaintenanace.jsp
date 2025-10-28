<%-- 
    Document   : managerCarmaintenanace
    Created on : Oct 23, 2025, 4:19:29 PM
    Author     : MinHeee
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

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
                padding: 6px 12px;
                border-radius: 6px;
                font-size: 13px;
                font-weight: bold;
                color: #fff;
            }
            .status-badge.waiting {
                background: #f59e0b;
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
                padding: 25px;
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
        </style>
    </head>
    <body>
        <div class="app">
            <!-- Sidebar -->
            <jsp:include page="/view/layout/sidebar.jsp"/>

            <!-- Main -->
            <div class="main">
                <div class="container">
                    <h2>Quản lý Supplier</h2>

                    <div class="top-bar">
                        <div style="display: flex; align-items: center; gap: 10px;">
                            <input type="text" class="search-box" placeholder="Tìm kiếm theo tên..." id="searchInput">

                            <!-- Dropdown filter theo status -->
                            <select id="statusFilter" class="status-filter">
                                <option value="">Tất cả</option>
                                <option value="true">Hoạt động</option>
                                <option value="false">Ngừng hoạt động</option>
                            </select>
                            <button type="button" class="btn btn-reload" onclick="window.location.href = 'supplier'">
                                🔁 Reload
                            </button>
                            <button onclick="openServiceModal()" class="btn btn-blue">Test Popup Phiếu Dịch Vụ</button>

                        </div>

                        <button class="btn btn-add" onclick="openModal()">+ Thêm Supplier</button>
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

                                                    <c:otherwise>
                                                        <button type="button" 
                                                                class="btn btn-primary btn-sm" 
                                                                onclick="openAssignModal('${m.maintenanceId}')">
                                                            Assign
                                                        </button>

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
                    <div id="serviceModal" class="modal">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h2>Phiếu dịch vụ</h2>
                                <span class="status-badge waiting">WAITING</span>
                                <span class="close" onclick="closeServiceModal()">&times;</span>
                            </div>

                            <div class="modal-body">
                                <div class="grid">
                                    <div class="form-group">
                                        <label>Mã phiếu dịch vụ</label>
                                        <input type="text" id="serviceId" value="SRV-2025-001" readonly />
                                    </div>
                                    <div class="form-group">
                                        <label>Mã lịch hẹn</label>
                                        <input type="text" id="createdDate" value="2025-10-28" readonly />
                                    </div>
                                    <div class="form-group">
                                        <label>Người tạo phiếu</label>
                                        <input type="text" id="createdBy" value="Admin" readonly />
                                    </div>
                                </div>



                                <div class="grid">
                                    <div class="form-group">
                                        <label>Tên khách hàng</label>
                                        <input type="text" id="customerName" value="Nguyễn Văn A" readonly />
                                    </div>
                                    <div class="form-group">
                                        <label>Số điện thoại</label>
                                        <input type="text" id="phone" value="0901234567" readonly />
                                    </div>
                                    <div class="form-group">
                                        <label>Email</label>
                                        <input type="text" id="email" value="vana@gmail.com" readonly />
                                    </div>
                                </div>

                                <div class="grid">
                                    <div class="form-group">
                                        <label>Hãng xe</label>
                                        <input type="text" id="carBrand" value="Toyota" readonly />
                                    </div>
                                    <div class="form-group">
                                        <label>Odometer</label>
                                        <input type="text" id="odometer" value="45,000 km" readonly />
                                    </div>
                                    <div class="form-group">
                                        <label>Biển số xe</label>
                                        <input type="text" id="licensePlate" value="51H-123.45" readonly />
                                    </div>
                                </div>

                                <div class="grid">
                                    <div class="form-group">
                                        <label>Ngày bảo trì</label>
                                        <input type="text" id="maintenanceDate" value="2025-10-30" readonly />
                                    </div>
                                    <div class="form-group">
                                        <label>Kỹ thuật viên</label>
                                        <div style="display:flex; gap:10px;">
                                            <input type="text" id="technician" value="Chưa chọn" readonly />
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label>Thời gian hoàn thành </label>
                                        <input type="text" id="licensePlate" value="51H-123.45" readonly />
                                    </div>
                                </div>

                                <div class="form-group full">
                                    <label>Ghi chú</label>
                                    <textarea id="note" rows="3" placeholder="Nhập ghi chú thêm..."></textarea>
                                </div>
                                <div class="grid">
                                    <div class="form-group">
                                        <label>Thông tin nhân viên sửa chữa </label>
                                        <h6>Chọn nhân viên sửa chữa * </label>
                                            <button class="btn btn-blue" type="button" onclick="chooseTechnician()">Chọn</button>                                    </div>

                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="pagination">
                        <c:if test="${currentPage > 1}">
                            <a href="supplier?page=${currentPage - 1}">« Trước</a>
                        </c:if>

                        <c:forEach begin="1" end="${totalPages}" var="i">
                            <a href="supplier?page=${i}" class="${i == currentPage ? 'active' : ''}">${i}</a>
                        </c:forEach>

                        <c:if test="${currentPage < totalPages}">
                            <a href="supplier?page=${currentPage + 1}">Sau »</a>
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
            function openServiceModal() {
                document.getElementById('serviceModal').style.display = 'block';
            }
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
        </script>

    </body>
</html>
