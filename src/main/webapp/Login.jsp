<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Đăng nhập</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/CSS/login.css">
    
</head>
<body>
    <div class="container mt-5">
        <div class="card p-4 mx-auto" style="max-width: 400px;">
            <h2 class="card-title text-center mb-4">Đăng nhập</h2>
            <%
                String errorMessage = (String) request.getAttribute("errorMessage");
                if (errorMessage == null) errorMessage = request.getParameter("errorMessage");
                if (errorMessage != null && !errorMessage.isEmpty()) {
            %>
                <div class="alert alert-danger"><%= errorMessage %></div>
            <% } %>
            <form action="<%= request.getContextPath() %>/LoginServlet" method="post">
                <div class="mb-3">
                    <label for="email" class="form-label">Email đăng nhập:</label>
                    <input type="email" class="form-control" id="email" name="email" required>
                </div>
                <div class="mb-3">
                    <label for="password" class="form-label">Mật khẩu:</label>
                    <input type="password" class="form-control" id="password" name="password" required>
                </div>
                <button type="submit" class="btn btn-primary w-100">Đăng nhập</button>
            </form>
            <div class="mt-3 text-center">
                <p>Chưa có tài khoản? <a href="<%= request.getContextPath() %>/register.jsp">Đăng ký ngay</a></p>
            </div>
        </div>
    </div>
</body>
</html>
