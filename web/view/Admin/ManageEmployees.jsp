<%-- 
    Document   : ManageEmployees
    Created on : Sep 22, 2025, 12:01:43 PM
    Author     : MinHeee
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8" />
        <title>Quản lý Nhân viên</title>
        <style>
            /* Giữ nguyên CSS sidebar từ HomePageForAdmin */
            * {
                box-sizing: border-box;
                margin: 0;
                padding: 0;
            }
            html,body {
                height: 100%;
                font-family: Inter, "See UI", Roboto, Arial, sans-serif;
                background:#f5f7fb;
                color:#111827;
            }

            .app {
                display: flex;
                height: 100vh;
            }

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

            .main {
                flex:1;
                padding:24px 32px;
                overflow:auto;
            }
            h1 {
                margin-bottom:16px;
            }

            table {
                width:100%;
                border-collapse: collapse;
                background:#fff;
                border-radius:8px;
                overflow:hidden;
                box-shadow:0 2px 6px rgba(0,0,0,0.05);
            }
            table thead th {
                background:#f8fafc;
                padding:12px 16px;
                text-align:left;
                border-bottom:1px solid #e5e7eb;
            }
            table tbody td {
                padding:12px 16px;
                border-bottom:1px solid #f1f5f9;
            }

            .btn {
                padding:6px 12px;
                border:none;
                border-radius:6px;
                margin:0 3px;
                cursor:pointer;
                font-size:14px;
            }
            .btn-add {
                background:#28a745;
                color:white;
            }
            .btn-edit {
                background:#ffc107;
                color:black;
            }
            .btn-delete {
                background:#dc3545;
                color:white;
            }
            .btn-detail {
                background:#007bff;
                color:white;
            }




            .modal {
                display: none; /* Luôn ẩn khi load trang */
                position: fixed;
                top: 0;
                left: 0;
                width: 100%;
                height: 100%;
                background: rgba(0,0,0,0.5);
                justify-content: center;
                align-items: center;
            }

            .modal-content {
                background: #fff;
                padding: 20px;
                border-radius: 8px;
                width: 400px;
            }

            .form-grid {
                display: grid;
                grid-template-columns: 1fr 1fr;
                gap: 15px 20px;
            }

            .form-group {
                display: flex;
                flex-direction: column;
            }

            .form-group label {
                font-weight: 600;
                margin-bottom: 5px;
            }

            .form-group input,
            .form-group select {
                padding: 8px 10px;
                border: 1px solid #ccc;
                border-radius: 5px;
            }

            .error {
                color: red;
                font-size: 12px;
                margin-top: 3px;
            }

        </style>
    </head>
    <body>
        <div class="app">
            <!-- Sidebar đồng bộ -->
            <jsp:include page="/view/layout/sidebar.jsp"/>


            <!-- Main content -->
            <main class="main">
                <h2 style="margin-bottom: 20px">Quản lý Nhân viên</h2>
                <button type="button" class="btn btn-add"
                        onclick="document.getElementById('addModal').style.display = 'flex'">
                    ➕ Add Employee
                </button>


                <form action="listEmployees" method="get" 
                      style=" display: flex; justify-content: space-between; align-items: center;">


                    <div style="display: flex; gap: 10px; align-items: center;">
                        <label for="roleFilter">Filter by Role:</label>
                        <select name="roleId" id="roleFilter" onchange="this.form.submit()">
                            <option value="" ${empty selectedRoleId ? "selected":""}>All Roles</option>
                            <c:forEach var="r" items="${roles}">
                                <option value="${r.roleID}" ${param.roleId == r.roleID ? "selected" : ""}>
                                    ${r.roleName}
                                </option>
                            </c:forEach>
                        </select>


                        <input type="text" name="keyword" value="${param.keyword}" placeholder="Search by name/email..." 
                               style="padding: 5px;" />
                        <button type="submit">Search</button>
                    </div>

                    <!-- Right side: Reload button -->
                    <div>
                        <button type="button" onclick="window.location.href = 'listEmployees'">Reload</button>
                    </div>
                </form>


                <br/><br/>
                <table>
                    <thead>
                        <tr>
                            <th>UserID</th>
                            <th>UserCode</th>
                            <th>FullName</th>
                            <th>Email</th>
                            <th>Phone</th>
                            <th>Male</th>
                            <th>Dob</th>
                            <th>Status</th>
                            <th>Position</th>
                            <th>Action</th> 
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="u" items="${users}">
                            <tr>
                                <td>${u.userId}</td>
                                <td>${u.userCode}</td>
                                <td>${u.fullName}</td>
                                <td>${u.email}</td>
                                <td>${u.phone}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${u.male}">Nam</c:when>
                                        <c:otherwise>Nữ</c:otherwise>
                                    </c:choose>
                                </td>
                                <td>${u.dateOfBirth}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${u.isActive}">Active</c:when>
                                        <c:otherwise>Inactive</c:otherwise>
                                    </c:choose>
                                </td>
                                <td>${u.role.roleName}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${u.isActive}">
                                            <button class="btn btn-update" 
                                                    onclick="document.getElementById('modal-${u.userId}').style.display = 'flex'">
                                                Update
                                            </button>                                    
                                            <a href="deleteEmployees?id=${u.userId}&status=0" class="btn btn-delete">
                                                Inactive
                                            </a>
                                        </c:when>
                                        <c:otherwise>
                                            <a href="deleteEmployees?id=${u.userId}&status=1" class="btn btn-add">
                                                Active
                                            </a>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
                <c:forEach var="u" items="${users}">
                    <!-- Modal Update cho user -->
                    <div id="modal-${u.userId}" class="modal">
                        <div class="modal-content">
                            <h2>Cập nhật nhân viên</h2>

                            <form action="${pageContext.request.contextPath}/updateEmployees" method="post">
                                <input type="hidden" name="userId" value="${u.userId}" />

                                <label>UserCode:</label>
                                <input type="text" name="userCode" value="${u.userCode}" /><br/><br/>
                                <span class="error">${errorUserCode}</span>


                                <label>FullName:</label>
                                <input type="text" name="fullName" value="${u.fullName}" /><br/><br/>

                                <label>Email:</label>
                                <input type="email" name="email" value="${u.email}" /><br/><br/>

                                <label>Phone:</label>
                                <input type="text" name="phone" value="${u.phone}" /><br/><br/>
                                <label>Gender:</label>
                                <select name="male">
                                    <option value="true" ${u.male ? "selected" : ""}>Male</option>
                                    <option value="false" ${!u.male ? "selected" : ""}>Female</option>
                                </select><br/><br/>
                                <label>DOB</label>
                                <input type="date" name="DOB" value="${param.DOB != null ? param.DOB : u.dateOfBirth}" />
                                <label>Gender:</label>
                                <label>Status:</label>
                                <select name="isActive">
                                    <option value="true" ${u.isActive ? "selected" : ""}>Active</option>
                                    <option value="false" ${!u.isActive ? "selected" : ""}>Inactive</option>
                                </select><br/><br/>

                                <label>Position</label>
                                <select name="roleID">
                                    <c:forEach var="r" items="${roles}">
                                        <option value="${r.roleID}">${r.roleName}</option>
                                    </c:forEach>
                                </select>

                                <button type="submit" class="btn btn-update">Save</button>
                                <button type="button" class="btn btn-delete"
                                        onclick="document.getElementById('modal-${u.userId}').style.display = 'none'">
                                    Cancel
                                </button>

                            </form>
                        </div>
                    </div>
                </c:forEach>


                <!-- Modal Add Employee -->
                <div id="addModal" class="modal" style="${not empty showAddModal ? 'display:flex' : 'display:none'}">
                    <div class="modal-content" style="width:800px; max-height:90vh; overflow:auto;">

                        <h2 style="margin-bottom:16px;">Thêm Nhân viên</h2>

                        <div class="avatar-preview" style="text-align:center; margin-bottom:20px;">
                            <img id="avatarPreview" src="https://via.placeholder.com/150" alt="Preview"
                                 style="width:120px;height:120px;object-fit:cover;border-radius:50%;border:2px solid #ccc;">
                            <p style="font-size:13px; color:#6b7280;">Preview Image</p>
                        </div>

                        <!-- Form duy nhất -->
                        <form id="addForm" action="addEmployees" method="post" enctype="multipart/form-data" class="form-grid">

                            <!-- User Code -->
                            <div class="form-group">
                                <label>User Code</label>
                                <input type="text" name="userCode" value="${param.userCode}">
                                <span class="error">${errorUserCode}</span>
                            </div>

                            <!-- Full Name -->
                            <div class="form-group">
                                <label>Full Name</label>
                                <input type="text" name="fullName" value="${param.fullName}">
                                <span class="error">${errorFullName}</span>
                            </div>

                            <!-- Username -->
                            <div class="form-group">
                                <label>Username</label>
                                <input type="text" name="username" value="${param.username}">
                                <span class="error">${errorUsername}</span>
                            </div>

                            <!-- Password -->
                            <div class="form-group">
                                <label>Password</label>
                                <input type="password" name="password">
                                <span class="error">${errorPassword}</span>
                            </div>

                            <!-- Email -->
                            <div class="form-group">
                                <label>Email</label>
                                <input type="email" name="email" value="${param.email}">
                                <span class="error">${errorEmail}</span>
                            </div>

                            <!-- Phone -->
                            <div class="form-group">
                                <label>Phone</label>
                                <input type="text" name="phone" value="${param.phone}">
                                <span class="error">${errorPhone}</span>
                            </div>

                            <!-- DOB -->
                            <div class="form-group">
                                <label>DOB</label>
                                <input type="date" name="DOB" value="${param.DOB}">
                                <span class="error">${errorDOB}</span>
                            </div>

                            <!-- Gender -->
                            <div class="form-group">
                                <label>Gender</label>
                                <select name="male">
                                    <option value="1" ${param.male=="1"?"selected":""}>Male</option>
                                    <option value="0" ${param.male=="0"?"selected":""}>Female</option>
                                </select>
                            </div>

                            <!-- Upload Image -->
                            <div class="form-group">
                                <label>Upload Image</label>
                                <input type="file" name="imageFile" accept="image/*" onchange="previewImage(event)">
                            </div>

                            <!-- Position -->
                            <div class="form-group">
                                <label>Position</label>
                                <select name="roleID">
                                    <c:forEach var="role" items="${roles}">
                                        <option value="${role.roleID}"
                                                ${param.roleID == role.roleID ? "selected":""}>
                                            ${role.roleName}, ${role.description}
                                        </option>
                                    </c:forEach>
                                </select>
                            </div>

                            <!-- Status -->
                            <div class="form-group">
                                <label>Status</label>
                                <select name="isActive">
                                    <option value="true" ${param.isActive=="true"?"selected":""}>Active</option>
                                    <option value="false" ${param.isActive=="false"?"selected":""}>Inactive</option>
                                </select>
                            </div>

                            <!-- Buttons -->
                            <div style="margin-top:20px; display:flex; gap:10px; justify-content:flex-end; grid-column: span 2;">
                                <button type="submit" class="btn btn-add">Save</button>
                                <button type="button" class="btn btn-delete"
                                        onclick="document.getElementById('addModal').style.display = 'none'">
                                    Cancel
                                </button>
                            </div>
                        </form>
                    </div>
                </div>

            </main>
        </div>
    </body>
</html>
<script>
    function previewImage(event) {
        const file = event.target.files[0];
        if (file) {
            const reader = new FileReader();
            reader.onload = function (e) {
                document.getElementById("avatarPreview").src = e.target.result;
            };
            reader.readAsDataURL(file);
        }
    }
</script>