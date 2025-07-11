<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Lịch sử đăng nhập</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>

<jsp:include page="/Header.jsp"/>

<%
    HttpSession sessionCheck = request.getSession(false);
    String role = (sessionCheck != null) ? (String) sessionCheck.getAttribute("userRole") : null;

    if (role == null || !role.equalsIgnoreCase("admin")) {
        response.sendError(403, "Bạn không có quyền truy cập!");
        return;
    }
%>

<div class="container mt-4">
    <h3 class="mb-3 text-center">Lịch sử đăng nhập</h3>

    <%
        List<String[]> history = (List<String[]>) request.getAttribute("history");
        String err = (String) request.getAttribute("error");
        if (err != null) {
    %>
        <div class="alert alert-danger"><%= err %></div>
    <%
        } else if (history == null || history.isEmpty()) {
    %>
        <div class="alert alert-info">Chưa có bản ghi đăng nhập.</div>
    <%
        } else {
    %>
    <table class="table table-bordered table-striped">
        <thead class="table-light">
            <tr>
                <th>#</th>
                <th>Họ tên</th>
                <th>Thời gian</th>
                <th>Địa chỉ IP</th>
            </tr>
        </thead>
        <tbody>
        <% for (String[] row : history) { %>
            <tr>
                <td><%= row[0] %></td>
                <td><%= row[1] %></td>
                <td><%= row[2] %></td>
                <td><%= row[3] %></td>
            </tr>
        <% } %>
        </tbody>
    </table>
    <% } %>

    <a href="<%= request.getContextPath() %>/danh_sach_san_pham.jsp" class="btn btn-secondary mt-3">
        <i class="fas fa-arrow-left"></i> Quay lại trang chủ
    </a>
</div>

<jsp:include page="/footer.jsp"/>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
