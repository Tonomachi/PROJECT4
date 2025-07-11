<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="Model.Model_NguoiDung" %>
<%
    Model_NguoiDung user = (Model_NguoiDung) request.getAttribute("user");
    if (user == null) {
        response.sendRedirect(request.getContextPath() + "/Login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Cập nhật thông tin cá nhân</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
</head>
<body>
<jsp:include page="/Custom_index.jsp" />

<div class="container mt-5 mb-5" style="max-width: 700px;">
    <h3 class="text-center mb-4">Cập nhật thông tin cá nhân</h3>

    <% if (request.getAttribute("message") != null) { %>
        <div class="alert alert-success text-center"><%= request.getAttribute("message") %></div>
    <% } %>
    <% if (request.getAttribute("errorMessage") != null) { %>
        <div class="alert alert-danger text-center"><%= request.getAttribute("errorMessage") %></div>
    <% } %>

    <form action="<%= request.getContextPath() %>/profile" method="post">
        <div class="mb-3">
            <label for="hoTen" class="form-label">Họ tên</label>
            <input type="text" class="form-control" id="hoTen" name="hoTen"
                   value="<%= user.getHoTen() %>" required>
        </div>

        <div class="mb-3">
            <label for="email" class="form-label">Email</label>
            <input type="email" class="form-control" id="email" name="email"
                   value="<%= user.getEmail() %>" required>
        </div>

        <div class="mb-3">
            <label for="soDienThoai" class="form-label">Số điện thoại</label>
            <input type="text" class="form-control" id="soDienThoai" name="soDienThoai"
                   value="<%= user.getSoDienThoai() %>" required>
        </div>

        <div class="mb-3">
            <label for="diaChi" class="form-label">Địa chỉ</label>
            <input type="text" class="form-control" id="diaChi" name="diaChi"
                   value="<%= user.getDiaChi() %>" required>
        </div>

        <div class="mb-3">
            <label class="form-label">Vai trò</label>
            <input type="text" class="form-control" value="<%= user.getVaiTro() %>" disabled>
        </div>

        <div class="text-end">
            <button type="submit" class="btn btn-primary">Cập nhật</button>
           <a href="<%= request.getContextPath() %>/thong_tin_nguoi_dung.jsp" class="btn btn-secondary">Quay lại</a>

        </div>
    </form>
</div>

<jsp:include page="/footer.jsp" />
</body>
</html>
