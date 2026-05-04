package com.atm.entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Lớp GiaoDich đại diện cho một giao dịch trên máy ATM
 * Dùng để ghi lại lịch sử các giao dịch
 */
public class GiaoDich {
    private String maGD;            // Mã giao dịch (ID)
    private String loaiGiaoDich;    // Loại giao dịch: "Rut_Tien", "Nap_Tien", "Chuyen_Khoan", etc.
    private double soTien;          // Số tiền giao dịch
    private LocalDateTime thoiGian; // Thời gian giao dịch
    private String trangThai;       // Trạng thái: "Thanh_Cong", "That_Bai"
    private String soTK_LienKet;    // Số tài khoản liên kết

    /**
     * Constructor không tham số
     */
    public GiaoDich() {
        this.trangThai = "Thanh_Cong";
        this.thoiGian = LocalDateTime.now();
    }

    /**
     * Constructor có tham số
     */
    public GiaoDich(String maGD, String loaiGiaoDich, double soTien, LocalDateTime thoiGian, String trangThai, String soTK_LienKet) {
        this.maGD = maGD;
        this.loaiGiaoDich = loaiGiaoDich;
        this.soTien = soTien;
        this.thoiGian = thoiGian;
        this.trangThai = trangThai;
        this.soTK_LienKet = soTK_LienKet;
    }

    // ============= GETTER & SETTER =============

    public String getMaGD() {
        return maGD;
    }

    public void setMaGD(String maGD) {
        this.maGD = maGD;
    }

    public String getLoaiGiaoDich() {
        return loaiGiaoDich;
    }

    public void setLoaiGiaoDich(String loaiGiaoDich) {
        this.loaiGiaoDich = loaiGiaoDich;
    }

    public double getSoTien() {
        return soTien;
    }

    public void setSoTien(double soTien) {
        this.soTien = soTien;
    }

    public LocalDateTime getThoiGian() {
        return thoiGian;
    }

    public void setThoiGian(LocalDateTime thoiGian) {
        this.thoiGian = thoiGian;
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

    // ============= PHƯƠNG THỨC CÔNG CỘNG =============

    /**
     * Phương thức tạo mã giao dịch duy nhất
     * Dựa trên timestamp và random
     */
    public static String taoMaGiaoDich() {
        long timestamp = System.currentTimeMillis();
        int random = (int) (Math.random() * 1000);
        return "GD" + timestamp + random;
    }

    /**
     * Hiển thị chi tiết giao dịch theo format
     */
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return String.format("[%s] | Loại: %-15s | Số tiền: %,10.0f VND | Trạng thái: %s | TK: %s | Lúc: %s",
                maGD != null ? maGD : "N/A",
                loaiGiaoDich,
                soTien,
                trangThai,
                soTK_LienKet,
                thoiGian.format(formatter)
        );
    }
}
