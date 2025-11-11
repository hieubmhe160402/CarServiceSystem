<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8" />
        <title>Quản lý Chi tiết Gói bảo dưỡng</title>
        <style>
            /* --- Reset & Base --- */
            * {
                box-sizing: border-box;
                margin: 0;
                padding: 0;
            }
            html, body {
                height: 100%;
                font-family: Inter, "Segoe UI", Roboto, Arial, sans-serif;
                background: #f5f7fb;
                color: #111827;
            }
            .app {
                display: flex;
                height: 100vh;
            }

            /* --- Sidebar giữ nguyên --- */
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
            h2 {
                margin-bottom: 20px;
                font-size: 20px;
                font-weight: 700;
            }

            /* --- Table --- */
            table {
                width: 100%;
                border-collapse: collapse;
                background: #fff;
                border-radius: 8px;
                overflow: hidden;
                box-shadow: 0 2px 6px rgba(0,0,0,0.05);
            }
            thead th {
                background: #f8fafc;
                padding: 12px 16px;
                text-align: left;
                border-bottom: 1px solid #e5e7eb;
            }
            tbody td {
                padding: 12px 16px;
                border-bottom: 1px solid #f1f5f9;
            }

            /* --- Buttons --- */
            .btn {
                border: none;
                border-radius: 6px;
                font-size: 14px;
                cursor: pointer;
                font-weight: 500;
                padding: 8px 16px;
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
                color: #fff;
                padding: 6px 12px;
                margin-right: 5px;
            }
            .btn-edit:hover {
                background: #2563eb;
            }
            .btn-delete {
                background: #ef4444;
                color: #fff;
                padding: 6px 12px;
            }
            .btn-delete:hover {
                background: #dc2626;
            }
            .btn-active {
                background: #3b82f6;
                color: #fff;
            }
            .btn-active:hover {
                background: #2563eb;
            }

            /* --- Search & Filter --- */
            .search-bar {
                display: flex;
                justify-content: space-between;
                align-items: center;
                margin-bottom: 20px;
                flex-wrap: wrap;
                gap: 12px;
            }
            .search-form {
                display: flex;
                align-items: center;
                gap: 10px;
                flex-wrap: wrap;
            }
            .search-form input,
            .search-form select {
                padding: 8px 12px;
                border: 1px solid #d1d5db;
                border-radius: 6px;
                font-size: 14px;
                transition: border-color 0.2s, box-shadow 0.2s;
            }
            .search-form input:focus,
            .search-form select:focus {
                outline: none;
                border-color: #2563eb;
                box-shadow: 0 0 0 2px rgba(37,99,235,0.2);
            }
            .search-form button[type="submit"] {
                background: #3b82f6;
                color: #fff;
            }
            .search-form button[type="submit"]:hover {
                background: #2563eb;
            }
            .search-form button[type="button"] {
                background: #0ea5e9;
                color: #fff;
            }
            .search-form button[type="button"]:hover {
                background: #0284c7;
            }

            /* --- Modal --- */
            .modal {
                display: none;
                position: fixed;
                top: 0;
                left: 0;
                width: 100%;
                height: 100%;
                background: rgba(0,0,0,0.5);
                justify-content: center;
                align-items: center;
                z-index: 999;
            }
            .modal-content {
                background: #fff;
                padding: 20px;
                border-radius: 8px;
                width: 500px;
                animation: fadeIn 0.2s ease-in-out;
            }
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
            .modal-content h3 {
                margin-bottom: 16px;
                font-size: 18px;
                font-weight: 600;
            }
            .form-group {
                display: flex;
                flex-direction: column;
                margin-bottom: 12px;
            }
            .form-group label {
                font-weight: 600;
                margin-bottom: 5px;
            }
            .form-group input,
            .form-group select,
            .form-group textarea {
                padding: 8px 10px;
                border: 1px solid #ccc;
                border-radius: 5px;
            }

            /* Pagination */
            .pagination {
                display: flex;
                justify-content: center;
                align-items: center;
                margin-top: 30px;
                gap: 5px;
            }
            .pagination a, .pagination span {
                padding: 8px 12px;
                text-decoration: none;
                border: 1px solid #ddd;
                color: #333;
                border-radius: 4px;
                transition: 0.2s;
                font-size: 14px;
            }
            .pagination a:hover {
                background: #007bff;
                color: white;
                border-color: #007bff;
            }
            .pagination .active {
                background: #007bff;
                color: white;
                border-color: #007bff;
                font-weight: bold;
            }
            .pagination .disabled {
                color: #ccc;
                cursor: not-allowed;
                background: #f8f9fa;
            }
        </style>
    </head>
    <body>
        <div class="app">
            <jsp:include page="/view/layout/sidebar.jsp"/>

            <main class="main">
                <h2>Quản lý Chi tiết Gói bảo dưỡng</h2>
                <c:if test="${not empty sessionScope.success}">
                    <div style="background:#d4edda;color:#155724;padding:10px 16px;border-radius:6px;margin-bottom:15px;">
                        ${sessionScope.success}
                    </div>
                    <c:remove var="success" scope="session"/>
                </c:if>

                <c:if test="${not empty sessionScope.error}">
                    <div style="background:#f8d7da;color:#721c24;padding:10px 16px;border-radius:6px;margin-bottom:15px;">
                        ${sessionScope.error}
                    </div>
                    <c:remove var="error" scope="session"/>
                </c:if>

                <!-- Search & Add -->
                <div class="search-bar">
                    <form class="search-form" action="managerPackage" method="get">
                        <label>Tên sản phẩm:</label>
                        <input type="text" name="productName" placeholder="Nhập tên sản phẩm..." value="${param.productName}" />
                        <label>Trạng thái:</label>
                        <select name="status">
                            <option value="" ${empty param.status ? "selected" : ""}>Tất cả</option>
                            <option value="1" ${param.status == "1" ? "selected" : ""}>Hoạt động</option>
                            <option value="0" ${param.status == "0" ? "selected" : ""}>Không hoạt động</option>
                        </select>
                        <button type="submit" class="btn btn-active">Tìm</button>
                        <button type="button" class="btn btn-active" onclick="window.location.href = 'managerPackage'">Tải lại</button>
                    </form>
                    <button class="btn btn-add" onclick="document.getElementById('addModal').style.display = 'flex'">+ Thêm mới</button>
                </div>

                <!-- Table -->
                <table>
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Mã Gói</th>
                            <th>Tên Sản phẩm</th>
                            <th>Số lượng</th>
                            <th>Trạng thái</th>
                            <th>Thứ tự</th>
                            <th>Ghi chú</th>
                            <th>Hành động</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:choose>
                            <c:when test="${not empty details}">
                                <c:forEach var="d" items="${details}">
                                    <tr>
                                        <td>${d.packageDetailId}</td>
                                        <td>${d.maintenancePackage.packageCode}</td>
                                        <td>${d.product.name}</td>
                                        <td>${d.quantity}</td>
                                        <td>
                                            <c:if test="${d.isRequired}">Hoạt động</c:if>
                                            <c:if test="${not d.isRequired}">Không hoạt động</c:if>
                                            </td>
                                            <td>${d.displayOrder}</td>
                                        <td>${d.notes}</td>
                                        <td>
                                            <c:if test="${d.isRequired}">
                                                <!-- ✅ Sửa đoạn này -->
                                                <button class="btn btn-edit"
                                                        data-id="${d.packageDetailId}"
                                                        data-product="${d.product.productId}"
                                                        data-quantity="${d.quantity}"
                                                        data-displayorder="${d.displayOrder}"
                                                        data-notes="${fn:escapeXml(d.notes)}"
                                                        onclick="openEditModalFromButton(this)">
                                                    Sửa
                                                </button>
                                            </c:if>

                                            <form action="managerPackage" method="post" style="display:inline;">
                                                <input type="hidden" name="action" value="toggleStatus"/>
                                                <input type="hidden" name="id" value="${d.packageDetailId}"/>
                                                <button type="submit" class="btn ${d.isRequired ? 'btn-delete' : 'btn-add'}">
                                                    <c:choose>
                                                        <c:when test="${d.isRequired}">Tắt</c:when>
                                                        <c:otherwise>Bật</c:otherwise>
                                                    </c:choose>
                                                </button>
                                            </form>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <tr>
                                    <td colspan="8" style="text-align:center;color:#999;">Không có dữ liệu phù hợp</td>
                                </tr>
                            </c:otherwise>
                        </c:choose>
                    </tbody>

                </table>

                <!-- Pagination -->
                <div class="pagination">
                    <c:choose>
                        <c:when test="${currentPage > 1}">
                            <a href="managerPackage?page=${currentPage - 1}&productName=${param.productName}&status=${param.status}">‹ Trước</a>
                        </c:when>
                        <c:otherwise><span class="disabled">‹ Trước</span></c:otherwise>
                    </c:choose>

                    <c:forEach begin="1" end="${totalPages}" var="i">
                        <c:choose>
                            <c:when test="${currentPage == i}"><span class="active">${i}</span></c:when>
                            <c:otherwise><a href="managerPackage?page=${i}&productName=${param.productName}&status=${param.status}">${i}</a></c:otherwise>
                        </c:choose>
                    </c:forEach>

                    <c:choose>
                        <c:when test="${currentPage < totalPages}">
                            <a href="managerPackage?page=${currentPage + 1}&productName=${param.productName}&status=${param.status}">Sau ›</a>
                        </c:when>
                        <c:otherwise><span class="disabled">Sau ›</span></c:otherwise>
                    </c:choose>
                </div>
            </main>
        </div>

        <!-- Modal Add -->
        <!-- Modal Add -->
        <div id="addModal" class="modal">
            <div class="modal-content">
                <h3>Thêm Chi tiết Gói bảo dưỡng</h3>
                <form action="managerPackage" method="post">
                    <input type="hidden" name="action" value="add" />

                    <!-- Mã gói -->
                    <div class="form-group">
                        <label>Mã gói:</label>
                        <select name="packageId" required>
                            <option value="">-- Chọn gói bảo dưỡng --</option>
                            <c:forEach var="pkg" items="${packages}">
                                <option value="${pkg.packageId}">${pkg.packageCode}</option>
                            </c:forEach>
                        </select>
                    </div>

                    <!-- Sản phẩm -->
                    <div class="form-group">
                        <label>Sản phẩm:</label>
                        <select name="productId" required>
                            <option value="">-- Chọn sản phẩm --</option>
                            <c:forEach var="p" items="${products}">
                                <option value="${p.productId}">${p.name}</option>
                            </c:forEach>
                        </select>
                    </div>

                    <!-- Số lượng -->
                    <div class="form-group">
                        <label>Số lượng:</label>
                        <input type="number" name="quantity" step="0.01" required>
                    </div>

                    <!-- Thứ tự -->
                    <div class="form-group">
                        <label>Thứ tự hiển thị:</label>
                        <input type="number" name="displayOrder" required>
                    </div>

                    <!-- Ghi chú -->
                    <div class="form-group">
                        <label>Ghi chú:</label>
                        <textarea name="notes" rows="3"></textarea>
                    </div>

                    <div style="display:flex;justify-content:flex-end;gap:10px;">
                        <button type="submit" class="btn btn-add">Lưu</button>
                        <button type="button" class="btn btn-delete" onclick="document.getElementById('addModal').style.display = 'none'">Hủy</button>
                    </div>
                </form>
            </div>
        </div>

        <!-- Modal Edit -->
        <div id="editModal" class="modal">
            <div class="modal-content">
                <h3>Cập nhật Chi tiết</h3>
                <form action="managerPackage" method="post">
                    <input type="hidden" name="action" value="update">
                    <input type="hidden" name="id" id="editId">

                    <div class="form-group">
                        <label>Sản phẩm:</label>
                        <select name="productId" id="editProduct">
                            <c:forEach var="p" items="${products}">
                                <option value="${p.productId}">${p.name}</option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="form-group">
                        <label>Số lượng:</label>
                        <input type="number" id="editQuantity" name="quantity" step="0.01" required>
                    </div>

                    <div class="form-group">
                        <label>Thứ tự hiển thị:</label>
                        <input type="number" id="editDisplayOrder" name="displayOrder" required>
                    </div>

                    <div class="form-group">
                        <label>Ghi chú:</label>
                        <textarea id="editNotes" name="notes" rows="3"></textarea>
                    </div>

                    <div style="display:flex;justify-content:flex-end;gap:10px;">
                        <button type="submit" class="btn btn-edit">Lưu</button>
                        <button type="button" class="btn btn-delete" onclick="document.getElementById('editModal').style.display = 'none'">Đóng</button>
                    </div>
                </form>
            </div>
        </div>

        <script>
            function openEditModalFromButton(btn) {
                const id = btn.dataset.id;
                const productId = btn.dataset.product;
                const quantity = btn.dataset.quantity;
                const displayOrder = btn.dataset.displayorder;
                const notes = btn.dataset.notes;

                document.getElementById('editId').value = id;

                const productSelect = document.getElementById('editProduct');
                if ([...productSelect.options].some(opt => opt.value === productId)) {
                    productSelect.value = productId;
                }

                document.getElementById('editQuantity').value = quantity;
                document.getElementById('editDisplayOrder').value = displayOrder;
                document.getElementById('editNotes').value = notes || '';

                document.getElementById('editModal').style.display = 'flex';
            }
        </script>
    </body>
</html>
