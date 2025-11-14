<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--<%@ page contentType="text/html;charset=UTF-8" language="java" %>--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>


<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Quản lý Vai trò (Role)</title>

        <style>
            * {
                box-sizing: border-box;
                margin: 0;
                padding: 0;
            }
            body, html {
                height: 100%;
                font-family: Inter, Arial, sans-serif;
                background: #f5f7fb;
                color: #111827;
            }

            .app {
                display: flex;
                height: 100vh;
            }
            .sidebar {
                width: 260px;
                background: linear-gradient(180deg,#0f2340,#0b1830);
                color: #fff;
                padding: 28px 18px;
                display: flex;
                flex-direction: column;
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
                color: rgba(255,255,255,0.9);
                text-decoration: none;
                padding: 10px 12px;
                border-radius: 10px;
            }
            .nav a.active, .nav a:hover {
                background: rgba(255,255,255,0.04);
            }

            .main {
                flex: 1;
                padding: 24px 32px;
                overflow-y: auto;
            }
            h2 {
                margin-bottom: 16px;
            }

            table {
                width: 100%;
                border-collapse: collapse;
                background: #fff;
                border-radius: 8px;
                overflow: hidden;
                box-shadow: 0 2px 6px rgba(0,0,0,0.05);
            }
            th, td {
                padding: 12px 16px;
                border-bottom: 1px solid #f1f5f9;
            }
            th {
                background: #f8fafc;
                text-align: left;
            }

            .btn {
                border: none;
                border-radius: 6px;
                cursor: pointer;
                font-size: 14px;
                padding: 8px 16px;
                font-weight: 500;
                transition: all 0.25s;
                margin: 0 3px;
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
            }

            .modal-content {
                background: #fff;
                padding: 20px;
                border-radius: 8px;
                width: 400px;
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
            .form-group input {
                padding: 8px;
                border: 1px solid #ccc;
                border-radius: 5px;
            }

            .error {
                color: red;
                font-size: 12px;
                margin-top: 3px;
            }

            .main form[action="roleManage"] {
                display: flex;
                align-items: center;
                gap: 10px;
                flex-wrap: wrap;
                margin-bottom: 16px;
            }
            .main form[action="roleManage"] input[type="text"] {
                padding: 8px 12px;
                border: 1px solid #d1d5db;
                border-radius: 6px;
                font-size: 14px;
                width: 280px;
                transition: border-color 0.2s, box-shadow 0.2s;
            }
            .main form[action="roleManage"] input[type="text"]:focus {
                outline: none;
                border-color: #2563eb;
                box-shadow: 0 0 0 2px rgba(37,99,235,0.2);
            }
            .main form[action="roleManage"] button[type="submit"] {
                background: #3b82f6;
                color: #fff;
            }
            .main form[action="roleManage"] button[type="submit"]:hover {
                background: #2563eb;
            }
            .main form[action="roleManage"] a.btn-delete {
                display: inline-flex;
                align-items: center;
                justify-content: center;
            }

            /* Pagination */
            .pagination {
                display:flex;
                gap:6px;
                margin-top:16px;
                align-items:center;
                flex-wrap:wrap;
            }
            .pagination a, .pagination span {
                display:inline-block;
                padding:6px 10px;
                border-radius:6px;
                text-decoration:none;
                color:#111827;
                background:#fff;
                border:1px solid #e5e7eb;
                font-size:14px;
            }
            .pagination a:hover {
                background:#f3f4f6;
            }
            .pagination .active {
                background:#0f8590;
                color:#fff;
                border-color:#0f8590;
            }
            .pagination .disabled {
                opacity:0.5;
                pointer-events:none;
            }

        </style>
    </head>
    <body>
        <div class="app">

            <!-- Sidebar -->
            <jsp:include page="/view/layout/sidebar.jsp" />

            <!-- Main -->
            <main class="main">
                <h2>Quản lý Vai trò (Role)</h2>

                <button class="btn btn-add" onclick="openAddModal()">➕ Thêm Role</button>
                <br><br>
                <form action="roleManage" method="get" style="margin-bottom:15px;">
                    <input type="text" name="keyword" value="${keyword}" placeholder="🔍 Nhập tên hoặc mô tả..."
                           style="padding:8px;width:250px;">
                    <button type="submit" class="btn btn-add">Tìm kiếm</button>
                    <a href="roleManage" class="btn btn-delete">Reload</a>
                </form>

                <c:if test="${not empty error}">
                    <div style="color:red;margin-bottom:10px;">${error}</div>
                </c:if>
                <c:if test="${not empty success}">
                    <div style="color:green;margin-bottom:10px;">${success}</div>
                </c:if>

                <table>
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Tên vai trò</th>
                            <th>Mô tả</th>
                            <th>Hành động</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="r" items="${roles}">
                            <tr>
                                <td>${r.roleID}</td>
                                <td>${r.roleName}</td>
                                <td>${r.description}</td>
                                <td>
                                    <button class="btn btn-edit" onclick="openEditModal(${r.roleID}, '${r.roleName}', '${r.description}')">Sửa</button>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
                <c:if test="${not empty totalPages && totalPages > 0}">
                    <div style="margin-top:12px;">
                        <div class="pagination">
                            <!-- Previous -->
                            <c:choose>
                                <c:when test="${currentPage > 1}">
                                    <c:url var="prevUrl" value="roleManage">
                                        <c:param name="page" value="${currentPage - 1}" />
                                        <c:if test="${not empty keyword}">
                                            <c:param name="keyword" value="${keyword}" />
                                        </c:if>
                                    </c:url>
                                    <a href="${prevUrl}">‹ Trước</a>
                                </c:when>
                                <c:otherwise>
                                    <span class="disabled">‹ Trước</span>
                                </c:otherwise>
                            </c:choose>

                            <!-- Page numbers -->
                            <!-- Nếu tổng trang nhỏ, hiện tất cả; nếu lớn, hiện window +-2 -->
                            <c:choose>
                                <c:when test="${totalPages <= 10}">
                                    <c:forEach var="i" begin="1" end="${totalPages}">
                                        <c:url var="pageUrl" value="roleManage">
                                            <c:param name="page" value="${i}" />
                                            <c:if test="${not empty keyword}">
                                                <c:param name="keyword" value="${keyword}" />
                                            </c:if>
                                        </c:url>
                                        <c:choose>
                                            <c:when test="${i == currentPage}">
                                                <span class="active">${i}</span>
                                            </c:when>
                                            <c:otherwise>
                                                <a href="${pageUrl}">${i}</a>
                                            </c:otherwise>
                                        </c:choose>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <!-- window: show first, ..., around current, ..., last -->
                                    <c:set var="start" value="${currentPage - 2}" />
                                    <c:set var="end" value="${currentPage + 2}" />
                                    <c:if test="${start < 1}">
                                        <c:set var="end" value="${end + (1 - start)}" />
                                        <c:set var="start" value="1" />
                                    </c:if>
                                    <c:if test="${end > totalPages}">
                                        <c:set var="start" value="${start - (end - totalPages)}" />
                                        <c:set var="end" value="${totalPages}" />
                                    </c:if>
                                    <c:if test="${start < 1}"><c:set var="start" value="1" /></c:if>

                                        <!-- first page -->
                                    <c:url var="firstUrl" value="roleManage">
                                        <c:param name="page" value="1" />
                                        <c:if test="${not empty keyword}"><c:param name="keyword" value="${keyword}" /></c:if>
                                    </c:url>
                                    <c:if test="${start > 1}">
                                        <a href="${firstUrl}">1</a>
                                        <span>...</span>
                                    </c:if>

                                    <c:forEach var="i" begin="${start}" end="${end}">
                                        <c:url var="purl" value="roleManage">
                                            <c:param name="page" value="${i}" />
                                            <c:if test="${not empty keyword}"><c:param name="keyword" value="${keyword}" /></c:if>
                                        </c:url>
                                        <c:choose>
                                            <c:when test="${i == currentPage}">
                                                <span class="active">${i}</span>
                                            </c:when>
                                            <c:otherwise>
                                                <a href="${purl}">${i}</a>
                                            </c:otherwise>
                                        </c:choose>
                                    </c:forEach>

                                    <c:if test="${end < totalPages}">
                                        <span>...</span>
                                        <c:url var="lastUrl" value="roleManage">
                                            <c:param name="page" value="${totalPages}" />
                                            <c:if test="${not empty keyword}"><c:param name="keyword" value="${keyword}" /></c:if>
                                        </c:url>
                                        <a href="${lastUrl}">${totalPages}</a>
                                    </c:if>
                                </c:otherwise>
                            </c:choose>

                            <!-- Next -->
                            <c:choose>
                                <c:when test="${currentPage < totalPages}">
                                    <c:url var="nextUrl" value="roleManage">
                                        <c:param name="page" value="${currentPage + 1}" />
                                        <c:if test="${not empty keyword}"><c:param name="keyword" value="${keyword}" /></c:if>
                                    </c:url>
                                    <a href="${nextUrl}">Sau ›</a>
                                </c:when>
                                <c:otherwise>
                                    <span class="disabled">Sau ›</span>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </c:if>

                <!-- Modal thêm -->
                <div id="addModal" class="modal">
                    <div class="modal-content">
                        <h3>Thêm Vai trò</h3>
                        <form action="roleManage" method="post">
                            <input type="hidden" name="action" value="insert">
                            <div class="form-group">
                                <label>Tên Role</label>
                                <input type="text" name="roleName" required>
                            </div>
                            <div class="form-group">
                                <label>Mô tả</label>
                                <input type="text" name="description">
                            </div>
                            <div style="text-align:right;">
                                <button type="submit" class="btn btn-add">Lưu</button>
                                <button type="button" class="btn btn-delete" onclick="closeAddModal()">Hủy</button>
                            </div>
                        </form>
                    </div>
                </div>

                <!-- Modal sửa -->
                <div id="editModal" class="modal">
                    <div class="modal-content">
                        <h3>Cập nhật Role</h3>
                        <form action="roleManage" method="post">
                            <input type="hidden" name="action" value="update">
                            <input type="hidden" id="editRoleId" name="roleID">
                            <div class="form-group">
                                <label>Tên Role</label>
                                <input type="text" id="editRoleName" name="roleName" required>
                            </div>
                            <div class="form-group">
                                <label>Mô tả</label>
                                <input type="text" id="editRoleDescription" name="description">
                            </div>
                            <div style="text-align:right;">
                                <button type="submit" class="btn btn-add">Cập nhật</button>
                                <button type="button" class="btn btn-delete" onclick="closeEditModal()">Hủy</button>
                            </div>
                        </form>
                    </div>
                </div>



            </main>
        </div>

        <script>
            function openAddModal() {
                document.getElementById('addModal').style.display = 'flex';
            }

            function closeAddModal() {
                document.getElementById('addModal').style.display = 'none';
            }

            function openEditModal(id, name, desc) {
                document.getElementById('editModal').style.display = 'flex';
                document.getElementById('editRoleId').value = id;
                document.getElementById('editRoleName').value = name;
                document.getElementById('editRoleDescription').value = desc;
            }

            function closeEditModal() {
                document.getElementById('editModal').style.display = 'none';
            }

            window.onclick = function (event) {
                const modals = [document.getElementById('addModal'), document.getElementById('editModal')];
                modals.forEach(m => {
                    if (event.target === m)
                        m.style.display = 'none';
                });
            };
        </script>
    </body>
</html>
