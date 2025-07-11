<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="Data.SanPham,Data.DanhMuc,Model.Model_SanPham,Model.Model_DanhMuc,java.util.List,java.sql.SQLException" %>

<%
    String ctx = request.getContextPath();
    String addToCartUrl = ctx + "/AddToCartServlet";
    String buyNowUrl = ctx + "/ThanhToanServlet?action=addToCartAndCheckout";

    SanPham spDAO = new SanPham();
    DanhMuc dmDAO = new DanhMuc();
    List<Model_SanPham> listSP = null;
    List<Model_DanhMuc> listDM = null;
    try {
        listSP = spDAO.getAllSanPhams();
        listDM = dmDAO.getAllDanhMuc();
    } catch (SQLException ex) {
        ex.printStackTrace();
    }
%>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>FoodSach.com - Sản phẩm</title>
    <meta name="viewport" content="width=device-width,initial-scale=1">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="<%= ctx %>/css/style.css">
    <script src="https://kit.fontawesome.com/a076d05399.js" crossorigin="anonymous"></script>
    <style>
        .product-card img { height: 200px; object-fit: cover; }
        /* Thêm CSS cho thông báo tạm thời */
        .btn-add-cart.added {
            background-color: #28a745; /* Màu xanh lá cây */
            border-color: #28a745;
        }
        .btn-add-cart.adding {
            opacity: 0.7;
            cursor: not-allowed;
        }
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
     <div class="row justify-content-center mb-4">
        <div class="col-md-6">
            <div class="input-group">
                <span class="input-group-text"><i class="fas fa-search"></i></span>
                <input id="searchBox" type="text" class="form-control" placeholder="Tìm kiếm theo tên sản phẩm...">
            </div>
        </div>
    </div>
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
                        <form class="add-to-cart-form">
                            <input type="hidden" name="maSanPham" value="<%= sp.getMaSanPham() %>">
                            <input type="hidden" name="soLuong" value="1">
                            <button type="submit" class="btn btn-primary btn-add-cart">🛒 Thêm vào giỏ</button>
                        </form>

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
    const cartCountElement = document.getElementById('cart-count'); // Lấy phần tử hiển thị số lượng giỏ hàng

    // Hàm cập nhật số lượng giỏ hàng ban đầu khi tải trang
    // Bạn cần có một Servlet hoặc API endpoint để lấy số lượng giỏ hàng hiện tại
    // Ví dụ:
    function updateInitialCartCount() {
        fetch(ctx + '/GetCartCountServlet') // Tạo một Servlet mới để trả về số lượng giỏ hàng
            .then(response => response.text())
            .then(count => {
                if (cartCountElement) {
                    cartCountElement.innerText = count;
                }
            })
            .catch(error => console.error('Error fetching initial cart count:', error));
    }
    // Gọi hàm này khi trang được tải
    updateInitialCartCount();


    // Xử lý thêm vào giỏ hàng
    document.querySelectorAll('.add-to-cart-form').forEach(form => {
        form.addEventListener('submit', function(event) {
            event.preventDefault();

            const btn = form.querySelector('.btn-add-cart');
            const originalBtnText = btn.innerText;

            // Vô hiệu hóa nút và đổi text khi đang xử lý
            btn.disabled = true;
            btn.classList.add('adding');
            btn.innerText = 'Đang thêm...';

            fetch(ctx + '/AddToCartServlet', {
                method: 'POST',
                body: new FormData(form)
            })
            .then(response => {
                if (!response.ok) { // Kiểm tra nếu phản hồi không thành công (ví dụ: 4xx, 5xx)
                    throw new Error('Network response was not ok.');
                }
                return response.text();
            })
            .then(txt => {
                const [status, payload] = txt.split('|');
                if (status === 'ok') {
                    if (cartCountElement) {
                        cartCountElement.innerText = payload; // Cập nhật số lượng giỏ hàng
                    }

                    // Hiệu ứng "Đã thêm"
                    btn.classList.remove('adding');
                    btn.classList.add('added');
                    btn.innerText = '✔ Đã thêm';
                    
                    setTimeout(() => {
                        btn.innerText = originalBtnText; // Quay lại text ban đầu
                        btn.classList.remove('added');
                        btn.disabled = false; // Kích hoạt lại nút
                    }, 1500); // Hiển thị "Đã thêm" trong 1.5 giây
                } else if (status === 'login-required') {
                    alert('Vui lòng đăng nhập để thêm sản phẩm vào giỏ hàng.');
                    window.location.href = ctx + '/Login.jsp';
                } else {
                    alert(payload || 'Có lỗi xảy ra khi thêm vào giỏ hàng!');
                    btn.innerText = originalBtnText;
                    btn.disabled = false;
                    btn.classList.remove('adding');
                }
            })
            .catch(error => {
                console.error('❌ Fetch error:', error);
                alert('Không thể kết nối đến máy chủ hoặc có lỗi xảy ra: ' + error.message);
                btn.innerText = originalBtnText;
                btn.disabled = false;
                btn.classList.remove('adding');
            });
        });
    });

    // Lọc theo danh mục + tìm kiếm (giữ nguyên)
    const buttons = document.querySelectorAll('.filter-button');
    const items = document.querySelectorAll('.food-item');
    const searchBox = document.getElementById('searchBox');

    function filterItems() {
        const selectedCategory = document.querySelector('.filter-button.active')?.dataset.categoryId || 'all';
        const keyword = searchBox.value.toLowerCase();

        items.forEach(item => {
            const itemCategory = item.dataset.categoryId;
            const itemName = item.querySelector('h5').textContent.toLowerCase();

            const matchCategory = (selectedCategory === 'all') || (itemCategory === selectedCategory);
            const matchSearch = itemName.includes(keyword);

            item.style.display = (matchCategory && matchSearch) ? 'block' : 'none';
        });
    }

    buttons.forEach(btn => {
        btn.addEventListener('click', () => {
            buttons.forEach(b => b.classList.remove('active'));
            btn.classList.add('active');
            filterItems();
        });
    });

    searchBox.addEventListener('input', () => {
        filterItems();
    });
});
</script>

</body>
</html>