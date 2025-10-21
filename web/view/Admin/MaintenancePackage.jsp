<%-- 
    Document   : MaintenancePackage
    Created on : Oct 21, 2025, 6:07:06 PM
    Author     : nxtru
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8" />
        <title>Quản lý combo</title>
        <style>
            * {
                box-sizing: border-box;
                margin: 0;
                padding: 0;
            }
            html,body {
                height: 100%;
                font-family: Inter, "Segoe UI", Roboto, Arial, sans-serif;
                background:#f5f7fb;
                color:#111827;
            }

            .app {
                display: flex;
                height: 100vh;
            }

            .sidebar {
                width: 260px;
                background: linear-gradient(180deg,#0f2340,#0b1830);
                color:#fff;
                padding:28px 18px;
                display:flex;
                flex-direction:column;
            }
            .brand {
                font-weight:800;
                font-size:18px;
                letter-spacing:1px;
                margin-bottom:22px;
            }
            .nav {
                margin-top:12px;
                display:flex;
                flex-direction:column;
                gap:8px;
            }
            .nav a {
                color:rgba(255,255,255,0.9);
                text-decoration:none;
                padding:10px 12px;
                border-radius:10px;
                display:flex;
                align-items:center;
                gap:12px;
            }
            .nav a.active, .nav a:hover {
                background: rgba(255,255,255,0.04);
            }

            .main {
                flex:1;
                padding:24px 32px;
                overflow:auto;
            }

            table {
                width:100%;
                border-collapse: collapse;
                background:#fff;
                border-radius:8px;
                overflow:hidden;
                box-shadow:0 2px 6px rgba(0,0,0,0.05);
            }
            table thead th {
                background:#f8fafc;
                padding:12px 16px;
                text-align:left;
                border-bottom:1px solid #e5e7eb;
            }
            table tbody td {
                padding:12px 16px;
                border-bottom:1px solid #f1f5f9;
                vertical-align: top;
            }

            .btn {
                padding:6px 10px;
                border:none;
                border-radius:6px;
                cursor:pointer;
                font-size:13px;
                transition:all 0.2s ease;
                white-space: nowrap;
            }
            .btn-add {
                background:#28a745;
                color:white;
            }
            .btn-edit {
                background:#ffc107;
                color:black;
            }
            .btn-delete {
                background:#dc3545;
                color:white;
            }
            .btn-detail {
                background:#007bff;
                color:white;
            }

            .action-buttons {
                display: flex;
                justify-content: flex-start;
                align-items: center;
                gap: 6px;
            }

            .modal {
                display: none;
                position: fixed;
                z-index: 999;
                left: 0;
                top: 0;
                width: 100%;
                height: 100%;
                background-color: rgba(0,0,0,0.4);
                align-items: center;
                justify-content: center;
            }

            .modal-content {
                background-color: #fff;
                padding: 20px 30px;
                border-radius: 10px;
                width: 500px;
                box-shadow: 0 4px 10px rgba(0,0,0,0.25);
                animation: fadeIn 0.3s ease;
            }

            .detail-table {
                width: 100%;
                border-collapse: collapse;
                margin-top: 10px;
            }
            .detail-table th {
                text-align: left;
                width: 40%;
                padding: 6px 4px;
                font-weight: 600;
                vertical-align: top;
            }
            .detail-table td {
                padding: 6px 4px;
            }

            .close-btn {
                float: right;
                font-size: 22px;
                font-weight: bold;
                color: #555;
                cursor: pointer;
            }
            .close-btn:hover {
                color: #000;
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
        </style>
    </head>
    <body>
        <div class="app">
            <jsp:include page="/view/layout/sidebar.jsp"/>

            <main class="main">
                <h2 style="margin-bottom: 20px">Quản lý Combo</h2>

                <button type="button" class="btn btn-add"
                        onclick="document.getElementById('addComboModal').style.display = 'flex'">➕ Thêm Combo</button>


                <form action="#" method="get" 
                      style="display: flex; justify-content: space-between; align-items: center; margin-top: 20px;">

                    <div style="display: flex; gap: 10px; align-items: center;">
                        <label for="roleFilter">Filter:</label>
                        <select name="status" id="roleFilter">
                            <option value="">All</option>
                            <option value="true">Active</option>
                            <option value="false">Inactive</option>
                        </select>

                        <input type="text" name="keyword" placeholder="Search by name/code..." 
                               style="padding: 5px;" />
                        <button type="submit">Search</button>
                    </div>

                    <div>
                        <button type="button" onclick="location.reload()">Reload</button>
                    </div>
                </form>

                <br/><br/>

                <table>
                    <thead>
                        <tr>
                            <th>PackageCode</th>
                            <th>PackageName</th>
                            <th>PackageDescription</th>
                            <th>DiscountPercent</th>
                            <th>FinalPrice</th>
                            <th>ProductName(s)</th>
                            <th>Status</th>
                            <th>Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:set var="processedCodes" value="," />
                        <c:forEach var="d" items="${listPackage}">
                            <c:if test="${not fn:contains(processedCodes, d.maintenancePackage.packageCode)}">
                                <c:set var="processedCodes" value="${processedCodes}${d.maintenancePackage.packageCode}," />

                                <c:set var="productsInPackage" value="" />
                                <c:forEach var="p" items="${listPackage}">
                                    <c:if test="${p.maintenancePackage.packageCode eq d.maintenancePackage.packageCode}">
                                        <c:set var="productsInPackage" value="${productsInPackage}${p.product.name}, " />
                                    </c:if>
                                </c:forEach>

                                <tr>
                                    <td>${d.maintenancePackage.packageCode}</td>
                                    <td>${d.maintenancePackage.name}</td>
                                    <td>${d.maintenancePackage.description}</td>
                                    <td>${d.maintenancePackage.discountPercent}%</td>
                                    <td>${d.maintenancePackage.finalPrice}</td>
                                    <td>${fn:substring(productsInPackage, 0, fn:length(productsInPackage)-2)}</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${d.maintenancePackage.isActive}">
                                                <span style="color:green;font-weight:600;">Active</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span style="color:red;font-weight:600;">Inactive</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <div class="action-buttons">
                                            <c:choose>
                                                <c:when test="${d.maintenancePackage.isActive}">
                                                    <button type="button" class="btn btn-detail"
                                                            onclick="document.getElementById('modal_${d.maintenancePackage.packageCode}').style.display = 'flex'">
                                                        👁 View
                                                    </button>
                                                    <button type="button" class="btn btn-edit"
                                                            onclick="document.getElementById('update_${d.maintenancePackage.packageCode}').style.display = 'flex'">
                                                        ✏ Update
                                                    </button>
                                                    <form action="maintenancePackage" method="post" style="display:inline;margin:0;">
                                                        <input type="hidden" name="action" value="changeStatus" />
                                                        <input type="hidden" name="packageId" value="${d.maintenancePackage.packageId}" />
                                                        <input type="hidden" name="status" value="false" />
                                                        <button type="submit" class="btn btn-delete">❌ Inactive</button>
                                                    </form>
                                                </c:when>
                                                <c:otherwise>
                                                    <form action="maintenancePackage" method="post" style="display:inline;margin:0;">
                                                        <input type="hidden" name="action" value="changeStatus" />
                                                        <input type="hidden" name="packageId" value="${d.maintenancePackage.packageId}" />
                                                        <input type="hidden" name="status" value="true" />
                                                        <button type="submit" class="btn btn-add">✅ Active</button>
                                                    </form>
                                                </c:otherwise>
                                            </c:choose>
                                        </div>
                                    </td>
                                </tr>
                            </c:if>
                        </c:forEach>
                    </tbody>
                </table>

                <c:set var="processedModalCodes" value="," />
                <c:forEach var="d" items="${listPackage}">
                    <c:if test="${not fn:contains(processedModalCodes, d.maintenancePackage.packageCode)}">
                        <c:set var="processedModalCodes" value="${processedModalCodes}${d.maintenancePackage.packageCode}," />

                        <div id="modal_${d.maintenancePackage.packageCode}" class="modal">
                            <div class="modal-content">
                                <span class="close-btn" onclick="document.getElementById('modal_${d.maintenancePackage.packageCode}').style.display = 'none'">&times;</span>
                                <h3 style="text-align:center; border-bottom:2px solid #0f2340; padding-bottom:8px;">
                                    Thông tin chi tiết gói combo
                                </h3>

                                <table class="detail-table">
                                    <tr><th>PackageCode</th><td>${d.maintenancePackage.packageCode}</td></tr>
                                    <tr><th>PackageName</th><td>${d.maintenancePackage.name}</td></tr>
                                    <tr><th>Description</th><td>${d.maintenancePackage.description}</td></tr>
                                    <tr><th>KilometerMilestone</th><td>${d.maintenancePackage.kilometerMilestone}</td></tr>
                                    <tr><th>MonthMilestone</th><td>${d.maintenancePackage.monthMilestone}</td></tr>
                                    <tr><th>ApplicableBrands</th><td>${d.maintenancePackage.applicableBrands}</td></tr>
                                    <tr><th>BasePrice</th><td>${d.maintenancePackage.basePrice}</td></tr>
                                    <tr><th>DiscountPercent</th><td>${d.maintenancePackage.discountPercent}%</td></tr>
                                    <tr><th>FinalPrice</th><td>${d.maintenancePackage.finalPrice}</td></tr>
                                    <tr><th>Status</th><td><c:out value="${d.maintenancePackage.isActive ? 'Active' : 'Inactive'}"/></td></tr>
                                    <tr>
                                        <th>Detail Product(s)</th>
                                        <td>
                                            <ol>
                                                <c:forEach var="detail" items="${listPackage}">
                                                    <c:if test="${detail.maintenancePackage.packageCode eq d.maintenancePackage.packageCode}">
                                                        <li>${detail.product.name}</li>
                                                        </c:if>
                                                    </c:forEach>
                                            </ol>
                                        </td>
                                    </tr>
                                </table>
                            </div>
                        </div>

                    </c:if>
                </c:forEach>

            </main>
        </div>
