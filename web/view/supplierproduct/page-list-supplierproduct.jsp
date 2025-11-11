<%-- 
    Document   : page-list-supplierproduct
    Created on : Oct 27, 2025, 2:36:44 PM
    Author     : MinHeee
--%>

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
                min-height: 100vh;
            }

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
                border-radius: 10px;
                padding: 25px;
                box-shadow: 0 3px 8px rgba(0,0,0,0.08);
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
                transition: border-color 0.2s, box-shadow 0.2s;
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

                    <c:if test="${not empty errorMessage}">
                        <div style="background-color: #f8d7da; color: #721c24; padding: 12px; border-radius: 6px; margin-bottom: 15px; border: 1px solid #f5c6cb;">
                            ${errorMessage}
                        </div>
                    </c:if>

                    <div class="top-bar">
                        <div style="display: flex; align-items: center; gap: 10px;">
                            <input type="text" class="search-box" placeholder="Tìm kiếm theo tên..." id="searchInput">

                            <!-- Dropdown filter theo status -->
                            <select id="statusFilter" class="status-filter">
                                <option value="">Tất cả</option>
                                <option value="true">Hoạt động</option>
                                <option value="false">Ngừng hoạt động</option>
                            </select>
                            <button type="button" class="btn btn-reload" onclick="window.location.href = 'supplierproduct'">
                                🔁 Reload
                            </button>
                        </div>

                        <div class="actions">
                            <button type="button" class="btn btn-add" onclick="openSupplierProductModal()">+ Thêm Supplier-Product</button>
                        </div>

                    </div>
                    <table>
                        <thead>
                            <tr>
                                <th>Supplier ID</th>
                                <th>Tên Supplier</th>
                                <th>Name</th>
                                <th>Thời gian giao hàng (ngày)</th>
                                <th>Giá ước tính</th>
                                <th>Chính sách</th>
                                <th>Trạng thái</th>
                                <th>Hành động</th>

                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${supplierProducts}" var="sp">
                                <tr>
                                    <td>${sp.supplier.supplierId}</td>
                                    <td>${sp.supplier.name}</td>
                                    <td>${sp.product.name}</td>
                                    <td>${sp.deliveryDuration}</td>
                                    <td>${sp.estimatedPrice}</td>
                                    <td class="description-cell" title="${sp.policies}">${sp.policies}</td>
                                    <td>
                                        <c:out value="${sp.isActive ? 'Hoạt động' : 'Ngừng'}"/>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${sp.isActive}">
                                                <!-- Nút Sửa -->
                                                <button type="button" class="btn btn-edit"
                                                        data-supplier-id="${sp.supplier.supplierId}"
                                                        data-product-id="${sp.product.productId}"
                                                        data-supplier-name="${sp.supplier.name}"
                                                        data-product-name="${sp.product.name}"
                                                        data-delivery-duration="${sp.deliveryDuration}"
                                                        data-estimated-price="${sp.estimatedPrice}"
                                                        data-policies="${sp.policies}"
                                                        data-is-active="${sp.isActive}"
                                                        onclick="editSupplierProductFromButton(this)">
                                                    Sửa
                                                </button>

                                                <!-- Nút Ngừng -->
                                                <form method="post" action="supplierproduct" style="display:inline;">
                                                    <input type="hidden" name="action" value="updateStatus">
                                                    <input type="hidden" name="supplierId" value="${sp.supplier.supplierId}">
                                                    <input type="hidden" name="productId" value="${sp.product.productId}">
                                                    <input type="hidden" name="status" value="false">
                                                    <button type="submit" class="btn btn-delete">Ngừng</button>
                                                </form>
                                            </c:when>

                                            <c:otherwise>
                                                <form method="post" action="supplierproduct" style="display:inline;">
                                                    <input type="hidden" name="action" value="updateStatus">
                                                    <input type="hidden" name="supplierId" value="${sp.supplier.supplierId}">
                                                    <input type="hidden" name="productId" value="${sp.product.productId}">
                                                    <input type="hidden" name="status" value="true">
                                                    <button type="submit" class="btn btn-add">Kích hoạt</button>
                                                </form>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>

                    <div class="pagination">
                        <c:if test="${totalPages > 1}">
                            <ul style="display: flex; list-style: none; gap: 8px; padding: 0;">
                                <!-- Nút Trước -->
                                <c:if test="${currentPage > 1}">
                                    <li><a href="supplierproduct?page=${currentPage - 1}">&laquo; Trước</a></li>
                                    </c:if>

                                <!-- Số trang -->
                                <c:forEach begin="1" end="${totalPages}" var="i">
                                    <li>
                                        <a href="supplierproduct?page=${i}"
                                           style="${i == currentPage ? 'font-weight:bold; text-decoration:underline;' : ''}">
                                            ${i}
                                        </a>
                                    </li>
                                </c:forEach>

                                <!-- Nút Sau -->
                                <c:if test="${currentPage < totalPages}">
                                    <li><a href="supplierproduct?page=${currentPage + 1}">Sau &raquo;</a></li>
                                    </c:if>
                            </ul>
                        </c:if>
                    </div>
                </div>
            </div>
        </div>

        <!-- Modal thêm/sửa -->
        <div id="supplierProductModal" class="modal" style="display:none;">
            <div class="modal-content">
                <span class="close" onclick="closeSupplierProductModal()">&times;</span>
                <h3 id="modalTitle">Thêm Supplier-Product</h3>

                <form action="supplierproduct" method="post">
                    <input type="hidden" name="action" id="formAction" value="add">
                    <input type="hidden" name="supplierId" id="hiddenSupplierId">
                    <input type="hidden" name="productId" id="hiddenProductId">

                    <div class="form-group">
                        <label>Tên nhà cung cấp:</label>
                        <select name="supplierName" id="supplierName">
                            <option value="">-- Chọn nhà cung cấp --</option>
                            <c:forEach var="name" items="${supplierNames}">
                                <option value="${name}">${name}</option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="form-group">
                        <label>Tên sản phẩm:</label>
                        <select name="productName" id="productName" required>
                            <option value="">-- Chọn sản phẩm --</option>
                            <c:forEach var="name" items="${productNames}">
                                <option value="${name}">${name}</option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="form-group">
                        <label>Thời gian giao hàng (ngày):</label>
                        <input type="number" name="deliveryDuration" id="editDeliveryDuration" required>
                    </div>

                    <div class="form-group">
                        <label>Giá ước tính:</label>
                        <input type="number" step="0.01" name="estimatedPrice" id="editEstimatedPrice" required>
                    </div>

                    <div class="form-group">
                        <label>Chính sách:</label>
                        <textarea name="policies" id="editPolicies" rows="3"></textarea>
                    </div>

                    <div class="form-group">
                        <label>Trạng thái:</label>
                        <select name="isActive" id="editIsActive">
                            <option value="true">Hoạt động</option>
                            <option value="false">Ngừng</option>
                        </select>
                    </div>

                    <div class="form-actions">
                        <button type="submit" class="btn btn-save" id="saveButton">Lưu</button>
                        <button type="button" class="btn btn-cancel" onclick="closeSupplierProductModal()">Hủy</button>
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
            // ========== MODAL THÊM / SỬA SUPPLIER-PRODUCT ==========
            function openSupplierProductModal() {
                document.getElementById("supplierProductModal").style.display = "block";
                document.getElementById("modalTitle").innerText = "Thêm Supplier-Product";
                document.getElementById("formAction").value = "add";
                document.getElementById("saveButton").innerText = "Lưu";

                // Enable dropdowns khi thêm mới
                document.getElementById("supplierName").disabled = false;
                document.getElementById("productName").disabled = false;

                // reset form
                document.querySelector("#supplierProductModal form").reset();
            }

            function closeSupplierProductModal() {
                const modal = document.getElementById('supplierProductModal');
                modal.style.display = 'none';
                const form = modal.querySelector('form');
                form.reset();

                // Enable lại dropdowns khi đóng modal
                document.getElementById("supplierName").disabled = false;
                document.getElementById("productName").disabled = false;
            }

            // Hàm mới: lấy dữ liệu từ data attributes của button
            function editSupplierProductFromButton(button) {
                const supplierId = button.getAttribute('data-supplier-id');
                const productId = button.getAttribute('data-product-id');
                const supplierName = button.getAttribute('data-supplier-name');
                const productName = button.getAttribute('data-product-name');
                const deliveryDuration = button.getAttribute('data-delivery-duration');
                const estimatedPrice = button.getAttribute('data-estimated-price');
                const policies = button.getAttribute('data-policies');
                const isActive = button.getAttribute('data-is-active');

                editSupplierProduct(supplierId, productId, supplierName, productName, deliveryDuration, estimatedPrice, policies, isActive);
            }

            function editSupplierProduct(supplierId, productId, supplierName, productName, deliveryDuration, estimatedPrice, policies, isActive) {
                document.getElementById("modalTitle").innerText = "Cập nhật Supplier-Product";
                document.getElementById("formAction").value = "update";
                document.getElementById("hiddenSupplierId").value = supplierId;
                document.getElementById("hiddenProductId").value = productId;

                // Set giá trị cho dropdowns (cho phép thay đổi)
                document.getElementById("supplierName").value = supplierName;
                document.getElementById("productName").value = productName;
                // Cho phép chỉnh sửa Supplier và Product
                document.getElementById("supplierName").disabled = false;
                document.getElementById("productName").disabled = false;
                document.getElementById("editDeliveryDuration").value = deliveryDuration;
                document.getElementById("editEstimatedPrice").value = estimatedPrice;
                document.getElementById("editPolicies").value = policies || '';
                document.getElementById("editIsActive").value = isActive === 'true' ? 'true' : 'false';

                document.getElementById("supplierProductModal").style.display = "block";
            }

            // ========== ĐÓNG MODAL KHI CLICK RA NGOÀI ==========
            window.onclick = function (event) {
                const modal = document.getElementById('supplierProductModal');
                if (event.target === modal) {
                    modal.style.display = 'none';
                }
            };

            // ========== LỌC THEO TÊN HOẶC TRẠNG THÁI ==========
            const searchInput = document.getElementById("searchInput");
            const statusFilter = document.getElementById("statusFilter");
            const tableRows = document.querySelectorAll("table tbody tr");

            function filterTable() {
                const searchText = searchInput.value.toLowerCase();
                const selectedStatus = statusFilter.value; // "", "true", "false"

                tableRows.forEach(row => {
                    const nameCell = row.querySelector("td:nth-child(2)"); // Tên Supplier
                    const statusCell = row.querySelector("td:nth-child(7)"); // Trạng thái
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

            // ========== RELOAD TRANG ==========
            function reloadSupplierProduct() {
                window.location.href = 'supplierproduct';
            }
        </script>

    </body>
</html>
