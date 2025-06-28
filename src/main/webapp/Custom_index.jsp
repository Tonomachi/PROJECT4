<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="Data.GioHang" %>
<%@ page import="Model.Model_NguoiDung" %>

<%
    String ctx = request.getContextPath();
    String tenDangNhap = (String) session.getAttribute("tenDangNhap");
    Model_NguoiDung user = (Model_NguoiDung) session.getAttribute("loggedInUser");

    int cartCount = 0;
    if (user != null) {
        try (GioHang dao = new GioHang()) {
            cartCount = dao.getTongSoSanPham(user.getMaNguoiDung());
        } catch (Exception e) {
            e.printStackTrace();
        }
    } else {
        Integer s = (Integer) session.getAttribute("cartCount");
        cartCount = (s != null) ? s : 0;
    }
%>

<!-- Header -->
<header class="header bg-danger py-2 text-white">
  <div class="container d-flex flex-wrap justify-content-between align-items-center">
    
    <!-- Logo -->
    <div class="logo d-flex align-items-center">
      <img src="<%= ctx %>/images/logo.png" alt="Logo" height="35" class="me-2">
      <span class="fs-4 fw-bold">FOOD CLEAN</span>
    </div>

    <!-- Search box -->
    <form class="d-flex flex-grow-1 mx-4" role="search">
      <input class="form-control rounded-start-pill" type="search" placeholder="Bạn cần tìm gì?" aria-label="Search">
      <button class="btn btn-light rounded-end-pill ms-1" type="submit">
        <i class="fas fa-search"></i>
      </button>
    </form>

    <!-- Right section -->
    <div class="d-flex align-items-center">

      <!-- Hotline -->
      <div class="d-flex flex-column align-items-end me-4">
        <span class="fw-semibold">Gọi mua hàng</span>
        <span>1800.2097</span>
      </div>

      <!-- Tra cứu đơn hàng -->
      <div class="me-4">
        <a href="<%= ctx %>/donhang" class="text-white text-decoration-none">
          <i class="fas fa-truck"></i>
          <span class="ms-1">Tra cứu đơn hàng</span>
        </a>
      </div>

      <!-- Giỏ hàng -->
      <div class="me-4 position-relative">
        <a href="<%= ctx %>/gio_hang.jsp"
           class="text-white text-decoration-none d-flex align-items-center position-relative">
          <i class="fas fa-shopping-bag fa-lg"></i>
          <span class="ms-1">Giỏ hàng</span>
          <% if (cartCount > 0) { %>
          <span id="cart-count" class="cart-badge position-absolute top-0 start-100 translate-middle badge rounded-pill bg-warning text-dark">
			  <%= cartCount %>
			</span>

          <% } %>
        </a>
      </div>

      <!-- Đăng nhập / Đăng xuất -->
      <div>
        <% if (tenDangNhap == null) { %>
          <a href="<%= ctx %>/Login.jsp" class="btn btn-light text-danger fw-bold">
            <i class="fas fa-user-circle"></i> Đăng nhập
          </a>
        <% } else { %>
          <div class="dropdown">
            <button class="btn btn-light text-danger fw-bold dropdown-toggle" type="button" data-bs-toggle="dropdown">
              <i class="fas fa-user-circle"></i> Xin chào, <%= tenDangNhap %>
            </button>
            <ul class="dropdown-menu dropdown-menu-end">
              <li><a class="dropdown-item" href="<%= ctx %>/thong_tin_nguoi_dung.jsp">Thông tin tài khoản</a></li>
              <li><a class="dropdown-item" href="<%= ctx %>/dangxuat.jsp">Đăng xuất</a></li>
            </ul>
          </div>
        <% } %>
      </div>
    </div>
  </div>
</header>

<!-- CSS riêng -->
<link rel="stylesheet" href="<%= ctx %>/css/cssheader.css">

<!-- Bootstrap + Font Awesome -->
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
<link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css" rel="stylesheet">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
