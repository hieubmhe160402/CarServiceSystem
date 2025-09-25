<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Add New Employee</title>
    <div style="text-align:left; margin: 10px 20px;">
        <a href="ListEmployees" style="text-decoration:none; color:black;">
            ← Back to List
        </a>
    </div>
    <style>
        .container {
            display: flex;
            justify-content: center;
            align-items: flex-start;
            margin-top: 20px;
        }
        .avatar-preview {
            margin-right: 20px;
            text-align: center;
        }
        .avatar-preview img {
            width: 150px;
            height: 150px;
            object-fit: cover;
            border-radius: 50%;
            border: 2px solid #ccc;
        }
        form {
            width: 30%;
            background: #f9f9f9;
            padding: 20px;
            border-radius: 8px;
        }
        label {
            display: block;
            margin-top: 10px;
        }
        input, select {
            width: 100%;
            padding: 8px;
            margin-top: 5px;
        }
        button {
            margin-top: 15px;
            padding: 10px;
            width: 100%;
            background: #4CAF50;
            color: white;
            border: none;
            border-radius: 4px;
        }
    </style>
</head>
<body>
    <h2 style="text-align:center;">Add New Employee</h2>

    <div class="container">
        <!-- Avatar Preview -->
        <div class="avatar-preview">
            <img id="avatarPreview" src="https://via.placeholder.com/150" alt="Preview">
            <p>Preview Image</p>
        </div>

        <!-- Form -->
        <form action="AddEmployees" method="post" enctype="multipart/form-data">
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

            <button type="submit">Save Employee</button>
        </form>
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
