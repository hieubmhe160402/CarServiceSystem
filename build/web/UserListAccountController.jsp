<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List, model.User"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Danh sách Users</title>
    <style>
        table { border-collapse: collapse; width: 100%; }
        th, td { border: 1px solid #ddd; padding: 8px; }
        th { background: #f4f4f4; }
        button {
            padding: 4px 8px;
            background: #4CAF50;
            color: #fff;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }
        /* Modal */
        .modal {
            display: none;
            position: fixed; 
            z-index: 999;
            left: 0; top: 0;
            width: 100%; height: 100%;
            background: rgba(0,0,0,0.5);
        }
        .modal-content {
            background: #fff;
            margin: 10% auto;
            padding: 20px;
            border-radius: 8px;
            width: 400px;
        }
    </style>
</head>
<body>
    <h2>Danh sách Users và Roles</h2>
    <table>
        <tr>
            <th>FullName</th>
            <th>Username</th>
            <th>Email</th>
            <th>Phone</th>
            <th>Active</th>
            <th>Role</th>
            <th>Action</th>
        </tr>
        <%
            List<User> list = (List<User>) request.getAttribute("userList");
            if (list != null) {
                for (User u : list) {
                    int roleId = (u.getRole() != null) ? u.getRole().getRoleID() : 0;
        %>
        <tr>
            <td><%= u.getFullName() %></td>
            <td><%= u.getUserName() %></td>
            <td><%= u.getEmail() %></td>
            <td><%= u.getPhone() %></td>
            <td><%= u.isIsActive() ? "Yes" : "No" %></td>
            <td><%= u.getRole() != null ? u.getRole().getRoleName() : "Chưa có role" %></td>
            <td>
                <% if (roleId > 0) { %>
                    <button onclick="openPermissionModal(<%= roleId %>, '<%= u.getRole().getRoleName() %>')">
                        Phân quyền
                    </button>
                <% } else { %>
                    <span>Không có role</span>
                <% } %>
            </td>
        </tr>
        <%
                }
            }
        %>
    </table>

    <!-- Modal -->
    <div id="permissionModal" class="modal">
        <div class="modal-content">
            <h3>Phân quyền cho Role: <span id="roleNameText"></span></h3>
            <form id="permissionForm">
                <input type="hidden" name="roleId" id="roleId" />
                <div id="permissionList">
                    <!-- checkboxes sẽ được load ở đây -->
                </div>
                <br/>
                <button type="button" onclick="submitPermissions()">Cập nhật</button>
                <button type="button" onclick="closePermissionModal()">Đóng</button>
            </form>
        </div>
    </div>

    <script>
        function openPermissionModal(roleId, roleName) {
            document.getElementById("roleId").value = roleId;
            document.getElementById("roleNameText").innerText = roleName;

            fetch("rolePermission?roleId=" + roleId)
                .then(res => res.json())
                .then(data => {
                    let html = "";
                    data.allPermissions.forEach(p => {
                        let checked = data.rolePermissions.some(rp => rp.permissionID === p.permissionID);
                        html += `
                          <label>
                            <input type="checkbox" name="permissionIds" value="${p.permissionID}" ${checked ? "checked" : ""}/>
                            ${p.name}
                          </label><br/>
                        `;
                    });
                    document.getElementById("permissionList").innerHTML = html;
                    document.getElementById("permissionModal").style.display = "block";
                });
        }

        function closePermissionModal() {
            document.getElementById("permissionModal").style.display = "none";
        }

        function submitPermissions() {
            const form = document.getElementById("permissionForm");
            const formData = new FormData(form);

            fetch("rolePermission", {
                method: "POST",
                body: formData
            }).then(() => {
                alert("Cập nhật thành công!");
                closePermissionModal();
                location.reload();
            });
        }
    </script>
</body>
</html>
