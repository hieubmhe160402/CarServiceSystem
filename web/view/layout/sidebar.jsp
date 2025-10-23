<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="model.User"%>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8" />
        <title>Sidebar</title>
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
            }

            .sidebar .brand {
                font-weight:800;
                font-size:18px;
                letter-spacing:1px;
                margin-bottom:22px;
            }
            .sidebar .nav {
                margin-top:12px;
            }
            .sidebar .nav a {
                color:rgba(255,255,255,0.9);
                text-decoration:none;
                padding:10px 12px;
                border-radius:10px;
                display:flex;
                align-items:center;
                gap:12px;
                transition: background 0.2s;
            }
            .sidebar .nav a.active, .sidebar .nav a:hover {
                background: rgba(255,255,255,0.08);
            }
            .sidebar .nav a .ico {
                width:12px;
                height:12px;
                background:#fff;
                border-radius:2px;
                opacity:0.9;
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
            }
            .user-panel .name {
                font-size: 14px;
                font-weight: 600;
                color: #fff;
            }
            .logout-btn {
                font-size: 12px;
                color: #ff6b6b;
                text-decoration: none;
                margin-top: 2px;
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
                <a href="view/Admin/HomePageForAdmin.jsp"><span class="ico"></span> Dashboard</a>
                <a href="/CarCareSystem/listEmployees"><span class="ico"></span> Quản lý nhân viên</a>
                <a href="/CarCareSystem/ManageCustomerController"><span class="ico"></span> Quản lý thông tin khách hàng</a>
                <a href="ManageServices.jsp"><span class="ico"></span> Quản lý nhà cung cấp</a>
                <a href="/CarCareSystem/products"><span class="ico"></span> Quản lý phụ tùng</a>
                <a href="/CarCareSystem/units"><span class="ico"></span> Quản lý đơn vị</a>
                <a href="/CarCareSystem/category"><span class="ico"></span> Quản lý danh mục</a>
                <a href="/CarCareSystem/roleManage"><span class="ico"></span> Quản lý Vai Trò(Role)</a>
                <a href="/CarCareSystem/managerPackage"><span class="ico"></span> Dách chi tiết trong gói combo </a>
                <a href="/CarCareSystem/maintenancePackage"><span class="ico"></span> Danh sách gói combo </a>
                <a href="SystemLog.jsp"><span class="ico"></span> Log hệ thống</a>
                <a href="Reports.jsp"><span class="ico"></span> Báo cáo</a>

                <% } else if ("ServiceTechnician".equals(roleName)) { %>
                <div class="brand">ServiceTechnician CARE SYSTEM</div>
                <a href="/CarCareSystem"><span class="ico"></span> Danh sách xe được giao</a>
                <a href="/CarCareSystem"><span class="ico"></span> Kiểm tra xe</a>
                <a href="/CarCareSystem"><span class="ico"></span> Báo giá</a>
                <a href="/CarCareSystem"><span class="ico"></span> Sửa chữa</a>
                <a href="/CarCareSystem"><span class="ico"></span> Quản lý kho phụ tùng</a>

                <% } else if ("CarOwner".equals(roleName)) { %>
                <div class="brand">CarOwner CARE SYSTEM</div>

                <a href="/CarCareSystem"><span class="ico"></span> Trang chủ</a>
                <a href="/CarCareSystem"><span class="ico"></span> Lịch sử bảo dưỡng</a>
                <a href="/CarCareSystem"><span class="ico"></span> Đặt lịch sửa chữa</a>
                <a href="/CarCareSystem"><span class="ico"></span> Theo dõi tiến độ xe</a>
                <a href="/CarCareSystem"><span class="ico"></span> Hồ sơ cá nhân</a>
                <% } else if ("Accountant".equals(roleName)) { %>
                <div class="brand">Accountant CARE SYSTEM</div>

                <a href="/CarCareSystem"><span class="ico"></span> Danh sách hóa đơn</a>
                <a href="/CarCareSystem"><span class="ico"></span> Theo dõi thanh toán</a>
                <a href="/CarCareSystem"><span class="ico"></span> Báo cáo tài chính</a>
                <a href="/CarCareSystem"><span class="ico"></span> Đối soát giao dịch</a>
                <% } else if ("Staff".equals(roleName)) { %>
                <div class="brand">Staff CARE SYSTEM</div>

                <a href="/CarCareSystem/listCarmaintenance"><span class="ico"></span> Quản lý bảo dưỡng </a>
                <a href="/CarCareSystem/listAppointmentServlet"><span class="ico"></span> Quản lý yêu cầu</a>
                <a href="/CarCareSystem"><span class="ico"></span> Tạo lịch hẹn</a>
                <a href="/CarCareSystem"><span class="ico"></span> Trạng thái xe</a>
                <a href="/CarCareSystem"><span class="ico"></span> Gửi hóa đơn</a>
                <a href="authController?action=changePassword" class="ico">Đổi mật khẩu</a>
                <% } else { %>
                <a href="#"><span class="ico"></span> Trang chủ</a>
                <% } %>
            </nav>

            <div class="user-panel">
                <div class="avatar"></div>
                <div class="user-info">
                    <div class="name"><%= user != null ? user.getFullName() : "Guest" %></div>
                    <a href="/CarCareSystem/authController?action=logout" class="logout-btn">Đăng xuất</a>
                </div>
            </div>

            <div class="footer">© 2025 CarCare</div>
        </aside>
    </body>
</html>
