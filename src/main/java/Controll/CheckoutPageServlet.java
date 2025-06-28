package Controll;

import Connect.ConnectSQL;
import Data.GioHang;
import Model.Model_GioHang;
import Model.Model_NguoiDung;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.List;

@WebServlet("/thanh_toan")
public class CheckoutPageServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Model_NguoiDung user = (session == null)
                ? null : (Model_NguoiDung) session.getAttribute("loggedInUser");

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/Login.jsp");
            return;
        }

        List<Model_GioHang> gioHangList = null;
        BigDecimal subtotal = BigDecimal.ZERO;
        String error = null;

        try (Connection conn = ConnectSQL.getConnection();
             GioHang dao   = new GioHang(conn)) {

            gioHangList = dao.getGioHangByMaNguoiDung(user.getMaNguoiDung());
            if (gioHangList == null || gioHangList.isEmpty()) {
                error = "Giỏ hàng của bạn đang trống.";
            } else {
                for (Model_GioHang g : gioHangList) {
                    subtotal = subtotal.add(
                            g.getSanPham().getGia()
                             .multiply(BigDecimal.valueOf(g.getSoLuong())));
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            error = "Lỗi tải giỏ hàng: " + ex.getMessage();
        }

        request.setAttribute("gioHangList", gioHangList); // KHÔNG null hóa
        request.setAttribute("subtotal",     subtotal);
        request.setAttribute("errorMessage", error);

        // GÁN GIỎ HÀNG VÀO SESSION ĐỂ THANH TOÁN JSP DÙNG ĐƯỢC
        session.setAttribute("gioHangList", gioHangList);
        session.setAttribute("subtotal", subtotal);

        request.getRequestDispatcher("/thanh_toan.jsp").forward(request, response);
    }
}
