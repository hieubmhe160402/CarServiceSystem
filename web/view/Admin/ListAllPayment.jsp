<%-- 
    Document   : ListAllPayment
    Created on : 9 thg 11, 2025, 11:35:01
    Author     : phamp
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
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
                background: linear-gradient(180deg, #0f2340, #0b1830);
                color: #fff;
                padding: 28px 18px;
                display: flex;
                flex-direction: column;
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
                box-shadow: 0 0 0 2px rgba(37, 99, 235, 0.2);
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

            th,
            td {
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
                background: rgba(0, 0, 0, 0.5);
                overflow-y: auto;
            }

            .modal-content {
                background: white;
                margin: 5% auto;
                padding: 30px;
                width: 500px;
                border-radius: 10px;
                position: relative;
                box-shadow: 0 5px 20px rgba(0, 0, 0, 0.15);
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
                background: rgba(0, 0, 0, 0.5);
                overflow-y: auto;
            }

            /* Hộp nội dung chính */
            .modal-content {
                background: #fff;
                margin: 3% auto;
                border-radius: 10px;
                width: 75%;
                max-width: 1000px;
                box-shadow: 0 5px 25px rgba(0, 0, 0, 0.25);
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

            .form-group input,
            .form-group textarea,
            .form-group select {
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
            <jsp:include page="/view/layout/sidebar.jsp" />

            <div class="main">
                <div class="container">
                    <h2>Quản lý giao dịch thanh toán</h2>

                    <!-- Hiển thị thông báo -->
                    <c:if test="${not empty success}">
                        <div
                            style="background: #d1fae5; color: #065f46; padding: 12px; border-radius: 6px; margin-bottom: 15px;">
                            ${success}
                        </div>
                    </c:if>
                    <c:if test="${not empty error}">
                        <div
                            style="background: #fee2e2; color: #991b1b; padding: 12px; border-radius: 6px; margin-bottom: 15px;">
                            ${error}
                        </div>
                    </c:if>

                    <div class="top-bar">
                        <form action="payments" method="get"
                              style="display: flex; align-items: center; gap: 10px;">
                            <input type="text" name="search" class="search-box"
                                   placeholder="Tìm kiếm theo biển số xe..." value="${searchKeyword}">
                            <select name="status" id="statusFilter" class="status-filter"
                                    onchange="this.form.submit()">
                                <option value="">Tất cả</option>
                                <option value="PENDING" ${selectedStatus=='PENDING' ? 'selected'
                                                          : '' }>PENDING</option>
                                <option value="DONE" ${selectedStatus=='DONE' ? 'selected' : '' }>
                                    Hoàn thành</option>
                            </select>
                            <button type="submit" class="btn"
                                    style="background: #3b82f6; color: white;">Tìm kiếm</button>
                            <button type="button" class="btn btn-reload"
                                    onclick="window.location.href = 'payments'">🔄 Tải lại</button>
                        </form>
                    </div>

                    <table id="maintenanceTable">
                        <thead>
                            <tr>
                                <th>TransactionID</th>
                                <th>LicensePlate</th>
                                <th>PaidAmount</th>
                                <th>PaymentMethod</th>
                                <th>PaymentDate</th>
                                <th>Status</th>
                                <th>Hành động</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:choose>
                                <c:when test="${not empty payments}">
                                    <c:forEach var="payment" items="${payments}">
                                        <tr>
                                            <td>${payment.transactionId}</td>
                                            <td>${payment.licensePlate != null ?
                                                  payment.licensePlate : '-'}</td>
                                            <td>
                                                <fmt:formatNumber value="${payment.paidAmount}"
                                                                  type="number" groupingUsed="true" /> VND
                                            </td>
                                            <td>
                                                <c:choose>
                                                    <c:when
                                                        test="${payment.paymentMethod eq 'CASH'}">
                                                        <span
                                                            style="background: #10b981; color: white; padding: 4px 8px; border-radius: 4px; font-size: 12px;">Tiền
                                                            mặt</span>
                                                        </c:when>
                                                        <c:when
                                                            test="${payment.paymentMethod eq 'TRANSFER'}">
                                                        <span
                                                            style="background: #3b82f6; color: white; padding: 4px 8px; border-radius: 4px; font-size: 12px;">Chuyển
                                                            khoản</span>
                                                        </c:when>
                                                        <c:otherwise>
                                                            ${payment.paymentMethod}
                                                        </c:otherwise>
                                                    </c:choose>
                                            </td>
                                            <td>${payment.paymentDate}</td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${payment.status eq 'DONE'}">
                                                        <span
                                                            style="background: #10b981; color: white; padding: 4px 8px; border-radius: 4px; font-size: 12px;">Hoàn
                                                            thành</span>
                                                        </c:when>
                                                        <c:when test="${payment.status eq 'PENDING'}">
                                                        <span
                                                            style="background: #f59e0b; color: white; padding: 4px 8px; border-radius: 4px; font-size: 12px;">PENDING</span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span
                                                            style="background: #6b7280; color: white; padding: 4px 8px; border-radius: 4px; font-size: 12px;">${payment.status
                                                                                                                                                            != null ? payment.status :
                                                                                                                                                            'PENDING'}</span>
                                                        </c:otherwise>
                                                    </c:choose>
                                            </td>
                                            <td>
                                                <form method="get" action="payments"
                                                      style="display:inline;">
                                                    <input type="hidden" name="action"
                                                           value="details" />
                                                    <input type="hidden" name="transactionId"
                                                           value="${payment.transactionId}" />
                                                    <input type="hidden" name="search"
                                                           value="${searchKeyword}" />
                                                    <input type="hidden" name="status"
                                                           value="${selectedStatus}" />
                                                    <button type="submit" class="btn btn-edit">Chi
                                                        tiết</button>
                                                </form>
                                                <c:if test="${payment.status ne 'DONE'}">
                                                    <form method="post" action="payments"
                                                          style="display:inline; margin-left: 5px;"
                                                          onsubmit="return confirm('Bạn có chắc chắn đã nhận đủ tiền?');">
                                                        <input type="hidden" name="action"
                                                               value="markAsDone" />
                                                        <input type="hidden" name="transactionId"
                                                               value="${payment.transactionId}" />
                                                        <input type="hidden" name="search"
                                                               value="${searchKeyword}" />
                                                        <input type="hidden" name="status"
                                                               value="${selectedStatus}" />
                                                        <button type="submit" class="btn"
                                                                style="background: #10b981; color: white; padding: 6px 12px;">
                                                            Đã nhận tiền
                                                        </button>
                                                    </form>
                                                </c:if>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <tr>
                                        <td colspan="7" style="text-align: center; padding: 20px;">
                                            Không có dữ liệu thanh toán
                                        </td>
                                    </tr>
                                </c:otherwise>
                            </c:choose>
                        </tbody>
                    </table>

                    <c:if test="${not empty detail}">
                        <c:url var="closePaymentsUrl" value="/payments">
                            <c:if test="${not empty searchKeyword}">
                                <c:param name="search" value="${searchKeyword}" />
                            </c:if>
                            <c:if test="${not empty selectedStatus}">
                                <c:param name="status" value="${selectedStatus}" />
                            </c:if>
                        </c:url>

                        <div id="serviceModal" class="modal" style="display:block;">

                            <div class="modal-content">

                                <div class="modal-header">

                                    <h2>Phiếu Thanh Toán</h2>

                                    <span class="status-badge
                                          ${detail.status eq 'WAITING' ? 'waiting' : 
                                            (detail.status eq 'PROCESSING' ? 'processing' : '')}">
                                              ${detail.status}
                                          </span>

                                          <a href="${closePaymentsUrl}" class="close">&times;</a>

                                    </div>

                                    <div class="modal-body">

                                        <div class="grid">

                                            <div class="form-group">
                                                <label>Người tạo</label>
                                                <input type="text" value="${detail.maintenanceId}" readonly />
                                            </div>

                                            <div class="form-group">
                                                <label>Phương thức</label>
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
                                                <input type="text" value="${detail.car.brand}" readonly />
                                            </div>

                                            <div class="form-group">
                                                <label>Odometer</label>
                                                <input type="text" value="${detail.odometer}" readonly />
                                            </div>

                                            <div class="form-group">
                                                <label>Tổng số tiền</label>
                                                <input type="text" value="${detail.assignedTechnician.userId}" readonly />
                                            </div>

                                        </div>


                                        <div class="form-group full">
                                            <h3 style="margin-bottom: 10px;">Danh sách dịch vụ sử dụng</h3>

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
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <c:choose>
                                                        <c:when test="${not empty products}">
                                                            <c:set var="prevPackage" value="" />
                                                            <c:set var="rowCount" value="0" />
                                                            <c:forEach var="item" items="${products}" varStatus="loop">
                                                                <c:if test="${item.packageCode ne prevPackage}">
                                                                    <c:set var="rowCount" value="0" />
                                                                    <c:forEach var="inner" items="${products}">
                                                                        <c:if test="${inner.packageCode eq item.packageCode}">
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
                                                                        <td rowspan="${rowCount}">
                                                                            <fmt:formatNumber value="${item.basePrice}" type="number" groupingUsed="true"/> VND
                                                                        </td>
                                                                        <td rowspan="${rowCount}">
                                                                            ${item.discountPercent}%
                                                                        </td>
                                                                        <td rowspan="${rowCount}">
                                                                            <fmt:formatNumber value="${item.finalPrice}" type="number" groupingUsed="true"/> VND
                                                                        </td>
                                                                    </c:if>
                                                                </tr>
                                                                <c:set var="prevPackage" value="${item.packageCode}" />
                                                            </c:forEach>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <tr>
                                                                <td colspan="6" style="text-align:center;">Không có sản phẩm nào trong phiếu bảo dưỡng này</td>
                                                            </tr>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </tbody>
                                            </table>

                                            <div style="text-align:right; margin-top:10px; font-weight:bold;">
                                                Tổng tiền:
                                                <fmt:formatNumber value="${detail.finalAmount}" type="number" groupingUsed="true"/> VND
                                            </div>

                                            <div style="text-align:right; margin-top:30px;">
                                                 <a href="${closePaymentsUrl}" class="btn btn-danger"
                                                    style="background-color:#dc3545; border:none; padding:8px 18px; border-radius:6px; color:white;">
                                                     Đóng
                                                 </a>
                                            </div>

                                        </div>

                                    </div>

                                </div>

                            </div>

                        </c:if>
                    </div>
                </div>
            </div>
        </body>

    </html>