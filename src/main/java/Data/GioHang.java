package Data;

import Connect.ConnectSQL;
import Model.Model_GioHang;
import Model.Model_SanPham;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/** DAO thao tác bảng giohang */
public class GioHang implements AutoCloseable {

    /* ---------- 1. FIELD ---------- */
    private Connection conn;

    /* ---------- 2. CONSTRUCTOR ---------- */
    /** Tự mở connection mới */
    public GioHang() throws SQLException {
        this.conn = ConnectSQL.getConnection();
    }
    /** Dùng connection truyền vào (ví dụ trong transaction) */
    public GioHang(Connection conn) { this.conn = conn; }

    /* ---------- 3. CLOSE (cho try-with-resources) ---------- */
    @Override public void close() { ConnectSQL.closeQuietly(conn); }

    /* ---------- 4. TIỆN ÍCH ---------- */
    /** Đếm tổng số SP (cộng số lượng) trong giỏ của 1 user */
    public int getTongSoSanPham(int uid) throws SQLException {
        String sql = "SELECT COALESCE(SUM(SoLuong),0) FROM giohang WHERE MaNguoiDung=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, uid);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    /* ---------- 5. LẤY DANH SÁCH GIỎ ---------- */
    public List<Model_GioHang> getGioHangByMaNguoiDung(int uid) throws SQLException {
        String sql = """
            SELECT gh.MaGioHang, gh.MaNguoiDung, gh.MaSanPham, gh.SoLuong, gh.NgayThem,
                   sp.MaDanhMuc, sp.TenSanPham, sp.MoTa, sp.Gia, sp.HinhAnh,
                   sp.SoLuong AS SoLuongTon
            FROM   giohang gh
                   JOIN sanpham sp ON gh.MaSanPham = sp.MaSanPham
            WHERE  gh.MaNguoiDung = ?
        """;

        List<Model_GioHang> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, uid);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Model_GioHang gh = new Model_GioHang(
                            rs.getInt("MaGioHang"),
                            rs.getInt("MaNguoiDung"),
                            rs.getInt("MaSanPham"),
                            rs.getInt("SoLuong"),
                            rs.getTimestamp("NgayThem").toLocalDateTime());

                    Model_SanPham sp = new Model_SanPham(
                            rs.getInt("MaSanPham"),
                            rs.getString("TenSanPham"),
                            rs.getString("MoTa"),
                            rs.getBigDecimal("Gia"),
                            rs.getInt("SoLuongTon"),
                            rs.getString("HinhAnh"),
                            rs.getInt("MaDanhMuc"));

                    gh.setSanPham(sp);
                    list.add(gh);
                }
            }
        }
        return list;
    }
    public int updateSoLuongGioHang(int uid, int pid, int qty) throws SQLException {
        String sql = "UPDATE giohang SET SoLuong=?, NgayThem=NOW() " +
                     "WHERE MaNguoiDung=? AND MaSanPham=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, qty);
            ps.setInt(2, uid);
            ps.setInt(3, pid);
            return ps.executeUpdate();          // trả về số dòng bị ảnh hưởng
        }
    }
    /* ---------- 6. THÊM / CẬP NHẬT ---------- */
    /** Thêm mới hoặc cộng dồn số lượng (nếu sản phẩm đã tồn tại) */
    public void addOrUpdateGioHang(int uid, int pid, int qty) throws SQLException {
        String sqlChk = "SELECT SoLuong FROM giohang WHERE MaNguoiDung=? AND MaSanPham=?";
        try (PreparedStatement chk = conn.prepareStatement(sqlChk)) {
            chk.setInt(1, uid); chk.setInt(2, pid);
            try (ResultSet rs = chk.executeQuery()) {
                if (rs.next()) {                               // đã có → update
                    String sqlUp = """
                        UPDATE giohang
                        SET SoLuong = SoLuong + ?, NgayThem = ?
                        WHERE MaNguoiDung = ? AND MaSanPham = ?
                    """;
                    try (PreparedStatement up = conn.prepareStatement(sqlUp)) {
                        up.setInt(1, qty);
                        up.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
                        up.setInt(3, uid);
                        up.setInt(4, pid);
                        up.executeUpdate();
                    }
                } else {                                       // chưa có → insert
                    String sqlIn = """
                        INSERT INTO giohang(MaNguoiDung, MaSanPham, SoLuong, NgayThem)
                        VALUES (?,?,?,?)
                    """;
                    try (PreparedStatement ins = conn.prepareStatement(sqlIn)) {
                        ins.setInt(1, uid);
                        ins.setInt(2, pid);
                        ins.setInt(3, qty);
                        ins.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
                        ins.executeUpdate();
                    }
                }
            }
        }
    }
    public void themHoacCapNhat(int maNguoiDung, int maSanPham, int soLuongMoi) throws SQLException {
        String checkSql = "SELECT SoLuong FROM giohang WHERE MaNguoiDung = ? AND MaSanPham = ?";
        try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
            checkStmt.setInt(1, maNguoiDung);
            checkStmt.setInt(2, maSanPham);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                int slCu = rs.getInt("SoLuong");
                int slMoi = slCu + soLuongMoi;

                String update = "UPDATE giohang SET SoLuong = ? WHERE MaNguoiDung = ? AND MaSanPham = ?";
                try (PreparedStatement updateStmt = conn.prepareStatement(update)) {
                    updateStmt.setInt(1, slMoi);
                    updateStmt.setInt(2, maNguoiDung);
                    updateStmt.setInt(3, maSanPham);
                    updateStmt.executeUpdate();
                }

            } else {
                String insert = "INSERT INTO giohang (MaNguoiDung, MaSanPham, SoLuong, NgayThem) VALUES (?, ?, ?, NOW())";
                try (PreparedStatement insertStmt = conn.prepareStatement(insert)) {
                    insertStmt.setInt(1, maNguoiDung);
                    insertStmt.setInt(2, maSanPham);
                    insertStmt.setInt(3, soLuongMoi);
                    insertStmt.executeUpdate();
                }
            }
        }
    }
    public int tinhTongSoLuong(int maNguoiDung) throws SQLException {
        int tong = 0;
        String sql = "SELECT SUM(SoLuong) FROM giohang WHERE MaNguoiDung = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maNguoiDung);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                tong = rs.getInt(1);  // rs.getInt có thể trả về 0 nếu không có hàng
            }
        }
        return tong;
    }


    /* ---------- 7. CẬP NHẬT SỐ LƯỢNG CỤ THỂ ---------- */
    public int updateSoLuongSanPham(int uid, int pid, int newQty) throws SQLException {
        String sql = """
            UPDATE giohang
            SET SoLuong = ?, NgayThem = ?
            WHERE MaNguoiDung = ? AND MaSanPham = ?
        """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, newQty);
            ps.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            ps.setInt(3, uid);
            ps.setInt(4, pid);
            return ps.executeUpdate();               // trả số dòng ảnh hưởng (0/1)
        }
    }

    /* ---------- 8. XOÁ 1 SẢN PHẨM ---------- */
    /** @return số dòng bị xoá (0 hoặc 1) */
    public int removeSanPhamFromGioHang(int uid, int pid) throws SQLException {
        String sql = "DELETE FROM giohang WHERE MaNguoiDung=? AND MaSanPham=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, uid);
            ps.setInt(2, pid);
            return ps.executeUpdate();
        }
    }

    /* ---------- 9. XOÁ TOÀN GIỎ ---------- */
    public int clearGioHang(int uid) throws SQLException {
        String sql = "DELETE FROM giohang WHERE MaNguoiDung=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, uid);
            return ps.executeUpdate();               // số dòng xoá
        }
    }
}
