<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="Model.Model_NguoiDung" %>
<%
    
    Model_NguoiDung user = (session != null) ? (Model_NguoiDung) session.getAttribute("loggedInUser") : null;
    if (user == null) {
        response.sendRedirect(request.getContextPath() + "/Login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Đổi mật khẩu</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
</head>
<body>
<jsp:include page="/Custom_index.jsp"/>

<div class="container mt-5 mb-5" style="max-width: 600px;">
    <h3 class="text-center mb-4">Đổi mật khẩu</h3>

    <% if (request.getAttribute("message") != null) { %>
        <div class="alert alert-success text-center"><%= request.getAttribute("message") %></div>
    <% } %>
    <% if (request.getAttribute("error") != null) { %>
        <div class="alert alert-danger text-center"><%= request.getAttribute("error") %></div>
    <% } %>

    <form method="post" action="<%= request.getContextPath() %>/doi-mat-khau">
        <div class="mb-3">
            <label class="form-label">Mật khẩu hiện tại</label>
            <input type="password" class="form-control" name="matKhauCu" required>
        </div>
        <div class="mb-3">
            <label class="form-label">Mật khẩu mới</label>
            <input type="password" class="form-control" name="matKhauMoi" required>
        </div>
        <div class="mb-3">
            <label class="form-label">Xác nhận mật khẩu mới</label>
            <input type="password" class="form-control" name="xacNhanMatKhau" required>
        </div>
        <div class="text-end">
            <button type="submit" class="btn btn-primary">Cập nhật</button>
            <a href="<%= request.getContextPath() %>/thong_tin_nguoi_dung.jsp" class="btn btn-secondary">Quay lại</a>
        </div>
    </form>
</div>

<jsp:include page="/footer.jsp"/>
</body>
</html>
