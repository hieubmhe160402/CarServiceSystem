<%-- 
    Document   : CustomerAndCarDetailController
    Created on : Sep 24, 2025, 9:02:29 PM
    Author     : Admin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="model.CustomerProfile"%>
<%@page import="java.util.List"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Customer Profiles</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        table { width: 100%; border-collapse: collapse; margin-top: 20px; }
        th, td { border: 1px solid #ddd; padding: 8px; text-align: center; }
        th { background-color: #f2f2f2; }
        h1 { color: #333; }
    </style>
</head>
<body>
    <h1>Danh sách khách hàng</h1>
    <table>
        <tr>
            
            <th>Họ tên</th>
            <th>Email</th>
            <th>Điện thoại</th>
            <th>Biển số</th>
            <th>Hãng xe</th>
            <th>Model</th>
            <th>Năm</th>
            <th>Ngày bảo dưỡng gần nhất</th>
            <th>Ngày bảo dưỡng kế tiếp</th>
        </tr>
        <%
    List<CustomerProfile> customers = (List<CustomerProfile>) request.getAttribute("customers");
    if (customers == null) {
%>
    <tr><td colspan="10" style="color:red;">❌ Không có dữ liệu - kiểm tra DAO hoặc servlet</td></tr>
    <p>Số lượng khách hàng: <%= (customers != null ? customers.size() : 0) %></p>

<%
    } else if (customers.isEmpty()) {
%>
    <tr><td colspan="10" style="color:orange;">⚠️ Không tìm thấy khách hàng nào</td></tr>
<%
    } else {
        for (CustomerProfile cp : customers) {
%>

        <tr>
            
            <td><%= cp.getFullName() %></td>
            <td><%= cp.getEmail() %></td>
            <td><%= cp.getPhone() %></td>
            <td><%= cp.getLicensePlate() != null ? cp.getLicensePlate() : "" %></td>
            <td><%= cp.getBrand() != null ? cp.getBrand() : "" %></td>
            <td><%= cp.getModel() != null ? cp.getModel() : "" %></td>
            <td><%= cp.getYear() > 0 ? cp.getYear() : "" %></td>
            <td><%= cp.getLastMaintenanceDate() != null ? cp.getLastMaintenanceDate() : "" %></td>
            <td><%= cp.getNextMaintenanceDate() != null ? cp.getNextMaintenanceDate() : "" %></td>
        </tr>
        <%
                }
            }
        %>
    </table>
</body>
</html>

