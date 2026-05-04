package com.atm.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Lớp TaiKhoan (Base Class) - Tài khoản ngân hàng
 * Implement IAccount và ITransaction để định nghĩa các hành động chung
 * Tính chất: Kế thừa - Các class con như TaiKhoanTietKiem, TaiKhoanDangDo sẽ kế thừa từ lớp này
 */
public class TaiKhoan implements IAccount, ITransaction {
    private String soTK;            // Số tài khoản (ID)
    private double soDu;            // Số dư hiện tại
    private String trangThai;       // Trạng thái: "Hoat_Dong", "Khoa"
    private String maKH;            // Mã khách hàng sở hữu tài khoản
    private LocalDateTime ngayMo;   // Ngày mở tài khoản
    private List<GiaoDich> lichSuGiaoDich;  // Lịch sử giao dịch

    /**
     * Constructor không tham số
     */
    public TaiKhoan() {
        this.lichSuGiaoDich = new ArrayList<>();
        this.trangThai = "Hoat_Dong";
    }

    /**
     * Constructor có tham số
     */
    public TaiKhoan(String soTK, double soDu, String maKH) {
        this.soTK = soTK;
        this.soDu = soDu;
        this.maKH = maKH;
        this.trangThai = "Hoat_Dong";
        this.ngayMo = LocalDateTime.now();
        this.lichSuGiaoDich = new ArrayList<>();
    }

    // ============= GETTER & SETTER =============

    public String getSoTK() {
        return soTK;
    }

    public void setSoTK(String soTK) {
        this.soTK = soTK;
    }

    public double getSoDu() {
        return soDu;
    }

    public void setSoDu(double soDu) {
        this.soDu = soDu;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public String getMaKH() {
        return maKH;
    }

    public void setMaKH(String maKH) {
        this.maKH = maKH;
    }

    public LocalDateTime getNgayMo() {
        return ngayMo;
    }

    public void setNgayMo(LocalDateTime ngayMo) {
        this.ngayMo = ngayMo;
    }

    public List<GiaoDich> getLichSuGiaoDich() {
        return lichSuGiaoDich;
    }

    // ============= IMPLEMENT INTERFACE ITransaction =============

    /**
     * Rút tiền từ tài khoản
     * Thread-safe: Phương thức này sẽ được sử dụng với synchronized ở tầng Service
     */
    @Override
    public boolean rutTien(double soTien) {
        if (!isTrangThaiHoatDong()) {
            System.out.println("❌ Tài khoản đang bị khóa. Không thể rút tiền.");
            return false;
        }

        if (soTien <= 0) {
            System.out.println("❌ Số tiền rút phải lớn hơn 0.");
            return false;
        }

        if (this.soDu < soTien) {
            System.out.println("❌ Số dư không đủ. Số dư hiện tại: " + this.soDu);
            return false;
        }

        // Thực hiện rút tiền
        this.soDu -= soTien;
        System.out.println("✅ Rút tiền thành công. Số tiền rút: " + soTien + " VND");
        System.out.println("📊 Số dư còn lại: " + this.soDu + " VND");

        // Ghi lại giao dịch
        GiaoDich giaoDich = new GiaoDich();
        giaoDich.setLoaiGiaoDich("Rut_Tien");
        giaoDich.setSoTien(soTien);
        giaoDich.setThoiGian(LocalDateTime.now());
        giaoDich.setTrangThai("Thanh_Cong");
        giaoDich.setSoTK_LienKet(this.soTK);
        this.lichSuGiaoDich.add(giaoDich);

        return true;
    }

    /**
     * Nạp tiền vào tài khoản
     */
    @Override
    public boolean napTien(double soTien) {
        if (!isTrangThaiHoatDong()) {
            System.out.println("❌ Tài khoản đang bị khóa. Không thể nạp tiền.");
            return false;
        }

        if (soTien <= 0) {
            System.out.println("❌ Số tiền nạp phải lớn hơn 0.");
            return false;
        }

        // Thực hiện nạp tiền
        this.soDu += soTien;
        System.out.println("✅ Nạp tiền thành công. Số tiền nạp: " + soTien + " VND");
        System.out.println("📊 Số dư hiện tại: " + this.soDu + " VND");

        // Ghi lại giao dịch
        GiaoDich giaoDich = new GiaoDich();
        giaoDich.setLoaiGiaoDich("Nap_Tien");
        giaoDich.setSoTien(soTien);
        giaoDich.setThoiGian(LocalDateTime.now());
        giaoDich.setTrangThai("Thanh_Cong");
        giaoDich.setSoTK_LienKet(this.soTK);
        this.lichSuGiaoDich.add(giaoDich);

        return true;
    }

    /**
     * Kiểm tra số dư tài khoản
     */
    @Override
    public double kiemTraSoDu() {
        if (!isTrangThaiHoatDong()) {
            System.out.println("❌ Tài khoản bị khóa. Không thể kiểm tra số dư.");
            return -1;
        }
        System.out.println("📊 Số dư tài khoản " + this.soTK + ": " + this.soDu + " VND");
        return this.soDu;
    }

    /**
     * Hiển thị lịch sử giao dịch
     */
    @Override
    public String hienThiLichSu() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== LỊCH SỬ GIAO DỊCH TÀI KHOẢN ").append(this.soTK).append(" ===\n");

        if (this.lichSuGiaoDich.isEmpty()) {
            sb.append("Chưa có giao dịch nào.\n");
        } else {
            for (int i = 0; i < this.lichSuGiaoDich.size(); i++) {
                GiaoDich gd = this.lichSuGiaoDich.get(i);
                sb.append((i + 1)).append(". ").append(gd.toString()).append("\n");
            }
        }

        return sb.toString();
    }

    // ============= IMPLEMENT INTERFACE IAccount =============

    /**
     * Khóa tài khoản
     */
    @Override
    public void khoa() {
        this.trangThai = "Khoa";
        System.out.println("🔒 Tài khoản " + this.soTK + " đã bị khóa.");
    }

    /**
     * Mở khóa tài khoản
     */
    @Override
    public void moKhoa() {
        this.trangThai = "Hoat_Dong";
        System.out.println("🔓 Tài khoản " + this.soTK + " đã được mở khóa.");
    }

    /**
     * Kiểm tra trạng thái tài khoản
     */
    @Override
    public boolean isTrangThaiHoatDong() {
        return "Hoat_Dong".equals(this.trangThai);
    }

    // ============= PHƯƠNG THỨC CÔNG CỘNG =============

    @Override
    public String toString() {
        return "TaiKhoan{" +
                "soTK='" + soTK + '\'' +
                ", soDu=" + soDu +
                ", trangThai='" + trangThai + '\'' +
                ", maKH='" + maKH + '\'' +
                ", ngayMo=" + ngayMo +
                '}';
    }
}
