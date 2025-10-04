<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Role List - Admin</title>
    <style>
        body { font-family: Inter, "Segoe UI", Roboto, Arial, sans-serif; margin:0; background:#f5f7fb; }
        .app { display:flex; height:100vh; }

        /* Sidebar */
        .sidebar { width:260px; background: linear-gradient(180deg,#0f2340,#0b1830); color:#fff; padding:28px 18px; display:flex; flex-direction:column; }
        .brand { font-weight:800; font-size:18px; letter-spacing:1px; margin-bottom:22px; }
        .nav { display:flex; flex-direction:column; gap:8px; margin-top:12px; }
        .nav a { color:rgba(255,255,255,0.9); text-decoration:none; padding:10px 12px; border-radius:10px; display:flex; align-items:center; gap:12px; }
        .nav a.active, .nav a:hover { background: rgba(255,255,255,0.04); }

        /* Main content */
        .main { flex:1; padding:24px 32px; overflow:auto; }
        .topbar { display:flex; justify-content:space-between; align-items:center; margin-bottom:18px; }
        .search input { padding:10px 12px; border-radius:12px; border:1px solid #e6eef8; width:300px; }
        .btn-add { background:green; color:white; border:none; padding:8px 12px; border-radius:8px; cursor:pointer; }
        .btn-delete { background:red; color:white; border:none; padding:6px 10px; border-radius:6px; cursor:pointer; }
        table { width:100%; border-collapse:collapse; margin-top:16px; background:#fff; border-radius:8px; overflow:hidden; }
        table th, table td { padding:12px 16px; border-bottom:1px solid #eef2f7; text-align:center; }
        table th { background:#f8fafc; color:#374151; }
        table tr:hover { background: #f1f5f9; }

        /* Popup modal */
        .modal { display:none; position:fixed; z-index:1000; left:0; top:0; width:100%; height:100%; background: rgba(0,0,0,0.5); }
        .modal-content { background:#fff; padding:20px; margin:10% auto; width:400px; border-radius:8px; text-align:center; }
        .modal-content input { width:90%; padding:6px; margin:5px 0; }
        .close { float:right; cursor:pointer; font-weight:bold; }
    </style>
</head>
<body>
    <div class="app">
        <aside class="sidebar">
            <div class="brand">CAR CARE SYSTEM</div>
            <nav class="nav">
                <a class="active" href="#">Dashboard</a>
                <a href="#">Quản lý Role</a>
                <a href="#">Quản lý nhân viên</a>
            </nav>
        </aside>

        <main class="main">
            <div class="topbar">
                <div class="search"><input type="text" placeholder="Tìm kiếm Role..." onkeyup="searchRole(this.value)"></div>
                <div><button class="btn-add" onclick="openModal()">+ Thêm Role</button></div>
            </div>

            <table id="roleTable">
                <thead>
                    <tr>
                        <th>RoleID</th>
                        <th>Role Name</th>
                        <th>Description</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="r" items="${listRole}">
                        <tr>
                            <td>${r.roleID}</td>
                            <td>${r.roleName}</td>
                            <td>${r.description}</td>
                            <td>
                                <form action="rolelist" method="post" style="display:inline;">
                                    <input type="hidden" name="action" value="delete"/>
                                    <input type="hidden" name="roleId" value="${r.roleID}"/>
                                    <button type="submit" class="btn-delete" onclick="return confirm('Bạn có chắc muốn xóa role này?');">Xóa</button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>

            <!-- Popup thêm Role -->
            <div id="addModal" class="modal">
                <div class="modal-content">
                    <span class="close" onclick="closeModal()">&times;</span>
                    <h3>Thêm Role mới</h3>
                    <form action="rolelist" method="post">
                        <input type="hidden" name="action" value="add"/>
                        <input type="text" name="roleName" placeholder="Tên role" required/>
                        <input type="text" name="description" placeholder="Mô tả"/>
                        <br/><br/>
                        <button type="submit" class="btn-add">Lưu</button>
                    </form>
                </div>
            </div>

        </main>
    </div>

    <script>
        function openModal() { document.getElementById("addModal").style.display = "block"; }
        function closeModal() { document.getElementById("addModal").style.display = "none"; }
        window.onclick = function(event) {
            if(event.target==document.getElementById("addModal")) closeModal();
        }

        // Simple client-side search filter
        function searchRole(keyword){
            keyword = keyword.toLowerCase();
            const rows = document.querySelectorAll("#roleTable tbody tr");
            rows.forEach(r=>{
                const name = r.cells[1].textContent.toLowerCase();
                r.style.display = name.includes(keyword) ? "" : "none";
            });
        }
    </script>
</body>
</html>
