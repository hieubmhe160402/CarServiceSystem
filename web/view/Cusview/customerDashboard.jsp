<%-- 
    Document   : Customer Dashboard
    Created on : Sep 19, 2025, 11:38:59 PM
    Author     : MinHeee
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Customer Dashboard - Car Service System</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%);
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
            color: #11998e;
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
            background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%);
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
        
        .car-info {
            background: white;
            border-radius: 15px;
            padding: 2rem;
            margin-bottom: 2rem;
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
        }
        
        .car-item {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 1rem;
            border-bottom: 1px solid #eee;
            transition: background-color 0.3s;
        }
        
        .car-item:hover {
            background-color: #f8f9fa;
        }
        
        .car-item:last-child {
            border-bottom: none;
        }
        
        .car-details h4 {
            color: #333;
            margin-bottom: 0.5rem;
        }
        
        .car-details p {
            color: #666;
            font-size: 0.9rem;
        }
        
        .car-status {
            padding: 0.3rem 0.8rem;
            border-radius: 15px;
            font-size: 0.8rem;
            font-weight: bold;
        }
        
        .status-good {
            background: #d4edda;
            color: #155724;
        }
        
        .status-warning {
            background: #fff3cd;
            color: #856404;
        }
        
        .status-danger {
            background: #f8d7da;
            color: #721c24;
        }
        
        .customer-badge {
            background: linear-gradient(135deg, #11998e, #38ef7d);
            color: white;
            padding: 0.3rem 0.8rem;
            border-radius: 20px;
            font-size: 0.8rem;
            font-weight: bold;
        }
        
        .quick-actions {
            background: white;
            border-radius: 15px;
            padding: 2rem;
            margin-bottom: 2rem;
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
        }
        
        .quick-actions h3 {
            margin-bottom: 1.5rem;
            color: #333;
        }
        
        .action-buttons {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
            gap: 1rem;
        }
        
        .action-btn {
            padding: 1rem;
            background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%);
            color: white;
            border: none;
            border-radius: 10px;
            font-weight: 600;
            cursor: pointer;
            transition: transform 0.2s ease;
            text-decoration: none;
            text-align: center;
        }
        
        .action-btn:hover {
            transform: translateY(-2px);
        }
    </style>
</head>
<body>
    <div class="header">
        <div class="logo">🚗 Car Service System</div>
        <div class="user-info">
            <span class="user-name">${currentUser.fullName}</span>
            <span class="customer-badge">CUSTOMER</span>
            <a href="AuthController?action=logout" class="logout-btn">Đăng xuất</a>
        </div>
    </div>
    
    <div class="container">
        <div class="welcome-card">
            <h1 class="welcome-title">Chào mừng Khách hàng!</h1>
            <p class="welcome-subtitle">Quản lý xe và dịch vụ bảo dưỡng của bạn</p>
        </div>
        
        <div class="car-info">
            <h3 style="margin-bottom: 1.5rem; color: #333;">Thông tin xe của bạn</h3>
            <div class="car-item">
                <div class="car-details">
                    <h4>Toyota Vios 2018</h4>
                    <p>Biển số: 43A-12345 | Màu: Trắng | Năm: 2018</p>
                </div>
                <span class="car-status status-good">Tốt</span>
            </div>
            <div class="car-item">
                <div class="car-details">
                    <h4>Honda City 2020</h4>
                    <p>Biển số: 51A-67890 | Màu: Đen | Năm: 2020</p>
                </div>
                <span class="car-status status-warning">Cần bảo dưỡng</span>
            </div>
        </div>
        
        <div class="quick-actions">
            <h3>Thao tác nhanh</h3>
            <div class="action-buttons">
                <a href="#" class="action-btn">📅 Đặt lịch hẹn</a>
                <a href="#" class="action-btn">🔧 Yêu cầu bảo dưỡng</a>
                <a href="#" class="action-btn">📋 Xem lịch sử</a>
                <a href="#" class="action-btn">💰 Thanh toán</a>
            </div>
        </div>
        
        <div class="dashboard-grid">
            <div class="dashboard-card">
                <div class="card-icon">📅</div>
                <h3 class="card-title">Đặt lịch hẹn</h3>
                <p class="card-description">Đặt lịch hẹn bảo dưỡng và sửa chữa xe</p>
                <a href="#" class="card-btn">Đặt lịch hẹn</a>
            </div>
            
            <div class="dashboard-card">
                <div class="card-icon">🚗</div>
                <h3 class="card-title">Quản lý xe</h3>
                <p class="card-description">Xem thông tin và lịch sử xe của bạn</p>
                <a href="#" class="card-btn">Quản lý xe</a>
            </div>
            
            <div class="dashboard-card">
                <div class="card-icon">📋</div>
                <h3 class="card-title">Lịch sử dịch vụ</h3>
                <p class="card-description">Xem lịch sử bảo dưỡng và sửa chữa</p>
                <a href="#" class="card-btn">Xem lịch sử</a>
            </div>
            
            <div class="dashboard-card">
                <div class="card-icon">💰</div>
                <h3 class="card-title">Thanh toán</h3>
                <p class="card-description">Xem hóa đơn và thanh toán dịch vụ</p>
                <a href="#" class="card-btn">Thanh toán</a>
            </div>
            
            <div class="dashboard-card">
                <div class="card-icon">⭐</div>
                <h3 class="card-title">Đánh giá dịch vụ</h3>
                <p class="card-description">Đánh giá chất lượng dịch vụ nhận được</p>
                <a href="#" class="card-btn">Đánh giá</a>
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
        // Auto refresh car status every 30 seconds
        setInterval(function() {
            // In a real application, you would fetch updated car status via AJAX
            console.log('Refreshing customer car status...');
        }, 30000);
        
        // Add click handlers for car items
        document.querySelectorAll('.car-item').forEach(item => {
            item.addEventListener('click', function() {
                // Highlight selected car
                document.querySelectorAll('.car-item').forEach(i => i.style.backgroundColor = '');
                this.style.backgroundColor = '#e8f5e8';
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
        
        // Add click handlers for quick action buttons
        document.querySelectorAll('.action-btn').forEach(btn => {
            btn.addEventListener('click', function(e) {
                // Add loading animation
                const originalText = this.textContent;
                this.textContent = 'Đang tải...';
                this.style.opacity = '0.7';
                
                // Reset after 2 seconds (in real app, this would be after actual action)
                setTimeout(() => {
                    this.textContent = originalText;
                    this.style.opacity = '1';
                }, 2000);
            });
        });
    </script>
</body>
</html>
