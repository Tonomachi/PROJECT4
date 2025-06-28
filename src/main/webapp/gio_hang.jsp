<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*, java.text.NumberFormat, java.util.Locale" %>
<%@ page import="Model.Model_GioHang, Model.Model_SanPham, Model.Model_NguoiDung, Data.GioHang" %>

<%
    Model_NguoiDung user = (Model_NguoiDung) session.getAttribute("loggedInUser");
    List<Model_GioHang> gioHangList = null;
    long tongTien = 0;
    int tongSoLuong = 0;
    NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

    if (user != null) {
        try (GioHang dao = new GioHang()) {
            gioHangList = dao.getGioHangByMaNguoiDung(user.getMaNguoiDung());
            tongSoLuong = dao.getTongSoSanPham(user.getMaNguoiDung());
            session.setAttribute("cartCount", tongSoLuong); // cập nhật cho header hiển thị đúng
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Giỏ hàng</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/giohang.css">
</head>
<body class="container mt-5">
<jsp:include page="/Custom_index.jsp" />
<h2 class="mb-4">🛒 Giỏ hàng</h2>

<%-- Hiển thị thông báo nếu có --%>
<%
    String msg = (String) session.getAttribute("cartMessage");
    String msgType = (String) session.getAttribute("cartMessageType");
    if (msg != null && msgType != null) {
%>
    <div class="alert alert-<%= msgType %> alert-dismissible fade show" role="alert">
        <%= msg %>
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
    </div>
<%
        session.removeAttribute("cartMessage");
        session.removeAttribute("cartMessageType");
    }
%>

<%
    if (gioHangList == null || gioHangList.isEmpty()) {
%>
    <h5><strong>Giỏ hàng của bạn đang trống.</strong></h5>
<%
    } else {
%>
<table class="table table-bordered table-hover">
    <thead class="table-light">
        <tr>
            <th>Sản phẩm</th>
            <th>Hình ảnh</th>
            <th>Giá</th>
            <th>Số lượng</th>
            <th>Thành tiền</th>
            <th>Hành động</th>
        </tr>
    </thead>
    <tbody>
    <%
        for (Model_GioHang item : gioHangList) {
            Model_SanPham sp = item.getSanPham();
            long gia = sp.getGia().longValue();
            int soLuong = item.getSoLuong();
            int tonKho = sp.getSoLuongTonKho();
            long thanhTien = gia * soLuong;
            tongTien += thanhTien;
    %>
        <tr>
            <td><%= sp.getTenSanPham() %></td>
            <td><img src="uploads/<%= sp.getHinhAnh() %>" width="80"></td>
            <td><%= currencyFormat.format(gia) %></td>

            <td>
			<form action="UpdateQuantityServlet" method="post" class="d-flex update-qty-form">
			    <input type="hidden" name="maSanPham" value="<%= sp.getMaSanPham() %>">
			    <input
			        type="number"
			        name="soLuongMoi"
			        value="<%= soLuong %>"
			        min="1"
			        max="<%= tonKho %>"
			        class="form-control form-control-sm me-1"
			        style="width:90px">
			    <button class="btn btn-primary btn-sm">Cập nhật</button>
			</form>
            </td>

            <td><%= currencyFormat.format(thanhTien) %></td>
            <td>
                <form action="RemoveFromCartServlet" method="post"
                      onsubmit="return confirm('Bạn chắc chắn muốn xóa sản phẩm này?');">
                    <input type="hidden" name="maSanPham" value="<%= sp.getMaSanPham() %>">
                    <button type="submit" class="btn btn-danger btn-sm">Xóa</button>
                </form>
            </td>
        </tr>

        <% if (soLuong > tonKho) { %>
            <tr>
                <td colspan="6" class="text-danger fw-bold">
                    ⚠️ Sản phẩm "<%= sp.getTenSanPham() %>" vượt quá số lượng tồn kho!
                </td>
            </tr>
        <% } %>
    <%
        }
    %>
    </tbody>
</table>

<div class="d-flex justify-content-between align-items-center">
    <h5>Tổng cộng: <strong><%= currencyFormat.format(tongTien) %></strong></h5>
    <form action="<%= request.getContextPath() %>/thanh_toan" method="get">
        <button type="submit" class="btn btn-success">Thanh toán</button>
    </form>
</div>

<a href="Index.jsp" class="btn btn-link mt-3">← Quay lại</a>
<%
    }
%>

<jsp:include page="/footer.jsp" />
<script>
document.addEventListener("DOMContentLoaded", () => {
    const ctx = "<%= request.getContextPath() %>";

    document.querySelectorAll(".update-qty-form").forEach(form => {
        form.addEventListener("submit", e => {
            e.preventDefault();

            const pid = form.elements["maSanPham"].value;
            const qty = form.elements["soLuongMoi"].value;

            const data = new URLSearchParams({
                maSanPham: pid,
                soLuongMoi: qty
            });

            fetch(ctx + "/UpdateQuantityServlet", {
                method: "POST",
                headers: {
                    "Content-Type": "application/x-www-form-urlencoded"
                },
                body: data.toString()
            })
            .then(res => res.text())
            .then(text => {
                if (text.startsWith("ok")) {
                    location.reload();
                } else if (text.startsWith("login-required")) {
                    window.location.href = ctx + "/Login.jsp";
                } else {
                    const msg = text.split("|")[1] || "Cập nhật thất bại.";
                    alert("❌ " + msg);
                }
            })
            .catch(err => {
                console.error("Fetch error:", err);
                alert("❌ Không thể kết nối máy chủ.");
            });
        });
    });
});
</script>
</body>
</html>
