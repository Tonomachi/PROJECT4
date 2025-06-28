package Model;

import java.time.LocalDateTime;

public class Model_DonHang {
    private int maDonHang;
    private int maNguoiDung;
    private double tongTien;
    private String trangThai;
    private LocalDateTime ngayDat;
    private String diaChiGiao;     // ✅ sửa tên cho trùng DB
    private String ghiChu;

    // Constructors
    public Model_DonHang() {}

    public Model_DonHang(int maDonHang, int maNguoiDung, double tongTien, String trangThai,
                         LocalDateTime ngayDat, String diaChiGiao, String ghiChu) {
        this.maDonHang = maDonHang;
        this.maNguoiDung = maNguoiDung;
        this.tongTien = tongTien;
        this.trangThai = trangThai;
        this.ngayDat = ngayDat;
        this.diaChiGiao = diaChiGiao;
        this.ghiChu = ghiChu;
    }

    // Getters
    public int getMaDonHang() { return maDonHang; }
    public int getMaNguoiDung() { return maNguoiDung; }
    public double getTongTien() { return tongTien; }
    public String getTrangThai() { return trangThai; }
    public LocalDateTime getNgayDat() { return ngayDat; }
    public String getDiaChiGiao() { return diaChiGiao; }  // ✅ đổi tên getter
    public String getGhiChu() { return ghiChu; }

    // Setters
    public void setMaDonHang(int maDonHang) { this.maDonHang = maDonHang; }
    public void setMaNguoiDung(int maNguoiDung) { this.maNguoiDung = maNguoiDung; }
    public void setTongTien(double tongTien) { this.tongTien = tongTien; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
    public void setNgayDat(LocalDateTime ngayDat) { this.ngayDat = ngayDat; }
    public void setDiaChiGiao(String diaChiGiao) { this.diaChiGiao = diaChiGiao; } // ✅ đổi tên setter
    public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu; }
}
