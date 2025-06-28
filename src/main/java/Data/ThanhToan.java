package Data;

import java.sql.*;
import java.time.LocalDateTime;
import Model.Model_ThanhToan;

public class ThanhToan {
    private final Connection conn;

    public ThanhToan(Connection conn) {
        this.conn = conn;
    }

    public void addThanhToan(Model_ThanhToan t) throws SQLException {
        String sql = """
            INSERT INTO thanhtoan
            (MaDonHang, HinhThuc, TrangThai, NgayThanhToan)
            VALUES (?,?,?,?)
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt   (1, t.getMaDonHang());
            ps.setString(2, t.getHinhThuc());           // ← KHÔNG ĐƯỢC NULL
            ps.setString(3, t.getTrangThai());

            LocalDateTime ngay = t.getNgayThanhToan();
            if (ngay != null) {
                ps.setTimestamp(4, Timestamp.valueOf(ngay));
            } else {
                ps.setNull(4, Types.TIMESTAMP);
            }
            System.out.println("▶ [DAO] Insert thanh toán: HinhThuc=" + t.getHinhThuc());
            ps.executeUpdate();
        }
    }
}
