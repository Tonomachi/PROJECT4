package Data;

import Model.Model_DonHang;
import java.sql.*;
import java.time.LocalDateTime;
import Connect.ConnectSQL;

public class DonHang {
    private Connection conn;

    public DonHang() {
        try {
            this.conn = ConnectSQL.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Lỗi kết nối cơ sở dữ liệu trong DonHang DAO (default constructor): " + e.getMessage());
        }
    }

    public DonHang(Connection connection) {
        this.conn = connection;
    }

    public int addDonHang(Model_DonHang donHang) throws SQLException {
    	String sql = "INSERT INTO donhang (MaNguoiDung, TongTien, TrangThai, NgayDat, DiaChiGiao, GhiChu) VALUES (?, ?, ?, ?, ?, ?)";

        int maDonHang = -1;
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) { // Tự động đóng ps
            ps.setInt(1, donHang.getMaNguoiDung());
            ps.setDouble(2, donHang.getTongTien());
            ps.setString(3, donHang.getTrangThai());
            ps.setTimestamp(4, Timestamp.valueOf(donHang.getNgayDat()));
            ps.setString(5, donHang.getDiaChiGiao());
            ps.setString(6, donHang.getGhiChu());

            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) { // Tự động đóng rs
                    if (rs.next()) {
                        maDonHang = rs.getInt(1);
                    }
                }
            }
        }
        return maDonHang;
    }
}