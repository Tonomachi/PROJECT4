<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="Model.Model_NguoiDung"%>
<%@ page import="java.net.URLEncoder"%>
<%@ page import="java.nio.charset.StandardCharsets"%>

<%
    Model_NguoiDung u = (Model_NguoiDung) request.getAttribute("editUser");
    if (u == null) {
        // Nếu không có đối tượng người dùng, chuyển hướng về trang quản lý
        String msg = URLEncoder.encode("Không tìm thấy người dùng để chỉnh sửa!", StandardCharsets.UTF_8);
        response.sendRedirect(request.getContextPath() + "/UserManagementServlet?action=list&errorMessage=" + msg);
        return;
    }
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Chỉnh sửa thông tin người dùng - Admin</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<jsp:include page="/Header.jsp"/>

<div class="container mt-5" style="max-width:700px">
    <h3 class="text-center mb-4">Cập nhật thông tin người dùng (Admin)</h3>

    <% String err = (String) request.getAttribute("errorMessage");
       String msg = (String) request.getAttribute("message"); %>
    <% if (err != null) { %>
        <div class="alert alert-danger"><%= err %></div>
    <% } else if (msg != null) { %>
        <div class="alert alert-success"><%= msg %></div>
    <% } %>

    <form method="post" action="<%= request.getContextPath() %>/UserManagementServlet">
        <input type="hidden" name="action" value="updateUserInfo">
        <input type="hidden" name="maNguoiDung" value="<%= u.getMaNguoiDung() %>">
        <div class="mb-3">
            <label class="form-label">Họ tên</label>
            <input class="form-control" name="hoTen" value="<%= u.getHoTen() %>" required>
        </div>

        <div class="mb-3">
            <label class="form-label">Email</label>
            <input class="form-control" name="email" value="<%= u.getEmail() %>" type="email" required>
        </div>

     
        <div class="mb-3">
            <label class="form-label">Số điện thoại</label>
            <input class="form-control" name="soDienThoai" value="<%= u.getSoDienThoai() %>" required>
        </div>

        <div class="mb-3">
            <label class="form-label">Địa chỉ</label>
            <input class="form-control" name="diaChi" value="<%= u.getDiaChi() %>">
        </div>

        <div class="mb-3">
            <label class="form-label">Vai trò</label>
            <select class="form-select" name="vaiTro">
                <option value="user" <%= "user".equalsIgnoreCase(u.getVaiTro()) ? "selected" : "" %>>User</option>
                <option value="admin" <%= "admin".equalsIgnoreCase(u.getVaiTro()) ? "selected" : "" %>>Admin</option>
                </select>
        </div>

        <div class="d-flex justify-content-between">
            <a href="<%= request.getContextPath() %>/UserManagementServlet?action=list" class="btn btn-secondary">
                Quay lại
            </a>
            <button type="submit" class="btn btn-success">Lưu thay đổi</button>
        </div>
    </form>
</div>

<jsp:include page="/footer.jsp"/>
</body>
</html>