<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="Data.SanPham,Data.DanhMuc,Model.Model_SanPham,Model.Model_DanhMuc,java.util.List,java.sql.SQLException" %>

<%
    String ctx          = request.getContextPath();
    String addToCartUrl = ctx + "/AddToCartServlet";
    String buyNowUrl    = ctx + "/ThanhToanServlet?action=addToCartAndCheckout";

    SanPham  spDAO  = new SanPham();
    DanhMuc  dmDAO  = new DanhMuc();
    List<Model_SanPham>   listSP = null;
    List<Model_DanhMuc>   listDM = null;
    try {
        listSP = spDAO.getAllSanPhams();
        listDM = dmDAO.getAllDanhMuc();
    } catch (SQLException ex) { ex.printStackTrace(); }
%>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>FoodSach.com - Sản phẩm</title>
    <meta name="viewport" content="width=device-width,initial-scale=1">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="<%= ctx %>/css/style.css">
    <style>
        .product-card img { height: 200px; object-fit: cover; }
    </style>
</head>
<body>
<jsp:include page="/Custom_index.jsp"/>

<div class="container my-4">
    <div class="row">
        <div class="col-md-3">
            <div class="category-box p-3 shadow-sm">
                <h5 class="text-success">Danh mục sản phẩm</h5>
                <ul class="list-unstyled category-list">
                    <li><button class="btn w-100 text-start filter-button active" data-category-id="all">Tất cả</button></li>
                    <% if (listDM != null) for (var dm : listDM) { %>
                    <li><button class="btn w-100 text-start filter-button" data-category-id="<%= dm.getMaDanhMuc() %>"><%= dm.getTenDanhMuc() %></button></li>
                    <% } %>
                </ul>
            </div>
        </div>

        <div class="col-md-9">
            <img src="<%= ctx %>/images/tải xuống (1).jfif" class="w-100 rounded shadow-sm" style="height:400px;object-fit:cover">
        </div>
    </div>
</div>

<div class="container pb-5">
    <h2 class="text-center my-5">SẢN PHẨM CỦA CHÚNG TÔI</h2>
    <div class="row row-cols-1 row-cols-sm-2 row-cols-md-3 row-cols-lg-4 g-4" id="food-list-container">
        <% if (listSP != null) for (var sp : listSP) {
            String img = (sp.getHinhAnh() == null || sp.getHinhAnh().isEmpty()) ? "default.jpg" : sp.getHinhAnh().replace("\\", "/");
        %>
        <div class="col food-item" data-category-id="<%= sp.getMaDanhMuc() %>">
            <div class="card product-card">
                <img src="<%= ctx %>/images/<%= img %>" class="card-img-top" alt="<%= sp.getTenSanPham() %>">
                <div class="card-body text-center">
                    <h5><%= sp.getTenSanPham() %></h5>
                    <p class="fw-bold text-danger"><%= String.format("%,.0f", sp.getGia()) %> đ/Kg</p>

                    <div class="d-flex flex-column gap-2">
                        <!-- Thêm vào giỏ -->
                        <form class="add-to-cart-form">
                            <input type="hidden" name="maSanPham" value="<%= sp.getMaSanPham() %>">
                            <input type="hidden" name="soLuong" value="1">
                            <button type="submit" class="btn btn-primary btn-add-cart">🛒 Thêm vào giỏ</button>
                        </form>

                        <!-- Mua ngay -->
                        <form action="<%= buyNowUrl %>" method="post">
                            <input type="hidden" name="maSanPham" value="<%= sp.getMaSanPham() %>">
                            <input type="hidden" name="soLuong" value="1">
                            <button type="submit" class="btn btn-buy-now w-100">Mua ngay</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
        <% } %>
    </div>
</div>

<jsp:include page="/footer.jsp"/>

<script>
document.addEventListener('DOMContentLoaded', () => {
    const ctx = '<%= request.getContextPath() %>';
    document.querySelectorAll('.add-to-cart-form').forEach(form => {
        form.addEventListener('submit', function(event) {
            event.preventDefault();
            fetch(ctx + '/AddToCartServlet', {
                method: 'POST',
                body: new FormData(form)
            })
            .then(response => response.text())
            .then(txt => {
                const [status, payload] = txt.split('|');
                if (status === 'ok') {
                    const countElement = document.getElementById('cart-count');
                    if (countElement) countElement.innerText = payload;

                    const btn = form.querySelector('.btn-add-cart');
                    if (btn) {
                        btn.innerText = '✔ Đã thêm';
                        setTimeout(() => btn.innerText = '🛒 Thêm vào giỏ', 1500);
                    }
                } else if (status === 'login-required') {
                    window.location.href = ctx + '/Login.jsp';
                } else {
                    alert(payload || 'Có lỗi xảy ra!');
                }
            })
            .catch(error => {
                console.error('❌ Fetch error:', error);
                alert('Không kết nối được máy chủ!');
            });
        });
    });
});
</script>
</body>
</html>