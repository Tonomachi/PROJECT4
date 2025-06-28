package Controll;

import Data.GioHang;
import Model.Model_GioHang;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/gio_hang")
public class CartPageServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Integer uid = (Integer) req.getSession().getAttribute("maNguoiDung");
        if (uid == null) { resp.sendRedirect("Login.jsp"); return; }

        try (GioHang dao = new GioHang()) {
            List<Model_GioHang> list = dao.getGioHangByMaNguoiDung(uid);
            req.getSession().setAttribute("gioHangList", list);   // cho icon
            req.setAttribute("gioHangList", list);                // cho JSP hiển thị
        } catch (Exception e) {
            throw new ServletException(e);
        }
        req.getRequestDispatcher("/gio_hang.jsp").forward(req, resp);
    }
}
