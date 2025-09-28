<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.*, model.ServiceHistory"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Lịch sử dịch vụ</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        table { width: 100%; border-collapse: collapse; margin-top: 20px; }
        th, td { border: 1px solid #ddd; padding: 8px; text-align: center; }
        th { background-color: #f2f2f2; }
        h1 { color: #333; }
    </style>
</head>
<body>
    <h1>Danh sách dịch vụ khách hàng đã sử dụng</h1>
    <table>
        <tr>
            
            <th>Tên khách hàng</th>
            <th>Biển số xe</th>
            <th>Mã bảo dưỡng</th>
            <th>Ngày bảo dưỡng</th>
            <th>Tên dịch vụ</th>
            <th>Số lượng</th>
            <th>Đơn giá</th>
            <th>Thành tiền</th>
        </tr>

        <%
            List<ServiceHistory> histories = (List<ServiceHistory>) request.getAttribute("histories");
            if (histories == null || histories.isEmpty()) {
        %>
            <tr><td colspan="9" style="color:red;">⚠️ Không có dữ liệu dịch vụ</td></tr>
        <%
            } else {
                for (ServiceHistory sh : histories) {
        %>
            <tr>

                <td><%= sh.getCustomerName() %></td>
                <td><%= sh.getLicensePlate() %></td>
                <td><%= sh.getMaintenanceId() %></td>
                <td><%= sh.getMaintenanceDate() %></td>
                <td><%= sh.getServiceName() %></td>
                <td><%= sh.getQuantity() %></td>
                <td><%= sh.getUnitPrice() %></td>
                <td><%= sh.getTotalPrice() %></td>
            </tr>
        <%
                }
            }
        %>
    </table>
</body>
</html>
