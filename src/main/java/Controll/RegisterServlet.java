package Controll;

import Data.NguoiDung;
import Model.Model_NguoiDung;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet { // Đã đổi tên class thành RegisterServlet cho chuẩn
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String hoTen = request.getParameter("hoTen");
        String email = request.getParameter("email");
        String matKhau = request.getParameter("matKhau");
        String xacNhanMatKhau = request.getParameter("xacNhanMatKhau");
        String soDienThoai = request.getParameter("soDienThoai");
        String diaChi = request.getParameter("diaChi");

        NguoiDung nguoiDungDao = new NguoiDung();

        if (!matKhau.equals(xacNhanMatKhau)) {
            request.setAttribute("errorMessage", "Mật khẩu và xác nhận mật khẩu không khớp.");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        try {
            if (nguoiDungDao.isEmailExists(email)) {
                request.setAttribute("errorMessage", "Email đã tồn tại. Vui lòng sử dụng email khác.");
                request.getRequestDispatcher("/register.jsp").forward(request, response);
                return;
            }

            Model_NguoiDung newUser = new Model_NguoiDung();
            newUser.setHoTen(hoTen);
            newUser.setEmail(email);
            newUser.setMatKhau(matKhau); // Nhắc lại: nên băm mật khẩu
            newUser.setSoDienThoai(soDienThoai);
            newUser.setDiaChi(diaChi);
            newUser.setVaiTro("KhachHang"); // Mặc định vai trò là KhachHang khi đăng ký

            boolean isRegistered = nguoiDungDao.registerUser(newUser); // Lỗi Type mismatch đã được sửa trong NguoiDung.java

            if (isRegistered) {
                request.setAttribute("successMessage", "Đăng ký thành công! Bạn có thể đăng nhập ngay.");
                response.sendRedirect(request.getContextPath() + "/Login.jsp"); // Chuyển hướng về trang login sau khi đăng ký thành công
            } else {
                request.setAttribute("errorMessage", "Đăng ký thất bại. Vui lòng thử lại.");
                request.getRequestDispatcher("/register.jsp").forward(request, response);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Đã xảy ra lỗi cơ sở dữ liệu: " + e.getMessage());
            request.getRequestDispatcher("/register.jsp").forward(request, response);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/register.jsp").forward(request, response);
    }
}