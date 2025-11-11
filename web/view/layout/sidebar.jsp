<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="model.User"%>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8" />
        <title>Sidebar</title>
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
        <style>
            * {
                box-sizing: border-box;
                margin: 0;
                padding: 0;
            }
            body {
                font-family: Inter, "Segoe UI", Roboto, Arial, sans-serif;
                background:#f5f7fb;
                color:#111827;
            }

            .sidebar {
                width: 260px;
                height: 100vh;
                background: linear-gradient(180deg,#0f2340,#0b1830);
                color:#fff;
                padding:28px 18px;
                display:flex;
                flex-direction:column;
                box-shadow: 4px 0 12px rgba(0, 0, 0, 0.1);
            }

            .sidebar .brand {
                font-weight:800;
                font-size:18px;
                letter-spacing:1px;
                margin-bottom:22px;
                text-align: center;
            }
            .sidebar .nav {
                margin-top:12px;
                display: flex;
                flex-direction: column;
                gap: 8px;
            }
            .sidebar .nav a {
                color:rgba(255,255,255,0.9);
                text-decoration:none;
                padding:10px 12px;
                border-radius:10px;
                display:flex;
                align-items:center;
                gap:12px;
                transition: all 0.3s ease;
            }
            .sidebar .nav a.active, .sidebar .nav a:hover {
                background: rgba(255,255,255,0.15);
                transform: translateX(4px);
            }
            .sidebar .nav a i {
                width: 20px;
                text-align: center;
                font-size: 16px;
            }

            .user-panel {
                margin-top: auto;
                padding: 12px;
                border-top: 1px solid rgba(255,255,255,0.1);
                border-bottom: 1px solid rgba(255,255,255,0.1);
                display: flex;
                align-items: center;
                gap: 12px;
            }
            .user-panel .avatar {
                width: 40px;
                height: 40px;
                border-radius: 50%;
                background: #e6eef8 url('https://cdn-icons-png.flaticon.com/512/149/149071.png') no-repeat center/cover;
                border: 2px solid rgba(255,255,255,0.2);
            }
            .user-panel .name {
                font-size: 14px;
                font-weight: 600;
                color: #fff;
            }
            .user-panel .role {
                font-size: 11px;
                color: rgba(255,255,255,0.6);
                margin-top: 2px;
            }
            .logout-btn {
                font-size: 12px;
                color: #ff6b6b;
                text-decoration: none;
                margin-top: 4px;
                display: inline-block;
            }
            .logout-btn:hover {
                text-decoration: underline;
            }
            .footer {
                margin-top: 10px;
                font-size:12px;
                color:rgba(255,255,255,0.6);
                text-align:center;
            }
        </style>
    </head>
    <body>
        <%
            User user = (User) session.getAttribute("user");
            String roleName = user != null && user.getRole() != null ? user.getRole().getRoleName() : "";
        %>

        <aside class="sidebar">
            <nav class="nav">
                <% if ("Admin".equals(roleName)) { %>
                <div class="brand">Admin Car Care</div>
                <a href="/CarCareSystem/dashboard"><span class="ico"></span> Dashboard</a>
                <a href="/CarCareSystem/listEmployees"><span class="ico"></span> Quản lý nhân viên</a>
                <a href="/CarCareSystem/supplier"><span class="ico"></span> Quản lý nhà cung cấp</a>
                <a href="/CarCareSystem/supplierproduct"><span class="ico"></span> Quản lý nhà supplier-product</a>
                <a href="/CarCareSystem/products"><span class="ico"></span> Quản lý phụ tùng</a>
                <a href="/CarCareSystem/units"><span class="ico"></span> Quản lý đơn vị</a>
                <a href="/CarCareSystem/category"><span class="ico"></span> Quản lý danh mục</a>
                <a href="/CarCareSystem/roleManage"><span class="ico"></span> Quản lý Vai Trò(Role)</a>
                <a href="/CarCareSystem/managerPackage"><span class="ico"></span> Dách chi tiết trong gói combo </a>
                <a href="/CarCareSystem/maintenancePackage"><span class="ico"></span> Danh sách gói combo </a>
                <a href="/CarCareSystem/payments"><span class="ico"></span> Lịch sử sửa xe</a>
                <a href="authController?action=changePassword" class="ico">Đổi mật khẩu</a>

                <% } else if ("ServiceTechnician".equals(roleName)) { %>
                <div class="brand">🔧 KỸ THUẬT VIÊN</div>
                <a href="/CarCareSystem"><i class="fas fa-car"></i> Danh sách xe được giao</a>
                <a href="authController?action=changePassword" class="ico">Đổi mật khẩu</a>


                <% } else if ("CarOwner".equals(roleName)) { %>
                <div class="brand">🚗 CAR MANAGEMENT</div>
                <a href="userProfileController"><i class="fas fa-user"></i> Hồ sơ cá nhân</a>
                <a href="bookingAppoitments"><i class="fas fa-calendar-check"></i> Đặt lịch bảo dưỡng</a>
                <a href="userAppoinmentsHistoryController"><i class="fas fa-history"></i> Lịch sử lịch hẹn</a>
                <a href="authController?action=changePassword" class="ico">Đổi mật khẩu</a>


                <% } else if ("Accountant".equals(roleName)) { %>
                <div class="brand">💰 KẾ TOÁN</div>
                <a href="/CarCareSystem"><i class="fas fa-file-invoice"></i> Danh sách hóa đơn</a>
                <a href="/CarCareSystem"><i class="fas fa-credit-card"></i> Theo dõi thanh toán</a>
                <a href="/CarCareSystem"><i class="fas fa-chart-pie"></i> Báo cáo tài chính</a>
                <a href="/CarCareSystem"><i class="fas fa-calculator"></i> Đối soát giao dịch</a>

                <% } else if ("Staff".equals(roleName)) { %>
                <div class="brand">Staff CARE SYSTEM</div>

                <a href="/CarCareSystem/listCarmaintenance"><span class="ico"></span> Quản lý bảo dưỡng </a>
                <a href="/CarCareSystem/listAppointmentServlet"><span class="ico"></span> Quản lý yêu cầu</a>
                <a href="authController?action=changePassword" class="ico">Đổi mật khẩu</a>
                <% } else { %>
                <div class="brand">🚗 CAR CARE</div>
                <a href="#"><i class="fas fa-home"></i> Trang chủ</a>
                <% } %>
            </nav>

            <div class="user-panel">
                <div class="avatar"></div>
                <div class="user-info">
                    <div class="name"><%= user != null ? user.getFullName() : "Guest" %></div>
                    <div class="role"><%= roleName %></div>
                    <a href="/CarCareSystem/authController?action=logout" class="logout-btn">
                        <i class="fas fa-sign-out-alt"></i> Đăng xuất
                    </a>
                </div>
            </div>

            <div class="footer">© 2025 CarCare System</div>
        </aside>
    </body>
</html>