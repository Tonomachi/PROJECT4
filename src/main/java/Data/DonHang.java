package Data;

import Connect.ConnectSQL;
import Model.Model_ChiTietDonHang;
import Model.Model_DonHang;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;          // ← import nằm đúng chỗ!

public class DonHang {

    private Connection conn;

    /* ----- constructor ----- */
    public DonHang() {
        try { this.conn = ConnectSQL.getConnection(); }
        catch (SQLException e) { e.printStackTrace(); }
    }
    public DonHang(Connection c) { this.conn = c; }

    /* ----- thêm đơn hàng ----- */
    public int addDonHang(Model_DonHang d) throws SQLException {
        String sql = """
            INSERT INTO donhang
                   (MaNguoiDung, TongTien, TrangThai, NgayDat, DiaChiGiao, GhiChu)
            VALUES (?,?,?,?,?,?)
        """;
        try (PreparedStatement ps =
                 conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt   (1, d.getMaNguoiDung());
            ps.setDouble(2, d.getTongTien());
            ps.setString(3, d.getTrangThai());
            ps.setTimestamp(4, Timestamp.valueOf(d.getNgayDat()));
            ps.setString(5, d.getDiaChiGiao());
            ps.setString(6, d.getGhiChu());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                return rs.next() ? rs.getInt(1) : -1;
            }
        }
    }

    /* ----- lấy danh sách + thông tin KH ----- */
    public List<Model_DonHang> getAllWithUserInfo() throws SQLException {
        String sql = """
            SELECT dh.*, nd.HoTen, nd.SoDienThoai
            FROM   donhang dh
                   JOIN nguoidung nd ON dh.MaNguoiDung = nd.MaNguoiDung
            ORDER  BY dh.NgayDat DESC
        """;
        List<Model_DonHang> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Model_DonHang d = new Model_DonHang();
                d.setMaDonHang(rs.getInt("MaDonHang"));
                d.setMaNguoiDung(rs.getInt("MaNguoiDung"));
                d.setTongTien(rs.getDouble("TongTien"));
                d.setTrangThai(rs.getString("TrangThai"));
                d.setNgayDat(rs.getTimestamp("NgayDat").toLocalDateTime());
                d.setDiaChiGiao(rs.getString("DiaChiGiao"));
                d.setGhiChu(rs.getString("GhiChu"));
                d.setHoTenNguoiDung(rs.getString("HoTen"));
                d.setSoDienThoaiNguoiDung(rs.getString("SoDienThoai"));
                list.add(d);
            }
        }
        return list;
    }

    /* ----- chi tiết đơn ----- */
    public List<Model_ChiTietDonHang> getChiTietByMaDon(int maDon) throws SQLException {
        String sql = """
            SELECT ct.MaSanPham, sp.TenSanPham, ct.SoLuong, ct.DonGia
            FROM   chitietdonhang ct
                   JOIN sanpham sp ON ct.MaSanPham = sp.MaSanPham
            WHERE  ct.MaDonHang = ?
        """;
        List<Model_ChiTietDonHang> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maDon);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Model_ChiTietDonHang ct = new Model_ChiTietDonHang();
                    ct.setMaSanPham(rs.getInt("MaSanPham"));
                    ct.setTenSanPham(rs.getString("TenSanPham"));
                    ct.setSoLuong(rs.getInt("SoLuong"));
                    ct.setDonGia(rs.getDouble("DonGia"));
                    list.add(ct);
                }
            }
        }
        return list;
    }
}
