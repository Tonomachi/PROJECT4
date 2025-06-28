<%-- File: webapp/danh_sach_nguoi_dung.jsp --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="Model.Model_NguoiDung" %>
<%@ page import="Data.NguoiDung" %>
<%@ page import="java.util.List" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="java.net.URLEncoder" %>
<%@ page import="java.net.URLDecoder" %> <%-- THÊM DÒNG NÀY --%>
<%@ page import="java.nio.charset.StandardCharsets" %>

<%
    Model_NguoiDung loggedInUser = (Model_NguoiDung) session.getAttribute("loggedInUser");
    String userName = (loggedInUser != null) ? loggedInUser.getHoTen() : null;
    String userRole = (loggedInUser != null && loggedInUser.getVaiTro() != null) ? loggedInUser.getVaiTro().trim() : null;

    // Kiểm tra quyền truy cập: Chỉ admin mới được vào trang này
    if (loggedInUser == null || !"admin".equals(userRole)) {
        String encodedErrorMessage = URLEncoder.encode("Bạn không có quyền truy cập trang này. Vui lòng đăng nhập với tài khoản admin.", StandardCharsets.UTF_8.toString());
        response.sendRedirect(request.getContextPath() + "/Login.jsp?errorMessage=" + encodedErrorMessage);
        return;
    }

    List<Model_NguoiDung> nguoiDungList = null;
    Data.NguoiDung nguoiDungDAO = new Data.NguoiDung(); // Giả định bạn có DAO cho NguoiDung

    try {
        nguoiDungList = nguoiDungDAO.getAllUsers(); // Giả định phương thức là getAllUsers()
    } catch (SQLException e) {
        e.printStackTrace();
        // Xử lý lỗi nếu không lấy được danh sách người dùng
        request.setAttribute("errorMessage", "Lỗi khi tải danh sách người dùng: " + e.getMessage());
    }

    // Lấy thông báo lỗi/thành công từ request (nếu có)
    String errorMessage = (String) request.getAttribute("errorMessage");
    String successMessage = (String) request.getParameter("message"); // Lấy message từ URL

    if (successMessage != null && !successMessage.isEmpty()) {
        successMessage = URLDecoder.decode(successMessage, StandardCharsets.UTF_8.toString());
    }
    if (errorMessage == null) { // Nếu errorMessage chưa được set từ catch block
        String encodedErrorMessageParam = request.getParameter("errorMessage");
        if (encodedErrorMessageParam != null && !encodedErrorMessageParam.isEmpty()) {
            errorMessage = URLDecoder.decode(encodedErrorMessageParam, StandardCharsets.UTF_8.toString());
        }
    }
%>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Danh sách người dùng - FoodSach.com</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
    <style>
        /* Các style tùy chỉnh cho trang này nếu cần */
    </style>
</head>
<body>

    <%-- Include Header.jsp để có thanh điều hướng --%>
    <jsp:include page="/Header.jsp" />

    <div class="container mt-4">
       
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h1 class="text-dark">Danh sách người dùng</h1>
            
        </div>


        <%-- Các thẻ alert thông báo (copy từ danh_sach_san_pham.jsp) --%>
        <% if (errorMessage != null && !errorMessage.isEmpty()) { %>
            <div id="errorMessageAlert" class="alert alert-danger text-center alert-dismissible fade show" role="alert">
                <%= errorMessage %>
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
        <% } %>
        <% if (successMessage != null && !successMessage.isEmpty()) { %>
            <div id="successMessageAlert" class="alert alert-success text-center alert-dismissible fade show" role="alert">
                <%= successMessage %>
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
        <% } %>

        

        <table class="table table-bordered table-striped">
            <thead>
                <tr>
                    <th>Mã Người Dùng</th>
                    <th>Họ Tên</th>
                    <th>Email</th>
                    <th>Số Điện Thoại</th>
                    <th>Địa Chỉ</th>
                    <th>Vai Trò</th>
                    <th>Ngày Tạo</th>
                    <th>Hành động</th>
                </tr>
            </thead>
            <tbody>
                <%
                    if (nguoiDungList != null && !nguoiDungList.isEmpty()) {
                        for (Model_NguoiDung user : nguoiDungList) {
                %>
                            <tr>
                                <td><%= user.getMaNguoiDung() %></td>
                                <td><%= user.getHoTen() %></td>
                                <td><%= user.getEmail() %></td>
                                <td><%= user.getSoDienThoai() != null ? user.getSoDienThoai() : "N/A" %></td>
                                <td><%= user.getDiaChi() != null ? user.getDiaChi() : "N/A" %></td>
                                <td><%= user.getVaiTro() %></td>
                                <td><%= user.getNgayTao() %></td>
                                <td>
                                    <%-- Chỉ hiển thị nút Sửa vai trò nếu người dùng hiện tại là admin --%>
                                    <% if ("admin".equals(userRole)) { %>
                                        <a href="<%= request.getContextPath() %>/UserManagementServlet?action=editRole&id=<%= user.getMaNguoiDung() %>" class="btn btn-info btn-sm">Sửa vai trò</a>
                                    <% } else { %>
                                        <span class="text-muted">Không có quyền</span>
                                    <% } %>
                                </td>
                            </tr>
                <%
                        }
                    } else {
                %>
                        <tr>
                            <td colspan="8" class="text-center">Không có người dùng nào để hiển thị.</td>
                        </tr>
                <%
                    }
                %>
            </tbody>
        </table>
    </div>

    <%-- Include footer.jsp --%>
    <jsp:include page="/footer.jsp" />

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            // Đoạn mã làm biến mất thông báo sau vài giây (copy từ danh_sach_san_pham.jsp)
            const errorMessageAlert = document.getElementById('errorMessageAlert');
            const successMessageAlert = document.getElementById('successMessageAlert');

            if (errorMessageAlert) {
                setTimeout(() => {
                    const bsAlert = bootstrap.Alert.getInstance(errorMessageAlert);
                    if (bsAlert) {
                        bsAlert.dispose();
                    } else {
                        errorMessageAlert.remove();
                    }
                }, 5000); // 5000 milliseconds = 5 giây
            }

            if (successMessageAlert) {
                setTimeout(() => {
                    const bsAlert = bootstrap.Alert.getInstance(successMessageAlert);
                    if (bsAlert) {
                        bsAlert.dispose();
                    } else {
                        successMessageAlert.remove();
                    }
                }, 5000); // 5000 milliseconds = 5 giây
            }
        });
    </script>
</body>
</html>