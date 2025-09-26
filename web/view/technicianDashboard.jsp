<%-- 
    Document   : Technician Dashboard
    Created on : Sep 19, 2025, 11:38:59 PM
    Author     : MinHeee
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Technician Dashboard - Car Service System</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
            min-height: 100vh;
        }
        
        .header {
            background: rgba(255, 255, 255, 0.95);
            padding: 1rem 2rem;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
            display: flex;
            justify-content: space-between;
            align-items: center;
        }
        
        .logo {
            font-size: 1.5rem;
            font-weight: bold;
            color: #4facfe;
        }
        
        .user-info {
            display: flex;
            align-items: center;
            gap: 1rem;
        }
        
        .user-name {
            font-weight: 600;
            color: #333;
        }
        
        .logout-btn {
            background: #dc3545;
            color: white;
            padding: 0.5rem 1rem;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            text-decoration: none;
            transition: background-color 0.3s;
        }
        
        .logout-btn:hover {
            background: #c82333;
        }
        
        .container {
            max-width: 1200px;
            margin: 2rem auto;
            padding: 0 2rem;
        }
        
        .welcome-card {
            background: white;
            border-radius: 15px;
            padding: 2rem;
            margin-bottom: 2rem;
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
            text-align: center;
        }
        
        .welcome-title {
            font-size: 2rem;
            color: #333;
            margin-bottom: 1rem;
        }
        
        .welcome-subtitle {
            color: #666;
            font-size: 1.1rem;
        }
        
        .dashboard-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
            gap: 2rem;
        }
        
        .dashboard-card {
            background: white;
            border-radius: 15px;
            padding: 2rem;
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
            transition: transform 0.3s ease;
        }
        
        .dashboard-card:hover {
            transform: translateY(-5px);
        }
        
        .card-icon {
            font-size: 3rem;
            margin-bottom: 1rem;
            text-align: center;
        }
        
        .card-title {
            font-size: 1.5rem;
            font-weight: bold;
            color: #333;
            margin-bottom: 1rem;
            text-align: center;
        }
        
        .card-description {
            color: #666;
            text-align: center;
            margin-bottom: 1.5rem;
        }
        
        .card-btn {
            width: 100%;
            padding: 1rem;
            background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
            color: white;
            border: none;
            border-radius: 10px;
            font-size: 1rem;
            font-weight: 600;
            cursor: pointer;
            transition: transform 0.2s ease;
            text-decoration: none;
            display: block;
            text-align: center;
        }
        
        .card-btn:hover {
            transform: translateY(-2px);
        }
        
        .stats-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
            gap: 1rem;
            margin-bottom: 2rem;
        }
        
        .stat-card {
            background: white;
            border-radius: 10px;
            padding: 1.5rem;
            text-align: center;
            box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
        }
        
        .stat-number {
            font-size: 2rem;
            font-weight: bold;
            color: #4facfe;
            margin-bottom: 0.5rem;
        }
        
        .stat-label {
            color: #666;
            font-size: 0.9rem;
        }
        
        .technician-badge {
            background: linear-gradient(135deg, #4facfe, #00f2fe);
            color: white;
            padding: 0.3rem 0.8rem;
            border-radius: 20px;
            font-size: 0.8rem;
            font-weight: bold;
        }
        
        .task-list {
            background: white;
            border-radius: 15px;
            padding: 2rem;
            margin-bottom: 2rem;
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
        }
        
        .task-item {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 1rem;
            border-bottom: 1px solid #eee;
            transition: background-color 0.3s;
        }
        
        .task-item:hover {
            background-color: #f8f9fa;
        }
        
        .task-item:last-child {
            border-bottom: none;
        }
        
        .task-info h4 {
            color: #333;
            margin-bottom: 0.5rem;
        }
        
        .task-info p {
            color: #666;
            font-size: 0.9rem;
        }
        
        .task-status {
            padding: 0.3rem 0.8rem;
            border-radius: 15px;
            font-size: 0.8rem;
            font-weight: bold;
        }
        
        .status-pending {
            background: #fff3cd;
            color: #856404;
        }
        
        .status-in-progress {
            background: #d1ecf1;
            color: #0c5460;
        }
        
        .status-completed {
            background: #d4edda;
            color: #155724;
        }
    </style>
</head>
<body>
    <div class="header">
        <div class="logo">🔧 Car Service System</div>
        <div class="user-info">
            <span class="user-name">${currentUser.fullName}</span>
            <span class="technician-badge">TECHNICIAN</span>
            <a href="AuthController?action=logout" class="logout-btn">Đăng xuất</a>
        </div>
    </div>
    
    <div class="container">
        <div class="welcome-card">
            <h1 class="welcome-title">Chào mừng Kỹ thuật viên!</h1>
            <p class="welcome-subtitle">Quản lý công việc bảo dưỡng và sửa chữa xe</p>
        </div>
        
        <div class="stats-grid">
            <div class="stat-card">
                <div class="stat-number">12</div>
                <div class="stat-label">Công việc hôm nay</div>
            </div>
            <div class="stat-card">
                <div class="stat-number">8</div>
                <div class="stat-label">Đang thực hiện</div>
            </div>
            <div class="stat-card">
                <div class="stat-number">4</div>
                <div class="stat-label">Hoàn thành</div>
            </div>
            <div class="stat-card">
                <div class="stat-number">2</div>
                <div class="stat-label">Chờ phụ tùng</div>
            </div>
        </div>
        
        <div class="task-list">
            <h3 style="margin-bottom: 1.5rem; color: #333;">Công việc được giao</h3>
            <div class="task-item">
                <div class="task-info">
                    <h4>Bảo dưỡng định kỳ - Toyota Vios</h4>
                    <p>Biển số: 43A-12345 | Khách hàng: Lê Thị C</p>
                </div>
                <span class="task-status status-in-progress">Đang thực hiện</span>
            </div>
            <div class="task-item">
                <div class="task-info">
                    <h4>Thay dầu nhớt - Honda City</h4>
                    <p>Biển số: 51A-67890 | Khách hàng: Nguyễn Văn B</p>
                </div>
                <span class="task-status status-pending">Chờ xử lý</span>
            </div>
            <div class="task-item">
                <div class="task-info">
                    <h4>Kiểm tra phanh - Ford Focus</h4>
                    <p>Biển số: 30A-11111 | Khách hàng: Trần Thị D</p>
                </div>
                <span class="task-status status-completed">Hoàn thành</span>
            </div>
        </div>
        
        <div class="dashboard-grid">
            <div class="dashboard-card">
                <div class="card-icon">📋</div>
                <h3 class="card-title">Danh sách công việc</h3>
                <p class="card-description">Xem và quản lý các công việc được giao</p>
                <a href="#" class="card-btn">Xem công việc</a>
            </div>
            
            <div class="dashboard-card">
                <div class="card-icon">🔧</div>
                <h3 class="card-title">Thực hiện bảo dưỡng</h3>
                <p class="card-description">Bắt đầu và theo dõi quá trình bảo dưỡng xe</p>
                <a href="#" class="card-btn">Bắt đầu bảo dưỡng</a>
            </div>
            
            <div class="dashboard-card">
                <div class="card-icon">📝</div>
                <h3 class="card-title">Báo cáo công việc</h3>
                <p class="card-description">Cập nhật tiến độ và ghi chú công việc</p>
                <a href="#" class="card-btn">Cập nhật báo cáo</a>
            </div>
            
            <div class="dashboard-card">
                <div class="card-icon">📦</div>
                <h3 class="card-title">Quản lý phụ tùng</h3>
                <p class="card-description">Kiểm tra và yêu cầu phụ tùng cần thiết</p>
                <a href="#" class="card-btn">Quản lý phụ tùng</a>
            </div>
            
            <div class="dashboard-card">
                <div class="card-icon">📊</div>
                <h3 class="card-title">Thống kê cá nhân</h3>
                <p class="card-description">Xem hiệu suất và thành tích công việc</p>
                <a href="#" class="card-btn">Xem thống kê</a>
            </div>
            
            <div class="dashboard-card">
                <div class="card-icon">🔐</div>
                <h3 class="card-title">Đổi mật khẩu</h3>
                <p class="card-description">Thay đổi mật khẩu tài khoản</p>
                <a href="AuthController?action=changePassword" class="card-btn">Đổi mật khẩu</a>
            </div>
        </div>
    </div>
    
    <script>
        // Auto refresh task list every 30 seconds
        setInterval(function() {
            // In a real application, you would fetch updated tasks via AJAX
            console.log('Refreshing technician task list...');
        }, 30000);
        
        // Add click handlers for task items
        document.querySelectorAll('.task-item').forEach(item => {
            item.addEventListener('click', function() {
                // Highlight selected task
                document.querySelectorAll('.task-item').forEach(i => i.style.backgroundColor = '');
                this.style.backgroundColor = '#e3f2fd';
            });
        });
        
        // Add click handlers for dashboard cards
        document.querySelectorAll('.dashboard-card').forEach(card => {
            card.addEventListener('click', function(e) {
                if (e.target.classList.contains('card-btn')) {
                    // Add loading animation
                    e.target.textContent = 'Đang tải...';
                    e.target.style.opacity = '0.7';
                }
            });
        });
    </script>
</body>
</html>
