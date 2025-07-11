// Servlet: ChangePasswordServlet.java
package Controll;

import Data.NguoiDung;
import Model.Model_NguoiDung;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/doi-mat-khau")
public class ChangePasswordServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        Model_NguoiDung user = (session != null) ? (Model_NguoiDung) session.getAttribute("loggedInUser") : null;

        if (user == null || !"KhachHang".equals(user.getVaiTro())) {
            resp.sendRedirect(req.getContextPath() + "/Login.jsp");
            return;
        }

        req.getRequestDispatcher("/doi_mat_khau.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        HttpSession session = req.getSession(false);
        Model_NguoiDung user = (session != null) ? (Model_NguoiDung) session.getAttribute("loggedInUser") : null;

        if (user == null || !"KhachHang".equals(user.getVaiTro())) {
            resp.sendRedirect(req.getContextPath() + "/Login.jsp");
            return;
        }

        String current = req.getParameter("matKhauCu");
        String newPass = req.getParameter("matKhauMoi");
        String confirm = req.getParameter("xacNhanMatKhau");

        if (!user.getMatKhau().equals(current)) {
            req.setAttribute("error", "Mật khẩu hiện tại không đúng.");
        } else if (!newPass.equals(confirm)) {
            req.setAttribute("error", "Mật khẩu xác nhận không khớp.");
        } else {
            try {
                NguoiDung dao = new NguoiDung();
                dao.updatePassword(user.getMaNguoiDung(), newPass);
                user.setMatKhau(newPass);
                session.setAttribute("loggedInUser", user);
                req.setAttribute("message", "Đổi mật khẩu thành công.");
            } catch (SQLException e) {
                req.setAttribute("error", "Lỗi CSDL: " + e.getMessage());
            }
        }

        req.getRequestDispatcher("/doi_mat_khau.jsp").forward(req, resp);
    }
}
