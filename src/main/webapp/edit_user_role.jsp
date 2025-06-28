<%-- File: webapp/edit_user_role.jsp --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="Model.Model_NguoiDung" %>
<%
    Model_NguoiDung userToEdit = (Model_NguoiDung) request.getAttribute("userToEdit");
    String errorMessage = (String) request.getAttribute("errorMessage");
    String successMessage = (String) request.getAttribute("successMessage");

    Model_NguoiDung loggedInUser = (Model_NguoiDung) session.getAttribute("loggedInUser");
    if (loggedInUser == null || !"admin".equals(loggedInUser.getVaiTro())) {
        response.sendRedirect(request.getContextPath() + "/login.jsp?errorMessage=Bạn không có quyền truy cập trang này."); // Đã thêm context path
        return;
    }

    if (userToEdit == null) {
        response.sendRedirect(request.getContextPath() + "/danh_sach_nguoi_dung.jsp?errorMessage=Không tìm thấy người dùng."); // Đã sửa tên file và thêm context path
        return;
    }
%>
<html>
<head>
    <title>Chỉnh sửa vai trò người dùng</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <style>
        body {
            background-color: #f8f9fa;
        }
        .card {
            margin-top: 50px;
            box-shadow: 0 4px 8px rgba(0,0,0,0.1);
        }
    </style>
</head>
<body>
<div class="container mt-5">
    <div class="card p-4 mx-auto" style="max-width: 500px;">
        <h2 class="card-title text-center mb-4">Chỉnh sửa vai trò người dùng</h2>
        <%
            if (errorMessage != null && !errorMessage.isEmpty()) {
        %>
            <div class="alert alert-danger" role="alert">
                <%= errorMessage %>
            </div>
        <%
            } else if (successMessage != null && !successMessage.isEmpty()) {
        %>
            <div class="alert alert-success" role="alert">
                <%= successMessage %>
            </div>
        <%
            }
        %>
        <form action="<%= request.getContextPath() %>/UserManagementServlet" method="post"> <%-- Đã thêm context path --%>
            <input type="hidden" name="action" value="updateRole">
            <input type="hidden" name="maNguoiDung" value="<%= userToEdit.getMaNguoiDung() %>">

            <div class="mb-3">
                <label for="hoTen" class="form-label">Họ Tên:</label>
                <input type="text" class="form-control" id="hoTen" value="<%= userToEdit.getHoTen() %>" readonly>
            </div>
            <div class="mb-3">
                <label for="email" class="form-label">Email:</label>
                <input type="email" class="form-control" id="email" value="<%= userToEdit.getEmail() %>" readonly>
            </div>
            <div class="mb-3">
                <label for="vaiTro" class="form-label">Vai trò hiện tại:</label>
                <input type="text" class="form-control" id="vaiTro" value="<%= userToEdit.getVaiTro() %>" readonly>
            </div>
            <div class="mb-3">
                <label for="newVaiTro" class="form-label">Thay đổi vai trò thành:</label>
                <select class="form-select" id="newVaiTro" name="newVaiTro" required>
                    <option value="admin" <%= "admin".equals(userToEdit.getVaiTro()) ? "selected" : "" %>>Admin</option>
                    <option value="KhachHang" <%= "KhachHang".equals(userToEdit.getVaiTro()) ? "selected" : "" %>>Khách hàng</option>
                </select>
            </div>
            <button type="submit" class="btn btn-primary w-100">Cập nhật vai trò</button>
            <a href="<%= request.getContextPath() %>/danh_sach_nguoi_dung.jsp" class="btn btn-secondary w-100 mt-2">Hủy</a> <%-- Đã thêm context path --%>
        </form>
    </div>
</div>
</body>
</html>