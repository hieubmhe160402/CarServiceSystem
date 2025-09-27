<%-- 
    Document   : Change Password
    Created on : Sep 19, 2025, 11:38:59 PM
    Author     : MinHeee
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Đổi mật khẩu - Car Service System</title>
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
            display: flex;
            align-items: center;
            justify-content: center;
        }
        
        .change-password-container {
            background: white;
            padding: 40px;
            border-radius: 15px;
            box-shadow: 0 15px 35px rgba(0, 0, 0, 0.1);
            width: 100%;
            max-width: 450px;
        }
        
        .change-password-header {
            text-align: center;
            margin-bottom: 30px;
        }
        
        .change-password-header h1 {
            color: #333;
            font-size: 28px;
            margin-bottom: 10px;
        }
        
        .change-password-header p {
            color: #666;
            font-size: 14px;
        }
        
        .user-info {
            background: #f8f9fa;
            padding: 15px;
            border-radius: 8px;
            margin-bottom: 25px;
            text-align: center;
        }
        
        .user-info h3 {
            color: #333;
            margin-bottom: 5px;
        }
        
        .user-info p {
            color: #666;
            font-size: 14px;
        }
        
        .form-group {
            margin-bottom: 20px;
        }
        
        .form-group label {
            display: block;
            margin-bottom: 8px;
            color: #333;
            font-weight: 500;
        }
        
        .form-group input {
            width: 100%;
            padding: 12px 15px;
            border: 2px solid #e1e5e9;
            border-radius: 8px;
            font-size: 16px;
            transition: border-color 0.3s ease;
        }
        
        .form-group input:focus {
            outline: none;
            border-color: #667eea;
        }
        
        .password-strength {
            margin-top: 5px;
            font-size: 12px;
            color: #666;
        }
        
        .password-strength.weak {
            color: #dc3545;
        }
        
        .password-strength.medium {
            color: #ffc107;
        }
        
        .password-strength.strong {
            color: #28a745;
        }
        
        .btn-change {
            width: 100%;
            padding: 12px;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border: none;
            border-radius: 8px;
            font-size: 16px;
            font-weight: 600;
            cursor: pointer;
            transition: transform 0.2s ease;
            margin-bottom: 15px;
        }
        
        .btn-change:hover {
            transform: translateY(-2px);
        }
        
        .btn-change:active {
            transform: translateY(0);
        }
        
        .btn-back {
            width: 100%;
            padding: 12px;
            background: #6c757d;
            color: white;
            border: none;
            border-radius: 8px;
            font-size: 16px;
            font-weight: 600;
            cursor: pointer;
            transition: background-color 0.3s ease;
            text-decoration: none;
            display: inline-block;
            text-align: center;
        }
        
        .btn-back:hover {
            background: #5a6268;
        }
        
        .alert {
            padding: 12px 15px;
            border-radius: 8px;
            margin-bottom: 20px;
            font-size: 14px;
        }
        
        .alert-error {
            background-color: #f8d7da;
            color: #721c24;
            border: 1px solid #f5c6cb;
        }
        
        .alert-success {
            background-color: #d4edda;
            color: #155724;
            border: 1px solid #c3e6cb;
        }
        
        .password-requirements {
            background: #f8f9fa;
            padding: 15px;
            border-radius: 8px;
            margin-bottom: 20px;
            font-size: 13px;
            color: #666;
        }
        
        .password-requirements h4 {
            margin-bottom: 8px;
            color: #333;
        }
        
        .password-requirements ul {
            margin-left: 20px;
        }
        
        .password-requirements li {
            margin-bottom: 3px;
        }
    </style>
</head>
<body>
    <div class="change-password-container">
        <div class="change-password-header">
            <h1>Đổi mật khẩu</h1>
            <p>Cập nhật mật khẩu để bảo mật tài khoản của bạn</p>
        </div>
        
        <c:if test="${not empty sessionScope.user}">
            <div class="user-info">
                <h3>${sessionScope.user.fullName}</h3>
                <p>@${sessionScope.user.userName}</p>
            </div>
        </c:if>
        
        <c:if test="${not empty error}">
            <div class="alert alert-error">
                ${error}
            </div>
        </c:if>
        
        <c:if test="${not empty success}">
            <div class="alert alert-success">
                ${success}
            </div>
        </c:if>
        
        <div class="password-requirements">
            <h4>Yêu cầu mật khẩu:</h4>
            <ul>
                <li>Ít nhất 6 ký tự</li>
                <li>Không được trùng với mật khẩu cũ</li>
                <li>Nên bao gồm chữ hoa, chữ thường và số</li>
            </ul>
        </div>
        
        <form action="${pageContext.request.contextPath}/AuthController" method="post">
            <input type="hidden" name="action" value="changePassword">
            
            <div class="form-group">
                <label for="oldPassword">Mật khẩu hiện tại:</label>
                <input type="password" id="oldPassword" name="oldPassword" required 
                       placeholder="Nhập mật khẩu hiện tại">
            </div>
            
            <div class="form-group">
                <label for="newPassword">Mật khẩu mới:</label>
                <input type="password" id="newPassword" name="newPassword" required 
                       placeholder="Nhập mật khẩu mới">
                <div id="passwordStrength" class="password-strength"></div>
            </div>
            
            <div class="form-group">
                <label for="confirmPassword">Xác nhận mật khẩu mới:</label>
                <input type="password" id="confirmPassword" name="confirmPassword" required 
                       placeholder="Nhập lại mật khẩu mới">
            </div>
            
            <button type="submit" class="btn-change">Đổi mật khẩu</button>
        </form>
        
        <a href="${pageContext.request.contextPath}/PermissionGroupController" class="btn-back">
            Quay lại trang chủ
        </a>
    </div>
    
    <script>
        // Password strength checker
        document.getElementById('newPassword').addEventListener('input', function() {
            const password = this.value;
            const strengthDiv = document.getElementById('passwordStrength');
            
            if (password.length === 0) {
                strengthDiv.textContent = '';
                strengthDiv.className = 'password-strength';
                return;
            }
            
            let strength = 0;
            let feedback = '';
            
            if (password.length >= 6) strength++;
            if (/[a-z]/.test(password)) strength++;
            if (/[A-Z]/.test(password)) strength++;
            if (/[0-9]/.test(password)) strength++;
            if (/[^A-Za-z0-9]/.test(password)) strength++;
            
            if (strength < 3) {
                feedback = 'Mật khẩu yếu';
                strengthDiv.className = 'password-strength weak';
            } else if (strength < 4) {
                feedback = 'Mật khẩu trung bình';
                strengthDiv.className = 'password-strength medium';
            } else {
                feedback = 'Mật khẩu mạnh';
                strengthDiv.className = 'password-strength strong';
            }
            
            strengthDiv.textContent = feedback;
        });
        
        // Confirm password validation
        document.getElementById('confirmPassword').addEventListener('input', function() {
            const newPassword = document.getElementById('newPassword').value;
            const confirmPassword = this.value;
            
            if (confirmPassword && newPassword !== confirmPassword) {
                this.style.borderColor = '#dc3545';
            } else {
                this.style.borderColor = '#e1e5e9';
            }
        });
        
        // Form validation
        document.querySelector('form').addEventListener('submit', function(e) {
            const oldPassword = document.getElementById('oldPassword').value.trim();
            const newPassword = document.getElementById('newPassword').value.trim();
            const confirmPassword = document.getElementById('confirmPassword').value.trim();
            
            if (!oldPassword || !newPassword || !confirmPassword) {
                e.preventDefault();
                alert('Vui lòng nhập đầy đủ thông tin!');
                return false;
            }
            
            if (newPassword !== confirmPassword) {
                e.preventDefault();
                alert('Mật khẩu mới và xác nhận mật khẩu không khớp!');
                return false;
            }
            
            if (newPassword.length < 6) {
                e.preventDefault();
                alert('Mật khẩu mới phải có ít nhất 6 ký tự!');
                return false;
            }
            
            if (oldPassword === newPassword) {
                e.preventDefault();
                alert('Mật khẩu mới không được trùng với mật khẩu cũ!');
                return false;
            }
        });
    </script>
</body>
</html>
