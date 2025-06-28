<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="jakarta.servlet.http.HttpSession" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Đặt hàng thành công</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
</head>
<body>
<jsp:include page="/Custom_index.jsp" />

<div class="container mt-5 text-center">
    <h2 class="text-success mb-4">✅ ĐẶT HÀNG THÀNH CÔNG!</h2>
    <p class="lead">Cảm ơn bạn đã mua hàng tại <strong>FOOD CLEAN</strong>.</p>
    <p>Chúng tôi sẽ liên hệ với bạn để xác nhận đơn hàng và giao hàng trong thời gian sớm nhất.</p>
    <a href="Index.jsp" class="btn btn-primary mt-4">← Tiếp tục mua sắm</a>
</div>

<jsp:include page="/footer.jsp" />
</body>
</html>
