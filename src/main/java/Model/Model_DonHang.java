package Model;

import java.time.LocalDateTime;

public class Model_DonHang {
    private int maDonHang;
    private int maNguoiDung;
    private double tongTien;
    private String trangThai;
    private LocalDateTime ngayDat;
    private String diaChiGiao;
    private String ghiChu;

    // Thông tin người dùng (JOIN từ bảng nguoidung)
    private String hoTenNguoiDung;
    private String soDienThoaiNguoiDung;

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
    public int getMaDonHang() {
        return maDonHang;
    }

    public int getMaNguoiDung() {
        return maNguoiDung;
    }

    public double getTongTien() {
        return tongTien;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public LocalDateTime getNgayDat() {
        return ngayDat;
    }

    public String getDiaChiGiao() {
        return diaChiGiao;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public String getHoTenNguoiDung() {
        return hoTenNguoiDung;
    }

    public String getSoDienThoaiNguoiDung() {
        return soDienThoaiNguoiDung;
    }

    // Setters
    public void setMaDonHang(int maDonHang) {
        this.maDonHang = maDonHang;
    }

    public void setMaNguoiDung(int maNguoiDung) {
        this.maNguoiDung = maNguoiDung;
    }

    public void setTongTien(double tongTien) {
        this.tongTien = tongTien;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public void setNgayDat(LocalDateTime ngayDat) {
        this.ngayDat = ngayDat;
    }

    public void setDiaChiGiao(String diaChiGiao) {
        this.diaChiGiao = diaChiGiao;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    public void setHoTenNguoiDung(String hoTenNguoiDung) {
        this.hoTenNguoiDung = hoTenNguoiDung;
    }

    public void setSoDienThoaiNguoiDung(String soDienThoaiNguoiDung) {
        this.soDienThoaiNguoiDung = soDienThoaiNguoiDung;
    }
}
