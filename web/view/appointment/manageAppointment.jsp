<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8" />
        <title>Quản lý lịch hẹn dịch vụ</title>
        <style>
            * {
                box-sizing: border-box;
                margin: 0;
                padding: 0;
            }
            html, body {
                height: 100%;
                font-family: Inter, Roboto, Arial, sans-serif;
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

            .main {
                flex: 1;
                padding: 24px 32px;
                overflow: auto;
            }

            h2 {
                margin-bottom: 20px;
            }

            .btn {
                padding: 6px 12px;
                border: none;
                border-radius: 6px;
                margin: 0 3px;
                cursor: pointer;
                font-size: 14px;
            }
            .btn-add {
                background: #28a745;
                color: white;
            }
            .btn-confirm {
                background: #007bff;
                color: white;
            }
            .btn-cancel {
                background: #dc3545;
                color: white;
            }

            table {
                width: 100%;
                border-collapse: collapse;
                background: #fff;
                border-radius: 8px;
                overflow: hidden;
                box-shadow: 0 2px 6px rgba(0,0,0,0.05);
            }
            table thead th {
                background: #f8fafc;
                padding: 12px 16px;
                text-align: left;
                border-bottom: 1px solid #e5e7eb;
            }
            table tbody td {
                padding: 12px 16px;
                border-bottom: 1px solid #f1f5f9;
            }

            /* MODAL */
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
                z-index: 1000;
            }

            .modal-content {
                background: #fff;
                padding: 20px;
                border-radius: 8px;
                width: 400px;
                box-shadow: 0 4px 10px rgba(0,0,0,0.3);
            }

            .modal-content h3 {
                margin-bottom: 15px;
                text-align: center;
            }

            .modal-content label {
                display: block;
                font-weight: 600;
                margin-top: 10px;
            }

            .modal-content select,
            .modal-content input,
            .modal-content textarea {
                width: 100%;
                padding: 8px;
                margin-top: 5px;
                border: 1px solid #ccc;
                border-radius: 5px;
            }

            .close {
                float: right;
                font-size: 20px;
                cursor: pointer;
            }

            .close:hover {
                color: red;
            }

        </style>
    </head>
    <body>
        <div class="app">
            <jsp:include page="/view/layout/sidebar.jsp"/>

            <main class="main">
                <h2>Quản lý lịch hẹn dịch vụ</h2>

                <!-- Nút mở popup -->
                <button type="button" class="btn btn-add"
                        onclick="document.getElementById('addModal').style.display = 'flex'">
                    ➕ Tạo lịch hẹn mới
                </button>

                <!-- Bộ lọc -->
                <form action="listAppointmentServlet" method="get"
                      style="margin-top: 10px; display:flex; gap:15px; align-items:center;">
                    <label for="statusFilter">Trạng thái:</label>
                    <select name="status" id="statusFilter" onchange="this.form.submit()">
                        <option value="" ${empty param.status ? 'selected' : ''}>Tất cả</option>
                        <option value="PENDING" ${param.status == 'PENDING' ? 'selected' : ''}>Pending</option>
                        <option value="CONFIRMED" ${param.status == 'CONFIRMED' ? 'selected' : ''}>Confirmed</option>
                        <option value="CANCELLED" ${param.status == 'CANCELLED' ? 'selected' : ''}>Cancelled</option>
                    </select>

                    <label for="packageFilter">Gói bảo dưỡng:</label>
                    <select name="packageId" id="packageFilter" onchange="this.form.submit()">
                        <option value="" ${empty param.packageId ? 'selected' : ''}>Tất cả</option>
                        <c:forEach var="pkg" items="${packages}">
                            <option value="${pkg.packageId}" ${param.packageId == pkg.packageId ? 'selected' : ''}>
                                ${pkg.packageCode} - ${pkg.name}
                            </option>
                        </c:forEach>
                    </select>

                    <button type="button" onclick="window.location.href = 'listAppointmentServlet'">Làm mới</button>
                </form>


                <br/>

                <!-- Bảng lịch hẹn -->
                <table>
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Xe</th>
                            <th>Ngày hẹn</th>
                            <th>Gói bảo dưỡng</th>
                            <th>Trạng thái</th>
                            <th>Ghi chú</th>
                            <th>Hành động</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:choose>
                            <c:when test="${not empty appointments}">
                                <c:forEach var="a" items="${appointments}">
                                    <tr>
                                        <td>${a.appointmentId}</td>
                                        <td>${a.car.brand} ${a.car.model} - ${a.car.licensePlate}</td>
                                        <td>${a.appointmentDate}</td>
                                        <td>${a.requestedPackage.packageCode}</td>
                                        <td>${a.status}</td>
                                        <td>${a.notes}</td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${a.status eq 'PENDING'}">
                                                    <form action="listAppointmentServlet" method="post" style="display:inline;">
                                                        <input type="hidden" name="appointmentId" value="${a.appointmentId}">
                                                        <input type="hidden" name="status" value="${selectedStatus}">
                                                        <input type="hidden" name="packageId" value="${selectedPackageId}">
                                                        <button type="submit" name="action" value="confirm" class="btn btn-confirm">Xác nhận</button>
                                                    </form>

                                                    <form action="listAppointmentServlet" method="post" style="display:inline;">
                                                        <input type="hidden" name="appointmentId" value="${a.appointmentId}">
                                                        <input type="hidden" name="status" value="${selectedStatus}">
                                                        <input type="hidden" name="packageId" value="${selectedPackageId}">
                                                        <button type="submit" name="action" value="cancel" class="btn btn-danger">Hủy</button>
                                                    </form>
                                                </c:when>

                                                <c:when test="${a.status eq 'CONFIRMED'}">
                                                    <form action="listAppointmentServlet" method="post" style="display:inline;">
                                                        <input type="hidden" name="appointmentId" value="${a.appointmentId}">
                                                        <input type="hidden" name="status" value="${selectedStatus != null ? selectedStatus : ''}">
                                                        <input type="hidden" name="packageId" value="${selectedPackageId != null ? selectedPackageId : ''}">
                                                        <button type="submit" name="action" class="btn btn-cancel" value="view">View</button>
                                                    </form>
                                                </c:when>
                                                <c:otherwise>
                                                    <button class="btn" disabled>Không có hành động</button>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <tr>
                                    <td colspan="7" style="text-align:center; color:#6b7280;">
                                        Không có lịch hẹn nào.
                                    </td>
                                </tr>
                            </c:otherwise>
                        </c:choose>
                    </tbody>
                </table>

                <!-- Modal thêm lịch hẹn -->
                <c:if test="${showAddModal}">
                    <script>
                        document.addEventListener("DOMContentLoaded", function () {
                            const modal = document.getElementById("addModal");
                            if (modal)
                                modal.style.display = "flex"; // giữ đúng layout popup
                        });
                    </script>
                </c:if>


                <div id="addModal" class="modal">
                    <div class="modal-content">
                        <span class="close" onclick="document.getElementById('addModal').style.display = 'none'">&times;</span>
                        <h3>Thêm lịch hẹn mới</h3>

                        <form action="listAppointmentServlet" method="post">
                            <input type="hidden" name="action" value="add">

                            <!-- Chọn khách hàng -->
                            <label>Khách hàng:</label>
                            <select id="ownerSelect" required>
                                <option value="">-- Chọn khách hàng --</option>
                                <c:set var="prevOwner" value="" />
                                <c:forEach var="c" items="${cars}">
                                    <c:if test="${c.owner.fullName ne prevOwner}">
                                        <option value="${c.owner.userId}">${c.owner.fullName}</option>
                                        <c:set var="prevOwner" value="${c.owner.fullName}" />
                                    </c:if>
                                </c:forEach>
                            </select>

                            <!-- Xe -->
                            <label>Xe (hãng + model + biển số):</label>
                            <select id="carSelect" name="carId" required>
                                <option value="">-- Chọn xe --</option>
                                <c:forEach var="c" items="${cars}">
                                    <option value="${c.carId}" data-owner="${c.owner.userId}"
                                            <c:if test="${selectedCarId == c.carId}">selected</c:if>>
                                        ${c.brand} ${c.model} - ${c.licensePlate}
                                    </option>
                                </c:forEach>
                            </select>

                            <!-- Gói bảo dưỡng -->
                            <label>Gói bảo dưỡng:</label>
                            <select name="packageId" required>
                                <c:forEach var="p" items="${packages}">
                                    <option value="${p.packageId}"
                                            <c:if test="${selectedPackageId == p.packageId}">selected</c:if>>
                                        ${p.packageCode} - ${p.name}
                                    </option>
                                </c:forEach>
                            </select>

                            <!-- Ngày hẹn -->
                            <label>Ngày hẹn:</label>
                            <input type="datetime-local" name="appointmentDate" value="${enteredDate}" required>

                            <c:if test="${not empty errorTimeMessage}">
                                <div style="color:red; font-size: 13px; margin-top: 4px;">
                                    ${errorTimeMessage}
                                </div>
                            </c:if>

                            <!-- Ghi chú -->
                            <label>Ghi chú:</label>
                            <textarea name="notes" rows="3">${enteredNotes}</textarea>

                            <button type="submit" class="btn btn-confirm" style="margin-top:10px;">Thêm mới</button>
                        </form>
                    </div>
                </div>

                <!-- Modal Detail when status Confirmed -->
                <!-- Modal chi tiết lịch hẹn -->
                <c:if test="${not empty appointmentDetail}">
                    <div id="detailModal" class="modal" style="display:flex;">
                        <div class="modal-content">
                            <span class="close" onclick="document.getElementById('detailModal').style.display = 'none'">&times;</span>
                            <h3>Chi tiết lịch hẹn</h3>

                            <p><strong>👤 Tên khách hàng:</strong> ${appointmentDetail.createdBy.fullName}</p>
                            <p><strong>📧 Email:</strong> ${appointmentDetail.createdBy.email}</p>
                            <p><strong>📞 Số điện thoại:</strong> ${appointmentDetail.createdBy.phone}</p>
                            <p><strong>🎂 Ngày sinh:</strong> ${appointmentDetail.createdBy.dateOfBirth}</p>
                            <p><strong>🚹 Giới tính:</strong>
                                <c:choose>
                                    <c:when test="${appointmentDetail.createdBy.male}">Nam</c:when>
                                    <c:otherwise>Nữ</c:otherwise>
                                </c:choose>
                            </p>



                            <p><strong>🚗 Xe:</strong> ${appointmentDetail.car.brand} ${appointmentDetail.car.model} (${appointmentDetail.car.licensePlate})</p>
                            <p><strong>📅  Ngày hẹn:</strong> ${appointmentDetail.appointmentDate}</p>
                            <p><strong>🧑‍🔧 Ngày xác nhận:</strong> ${appointmentDetail.confirmedDate}</p>
                            <p><strong>✅ Trạng thái:</strong> ${appointmentDetail.status}</p>
                            <p><strong>💬 Ghi chú:</strong> ${appointmentDetail.notes}</p>

                            <div style="text-align:center; margin-top:15px;">
                                <button class="btn btn-cancel" onclick="document.getElementById('detailModal').style.display = 'none'">Đóng</button>
                            </div>
                        </div>
                    </div>
                </c:if>

                <div class="pagination" style="text-align:center; margin-top:20px;">
                    <c:if test="${totalPages > 1}">
                        <c:forEach var="i" begin="1" end="${totalPages}">
                            <c:choose>
                                <c:when test="${i == currentPage}">
                                    <span style="padding:6px 12px; background-color:#007bff; color:white; border-radius:5px;">${i}</span>
                                </c:when>
                                <c:otherwise>
                                    <a href="?page=${i}&status=${selectedStatus}&packageId=${selectedPackageId}"
                                       style="padding:6px 12px; text-decoration:none; border:1px solid #ccc; border-radius:5px; margin:0 2px;">
                                        ${i}
                                    </a>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </c:if>
                </div>

            </main>
        </div>

        <script>
            window.onclick = function (event) {
                const addModal = document.getElementById("addModal");
                const detailModal = document.getElementById("detailModal");

                if (event.target === addModal) {
                    addModal.style.display = "none";
                }

                if (event.target === detailModal) {
                    detailModal.style.display = "none";
                }
            };
        </script>
        <script>
            document.getElementById('ownerSelect').addEventListener('change', function () {
                const selectedOwner = this.value;
                const carOptions = document.querySelectorAll('#carSelect option[data-owner]');
                document.getElementById('carSelect').value = "";

                carOptions.forEach(opt => {
                    opt.style.display = (opt.getAttribute('data-owner') === selectedOwner) ? 'block' : 'none';
                });
            });
        </script>
    </body>
</html>
