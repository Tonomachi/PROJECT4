<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="Model.Model_NguoiDung" %>
<%@ page import="java.net.URLEncoder" %>
<%@ page import="java.nio.charset.StandardCharsets" %>

<%
    Model_NguoiDung loggedInUser = (Model_NguoiDung) session.getAttribute("loggedInUser");
    String userName = (loggedInUser != null) ? loggedInUser.getHoTen() : null;
    String userRole = (loggedInUser != null && loggedInUser.getVaiTro() != null) ? loggedInUser.getVaiTro().trim() : null;
%>

<nav class="navbar navbar-expand-lg navbar-light bg-light shadow-sm">
    <div class="container">
        <a class="navbar-brand text-success fw-bold fs-4" href="<%= request.getContextPath() %>/index.jsp">FoodSach.com</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                <%-- Link TRANG CHỦ, trỏ đến ProductPageServlet --%>
                <li class="nav-item">
                    <a class="nav-link" href="<%= request.getContextPath() %>/danh_sach_san_pham.jsp">TRANG CHỦ</a>
                </li>
                <%-- Các nút quản lý riêng biệt (chỉ hiển thị cho Admin) --%>
                <% if ("admin".equalsIgnoreCase(userRole)) { %>
                    <li class="nav-item">
                        <a class="nav-link" href="<%= request.getContextPath() %>/UserManagementServlet">Quản lý người dùng</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="<%= request.getContextPath() %>/LoginHistoryServlet">Quản lý lịch sử đăng nhập</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="<%= request.getContextPath() %>/SanPhamServlet">Quản lý sản phẩm</a>
                    </li>
                    <%-- Thêm các link quản lý khác nếu có --%>
                    <li class="nav-item">
                        <a class="nav-link" href="<%= request.getContextPath() %>/QuanLyDonHangServlet">Quản lý đơn hàng</a>
                    </li>
                <% } %>
            </ul>
            <form class="d-flex me-2">
                <input class="form-control me-2" type="search" placeholder="Nhập từ khóa tìm kiếm" aria-label="Search">
                <button class="btn btn-outline-success" type="submit"><i class="fas fa-search"></i></button>
            </form>
            <ul class="navbar-nav">
             

                <% if (loggedInUser != null) { %>
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" id="navbarUserDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                            <i class="fas fa-user-circle"></i> Xin chào, <%= userName %>
                        </a>
                        <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="navbarUserDropdown">
                            <li><a class="dropdown-item" href="<%= request.getContextPath() %>/thong_tin_tai_khoan.jsp">Thông tin tài khoản</a></li>
                            <li><a class="dropdown-item" href="<%= request.getContextPath() %>/lich_su_don_hang.jsp">Lịch sử đơn hàng</a></li>
                            <li><hr class="dropdown-divider"></li>
                            <li><a class="dropdown-item text-danger" href="<%= request.getContextPath() %>/LoginServlet?action=logout">Đăng xuất</a></li>
                        </ul>
                    </li>
                <% } else { %>
                    <li class="nav-item">
                        <a class="btn btn-success me-2" href="<%= request.getContextPath() %>/Login.jsp">Đăng nhập</a>
                    </li>
                    <li class="nav-item">
                        <a class="btn btn-outline-success" href="<%= request.getContextPath() %>/register.jsp">Đăng ký</a>
                    </li>
                <% } %>
            </ul>
        </div>
    </div>
</nav>

<script>
    document.addEventListener('DOMContentLoaded', function() {
        // Lấy thẻ <a> của nút "TRANG CHỦ"
        const homeLink = document.querySelector('a[href="<%= request.getContextPath() %>/trang_san_pham"]');

        if (homeLink) {
            homeLink.addEventListener('click', function(event) {
                // Ngăn chặn hành vi mặc định của link nếu bạn muốn thêm logic JS trước
                // event.preventDefault();

                // Chuyển hướng trực tiếp đến trang_san_pham. Servlet sẽ xử lý.
                window.location.href = this.href;
            });
        }
    });
</script>