<%-- 
    Document   : managerCarmaintenanaceByTech
    Created on : Nov 2, 2025, 1:57:40 PM
    Author     : nxtru
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Quản lý Bảo dưỡng xe</title>
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
            .sidebar {
                width: 260px;
                background: linear-gradient(180deg,#0f2340,#0b1830);
                color:#fff;
                padding:28px 18px;
                display:flex;
                flex-direction:column;
            }

            /* --- MAIN CONTENT --- */
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

            /* --- FILTER --- */
            .status-filter {
                padding: 6px 10px;
                border-radius: 5px;
                border: 1px solid #ccc;
                font-size: 14px;
            }

            /* --- MODAL --- */
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
        </style>
    </head>
    <body>
        <div class="app">
            <jsp:include page="/view/layout/sidebar.jsp"/>

            <div class="main">
                <div class="container">
                    <h2>Quản lý Bảo dưỡng xe</h2>

                    <div class="top-bar">
                        <div style="display: flex; align-items: center; gap: 10px;">
                            <input type="text" class="search-box" placeholder="Tìm kiếm theo xe..." id="searchInput">
                            <select id="statusFilter" class="status-filter">
                                <option value="">Tất cả</option>
                                <option value="PENDING">Chờ xử lý</option>
                                <option value="PROCESSING">Đang xử lý</option>
                                <option value="COMPLETED">Hoàn thành</option>
                                <option value="CANCELLED">Hủy</option>
                            </select>
                            <button type="button" class="btn btn-reload" onclick="window.location.reload()">🔁 Reload</button>
                        </div>
                    </div>

                    <table id="maintenanceTable">
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Thông tin xe</th>
                                <th>Mã gói</th>
                                <th>Tên gói</th>
                                <th>Số km</th>
                                <th>Trạng thái</th>
                                <th>Ghi chú</th>
                                <th>Ngày tạo</th>
                                <th>Hành động</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:choose>
                                <c:when test="${not empty maintenanceList}">
                                    <c:forEach var="item" items="${maintenanceList}">
                                        <tr>
                                            <td>${item.maintenanceID}</td>
                                            <td>${item.carInfo}</td>
                                            <td>${item.packageCode}</td>
                                            <td>${item.packageName}</td>
                                            <td>${item.odometer}</td>
                                            <td>${item.status}</td>
                                            <td class="description-cell">${item.notes}</td>
                                            <td>${item.createdDate}</td>
                                            <td>
                                                <button class="btn btn-edit">Chi tiết</button>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <tr>
                                        <td colspan="9" style="text-align: center; padding: 20px;">
                                            Không có dữ liệu bảo dưỡng
                                        </td>
                                    </tr>
                                </c:otherwise>
                            </c:choose>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>

        <script>
            // --- Filter ---
            const searchInput = document.getElementById("searchInput");
            const statusFilter = document.getElementById("statusFilter");
            const rows = document.querySelectorAll("#maintenanceTable tbody tr");

            function filterTable() {
                const searchText = searchInput.value.toLowerCase();
                const status = statusFilter.value;
                rows.forEach(row => {
                    // Bỏ qua hàng "Không có dữ liệu"
                    if (row.cells.length === 1) {
                        return;
                    }
                    const carInfo = row.cells[1].textContent.toLowerCase();
                    const statusText = row.cells[5].textContent.toLowerCase();
                    const matchName = carInfo.includes(searchText);
                    const matchStatus = !status || statusText.includes(status.toLowerCase());
                    row.style.display = matchName && matchStatus ? "" : "none";
                });
            }

            searchInput.addEventListener("input", filterTable);
            statusFilter.addEventListener("change", filterTable);
        </script>
    </body>
</html>
