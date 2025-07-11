package Controll;

import Data.NguoiDung;
import Model.Model_NguoiDung;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/profile")
public class ProfileServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        Model_NguoiDung user = (session != null) 
                ? (Model_NguoiDung) session.getAttribute("loggedInUser") : null;

        if (user == null || !"KhachHang".equals(user.getVaiTro())) {
            resp.sendRedirect(req.getContextPath() + "/Login.jsp");
            return;
        }

        req.setAttribute("user", user);
        req.getRequestDispatcher("/nguoi_dung_sua.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        HttpSession session = req.getSession(false);
        Model_NguoiDung user = (session != null)
                ? (Model_NguoiDung) session.getAttribute("loggedInUser") : null;

        if (user == null || !"KhachHang".equals(user.getVaiTro())) {
            resp.sendRedirect(req.getContextPath() + "/Login.jsp");
            return;
        }

        String hoTen = req.getParameter("hoTen");
        String email = req.getParameter("email");
        String sdt   = req.getParameter("soDienThoai");
        String diaChi = req.getParameter("diaChi");

        user.setHoTen(hoTen);
        user.setEmail(email);
        user.setSoDienThoai(sdt);
        user.setDiaChi(diaChi);

        try {
            NguoiDung dao = new NguoiDung();
            dao.updateUserInfo(user);
            session.setAttribute("loggedInUser", user);
            req.setAttribute("message", "Cập nhật thành công.");
        } catch (SQLException e) {
            req.setAttribute("errorMessage", "Lỗi CSDL: " + e.getMessage());
        }

        req.setAttribute("user", user);
        req.getRequestDispatcher("/nguoi_dung_sua.jsp").forward(req, resp);
    }
}
