package Controll;

import Connect.ConnectSQL;
import Data.*;
import Model.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.List;

@WebServlet("/ThanhToanServlet")
public class ThanhToanServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        /* -------- 0. XÁC THỰC ĐĂNG NHẬP -------- */
        HttpSession ss = req.getSession(false);
        Model_NguoiDung user = (ss == null) ? null
                : (Model_NguoiDung) ss.getAttribute("loggedInUser");
        if (user == null) {
            resp.sendRedirect("Login.jsp");
            return;
        }

        /* -------- 1. NHÁNH “Mua ngay” (addToCart & goto checkout) -------- */
        if ("addToCartAndCheckout".equals(req.getParameter("action"))) {
            try (GioHang dao = new GioHang()) {
                int pid = Integer.parseInt(req.getParameter("maSanPham"));
                int qty = Integer.parseInt(req.getParameter("soLuong"));
                dao.addOrUpdateGioHang(user.getMaNguoiDung(), pid, qty);
            } catch (Exception e) {
                req.setAttribute("errorMessage", "Lỗi khi thêm sản phẩm vào giỏ!");
                req.getRequestDispatcher("/index.jsp").forward(req, resp);
                return;
            }
            resp.sendRedirect(req.getContextPath() + "/thanh_toan");
            return;
        }

        /* -------- 2. ĐỌC DỮ LIỆU FORM -------- */
        String diaChi = req.getParameter("diaChi");
        if (diaChi == null || diaChi.isBlank()) diaChi = user.getDiaChi();

        String ghiChu   = req.getParameter("ghiChu");
        String hinhThuc = req.getParameter("hinhThucThanhToan");
        if (hinhThuc == null || hinhThuc.isBlank()) hinhThuc = "COD";

        /* -------- 3. XỬ LÝ GIAO DỊCH -------- */
        try (Connection conn = ConnectSQL.getConnection()) {
            conn.setAutoCommit(false);

            GioHang           ghDAO = new GioHang(conn);
            SanPham           spDAO = new SanPham(conn);
            DonHang           dhDAO = new DonHang(conn);
            ChiTietDonHang    ctDAO = new ChiTietDonHang(conn);
            ThanhToan         ttDAO = new ThanhToan(conn);

            List<Model_GioHang> cart = ghDAO.getGioHangByMaNguoiDung(user.getMaNguoiDung());
            if (cart == null || cart.isEmpty())
                throw new Exception("Giỏ hàng trống – không thể thanh toán.");

            /* ---- 3.1 kiểm kho & tính tổng ---- */
            BigDecimal tongTien = BigDecimal.ZERO;
            for (Model_GioHang item : cart) {
                Model_SanPham sp = spDAO.getSanPhamById(item.getMaSanPham());
                if (sp == null || sp.getSoLuongTonKho() < item.getSoLuong())
                    throw new Exception("Sản phẩm '" + item.getMaSanPham() + "' không đủ tồn kho.");
                tongTien = tongTien.add(
                        sp.getGia().multiply(BigDecimal.valueOf(item.getSoLuong())));
            }

            /* ---- 3.2 tạo đơn hàng ---- */
            Model_DonHang dh = new Model_DonHang();
            dh.setMaNguoiDung(user.getMaNguoiDung());
            dh.setTongTien(tongTien.doubleValue());
            dh.setTrangThai("Chờ xác nhận");
            dh.setNgayDat(LocalDateTime.now());
            dh.setDiaChiGiao(diaChi);
            dh.setGhiChu(ghiChu);
            int maDH = dhDAO.addDonHang(dh);
            if (maDH <= 0) throw new Exception("Không tạo được đơn hàng.");

            /* ---- 3.3 chi tiết + cập nhật tồn kho ---- */
            for (Model_GioHang item : cart) {
                Model_ChiTietDonHang ct = new Model_ChiTietDonHang();
                ct.setMaDonHang(maDH);
                ct.setMaSanPham(item.getMaSanPham());
                ct.setSoLuong(item.getSoLuong());
                ct.setDonGia(item.getSanPham().getGia().doubleValue());
                ctDAO.addChiTietDonHang(ct);

                spDAO.updateSoLuongTon(item.getMaSanPham(), item.getSoLuong());
            }

            /* ---- 3.4 ghi thanh toán ---- */
            Model_ThanhToan pay = new Model_ThanhToan();
            pay.setMaDonHang(maDH);
            pay.setHinhThuc(hinhThuc);
            pay.setTrangThai("ChuaThanhToan");
            ttDAO.addThanhToan(pay);

            /* ---- 3.5 clear giỏ & session ---- */
            ghDAO.clearGioHang(user.getMaNguoiDung());
            ss.removeAttribute("gioHangList");
            ss.setAttribute("cartCount", 0);

            conn.commit();

            /* ---- 3.6 redirect xác nhận ---- */
            resp.sendRedirect(req.getContextPath()
                    + "/xac_nhan_don_hang.jsp?maDonHang=" + maDH);

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("errorMessage", "Lỗi thanh toán: " + e.getMessage());
            req.getRequestDispatcher("/thanh_toan.jsp").forward(req, resp);
        }
    }
}
