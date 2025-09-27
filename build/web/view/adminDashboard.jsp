<%-- 
    Document   : Admin Dashboard
    Created on : Sep 19, 2025, 11:38:59 PM
    Author     : MinHeee
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Admin Dashboard - Car Service System</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
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
            color: #667eea;
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
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
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
            color: #667eea;
            margin-bottom: 0.5rem;
        }
        
        .stat-label {
            color: #666;
            font-size: 0.9rem;
        }
        
        .admin-badge {
            background: linear-gradient(135deg, #ff6b6b, #ee5a24);
            color: white;
            padding: 0.3rem 0.8rem;
            border-radius: 20px;
            font-size: 0.8rem;
            font-weight: bold;
        }
    </style>
</head>
<body>
    <div class="header">
        <div class="logo">🚗 Car Service System</div>
        <div class="user-info">
            <span class="user-name">${currentUser.fullName}</span>
            <span class="admin-badge">ADMIN</span>
            <a href="AuthController?action=logout" class="logout-btn">Đăng xuất</a>
        </div>
    </div>
    
    <div class="container">
        <div class="welcome-card">
            <h1 class="welcome-title">Chào mừng Admin!</h1>
            <p class="welcome-subtitle">Quản lý toàn bộ hệ thống Car Service System</p>
        </div>
        
        <div class="stats-grid">
            <div class="stat-card">
                <div class="stat-number">156</div>
                <div class="stat-label">Tổng số khách hàng</div>
            </div>
            <div class="stat-card">
                <div class="stat-number">23</div>
                <div class="stat-label">Kỹ thuật viên</div>
            </div>
            <div class="stat-card">
                <div class="stat-number">89</div>
                <div class="stat-label">Lịch hẹn hôm nay</div>
            </div>
            <div class="stat-card">
                <div class="stat-number">45</div>
                <div class="stat-label">Bảo dưỡng đang thực hiện</div>
            </div>
        </div>
        
        <div class="dashboard-grid">
            <div class="dashboard-card">
                <div class="card-icon">👥</div>
                <h3 class="card-title">Quản lý người dùng</h3>
                <p class="card-description">Quản lý thông tin khách hàng, kỹ thuật viên và phân quyền</p>
                <a href="#" class="card-btn">Quản lý người dùng</a>
            </div>
            
            <div class="dashboard-card">
                <div class="card-icon">🚗</div>
                <h3 class="card-title">Quản lý xe</h3>
                <p class="card-description">Quản lý thông tin xe, lịch sử bảo dưỡng và lịch hẹn</p>
                <a href="#" class="card-btn">Quản lý xe</a>
            </div>
            
            <div class="dashboard-card">
                <div class="card-icon">🔧</div>
                <h3 class="card-title">Quản lý dịch vụ</h3>
                <p class="card-description">Quản lý dịch vụ bảo dưỡng, phụ tùng và nhà cung cấp</p>
                <a href="#" class="card-btn">Quản lý dịch vụ</a>
            </div>
            
            <div class="dashboard-card">
                <div class="card-icon">📊</div>
                <h3 class="card-title">Báo cáo thống kê</h3>
                <p class="card-description">Xem báo cáo doanh thu, hiệu suất và thống kê hệ thống</p>
                <a href="#" class="card-btn">Xem báo cáo</a>
            </div>
            
            <div class="dashboard-card">
                <div class="card-icon">⚙️</div>
                <h3 class="card-title">Cài đặt hệ thống</h3>
                <p class="card-description">Cấu hình hệ thống, phân quyền và cài đặt chung</p>
                <a href="#" class="card-btn">Cài đặt</a>
            </div>
            
            <div class="dashboard-card">
                <div class="card-icon">🔐</div>
                <h3 class="card-title">Đổi mật khẩu</h3>
                <p class="card-description">Thay đổi mật khẩu tài khoản admin</p>
                <a href="AuthController?action=changePassword" class="card-btn">Đổi mật khẩu</a>
            </div>
        </div>
    </div>
    
    <script>
        // Auto refresh stats every 30 seconds
        setInterval(function() {
            // In a real application, you would fetch updated stats via AJAX
            console.log('Refreshing admin dashboard stats...');
        }, 30000);
        
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
