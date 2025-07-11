<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="Model.*" %>
<%@ page import="Data.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="java.net.URLDecoder" %>
<%@ page import="java.nio.charset.StandardCharsets" %>

<%
    Model_NguoiDung loggedInUser = (Model_NguoiDung) session.getAttribute("loggedInUser");
    String userRole = (loggedInUser != null && loggedInUser.getVaiTro() != null) ? loggedInUser.getVaiTro().trim() : null;

    if (loggedInUser == null || !"admin".equals(userRole)) {
        String encodedErrorMessage = request.getParameter("errorMessage");
        response.sendRedirect(request.getContextPath() + "/Login.jsp" + (encodedErrorMessage != null ? "?errorMessage=" + encodedErrorMessage : ""));
        return;
    }

    List<Model_SanPham> sanPhams = (List<Model_SanPham>) request.getAttribute("sanPhams");
    List<Model_DanhMuc> danhMucList = (List<Model_DanhMuc>) request.getAttribute("danhMucList");

    String errorMessage = (String) request.getAttribute("errorMessage");
    String successMessage = (String) request.getParameter("message");

    if (errorMessage == null) {
        String encodedErrorMessageParam = request.getParameter("errorMessage");
        if (encodedErrorMessageParam != null && !encodedErrorMessageParam.isEmpty()) {
            errorMessage = URLDecoder.decode(encodedErrorMessageParam, StandardCharsets.UTF_8.toString());
        }
    }
    if (successMessage != null && !successMessage.isEmpty()) {
        successMessage = URLDecoder.decode(successMessage, StandardCharsets.UTF_8.toString());
    }

    if (sanPhams == null || danhMucList == null) {
        SanPham sanPhamDAO = new SanPham();
        DanhMuc danhMucDAO = new DanhMuc();
        try {
            sanPhams = sanPhamDAO.getAllSanPhams();
            danhMucList = danhMucDAO.getAllDanhMuc();
        } catch (SQLException e) {
            e.printStackTrace();
            errorMessage = "Lỗi khi tải danh sách sản phẩm hoặc danh mục từ cơ sở dữ liệu: " + e.getMessage();
        }
    }
%>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản Lý Sản Phẩm - FoodSach.com</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
    <style>
        .category-filters .filter-button.active {
            background-color: #007bff;
            color: white;
            border-color: #007bff;
        }
    </style>
</head>
<body>

<jsp:include page="/Header.jsp" />

<div class="container mt-4">
    <h1 class="text-center mb-4 text-dark">Trang Quản Lý Sản Phẩm (ADMIN)</h1>
    <p class="text-center text-muted">Đây là trang hiển thị danh sách sản phẩm. Bạn có thể thực hiện thêm, sửa, xóa sản phẩm tại đây.</p>

    <%-- Alert thông báo --%>
    <% if (errorMessage != null) { %>
        <div id="errorMessageAlert" class="alert alert-danger text-center alert-dismissible fade show" role="alert">
            <%= errorMessage %>
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    <% } %>
    <% if (successMessage != null) { %>
        <div id="successMessageAlert" class="alert alert-success text-center alert-dismissible fade show" role="alert">
            <%= successMessage %>
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    <% } %>

    <div class="category-filters text-center mb-4">
        <button class="btn btn-outline-primary m-1 filter-button active" data-category-id="all">Tất cả</button>
        <% if (danhMucList != null) {
            for (Model_DanhMuc dm : danhMucList) { %>
                <button class="btn btn-outline-primary m-1 filter-button" data-category-id="<%= dm.getMaDanhMuc() %>"><%= dm.getTenDanhMuc() %></button>
        <% }} %>
    </div>

    <div class="d-flex justify-content-between mb-3">
        <a href="<%= request.getContextPath() %>/SanPhamServlet?action=new" class="btn btn-primary">
            <i class="fas fa-plus-circle"></i> Thêm sản phẩm mới
        </a>

        <!-- 🔍 Thanh tìm kiếm -->
        <div class="input-group w-50">
            <span class="input-group-text"><i class="fas fa-search"></i></span>
            <input id="searchBox" type="text" class="form-control" placeholder="Tìm theo tên sản phẩm...">
        </div>
    </div>

    <div class="row row-cols-1 row-cols-md-3 g-4 mb-5" id="food-list-container">
        <% if (sanPhams != null && !sanPhams.isEmpty()) {
            for (Model_SanPham sp : sanPhams) { %>
                <div class="col food-item" data-category-id="<%= sp.getMaDanhMuc() %>">
                    <div class="card h-100 shadow-sm border-0">
                        <%
                            String img = (sp.getHinhAnh() == null || sp.getHinhAnh().isEmpty()) ? "default.jpg" : sp.getHinhAnh().replace("\\", "/");
                            String finalImageUrl = request.getContextPath() + "/images/" + img;
                        %>
                        <img src="<%= finalImageUrl %>" class="card-img-top" alt="<%= sp.getTenSanPham() %>" style="height: 200px; object-fit: cover;">
                        <div class="card-body text-center">
                            <h5 class="card-title text-primary"><%= sp.getTenSanPham() %></h5>
                            <p class="card-text text-danger fw-bold fs-5"><%= String.format("%,.0f", sp.getGia()) %> đ/Kg</p>
                            <p class="card-text text-muted">Số lượng: <%= sp.getSoLuongTonKho() %></p>
                            <p class="card-text text-muted">Mô tả: <%= sp.getMoTa() != null && sp.getMoTa().length() > 50 ? sp.getMoTa().substring(0, 50) + "..." : (sp.getMoTa() != null ? sp.getMoTa() : "") %></p>

                            <hr>
                            <a href="<%= request.getContextPath() %>/SanPhamServlet?action=edit&id=<%= sp.getMaSanPham() %>" class="btn btn-warning btn-sm me-2">
                                <i class="fas fa-edit"></i> Sửa
                            </a>
                            <a href="<%= request.getContextPath() %>/SanPhamServlet?action=delete&id=<%= sp.getMaSanPham() %>" class="btn btn-danger btn-sm" onclick="return confirm('Bạn có chắc chắn muốn xóa sản phẩm này không?');">
                                <i class="fas fa-trash-alt"></i> Xóa
                            </a>
                        </div>
                    </div>
                </div>
        <%  }} else { %>
            <div class="col-12">
                <p class="text-center">Hiện chưa có sản phẩm nào để quản lý.</p>
            </div>
        <% } %>
    </div>
</div>

<jsp:include page="/footer.jsp" />

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script>
    document.addEventListener('DOMContentLoaded', function () {
        // Filter danh mục
        const filterButtons = document.querySelectorAll('.filter-button');
        const foodItems = document.querySelectorAll('.food-item');

        filterButtons.forEach(button => {
            button.addEventListener('click', function () {
                filterButtons.forEach(btn => btn.classList.remove('active'));
                this.classList.add('active');

                const selectedCategoryId = this.dataset.categoryId;

                foodItems.forEach(item => {
                    const itemCategoryId = item.dataset.categoryId;
                    item.style.display = (selectedCategoryId === 'all' || itemCategoryId === selectedCategoryId) ? 'block' : 'none';
                });
            });
        });

        // 🔍 Tìm kiếm theo tên sản phẩm
        const searchBox = document.getElementById('searchBox');
        searchBox.addEventListener('input', function () {
            const keyword = this.value.toLowerCase();
            foodItems.forEach(item => {
                const name = item.querySelector('.card-title').textContent.toLowerCase();
                item.style.display = name.includes(keyword) ? 'block' : 'none';
            });
        });

        // Ẩn alert sau 5 giây
        const errorMessageAlert = document.getElementById('errorMessageAlert');
        const successMessageAlert = document.getElementById('successMessageAlert');
        if (errorMessageAlert) setTimeout(() => errorMessageAlert.remove(), 5000);
        if (successMessageAlert) setTimeout(() => successMessageAlert.remove(), 5000);
    });
</script>
</body>
</html>
