<%-- 
    Document   : managerCarmaintenanace
    Created on : Oct 23, 2025, 4:19:29 PM
    Author     : MinHeee
--%>

<%@ page contentType="text/html; charset=UTF-8" %>
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
            <!-- Sidebar include -->
            <jsp:include page="/view/layout/sidebar.jsp"/>

            <main class="main">
                <h2>Quản lý lịch hẹn dịch vụ</h2>

                <!-- Nút mở popup -->
                <button type="button" class="btn btn-add" onclick="document.getElementById('addModal').style.display = 'flex'">
                    ➕ Tạo lịch hẹn mới
                </button>

                <!-- Bộ lọc -->
                <form action="#" method="get" style="margin-top: 10px; display:flex; gap:15px; align-items:center;">
                    <label for="statusFilter">Trạng thái:</label>
                    <select id="statusFilter">
                        <option value="">Tất cả</option>
                        <option value="PENDING">Pending</option>
                        <option value="CONFIRMED">Confirmed</option>
                        <option value="CANCELLED">Cancelled</option>
                    </select>

                    <label for="packageFilter">Gói bảo dưỡng:</label>
                    <select id="packageFilter">
                        <option value="">Tất cả</option>
                        <option value="1">PKG001 - Gói cơ bản</option>
                        <option value="2">PKG002 - Gói nâng cao</option>
                    </select>

                    <button type="button">Làm mới</button>
                </form>

                <br/>

                <!-- Bảng lịch hẹn -->
                <table>
                    <thead>
                        <tr>
                            <th>MaintenanceID</th>
                            <th>AppointmentID</th>
                            <th>Customer</th>
                            <th>License Plate</th>
                            <th>Hãng xe</th>
                            <th>DateAppointment</th>
                            <th>Status</th>
                            <th>Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td>1</td>
                            <td>Toyota Vios - 30A-12345</td>
                            <td>2025-10-25 08:00</td>
                            <td>PKG001</td>
                            <td>PENDING</td>
                            <td>Kiểm tra phanh</td>
                            <td>
                                <button class="btn btn-confirm">Xác nhận</button>
                                <button class="btn btn-cancel">Hủy</button>
                            </td>
                        </tr>
                        <tr>
                            <td>2</td>
                            <td>Honda City - 29B-88888</td>
                            <td>2025-10-25 10:00</td>
                            <td>PKG002</td>
                            <td>CONFIRMED</td>
                            <td>Thay nhớt</td>
                            <td>
                                <button class="btn btn-cancel">Xem</button>
                            </td>
                        </tr>
                    </tbody>
                </table>

                <!-- Modal thêm lịch hẹn -->
                <div id="addModal" class="modal">
                    <div class="modal-content">
                        <span class="close" onclick="document.getElementById('addModal').style.display = 'none'">&times;</span>
                        <h3>Thêm lịch hẹn mới</h3>

                        <form action="#" method="post">
                            <label>Khách hàng:</label>
                            <select id="ownerSelect" required>
                                <option value="">-- Chọn khách hàng --</option>
                                <option value="1">Nguyễn Văn A</option>
                                <option value="2">Trần Thị B</option>
                            </select>

                            <label>Xe:</label>
                            <select id="carSelect" required>
                                <option value="">-- Chọn xe --</option>
                                <option value="1" data-owner="1">Toyota Vios - 30A-12345</option>
                                <option value="2" data-owner="2">Honda City - 29B-88888</option>
                            </select>

                            <label>Gói bảo dưỡng:</label>
                            <select required>
                                <option value="1">PKG001 - Gói cơ bản</option>
                                <option value="2">PKG002 - Gói nâng cao</option>
                            </select>

                            <label>Ngày hẹn:</label>
                            <input type="datetime-local" required>

                            <label>Ghi chú:</label>
                            <textarea rows="3"></textarea>

                            <button type="submit" class="btn btn-confirm" style="margin-top:10px;">Thêm mới</button>
                        </form>
                    </div>
                </div>

                <!-- Modal chi tiết lịch hẹn -->
                <div id="detailModal" class="modal">
                    <div class="modal-content">
                        <span class="close" onclick="document.getElementById('detailModal').style.display = 'none'">&times;</span>
                        <h3>Chi tiết lịch hẹn</h3>

                        <p><strong>👤 Tên khách hàng:</strong> Nguyễn Văn A</p>
                        <p><strong>📧 Email:</strong> a@example.com</p>
                        <p><strong>📞 SĐT:</strong> 0901234567</p>
                        <p><strong>🚗 Xe:</strong> Toyota Vios (30A-12345)</p>
                        <p><strong>📅 Ngày hẹn:</strong> 2025-10-25 08:00</p>
                        <p><strong>✅ Trạng thái:</strong> Confirmed</p>
                        <p><strong>💬 Ghi chú:</strong> Kiểm tra phanh</p>

                        <div style="text-align:center; margin-top:15px;">
                            <button class="btn btn-cancel" onclick="document.getElementById('detailModal').style.display = 'none'">Đóng</button>
                        </div>
                    </div>
                </div>
            </main>
        </div>

        <script>
            window.onclick = function (event) {
                const addModal = document.getElementById("addModal");
                const detailModal = document.getElementById("detailModal");
                if (event.target === addModal)
                    addModal.style.display = "none";
                if (event.target === detailModal)
                    detailModal.style.display = "none";
            };
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
