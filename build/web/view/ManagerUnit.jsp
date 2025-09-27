<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Quản lý Unit</title>
        <style>
            table {
                width: 80%;
                margin: 20px auto;
                border-collapse: collapse;
            }
            th, td {
                border: 1px solid #ccc;
                padding: 8px;
                text-align: center;
            }
            th {
                background-color: #f2f2f2;
            }
            a.btn {
                padding: 5px 10px;
                margin: 2px;
                border-radius: 4px;
                text-decoration: none;
                color: white;
            }
            .add {
                background-color: green;
            }
            .update {
                background-color: orange;
                cursor: pointer;
            }
            .delete {
                background-color: red;
            }

            /* Modal */
            .modal {
                display: none;
                position: fixed;
                z-index: 1000;
                left: 0;
                top: 0;
                width: 100%;
                height: 100%;
                background-color: rgba(0,0,0,0.5);
            }
            .modal-content {
                background: white;
                width: 400px;
                margin: 10% auto;
                padding: 20px;
                border-radius: 8px;
                text-align: left;
            }
            .btn-save {
                background: green;
                color: #fff;
                border: none;
                padding: 8px 15px;
                border-radius: 4px;
                cursor: pointer;
            }
            .btn-cancel {
                background: red;
                color: #fff;
                border: none;
                padding: 8px 15px;
                border-radius: 4px;
                cursor: pointer;
            }
            .back-link {
                display: inline-block;
                margin: 10px 0;
                padding: 8px 15px;
                background-color: #007bff;
                color: white;
                text-decoration: none;
                border-radius: 4px;
            }
        </style>
    </head>
    <body>
        <a href="${pageContext.request.contextPath}/ListProduct" class="back-link">← Quay lại Product</a>
        <h2 style="text-align: center;">Danh sách Unit</h2>

        <div style="text-align: center; margin-bottom: 15px;">
            <a class="btn add" onclick="document.getElementById('modal-add').style.display = 'block'">➕ Thêm mới</a>
        </div>

        <div id="modal-add" class="modal">
            <div class="modal-content">
                <h2>Thêm mới Unit</h2>
                <form action="${pageContext.request.contextPath}/AddUnit" method="post">
                    <label>Tên Unit:</label>
                    <input type="text" name="name" required /><br/><br/>

                    <label>Loại:</label>
                    <input type="text" name="type" placeholder="VD: Đơn vị đo, Đơn vị thời gian" required /><br/><br/>

                    <label>Mô tả:</label>
                    <textarea name="description"></textarea><br/><br/>

                    <button type="submit" class="btn-save">Lưu</button>
                    <button type="button" class="btn-cancel"
                            onclick="document.getElementById('modal-add').style.display = 'none'">
                        Hủy
                    </button>
                </form>
            </div>
        </div>

        <table>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Tên Unit</th>
                    <th>Loại</th>
                    <th>Mô tả</th>
                    <th>Hành động</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="u" items="${listUnit}">
                    <tr>
                        <td>${u.unitId}</td>
                        <td>${u.name}</td>
                        <td>${u.type}</td>
                        <td>${u.description}</td>
                        <td>
                            <a class="btn update" onclick="document.getElementById('modal-${u.unitId}').style.display = 'block'">✏️ Sửa</a>
                            <a href="DeleteUnit?id=${u.unitId}" 
                               class="btn delete" 
                               onclick="return confirm('Bạn có chắc muốn xóa Unit này không?')">
                                🗑️ Xóa
                            </a>
                        </td>
                    </tr>

                    <!-- Popup modal cho từng Unit -->
                <div id="modal-${u.unitId}" class="modal">
                    <div class="modal-content">
                        <h2>Cập nhật Unit</h2>
                        <form action="${pageContext.request.contextPath}/UpdateUnit" method="post">
                            <input type="hidden" name="id" value="${u.unitId}" />

                            <label>Tên Unit:</label>
                            <input type="text" name="name" value="${u.name}" required /><br/><br/>

                            <label>Loại:</label>
                            <input type="text" name="type" value="${u.type}" required /><br/><br/>

                            <label>Mô tả:</label>
                            <textarea name="description">${u.description}</textarea><br/><br/>

                            <button type="submit" class="btn-save">Lưu</button>
                            <button type="button" class="btn-cancel"
                                    onclick="document.getElementById('modal-${u.unitId}').style.display = 'none'">
                                Hủy
                            </button>
                        </form>
                    </div>
                </div>
            </c:forEach>
        </tbody>
    </table>

    <script>
        // Đóng modal khi click ra ngoài
        window.onclick = function (event) {
            let modals = document.getElementsByClassName("modal");
            for (let m of modals) {
                if (event.target == m) {
                    m.style.display = "none";
                }
            }
        }
    </script>
</body>
</html>