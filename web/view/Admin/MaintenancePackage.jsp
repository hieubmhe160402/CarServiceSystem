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
                width: 800px;
                box-shadow: 0 4px 10px rgba(0,0,0,0.25);
                animation: fadeIn 0.3s ease;
            }
            .form-row {
                display: grid;
                grid-template-columns: 1fr 1fr; /* Chia thành 2 cột bằng nhau */
                gap: 0 20px; /* 0px cho khoảng cách dọc, 20px cho ngang */
            }

            .form-full-width {
                grid-column: 1 / -1; /* Làm cho trường này rộng hết 2 cột */
            }
            /* ✅ Form styling theo pattern MaintenancePackageDetails */
            .form-group {
                display: flex;
                flex-direction: column;
                margin-bottom: 12px;
            }

            .form-group label {
                font-weight: 600;
                margin-bottom: 5px;
            }

            .form-group input,
            .form-group select,
            .form-group textarea {
                padding: 8px 10px;
                border: 1px solid #ccc;
                border-radius: 5px;
            }

            .error {
                color: red;
                font-size: 12px;
                margin-top: 3px;
            }

            /* ✅ Enhanced form styling */
            .form-group input:focus,
            .form-group select:focus,
            .form-group textarea:focus {
                outline: none;
                border-color: #007bff;
                box-shadow: 0 0 0 2px rgba(0, 123, 255, 0.25);
            }

            .form-group input[readonly] {
                background-color: #f8f9fa !important;
                cursor: not-allowed;
            }

            .form-group small {
                color: #6c757d;
                font-size: 12px;
                margin-top: 2px;
            }

            /* ✅ Search & Filter theo pattern MaintenancePackageDetails */
            .search-bar {
                display: flex;
                justify-content: space-between;
                align-items: center;
                margin-bottom: 20px;
            }
            .search-form {
                display: flex;
                align-items: center;
                gap: 10px;
            }
            .search-form input, .search-form select {
                padding: 8px 10px;
                border: 1px solid #ccc;
                border-radius: 6px;
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

            .error-message {
                color: #dc3545;
                font-size: 12px;
                margin-top: 2px;
            }

            .success-message {
                color: #28a745;
                font-size: 12px;
                margin-top: 2px;
            }
        </style>

        <script>
            // ✅ PackageCode validation
            function validatePackageCode() {
                const packageCodeInput = document.getElementById('packageCode');
                const packageCode = packageCodeInput.value.trim().toUpperCase();

                // Remove existing error message
                const existingError = document.getElementById('packageCodeError');
                if (existingError) {
                    existingError.textContent = '';
                }

                if (packageCode && !packageCode.startsWith('PKG-')) {
                    if (existingError) {
                        existingError.textContent = 'Package Code phải bắt đầu bằng "PKG-"';
                    }
                    packageCodeInput.style.borderColor = '#dc3545';
                    return false;
                } else {
                    packageCodeInput.style.borderColor = '#28a745';
                    packageCodeInput.value = packageCode; // Normalize to uppercase
                    return true;
                }
            }

            // ✅ FinalPrice được tính tự động từ database (computed column)

            // ✅ Form validation before submit
            function validateForm() {
                const packageCodeValid = validatePackageCode();
                const basePrice = document.getElementById('basePrice').value;
                const name = document.querySelector('input[name="name"]').value;

                if (!packageCodeValid) {
                    alert('Vui lòng kiểm tra Package Code!');
                    return false;
                }

                if (!basePrice || parseFloat(basePrice) <= 0) {
                    alert('BasePrice phải lớn hơn 0!');
                    return false;
                }

                if (!name.trim()) {
                    alert('Package Name không được để trống!');
                    return false;
                }

                return true;
            }

            // ✅ Auto-calculate on page load
            document.addEventListener('DOMContentLoaded', function () {
                // FinalPrice sẽ được tính tự động từ DB, không cần calculate ở frontend
            });

            // ✅ Update modal functions - FinalPrice được tính tự động từ database

            function validateUpdateForm(packageCode) {
                const basePrice = document.getElementById('updateBasePrice_' + packageCode).value;
                const name = document.getElementById('updateName_' + packageCode).value;

                if (!basePrice || parseFloat(basePrice) <= 0) {
                    alert('BasePrice phải lớn hơn 0!');
                    return false;
                }

                if (!name.trim()) {
                    alert('Package Name không được để trống!');
                    return false;
                }

                return true;
            }
        </script>
    </head>
    <body>
        <div class="app">
            <jsp:include page="/view/layout/sidebar.jsp"/>

            <main class="main">
                <h2 style="margin-bottom: 20px">Quản lý Combo</h2>

                <!-- ✅ Error/Success Messages -->
                <c:if test="${not empty error}">
                    <div style="background-color: #f8d7da; color: #721c24; padding: 10px; border-radius: 5px; margin-bottom: 15px;">
                        ❌ ${error}
                    </div>
                </c:if>
                <c:if test="${not empty success}">
                    <div style="background-color: #d4edda; color: #155724; padding: 10px; border-radius: 5px; margin-bottom: 15px;">
                        ✅ ${success}
                    </div>
                </c:if>

                <!-- ✅ Search & Filter theo pattern MaintenancePackageDetails -->
                <div class="search-bar">
                    <form class="search-form" action="maintenancePackage" method="get">
                        <label>Tên gói:</label>
                        <input type="text" name="keyword" placeholder="Nhập tên gói hoặc mã gói..." value="${currentKeyword}" />
                        <label>Trạng thái:</label>
                        <select name="status">
                            <option value="" ${empty currentStatus ? "selected" : ""}>Tất cả</option>
                            <option value="true" ${currentStatus == "true" ? "selected" : ""}>Hoạt động</option>
                            <option value="false" ${currentStatus == "false" ? "selected" : ""}>Không hoạt động</option>
                        </select>
                        <button type="submit" class="btn btn-active">Tìm</button>
                        <button type="button" class="btn btn-active" onclick="window.location.href = 'maintenancePackage'">Tải lại</button>
                    </form>
                    <button class="btn btn-add" onclick="document.getElementById('addComboModal').style.display = 'flex'">+ Thêm Combo</button>
                </div>

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

                        <!--                                    //Add maitenancePackage-->
                        <div id="addComboModal" class="modal">
                            <div class="modal-content" style="width: 800px;">
                                <span class="close-btn" onclick="document.getElementById('addComboModal').style.display = 'none'">&times;</span>
                                <h3 style="text-align:center; border-bottom:2px solid #28a745; padding-bottom:8px;">Thêm Combo Mới</h3>

                                <form action="maintenancePackage" method="post" enctype="multipart/form-data" onsubmit="return validateForm()">
                                    <input type="hidden" name="action" value="addCombo">

                                    <%-- Bọc các trường trong form-row --%>
                                    <div class="form-row">

                                        <%-- CỘT BÊN TRÁI --%>
                                        <div>
                                            <div class="form-group">
                                                <label>Mã gói *</label>
                                                <input type="text" name="packageCode" id="packageCode" required 
                                                       placeholder="PKG-001" onblur="validatePackageCode()">
                                                <span class="error" id="packageCodeError"></span>
                                            </div>

                                            <div class="form-group">
                                                <label>Tên gói *</label>
                                                <input type="text" name="name" required>
                                            </div>

                                            <div class="form-group">
                                                <label>Cột mốc km *</label>
                                                <input type="number" name="kilometerMilestone" required>
                                            </div>

                                            <div class="form-group">
                                                <label>Cột mốc tháng *</label>
                                                <input type="number" name="monthMilestone" required>
                                            </div>

                                            <div class="form-group">
                                                <label>Giá gốc *</label>
                                                <input type="number" name="basePrice" id="basePrice" required 
                                                       title="FinalPrice sẽ được tính tự động từ BasePrice và DiscountPercent">
                                            </div>

                                            <div class="form-group">
                                                <label>Trạng thái</label>
                                                <select name="isActive">
                                                    <option value="true" selected>Hoạt động</option>
                                                    <option value="false">Không hoạt động</option>
                                                </select>
                                            </div>
                                        </div>

                                        <%-- CỘT BÊN PHẢI --%>
                                        <div>
                                            <div class="form-group">
                                                <label>Phần trăm giảm giá (%)</label>
                                                <input type="number" name="discountPercent" id="discountPercent" 
                                                       min="0" max="100" value="0" 
                                                       title="FinalPrice sẽ được tính tự động từ BasePrice và DiscountPercent">
                                            </div>

                                            <div class="form-group">
                                                <label>Giá cuối cùng</label>
                                                <input type="text" name="finalPrice" id="finalPrice" 
                                                       readonly style="background-color:#f8f9fa;" 
                                                       placeholder="Sẽ được tính tự động từ DB" 
                                                       title="FinalPrice được tính tự động từ BasePrice và DiscountPercent trong database">
                                            </div>

                                            <div class="form-group">
                                                <label>Thời gian ước tính (giờ) *</label>
                                                <input type="number" name="estimatedDurationHours" required>
                                            </div>

                                            <div class="form-group">
                                                <label>Thương hiệu áp dụng *</label>
                                                <input type="text" name="applicableBrand" required>
                                            </div>

                                            <div class="form-group">
                                                <label>Thứ tự hiển thị</label>
                                                <input type="number" name="displayOrder" min="1">
                                            </div>

                                            <div class="form-group">
                                                <label>Người tạo</label>
                                                <input type="text" name="createdBy" value="${sessionScope.user.userId}" readonly>
                                            </div>
                                        </div>
                                    </div> <%-- Kết thúc .form-row --%>

                                    <%-- CÁC TRƯỜNG FULL-WIDTH --%>
                                    <div class="form-group form-full-width">
                                        <label>Mô tả</label>
                                        <textarea name="description" rows="3"></textarea>
                                    </div>

                                    <div class="form-group form-full-width">
                                        <label>Hình ảnh</label>
                                        <input type="file" name="image" accept="image/*">
                                    </div>

                                    <div style="display:flex;justify-content:flex-end;gap:10px; margin-top: 15px;">
                                        <button type="submit" class="btn btn-add">Lưu</button>
                                        <button type="button" class="btn btn-delete" onclick="document.getElementById('addComboModal').style.display = 'none'">Hủy</button>
                                    </div>
                                </form>
                            </div>
                        </div       

                        <!-- ✅ UPDATE MODAL -->
                        <div id="update_${d.maintenancePackage.packageCode}" class="modal">
                            <div class="modal-content" style="width: 800px;">
                                <span class="close-btn" onclick="document.getElementById('update_${d.maintenancePackage.packageCode}').style.display = 'none'">&times;</span>
                                <h3 style="text-align:center; border-bottom:2px solid #ffc107; padding-bottom:8px;">Cập nhật Combo</h3>

                                <form action="maintenancePackage" method="post" enctype="multipart/form-data" onsubmit="return validateUpdateForm('${d.maintenancePackage.packageCode}')">
                                    <input type="hidden" name="action" value="updateCombo">
                                    <input type="hidden" name="packageId" value="${d.maintenancePackage.packageId}">

                                    <div class="form-row">

                                        <div>
                                            <div class="form-group">
                                                <label>PackageCode</label>
                                                <input type="text" name="packageCode" id="updatePackageCode_${d.maintenancePackage.packageCode}" 
                                                       value="${d.maintenancePackage.packageCode}" readonly 
                                                       style="background-color:#f8f9fa;">
                                            </div>

                                            <div class="form-group">
                                                <label>PackageName *</label>
                                                <input type="text" name="name" id="updateName_${d.maintenancePackage.packageCode}" 
                                                       value="${d.maintenancePackage.name}" required>
                                            </div>

                                            <div class="form-group">
                                                <label>KilometerMilestone *</label>
                                                <input type="number" name="kilometerMilestone" id="updateKm_${d.maintenancePackage.packageCode}" 
                                                       value="${d.maintenancePackage.kilometerMilestone}" required>
                                            </div>

                                            <div class="form-group">
                                                <label>MonthMilestone *</label>
                                                <input type="number" name="monthMilestone" id="updateMonth_${d.maintenancePackage.packageCode}" 
                                                       value="${d.maintenancePackage.monthMilestone}" required>
                                            </div>

                                            <div class="form-group">
                                                <label>BasePrice *</label>
                                                <input type="number" name="basePrice" id="updateBasePrice_${d.maintenancePackage.packageCode}" 
                                                       value="${d.maintenancePackage.basePrice}" required>
                                            </div>
                                        </div>

                                        <div>
                                            <div class="form-group">
                                                <label>FinalPrice</label>
                                                <input type="text" name="finalPrice" id="updateFinalPrice_${d.maintenancePackage.packageCode}" 
                                                       readonly style="background-color:#f8f9fa;" 
                                                       value="${d.maintenancePackage.finalPrice}">
                                            </div>

                                            <div class="form-group">
                                                <label>EstimatedDuration *</label>
                                                <input type="number" name="estimatedDurationHours" id="updateDuration_${d.maintenancePackage.packageCode}" 
                                                       value="${d.maintenancePackage.estimatedDurationHours}" required>
                                            </div>

                                            <div class="form-group">
                                                <label>ApplicableBrand *</label>
                                                <input type="text" name="applicableBrand" id="updateBrand_${d.maintenancePackage.packageCode}" 
                                                       value="${d.maintenancePackage.applicableBrands}" required>
                                            </div>

                                            <div class="form-group">
                                                <label>DisplayOrder</label>
                                                <input type="number" name="displayOrder" id="updateOrder_${d.maintenancePackage.packageCode}" 
                                                       value="${d.maintenancePackage.displayOrder}" min="1">
                                            </div>

                                            <div class="form-group">
                                                <label>DiscountPercent</label>
                                                <input type="number" name="discountPercent" id="updateDiscountPercent_${d.maintenancePackage.packageCode}" 
                                                       value="${d.maintenancePackage.discountPercent}" min="0" max="100">
                                            </div>
                                        </div>
                                    </div> <div class="form-group form-full-width">
                                        <label>Trạng thái</label>
                                        <select name="isActive" id="updateStatus_${d.maintenancePackage.packageCode}">
                                            <option value="true" ${d.maintenancePackage.isActive ? 'selected' : ''}>Active</option>
                                            <option value="false" ${!d.maintenancePackage.isActive ? 'selected' : ''}>Inactive</option>
                                        </select>
                                    </div>

                                    <div class="form-group form-full-width">
                                        <label>Người tạo</label>
                                        <input type="text" name="createdBy" value="${sessionScope.user.userId}" readonly style="background-color:#f8f9fa;">
                                    </div>

                                    <div class="form-group form-full-width">
                                        <label>Description</label>
                                        <textarea name="description" rows="3">${d.maintenancePackage.description}</textarea>
                                    </div>

                                    <div class="form-group image-upload-section form-full-width">
                                        <label>Hình ảnh</label>
                                        <input type="file" name="image" accept="image/*">
                                        <small style="color: #666;">Để trống nếu không muốn thay đổi ảnh</small>
                                        <c:if test="${not empty d.maintenancePackage.image}">
                                            <img src="${d.maintenancePackage.image}" alt="Current image" class="image-preview">
                                        </c:if>
                                    </div>

                                    <div style="display:flex;justify-content:flex-end;gap:10px; margin-top: 15px;">
                                        <button type="submit" class="btn btn-edit">💾 Lưu thay đổi</button>
                                        <button type="button" class="btn btn-delete" onclick="document.getElementById('update_${d.maintenancePackage.packageCode}').style.display = 'none'">Đóng</button>
                                    </div>
                                </form>
                            </div>
                        </div>  

                    </c:if>
                </c:forEach>

            </main>
        </div>
