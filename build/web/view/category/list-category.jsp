<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Quản lý Category</title>
    <style>
        * { margin: 0; padding: 0; box-sizing: border-box; }

        body {
            font-family: Arial, sans-serif;
            background: #f5f5f5;
        }

        .app {
            display: flex;
            min-height: 100vh;
        }

        /* Sidebar included */
        .sidebar {
            width: 240px;
            background-color: #1e293b;
            color: #fff;
            display: flex;
            flex-direction: column;
            padding: 20px;
        }

        .sidebar h2 {
            font-size: 20px;
            margin-bottom: 20px;
            text-align: center;
        }

        .sidebar a {
            color: #cbd5e1;
            text-decoration: none;
            padding: 10px 12px;
            border-radius: 8px;
            margin-bottom: 8px;
            display: block;
            transition: background 0.2s, color 0.2s;
        }

        .sidebar a:hover,
        .sidebar a.active {
            background: #334155;
            color: #fff;
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
        }

        .btn {
            padding: 10px 20px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 14px;
        }
        .btn-add { background: #28a745; color: white; }
        .btn-edit { background: #007bff; color: white; margin-right: 5px; }
        .btn-delete { background: #dc3545; color: white; }
        .btn-cancel { background: #6c757d; color: white; }
        .btn-save { background: #28a745; color: white; }

        .btn-add:hover { background: #218838; }

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
        }

        tr:hover { background: #f8f9fa; }

        .description-cell {
            max-width: 300px;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
        }

        /* Pagination */
        .pagination {
            display: flex;
            justify-content: flex-end;
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
            left: 0; top: 0;
            width: 100%; height: 100%;
            background: rgba(0,0,0,0.5);
        }

        .modal-content {
            background: white;
            margin: 5% auto;
            padding: 30px;
            width: 500px;
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

        .close:hover { color: #000; }

        .form-group { margin-bottom: 15px; }
        .form-group label {
            display: block;
            margin-bottom: 5px;
            font-weight: 600;
            color: #333;
        }

        .form-group input, .form-group select, .form-group textarea {
            width: 100%;
            padding: 8px;
            border: 1px solid #ddd;
            border-radius: 4px;
        }

        .form-group textarea {
            resize: vertical;
            min-height: 80px;
        }

        .form-actions {
            display: flex;
            gap: 10px;
            justify-content: flex-end;
            margin-top: 20px;
        }

        .search-box {
            padding: 8px;
            border: 1px solid #ddd;
            border-radius: 4px;
            width: 300px;
        }

        /* Delete Modal */
        .modal-delete {
            background: white;
            margin: 15% auto;
            padding: 30px;
            width: 400px;
            border-radius: 8px;
            position: relative;
            text-align: center;
        }

        .modal-delete .icon-warning {
            font-size: 60px;
            color: #dc3545;
            margin-bottom: 20px;
        }

        .modal-delete h3 { margin-bottom: 15px; color: #333; }
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
    </style>
</head>
<body>
    <div class="app">
        <!-- 🧩 Sidebar include -->
        <jsp:include page="/view/layout/sidebar.jsp"/>

        <!-- Main content -->
        <div class="main">
            <div class="container">
                <h2>Quản lý Category</h2>

                <div class="top-bar">
                    <div style="display: flex; gap: 10px;">
                        <input type="text" class="search-box" placeholder="Tìm kiếm theo tên..." id="searchInput">
                        <select class="search-box" id="filterType" style="width: 200px;" onchange="applyFilter()">
                            <option value="">-- All Types --</option>
                            <option value="PART" ${filterType=='PART' ? 'selected' : ''}>PART</option>
                            <option value="SERVICE" ${filterType=='SERVICE' ? 'selected' : ''}>SERVICE</option>
                        </select>
                    </div>
                    <button class="btn btn-add" onclick="openModal()">+ Thêm Category</button>
                </div>

                <table>
                    <thead>
                        <tr>
                            <th>CategoryID</th>
                            <th>Name</th>
                            <th>Type</th>
                            <th>Description</th>
                            <th>Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${categoryList}" var="cat">
                            <tr>
                                <td>${cat.categoryId}</td>
                                <td>${cat.name}</td>
                                <td>${cat.type}</td>
                                <td class="description-cell" title="${cat.description}">${cat.description}</td>
                                <td>
                                    <button class="btn btn-edit" onclick="editCategory(${cat.categoryId}, '${cat.name}', '${cat.type}', '${cat.description}')">Sửa</button>
                                    <button class="btn btn-delete" onclick="deleteCategory(${cat.categoryId})">Xóa</button>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>

                <!-- Pagination -->
                <div class="pagination">
                    <c:choose>
                        <c:when test="${currentPage > 1}">
                            <a href="category?page=${currentPage - 1}">&laquo; Prev</a>
                        </c:when>
                        <c:otherwise><span class="disabled">&laquo; Prev</span></c:otherwise>
                    </c:choose>

                    <c:forEach begin="1" end="${totalPages > 4 ? 4 : totalPages}" var="i">
                        <c:choose>
                            <c:when test="${currentPage == i}"><span class="active">${i}</span></c:when>
                            <c:otherwise><a href="category?page=${i}">${i}</a></c:otherwise>
                        </c:choose>
                    </c:forEach>

                    <c:choose>
                        <c:when test="${currentPage < totalPages}"><a href="category?page=${currentPage + 1}">Next &raquo;</a></c:when>
                        <c:otherwise><span class="disabled">Next &raquo;</span></c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </div>

    <!-- Modal -->
    <div id="categoryModal" class="modal">
        <div class="modal-content">
            <span class="close" onclick="closeModal()">&times;</span>
            <h3 id="modalTitle">Thêm Category</h3>
            <form id="categoryForm" method="post" action="category">
                <input type="hidden" id="categoryID" name="categoryID">
                <input type="hidden" id="action" name="action" value="add">

                <div class="form-group">
                    <label>Name *</label>
                    <input type="text" id="name" name="name" required>
                </div>

                <div class="form-group">
                    <label>Type *</label>
                    <select id="type" name="type" required>
                        <option value="PART">PART</option>
                        <option value="SERVICE">SERVICE</option>
                    </select>
                </div>

                <div class="form-group">
                    <label>Description</label>
                    <textarea id="description" name="description"></textarea>
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
            <p>Bạn có chắc chắn muốn xóa category này không?<br>Hành động này không thể hoàn tác!</p>
            <div class="btn-group">
                <button type="button" class="btn btn-cancel" onclick="closeDeleteModal()">Hủy</button>
                <button type="button" class="btn btn-delete" onclick="confirmDelete()">Xóa</button>
            </div>
        </div>
    </div>

    <script>
        let deleteId = null;

        function applyFilter() {
            let type = document.getElementById('filterType').value;
            window.location.href = 'category?page=1&type=' + type;
        }

        function openModal() {
            document.getElementById('categoryModal').style.display = 'block';
            document.getElementById('modalTitle').textContent = 'Thêm Category';
            document.getElementById('categoryForm').reset();
            document.getElementById('action').value = 'add';
            document.getElementById('categoryID').value = '';
        }

        function closeModal() { document.getElementById('categoryModal').style.display = 'none'; }

        function editCategory(id, name, type, desc) {
            document.getElementById('categoryModal').style.display = 'block';
            document.getElementById('modalTitle').textContent = 'Sửa Category';
            document.getElementById('action').value = 'edit';
            document.getElementById('categoryID').value = id;
            document.getElementById('name').value = name;
            document.getElementById('type').value = type;
            document.getElementById('description').value = desc;
        }

        function deleteCategory(id) {
            deleteId = id;
            document.getElementById('deleteModal').style.display = 'block';
        }

        function closeDeleteModal() {
            document.getElementById('deleteModal').style.display = 'none';
            deleteId = null;
        }

        function confirmDelete() {
            if (deleteId) {
                window.location.href = 'delete-category?id=' + deleteId;
            }
        }

        window.onclick = function (event) {
            const categoryModal = document.getElementById('categoryModal');
            const deleteModal = document.getElementById('deleteModal');
            if (event.target === categoryModal) closeModal();
            if (event.target === deleteModal) closeDeleteModal();
        };

        document.getElementById('searchInput').addEventListener('keyup', function () {
            const filter = this.value.toUpperCase();
            const rows = document.querySelectorAll('tbody tr');
            rows.forEach(row => {
                const name = row.cells[1].textContent;
                row.style.display = name.toUpperCase().includes(filter) ? '' : 'none';
            });
        });
    </script>
</body>
</html>
