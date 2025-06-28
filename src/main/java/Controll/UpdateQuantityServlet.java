package Controll;

import Data.GioHang;
import Model.Model_GioHang;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/UpdateQuantityServlet")
public class UpdateQuantityServlet extends HttpServlet {

    @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }

    @Override protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/plain; charset=UTF-8");

        HttpSession ss = req.getSession(false);
        Integer uid   = (ss == null) ? null : (Integer) ss.getAttribute("maNguoiDung");
        if (uid == null) { resp.getWriter().write("login-required"); return; }

        int pid = Integer.parseInt(req.getParameter("maSanPham"));
        int qty = Integer.parseInt(req.getParameter("soLuongMoi"));

        try (GioHang dao = new GioHang()) {

            if (dao.updateSoLuongGioHang(uid, pid, qty) == 0) {
                resp.getWriter().write("error|Không tìm thấy sản phẩm.");
                return;
            }

            /* cập nhật giỏ + badge */
            List<Model_GioHang> list = dao.getGioHangByMaNguoiDung(uid);
            ss.setAttribute("gioHangList", list);

            int total = dao.getTongSoSanPham(uid);
            ss.setAttribute("cartCount", total);

            resp.getWriter().write("ok|" + total);

        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().write("error|Lỗi cập nhật số lượng");
        }
    }
}
