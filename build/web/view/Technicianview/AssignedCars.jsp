<%-- 
    Document   : AssignedCars
    Created on : Oct 6, 2025, 2:47:22 PM
    Author     : MinHeee
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <title>Technication Dashboard - Car Care System</title>
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
                <!-- Top bar -->
                <div class="topbar">
                    <div class="search">
                        <input placeholder="Tìm kiếm..." />
                    </div>
                    <div class="top-actions">
                        <div>🔔</div>
                        <div class="avatar">
                            <div class="circle"></div>
                            <div>Technication</div>
                        </div>
                    </div>
                </div>

                <!-- Page title -->
                <h2 style="margin-bottom: 16px;">Danh sách công việc bảo dưỡng</h2>

                <!-- Table -->
                <div class="card" style="padding: 20px; background: #fff; border-radius: 12px;">
                    <table style="width: 100%; border-collapse: collapse;">
                        <thead style="background: #f4f6f8;">
                            <tr>
                                <th style="padding: 10px; border-bottom: 2px solid #ddd;">Mã công việc</th>
                                <th style="padding: 10px; border-bottom: 2px solid #ddd;">Tên xe</th>
                                <th style="padding: 10px; border-bottom: 2px solid #ddd;">Loại dịch vụ</th>
                                <th style="padding: 10px; border-bottom: 2px solid #ddd;">Kỹ thuật viên</th>
                                <th style="padding: 10px; border-bottom: 2px solid #ddd;">Trạng thái</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td style="padding: 10px; border-bottom: 1px solid #eee;">#CV001</td>
                                <td style="padding: 10px; border-bottom: 1px solid #eee;">Toyota Vios</td>
                                <td style="padding: 10px; border-bottom: 1px solid #eee;">Thay dầu</td>
                                <td style="padding: 10px; border-bottom: 1px solid #eee;">Nguyễn Văn A</td>
                                <td style="padding: 10px; border-bottom: 1px solid #eee;">Đang thực hiện</td>
                            </tr>
                            <tr>
                                <td style="padding: 10px; border-bottom: 1px solid #eee;">#CV002</td>
                                <td style="padding: 10px; border-bottom: 1px solid #eee;">Honda CRV</td>
                                <td style="padding: 10px; border-bottom: 1px solid #eee;">Bảo dưỡng định kỳ</td>
                                <td style="padding: 10px; border-bottom: 1px solid #eee;">Trần Văn B</td>
                                <td style="padding: 10px; border-bottom: 1px solid #eee;">Hoàn thành</td>
                            </tr>
                            <tr>
                                <td style="padding: 10px; border-bottom: 1px solid #eee;">#CV003</td>
                                <td style="padding: 10px; border-bottom: 1px solid #eee;">Mazda CX-5</td>
                                <td style="padding: 10px; border-bottom: 1px solid #eee;">Rửa xe</td>
                                <td style="padding: 10px; border-bottom: 1px solid #eee;">Lê Văn C</td>
                                <td style="padding: 10px; border-bottom: 1px solid #eee;">Chờ duyệt</td>
                            </tr>
                            <tr>
                                <td style="padding: 10px; border-bottom: 1px solid #eee;">#CV004</td>
                                <td style="padding: 10px; border-bottom: 1px solid #eee;">Hyundai Accent</td>
                                <td style="padding: 10px; border-bottom: 1px solid #eee;">Sửa chữa động cơ</td>
                                <td style="padding: 10px; border-bottom: 1px solid #eee;">Đỗ Văn D</td>
                                <td style="padding: 10px; border-bottom: 1px solid #eee;">Đang chờ linh kiện</td>
                            </tr>
                            <tr>
                                <td style="padding: 10px;">#CV005</td>
                                <td style="padding: 10px;">Kia Morning</td>
                                <td style="padding: 10px;">Thay lốp</td>
                                <td style="padding: 10px;">Phạm Văn E</td>
                                <td style="padding: 10px;">Đã giao xe</td>
                            </tr>
                        </tbody>
                    </table>
                </div>

            </main>
        </div>
    </body>

</html>