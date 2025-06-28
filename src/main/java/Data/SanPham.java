package Data;

import Model.Model_SanPham;
import Connect.ConnectSQL;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SanPham {

    private Connection conn;

    // Constructor mặc định
    public SanPham() {
        try {
            this.conn = ConnectSQL.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Lỗi kết nối trong SanPham DAO (constructor mặc định): " + e.getMessage());
        }
    }

    // Constructor truyền sẵn Connection (để dùng trong transaction)
    public SanPham(Connection conn) {
        this.conn = conn;
    }

    // Lấy tất cả sản phẩm
    public List<Model_SanPham> getAllSanPhams() throws SQLException {
        List<Model_SanPham> sanPhams = new ArrayList<>();
        String sql = "SELECT MaSanPham, TenSanPham, MoTa, Gia, SoLuong, HinhAnh, MaDanhMuc FROM SanPham";

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Model_SanPham sp = new Model_SanPham();
                sp.setMaSanPham(rs.getInt("MaSanPham"));
                sp.setTenSanPham(rs.getString("TenSanPham"));
                sp.setMoTa(rs.getString("MoTa"));
                sp.setGia(rs.getBigDecimal("Gia"));
                sp.setSoLuongTonKho(rs.getInt("SoLuong"));
                sp.setHinhAnh(rs.getString("HinhAnh"));
                sp.setMaDanhMuc(rs.getInt("MaDanhMuc"));
                sanPhams.add(sp);
            }
        }
        return sanPhams;
    }

    // Lấy sản phẩm theo ID
    public Model_SanPham getSanPhamById(int maSanPham) throws SQLException {
        Model_SanPham sanPham = null;
        String sql = "SELECT MaSanPham, TenSanPham, MoTa, Gia, SoLuong, HinhAnh, MaDanhMuc FROM SanPham WHERE MaSanPham = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maSanPham);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    sanPham = new Model_SanPham();
                    sanPham.setMaSanPham(rs.getInt("MaSanPham"));
                    sanPham.setTenSanPham(rs.getString("TenSanPham"));
                    sanPham.setMoTa(rs.getString("MoTa"));
                    sanPham.setGia(rs.getBigDecimal("Gia"));
                    sanPham.setSoLuongTonKho(rs.getInt("SoLuong"));
                    sanPham.setHinhAnh(rs.getString("HinhAnh"));
                    sanPham.setMaDanhMuc(rs.getInt("MaDanhMuc"));
                }
            }
        }
        return sanPham;
    }

    // Thêm mới sản phẩm
    public int insertSanPham(Model_SanPham sanPham) throws SQLException {
        String sql = "INSERT INTO SanPham (TenSanPham, MoTa, Gia, SoLuong, HinhAnh, MaDanhMuc) VALUES (?, ?, ?, ?, ?, ?)";
        int generatedId = -1;

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, sanPham.getTenSanPham());
            ps.setString(2, sanPham.getMoTa());
            ps.setBigDecimal(3, sanPham.getGia());
            ps.setInt(4, sanPham.getSoLuongTonKho());
            ps.setString(5, sanPham.getHinhAnh());
            ps.setInt(6, sanPham.getMaDanhMuc());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        generatedId = rs.getInt(1);
                    }
                }
            }
        }
        return generatedId;
    }

    // Cập nhật sản phẩm
    public boolean updateSanPham(Model_SanPham sanPham) throws SQLException {
        String sql = "UPDATE SanPham SET TenSanPham = ?, MoTa = ?, Gia = ?, SoLuong = ?, HinhAnh = ?, MaDanhMuc = ? WHERE MaSanPham = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, sanPham.getTenSanPham());
            ps.setString(2, sanPham.getMoTa());
            ps.setBigDecimal(3, sanPham.getGia());
            ps.setInt(4, sanPham.getSoLuongTonKho());
            ps.setString(5, sanPham.getHinhAnh());
            ps.setInt(6, sanPham.getMaDanhMuc());
            ps.setInt(7, sanPham.getMaSanPham());

            return ps.executeUpdate() > 0;
        }
    }

    // Xoá sản phẩm
    public boolean deleteSanPham(int maSanPham) throws SQLException {
        String sql = "DELETE FROM SanPham WHERE MaSanPham = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maSanPham);
            return ps.executeUpdate() > 0;
        }
    }

    // Giảm số lượng tồn kho khi thanh toán
    public boolean updateSoLuongTon(int maSanPham, int soLuongGiam) throws SQLException {
        String sql = "UPDATE SanPham SET SoLuong = SoLuong - ? WHERE MaSanPham = ? AND SoLuong >= ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, soLuongGiam);
            ps.setInt(2, maSanPham);
            ps.setInt(3, soLuongGiam);
            return ps.executeUpdate() > 0;
        }
    }

    // Lấy sản phẩm theo danh mục
    public List<Model_SanPham> getSanPhamsByMaDanhMuc(int maDanhMuc) throws SQLException {
        List<Model_SanPham> sanPhams = new ArrayList<>();
        String sql = "SELECT MaSanPham, TenSanPham, MoTa, Gia, SoLuong, HinhAnh, MaDanhMuc FROM SanPham WHERE MaDanhMuc = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maDanhMuc);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Model_SanPham sp = new Model_SanPham();
                    sp.setMaSanPham(rs.getInt("MaSanPham"));
                    sp.setTenSanPham(rs.getString("TenSanPham"));
                    sp.setMoTa(rs.getString("MoTa"));
                    sp.setGia(rs.getBigDecimal("Gia"));
                    sp.setSoLuongTonKho(rs.getInt("SoLuong"));
                    sp.setHinhAnh(rs.getString("HinhAnh"));
                    sp.setMaDanhMuc(rs.getInt("MaDanhMuc"));
                    sanPhams.add(sp);
                }
            }
        }
        return sanPhams;
    }

    // Lấy sản phẩm nổi bật (mới nhất)
    public List<Model_SanPham> getFeaturedSanPhams() throws SQLException {
        List<Model_SanPham> sanPhams = new ArrayList<>();
        String sql = "SELECT MaSanPham, TenSanPham, MoTa, Gia, SoLuong, HinhAnh, MaDanhMuc FROM SanPham ORDER BY MaSanPham DESC LIMIT 6";

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Model_SanPham sp = new Model_SanPham();
                sp.setMaSanPham(rs.getInt("MaSanPham"));
                sp.setTenSanPham(rs.getString("TenSanPham"));
                sp.setMoTa(rs.getString("MoTa"));
                sp.setGia(rs.getBigDecimal("Gia"));
                sp.setSoLuongTonKho(rs.getInt("SoLuong"));
                sp.setHinhAnh(rs.getString("HinhAnh"));
                sp.setMaDanhMuc(rs.getInt("MaDanhMuc"));
                sanPhams.add(sp);
            }
        }
        return sanPhams;
    }
}
