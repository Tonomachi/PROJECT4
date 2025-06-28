package Controll;

import Data.GioHang;
import Model.Model_GioHang;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/RemoveFromCartServlet")
public class RemoveFromCartServlet extends HttpServlet {

    @Override protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/plain; charset=UTF-8");

        HttpSession ss = req.getSession(false);
        Integer uid = (ss == null) ? null : (Integer) ss.getAttribute("maNguoiDung");
        if (uid == null) { resp.getWriter().write("login-required"); return; }

        int pid = Integer.parseInt(req.getParameter("maSanPham"));

        try (GioHang dao = new GioHang()) {

            dao.removeSanPhamFromGioHang(uid, pid);

            List<Model_GioHang> list = dao.getGioHangByMaNguoiDung(uid);
            ss.setAttribute("gioHangList", list);

            int total = dao.getTongSoSanPham(uid);
            ss.setAttribute("cartCount", total);

            resp.getWriter().write("ok|" + total);          // dùng Ajax thì trả về ok|

        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().write("error|Lỗi xoá sản phẩm");
        }
    }
}

