<%--
    Document   : HomePageForAdmin
    Created on : Sep 22, 2025, 11:50:39 AM
    Author     : MinHeee

    This is a standalone HTML/CSS/JS mockup for the Admin Dashboard.
    To use in your JSP page, you can replace the <body> content of your JSP
    with the <body> content below (and move the <style> and <script> blocks
    into your JSP layout or keep them inline).//key
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <title>Admin Dashboard - Car Care System</title>
        <style>
            /* Reset & base */
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

            /* Layout */
            .app {
                display: flex;
                height: 100vh;
            }

            /* Sidebar */
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
            .nav a .ico {
                width:12px;
                height:12px;
                background:#fff;
                border-radius:2px;
                opacity:0.9;
            }

            /* Main */
            .main {
                flex:1;
                padding:24px 32px;
                overflow:auto;
            }
            .topbar {
                display:flex;
                align-items:center;
                justify-content:space-between;
                gap:12px;
                margin-bottom:18px;
            }
            .search {
                flex:1;
                max-width:520px;
            }
            .search input {
                width:100%;
                padding:12px 16px;
                border-radius:12px;
                border:1px solid #e6eef8;
                background:#fff;
            }
            .top-actions {
                display:flex;
                align-items:center;
                gap:18px;
            }
            .avatar {
                display:flex;
                align-items:center;
                gap:8px;
            }
            .avatar .circle{
                width:36px;
                height:36px;
                border-radius:50%;
                background:#e6eef8;
                display:inline-block;
            }

            /* Dashboard cards */
            .grid {
                display:grid;
                grid-template-columns: repeat(4, 1fr);
                gap:16px;
                margin-bottom:18px;
            }
            .card {
                background:#fff;
                border-radius:12px;
                padding:18px;
                box-shadow: 0 4px 14px rgba(16,24,40,0.04);
            }
            .card .value {
                font-size:24px;
                font-weight:700;
            }
            .card .label {
                color:#6b7280;
                margin-top:8px;
            }

            /* Charts + tables area */
            .panels {
                display:grid;
                grid-template-columns: 2fr 1fr;
                gap:16px;
                margin-bottom:18px;
            }
            .chart {
                height:260px;
                padding:18px;
            }
            .chart .chart-placeholder{
                width:100%;
                height:200px;
                background: linear-gradient(90deg, rgba(59,130,246,0.12), rgba(59,130,246,0.05));
                border-radius:8px;
                display:flex;
                align-items:center;
                justify-content:center;
                color:#2563eb;
                font-weight:600;
            }

            .tables {
                display:grid;
                grid-template-columns: 1fr 1fr;
                gap:16px;
            }
            table {
                width:100%;
                border-collapse:collapse;
                background:#fff;
                border-radius:8px;
                overflow:hidden;
            }
            table thead th {
                text-align:left;
                padding:12px 16px;
                background:#f8fafc;
                color:#374151;
                border-bottom:1px solid #eef2f7;
            }
            table tbody td {
                padding:12px 16px;
                border-bottom:1px solid #f1f5f9;
                color:#374151;
            }

            /* Responsive */
            @media (max-width: 1000px){
                .grid{
                    grid-template-columns: repeat(2,1fr);
                }
                .panels{
                    grid-template-columns: 1fr;
                }
                .tables{
                    grid-template-columns: 1fr;
                }
                .sidebar{
                    display:none;
                }
            }
        </style>
    </head>
    <body>
        <div class="app">
            <jsp:include page="/view/layout/sidebar.jsp"/>

            <main class="main">
                <div class="topbar">
                    <div class="search"><input placeholder="Tìm kiếm..." /></div>
                    <div class="top-actions">
                        <div>🔔</div>
                        <div class="avatar"><div class="circle"></div><div>Admin</div></div>
                    </div>
                </div>

                <h2 style="margin-bottom:12px;">Dashboard</h2>

                <div class="grid">
                    <div class="card"><div class="value">462</div><div class="label">Khách hàng</div></div>
                    <div class="card"><div class="value">85</div><div class="label">Xe đang bảo dưỡng</div></div>
                    <div class="card"><div class="value">12.5M</div><div class="label">Doanh thu hôm nay</div></div>
                    <div class="card"><div class="value">378.9M</div><div class="label">Doanh thu tháng này</div></div>
                </div>

                <div class="panels">
                    <div class="card chart">
                        <h3 style="margin-bottom:12px;">Doanh thu theo tháng</h3>
                        <div class="chart-placeholder">Biểu đồ cột (thay bằng Chart.js khi tích hợp)</div>
                    </div>

                    <div class="card chart">
                        <h3 style="margin-bottom:12px;">Dịch vụ phổ biến</h3>
                        <div class="chart-placeholder">Biểu đồ tròn (thay bằng Chart.js khi tích hợp)</div>
                    </div>
                </div>

                <div class="tables">
                    <div>
                        <table>
                            <thead>
                                <tr><th colspan="3">Khách hàng mới nhất</th></tr>
                                <tr><th>#</th><th>Tên</th><th>SDT</th></tr>
                            </thead>
                            <tbody>
                                <tr><td>1</td><td>Nguyễn Văn A</td><td>0912345678</td></tr>
                                <tr><td>2</td><td>Trần Thị B</td><td>0912345579</td></tr>
                                <tr><td>3</td><td>Lê Văn C</td><td>0912345670</td></tr>
                            </tbody>
                        </table>
                    </div>

                    <div>
                        <table>
                            <thead>
                                <tr><th colspan="3">Yêu cầu dịch vụ gần đây</th></tr>
                                <tr><th>#</th><th>Người dùng</th><th>Dịch vụ</th></tr>
                            </thead>
                            <tbody>
                                <tr><td>1</td><td>Nguyễn Văn con</td><td>Thay dầu</td></tr>
                                <tr><td>2</td><td>Trần Thị D</td><td>Sửa chữa</td></tr>
                                <tr><td>3</td><td>Lê Văn F</td><td>Thay phụ tùng</td></tr>
                            </tbody>
                        </table>
                    </div>
                </div>

            </main>
        </div>

        <script>
            // Placeholder JS: khi tích hợp vào dự án, thay chart-placeholder bằng Chart.js hoặc Recharts.
            // Nếu bạn muốn, mình có thể cung cấp version kèm Chart.js CDN để hiển thị mock charts.
        </script>
    </body>
</html>