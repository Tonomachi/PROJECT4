package Data;

import Model.Model_DanhMuc;
import Connect.ConnectSQL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DanhMuc {

    public void insertDanhMuc(Model_DanhMuc danhMuc) throws SQLException {
        String sql = "INSERT INTO danhmuc (TenDanhMuc, MoTa) VALUES (?, ?)";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, danhMuc.getTenDanhMuc());
            ps.setString(2, danhMuc.getMoTa());
            ps.executeUpdate();
        }
    }

    public List<Model_DanhMuc> getAllDanhMuc() throws SQLException {
        List<Model_DanhMuc> list = new ArrayList<>();
        String sql = "SELECT MaDanhMuc, TenDanhMuc, MoTa FROM danhmuc ORDER BY TenDanhMuc ASC";

        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Model_DanhMuc dm = new Model_DanhMuc();
                dm.setMaDanhMuc(rs.getInt("MaDanhMuc"));
                dm.setTenDanhMuc(rs.getString("TenDanhMuc"));
                dm.setMoTa(rs.getString("MoTa"));
                list.add(dm);
            }
        }
        return list;
    }

    public Model_DanhMuc getDanhMucById(int id) throws SQLException {
        String sql = "SELECT MaDanhMuc, TenDanhMuc, MoTa FROM danhmuc WHERE MaDanhMuc = ?";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Model_DanhMuc(
                        rs.getInt("MaDanhMuc"),
                        rs.getString("TenDanhMuc"),
                        rs.getString("MoTa")
                    );
                }
            }
        }
        return null;
    }

    public void updateDanhMuc(Model_DanhMuc danhMuc) throws SQLException {
        String sql = "UPDATE danhmuc SET TenDanhMuc = ?, MoTa = ? WHERE MaDanhMuc = ?";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, danhMuc.getTenDanhMuc());
            ps.setString(2, danhMuc.getMoTa());
            ps.setInt(3, danhMuc.getMaDanhMuc());
            ps.executeUpdate();
        }
    }

    public void deleteDanhMuc(int maDanhMuc) throws SQLException {
        String sql = "DELETE FROM danhmuc WHERE MaDanhMuc = ?";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maDanhMuc);
            ps.executeUpdate();
        }
    }
}