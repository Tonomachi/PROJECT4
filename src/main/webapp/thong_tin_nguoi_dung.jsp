<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="Model.Model_NguoiDung" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Thông tin tài khoản - FoodSach.com</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/custom.css">
</head>
<body>

<jsp:include page="/Custom_index.jsp" />

<div class="container mt-4">
   <a href="<%= request.getContextPath() %>/Index.jsp" class="btn btn-outline-primary mb-3">
    <i class="fas fa-arrow-left"></i> Quay lại trang chính
</a>

    <%
        Model_NguoiDung user = (Model_NguoiDung) session.getAttribute("loggedInUser");
        if (user != null) {
    %>
    <div class="card p-4 shadow">
        <h3 class="mb-4">Thông tin tài khoản của bạn</h3>
        <ul class="list-group list-group-flush">
            <li class="list-group-item"><strong>Họ tên:</strong> <%= user.getHoTen() %></li>
            <li class="list-group-item"><strong>Email:</strong> <%= user.getEmail() %></li>
            <li class="list-group-item"><strong>Số điện thoại:</strong> <%= user.getSoDienThoai() %></li>
            <li class="list-group-item"><strong>Địa chỉ:</strong> <%= user.getDiaChi() %></li>
            <li class="list-group-item"><strong>Vai trò:</strong> <%= user.getVaiTro() %></li>
        </ul>
        <div class="mt-4 d-flex gap-2">
           <a href="<%= request.getContextPath() %>/profile" class="btn btn-success">Chỉnh sửa thông tin</a>
    <a href="<%= request.getContextPath() %>/doi-mat-khau" class="btn btn-outline-secondary">Đổi mật khẩu</a>

        </div>
    </div>
    <%
        } else {
    %>
    <div class="alert alert-warning text-center">
        Bạn chưa đăng nhập. Vui lòng <a href="<%= request.getContextPath() %>/Login.jsp">đăng nhập</a> để xem thông tin tài khoản.
    </div>
    <%
        }
    %>
</div>

<jsp:include page="/footer.jsp" />

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
