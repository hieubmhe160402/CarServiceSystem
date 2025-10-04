<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.*, model.ServiceHistory"%>
<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8" />
  <title>Lịch sử dịch vụ - Car Care System</title>
  <style>
    * { box-sizing: border-box; margin: 0; padding: 0; }
    html,body { height: 100%; font-family: Inter, "Segoe UI", Roboto, Arial, sans-serif; background:#f5f7fb; color:#111827; }

    .app { display: flex; height: 100vh; }
    .sidebar { width: 260px; background: linear-gradient(180deg,#0f2340,#0b1830); color:#fff; padding:28px 18px; display:flex; flex-direction:column; }
    .brand { font-weight:800; font-size:18px; letter-spacing:1px; margin-bottom:22px; }
    .nav { margin-top:12px; display:flex; flex-direction:column; gap:8px; }
    .nav a { color:rgba(255,255,255,0.9); text-decoration:none; padding:10px 12px; border-radius:10px; display:flex; align-items:center; gap:12px; }
    .nav a.active, .nav a:hover { background: rgba(255,255,255,0.04); }
    .nav a .ico { width:12px; height:12px; background:#fff; border-radius:2px; opacity:0.9; }

    .main { flex:1; padding:24px 32px; overflow:auto; }
    .topbar { display:flex; align-items:center; justify-content:space-between; gap:12px; margin-bottom:18px; }
    .search { flex:1; max-width:520px; }
    .search input { width:100%; padding:12px 16px; border-radius:12px; border:1px solid #e6eef8; background:#fff; }
    .top-actions { display:flex; align-items:center; gap:18px; }
    .avatar { display:flex; align-items:center; gap:8px; }
    .avatar .circle{ width:36px; height:36px; border-radius:50%; background:#e6eef8; display:inline-block; }

    .card { background:#fff; border-radius:12px; padding:18px; box-shadow: 0 4px 14px rgba(16,24,40,0.04); margin-bottom:18px; }

    table { width:100%; border-collapse:collapse; background:#fff; border-radius:8px; overflow:hidden; }
    table thead th { text-align:center; padding:12px 16px; background:#f8fafc; color:#374151; border-bottom:1px solid #eef2f7; }
    table tbody td { padding:12px 16px; border-bottom:1px solid #f1f5f9; color:#374151; text-align:center; }

    @media (max-width: 1000px){
      .sidebar{ display:none; }
    }
  </style>
</head>
<body>
  <div class="app">
    <!-- Sidebar -->
    <aside class="sidebar">
      <div class="brand">CAR CARE SYSTEM</div>
      <nav class="nav">
        <a href="AdminDashboard.jsp"><span class="ico"></span> Dashboard</a>
        <a href="ManageEmployees.jsp"><span class="ico"></span> Quản lý nhân viên</a>
        <a class="active" href="#"><span class="ico"></span> Lịch sử dịch vụ</a>
        <a href="#"><span class="ico"></span> Quản lý dịch vụ</a>
        <a href="#"><span class="ico"></span> Quản lý phụ tùng</a>
        <a href="#"><span class="ico"></span> Quản lý Service Centre</a>
        <a href="#"><span class="ico"></span> Báo cáo</a>
      </nav>
      <div style="flex:1"></div>
      <div style="font-size:12px; color:rgba(255,255,255,0.6);">© 2025 CarCare</div>
    </aside>

    <!-- Main -->
    <main class="main">
      <div class="topbar">
        <div class="search"><input placeholder="Tìm kiếm..." /></div>
        <div class="top-actions">
          <div>🔔</div>
          <div class="avatar"><div class="circle"></div><div>Admin</div></div>
        </div>
      </div>

      <div class="card">
        <h2 style="margin-bottom:16px;">Danh sách dịch vụ khách hàng đã sử dụng</h2>
        <table>
          <thead>
            <tr>
              <th>Tên khách hàng</th>
              <th>Biển số xe</th>
              <th>Mã bảo dưỡng</th>
              <th>Ngày bảo dưỡng</th>
              <th>Tên dịch vụ</th>
              <th>Số lượng</th>
              <th>Đơn giá</th>
              <th>Thành tiền</th>
            </tr>
          </thead>
          <tbody>
            <%
              List<ServiceHistory> histories = (List<ServiceHistory>) request.getAttribute("histories");
              if (histories == null || histories.isEmpty()) {
            %>
              <tr><td colspan="8" style="color:red; text-align:center;">⚠️ Không có dữ liệu dịch vụ</td></tr>
            <%
              } else {
                  for (ServiceHistory sh : histories) {
            %>
              <tr>
                <td><%= sh.getCustomerName() %></td>
                <td><%= sh.getLicensePlate() %></td>
                <td><%= sh.getMaintenanceId() %></td>
                <td><%= sh.getMaintenanceDate() %></td>
                <td><%= sh.getServiceName() %></td>
                <td><%= sh.getQuantity() %></td>
                <td><%= sh.getUnitPrice() %></td>
                <td><%= sh.getTotalPrice() %></td>
              </tr>
            <%
                  }
              }
            %>
          </tbody>
        </table>
      </div>
    </main>
  </div>
</body>
</html>
