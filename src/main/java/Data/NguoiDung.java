package Data;

import Model.Model_NguoiDung;
import Connect.ConnectSQL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NguoiDung {

    public Model_NguoiDung validateLogin(String email, String matKhau) throws SQLException {
        String sql = "SELECT MaNguoiDung, HoTen, Email, MatKhau, SoDienThoai, DiaChi, VaiTro, NgayTao FROM nguoidung WHERE Email = ? AND MatKhau = ?";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, matKhau);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Model_NguoiDung nguoiDung = new Model_NguoiDung();
                    nguoiDung.setMaNguoiDung(rs.getInt("MaNguoiDung"));
                    nguoiDung.setHoTen(rs.getString("HoTen"));
                    nguoiDung.setEmail(rs.getString("Email"));
                    nguoiDung.setMatKhau(rs.getString("MatKhau"));
                    nguoiDung.setSoDienThoai(rs.getString("SoDienThoai"));
                    nguoiDung.setDiaChi(rs.getString("DiaChi"));
                    nguoiDung.setVaiTro(rs.getString("VaiTro")); // Đảm bảo tên cột chính xác "VaiTro"
                    nguoiDung.setNgayTao(rs.getTimestamp("NgayTao"));
                    return nguoiDung;
                }
            }
        }
        return null;
    }

    public boolean isEmailExists(String email) throws SQLException {
        String sql = "SELECT COUNT(*) FROM nguoidung WHERE Email = ?";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    // Đã sửa: Kiểu trả về là boolean và thêm return
    public boolean registerUser(Model_NguoiDung nguoiDung) throws SQLException {
        String sql = "INSERT INTO nguoidung (HoTen, Email, MatKhau, SoDienThoai, DiaChi, VaiTro) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nguoiDung.getHoTen());
            ps.setString(2, nguoiDung.getEmail());
            ps.setString(3, nguoiDung.getMatKhau());
            ps.setString(4, nguoiDung.getSoDienThoai());
            ps.setString(5, nguoiDung.getDiaChi());
            ps.setString(6, nguoiDung.getVaiTro());

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0; // Trả về true nếu thành công
        }
    }

    public Model_NguoiDung getUserById(int maNguoiDung) throws SQLException {
        String sql = "SELECT MaNguoiDung, HoTen, Email, MatKhau, SoDienThoai, DiaChi, VaiTro, NgayTao FROM nguoidung WHERE MaNguoiDung = ?";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maNguoiDung);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Model_NguoiDung nguoiDung = new Model_NguoiDung();
                    nguoiDung.setMaNguoiDung(rs.getInt("MaNguoiDung"));
                    nguoiDung.setHoTen(rs.getString("HoTen"));
                    nguoiDung.setEmail(rs.getString("Email"));
                    nguoiDung.setMatKhau(rs.getString("MatKhau"));
                    nguoiDung.setSoDienThoai(rs.getString("SoDienThoai"));
                    nguoiDung.setDiaChi(rs.getString("DiaChi"));
                    nguoiDung.setVaiTro(rs.getString("VaiTro"));
                    nguoiDung.setNgayTao(rs.getTimestamp("NgayTao"));
                    return nguoiDung;
                }
            }
        }
        return null;
    }

    public boolean updateUserRole(Model_NguoiDung user) throws SQLException {
        String sql = "UPDATE nguoidung SET VaiTro = ? WHERE MaNguoiDung = ?";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getVaiTro());
            ps.setInt(2, user.getMaNguoiDung());
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public List<Model_NguoiDung> getAllUsers() throws SQLException {
        List<Model_NguoiDung> userList = new ArrayList<>();
        String sql = "SELECT MaNguoiDung, HoTen, Email, MatKhau, SoDienThoai, DiaChi, VaiTro, NgayTao FROM nguoidung";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Model_NguoiDung nguoiDung = new Model_NguoiDung();
                nguoiDung.setMaNguoiDung(rs.getInt("MaNguoiDung"));
                nguoiDung.setHoTen(rs.getString("HoTen"));
                nguoiDung.setEmail(rs.getString("Email"));
                nguoiDung.setMatKhau(rs.getString("MatKhau"));
                nguoiDung.setSoDienThoai(rs.getString("SoDienThoai"));
                nguoiDung.setDiaChi(rs.getString("DiaChi"));
                nguoiDung.setVaiTro(rs.getString("VaiTro"));
                nguoiDung.setNgayTao(rs.getTimestamp("NgayTao"));
                userList.add(nguoiDung);
            }
        }
        return userList;
    }

    // Phương thức 'updateVaiTro' bạn có thể giữ hoặc xóa tùy ý
    public boolean updateVaiTro(int maNguoiDung, String newVaiTro) throws SQLException {
        String sql = "UPDATE nguoidung SET VaiTro = ? WHERE MaNguoiDung = ?";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newVaiTro);
            ps.setInt(2, maNguoiDung);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        }
    }
}