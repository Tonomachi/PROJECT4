<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="Model.Model_NguoiDung" %>

<%
    Model_NguoiDung loggedInUser = (Model_NguoiDung) session.getAttribute("loggedInUser");
    String userName = (loggedInUser != null) ? loggedInUser.getHoTen() : null;
    String userRole = (loggedInUser != null && loggedInUser.getVaiTro() != null) ? loggedInUser.getVaiTro().trim() : null;
%>

<!-- Phần head chứa Bootstrap + CSS riêng -->
<head>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/header.css">
    <script src="https://kit.fontawesome.com/a076d05399.js" crossorigin="anonymous"></script> <!-- icon người dùng -->
</head>

<!-- Navbar -->
<nav class="navbar navbar-expand-lg navbar-light bg-light shadow-sm">
    <div class="container">
        <!-- Logo -->
        <a class="navbar-brand text-success fw-bold fs-4" href="<%= request.getContextPath() %>/index.jsp">FoodSach.com</a>

        <!-- Nút toggle khi màn nhỏ -->
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse"
                data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false"
                aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>

        <!-- Nội dung menu -->
        <div class="collapse navbar-collapse" id="navbarNav">
            <!-- Menu trái -->
            <ul class="navbar-nav w-100 d-flex align-items-center justify-content-evenly">
                <% if ("admin".equalsIgnoreCase(userRole)) { %>
                    <li class="nav-item">
                        <a class="nav-link" href="<%= request.getContextPath() %>/UserManagementServlet">Quản lý người dùng</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="<%= request.getContextPath() %>/admin/lich-su-dang-nhap">Lịch sử đăng nhập</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="<%= request.getContextPath() %>/SanPhamServlet">Quản lý sản phẩm</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="<%= request.getContextPath() %>/QuanLyDonHangServlet">Quản lý đơn hàng</a>
                    </li>
                <% } %>

                <!-- User dropdown nằm bên phải -->
                <li class="nav-item dropdown ms-auto">
                    <% if (loggedInUser != null) { %>
                        <a class="nav-link dropdown-toggle" href="#" id="navbarUserDropdown"
                           role="button" data-bs-toggle="dropdown" aria-expanded="false">
                            <i class="fas fa-user-circle"></i> Xin chào, <%= userName %>
                        </a>
                        <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="navbarUserDropdown">
                            <li><a class="dropdown-item text-danger" href="<%= request.getContextPath() %>/LoginServlet?action=logout">Đăng xuất</a></li>
                        </ul>
                    <% } else { %>
                        <a class="btn btn-success me-2" href="<%= request.getContextPath() %>/Login.jsp">Đăng nhập</a>
                        <a class="btn btn-outline-success" href="<%= request.getContextPath() %>/register.jsp">Đăng ký</a>
                    <% } %>
                </li>
            </ul>
        </div>
    </div>
</nav>
