<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*, Model.Model_GioHang, Model.Model_SanPham" %>

<%
    List<Model_GioHang> cartItems = (List<Model_GioHang>) request.getAttribute("cartItems");
    if (cartItems == null || cartItems.isEmpty()) {
%>
    <h3>Giỏ hàng của bạn đang trống.</h3>
<%
    } else {
%>
    <h3>Giỏ hàng của bạn:</h3>
    <table class="table table-bordered">
        <thead>
            <tr>
                <th>Hình ảnh</th>
                <th>Tên sản phẩm</th>
                <th>Giá</th>
                <th>Số lượng</th>
                <th>Thành tiền</th>
            </tr>
        </thead>
        <tbody>
        <%
            double tongTien = 0;
            for (Model_GioHang gh : cartItems) {
                Model_SanPham sp = gh.getSanPham();
                double thanhTien = sp.getGia().doubleValue() * gh.getSoLuong();
                tongTien += thanhTien;
        %>
            <tr>
                <td><img src="<%= sp.getHinhAnh() %>" width="80" height="80" alt=""></td>
                <td><%= sp.getTenSanPham() %></td>
                <td><%= sp.getGia() %> đ</td>
                <td><%= gh.getSoLuong() %></td>
                <td><%= thanhTien %> đ</td>
            </tr>
        <%
            }
        %>
            <tr>
                <td colspan="4" class="text-end"><strong>Tổng cộng:</strong></td>
                <td><strong><%= tongTien %> đ</strong></td>
            </tr>
        </tbody>
    </table>
<%
    }
%>
