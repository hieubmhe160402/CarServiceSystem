<%-- 
    Document   : Reset Password
    Created on : Sep 19, 2025, 11:38:59 PM
    Author     : MinHeee
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Đặt lại mật khẩu - Car Service System</title>
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
        
        .reset-password-container {
            background: white;
            padding: 40px;
            border-radius: 15px;
            box-shadow: 0 15px 35px rgba(0, 0, 0, 0.1);
            width: 100%;
            max-width: 450px;
        }
        
        .reset-password-header {
            text-align: center;
            margin-bottom: 30px;
        }
        
        .reset-password-header h1 {
            color: #333;
            font-size: 28px;
            margin-bottom: 10px;
        }
        
        .reset-password-header p {
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
        
        .btn-reset {
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
        
        .btn-reset:hover {
            transform: translateY(-2px);
        }
        
        .btn-reset:active {
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
        
        .info-box {
            background: #e3f2fd;
            padding: 15px;
            border-radius: 8px;
            margin-bottom: 20px;
            border-left: 4px solid #2196f3;
        }
        
        .info-box h4 {
            color: #1976d2;
            margin-bottom: 8px;
        }
        
        .info-box p {
            color: #666;
            font-size: 14px;
            margin-bottom: 5px;
        }
        
        .token-display {
            background: #f8f9fa;
            padding: 15px;
            border-radius: 8px;
            margin-bottom: 20px;
            text-align: center;
            border: 2px dashed #dee2e6;
        }
        
        .token-display h4 {
            color: #333;
            margin-bottom: 10px;
        }
        
        .token-display .token {
            font-family: 'Courier New', monospace;
            font-size: 14px;
            color: #667eea;
            word-break: break-all;
            background: white;
            padding: 10px;
            border-radius: 4px;
            border: 1px solid #e1e5e9;
        }
        
        .token-display p {
            color: #666;
            font-size: 12px;
            margin-top: 10px;
        }
        
        .steps {
            background: #f8f9fa;
            padding: 20px;
            border-radius: 8px;
            margin-bottom: 20px;
        }
        
        .steps h4 {
            color: #333;
            margin-bottom: 15px;
        }
        
        .steps ol {
            margin-left: 20px;
            color: #666;
            font-size: 14px;
        }
        
        .steps li {
            margin-bottom: 8px;
        }
    </style>
</head>
<body>
    <div class="reset-password-container">
        <div class="reset-password-header">
            <h1>Đặt lại mật khẩu</h1>
            <p>Nhập email để nhận link đặt lại mật khẩu</p>
        </div>
        
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
        
        <c:if test="${not empty token}">
            <div class="token-display">
                <h4>Token đặt lại mật khẩu:</h4>
                <div class="token">${token}</div>
                <p>Token này có hiệu lực trong 15 phút. Vui lòng sử dụng để đặt lại mật khẩu.</p>
            </div>
            
            <div class="info-box">
                <h4>Hướng dẫn sử dụng:</h4>
                <p>1. Copy token ở trên</p>
                <p>2. Click vào nút "Đặt lại mật khẩu" bên dưới</p>
                <p>3. Nhập token và mật khẩu mới</p>
            </div>
            
            <a href="${pageContext.request.contextPath}/AuthController?action=newPassword&token=${token}" 
               class="btn-reset" style="text-decoration: none; display: block;">
                Đặt lại mật khẩu
            </a>
        </c:if>
        
        <c:if test="${empty token}">
            <div class="steps">
                <h4>Quy trình đặt lại mật khẩu:</h4>
                <ol>
                    <li>Nhập email đã đăng ký tài khoản</li>
                    <li>Hệ thống sẽ tạo token đặt lại mật khẩu</li>
                    <li>Sử dụng token để đặt lại mật khẩu mới</li>
                    <li>Token có hiệu lực trong 15 phút</li>
                </ol>
            </div>
            
            <form action="${pageContext.request.contextPath}/AuthController" method="post">
                <input type="hidden" name="action" value="resetPassword">
                
                <div class="form-group">
                    <label for="email">Email đăng ký:</label>
                    <input type="email" id="email" name="email" required 
                           placeholder="Nhập email đã đăng ký" value="${param.email}">
                </div>
                
                <button type="submit" class="btn-reset">Gửi yêu cầu đặt lại</button>
            </form>
        </c:if>
        
        <a href="${pageContext.request.contextPath}/AuthController?action=login" class="btn-back">
            Quay lại đăng nhập
        </a>
    </div>
    
    <script>
        // Email validation
        document.getElementById('email').addEventListener('input', function() {
            const email = this.value;
            const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
            
            if (email && !emailRegex.test(email)) {
                this.style.borderColor = '#dc3545';
            } else {
                this.style.borderColor = '#e1e5e9';
            }
        });
        
        // Form validation
        document.querySelector('form')?.addEventListener('submit', function(e) {
            const email = document.getElementById('email').value.trim();
            
            if (!email) {
                e.preventDefault();
                alert('Vui lòng nhập email!');
                return false;
            }
            
            const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
            if (!emailRegex.test(email)) {
                e.preventDefault();
                alert('Vui lòng nhập email hợp lệ!');
                return false;
            }
        });
        
        // Auto focus on email field
        document.getElementById('email')?.focus();
    </script>
</body>
</html>
