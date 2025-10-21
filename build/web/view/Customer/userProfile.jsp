<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="model.Car"%>
<%@page import="java.util.List"%>
<%@page import="model.User"%>
<%
    User user = (User) request.getAttribute("user");
    List<Car> cars = (List<Car>) request.getAttribute("cars");
    String search = (String) request.getAttribute("search");
%>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>User Profile - Car Management</title>
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
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
                min-height: 100vh;
            }

            /* Main Content */
            .main {
                flex: 1;
                padding: 40px;
                overflow: auto;
                background: linear-gradient(135deg, #f5f7fb 0%, #e8ecf4 100%);
            }

            /* Profile Header Card */
            .profile-card {
                background: #fff;
                border-radius: 16px;
                padding: 35px;
                margin-bottom: 30px;
                box-shadow: 0 4px 16px rgba(0, 0, 0, 0.06);
                border: 1px solid #e5e7eb;
                display: flex;
                align-items: center;
                gap: 30px;
                position: relative;
            }

            .profile-image {
                position: relative;
            }

            .profile-image img {
                width: 120px;
                height: 120px;
                border-radius: 50%;
                border: 4px solid #e5e7eb;
                object-fit: cover;
                box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
            }

            .profile-info {
                flex: 1;
            }

            .profile-info h2 {
                font-size: 28px;
                font-weight: 800;
                margin-bottom: 16px;
                color: #0f2340;
            }

            .info-grid {
                display: grid;
                grid-template-columns: repeat(3, 1fr);
                gap: 20px;
            }

            .info-item {
                display: flex;
                flex-direction: column;
                gap: 6px;
            }

            .info-item .label {
                font-size: 12px;
                color: #6b7280;
                font-weight: 700;
                text-transform: uppercase;
                letter-spacing: 0.5px;
            }

            .info-item .value {
                font-size: 15px;
                color: #111827;
                font-weight: 600;
            }

            .info-item i {
                color: #16a34a;
                margin-right: 8px;
            }

            /* Buttons */
            .btn {
                padding: 10px 20px;
                border: none;
                border-radius: 10px;
                cursor: pointer;
                font-size: 14px;
                font-weight: 700;
                transition: all 0.3s ease;
                display: inline-flex;
                align-items: center;
                gap: 8px;
            }

            .btn-edit {
                background: linear-gradient(135deg, #ffc107 0%, #ff9800 100%);
                color: #000;
                position: absolute;
                top: 24px;
                right: 24px;
                box-shadow: 0 4px 12px rgba(255, 193, 7, 0.3);
            }

            .btn-edit:hover {
                background: linear-gradient(135deg, #ff9800 0%, #f57c00 100%);
                transform: translateY(-2px);
                box-shadow: 0 6px 20px rgba(255, 193, 7, 0.4);
            }

            .btn-add {
                background: linear-gradient(135deg, #16a34a 0%, #15803d 100%);
                color: white;
                box-shadow: 0 4px 12px rgba(22, 163, 74, 0.3);
            }

            .btn-add:hover {
                background: linear-gradient(135deg, #15803d 0%, #166534 100%);
                transform: translateY(-2px);
                box-shadow: 0 6px 20px rgba(22, 163, 74, 0.4);
            }

            .btn-update {
                background: linear-gradient(135deg, #0f2340 0%, #1e3a5f 100%);
                color: white;
                padding: 6px 14px;
                font-size: 13px;
            }

            .btn-update:hover {
                background: linear-gradient(135deg, #1e3a5f 0%, #2d4a7c 100%);
                transform: translateY(-2px);
            }

            .btn-delete {
                background: linear-gradient(135deg, #dc3545 0%, #c82333 100%);
                color: white;
                padding: 6px 14px;
                font-size: 13px;
            }

            .btn-delete:hover {
                background: linear-gradient(135deg, #c82333 0%, #bd2130 100%);
                transform: translateY(-2px);
            }

            /* Section Header */
            .section-header {
                display: flex;
                justify-content: space-between;
                align-items: center;
                margin-bottom: 25px;
                padding-bottom: 15px;
                border-bottom: 3px solid #e5e7eb;
            }

            .section-header h3 {
                font-size: 22px;
                font-weight: 800;
                color: #0f2340;
                display: flex;
                align-items: center;
                gap: 12px;
            }

            .section-header h3 i {
                color: #16a34a;
            }

            /* Search Box */
            .search-form {
                display: flex;
                gap: 12px;
                margin-bottom: 25px;
                background: white;
                padding: 20px;
                border-radius: 12px;
                box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
            }

            .search-input {
                flex: 1;
                padding: 10px 16px;
                border: 2px solid #e5e7eb;
                border-radius: 10px;
                font-size: 14px;
                transition: all 0.3s ease;
            }

            .search-input:focus {
                outline: none;
                border-color: #16a34a;
                box-shadow: 0 0 0 4px rgba(22, 163, 74, 0.1);
            }

            .btn-search {
                background: linear-gradient(135deg, #0f2340 0%, #1e3a5f 100%);
                color: white;
                padding: 10px 24px;
                border: none;
                border-radius: 10px;
                cursor: pointer;
                font-weight: 700;
                transition: all 0.3s ease;
                display: flex;
                align-items: center;
                gap: 8px;
            }

            .btn-search:hover {
                background: linear-gradient(135deg, #1e3a5f 0%, #2d4a7c 100%);
                transform: translateY(-2px);
            }

            /* Table */
            .table-card {
                background: white;
                border-radius: 16px;
                overflow: hidden;
                box-shadow: 0 4px 16px rgba(0, 0, 0, 0.06);
                border: 1px solid #e5e7eb;
            }

            table {
                width: 100%;
                border-collapse: collapse;
            }

            table thead th {
                background: linear-gradient(135deg, #0f2340 0%, #1e3a5f 100%);
                color: white;
                padding: 18px 16px;
                text-align: left;
                font-weight: 700;
                font-size: 14px;
                text-transform: uppercase;
                letter-spacing: 0.5px;
            }

            table tbody td {
                padding: 16px;
                border-bottom: 1px solid #f3f4f6;
                color: #374151;
                font-size: 14px;
            }

            table tbody tr {
                transition: all 0.3s ease;
            }

            table tbody tr:hover {
                background: #f9fafb;
                transform: scale(1.01);
            }

            /* Modal */
            .modal {
                display: none;
                position: fixed;
                z-index: 1000;
                left: 0;
                top: 0;
                width: 100%;
                height: 100%;
                background: rgba(0, 0, 0, 0.5);
                justify-content: center;
                align-items: center;
            }

            .modal-content {
                background: #fff;
                padding: 35px;
                border-radius: 16px;
                width: 850px;
                max-height: 90vh;
                overflow: auto;
                box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
            }

            .modal-content h2 {
                margin-bottom: 25px;
                font-size: 24px;
                font-weight: 800;
                color: #0f2340;
                display: flex;
                align-items: center;
                gap: 12px;
            }

            .modal-content h2 i {
                color: #16a34a;
            }

            /* Form Grid */
            .form-grid {
                display: grid;
                grid-template-columns: 1fr 1fr;
                gap: 20px;
            }

            .form-group {
                display: flex;
                flex-direction: column;
            }

            .form-group.full-width {
                grid-column: span 2;
            }

            .form-group label {
                font-weight: 700;
                margin-bottom: 8px;
                color: #374151;
                font-size: 14px;
            }

            .form-group input,
            .form-group select {
                padding: 10px 14px;
                border: 2px solid #e5e7eb;
                border-radius: 10px;
                font-size: 14px;
                font-family: inherit;
                transition: all 0.3s ease;
            }

            .form-group input:focus,
            .form-group select:focus {
                outline: none;
                border-color: #16a34a;
                box-shadow: 0 0 0 4px rgba(22, 163, 74, 0.1);
            }

            .form-actions {
                margin-top: 25px;
                display: flex;
                gap: 12px;
                justify-content: flex-end;
                grid-column: span 2;
            }

            /* No data message */
            .no-data {
                text-align: center;
                padding: 60px;
                color: #9ca3af;
                background: #fff;
                border-radius: 16px;
                box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
            }

            .no-data i {
                font-size: 64px;
                margin-bottom: 20px;
                opacity: 0.3;
            }

            .no-data h4 {
                font-size: 18px;
                color: #6b7280;
                margin-bottom: 8px;
            }

            @media (max-width: 768px) {
                .app {
                    flex-direction: column;
                }

                .main {
                    padding: 20px;
                }

                .info-grid {
                    grid-template-columns: 1fr;
                }

                .form-grid {
                    grid-template-columns: 1fr;
                }

                .modal-content {
                    width: 95%;
                    padding: 20px;
                }

                .profile-card {
                    flex-direction: column;
                    text-align: center;
                }

                .btn-edit {
                    position: relative;
                    top: 0;
                    right: 0;
                    margin-top: 20px;
                }
            }
        </style>
    </head>
    <body>
        <div class="app">
            <!-- Include Sidebar -->
            <jsp:include page="/view/layout/sidebar.jsp"/>  

            <!-- Main Content -->
            <main class="main">
                <!-- Profile Card -->
                <div class="profile-card">
                    <% 
                        String userImage = user.getImage();
                        if (userImage == null || userImage.equals("null") || userImage.isEmpty()) {
                            userImage = "https://ui-avatars.com/api/?name=" + user.getFullName().replace(" ", "+") + "&size=120&background=0f2340&color=fff&bold=true";
                        }
                    %>
                    <div class="profile-image">
                        <img src="<%= userImage %>" alt="Profile" onerror="this.src='https://ui-avatars.com/api/?name=User&size=120&background=0f2340&color=fff&bold=true'">
                    </div>
                    <div class="profile-info">
                        <h2><%= user.getFullName() %></h2>
                        <div class="info-grid">
                            <div class="info-item">
                                <div class="label">
                                    <i class="fas fa-envelope"></i>Email
                                </div>
                                <div class="value"><%= user.getEmail() %></div>
                            </div>
                            <div class="info-item">
                                <div class="label">
                                    <i class="fas fa-phone"></i>Phone
                                </div>
                                <div class="value"><%= user.getPhone() != null ? user.getPhone() : "N/A" %></div>
                            </div>
                            <div class="info-item">
                                <div class="label">
                                    <i class="fas fa-venus-mars"></i>Gender
                                </div>
                                <div class="value"><%= user.getMale() ? "Male" : "Female" %></div>
                            </div>
                        </div>
                    </div>
                    <button class="btn btn-edit" onclick="document.getElementById('editProfileModal').style.display = 'flex'">
                        <i class="fas fa-edit"></i> Edit Profile
                    </button>
                </div>

                <!-- Cars Section -->
                <div class="section-header">
                    <h3><i class="fas fa-car"></i> Xe của tôi</h3>
                    <button class="btn btn-add" onclick="document.getElementById('addModal').style.display = 'flex'">
                        <i class="fas fa-plus"></i> Thêm Xe Mới
                    </button>
                </div>

                <!-- Search Form -->
                <form method="get" action="userProfileController" class="search-form">
                    <input type="text" name="search" class="search-input" value="<%= (search != null ? search : "") %>" placeholder="Tìm kiếm theo hãng, biển số...">
                    <button type="submit" class="btn-search">
                        <i class="fas fa-search"></i> Tìm kiếm
                    </button>
                </form>

                <!-- Cars Table -->
                <% if (cars != null && !cars.isEmpty()) { %>
                <div class="table-card">
                    <table>
                        <thead>
                            <tr>
                                <th><i class="fas fa-car"></i> Hãng</th>
                                <th><i class="fas fa-id-card"></i> Biển số</th>
                                <th><i class="fas fa-palette"></i> Màu</th>
                                <th><i class="fas fa-tag"></i> Model</th>
                                <th><i class="fas fa-calendar"></i> Năm SX</th>
                                <th><i class="fas fa-tachometer-alt"></i> Số km</th>
                                <th><i class="fas fa-cog"></i> Hành động</th>
                            </tr>
                        </thead>
                        <tbody>
                            <% for (Car c : cars) { %>
                            <tr>
                                <td><strong><%= c.getBrand() %></strong></td>
                                <td><%= c.getLicensePlate() %></td>
                                <td><%= c.getColor() %></td>
                                <td><%= c.getModel() != null ? c.getModel() : "N/A" %></td>
                                <td><%= c.getYear() != 0 ? c.getYear() : "N/A" %></td>
                                <td><%= c.getCurrentOdometer() != 0 ? String.format("%,d km", c.getCurrentOdometer()) : "N/A" %></td>
                                <td>
                                    <button class="btn btn-update" onclick="document.getElementById('modal-<%= c.getCarId() %>').style.display = 'flex'">
                                        <i class="fas fa-edit"></i> Sửa
                                    </button>
                                    <form action="userProfileController" method="post" style="display:inline;">
                                        <input type="hidden" name="action" value="delete">
                                        <input type="hidden" name="carId" value="<%= c.getCarId() %>">
                                        <button type="submit" class="btn btn-delete" onclick="return confirm('Bạn có chắc muốn xóa xe này?')">
                                            <i class="fas fa-trash"></i> Xóa
                                        </button>
                                    </form>
                                </td>
                            </tr>
                            <% } %>
                        </tbody>
                    </table>
                </div>
                <% } else { %>
                <div class="no-data">
                    <i class="fas fa-car"></i>
                    <h4>Chưa có xe nào</h4>
                    <p>Thêm xe đầu tiên của bạn để bắt đầu!</p>
                </div>
                <% } %>
            </main>
        </div>

        <!-- Edit Profile Modal -->
        <div id="editProfileModal" class="modal">
            <div class="modal-content">
                <h2><i class="fas fa-user-edit"></i> Chỉnh sửa thông tin</h2>
                <form action="userProfileController" method="post" class="form-grid">
                    <input type="hidden" name="action" value="updateProfile">

                    <div class="form-group">
                        <label>Họ và tên</label>
                        <input type="text" name="fullName" value="<%= user.getFullName() %>" required>
                    </div>

                    <div class="form-group">
                        <label>Email</label>
                        <input type="email" name="email" value="<%= user.getEmail() %>" required>
                    </div>

                    <div class="form-group">
                        <label>Số điện thoại</label>
                        <input type="text" name="phone" value="<%= user.getPhone() != null ? user.getPhone() : "" %>">
                    </div>

                    <div class="form-group">
                        <label>Giới tính</label>
                        <select name="male">
                            <option value="true" <%= user.getMale() ? "selected" : "" %>>Nam</option>
                            <option value="false" <%= !user.getMale() ? "selected" : "" %>>Nữ</option>
                        </select>
                    </div>

                    <div class="form-group">
                        <label>Ngày sinh</label>
                        <input type="date" name="dateOfBirth" value="<%= user.getDateOfBirth() != null ? user.getDateOfBirth() : "" %>">
                    </div>

                    <div class="form-actions">
                        <button type="submit" class="btn btn-update">
                            <i class="fas fa-save"></i> Lưu thay đổi
                        </button>
                        <button type="button" class="btn btn-delete" onclick="document.getElementById('editProfileModal').style.display = 'none'">
                            <i class="fas fa-times"></i> Hủy
                        </button>
                    </div>
                </form>
            </div>
        </div>

        <!-- Add Car Modal -->
        <div id="addModal" class="modal">
            <div class="modal-content">
                <h2><i class="fas fa-plus-circle"></i> Thêm xe mới</h2>
                <form method="post" action="userProfileController" class="form-grid">
                    <input type="hidden" name="action" value="add">

                    <div class="form-group">
                        <label>Biển số xe *</label>
                        <input name="licensePlate" required placeholder="VD: 29A-12345">
                    </div>

                    <div class="form-group">
                        <label>Hãng xe *</label>
                        <input name="brand" required placeholder="VD: Toyota">
                    </div>

                    <div class="form-group">
                        <label>Model</label>
                        <input name="model" placeholder="VD: Camry">
                    </div>

                    <div class="form-group">
                        <label>Năm sản xuất</label>
                        <input type="number" name="year" placeholder="VD: 2023">
                    </div>

                    <div class="form-group">
                        <label>Màu sắc</label>
                        <input name="color" placeholder="VD: Trắng">
                    </div>

                    <div class="form-group">
                        <label>Số máy</label>
                        <input name="engineNumber" placeholder="Số máy">
                    </div>

                    <div class="form-group">
                        <label>Số khung</label>
                        <input name="chassisNumber" placeholder="Số khung">
                    </div>

                    <div class="form-group">
                        <label>Ngày mua</label>
                        <input type="date" name="purchaseDate">
                    </div>

                    <div class="form-group">
                        <label>Số km hiện tại</label>
                        <input type="number" name="currentOdometer" placeholder="VD: 50000">
                    </div>

                    <div class="form-actions">
                        <button type="submit" class="btn btn-add">
                            <i class="fas fa-check"></i> Thêm xe
                        </button>
                        <button type="button" class="btn btn-delete" onclick="document.getElementById('addModal').style.display = 'none'">
                            <i class="fas fa-times"></i> Hủy
                        </button>
                    </div>
                </form>
            </div>
        </div>

        <!-- Edit Car Modals -->
        <% if (cars != null) {
            for (Car c : cars) { %>
        <div id="modal-<%= c.getCarId() %>" class="modal">
            <div class="modal-content">
                <h2><i class="fas fa-edit"></i> Cập nhật thông tin xe</h2>
                <form method="post" action="userProfileController" class="form-grid">
                    <input type="hidden" name="action" value="update">
                    <input type="hidden" name="carId" value="<%= c.getCarId() %>">

                    <div class="form-group">
                        <label>Biển số xe *</label>
                        <input name="licensePlate" value="<%= c.getLicensePlate() %>" required>
                    </div>

                    <div class="form-group">
                        <label>Hãng xe *</label>
                        <input name="brand" value="<%= c.getBrand() %>" required>
                    </div>

                    <div class="form-group">
                        <label>Model</label>
                        <input name="model" value="<%= c.getModel() != null ? c.getModel() : "" %>">
                    </div>

                    <div class="form-group">
                        <label>Năm sản xuất</label>
                        <input type="number" name="year" value="<%= c.getYear() != 0 ? c.getYear() : "" %>">
                    </div>

                    <div class="form-group">
                        <label>Màu sắc</label>
                        <input name="color" value="<%= c.getColor() %>">
                    </div>

                    <div class="form-group">
                        <label>Số máy</label>
                        <input name="engineNumber" value="<%= c.getEngineNumber() != null ? c.getEngineNumber() : "" %>">
                    </div>

                    <div class="form-group">
                        <label>Số khung</label>
                        <input name="chassisNumber" value="<%= c.getChassisNumber() != null ? c.getChassisNumber() : "" %>">
                    </div>

                    <div class="form-group">
                        <label>Ngày mua</label>
                        <input type="date" name="purchaseDate" value="<%= c.getPurchaseDate() != null ? c.getPurchaseDate() : "" %>">
                    </div>

                    <div class="form-group">
                        <label>Số km hiện tại</label>
                        <input type="number" name="currentOdometer" value="<%= c.getCurrentOdometer() != null ? c.getCurrentOdometer() : "" %>">
                    </div>

                    <div class="form-actions">
                        <button type="submit" class="btn btn-update">
                            <i class="fas fa-save"></i> Lưu thay đổi
                        </button>
                        <button type="button" class="btn btn-delete" onclick="document.getElementById('modal-<%= c.getCarId() %>').style.display = 'none'">
                            <i class="fas fa-times"></i> Hủy
                        </button>
                    </div>
                </form>
            </div>
        </div>
        <% } } %>

        <script>
            // Close modal when clicking outside
            window.onclick = function (event) {
                if (event.target.classList.contains('modal')) {
                    event.target.style.display = 'none';
                }
            }
        </script>
    </body>
</html>