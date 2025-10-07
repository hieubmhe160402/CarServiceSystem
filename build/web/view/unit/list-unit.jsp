<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Quản lý Unit</title>
        <style>
            * {
                margin: 0;
                padding: 0;
                box-sizing: border-box;
            }

            body {
                font-family: Arial, sans-serif;
                background: #f5f5f5;
            }

            .app {
                display: flex;
                min-height: 100vh;
            }

            /* Sidebar */
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
                border: 1px solid #ddd;
                border-radius: 4px;
                font-size: 14px;
            }

            .search-box {
                width: 250px;
            }

            .filter-select {
                width: 200px;
            }

            /* Buttons */
            .btn {
                padding: 8px 16px;
                border: none;
                border-radius: 4px;
                cursor: pointer;
                font-size: 14px;
                transition: background 0.3s;
            }

            .btn-add {
                background: #28a745;
                color: white;
            }
            .btn-add:hover {
                background: #218838;
            }

            .btn-edit {
                background: #007bff;
                color: white;
                font-size: 13px;
                padding: 6px 12px;
                margin-right: 4px;
            }
            .btn-edit:hover {
                background: #0056b3;
            }

            .btn-delete {
                background: #dc3545;
                color: white;
                font-size: 13px;
                padding: 6px 12px;
            }
            .btn-delete:hover {
                background: #c82333;
            }

            .btn-cancel {
                background: #6c757d;
                color: white;
            }
            .btn-cancel:hover {
                background: #545b62;
            }

            .btn-save {
                background: #28a745;
                color: white;
            }
            .btn-save:hover {
                background: #218838;
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

            td[title] {
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
                width: 500px;
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

            .form-group {
                margin-bottom: 15px;
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

            .required {
                color: red;
            }

            /* Responsive */
            @media (max-width: 768px) {
                .container {
                    padding: 15px;
                }

                table, thead, tbody, th, td, tr {
                    display: block;
                }

                thead {
                    display: none;
                }

                tr {
                    margin-bottom: 15px;
                    border: 1px solid #ddd;
                    border-radius: 8px;
                    background: #fff;
                    padding: 10px;
                }

                td {
                    border: none;
                    display: flex;
                    justify-content: space-between;
                    padding: 8px 10px;
                }

                td::before {
                    content: attr(data-label);
                    font-weight: bold;
                    color: #555;
                }
            }
        </style>

    </head>
    <body>
        <div class="app">
            <!-- 🧩 Sidebar include -->
            <jsp:include page="/view/layout/sidebar.jsp"/>
            <div class="main">
                <div class="container">
                    <h2>Quản lý Unit</h2>
                    <div class="top-bar">
                        <div style="display: flex; gap: 10px;">
                            <input type="text" class="search-box" placeholder="Tìm theo tên..." id="searchInput">
                            <select class="search-box" id="filterType" style="width: 200px;" onchange="applyFilter()">
                                <option value="">-- All Types --</option>
                                <option value="PART" ${filterType=='PART' ? 'selected' : ''}>PART</option>
                                <option value="SERVICE" ${filterType=='SERVICE' ? 'selected' : ''}>SERVICE</option>
                            </select>
                        </div>
                        <button class="btn btn-add" onclick="openModal()">+ Thêm Unit</button>
                    </div>

                    <table>
                        <thead>
                            <tr>
                                <th>UnitID</th>
                                <th>Name</th>
                                <th>Type</th>
                                <th>Description</th>
                                <th>Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${unitList}" var="u">
                                <tr>
                                    <td>${u.unitId}</td>
                                    <td>${u.name}</td>
                                    <td>${u.type}</td>
                                    <td title="${u.description}">${u.description}</td>
                                    <td>
                                        <button class="btn btn-edit" onclick="editUnit(${u.unitId}, '${u.name}', '${u.type}', '${u.description}')">Sửa</button>
                                        <button class="btn btn-delete" onclick="deleteUnit(${u.unitId})">Xóa</button>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>

                    <div class="pagination">
                        <c:choose>
                            <c:when test="${currentPage > 1}">
                                <a href="units?page=${currentPage - 1}&type=${filterType}">&laquo; Prev</a>
                            </c:when>
                            <c:otherwise>
                                <span class="disabled">&laquo; Prev</span>
                            </c:otherwise>
                        </c:choose>

                        <c:forEach begin="1" end="${totalPages > 4 ? 4 : totalPages}" var="i">
                            <c:choose>
                                <c:when test="${currentPage == i}">
                                    <span class="active">${i}</span>
                                </c:when>
                                <c:otherwise>
                                    <a href="units?page=${i}&type=${filterType}">${i}</a>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>

                        <c:choose>
                            <c:when test="${currentPage < totalPages && totalPages <= 4}">
                                <a href="units?page=${currentPage + 1}&type=${filterType}">Next &raquo;</a>
                            </c:when>
                            <c:when test="${currentPage < 4 && totalPages > 4}">
                                <a href="units?page=${currentPage + 1}&type=${filterType}">Next &raquo;</a>
                            </c:when>
                            <c:otherwise>
                                <span class="disabled">Next &raquo;</span>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>
        </div>

        <div id="unitModal" class="modal">
            <div class="modal-content">
                <span class="close" onclick="closeModal()">&times;</span>
                <h3 id="modalTitle">Thêm Unit</h3>
                <form id="unitForm" method="post" action="units">
                    <input type="hidden" id="unitID" name="unitID">
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

        <div id="deleteModal" class="modal">
            <div class="modal-delete">
                <div class="icon-warning">⚠️</div>
                <h3>Xác nhận xóa</h3>
                <p>Bạn có chắc chắn muốn xóa unit này không?<br>Hành động này không thể hoàn tác!</p>
                <div class="btn-group">
                    <button type="button" class="btn btn-cancel" onclick="closeDeleteModal()">Hủy</button>
                    <button type="button" class="btn btn-delete" onclick="confirmDelete()">Xóa</button>
                </div>
            </div>
        </div>

        <script>
            let deleteId = null;

            function applyFilter() {
                const type = document.getElementById('filterType').value;
                window.location.href = 'units?page=1&type=' + type;
            }

            function openModal() {
                document.getElementById('unitModal').style.display = 'block';
                document.getElementById('modalTitle').textContent = 'Thêm Unit';
                document.getElementById('unitForm').reset();
                document.getElementById('action').value = 'add';
                document.getElementById('unitID').value = '';
            }

            function closeModal() {
                document.getElementById('unitModal').style.display = 'none';
            }

            function editUnit(id, name, type, desc) {
                document.getElementById('unitModal').style.display = 'block';
                document.getElementById('modalTitle').textContent = 'Sửa Unit';
                document.getElementById('action').value = 'edit';
                document.getElementById('unitID').value = id;
                document.getElementById('name').value = name;
                document.getElementById('type').value = type;
                document.getElementById('description').value = desc;
            }

            function deleteUnit(id) {
                deleteId = id;
                document.getElementById('deleteModal').style.display = 'block';
            }

            function closeDeleteModal() {
                document.getElementById('deleteModal').style.display = 'none';
                deleteId = null;
            }

            function confirmDelete() {
                if (deleteId) {
                    window.location.href = 'delete-unit?id=' + deleteId;
                }
            }

            window.onclick = function (event) {
                var unitModal = document.getElementById('unitModal');
                var deleteModal = document.getElementById('deleteModal');
                if (event.target == unitModal)
                    closeModal();
                if (event.target == deleteModal)
                    closeDeleteModal();
            }

            document.getElementById('searchInput').addEventListener('keyup', function () {
                var filter = this.value.toUpperCase();
                var rows = document.querySelectorAll('tbody tr');
                rows.forEach(function (row) {
                    var name = row.cells[1].textContent;
                    row.style.display = name.toUpperCase().indexOf(filter) > -1 ? '' : 'none';
                });
            });
        </script>
    </body>
</html>


