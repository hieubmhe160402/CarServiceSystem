<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8" />
        <title>Quản lý Chi tiết Gói bảo dưỡng</title>
        <style>
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
            .main {
                flex: 1;
                padding: 24px 32px;
                overflow: auto;
            }

            h2 {
                margin-bottom: 16px;
                font-size: 20px;
                font-weight: 700;
            }

            .card {
                background: #fff;
                border-radius: 12px;
                padding: 18px;
                box-shadow: 0 4px 14px rgba(16,24,40,0.04);
                margin-bottom: 24px;
            }

            table {
                width: 100%;
                border-collapse: collapse;
                background: #fff;
                border-radius: 12px;
                overflow: hidden;
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

            .btn {
                padding: 6px 12px;
                border: none;
                border-radius: 6px;
                font-size: 13px;
                cursor: pointer;
                font-weight: 500;
                transition: 0.2s;
            }
            .btn-edit {
                background-color: #ffc107;
                color: #111;
            }
            .btn-delete {
                background-color: #dc3545;
                color: #fff;
            }
            .btn-active {
                background-color: #28a745;
                color: #fff;
            }
            .btn:hover {
                opacity: 0.9;
            }

            .search-form {
                margin-bottom: 18px;
                display: flex;
                align-items: center;
                gap: 8px;
            }
            .search-form input {
                padding: 8px 10px;
                border: 1px solid #ccc;
                border-radius: 6px;
            }
            .btn-refresh {
                background: #007bff;
                color: #fff;
            }

            td.actions {
                text-align: center;
                white-space: nowrap;
            }
            td.actions form {
                display: inline;
            }
            td.actions div {
                display: flex;
                justify-content: center;
                gap: 8px;
            }
        </style>
    </head>
    <body>
        <div class="app">
            <jsp:include page="/view/layout/sidebar.jsp"/>

            <main class="main">
                <h2>Quản lý Chi tiết Gói bảo dưỡng</h2>

                <div class="card">
                    <form class="search-form" action="ManagePackageDetails" method="get">
                        <label>Mã gói:</label>
                        <input type="text" name="packageCode" placeholder="Nhập PackageCode..." />
                        <button type="submit" class="btn btn-refresh">Tìm</button>
                        <a href="ManagePackageDetails" class="btn btn-refresh">Reload</a>
                    </form>

                    <table>
                        <thead>
                            <tr>
                                <th>PackageDetailID</th>
                                <th>PackageCode</th>
                                <th>ProductName</th>
                                <th>Quantity</th>
                                <th>Status</th>
                                <th>DisplayOrder</th>
                                <th>Notes</th>
                                <th style="text-align:center;">Actions</th>
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
                                                <c:if test="${not d.isRequired}">Ngưng kinh doanh</c:if>
                                                </td>
                                                <td>${d.displayOrder}</td>
                                            <td>${d.notes}</td>
                                            <td>
                                                <div style="display:flex;gap:8px;">
                                                    <c:if test="${d.isRequired}">
                                                        <!-- Nút Update -->
                                                        <button type="button" class="btn btn-edit"
                                                                onclick="openEditModal('${d.packageDetailId}', '${d.product.productId}', '${d.quantity}', '${d.displayOrder}', '${d.notes}', '${d.isRequired}')">
                                                            Update
                                                        </button>
                                                    </c:if>

                                                    <!-- Nút Ngưng/Kinh doanh -->
                                                    <form action="managerPackage" method="post">
                                                        <input type="hidden" name="action" value="toggleStatus"/>
                                                        <input type="hidden" name="id" value="${d.packageDetailId}"/>
                                                        <button type="submit" class="btn ${d.isRequired ? 'btn-delete' : 'btn-active'}">
                                                            <c:choose>
                                                                <c:when test="${d.isRequired}">Ngưng kinh doanh</c:when>
                                                                <c:otherwise>Kinh doanh lại</c:otherwise>
                                                            </c:choose>
                                                        </button>
                                                    </form>
                                                </div>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <tr>
                                        <td colspan="9" style="text-align:center; color:#999;">
                                            (Chưa có dữ liệu hiển thị)
                                        </td>
                                    </tr>
                                </c:otherwise>
                            </c:choose>
                        </tbody>
                    </table>
                </div>
            </main>
        </div>

    </body>

</html>
