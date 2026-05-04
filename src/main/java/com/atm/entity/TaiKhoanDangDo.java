package com.atm.entity;

/**
 * Lớp TaiKhoanDangDo kế thừa từ TaiKhoan
 * Tài khoản đang dùng - Rút tiền tự do, không có hạn chế thời gian
 * Tính chất: Kế thừa - Mở rộng chức năng của TaiKhoan cha
 */
public class TaiKhoanDangDo extends TaiKhoan {
    private double phiBaoHanh;      // Phí bảo hành hàng tháng
    private int soLanRutTrongThang; // Số lần rút trong tháng
    private int hanSoLanRutToiDa;   // Hạn số lần rút tối đa trong tháng

    /**
     * Constructor không tham số
     */
    public TaiKhoanDangDo() {
        super();
        this.phiBaoHanh = 5000;    // Mặc định 5000 VND/tháng
        this.hanSoLanRutToiDa = 10; // Mặc định 10 lần/tháng
    }

    /**
     * Constructor có tham số
     */
    public TaiKhoanDangDo(String soTK, double soDu, String maKH, double phiBaoHanh, int hanSoLanRutToiDa) {
        super(soTK, soDu, maKH);
        this.phiBaoHanh = phiBaoHanh;
        this.hanSoLanRutToiDa = hanSoLanRutToiDa;
        this.soLanRutTrongThang = 0;
    }

    // ============= GETTER & SETTER =============

    public double getPhiBaoHanh() {
        return phiBaoHanh;
    }

    public void setPhiBaoHanh(double phiBaoHanh) {
        this.phiBaoHanh = phiBaoHanh;
    }

    public int getSoLanRutTrongThang() {
        return soLanRutTrongThang;
    }

    public void setSoLanRutTrongThang(int soLanRutTrongThang) {
        this.soLanRutTrongThang = soLanRutTrongThang;
    }

    public int getHanSoLanRutToiDa() {
        return hanSoLanRutToiDa;
    }

    public void setHanSoLanRutToiDa(int hanSoLanRutToiDa) {
        this.hanSoLanRutToiDa = hanSoLanRutToiDa;
    }

    // ============= PHƯƠNG THỨC ĐẶC THỤ CỦA CLASS =============

    /**
     * Trừ phí bảo hành hàng tháng
     */
    public void truPhiBaoHanh() {
        if (this.getSoDu() >= this.phiBaoHanh) {
            double soDuMoi = this.getSoDu() - this.phiBaoHanh;
            this.setSoDu(soDuMoi);
            System.out.println("💳 Đã trừ phí bảo hành: " + this.phiBaoHanh + " VND");
            System.out.println("📊 Số dư còn lại: " + this.getSoDu() + " VND");
        } else {
            System.out.println("❌ Số dư không đủ để trừ phí bảo hành.");
        }
    }

    /**
     * Reset lại số lần rút (sau mỗi tháng)
     */
    public void resetSoLanRutThang() {
        this.soLanRutTrongThang = 0;
        System.out.println("🔄 Đã reset số lần rút trong tháng.");
    }

    /**
     * Override phương thức rutTien - Kiểm tra số lần rút tối đa
     */
    @Override
    public boolean rutTien(double soTien) {
        // Kiểm tra số lần rút trong tháng
        if (this.soLanRutTrongThang >= this.hanSoLanRutToiDa) {
            System.out.println("❌ Đã vượt quá số lần rút tối đa (" + this.hanSoLanRutToiDa + " lần/tháng).");
            return false;
        }

        // Thực hiện rút tiền (gọi phương thức cha)
        boolean ketQua = super.rutTien(soTien);

        // Nếu rút tiền thành công, cập nhật số lần rút
        if (ketQua) {
            this.soLanRutTrongThang++;
            System.out.println("📝 Số lần rút trong tháng: " + this.soLanRutTrongThang + "/" + this.hanSoLanRutToiDa);
        }

        return ketQua;
    }

    @Override
    public String toString() {
        return "TaiKhoanDangDo{" +
                "soTK='" + this.getSoTK() + '\'' +
                ", soDu=" + this.getSoDu() +
                ", trangThai='" + this.getTrangThai() + '\'' +
                ", phiBaoHanh=" + phiBaoHanh + " VND" +
                ", soLanRutTrongThang=" + soLanRutTrongThang + "/" + hanSoLanRutToiDa +
                '}';
    }
}
