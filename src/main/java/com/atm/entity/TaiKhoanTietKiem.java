package com.atm.entity;

import java.time.LocalDateTime;

/**
 * Lớp TaiKhoanTietKiem kế thừa từ TaiKhoan
 * Tài khoản tiết kiệm - Có lãi suất, hạn rút tối thiểu
 * Tính chất: Kế thừa - Mở rộng chức năng của TaiKhoan cha
 */
public class TaiKhoanTietKiem extends TaiKhoan {
    private double laiSuat;           // Lãi suất (%/năm)
    private int hanRutToiThieu;       // Hạn rút tiền tối thiểu (ngày)
    private LocalDateTime ngayRutCuoi; // Ngày rút tiền lần cuối

    /**
     * Constructor không tham số
     */
    public TaiKhoanTietKiem() {
        super();
        this.laiSuat = 0.05;  // Mặc định 5% lãi suất/năm
        this.hanRutToiThieu = 30; // Mặc định 30 ngày
    }

    /**
     * Constructor có tham số
     */
    public TaiKhoanTietKiem(String soTK, double soDu, String maKH, double laiSuat, int hanRutToiThieu) {
        super(soTK, soDu, maKH);
        this.laiSuat = laiSuat;
        this.hanRutToiThieu = hanRutToiThieu;
        this.ngayRutCuoi = LocalDateTime.now();
    }

    // ============= GETTER & SETTER =============

    public double getLaiSuat() {
        return laiSuat;
    }

    public void setLaiSuat(double laiSuat) {
        this.laiSuat = laiSuat;
    }

    public int getHanRutToiThieu() {
        return hanRutToiThieu;
    }

    public void setHanRutToiThieu(int hanRutToiThieu) {
        this.hanRutToiThieu = hanRutToiThieu;
    }

    public LocalDateTime getNgayRutCuoi() {
        return ngayRutCuoi;
    }

    public void setNgayRutCuoi(LocalDateTime ngayRutCuoi) {
        this.ngayRutCuoi = ngayRutCuoi;
    }

    // ============= PHƯƠNG THỨC ĐẶC THỤ CỦA CLASS =============

    /**
     * Tính tiền lãi cho tài khoản tiết kiệm
     * Lãi suất được tính dựa trên số dư và thời gian gữi
     */
    public double tinhTienLai(int soNgayGui) {
        double tienLai = this.getSoDu() * this.laiSuat * (soNgayGui / 365.0);
        System.out.println("💰 Tiền lãi cho " + soNgayGui + " ngày: " + tienLai + " VND");
        return tienLai;
    }

    /**
     * Cộng tiền lãi vào tài khoản
     */
    public void congTienLai(double tienLai) {
        this.setSoDu(this.getSoDu() + tienLai);
        System.out.println("✅ Đã cộng tiền lãi: " + tienLai + " VND");
        System.out.println("📊 Số dư mới: " + this.getSoDu() + " VND");
    }

    /**
     * Override phương thức rutTien - Kiểm tra hạn rút tối thiểu
     */
    @Override
    public boolean rutTien(double soTien) {
        // Kiểm tra hạn rút tối thiểu
        LocalDateTime now = LocalDateTime.now();
        long ngayTroiQua = java.time.temporal.ChronoUnit.DAYS.between(this.ngayRutCuoi, now);

        if (ngayTroiQua < this.hanRutToiThieu) {
            System.out.println("❌ Tài khoản tiết kiệm phải gữi tối thiểu " + this.hanRutToiThieu + " ngày.");
            System.out.println("   Còn lại " + (this.hanRutToiThieu - ngayTroiQua) + " ngày nữa mới được rút.");
            return false;
        }

        // Nếu vượt quá hạn, tiền lãi sẽ được cộng thêm
        boolean ketQua = super.rutTien(soTien);
        if (ketQua) {
            this.ngayRutCuoi = now;
        }
        return ketQua;
    }

    @Override
    public String toString() {
        return "TaiKhoanTietKiem{" +
                "soTK='" + this.getSoTK() + '\'' +
                ", soDu=" + this.getSoDu() +
                ", trangThai='" + this.getTrangThai() + '\'' +
                ", laiSuat=" + laiSuat + "%" +
                ", hanRutToiThieu=" + hanRutToiThieu + " ngày" +
                '}';
    }
}
