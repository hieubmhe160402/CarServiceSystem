<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8" />
        <title>Thêm Nhân viên</title>
        <style>
            /* CSS chung */
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

            /* Main content */
            .main {
                flex:1;
                padding:24px 32px;
                overflow:auto;

                /* căn giữa ngang */
                display:flex;
                justify-content:center;
                align-items:flex-start;
            }

            .form-wrapper {
                width:100%;
                max-width:700px;
            }

            h1 {
                margin-bottom:16px;
            }

            .form-container {
                background:#fff;
                padding:24px;
                border-radius:8px;
                box-shadow:0 2px 6px rgba(0,0,0,0.05);
                width:100%;
            }

            label {
                display:block;
                margin-top:12px;
                font-weight:500;
            }
            input, select {
                width:100%;
                padding:10px;
                margin-top:6px;
                border:1px solid #d1d5db;
                border-radius:6px;
            }
            .btn {
                padding:10px 16px;
                border:none;
                border-radius:6px;
                cursor:pointer;
                font-size:14px;
                margin-top:20px;
            }
            .btn-save {
                background:#0f2340;
                color:white;
                width:100%;
            }

            .avatar-preview {
                text-align:center;
                margin-bottom:20px;
            }
            .avatar-preview img {
                width:120px;
                height:120px;
                object-fit:cover;
                border-radius:50%;
                border:2px solid #ccc;
            }
        </style>
    </head>
    <body>
        <div class="app">
            <!-- Sidebar -->
            <aside class="sidebar">
                <div class="brand">CAR CARE SYSTEM</div>
                <nav class="nav">
                    <a href="./HomePageForAdmin.jsp"><span class="ico"></span> Dashboard</a>
                    <a class="active" href="./ManageEmployees.jsp"><span class="ico"></span> Quản lý nhân viên</a>
                    <a href="#"><span class="ico"></span> Quản lý dịch vụ</a>
                    <a href="#"><span class="ico"></span> Quản lý phụ tùng</a>
                    <a href="#"><span class="ico"></span> Quản lý Service Centre</a>
                    <a href="#"><span class="ico"></span> Log hệ thống</a>
                    <a href="#"><span class="ico"></span> Báo cáo</a>
                </nav>
                <div style="flex:1"></div>
                <div style="font-size:12px; color:rgba(255,255,255,0.6);">© 2025 CarCare</div>
            </aside>

            <!-- Main -->
            <main class="main">
                <div class="form-wrapper">
                    <h1>Thêm Nhân viên</h1>
                    <div style="margin-bottom:16px;">
                        <a href="listEmployees" style="text-decoration:none; color:#374151;">← Quay lại danh sách</a>
                    </div>

                    <div class="form-container">
                        <div class="avatar-preview">
                            <img id="avatarPreview" src="https://via.placeholder.com/150" alt="Preview">
                            <p style="font-size:13px; color:#6b7280;">Preview Image</p>
                        </div>

                        <form action="addEmployees" method="post" enctype="multipart/form-data">
                            <label>User Code</label>
                            <input type="text" name="userCode" value="${param.userCode}">
                            <span style="color:red">${errorUserCode}</span>

                            <label>Full Name</label>
                            <input type="text" name="fullName" value="${param.fullName}">
                            <span style="color:red">${errorFullName}</span>

                            <label>Username</label>
                            <input type="text" name="username" value="${param.username}">
                            <span style="color:red">${errorUsername}</span>

                            <label>Password</label>
                            <input type="password" name="password">
                            <span style="color:red">${errorPassword}</span>

                            <label>Email</label>
                            <input type="email" name="email" value="${param.email}">
                            <span style="color:red">${errorEmail}</span>

                            <label>Phone</label>
                            <input type="text" name="phone" value="${param.phone}">
                            <span style="color:red">${errorPhone}</span>

                            <label>DOB</label>
                            <input type="date" name="DOB" value="${param.DOB}">
                            <span style="color:red">${errorDOB}</span>

                            <label>Gender</label>
                            <select name="male">
                                <option value="1">Male</option>
                                <option value="0">Female</option>
                            </select>

                            <label>Upload Image</label>
                            <input type="file" name="imageFile" accept="image/*" onchange="previewImage(event)">

                            <label>Position</label>
                            <select name="roleID">
                                <c:forEach var="role" items="${roles}">
                                    <option value="${role.roleID}">${role.roleName}, ${role.description}</option>
                                </c:forEach>
                            </select>

                            <label>Status</label>
                            <select name="isActive">
                                <option value="true">Active</option>
                                <option value="false">Inactive</option>
                            </select>

                            <button type="submit" class="btn btn-save">Save Employee</button>
                        </form>
                    </div>
                </div>
            </main>
        </div>

        <script>
            function previewImage(event) {
                const reader = new FileReader();
                reader.onload = function () {
                    document.getElementById('avatarPreview').src = reader.result;
                }
                reader.readAsDataURL(event.target.files[0]);
            }
        </script>
    </body>
</html>
