package Controll;

import Data.GioHang;
import Data.SanPham;
import Model.Model_GioHang;
import Model.Model_NguoiDung;
import Model.Model_SanPham;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/AddToCartServlet")
@MultipartConfig
public class AddToCartServlet extends HttpServlet {

    @Override protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain; charset=UTF-8");

        HttpSession session = request.getSession(true);
        Model_NguoiDung user = (Model_NguoiDung) session.getAttribute("loggedInUser");

        int pid = Integer.parseInt(request.getParameter("maSanPham"));
        int qty = Integer.parseInt(request.getParameter("soLuong"));

        try {
            /* --- 1. ĐÃ ĐĂNG NHẬP -------------- */
            if (user != null) {
                try (GioHang dao = new GioHang()) {
                    dao.addOrUpdateGioHang(user.getMaNguoiDung(), pid, qty);

                    int total = dao.getTongSoSanPham(user.getMaNguoiDung());
                    session.setAttribute("cartCount", total);          // ⭐ badge dùng
                    response.getWriter().write("ok|" + total);
                }
                return;
            }

            /* --- 2. CHƯA ĐĂNG NHẬP (lưu session) */
            @SuppressWarnings("unchecked")
            List<Model_GioHang> list =
                    (List<Model_GioHang>) session.getAttribute("gioHangList");
            if (list == null) list = new ArrayList<>();

            boolean found = false;
            for (Model_GioHang gh : list) {
                if (gh.getMaSanPham() == pid) {
                    gh.setSoLuong(gh.getSoLuong() + qty);
                    found = true; break;
                }
            }
            if (!found) {
                Model_SanPham sp = new SanPham().getSanPhamById(pid);
                list.add(new Model_GioHang(sp, qty));
            }

            int total = list.stream().mapToInt(Model_GioHang::getSoLuong).sum();
            session.setAttribute("gioHangList", list);
            session.setAttribute("cartCount",  total);

            response.getWriter().write("ok|" + total);

        } catch (Exception ex) {
            ex.printStackTrace();
            response.getWriter().write("error|Lỗi thêm giỏ hàng!");
        }
    }
}
