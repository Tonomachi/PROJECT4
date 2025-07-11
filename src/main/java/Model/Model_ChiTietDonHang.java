package Model;

public class Model_ChiTietDonHang {
    private int maChiTiet;
    private int maDonHang;
    private int maSanPham;
    private int soLuong;
    private double donGia; // Đơn giá tại thời điểm đặt hàng

    private String tenSanPham; // ✅ Tên sản phẩm (lấy từ bảng SanPham)

    // Constructors
    public Model_ChiTietDonHang() {
    }

    public Model_ChiTietDonHang(int maChiTiet, int maDonHang, int maSanPham, int soLuong, double donGia) {
        this.maChiTiet = maChiTiet;
        this.maDonHang = maDonHang;
        this.maSanPham = maSanPham;
        this.soLuong = soLuong;
        this.donGia = donGia;
    }

    // Getters
    public int getMaChiTiet() {
        return maChiTiet;
    }

    public int getMaDonHang() {
        return maDonHang;
    }

    public int getMaSanPham() {
        return maSanPham;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public double getDonGia() {
        return donGia;
    }

    public String getTenSanPham() {
        return tenSanPham;
    }

    // Setters
    public void setMaChiTiet(int maChiTiet) {
        this.maChiTiet = maChiTiet;
    }

    public void setMaDonHang(int maDonHang) {
        this.maDonHang = maDonHang;
    }

    public void setMaSanPham(int maSanPham) {
        this.maSanPham = maSanPham;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public void setDonGia(double donGia) {
        this.donGia = donGia;
    }

    public void setTenSanPham(String tenSanPham) {
        this.tenSanPham = tenSanPham;
    }
}
