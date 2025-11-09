<%-- 
    Document   : managerCarmaintenanaceByTech
    Created on : Nov 2, 2025, 1:57:40 PM
    Author     : nxtru
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

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

            /* --- MODAL PHIẾU DỊCH VỤ --- */
            .modal {
                display: none;
                position: fixed;
                z-index: 2000;
                left: 0;
                top: 0;
                width: 100%;
                height: 100%;
                background: rgba(0,0,0,0.5);
                overflow-y: auto;
            }

            /* Hộp nội dung chính */
            .modal-content {
                background: #fff;
                margin: 3% auto;
                border-radius: 10px;
                width: 75%;
                max-width: 1000px;
                box-shadow: 0 5px 25px rgba(0,0,0,0.25);
                animation: fadeIn 0.3s ease-in-out;
            }

            /* Header của phiếu */
            .modal-header {
                display: flex;
                justify-content: space-between;
                align-items: center;
                background: #e6f0ff;
                padding: 15px 25px;
                border-radius: 10px 10px 0 0;
                border-bottom: 1px solid #cbd5e1;
            }
            .modal-header h2 {
                font-size: 20px;
                font-weight: 600;
                color: #1e3a8a;
            }
            .status-badge {
                display: inline-block;
                padding: 6px 12px;
                border-radius: 6px;
                font-weight: bold;
                font-size: 13px;
                text-transform: uppercase;
            }

            /* Trạng thái WAITING - nền vàng nhạt */
            .status-badge.waiting {
                background-color: #fff9e6;
                color: #b38b00;
                border: 1px solid #ffe58f;
            }

            /* Trạng thái PROCESSING - nền xanh nhạt + viền */
            .status-badge.processing {
                background-color: #e6f0ff;
                color: #004085;
                border: 1px solid #b3d1ff;
            }

            /* Nội dung body */
            .modal-body {
                padding: 20px;
            }
            .grid {
                display: grid;
                grid-template-columns: repeat(3, 1fr);
                gap: 20px;
                margin-bottom: 20px;
            }
            .form-group {
                margin-bottom: 15px;
            }
            .form-group label {
                font-weight: 600;
                margin-bottom: 6px;
                color: #111827;
                display: block;
            }
            .form-group input, .form-group textarea, .form-group select {
                width: 100%;
                padding: 8px 10px;
                border: 1px solid #d1d5db;
                border-radius: 6px;
                font-size: 14px;
            }
            .form-group.full {
                grid-column: 1 / 4;
            }

            /* Animation */
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

            <div class="main">
                <div class="container">
                    <h2>Quản lý Bảo dưỡng xe</h2>

                    <div class="top-bar">
                        <div style="display: flex; align-items: center; gap: 10px;">
                            <input type="text" class="search-box" placeholder="Tìm kiếm theo xe..." id="searchInput">
                            <select id="statusFilter" class="status-filter">
                                <option value="">Tất cả</option>
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
                                                <form method="GET" action="listcarmaintenanacebytech" style="display: inline-block;">
                                                    <input type="hidden" name="action" value="detail">
                                                    <input type="hidden" name="maintenanceId" value="${item.maintenanceID}">
                                                    <button type="submit" class="btn btn-edit">Chi tiết</button>
                                                </form>
                                                <c:if test="${item.status != 'CANCELLED' && item.status != 'COMPLETED'}">
                                                    <form method="POST" action="listcarmaintenanacebytech" style="display: inline-block; margin-left: 5px;">
                                                        <input type="hidden" name="action" value="cancel">
                                                        <input type="hidden" name="maintenanceId" value="${item.maintenanceID}">
                                                        <button type="submit" class="btn btn-delete">Hủy</button>
                                                    </form>
                                                    <form method="POST" action="listcarmaintenanacebytech" style="display: inline-block; margin-left: 5px;">
                                                        <input type="hidden" name="action" value="complete">
                                                        <input type="hidden" name="maintenanceId" value="${item.maintenanceID}">
                                                        <button type="submit" class="btn" style="background: #10b981; color: white; padding: 6px 12px;">Hoàn thành</button>
                                                    </form>
                                                </c:if>
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
                    <c:if test="${not empty detail}">
                        <div id="serviceModal" class="modal" style="display:block;">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <h2>Phiếu dịch vụ</h2>
                                    <span class="status-badge
                                          ${detail.status eq 'WAITING' ? 'waiting' : 
                                            (detail.status eq 'PROCESSING' ? 'processing' : '')}">
                                              ${detail.status}
                                          </span>
                                          <a href="listcarmaintenanacebytech" class="close">&times;</a>
                                    </div>

                                    <div class="modal-body">
                                        <div class="grid">
                                            <div class="form-group">
                                                <label>Mã phiếu dịch vụ</label>
                                                <input type="text" value="${detail.maintenanceId}" readonly />
                                            </div>
                                            <div class="form-group">
                                                <label>Mã lịch hẹn</label>
                                                <input type="text" value="${detail.appointment.appointmentId}" readonly />
                                            </div>
                                            <div class="form-group">
                                                <label>Ngày bảo trì</label>
                                                <input type="text" value="${detail.maintenanceDate}" readonly />
                                            </div>
                                        </div>

                                        <div class="grid">
                                            <div class="form-group">
                                                <label>Tên khách hàng</label>
                                                <input type="text" value="${detail.car.owner.fullName}" readonly />
                                            </div>
                                            <div class="form-group">
                                                <label>Số điện thoại</label>
                                                <input type="text" value="${detail.car.owner.phone}" readonly />
                                            </div>
                                            <div class="form-group">
                                                <label>Email</label>
                                                <input type="text" value="${detail.car.owner.email}" readonly />
                                            </div>
                                        </div>

                                        <div class="grid">
                                            <div class="form-group">
                                                <label>Thông tin xe</label>
                                                <input type="text" value="${detail.car.brand} ${detail.car.model}" readonly />
                                            </div>
                                            <div class="form-group">
                                                <label>Odometer</label>
                                                <input type="text" value="${detail.odometer}" readonly />
                                            </div>
                                            <div class="form-group">
                                                <label>Người tạo</label>
                                                <input type="text" value="${detail.createdBy.fullName}" readonly />
                                            </div>
                                        </div>



                                        <div class="form-group full">
                                            <label>Ghi chú</label>
                                            <textarea readonly>${detail.notes}</textarea>
                                        </div>

                                        <div class="form-group full">
                                            <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 10px;">
                                                <h3 style="margin: 0;">Danh sách dịch vụ sử dụng</h3>
                                                <c:if test="${detail.status != 'CANCELLED' && detail.status != 'COMPLETED'}">
                                                    <div style="display: flex; gap: 10px;">
                                                        <form method="GET" action="listcarmaintenanacebytech" style="display: inline-block;">
                                                            <input type="hidden" name="action" value="addService">
                                                            <input type="hidden" name="maintenanceId" value="${detail.maintenanceId}">
                                                            <button type="submit" class="btn-add-service" style="background: #3b82f6; color: white; padding: 8px 16px; border: none; border-radius: 6px; cursor: pointer; font-weight: 500;">+ Dịch vụ</button>
                                                        </form>
                                                        <form method="GET" action="listcarmaintenanacebytech" style="display: inline-block;">
                                                            <input type="hidden" name="action" value="addPart">
                                                            <input type="hidden" name="maintenanceId" value="${detail.maintenanceId}">
                                                            <button type="submit" class="btn-add-part" style="background: #e5e7eb; color: #374151; padding: 8px 16px; border: none; border-radius: 6px; cursor: pointer; font-weight: 500;">+ Linh kiện</button>
                                                        </form>
                                                    </div>
                                                </c:if>
                                            </div>
                                            <table border="1" cellspacing="0" cellpadding="8"
                                                   style="width:100%; border-collapse:collapse; text-align:left;">
                                                <thead style="background:#f5f5f5;">
                                                    <tr>
                                                        <th>Mã SP</th>
                                                        <th>Tên linh kiện/Dịch vụ</th>
                                                        <th>Số lượng</th>
                                                        <th>Đơn giá</th>
                                                        <th>Giảm giá</th>
                                                        <th>Thành tiền</th>
                                                        <th>Hành động</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <c:choose>
                                                        <c:when test="${not empty products}">
                                                            <c:set var="prevPackage" value="" />
                                                            <c:set var="rowCount" value="0" />

                                                            <c:forEach var="item" items="${products}">
                                                                <c:choose>
                                                                    <c:when test="${item.itemType == 'Dịch vụ combo'}">
                                                                        <c:if test="${item.packageCode ne prevPackage}">
                                                                            <c:set var="rowCount" value="0" />
                                                                            <c:forEach var="inner" items="${products}">
                                                                                <c:if test="${inner.itemType == 'Dịch vụ combo' && inner.packageCode == item.packageCode}">
                                                                                    <c:set var="rowCount" value="${rowCount + 1}" />
                                                                                </c:if>
                                                                            </c:forEach>
                                                                        </c:if>
                                                                        <tr>
                                                                            <c:if test="${item.packageCode ne prevPackage}">
                                                                                <td rowspan="${rowCount}">${item.packageCode}</td>
                                                                            </c:if>
                                                                            <td>${item.productName}</td>
                                                                            <td>${item.quantity}</td>
                                                                            <c:if test="${item.packageCode ne prevPackage}">
                                                                                <td rowspan="${rowCount}"><fmt:formatNumber value="${item.basePrice}" type="number" groupingUsed="true"/> VND</td>
                                                                                <td rowspan="${rowCount}">${item.discountPercent}%</td>
                                                                                <td rowspan="${rowCount}"><fmt:formatNumber value="${item.finalPrice}" type="number" groupingUsed="true"/> VND</td>
                                                                                <td rowspan="${rowCount}"></td>
                                                                            </c:if>

                                                                        </tr>
                                                                        <c:set var="prevPackage" value="${item.packageCode}" />
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <tr>
                                                                            <td>${item.packageCode}</td>
                                                                            <td>${item.productName}</td>
                                                                            <td>${item.quantity}</td>
                                                                            <td><fmt:formatNumber value="${item.basePrice}" type="number" groupingUsed="true"/> VND</td>
                                                                            <td>${item.discountPercent}%</td>
                                                                            <td><fmt:formatNumber value="${item.finalPrice}" type="number" groupingUsed="true"/> VND</td>
                                                                            <td>
                                                                                <c:if test="${detail.status != 'CANCELLED' && detail.status != 'COMPLETED'}">
                                                                                    <c:if test="${item.itemType == 'Dịch vụ lẻ' && item.serviceDetailId != null}">
                                                                                        <form method="POST" action="listcarmaintenanacebytech" style="display: inline-block;" 
                                                                                              onsubmit="return confirm('Bạn có chắc chắn muốn xóa dịch vụ này?');">
                                                                                            <input type="hidden" name="action" value="deleteService">
                                                                                            <input type="hidden" name="maintenanceId" value="${detail.maintenanceId}">
                                                                                            <input type="hidden" name="serviceDetailId" value="${item.serviceDetailId}">
                                                                                            <button type="submit" style="background: #ef4444; color: white; padding: 4px 8px; border: none; border-radius: 4px; cursor: pointer; font-size: 12px;">Xóa</button>
                                                                                        </form>
                                                                                    </c:if>
                                                                                    <c:if test="${item.itemType == 'Phụ tùng thay thế' && item.servicePartDetailId != null}">
                                                                                        <form method="POST" action="listcarmaintenanacebytech" style="display: inline-block;" 
                                                                                              onsubmit="return confirm('Bạn có chắc chắn muốn xóa linh kiện này?');">
                                                                                            <input type="hidden" name="action" value="deletePart">
                                                                                            <input type="hidden" name="maintenanceId" value="${detail.maintenanceId}">
                                                                                            <input type="hidden" name="servicePartDetailId" value="${item.servicePartDetailId}">
                                                                                            <button type="submit" style="background: #ef4444; color: white; padding: 4px 8px; border: none; border-radius: 4px; cursor: pointer; font-size: 12px;">Xóa</button>
                                                                                        </form>
                                                                                    </c:if>
                                                                                </c:if>
                                                                            </td>
                                                                        </tr>
                                                                    </c:otherwise>
                                                                </c:choose>
                                                            </c:forEach>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <tr>
                                                                <td colspan="7" style="text-align:center;">Không có sản phẩm nào trong phiếu bảo dưỡng này</td>
                                                            </tr>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </tbody>
                                            </table>

                                            <div style="text-align:right; margin-top:10px; font-weight:bold;">
                                                Tổng tiền:
                                                <fmt:formatNumber value="${detail.finalAmount}" type="number" maxFractionDigits="0" groupingUsed="true"/> VND
                                            </div>

                                            <div style="text-align:right; margin-top:30px;">
                                                <a href="listcarmaintenanacebytech" class="btn btn-danger" 
                                                   style="background-color:#dc3545; border:none; padding:8px 18px; border-radius:6px; color:white; text-decoration:none;">
                                                    Đóng
                                                </a>
                                            </div>
                                        </div>

                                    </div>
                                </div>
                            </div>
                        </c:if>

                        <c:if test="${not empty addType}">
                            <div id="addItemModal" class="add-item-modal" style="display:block;">
                                <div class="add-item-modal-content">
                                    <div class="add-item-modal-header">
                                        <h3>
                                            <c:choose>
                                                <c:when test="${addType eq 'addService'}">Thêm dịch vụ</c:when>
                                                <c:when test="${addType eq 'addPart'}">Thêm linh kiện</c:when>
                                            </c:choose>
                                        </h3>
                                        <a href="listcarmaintenanacebytech?action=detail&maintenanceId=${maintenanceId}" class="close">&times;</a>
                                    </div>
                                    <div class="add-item-modal-body">
                                        <form method="POST" action="listcarmaintenanacebytech" id="addItemForm">
                                            <input type="hidden" name="action" value="${addType}">
                                            <input type="hidden" name="maintenanceId" value="${maintenanceId}">

                                            <div class="form-group">
                                                <label>Chọn sản phẩm *</label>
                                                <select name="productId" id="productSelect" required style="width: 100%; padding: 8px 10px; border: 1px solid #d1d5db; border-radius: 6px;">
                                                    <option value="">Chọn sản phẩm</option>
                                                    <c:forEach var="product" items="${productList}">
                                                        <option value="${product.productId}" data-price="${product.price}">${product.name}</option>
                                                    </c:forEach>
                                                </select>
                                            </div>

                                            <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 15px; margin-top: 15px;">
                                                <div class="form-group">
                                                    <label>Số lượng</label>
                                                    <input type="number" name="quantity" id="quantityInput" step="0.01" min="0.01" required 
                                                           onchange="calculateTotal()" style="width: 100%; padding: 8px 10px; border: 1px solid #d1d5db; border-radius: 6px;">
                                                </div>
                                                <div class="form-group">
                                                    <label>Đơn giá</label>
                                                    <input type="number" name="unitPrice" id="unitPriceInput" step="0.01" min="0" required 
                                                           onchange="calculateTotal()" style="width: 100%; padding: 8px 10px; border: 1px solid #d1d5db; border-radius: 6px;">
                                                </div>
                                            </div>

                                            <div class="form-group" style="margin-top: 15px;">
                                                <label>Notes</label>
                                                <textarea name="notes" rows="3" placeholder="Ghi chú ngắn gọn" 
                                                          style="width: 100%; padding: 8px 10px; border: 1px solid #d1d5db; border-radius: 6px;"></textarea>
                                            </div>

                                            <div class="form-group" style="margin-top: 15px;">
                                                <label>Tổng tiền</label>
                                                <input type="text" id="totalPriceInput" readonly 
                                                       style="width: 100%; padding: 8px 10px; border: 1px solid #d1d5db; border-radius: 6px; background: #f5f5f5;">
                                            </div>

                                            <div style="text-align: right; margin-top: 20px;">
                                                <button type="button" onclick="window.location.href = 'listcarmaintenanacebytech?action=detail&maintenanceId=${maintenanceId}'" 
                                                        style="background: #e5e7eb; color: #374151; padding: 8px 18px; border: none; border-radius: 6px; cursor: pointer; margin-right: 10px;">
                                                    Hủy
                                                </button>
                                                <button type="submit" 
                                                        style="background: #16a34a; color: white; padding: 8px 18px; border: none; border-radius: 6px; cursor: pointer; font-weight: 500;">
                                                    Xác nhận
                                                </button>
                                            </div>
                                        </form>
                                    </div>
                                </div>
                            </div>
                        </c:if>
                    </div>
                </div>
            </div>

            <style>
                /* Modal thêm dịch vụ/linh kiện */
                .add-item-modal {
                    display: none;
                    position: fixed;
                    z-index: 3000;
                    left: 0;
                    top: 0;
                    width: 100%;
                    height: 100%;
                    background: rgba(0,0,0,0.5);
                    overflow-y: auto;
                }

                .add-item-modal-content {
                    background: white;
                    margin: 5% auto;
                    padding: 30px;
                    width: 500px;
                    border-radius: 10px;
                    box-shadow: 0 5px 20px rgba(0,0,0,0.15);
                    animation: fadeIn 0.3s ease-in-out;
                }

                .add-item-modal-header {
                    display: flex;
                    justify-content: space-between;
                    align-items: center;
                    margin-bottom: 20px;
                    padding-bottom: 15px;
                    border-bottom: 1px solid #e5e7eb;
                }

                .add-item-modal-header h3 {
                    margin: 0;
                    font-size: 18px;
                    font-weight: 600;
                    color: #111827;
                }

                .add-item-modal-body {
                    margin-top: 20px;
                }

                .add-item-modal-body label {
                    display: block;
                    font-weight: 600;
                    margin-bottom: 6px;
                    color: #111827;
                    font-size: 14px;
                }
            </style>

            <script>
                // Tính tổng tiền tự động
                function calculateTotal() {
                    const quantity = parseFloat(document.getElementById('quantityInput').value) || 0;
                    const unitPrice = parseFloat(document.getElementById('unitPriceInput').value) || 0;
                    const total = quantity * unitPrice;

                    // Format số với dấu phẩy
                    document.getElementById('totalPriceInput').value = total.toLocaleString('vi-VN') + ' VND';
                }

                // Tự động điền đơn giá khi chọn sản phẩm
                document.addEventListener('DOMContentLoaded', function () {
                    const productSelect = document.getElementById('productSelect');
                    const unitPriceInput = document.getElementById('unitPriceInput');

                    if (productSelect) {
                        productSelect.addEventListener('change', function () {
                            const selectedOption = this.options[this.selectedIndex];
                            if (selectedOption && selectedOption.dataset.price) {
                                unitPriceInput.value = selectedOption.dataset.price;
                                calculateTotal();
                            }
                        });
                    }
                });
            </script>
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
