<%-- File: webapp/register.jsp --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Đăng ký tài khoản mới</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
</head>
<body>
    <div class="container mt-5">
        <div class="card p-4 mx-auto" style="max-width: 500px;">
            <h2 class="card-title text-center mb-4">Đăng ký tài khoản mới</h2>
            <%
                String errorMessage = (String) request.getAttribute("errorMessage");
                String successMessage = (String) request.getAttribute("successMessage");
                if (errorMessage != null && !errorMessage.isEmpty()) {
            %>
                <div class="alert alert-danger" role="alert">
                    <%= errorMessage %>
                </div>
            <%
                } else if (successMessage != null && !successMessage.isEmpty()) {
            %>
                <div class="alert alert-success" role="alert">
                    <%= successMessage %> <a href="<%= request.getContextPath() %>/login.jsp">Đăng nhập ngay!</a> <%-- Đã thêm context path --%>
                </div>
            <%
                }
            %>
            <form action="<%= request.getContextPath() %>/RegisterServlet" method="post"> <%-- Đã thêm context path --%>
                <div class="mb-3">
                    <label for="hoTen" class="form-label">Họ Tên:</label>
                    <input type="text" class="form-control" id="hoTen" name="hoTen" required>
                </div>
                <div class="mb-3">
                    <label for="email" class="form-label">Email đăng nhập:</label>
                    <input type="email" class="form-control" id="email" name="email" required>
                </div>
                <div class="mb-3">
                    <label for="matKhau" class="form-label">Mật khẩu:</label>
                    <input type="password" class="form-control" id="matKhau" name="matKhau" required>
                </div>
                <div class="mb-3">
                    <label for="xacNhanMatKhau" class="form-label">Xác nhận mật khẩu:</label>
                    <input type="password" class="form-control" id="xacNhanMatKhau" name="xacNhanMatKhau" required>
                </div>
                <div class="mb-3">
                    <label for="soDienThoai" class="form-label">Số điện thoại:</label>
                    <input type="tel" class="form-control" id="soDienThoai" name="soDienThoai">
                </div>
                <div class="mb-3">
                    <label for="diaChi" class="form-label">Địa chỉ:</label>
                    <textarea class="form-control" id="diaChi" name="diaChi" rows="3"></textarea>
                </div>
                <button type="submit" class="btn btn-success w-100">Đăng ký</button>
            </form>
            <div class="mt-3 text-center">
                <p>Đã có tài khoản? <a href="<%= request.getContextPath() %>/Login.jsp">Đăng nhập</a></p> <%-- Đã thêm context path --%>
            </div>
        </div>
    </div>
</body>
</html>