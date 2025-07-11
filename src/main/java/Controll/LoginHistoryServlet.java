package Controll;

import Data.NguoiDung;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/admin/lich-su-dang-nhap")
public class LoginHistoryServlet extends HttpServlet {

    @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession ss = req.getSession(false);
        String role = (ss != null) ? (String) ss.getAttribute("userRole") : null;

        if (role == null || !"admin".equalsIgnoreCase(role)) {
            resp.sendError(403, "Bạn không có quyền truy cập!");
            return;
        }

        try {
            NguoiDung dao = new NguoiDung();
            List<String[]> history = dao.getLichSuDangNhap();
            req.setAttribute("history", history);
        } catch (SQLException e) {
            req.setAttribute("error", "Lỗi CSDL: " + e.getMessage());
        }

        req.getRequestDispatcher("/lich_su_dang_nhap.jsp").forward(req, resp);
    }
}
