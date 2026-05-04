package com.atm.entity;

/**
 * Lớp KhachHang đại diện cho khách hàng ngân hàng
 * Chứa thông tin cơ bản của khách hàng
 */
public class KhachHang {
    private String maKH;           // Mã khách hàng (ID)
    private String hoTen;          // Họ và tên
    private String soCCCD;         // Số chứng chỉ căn cước
    private String soDienThoai;    // Số điện thoại
    private String email;          // Email

    /**
     * Constructor không tham số (mặc định)
     */
    public KhachHang() {
    }

    /**
     * Constructor đầy đủ tham số
     */
    public KhachHang(String maKH, String hoTen, String soCCCD, String soDienThoai, String email) {
        this.maKH = maKH;
        this.hoTen = hoTen;
        this.soCCCD = soCCCD;
        this.soDienThoai = soDienThoai;
        this.email = email;
    }

    // ============= GETTER & SETTER =============

    public String getMaKH() {
        return maKH;
    }

    public void setMaKH(String maKH) {
        this.maKH = maKH;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getSoCCCD() {
        return soCCCD;
    }

    public void setSoCCCD(String soCCCD) {
        this.soCCCD = soCCCD;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // ============= PHƯƠNG THỨC CÔNG CỘNG =============

    /**
     * Hiển thị thông tin khách hàng
     */
    @Override
    public String toString() {
        return "KhachHang{" +
                "maKH='" + maKH + '\'' +
                ", hoTen='" + hoTen + '\'' +
                ", soCCCD='" + soCCCD + '\'' +
                ", soDienThoai='" + soDienThoai + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
