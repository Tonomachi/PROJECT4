package Controll;

import Data.GioHang;
import Data.NguoiDung;
import Model.Model_GioHang;
import Model.Model_NguoiDung;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

    /* ======================  ĐĂNG NHẬP  (POST)  ====================== */
    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String email    = request.getParameter("email");
        String password = request.getParameter("password");

        try {
            /* 1. Kiểm tra tài khoản */
            NguoiDung ndDao = new NguoiDung();
            Model_NguoiDung user = ndDao.validateLogin(email, password);

            if (user == null) {                       // Sai tài khoản
                request.setAttribute(
                        "errorMessage", "Email hoặc mật khẩu không đúng.");
                request.getRequestDispatcher("/Login.jsp")
                       .forward(request, response);
                return;
            }

            /* 2. Ghi lịch sử đăng nhập  ------------------------------ */
            String ip = request.getRemoteAddr();
            try {
                ndDao.logLogin(user.getMaNguoiDung(), ip);
            } catch (SQLException ex) {
                ex.printStackTrace();                 // log lỗi nhưng không cản đăng nhập
            }

            /* 3. Lưu thông tin vào session --------------------------- */
            HttpSession session = request.getSession(true);
            session.setAttribute("loggedInUser", user);
            session.setAttribute("userRole",    user.getVaiTro().trim());
            session.setAttribute("tenDangNhap", user.getHoTen());
            session.setAttribute("maNguoiDung", user.getMaNguoiDung());

            /* 4. Nạp giỏ hàng để badge hoạt động -------------------- */
            try (GioHang ghDao = new GioHang()) {
                List<Model_GioHang> cart =
                        ghDao.getGioHangByMaNguoiDung(user.getMaNguoiDung());
                session.setAttribute("gioHangList", cart);
            }

            /* 5. Điều hướng ------------------------------------------ */
            String role = user.getVaiTro().trim().toLowerCase();
            if ("admin".equals(role)) {
                response.sendRedirect(
                        request.getContextPath() + "/danh_sach_san_pham.jsp");
            } else {
                String target = (String) session.getAttribute("afterLoginRedirect");
                session.removeAttribute("afterLoginRedirect");
                if (target == null)
                    target = request.getContextPath() + "/Index.jsp";
                response.sendRedirect(target);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Lỗi CSDL: " + e.getMessage());
            request.getRequestDispatcher("/Login.jsp")
                   .forward(request, response);
        }
    }

    /* ======================  HIỂN THỊ FORM / ĐĂNG XUẤT  (GET)  ====================== */
    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if ("logout".equals(action)) {                // Đăng xuất
            HttpSession session = request.getSession(false);
            if (session != null) session.invalidate();
            response.sendRedirect(request.getContextPath() + "/Login.jsp");
            return;
        }

        // Mặc định: hiển thị form đăng nhập
        request.getRequestDispatcher("/Login.jsp").forward(request, response);
    }
}
