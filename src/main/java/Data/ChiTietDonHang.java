package Data;

import Model.Model_ChiTietDonHang;
import java.sql.*;

import Connect.ConnectSQL;

public class ChiTietDonHang {
    private Connection conn;

    // Constructor mặc định: tự tạo và quản lý connection
    public ChiTietDonHang() {
        try {
            this.conn = ConnectSQL.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Lỗi kết nối cơ sở dữ liệu trong ChiTietDonHang DAO (default constructor): " + e.getMessage());
        }
    }

    // Constructor cho transaction: nhận connection từ bên ngoài
    public ChiTietDonHang(Connection connection) {
        this.conn = connection;
    }

    public void addChiTietDonHang(Model_ChiTietDonHang chiTiet) throws SQLException {
        String sql = "INSERT INTO chitietdonhang (MaDonHang, MaSanPham, SoLuong, DonGia) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) { // Tự động đóng ps
            ps.setInt(1, chiTiet.getMaDonHang());
            ps.setInt(2, chiTiet.getMaSanPham());
            ps.setInt(3, chiTiet.getSoLuong());
            ps.setDouble(4, chiTiet.getDonGia());
            ps.executeUpdate();
        }
        // KHÔNG đóng connection ở đây. Servlet sẽ đóng nó.
    }

    // Các phương thức khác như getChiTietDonHangByMaDonHang
}