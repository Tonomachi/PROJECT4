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

    // ... (Các phương thức validateLogin, isEmailExists, registerUser, getUserById, updateUserRole, getAllUsers, updateVaiTro, updatePassword, getLichSuDangNhap, logLogin giữ nguyên)

    // Phương thức CẬP NHẬT TẤT CẢ THÔNG TIN NGƯỜI DÙNG (cho Admin) - KHÔNG BAO GỒM MẬT KHẨU
    public boolean updateAllUserInfo(Model_NguoiDung u) throws SQLException {
        String sql = """
            UPDATE nguoidung
               SET HoTen       = ? ,
                   Email       = ? ,
                   SoDienThoai = ? ,
                   DiaChi      = ? ,
                   VaiTro      = ?
             WHERE MaNguoiDung = ?
        """;
        try (Connection c = ConnectSQL.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, u.getHoTen());
            ps.setString(2, u.getEmail());
            // ps.setString(3, u.getMatKhau()); // XÓA DÒNG NÀY ĐỂ KHÔNG CẬP NHẬT MẬT KHẨU
            ps.setString(3, u.getSoDienThoai()); // Sửa lại chỉ số tham số
            ps.setString(4, u.getDiaChi());     // Sửa lại chỉ số tham số
            ps.setString(5, u.getVaiTro());     // Sửa lại chỉ số tham số
            ps.setInt   (6, u.getMaNguoiDung());// Sửa lại chỉ số tham số
            return ps.executeUpdate() > 0;
        }
    }

    // --- Các phương thức còn lại của bạn (giữ nguyên) ---

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
                    nguoiDung.setVaiTro(rs.getString("VaiTro"));
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
            return rowsAffected > 0;
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


    public boolean updateUserInfo(Model_NguoiDung u) throws SQLException {
        String sql = """
            UPDATE nguoidung
               SET HoTen       = ? ,
                   Email       = ? ,
                   SoDienThoai = ? ,
                   DiaChi      = ?
             WHERE MaNguoiDung = ?
        """;
        try (Connection c = ConnectSQL.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, u.getHoTen());
            ps.setString(2, u.getEmail());
            ps.setString(3, u.getSoDienThoai());
            ps.setString(4, u.getDiaChi());
            ps.setInt   (5, u.getMaNguoiDung());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean updatePassword(int maNguoiDung, String newPassword) throws SQLException {
        String sql = "UPDATE nguoidung SET MatKhau = ? WHERE MaNguoiDung = ?";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newPassword);
            ps.setInt(2, maNguoiDung);
            return ps.executeUpdate() > 0;
        }
    }
    public List<String[]> getLichSuDangNhap() throws SQLException {
        List<String[]> list = new ArrayList<>();
        String sql = """
            SELECT ls.ID, nd.HoTen, ls.ThoiGian, ls.DiaChiIP
            FROM   lichsu_dangnhap ls
                   JOIN nguoidung nd ON ls.MaNguoiDung = nd.MaNguoiDung
            ORDER  BY ls.ThoiGian DESC
        """;
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new String[]{
                    rs.getString("ID"),
                    rs.getString("HoTen"),
                    rs.getString("ThoiGian"),
                    rs.getString("DiaChiIP")
                });
            }
        }
        return list;
    }
    public boolean logLogin(int maNguoiDung, String ip) throws SQLException {
        String sql = "INSERT INTO lichsu_dangnhap(MaNguoiDung,DiaChiIP) VALUES(?,?)";
        try (Connection c = ConnectSQL.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt   (1, maNguoiDung);
            ps.setString(2, ip);
            return ps.executeUpdate() > 0;
        }
    }
}