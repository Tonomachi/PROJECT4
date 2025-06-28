package Model;

import java.time.LocalDateTime;

public class Model_GioHang {
    private int maGioHang;
    private int maNguoiDung;
    private int maSanPham;
    private int soLuong;
    private LocalDateTime ngayThem;

    private Model_SanPham sanPham; // Đối tượng sản phẩm tương ứng

    public Model_GioHang() {}

    // Dùng khi chỉ cần hiện sản phẩm và số lượng trong JSP
    public Model_GioHang(Model_SanPham sanPham, int soLuong) {
        this.sanPham = sanPham;
        this.soLuong = soLuong;
        this.maSanPham = sanPham.getMaSanPham();
    }

    public Model_GioHang(int maGioHang, int maNguoiDung, int maSanPham, int soLuong, LocalDateTime ngayThem) {
        this.maGioHang = maGioHang;
        this.maNguoiDung = maNguoiDung;
        this.maSanPham = maSanPham;
        this.soLuong = soLuong;
        this.ngayThem = ngayThem;
    }

    // Getters
    public int getMaGioHang() {
        return maGioHang;
    }

    public int getMaNguoiDung() {
        return maNguoiDung;
    }

    public int getMaSanPham() {
        return maSanPham;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public LocalDateTime getNgayThem() {
        return ngayThem;
    }

    public Model_SanPham getSanPham() {
        return sanPham;
    }

    // Setters
    public void setMaGioHang(int maGioHang) {
        this.maGioHang = maGioHang;
    }

    public void setMaNguoiDung(int maNguoiDung) {
        this.maNguoiDung = maNguoiDung;
    }

    public void setMaSanPham(int maSanPham) {
        this.maSanPham = maSanPham;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public void setNgayThem(LocalDateTime ngayThem) {
        this.ngayThem = ngayThem;
    }

    public void setSanPham(Model_SanPham sanPham) {
        this.sanPham = sanPham;
        this.maSanPham = sanPham.getMaSanPham(); // đảm bảo đồng bộ
    }
    
}
