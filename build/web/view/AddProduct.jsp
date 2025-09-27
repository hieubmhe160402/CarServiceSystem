<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Add Product</title>
        <style>
            form {
                width: 600px;
                margin: 20px auto;
                padding: 15px;
                border: 1px solid #ccc;
                border-radius: 6px;
            }
            label {
                display: block;
                margin-top: 10px;
                font-weight: bold;
            }
            input, select, textarea {
                width: 100%;
                padding: 6px;
                margin-top: 4px;
                box-sizing: border-box;
            }
            .btn {
                margin-top: 15px;
                padding: 8px 14px;
                border: none;
                border-radius: 4px;
                cursor: pointer;
            }
            .btn-save {
                background-color: #4CAF50;
                color: white;
            }
            .btn-cancel {
                background-color: #ccc;
                margin-left: 10px;
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
        <h2 style="text-align: center">${product != null ? 'Cập nhật sản phẩm' : 'Thêm sản phẩm mới'}</h2>

        <form action="${pageContext.request.contextPath}/${product != null ? 'UpdateProduct' : 'AddProduct'}" method="post">
            <!-- Trường ẩn để xác định ID khi Update -->
            <c:if test="${product != null}">
                <input type="hidden" name="id" value="${product.productId}">
            </c:if>

            <label>Mã sản phẩm (Code):</label>
            <input type="text" name="code" value="${product != null ? product.code : ''}" required>

            <label>Tên sản phẩm (Name):</label>
            <input type="text" name="name" value="${product != null ? product.name : ''}" required>

            <label>Loại (Type):</label>
            <select name="type" onchange="location = '${pageContext.request.contextPath}/${product != null ? 'UpdateProduct' : 'AddProduct'}?id=${product.productId}&type=' + this.value;">
                <c:forEach var="t" items="${types}">
                    <option value="${t}" ${t == selectedType ? "selected" : ""}>${t}</option>
                </c:forEach>
            </select>

            <label>Giá (Price):</label>
            <input type="number" step="0.01" name="price" value="${product != null ? product.price : ''}" required>

            <label>Mô tả (Description):</label>
            <textarea name="description">${product != null ? product.description : ''}</textarea>

            <label>Hình ảnh (Image URL):</label>
            <input type="text" name="image" value="${product != null ? product.image : ''}">

            <label>Danh mục (Category):</label>
            <select name="categoryName">
                <c:forEach var="n" items="${names}">
                    <option value="${n}" ${product != null && product.category != null && product.category.name == n ? 'selected' : ''}>${n}</option>
                </c:forEach>
            </select>

            <label for="unit">Đơn vị:</label>
            <select name="unitId" id="unit">
                <c:forEach var="u" items="${units}">
                    <option value="${u.unitId}" ${product != null && product.unit != null && product.unit.unitId == u.unitId ? 'selected' : ''}>${u.name}</option>
                </c:forEach>
            </select>

            <label>Bảo hành (tháng):</label>
            <input type="number" name="warrantyPeriodMonths" value="${product != null ? product.warrantyPeriodMonths : 0}" min="0" step="1" required>

            <label>Tồn kho tối thiểu:</label>
            <input type="number" name="minStockLevel" value="${product != null ? product.minStockLevel : 0}" min="0" step="1" required>

            <label>Thời lượng ước tính (giờ):</label>
            <input type="number" name="estimatedDurationHours" value="${product != null ? product.estimatedDurationHours : 0}" min="0" step="0.1" required>

            <label>Kích hoạt:</label>
            <input type="checkbox" name="isActive" ${product != null && product.isActive ? 'checked' : 'checked'}>

            <div style="text-align: center">
                <button type="submit" class="btn btn-save">${product != null ? 'Cập nhật' : 'Lưu'}</button>
                <a href="${pageContext.request.contextPath}/ListProduct">
                    <button type="button" class="btn btn-cancel">Hủy</button>
                </a>
            </div>
        </form>

    </body>
</html>