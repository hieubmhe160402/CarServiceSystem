<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Quản lý Sản phẩm (Phụ tùng)</title>
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
                min-height: 100vh;
            }

            /* Sidebar */
            .sidebar {
                width: 260px;
                height: 100vh;
                background: linear-gradient(180deg,#0f2340,#0b1830);
                color: #fff;
                display: flex;
                flex-direction: column;
                padding: 28px 18px;
                box-shadow: 4px 0 12px rgba(0, 0, 0, 0.1);
            }

            .sidebar .nav {
                margin-top: 12px;
                display: flex;
                flex-direction: column;
                gap: 8px;
            }

            .sidebar .nav a {
                color: rgba(255,255,255,0.9);
                text-decoration: none;
                padding: 10px 12px;
                border-radius: 10px;
                display: flex;
                align-items: center;
                gap: 12px;
                transition: all 0.3s ease;
            }

            .sidebar .nav a:hover,
            .sidebar .nav a.active {
                background: rgba(255,255,255,0.15);
                transform: translateX(4px);
            }

            /* Main content */
            .main {
                flex: 1;
                padding: 20px;
            }

            .container {
                background: white;
                padding: 20px;
                border-radius: 8px;
                margin: auto;
            }

            h2 {
                margin-bottom: 20px;
                color: #333;
            }

            .top-bar {
                display: flex;
                justify-content: space-between;
                align-items: center;
                margin-bottom: 20px;
                flex-wrap: wrap;
                gap: 10px;
            }

            .search-filter-group {
                display: flex;
                gap: 10px;
                flex-wrap: wrap;
            }

            .search-box,
            .filter-select {
                padding: 8px 12px;
                border: 1px solid #d1d5db;
                border-radius: 6px;
                font-size: 14px;
                transition: border-color 0.2s, box-shadow 0.2s;
            }

            .search-box {
                width: 280px;
            }

            .filter-select {
                width: 200px;
            }

            .search-box:focus,
            .filter-select:focus {
                outline: none;
                border-color: #2563eb;
                box-shadow: 0 0 0 2px rgba(37,99,235,0.2);
            }

            /* Buttons */
            .btn {
                border: none;
                border-radius: 6px;
                cursor: pointer;
                font-size: 14px;
                padding: 8px 16px;
                font-weight: 500;
                transition: all 0.25s;
            }

            .btn-add,
            .btn-save {
                background: #16a34a;
                color: #fff;
            }
            .btn-add:hover,
            .btn-save:hover {
                background: #15803d;
            }

            .btn-edit {
                background: #3b82f6;
                color: #fff;
                font-size: 13px;
                padding: 6px 12px;
                margin-right: 4px;
            }
            .btn-edit:hover {
                background: #2563eb;
            }

            .btn-delete {
                background: #ef4444;
                color: #fff;
                font-size: 13px;
                padding: 6px 12px;
            }
            .btn-delete:hover {
                background: #dc2626;
            }

            .btn-cancel {
                background: #6c757d;
                color: #fff;
            }
            .btn-cancel:hover {
                background: #565f67;
            }

            /* Table */
            table {
                width: 100%;
                border-collapse: collapse;
                margin-top: 20px;
            }

            th, td {
                padding: 12px;
                text-align: left;
                border-bottom: 1px solid #ddd;
            }

            th {
                background: #f8f9fa;
                font-weight: 600;
                color: #333;
                font-size: 14px;
            }

            tr:hover {
                background: #f8f9fa;
            }

            .product-image {
                width: 50px;
                height: 50px;
                object-fit: cover;
                border-radius: 4px;
            }

            .price-cell {
                font-weight: 600;
                color: #28a745;
            }

            .status-badge {
                padding: 4px 10px;
                border-radius: 12px;
                font-size: 12px;
                font-weight: 500;
            }

            .status-active {
                background: #d4edda;
                color: #155724;
            }
            .status-inactive {
                background: #f8d7da;
                color: #721c24;
            }

            .description-cell {
                max-width: 250px;
                overflow: hidden;
                text-overflow: ellipsis;
                white-space: nowrap;
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
                padding: 10px 15px;
                text-decoration: none;
                border: 1px solid #ddd;
                color: #333;
                border-radius: 4px;
                transition: all 0.3s;
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
                pointer-events: none;
                background: #f8f9fa;
            }

            /* Modal */
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
                margin: 3% auto;
                padding: 30px;
                width: 700px;
                max-width: 90%;
                border-radius: 8px;
                position: relative;
            }

            .close {
                position: absolute;
                right: 15px;
                top: 10px;
                font-size: 28px;
                cursor: pointer;
                color: #aaa;
            }

            .close:hover {
                color: #000;
            }

            .form-row {
                display: grid;
                grid-template-columns: 1fr 1fr;
                gap: 15px;
                margin-bottom: 15px;
            }

            .form-group {
                margin-bottom: 15px;
            }

            .form-group.full-width {
                grid-column: 1 / -1;
            }

            .form-group label {
                display: block;
                margin-bottom: 5px;
                font-weight: 600;
                color: #333;
            }

            .form-group input,
            .form-group select,
            .form-group textarea {
                width: 100%;
                padding: 8px;
                border: 1px solid #ddd;
                border-radius: 4px;
                font-size: 14px;
            }

            .form-group textarea {
                resize: vertical;
                min-height: 80px;
            }

            .form-group input:focus,
            .form-group select:focus,
            .form-group textarea:focus {
                outline: none;
                border-color: #007bff;
                box-shadow: 0 0 0 3px rgba(0,123,255,0.1);
            }

            .form-actions {
                display: flex;
                gap: 10px;
                justify-content: flex-end;
                margin-top: 20px;
                padding-top: 20px;
                border-top: 1px solid #eee;
            }

            /* Delete modal */
            .modal-delete {
                background: white;
                margin: 15% auto;
                padding: 30px;
                width: 400px;
                max-width: 90%;
                border-radius: 8px;
                text-align: center;
            }

            .modal-delete .icon-warning {
                font-size: 60px;
                color: #dc3545;
                margin-bottom: 20px;
            }

            .modal-delete h3 {
                margin-bottom: 15px;
                color: #333;
            }
            .modal-delete p {
                margin-bottom: 25px;
                color: #666;
                line-height: 1.5;
            }

            .modal-delete .btn-group {
                display: flex;
                gap: 10px;
                justify-content: center;
            }

            /* Misc */
            .required {
                color: red;
            }

            .form-hint {
                font-size: 12px;
                color: #6c757d;
                margin-top: 5px;
            }

            .image-preview {
                margin-top: 10px;
                max-width: 200px;
                max-height: 200px;
                display: none;
                border-radius: 4px;
                border: 1px solid #ddd;
            }
        </style>

    </head>
    <body>
        <div class="app">
            <!-- 🧩 Sidebar include -->
            <jsp:include page="/view/layout/sidebar.jsp"/>
            <div class="main">
                <div class="container">
                    <h2>Quản lý Sản phẩm (Phụ tùng)</h2>

                    <!-- Thông báo -->
                    <c:if test="${not empty sessionScope.success}">
                        <div style="padding: 15px; background: #d4edda; color: #155724; border: 1px solid #c3e6cb; border-radius: 4px; margin-bottom: 20px;">
                            ${sessionScope.success}
                        </div>
                        <c:remove var="success" scope="session" />
                    </c:if>

                    <c:if test="${not empty sessionScope.error}">
                        <div style="padding: 15px; background: #f8d7da; color: #721c24; border: 1px solid #f5c6cb; border-radius: 4px; margin-bottom: 20px;">
                            ${sessionScope.error}
                        </div>
                        <c:remove var="error" scope="session" />
                    </c:if>

                    <div class="top-bar">
                        <div class="search-filter-group">
                            <input type="text" class="search-box" placeholder="Tìm kiếm theo tên, mã..." id="searchInput">
                            <select class="filter-select" id="filterCategory" onchange="applyFilter()">
                                <option value="">-- Tất cả danh mục --</option>
                                <c:forEach items="${categoryList}" var="cat">
                                    <option value="${cat.categoryId}" ${filterCategory==cat.categoryId ? 'selected' : ''}>${cat.name}</option>
                                </c:forEach>
                            </select>
                            <select class="filter-select" id="filterStatus" onchange="applyFilter()">
                                <option value="">-- Tất cả trạng thái --</option>
                                <option value="1" ${filterStatus=='1' ? 'selected' : ''}>Đang kinh doanh</option>
                                <option value="0" ${filterStatus=='0' ? 'selected' : ''}>Ngưng kinh doanh</option>
                            </select>
                        </div>
                        <button class="btn btn-add" onclick="openModal()">+ Thêm Sản phẩm</button>
                    </div>

                    <table>
                        <thead>
                            <tr>
                                <th style="width: 8%;">Mã SP</th>
                                <th style="width: 6%;">Ảnh</th>
                                <th style="width: 18%;">Tên sản phẩm</th>
                                <th style="width: 12%;">Danh mục</th>
                                <th style="width: 10%;">Giá (VNĐ)</th>
                                <th style="width: 8%;">Đơn vị</th>
                                <th style="width: 8%;">Tồn kho tối thiểu</th>
                                <th style="width: 8%;">Bảo hành (tháng)</th>
                                <th style="width: 8%;">Trạng thái</th>
                                <th style="width: 14%;">Thao tác</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${productList}" var="prod">
                                <tr>
                                    <td>${prod.code}</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${not empty prod.image}">
                                                <img src="${prod.image}" class="product-image" alt="${prod.name}">
                                            </c:when>
                                            <c:otherwise>
                                                <img src="images/no-image.png" class="product-image" alt="No image">
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td title="${prod.name}">${prod.name}</td>
                                    <td>${prod.category.name}</td>
                                    <td class="price-cell">${prod.price}</td>
                                    <td>${prod.unit.name}</td>
                                    <td>${prod.minStockLevel}</td>
                                    <td>${prod.warrantyPeriodMonths}</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${prod.isActive}">
                                                <span class="status-badge status-active">Đang KD</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="status-badge status-inactive">Ngưng KD</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <button class="btn btn-edit" onclick='editProduct(${prod.productId}, "${prod.code}", "${prod.name}", ${prod.price}, "${prod.description}", "${prod.image}", ${prod.unit.unitId}, ${prod.category.categoryId}, ${prod.warrantyPeriodMonths}, ${prod.minStockLevel}, ${prod.isActive})'>Sửa</button>
                                        <button class="btn btn-delete" onclick="deleteProduct(${prod.productId}, '${prod.name}')">Xóa</button>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>

                    <!-- Pagination -->
                    <div class="pagination">
                        <c:choose>
                            <c:when test="${currentPage > 1}">
                                <a href="products?page=${currentPage - 1}&category=${filterCategory}&status=${filterStatus}">‹ Previous</a>
                            </c:when>
                            <c:otherwise>
                                <span class="disabled">‹ Previous</span>
                            </c:otherwise>
                        </c:choose>

                        <c:forEach begin="1" end="${totalPages > 5 ? 5 : totalPages}" var="i">
                            <c:choose>
                                <c:when test="${currentPage == i}">
                                    <span class="active">${i}</span>
                                </c:when>
                                <c:otherwise>
                                    <a href="products?page=${i}&category=${filterCategory}&status=${filterStatus}">${i}</a>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>

                        <c:choose>
                            <c:when test="${currentPage < totalPages}">
                                <a href="products?page=${currentPage + 1}&category=${filterCategory}&status=${filterStatus}">Next ›</a>
                            </c:when>
                            <c:otherwise>
                                <span class="disabled">Next ›</span>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>
        </div>

        <!-- Modal Form -->
        <div id="productModal" class="modal">
            <div class="modal-content">
                <span class="close" onclick="closeModal()">&times;</span>
                <h3 id="modalTitle">Thêm Sản phẩm mới</h3>
                <form id="productForm" method="post" action="products" enctype="multipart/form-data">
                    <input type="hidden" id="productID" name="productID">
                    <input type="hidden" id="action" name="action" value="add">
                    <input type="hidden" name="type" value="PART">
                    <input type="hidden" name="oldImage" value="${product.image}">

                    <div class="form-row">
                        <div class="form-group">
                            <label>Mã sản phẩm <span class="required">*</span></label>
                            <input type="text" id="code" name="code" required placeholder="VD: PRT001">
                            <div class="form-hint">Mã duy nhất để định danh sản phẩm</div>
                        </div>

                        <div class="form-group">
                            <label>Tên sản phẩm <span class="required">*</span></label>
                            <input type="text" id="name" name="name" required placeholder="VD: Dầu nhớt 5W30">
                        </div>
                    </div>

                    <div class="form-row">
                        <div class="form-group">
                            <label>Danh mục <span class="required">*</span></label>
                            <select id="categoryId" name="categoryId" required>
                                <option value="">-- Chọn danh mục --</option>
                                <c:forEach items="${categoryList}" var="cat">
                                    <option value="${cat.categoryId}">${cat.name}</option>
                                </c:forEach>
                            </select>
                        </div>

                        <div class="form-group">
                            <label>Đơn vị tính <span class="required">*</span></label>
                            <select id="unitId" name="unitId" required>
                                <option value="">-- Chọn đơn vị --</option>
                                <c:forEach items="${unitList}" var="unit">
                                    <option value="${unit.unitId}">${unit.name}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>

                    <div class="form-row">
                        <div class="form-group">
                            <label>Giá (VNĐ) <span class="required">*</span></label>
                            <input type="number" id="price" name="price" required min="0" step="1000" placeholder="0">
                        </div>

                        <div class="form-group">
                            <label>Bảo hành (tháng)</label>
                            <input type="number" id="warrantyPeriodMonths" name="warrantyPeriodMonths" min="0" value="0" placeholder="0">
                        </div>
                    </div>

                    <div class="form-row">
                        <div class="form-group">
                            <label>Tồn kho tối thiểu</label>
                            <input type="number" id="minStockLevel" name="minStockLevel" min="0" value="0" placeholder="0">
                            <div class="form-hint">Mức tồn kho cảnh báo cần nhập thêm</div>
                        </div>

                        <div class="form-group">
                            <label>Trạng thái</label>
                            <select id="isActive" name="isActive">
                                <option value="1">Đang kinh doanh</option>
                                <option value="0">Ngưng kinh doanh</option>
                            </select>
                        </div>
                    </div>

                    <div class="form-group full-width">
                        <label>Hình ảnh</label>
                        <input type="file" id="imageFile" name="imageFile" accept="image/*" onchange="previewImage(this)">
                        <input type="hidden" id="image" name="image">
                        <img id="imagePreview" class="image-preview" alt="Preview">
                        <div class="form-hint">Định dạng: JPG, PNG, GIF (Tối đa 5MB)</div>
                    </div>

                    <div class="form-group full-width">
                        <label>Mô tả sản phẩm</label>
                        <textarea id="description" name="description" rows="4" placeholder="Nhập mô tả chi tiết về sản phẩm..."></textarea>
                    </div>

                    <div class="form-actions">
                        <button type="button" class="btn btn-cancel" onclick="closeModal()">Hủy</button>
                        <button type="submit" class="btn btn-save">Lưu</button>
                    </div>
                </form>
            </div>
        </div>

        <!-- Delete Modal -->
        <div id="deleteModal" class="modal">
            <div class="modal-delete">
                <div class="icon-warning">⚠️</div>
                <h3>Xác nhận xóa</h3>
                <p id="deleteMessage">Bạn có chắc chắn muốn xóa sản phẩm này không?<br>Hành động này không thể hoàn tác!</p>
                <div class="btn-group">
                    <button type="button" class="btn btn-cancel" onclick="closeDeleteModal()">Hủy</button>
                    <button type="button" class="btn btn-delete" onclick="confirmDelete()">Xóa</button>
                </div>
            </div>
        </div>

        <script>
            let deleteId = null;
            let deleteName = '';

            function applyFilter() {
                let category = document.getElementById('filterCategory').value;
                let status = document.getElementById('filterStatus').value;
                window.location.href = 'products?page=1&category=' + category + '&status=' + status;
            }

            function openModal() {
                document.getElementById('productModal').style.display = 'block';
                document.getElementById('modalTitle').textContent = 'Thêm Sản phẩm mới';
                document.getElementById('productForm').reset();
                document.getElementById('action').value = 'add';
                document.getElementById('productID').value = '';
                document.getElementById('imagePreview').style.display = 'none';
            }

            function closeModal() {
                document.getElementById('productModal').style.display = 'none';
            }

            function editProduct(id, code, name, price, desc, image, unitId, categoryId, warranty, minStock, isActive) {
                document.getElementById('productModal').style.display = 'block';
                document.getElementById('modalTitle').textContent = 'Sửa Sản phẩm';
                document.getElementById('action').value = 'edit';
                document.getElementById('productID').value = id;
                document.getElementById('code').value = code;
                document.getElementById('name').value = name;
                document.getElementById('price').value = price;
                document.getElementById('description').value = desc || '';
                document.getElementById('image').value = image || '';
                document.getElementById('unitId').value = unitId;
                document.getElementById('categoryId').value = categoryId;
                document.getElementById('warrantyPeriodMonths').value = warranty;
                document.getElementById('minStockLevel').value = minStock;
                document.getElementById('isActive').value = isActive ? '1' : '0';

                if (image) {
                    document.getElementById('imagePreview').src = image;
                    document.getElementById('imagePreview').style.display = 'block';
                }
            }

            function deleteProduct(id, name) {
                deleteId = id;
                deleteName = name;
                document.getElementById('deleteMessage').innerHTML =
                        'Bạn có chắc chắn muốn xóa sản phẩm <strong>"' + name + '"</strong> không?<br>Hành động này không thể hoàn tác!';
                document.getElementById('deleteModal').style.display = 'block';
            }

            function closeDeleteModal() {
                document.getElementById('deleteModal').style.display = 'none';
                deleteId = null;
                deleteName = '';
            }

            function confirmDelete() {
                if (deleteId) {
                    window.location.href = 'delete-product?id=' + deleteId;
                }
            }

            function previewImage(input) {
                if (input.files && input.files[0]) {
                    var reader = new FileReader();
                    reader.onload = function (e) {
                        document.getElementById('imagePreview').src = e.target.result;
                        document.getElementById('imagePreview').style.display = 'block';
                    }
                    reader.readAsDataURL(input.files[0]);
                }
            }

            window.onclick = function (event) {
                var productModal = document.getElementById('productModal');
                var deleteModal = document.getElementById('deleteModal');

                if (event.target == productModal) {
                    closeModal();
                }
                if (event.target == deleteModal) {
                    closeDeleteModal();
                }
            }

            document.getElementById('searchInput').addEventListener('keyup', function () {
                var filter = this.value.toUpperCase();
                var rows = document.querySelectorAll('tbody tr');
                rows.forEach(function (row) {
                    var code = row.cells[0].textContent;
                    var name = row.cells[2].textContent;
                    var searchText = (code + ' ' + name).toUpperCase();
                    row.style.display = searchText.indexOf(filter) > -1 ? '' : 'none';
                });
            });
        </script>
    </body>
</html>