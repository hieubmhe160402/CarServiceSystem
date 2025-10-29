<%-- 
    Document   : page-list-supplier
    Created on : Oct 27, 2025
    Author     : MinHeee
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Quản lý Supplier</title>
        <style>
            /* --- RESET & BASE --- */
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
                        </div>

                        <button class="btn btn-add" onclick="openModal()">+ Thêm Supplier</button>
                    </div>
                    <table>
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Tên</th>
                                <th>Địa chỉ</th>
                                <th>Điện thoại</th>
                                <th>Email</th>
                                <th>Mô tả</th>
                                <th>Trạng thái</th>
                                <th>Ngày tạo</th>
                                <th>Hành động</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${suppliers}" var="s">
                                <tr>
                                    <td>${s.supplierId}</td>
                                    <td>${s.name}</td>
                                    <td>${s.address}</td>
                                    <td>${s.phone}</td>
                                    <td>${s.email}</td>
                                    <td class="description-cell" title="${s.description}">${s.description}</td>
                                    <td>
                                        <c:out value="${s.isActive ? 'Hoạt động' : 'Ngừng'}"/>
                                    </td>
                                    <td>${s.createdDate}</td>
                                    <td>
                                        <%-- Cấu trúc kiểm tra trạng thái nhà cung cấp --%>
                                        <c:choose>
                                            <c:when test="${s.isActive}">
                                                <!-- Nút sửa -->
                                                <button class="btn btn-edit"
                                                        onclick="editSupplier(${s.supplierId}, '${s.name}', '${s.address}', '${s.phone}', '${s.email}', '${s.description}', ${s.isActive})">
                                                    Sửa
                                                </button>

                                                <!-- Nút ngừng hoạt động -->
                                                <form method="post" action="supplier" style="display:inline;">
                                                    <input type="hidden" name="action" value="updateStatus">
                                                    <input type="hidden" name="id" value="${s.supplierId}">
                                                    <input type="hidden" name="status" value="false">
                                                    <button type="submit" class="btn btn-status-off">Ngừng</button>
                                                </form>
                                            </c:when>

                                            <c:otherwise>
                                                <!-- Nút kích hoạt -->
                                                <form method="post" action="supplier" style="display:inline;">
                                                    <input type="hidden" name="action" value="updateStatus">
                                                    <input type="hidden" name="id" value="${s.supplierId}">
                                                    <input type="hidden" name="status" value="true">
                                                    <button type="submit" class="btn btn-status-on">Kích hoạt</button>
                                                </form>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>

                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
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

        <!-- Modal thêm/sửa -->
        <div id="supplierModal" class="modal">
            <div class="modal-content">
                <span class="close" onclick="closeModal()">&times;</span>

                <h3 id="modalTitle">Thêm Supplier</h3>
                <c:if test="${showModal and not empty errorMessage}">
                    <div id="errorBox" style="background:#ffe6e6; color:#b71c1c; padding:10px; border-radius:6px; margin-bottom:10px; text-align:center; font-weight:500;">
                        <c:out value="${errorMessage}" escapeXml="false"/>
                    </div>
                </c:if>

                <form id="supplierForm" method="post" action="supplier">
                    <input type="hidden" id="supplierId" name="supplierId">
                    <input type="hidden" id="action" name="action" value="add">

                    <div class="form-group">
                        <label>Tên *</label>
                        <input type="text" id="name" name="name" value="${supplier.name}" required>
                    </div>

                    <div class="form-group">
                        <label>Địa chỉ *</label>
                        <input type="text" id="address" name="address" value="${supplier.address}" required>
                    </div>

                    <div class="form-group">
                        <label>Điện thoại *</label>
                        <input type="text" id="phone" name="phone" value="${supplier.phone}" required>
                    </div>

                    <div class="form-group">
                        <label>Email *</label>
                        <input type="email" id="email" name="email" value="${supplier.email}" required>
                    </div>

                    <div class="form-group">
                        <label>Mô tả</label>
                        <textarea id="description" name="description"></textarea>
                    </div>

                    <div class="form-group">
                        <label>Trạng thái</label>
                        <select id="isActive" name="isActive">
                            <option value="true">Hoạt động</option>
                            <option value="false">Ngừng</option>
                        </select>
                    </div>

                    <div class="form-actions">
                        <button type="button" class="btn btn-cancel" onclick="closeModal()">Hủy</button>
                        <button type="submit" class="btn btn-save">Lưu</button>
                    </div>
                </form>
            </div>
        </div>



        <c:if test="${showModal}">
            <script>
                window.addEventListener('DOMContentLoaded', () => {
                    document.getElementById('supplierModal').style.display = 'block';
                    document.getElementById('modalTitle').textContent = '${supplier != null && supplier.supplierId != 0 ? "Sửa Supplier" : "Thêm Supplier"}';
                });
            </script>
        </c:if>
        <script>
            let deleteId = null;

            function openModal() {
                document.getElementById('supplierModal').style.display = 'block';
                document.getElementById('modalTitle').textContent = 'Thêm Supplier';
                document.getElementById('supplierForm').reset();
                document.getElementById('action').value = 'add';
                const errorBox = document.getElementById('errorBox');
                if (errorBox)
                    errorBox.remove();
            }

            function closeModal() {
                const modal = document.getElementById('supplierModal');
                const form = document.getElementById('supplierForm');
                modal.style.display = 'none';
                form.reset(); // ✅ reset toàn bộ input

                // Xóa cả lỗi nếu có
                const errorBox = document.getElementById('errorBox');
                if (errorBox)
                    errorBox.remove();
            }

            function editSupplier(id, name, address, phone, email, description, isActive) {
                // Mở modal sửa
                document.getElementById('supplierModal').style.display = 'block';
                document.getElementById('modalTitle').textContent = 'Sửa Supplier';
                document.getElementById('action').value = 'edit';
                document.getElementById('supplierId').value = id;
                document.getElementById('name').value = name;
                document.getElementById('address').value = address;
                document.getElementById('phone').value = phone;
                document.getElementById('email').value = email;
                document.getElementById('description').value = description;
                document.getElementById('isActive').value = isActive;

                // ✅ Xóa lỗi cũ nếu có
                const errorBox = document.getElementById('errorBox');
                if (errorBox)
                    errorBox.remove();
            }

            function deleteSupplier(id) {
                deleteId = id;
                document.getElementById('deleteModal').style.display = 'block';
            }

            function closeDeleteModal() {
                document.getElementById('deleteModal').style.display = 'none';
                deleteId = null;
            }

            function confirmDelete() {
                if (deleteId) {
                    window.location.href = 'supplier?action=delete&id=' + deleteId;
                }
            }

            document.getElementById('searchInput').addEventListener('keyup', function () {
                const filter = this.value.toUpperCase();
                const rows = document.querySelectorAll('tbody tr');
                rows.forEach(row => {
                    const name = row.cells[1].textContent;
                    row.style.display = name.toUpperCase().includes(filter) ? '' : 'none';
                });
            });

            window.onclick = function (event) {
                const modals = [document.getElementById('supplierModal'), document.getElementById('deleteModal')];
                modals.forEach(m => {
                    if (event.target === m)
                        m.style.display = 'none';
                });
            };
        </script>
        <!--FilterbyStatus-->
        <script>
            const searchInput = document.getElementById("searchInput");
            const statusFilter = document.getElementById("statusFilter");
            const tableRows = document.querySelectorAll("table tbody tr");

            function filterTable() {
                const searchText = searchInput.value.toLowerCase();
                const selectedStatus = statusFilter.value; // "", "true", "false"

                tableRows.forEach(row => {
                    const nameCell = row.querySelector("td:nth-child(2)"); // cột Tên
                    const statusCell = row.querySelector("td:nth-child(7)"); // cột Trạng thái
                    if (!nameCell || !statusCell)
                        return;

                    const nameText = nameCell.textContent.toLowerCase();
                    const statusText = statusCell.textContent.toLowerCase();

                    const matchesName = nameText.includes(searchText);
                    const matchesStatus =
                            selectedStatus === "" ||
                            (selectedStatus === "true" && statusText.includes("hoạt động")) ||
                            (selectedStatus === "false" && statusText.includes("ngừng"));

                    if (matchesName && matchesStatus) {
                        row.style.display = "";
                    } else {
                        row.style.display = "none";
                    }
                });
            }

            searchInput.addEventListener("input", filterTable);
            statusFilter.addEventListener("change", filterTable);



        </script>
    </body>
</html>
