<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="Model.Model_SanPham" %>
<%@ page import="Model.Model_DanhMuc" %>
<%@ page import="java.util.List" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="Model.Model_NguoiDung" %>

<%
    // Kiểm tra quyền admin
    Model_NguoiDung loggedInUser = (Model_NguoiDung) session.getAttribute("loggedInUser");
    String userRole = (loggedInUser != null && loggedInUser.getVaiTro() != null) ? loggedInUser.getVaiTro().trim() : null;

    if (loggedInUser == null || !"admin".equalsIgnoreCase(userRole)) {
        response.sendRedirect(request.getContextPath() + "/Login.jsp?errorMessage=Bạn không có quyền truy cập trang này.");
        return;
    }

    // Lấy dữ liệu từ Request Attributes (do Servlet chuyển tiếp)
    Model_SanPham sanPham = (Model_SanPham) request.getAttribute("sanPham");
    List<Model_DanhMuc> danhMucList = (List<Model_DanhMuc>) request.getAttribute("danhMucList");
    String errorMessage = (String) request.getAttribute("errorMessage");

    // Xác định tiêu đề và nút submit
    String action = (sanPham != null && sanPham.getMaSanPham() != 0) ? "update" : "insert"; 
    String title = (action.equals("update")) ? "Cập nhật Sản Phẩm" : "Thêm Sản Phẩm Mới";
    String submitButtonText = (action.equals("update")) ? "Cập Nhật" : "Thêm Mới";
%>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><%= title %> - FoodSach.com</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>

    <jsp:include page="/Header.jsp" />

    <div class="container mt-4">
        <h1 class="text-center mb-4 text-dark"><%= title %></h1>

        <% if (errorMessage != null && !errorMessage.isEmpty()) { %>
            <div class="alert alert-danger text-center"><%= errorMessage %></div>
        <% } %>

        <div class="card shadow-sm border-0 mx-auto" style="max-width: 600px;">
            <div class="card-body">
                <form action="<%= request.getContextPath() %>/SanPhamServlet" method="post" enctype="multipart/form-data">
                    <input type="hidden" name="action" value="<%= action %>">
                    <% if (action.equals("update")) { %>
                        <input type="hidden" name="maSanPham" value="<%= sanPham.getMaSanPham() %>">
                        <input type="hidden" name="hinhAnhUrlCu" value="<%= sanPham.getHinhAnh() %>">
                    <% } %>

                    <div class="mb-3">
                        <label for="tenSanPham" class="form-label">Tên sản phẩm:</label>
                        <input type="text" class="form-control" id="tenSanPham" name="tenSanPham" value="<%= sanPham != null ? sanPham.getTenSanPham() : "" %>" required>
                    </div>
                    <div class="mb-3">
                        <label for="moTa" class="form-label">Mô tả:</label>
                        <textarea class="form-control" id="moTa" name="moTa" rows="3" required><%= sanPham != null ? sanPham.getMoTa() : "" %></textarea>
                    </div>
                    <div class="mb-3">
                        <label for="gia" class="form-label">Giá:</label>
                        <input type="number" step="0.01" class="form-control" id="gia" name="gia" value="<%= sanPham != null && sanPham.getGia() != null ? sanPham.getGia().toPlainString() : "" %>" required>
                    </div>

                    <div class="mb-3">
                        <label for="soLuong" class="form-label">Số lượng tồn kho:</label>
                        <input type="number" class="form-control" id="soLuong" name="soLuong" value="<%= sanPham != null ? sanPham.getSoLuongTonKho() : "" %>" required min="0">
                    </div>

                    <div class="mb-3">
                        <label for="maDanhMuc" class="form-label">Danh mục:</label>
                        <select class="form-select" id="maDanhMuc" name="maDanhMuc" required>
                            <option value="">Chọn danh mục</option>
                            <%
                                if (danhMucList != null) {
                                    for (Model_DanhMuc dm : danhMucList) {
                                        String selected = "";
                                        if (sanPham != null && sanPham.getMaDanhMuc() == dm.getMaDanhMuc()) {
                                            selected = "selected";
                                        }
                            %>
                                        <option value="<%= dm.getMaDanhMuc() %>" <%= selected %>><%= dm.getTenDanhMuc() %></option>
                            <%
                                    }
                                }
                            %>
                        </select>
                    </div>
                    <div class="mb-3">
                        <label for="hinhAnhFile" class="form-label">Tải ảnh lên:</label>
                        <input class="form-control" type="file" id="hinhAnhFile" name="hinhAnhFile">
                        <% if (sanPham != null && sanPham.getHinhAnh() != null && !sanPham.getHinhAnh().isEmpty()) { %>
                            <small class="form-text text-muted">Ảnh hiện tại:</small><br>
                            <img src="<%= request.getContextPath() %>/<%= sanPham.getHinhAnh() %>" alt="Ảnh sản phẩm hiện tại" class="img-thumbnail mt-2" style="max-width: 150px;">
                        <% } %>
                    </div>

                    <div class="d-grid gap-2">
                        <button type="submit" class="btn btn-success"><i class="fas fa-save"></i> <%= submitButtonText %></button>
                        <a href="<%= request.getContextPath() %>/SanPhamServlet?action=list" class="btn btn-secondary"><i class="fas fa-arrow-left"></i> Quay lại danh sách</a>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <jsp:include page="/footer.jsp" />

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>