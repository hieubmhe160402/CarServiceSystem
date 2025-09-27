<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Product List</title>
        <style>
            table {
                border-collapse: collapse;
                width: 100%;
                margin-top: 15px;
            }
            th, td {
                border: 1px solid #ccc;
                padding: 8px;
                text-align: left;
            }
            th {
                background: #eee;
            }
            img {
                width: 80px;
                height: auto;
            }
            .btn {
                padding: 6px 12px;
                margin: 5px 0;
                border: none;
                border-radius: 4px;
                cursor: pointer;
            }
            .btn-add {
                background-color: #4CAF50;
                color: white;
            }
            .btn-add:hover {
                background-color: #45a049;
            }
            .btn-unit {
                background-color: #17a2b8;
                color: white;
            }
            .btn-unit:hover {
                background-color: #138496;
            }
            .btn-category {
                background-color: #6f42c1;
                color: white;
            }
            .btn-category:hover {
                background-color: #5a32a3;
            }
            .btn-update {
                background-color: #2196F3;
                color: white;
            }
            .btn-update:hover {
                background-color: #0b7dda;
            }
            .btn-delete {
                background-color: #f44336;
                color: white;
            }
            .btn-delete:hover {
                background-color: #da190b;
            }
            .btn-group {
                margin-bottom: 15px;
            }
        </style>
    </head>
    <body>
        <h2>Danh sách sản phẩm</h2>

        <!-- Nhóm nút quản lý -->
        <div class="btn-group">
            <form action="${pageContext.request.contextPath}/AddProduct" method="get" style="display:inline;">
                <button type="submit" class="btn btn-add">+ Add Product</button>
            </form>
            
            <form action="${pageContext.request.contextPath}/ListUnit" method="get" style="display:inline;">
                <button type="submit" class="btn btn-unit">📏 Quản lý Unit</button>
            </form>
            
            <form action="${pageContext.request.contextPath}/ListCategory" method="get" style="display:inline;">
                <button type="submit" class="btn btn-category">📂 Quản lý Category</button>
            </form>
        </div>

        <table>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Code</th>
                    <th>Name</th>
                    <th>Type</th>
                    <th>Price</th>
                    <th>Description</th>
                    <th>Category</th>
                    <th>Unit</th>
                    <th>Warranty (Months)</th>
                    <th>Min Stock</th>
                    <th>Estimated Duration (Hours)</th>
                    <th>Active</th>
                    <th>Image</th>
                    <th>Created Date</th>
                    <th>Action</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="p" items="${products}">
                    <tr>
                        <td>${p.productId}</td>
                        <td>${p.code}</td>
                        <td>${p.name}</td>
                        <td>${p.type}</td>
                        <td>${p.price}</td>
                        <td>${p.description}</td>
                        <td>
                            <c:if test="${p.category != null}">
                                ${p.category.name}
                            </c:if>
                        </td>
                        <td>
                            <c:if test="${p.unit != null}">
                                ${p.unit.name}
                            </c:if>
                        </td>
                        <td>${p.warrantyPeriodMonths}</td>
                        <td>${p.minStockLevel}</td>
                        <td>${p.estimatedDurationHours}</td>
                        <td>
                            <c:choose>
                                <c:when test="${p.isActive}">Yes</c:when>
                                <c:otherwise>No</c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <c:if test="${not empty p.image}">
                                <img src="${p.image}" alt="image"/>
                            </c:if>
                        </td>
                        <td>${p.createdDate}</td>
                        <td>
                            <form action="${pageContext.request.contextPath}/UpdateProduct" method="get" style="display:inline;">
                                <input type="hidden" name="id" value="${p.productId}"/>
                                <button type="submit" class="btn btn-update">Update</button>
                            </form>
                            <a href="DeleteProduct?id=${p.productId}" 
                               onclick="return confirm('Bạn có chắc chắn muốn xóa sản phẩm này?');"
                               class="btn btn-delete">
                                Delete
                            </a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

    </body>
</html>