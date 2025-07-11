<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="Model.Model_NguoiDung"%>
<%
    Model_NguoiDung u = (Model_NguoiDung) session.getAttribute("loggedInUser");
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Cập nhật thông tin</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="container mt-4">

<h3 class="mb-4">Cập nhật thông tin cá nhân</h3>

<% if(request.getAttribute("msg") != null) { %>
    <div class="alert alert-info"><%= request.getAttribute("msg") %></div>
<% } %>

<form action="<%= request.getContextPath() %>/profile" method="post" class="row g-3">
    <div class="col-md-6">
        <label class="form-label">Họ tên</label>
        <input name="hoTen" required value="<%= u.getHoTen() %>" class="form-control">
    </div>
    <div class="col-md-6">
        <label class="form-label">Email</label>
        <input name="email" type="email" required value="<%= u.getEmail() %>" class="form-control">
    </div>
    <div class="col-md-6">
        <label class="form-label">Số điện thoại</label>
        <input name="soDienThoai" required value="<%= u.getSoDienThoai() %>" class="form-control">
    </div>
    <div class="col-12">
        <label class="form-label">Địa chỉ</label>
        <input name="diaChi" required value="<%= u.getDiaChi() %>" class="form-control">
    </div>
    <div class="col-md-4">
        <label class="form-label">Vai trò</label>
        <input value="<%= session.getAttribute("userRole") %>" disabled class="form-control">
    </div>
    <div class="col-12">
        <button class="btn btn-success">Lưu thay đổi</button>
        <a href="<%= request.getContextPath() %>/Index.jsp" class="btn btn-secondary ms-2">&#x21A9; Quay lại</a>
    </div>
</form>

</body>
</html>
