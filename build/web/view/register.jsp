<%-- 
    Document   : Register
    Created on : Sep 19, 2025, 11:38:59 PM
    Author     : MinHeee
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Đăng ký tài khoản - Car Service System</title>
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
            display: flex;
            align-items: center;
            justify-content: center;
            padding: 20px;
        }
        
        .register-container {
            background: white;
            padding: 40px;
            border-radius: 15px;
            box-shadow: 0 15px 35px rgba(0, 0, 0, 0.1);
            width: 100%;
            max-width: 600px;
        }
        
        .register-header {
            text-align: center;
            margin-bottom: 30px;
        }
        
        .register-header h1 {
            color: #333;
            font-size: 28px;
            margin-bottom: 10px;
        }
        
        .register-header p {
            color: #666;
            font-size: 14px;
        }
        
        .form-row {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 20px;
            margin-bottom: 20px;
        }
        
        .form-group {
            margin-bottom: 20px;
        }
        
        .form-group.full-width {
            grid-column: 1 / -1;
        }
        
        .form-group label {
            display: block;
            margin-bottom: 8px;
            color: #333;
            font-weight: 500;
        }
        
        .form-group input, .form-group select {
            width: 100%;
            padding: 12px 15px;
            border: 2px solid #e1e5e9;
            border-radius: 8px;
            font-size: 16px;
            transition: border-color 0.3s ease;
        }
        
        .form-group input:focus, .form-group select:focus {
            outline: none;
            border-color: #11998e;
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
        
        .btn-register {
            width: 100%;
            padding: 12px;
            background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%);
            color: white;
            border: none;
            border-radius: 8px;
            font-size: 16px;
            font-weight: 600;
            cursor: pointer;
            transition: transform 0.2s ease;
            margin-bottom: 15px;
        }
        
        .btn-register:hover {
            transform: translateY(-2px);
        }
        
        .btn-register:active {
            transform: translateY(0);
        }
        
        .btn-login {
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
        
        .btn-login:hover {
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
        
        .form-requirements {
            background: #f8f9fa;
            padding: 15px;
            border-radius: 8px;
            margin-bottom: 20px;
            font-size: 13px;
            color: #666;
        }
        
        .form-requirements h4 {
            margin-bottom: 8px;
            color: #333;
        }
        
        .form-requirements ul {
            margin-left: 20px;
        }
        
        .form-requirements li {
            margin-bottom: 3px;
        }
        
        .customer-badge {
            background: linear-gradient(135deg, #11998e, #38ef7d);
            color: white;
            padding: 0.3rem 0.8rem;
            border-radius: 20px;
            font-size: 0.8rem;
            font-weight: bold;
            display: inline-block;
            margin-bottom: 10px;
        }
        
        .success-login {
            text-align: center;
            margin-top: 20px;
            padding: 15px;
            background: #e8f5e8;
            border-radius: 8px;
            border: 1px solid #c3e6cb;
        }
        
        .success-login a {
            color: #11998e;
            text-decoration: none;
            font-weight: 600;
        }
        
        .success-login a:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>
    <div class="register-container">
        <div class="register-header">
            <div class="customer-badge">ĐĂNG KÝ KHÁCH HÀNG</div>
            <h1>Đăng ký tài khoản</h1>
            <p>Chỉ dành cho khách hàng - Tạo tài khoản để sử dụng dịch vụ</p>
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
            <div class="success-login">
                <p>Đăng ký thành công! Bạn có thể <a href="AuthController?action=login">đăng nhập ngay</a> với tài khoản: <strong>${registeredUsername}</strong></p>
            </div>
        </c:if>
        
        <c:if test="${empty success}">
            <div class="form-requirements">
                <h4>Yêu cầu đăng ký:</h4>
                <ul>
                    <li>Tất cả thông tin đều bắt buộc</li>
                    <li>Tên đăng nhập phải duy nhất</li>
                    <li>Email phải hợp lệ và chưa được sử dụng</li>
                    <li>Mật khẩu tối thiểu 6 ký tự</li>
                    <li>Chỉ có thể đăng ký với vai trò Khách hàng</li>
                </ul>
            </div>
            
            <form action="${pageContext.request.contextPath}/RegisterController" method="post">
                <div class="form-row">
                    <div class="form-group">
                        <label for="fullName">Họ và tên *:</label>
                        <input type="text" id="fullName" name="fullName" required 
                               placeholder="Nhập họ và tên" value="${param.fullName}">
                    </div>
                    
                    <div class="form-group">
                        <label for="userName">Tên đăng nhập *:</label>
                        <input type="text" id="userName" name="userName" required 
                               placeholder="Nhập tên đăng nhập" value="${param.userName}">
                    </div>
                </div>
                
                <div class="form-row">
                    <div class="form-group">
                        <label for="password">Mật khẩu *:</label>
                        <input type="password" id="password" name="password" required 
                               placeholder="Nhập mật khẩu">
                        <div id="passwordStrength" class="password-strength"></div>
                    </div>
                    
                    <div class="form-group">
                        <label for="confirmPassword">Xác nhận mật khẩu *:</label>
                        <input type="password" id="confirmPassword" name="confirmPassword" required 
                               placeholder="Nhập lại mật khẩu">
                    </div>
                </div>
                
                <div class="form-row">
                    <div class="form-group">
                        <label for="email">Email *:</label>
                        <input type="email" id="email" name="email" required 
                               placeholder="Nhập email" value="${param.email}">
                    </div>
                    
                    <div class="form-group">
                        <label for="phone">Số điện thoại *:</label>
                        <input type="tel" id="phone" name="phone" required 
                               placeholder="Nhập số điện thoại" value="${param.phone}">
                    </div>
                </div>
                
                <div class="form-row">
                    <div class="form-group">
                        <label for="gender">Giới tính *:</label>
                        <select id="gender" name="gender" required>
                            <option value="">Chọn giới tính</option>
                            <option value="male" ${param.gender == 'male' ? 'selected' : ''}>Nam</option>
                            <option value="female" ${param.gender == 'female' ? 'selected' : ''}>Nữ</option>
                        </select>
                    </div>
                    
                    <div class="form-group">
                        <label for="dateOfBirth">Ngày sinh *:</label>
                        <input type="date" id="dateOfBirth" name="dateOfBirth" required 
                               value="${param.dateOfBirth}">
                    </div>
                </div>
                
                <button type="submit" class="btn-register">Đăng ký tài khoản</button>
            </form>
        </c:if>
        
        <a href="AuthController?action=login" class="btn-login">
            Đã có tài khoản? Đăng nhập ngay
        </a>
    </div>
    
    <script>
        // Password strength checker
        document.getElementById('password').addEventListener('input', function() {
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
            const password = document.getElementById('password').value;
            const confirmPassword = this.value;
            
            if (confirmPassword && password !== confirmPassword) {
                this.style.borderColor = '#dc3545';
            } else {
                this.style.borderColor = '#e1e5e9';
            }
        });
        
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
        
        // Phone validation
        document.getElementById('phone').addEventListener('input', function() {
            const phone = this.value;
            const phoneRegex = /^[0-9+\-\s()]+$/;
            
            if (phone && !phoneRegex.test(phone)) {
                this.style.borderColor = '#dc3545';
            } else {
                this.style.borderColor = '#e1e5e9';
            }
        });
        
        // Form validation
        document.querySelector('form')?.addEventListener('submit', function(e) {
            const fullName = document.getElementById('fullName').value.trim();
            const userName = document.getElementById('userName').value.trim();
            const password = document.getElementById('password').value.trim();
            const confirmPassword = document.getElementById('confirmPassword').value.trim();
            const email = document.getElementById('email').value.trim();
            const phone = document.getElementById('phone').value.trim();
            const gender = document.getElementById('gender').value;
            const dateOfBirth = document.getElementById('dateOfBirth').value;
            
            if (!fullName || !userName || !password || !confirmPassword || !email || !phone || !gender || !dateOfBirth) {
                e.preventDefault();
                alert('Vui lòng nhập đầy đủ thông tin!');
                return false;
            }
            
            if (password !== confirmPassword) {
                e.preventDefault();
                alert('Mật khẩu và xác nhận mật khẩu không khớp!');
                return false;
            }
            
            if (password.length < 6) {
                e.preventDefault();
                alert('Mật khẩu phải có ít nhất 6 ký tự!');
                return false;
            }
            
            const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
            if (!emailRegex.test(email)) {
                e.preventDefault();
                alert('Email không hợp lệ!');
                return false;
            }
            
            // Check age (must be at least 16 years old)
            const today = new Date();
            const birthDate = new Date(dateOfBirth);
            const age = today.getFullYear() - birthDate.getFullYear();
            const monthDiff = today.getMonth() - birthDate.getMonth();
            
            if (monthDiff < 0 || (monthDiff === 0 && today.getDate() < birthDate.getDate())) {
                age--;
            }
            
            if (age < 16) {
                e.preventDefault();
                alert('Bạn phải ít nhất 16 tuổi để đăng ký!');
                return false;
            }
        });
        
        // Auto focus on first field
        document.getElementById('fullName').focus();
    </script>
</body>
</html>
