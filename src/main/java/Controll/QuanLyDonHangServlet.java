// Controll/QuanLyDonHangServlet.java
package Controll;

import Data.DonHang;
import Model.Model_ChiTietDonHang;
import Model.Model_DonHang;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@WebServlet("/QuanLyDonHangServlet")
public class QuanLyDonHangServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession ss = req.getSession(false);
        String role = (ss != null) ? (String) ss.getAttribute("userRole") : null;
        if (role == null || !role.equalsIgnoreCase("admin")) {
            resp.sendError(403, "Bạn không có quyền truy cập!");
            return;
        }

        try {
            DonHang dao = new DonHang();
            List<Model_DonHang> ds = dao.getAllWithUserInfo();

            // gom chi tiết theo mã đơn để show
            Map<Integer, List<Model_ChiTietDonHang>> chiTiet =
                    ds.stream().collect(Collectors.toMap(
                        Model_DonHang::getMaDonHang,
                        d -> {
                            try { return dao.getChiTietByMaDon(d.getMaDonHang()); }
                            catch (SQLException e) { return List.of(); }
                        }
                    ));

            req.setAttribute("orders", ds);
            req.setAttribute("detailsMap", chiTiet);
        } catch (SQLException e) {
            req.setAttribute("error", e.getMessage());
        }

        req.getRequestDispatcher("/quan_ly_don_hang.jsp").forward(req, resp);
    }
}
