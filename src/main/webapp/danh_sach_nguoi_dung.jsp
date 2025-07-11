<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="Model.Model_NguoiDung" %>
<%@ page import="Data.NguoiDung" %>
<%@ page import="java.util.List" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="java.net.URLEncoder,java.net.URLDecoder" %>
<%@ page import="java.nio.charset.StandardCharsets" %>

<%
    /* -------------------- Kiểm quyền admin -------------------- */
    Model_NguoiDung loggedInUser = (Model_NguoiDung) session.getAttribute("loggedInUser");
    String userRole = (loggedInUser != null && loggedInUser.getVaiTro() != null) ? loggedInUser.getVaiTro().trim() : null;
    if (loggedInUser == null || !"admin".equalsIgnoreCase(userRole)) {
        String encoded = URLEncoder.encode("Bạn không có quyền truy cập!", StandardCharsets.UTF_8);
        response.sendRedirect(request.getContextPath() + "/Login.jsp?errorMessage=" + encoded);
        return;
    }

    /* -------------------- Lấy danh sách -------------------- */
    List<Model_NguoiDung> nguoiDungList = null;
    try {
        nguoiDungList = new Data.NguoiDung().getAllUsers();
    } catch (SQLException e) {
        request.setAttribute("errorMessage", "Lỗi tải danh sách: " + e.getMessage());
    }

    String err  = (String) request.getAttribute("errorMessage");
    String succ = request.getParameter("message");
    if (succ != null) succ = URLDecoder.decode(succ, StandardCharsets.UTF_8);
%>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Danh sách người dùng</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<jsp:include page="/Header.jsp" />

<div class="container mt-4">

    <h1 class="text-center mb-3">Danh sách người dùng</h1>

    <!-- Thông báo -->
    <% if (err != null) { %>
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
            <%= err %>
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    <% } %>
    <% if (succ != null) { %>
        <div class="alert alert-success alert-dismissible fade show" role="alert">
            <%= succ %>
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    <% } %>

    <!-- 🔍 Ô tìm kiếm -->
    <div class="input-group mb-3">
        <span class="input-group-text"><i class="fas fa-search"></i></span>
        <input id="searchBox" type="text" class="form-control" placeholder="Tìm theo họ tên...">
    </div>

    <!-- Bảng người dùng -->
    <table id="userTable" class="table table-bordered table-striped align-middle">
        <thead class="table-light">
            <tr>
                <th>Mã</th>
                <th>Họ tên</th>
                <th>Email</th>
                <th>SĐT</th>
                <th>Địa chỉ</th>
                <th>Vai trò</th>
                <th>Ngày tạo</th>
                <th>Hành động</th>
            </tr>
        </thead>
        <tbody>
        <% if (nguoiDungList != null && !nguoiDungList.isEmpty()) {
               for (Model_NguoiDung u : nguoiDungList) { %>
            <tr>
                <td><%= u.getMaNguoiDung() %></td>
                <td><%= u.getHoTen() %></td>
                <td><%= u.getEmail() %></td>
                <td><%= u.getSoDienThoai() == null ? "N/A" : u.getSoDienThoai() %></td>
                <td><%= u.getDiaChi() == null ? "N/A" : u.getDiaChi() %></td>
                <td><%= u.getVaiTro() %></td>
                <td><%= u.getNgayTao() %></td>
                <td>
                    <a class="btn btn-info btn-sm"
   href="<%= request.getContextPath() %>/UserManagementServlet?action=editUser&id=<%= u.getMaNguoiDung() %>">
   Sửa thông tin
</a>
                </td>
            </tr>
        <%   }
           } else { %>
            <tr><td colspan="8" class="text-center">Không có dữ liệu.</td></tr>
        <% } %>
        </tbody>
    </table>

    <a href="<%= request.getContextPath() %>/danh_sach_san_pham.jsp" class="btn btn-secondary">
        <i class="fas fa-arrow-left"></i> Trang chủ
    </a>
</div>

<jsp:include page="/footer.jsp"/>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/js/all.min.js"></script>

<script>
/* ========== Lọc bảng theo ô tìm kiếm ========== */
document.addEventListener('DOMContentLoaded', () => {
    const searchBox = document.getElementById('searchBox');
    const table     = document.getElementById('userTable');
    const colIndex  = 1;      // cột Họ tên (0 = mã, 1 = họ tên, ...)

    searchBox.addEventListener('keyup', () => {
        const filter = searchBox.value.toLowerCase().trim();
        Array.from(table.tBodies[0].rows).forEach(tr => {
            const cellText = tr.cells[colIndex].innerText.toLowerCase();
            tr.style.display = cellText.includes(filter) ? '' : 'none';
        });
    });
});
</script>
</body>
</html>
