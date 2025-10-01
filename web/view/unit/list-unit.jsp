<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Quản lý Unit</title>
        <style>
            * { margin: 0; padding: 0; box-sizing: border-box; }
            body { font-family: Arial, sans-serif; background: #f5f5f5; padding: 20px; }
            .container { max-width: 1200px; margin: 0 auto; background: #fff; padding: 20px; border-radius: 8px; }
            h2 { margin-bottom: 20px; color: #333; }
            .top-bar { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; }
            .btn { padding: 10px 20px; border: none; border-radius: 4px; cursor: pointer; font-size: 14px; }
            .btn-add { background: #28a745; color: white; }
            .btn-add:hover { background: #218838; }
            .btn-edit { background: #007bff; color: white; margin-right: 5px; }
            .btn-delete { background: #dc3545; color: white; }
            .btn-save { background: #28a745; color: white; }
            .btn-cancel { background: #6c757d; color: white; }
            .search-box { padding: 8px; border: 1px solid #ddd; border-radius: 4px; width: 300px; }
            table { width: 100%; border-collapse: collapse; margin-top: 20px; }
            th, td { padding: 12px; text-align: left; border-bottom: 1px solid #ddd; }
            th { background: #f8f9fa; font-weight: 600; color: #333; }
            tr:hover { background: #f8f9fa; }
            .pagination { display: flex; justify-content: flex-end; gap: 5px; margin-top: 30px; }
            .pagination a, .pagination span { padding: 10px 15px; text-decoration: none; border: 1px solid #ddd; color: #333; border-radius: 4px; transition: all 0.3s; font-size: 14px; }
            .pagination a:hover { background: #007bff; color: white; border-color: #007bff; }
            .pagination .active { background: #007bff; color: white; border-color: #007bff; font-weight: bold; }
            .pagination .disabled { color: #ccc; cursor: not-allowed; pointer-events: none; background: #f8f9fa; }
            .modal { display: none; position: fixed; z-index: 1000; inset: 0; background: rgba(0,0,0,0.5); }
            .modal-content { background: #fff; margin: 5% auto; padding: 30px; width: 500px; border-radius: 8px; position: relative; }
            .close { position: absolute; right: 15px; top: 10px; font-size: 28px; cursor: pointer; color: #aaa; }
            .close:hover { color: #000; }
            .form-group { margin-bottom: 15px; }
            .form-group label { display: block; margin-bottom: 5px; font-weight: 600; color: #333; }
            .form-group input, .form-group select, .form-group textarea { width: 100%; padding: 8px; border: 1px solid #ddd; border-radius: 4px; }
            .form-group textarea { resize: vertical; min-height: 80px; }
            .form-actions { display: flex; gap: 10px; justify-content: flex-end; margin-top: 20px; }
            .modal-delete { background: white; margin: 15% auto; padding: 30px; width: 400px; border-radius: 8px; position: relative; text-align: center; }
            .modal-delete .icon-warning { font-size: 60px; color: #dc3545; margin-bottom: 20px; }
        </style>
    </head>
    <body>
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
                if (event.target == unitModal) closeModal();
                if (event.target == deleteModal) closeDeleteModal();
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


