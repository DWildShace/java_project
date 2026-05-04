package com.atm.entity;

import java.time.LocalDateTime;

/**
 * Lớp TheATM đại diện cho thẻ ATM vật lý
 * Quản lý định danh thẻ, mã PIN, và trạng thái khóa
 */
public class TheATM {
    private String soThe;           // Số thẻ ATM (16 chữ số)
    private String maPIN;           // Mã PIN (4 chữ số)
    private String trangThai;       // Trạng thái: "Hoat_Dong", "Khoa", "Het_Han"
    private String soTK_LienKet;    // Số tài khoản liên kết
    private int soLanNhapSai;       // Số lần nhập PIN sai liên tiếp
    private int hanNhapSaiToiDa;    // Hạn số lần nhập sai tối đa (mặc định 3)
    private LocalDateTime ngayCapThe; // Ngày cấp thẻ
    private LocalDateTime ngayHetHan; // Ngày hết hạn thẻ

    /**
     * Constructor không tham số
     */
    public TheATM() {
        this.soLanNhapSai = 0;
        this.hanNhapSaiToiDa = 3;
        this.trangThai = "Hoat_Dong";
    }

    /**
     * Constructor có tham số
     */
    public TheATM(String soThe, String maPIN, String soTK_LienKet) {
        this.soThe = soThe;
        this.maPIN = maPIN;
        this.soTK_LienKet = soTK_LienKet;
        this.soLanNhapSai = 0;
        this.hanNhapSaiToiDa = 3;
        this.trangThai = "Hoat_Dong";
        this.ngayCapThe = LocalDateTime.now();
        this.ngayHetHan = LocalDateTime.now().plusYears(5); // Thẻ có hạn 5 năm
    }

    // ============= GETTER & SETTER =============

    public String getSoThe() {
        return soThe;
    }

    public void setSoThe(String soThe) {
        this.soThe = soThe;
    }

    public String getMaPIN() {
        return maPIN;
    }

    public void setMaPIN(String maPIN) {
        this.maPIN = maPIN;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public String getSoTK_LienKet() {
        return soTK_LienKet;
    }

    public void setSoTK_LienKet(String soTK_LienKet) {
        this.soTK_LienKet = soTK_LienKet;
    }

    public int getSoLanNhapSai() {
        return soLanNhapSai;
    }

    public void setSoLanNhapSai(int soLanNhapSai) {
        this.soLanNhapSai = soLanNhapSai;
    }

    public int getHanNhapSaiToiDa() {
        return hanNhapSaiToiDa;
    }

    public void setHanNhapSaiToiDa(int hanNhapSaiToiDa) {
        this.hanNhapSaiToiDa = hanNhapSaiToiDa;
    }

    public LocalDateTime getNgayCapThe() {
        return ngayCapThe;
    }

    public void setNgayCapThe(LocalDateTime ngayCapThe) {
        this.ngayCapThe = ngayCapThe;
    }

    public LocalDateTime getNgayHetHan() {
        return ngayHetHan;
    }

    public void setNgayHetHan(LocalDateTime ngayHetHan) {
        this.ngayHetHan = ngayHetHan;
    }

    // ============= PHƯƠNG THỨC CÔNG CỘNG =============

    /**
     * Xác thực PIN - Kiểm tra xem PIN nhập vào có đúng không
     * Nếu sai, tăng bộ đếm nhập sai. Nếu vượt quá 3 lần, khóa thẻ
     */
    public boolean xacThucPIN(String pinNhap) {
        // Kiểm tra xem thẻ có bị khóa không
        if ("Khoa".equals(this.trangThai)) {
            System.out.println("❌ Thẻ này đã bị khóa do nhập PIN sai quá nhiều lần.");
            return false;
        }

        // Kiểm tra xem thẻ có hết hạn không
        if (LocalDateTime.now().isAfter(this.ngayHetHan)) {
            System.out.println("❌ Thẻ này đã hết hạn.");
            this.trangThai = "Het_Han";
            return false;
        }

        // Kiểm tra PIN
        if (this.maPIN.equals(pinNhap)) {
            System.out.println("✅ Xác thực PIN thành công!");
            this.soLanNhapSai = 0; // Reset bộ đếm khi nhập đúng
            return true;
        } else {
            this.soLanNhapSai++;
            System.out.println("❌ PIN sai. Lần nhập sai thứ " + this.soLanNhapSai + "/" + this.hanNhapSaiToiDa);

            // Khóa thẻ nếu nhập sai quá 3 lần
            if (this.soLanNhapSai >= this.hanNhapSaiToiDa) {
                System.out.println("🔒 Thẻ đã bị khóa do nhập PIN sai quá " + this.hanNhapSaiToiDa + " lần!");
                this.trangThai = "Khoa";
            }

            return false;
        }
    }

    /**
     * Mở khóa thẻ (cần quyền admin)
     */
    public void moKhoaThe() {
        this.trangThai = "Hoat_Dong";
        this.soLanNhapSai = 0;
        System.out.println("🔓 Thẻ đã được mở khóa.");
    }

    /**
     * Kiểm tra xem thẻ có đang hoạt động không
     */
    public boolean isTheHoatDong() {
        if ("Hoat_Dong".equals(this.trangThai) && LocalDateTime.now().isBefore(this.ngayHetHan)) {
            return true;
        }
        return false;
    }

    /**
     * Hiển thị thông tin thẻ
     */
    @Override
    public String toString() {
        return "TheATM{" +
                "soThe='" + soThe + '\'' +
                ", trangThai='" + trangThai + '\'' +
                ", soTK_LienKet='" + soTK_LienKet + '\'' +
                ", soLanNhapSai=" + soLanNhapSai + "/" + hanNhapSaiToiDa +
                ", ngayCapThe=" + ngayCapThe +
                ", ngayHetHan=" + ngayHetHan +
                '}';
    }
}
