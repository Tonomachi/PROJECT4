package Model;

import java.sql.Timestamp;

public class Model_NguoiDung {
    private int maNguoiDung;
    private String hoTen;
    private String email;
    private String matKhau;
    private String soDienThoai;
    private String diaChi;
    private String vaiTro;
    private Timestamp ngayTao;

    public Model_NguoiDung() { }

    public Model_NguoiDung(int maNguoiDung, String hoTen, String email, String matKhau,
                           String soDienThoai, String diaChi, String vaiTro, Timestamp ngayTao) {
        this.maNguoiDung = maNguoiDung;
        this.hoTen = hoTen;
        this.email = email;
        this.matKhau = matKhau;
        this.soDienThoai = soDienThoai;
        this.diaChi = diaChi;
        this.vaiTro = vaiTro;
        this.ngayTao = ngayTao;
    }

    public Model_NguoiDung(String email, String matKhau) {
        this.email = email;
        this.matKhau = matKhau;
    }

    public Model_NguoiDung(String hoTen, String email, String matKhau, String soDienThoai, String diaChi) {
        this.hoTen = hoTen;
        this.email = email;
        this.matKhau = matKhau;
        this.soDienThoai = soDienThoai;
        this.diaChi = diaChi;
    }

    // Getters and Setters
    public int getMaNguoiDung() { return maNguoiDung; }
    public void setMaNguoiDung(int maNguoiDung) { this.maNguoiDung = maNguoiDung; }

    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getMatKhau() { return matKhau; }
    public void setMatKhau(String matKhau) { this.matKhau = matKhau; }

    public String getSoDienThoai() { return soDienThoai; }
    public void setSoDienThoai(String soDienThoai) { this.soDienThoai = soDienThoai; }

    public String getDiaChi() { return diaChi; }
    public void setDiaChi(String diaChi) { this.diaChi = diaChi; }

    public String getVaiTro() { return vaiTro; }
    public void setVaiTro(String vaiTro) { this.vaiTro = vaiTro; }

    public Timestamp getNgayTao() { return ngayTao; }
    public void setNgayTao(Timestamp ngayTao) { this.ngayTao = ngayTao; }
}