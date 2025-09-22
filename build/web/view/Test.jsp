<%-- 
    Document   : Test
    Created on : Sep 19, 2025, 11:38:59 PM
    Author     : MinHeee
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Permission Groups</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background: #f4f6f9;
            margin: 0;
            padding: 20px;
        }
        h1 {
            color: #333;
            text-align: center;
            margin-bottom: 20px;
        }
        table {
            border-collapse: collapse;
            margin: 20px auto;
            width: 80%;
            background: #fff;
            box-shadow: 0 2px 6px rgba(0,0,0,0.1);
        }
        table th, table td {
            padding: 12px 15px;
            text-align: left;
        }
        table th {
            background: #007bff;
            color: #fff;
            text-transform: uppercase;
            font-size: 14px;
            letter-spacing: 1px;
        }
        table tr:nth-child(even) {
            background: #f9f9f9;
        }
        table tr:hover {
            background: #eaf3ff;
        }

        /* Button */
        .btn {
            display: inline-block;
            background: #007bff;
            color: white;
            padding: 10px 15px;
            text-decoration: none;
            border-radius: 5px;
            margin: 10px auto;
        }
        .btn:hover {
            background: #0056b3;
        }

        /* Modal styles */
        .modal {
            display: none; 
            position: fixed; 
            z-index: 1; 
            left: 0;
            top: 0;
            width: 100%; 
            height: 100%; 
            background-color: rgba(0,0,0,0.4); 
        }
        .modal-content {
            background-color: #fff;
            margin: 10% auto; 
            padding: 20px;
            border-radius: 8px;
            width: 400px;
            box-shadow: 0 4px 8px rgba(0,0,0,0.2);
        }
        .modal-content h2 {
            margin-top: 0;
        }
        .modal-content input, .modal-content textarea {
            width: 100%;
            padding: 8px;
            margin: 8px 0;
            border-radius: 4px;
            border: 1px solid #ccc;
        }
        .close {
            float: right;
            font-size: 20px;
            cursor: pointer;
            color: #aaa;
        }
        .close:hover {
            color: #000;
        }
    </style>
</head>
<body>
    <h1>Danh sách Permission Groups</h1>

    <!-- Nút Add -->
    <div style="text-align:center;">
        <button class="btn" onclick="openModal()">+ Add Permission Group</button>
    </div>

    <!-- Bảng hiển thị -->
    <table>
        <tr>
            <th>GroupID</th>
            <th>GroupName</th>
            <th>Description</th>
        </tr>
        <c:forEach var="g" items="${groups}">
            <tr>
                <td>${g.groupID}</td>
                <td>${g.groupName}</td>
                <td>${g.description}</td>
            </tr>
        </c:forEach>
    </table>

    <!-- Modal popup -->
    <div id="addModal" class="modal">
        <div class="modal-content">
            <span class="close" onclick="closeModal()">&times;</span>
            <h2>Thêm Permission Group</h2>
           <form action="${pageContext.request.contextPath}/PermissionGroupController" method="post">
    <label>Group Name:</label>
    <input type="text" name="groupName" required>

    <label>Description:</label>
    <textarea name="description"></textarea>

    <button type="submit" class="btn">Save</button>
</form>

        </div>
    </div>

    <script>
        function openModal() {
            document.getElementById("addModal").style.display = "block";
        }
        function closeModal() {
            document.getElementById("addModal").style.display = "none";
        }
        // Đóng khi click ra ngoài modal
        window.onclick = function(event) {
            let modal = document.getElementById("addModal");
            if (event.target === modal) {
                closeModal();
            }
        }
    </script>
</body>
</html>