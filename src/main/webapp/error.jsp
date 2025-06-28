<%-- File: webapp/error.jsp --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Lỗi</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
</head>
<body>
    <div class="container mt-5">
        <div class="alert alert-danger" role="alert">
            <h4 class="alert-heading">Đã xảy ra lỗi!</h4>
            <p>Xin lỗi, đã có một vấn đề xảy ra trong quá trình xử lý yêu cầu của bạn.</p>
            <%
                String errorMessage = (String) request.getAttribute("errorMessage");
                if (errorMessage != null && !errorMessage.isEmpty()) {
            %>
                <p><strong>Chi tiết lỗi:</strong> <%= errorMessage %></p>
            <%
                }
            %>
            <hr>
            <p class="mb-0">Vui lòng thử lại hoặc liên hệ với quản trị viên nếu vấn đề vẫn tiếp diễn.</p>
        </div>
        <a href="<%= request.getContextPath() %>/danh_sach_san_pham.jsp" class="btn btn-primary">Quay lại danh sách sản phẩm</a> <%-- Đã sửa tên file và thêm context path --%>
    </div>
</body>
</html>