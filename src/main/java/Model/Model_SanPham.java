package Model;

import java.math.BigDecimal;
// Đã xóa import java.sql.Timestamp;

public class Model_SanPham {
    private int maSanPham;
    private String tenSanPham;
    private String moTa;
    private BigDecimal gia;
    private String hinhAnh;
    private int maDanhMuc;
    private int soLuongTonKho; // Đã thêm lại SoLuongTonKho dựa trên cấu trúc DB của bạn

    public Model_SanPham() {
    }

    // Constructor khi thêm mới
    public Model_SanPham(String tenSanPham, String moTa, BigDecimal gia, int soLuongTonKho, String hinhAnh, int maDanhMuc) {
        this.tenSanPham = tenSanPham;
        this.moTa = moTa;
        this.gia = gia;
        this.soLuongTonKho = soLuongTonKho;
        this.hinhAnh = hinhAnh;
        this.maDanhMuc = maDanhMuc;
    }

    // Constructor khi đọc từ DB hoặc cập nhật
    public Model_SanPham(int maSanPham, String tenSanPham, String moTa, BigDecimal gia, int soLuongTonKho, String hinhAnh, int maDanhMuc) {
        this.maSanPham = maSanPham;
        this.tenSanPham = tenSanPham;
        this.moTa = moTa;
        this.gia = gia;
        this.soLuongTonKho = soLuongTonKho;
        this.hinhAnh = hinhAnh;
        this.maDanhMuc = maDanhMuc;
    }

    // Getters and Setters
    public int getMaSanPham() {
        return maSanPham;
    }

    public void setMaSanPham(int maSanPham) {
        this.maSanPham = maSanPham;
    }

    public String getTenSanPham() {
        return tenSanPham;
    }

    public void setTenSanPham(String tenSanPham) {
        this.tenSanPham = tenSanPham;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public BigDecimal getGia() {
        return gia;
    }

    public void setGia(BigDecimal gia) {
        this.gia = gia;
    }
    
    public int getSoLuongTonKho() {
        return soLuongTonKho;
    }

    public void setSoLuongTonKho(int soLuongTonKho) {
        this.soLuongTonKho = soLuongTonKho;
    }

    public String getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
    }

    public int getMaDanhMuc() {
        return maDanhMuc;
    }

    public void setMaDanhMuc(int maDanhMuc) {
        this.maDanhMuc = maDanhMuc;
    }
    
    // Đã xóa NgayTao và NgayCapNhat getters/setters
}