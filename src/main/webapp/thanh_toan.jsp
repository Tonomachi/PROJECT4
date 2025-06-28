<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, java.math.BigDecimal" %>
<%@ page import="Model.Model_GioHang, Model.Model_SanPham, Model.Model_NguoiDung" %>

<%
    Model_NguoiDung loggedInUser = (Model_NguoiDung) session.getAttribute("loggedInUser");
	List<Model_GioHang> gioHangList = (List<Model_GioHang>) session.getAttribute("gioHangList");
	BigDecimal subtotal = (BigDecimal) session.getAttribute("subtotal");

    if (subtotal == null) subtotal = BigDecimal.ZERO;
    String errorMessage = (String) request.getAttribute("errorMessage");
    String selectedMethod = request.getParameter("hinhThucThanhToan") != null
            ? request.getParameter("hinhThucThanhToan") : "COD";
%>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Thanh toán - FoodSach.com</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css" rel="stylesheet">
</head>
<body>
<jsp:include page="/Custom_index.jsp"/>

<div class="container mt-5">
<% if (loggedInUser == null) { %>
    <div class="alert alert-warning text-center">
        Bạn cần <a href="<%= request.getContextPath() %>/Login.jsp">đăng nhập</a> để thanh toán.
    </div>
<% } else { %>

    <h2 class="mb-4 text-center">Tiến hành thanh toán</h2>

    <% if (errorMessage != null) { %>
        <div class="alert alert-danger text-center"><%= errorMessage %></div>
    <% } %>

    <form action="<%= request.getContextPath() %>/ThanhToanServlet" method="post">

        <!-- 1. Đơn hàng -->
        <h4 class="mb-3">1. Đơn hàng của bạn</h4>
        <table class="table table-bordered text-center align-middle">
            <thead class="table-light">
            <tr>
                <th>Sản phẩm</th><th>Giá/Kg</th><th>Số lượng</th><th>Thành tiền</th>
            </tr>
            </thead>
            <tbody>
            <% if (gioHangList != null && !gioHangList.isEmpty()) {
                 for (Model_GioHang item : gioHangList) {
                     Model_SanPham sp = item.getSanPham(); %>
                <tr>
                    <td class="text-start">
                        <img src="<%= request.getContextPath() %>/images/<%= sp.getHinhAnh() %>" width="50">
                        <span><%= sp.getTenSanPham() %></span>
                    </td>
                    <td><%= String.format("%,.0f", sp.getGia()) %> đ</td>
                    <td><%= item.getSoLuong() %></td>
                    <td><%= String.format("%,.0f",
                           sp.getGia().multiply(BigDecimal.valueOf(item.getSoLuong()))) %> đ</td>
                </tr>
            <%   }
               } else { %>
                <tr><td colspan="4" class="text-danger">Giỏ hàng của bạn đang trống.</td></tr>
            <% } %>
            </tbody>
        </table>

        <div class="text-end mb-4">
            <strong>Tổng tiền:&nbsp;</strong>
            <%= String.format("%,.0f", subtotal) %> đ
        </div>

        <!-- 2. Thông tin giao hàng -->
        <h4 class="mb-3">2. Thông tin giao hàng</h4>
        <div class="row g-3">
            <div class="col-md-6">
                <label>Họ tên</label>
                <input class="form-control" name="hoTen"
                       value="<%= loggedInUser.getHoTen() %>" required>
            </div>
            <div class="col-md-6">
                <label>Số điện thoại</label>
                <input class="form-control" name="soDienThoai"
                       value="<%= loggedInUser.getSoDienThoai() %>" required>
            </div>
            <div class="col-12">
                <label>Địa chỉ</label>
                <input class="form-control" name="diaChi"
                       value="<%= loggedInUser.getDiaChi() %>" required>
            </div>
            <div class="col-12">
                <label>Ghi chú (tùy chọn)</label>
                <textarea class="form-control" name="ghiChu"></textarea>
            </div>
        </div>

        <!-- 3. hình thức thanh toán -->
        <h4 class="mt-4 mb-2">3. Phương thức thanh toán</h4>
        <div class="form-check">
            <input class="form-check-input" type="radio"
                   name="hinhThucThanhToan" id="cod" value="COD"
                   <%= "COD".equals(selectedMethod) ? "checked" : "" %>>
            <label class="form-check-label" for="cod">
                Thanh toán khi nhận hàng (COD)
            </label>
        </div>

        <hr class="mt-4">
        <button class="btn btn-success btn-lg w-100"
                <%= (gioHangList == null || gioHangList.isEmpty()) ? "disabled" : "" %>>
            Hoàn tất đặt hàng
        </button>
    </form>
<% } %>
</div>

<jsp:include page="/footer.jsp"/>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
