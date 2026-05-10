package com.atm;

import com.atm.entity.TaiKhoan;
import com.atm.service.ATMService;
import com.atm.util.Utils;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

/**
 * Lop nay dung de demo nhanh tren console:
 * - cac truong hop giao dich thanh cong
 * - toan bo cac truong hop loi/ngoai le da duoc xu ly trong ATMService
 */
public class DemoConsole {
    private static class DemoContext {
        private final ATMService atmService;
        private final TaiKhoan taiKhoanNguon;
        private final TaiKhoan taiKhoanDich;

        private DemoContext(ATMService atmService, TaiKhoan taiKhoanNguon, TaiKhoan taiKhoanDich) {
            this.atmService = atmService;
            this.taiKhoanNguon = taiKhoanNguon;
            this.taiKhoanDich = taiKhoanDich;
        }
    }

    public static void main(String[] args) {
        cauHinhConsoleUTF8();

        System.out.println("====================================");
        System.out.println(" DEMO CONSOLE - KIEM TRA NGOAI LE ATM");
        System.out.println("====================================");

        demoDangNhapThanhCong();
        demoRutTienThanhCong();
        demoChuyenTienThanhCong();
        demoNapTienThanhCong();
        demoDoiPinThanhCong();

        demoRutTienAm();
        demoRutTienBangKhong();
        demoNhapChuThayViSo();
        demoRutVuotSoDu();
        demoSaiPin3Lan();
        demoRutTienKhiTaiKhoanBiKhoa();
        demoChuyenKhoanDenTaiKhoanKhongTonTai();
        demoRutVuotHanMuc();
        demoDoiPinSaiDinhDang();
        demoDoiPinXacNhanKhongKhop();
    }

    private static void cauHinhConsoleUTF8() {
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));
        System.setErr(new PrintStream(System.err, true, StandardCharsets.UTF_8));
    }

    /**
     * Tao moi du lieu demo cho tung scenario de cac case doc lap voi nhau.
     */
    private static DemoContext taoDemoContextMacDinh() {
        ATMService atmService = new ATMService();
        TaiKhoan taiKhoanNguon = taoTaiKhoan("TK001", 10_000_000, "123456");
        TaiKhoan taiKhoanDich = taoTaiKhoan("TK002", 3_000_000, "654321");
        atmService.themTaiKhoan(taiKhoanNguon);
        atmService.themTaiKhoan(taiKhoanDich);
        return new DemoContext(atmService, taiKhoanNguon, taiKhoanDich);
    }

    private static TaiKhoan taoTaiKhoan(String soTK, double soDu, String pin) {
        return new TaiKhoan(soTK, soDu, "Hoat dong", "KH" + soTK, pin, LocalDateTime.now());
    }

    private static void demoDangNhapThanhCong() {
        DemoContext context = taoDemoContextMacDinh();
        inTieuDe("Dang nhap thanh cong");
        System.out.println(context.atmService.dangNhapATM(context.taiKhoanNguon, "123456"));
    }

    private static void demoRutTienThanhCong() {
        DemoContext context = taoDemoContextMacDinh();
        inTieuDe("Rut tien thanh cong");
        System.out.println(context.atmService.rutTien(context.taiKhoanNguon, "123456", "500000"));
        System.out.println("So du con lai: " + Utils.formatCurrency(context.taiKhoanNguon.getSoDu()));
    }

    private static void demoChuyenTienThanhCong() {
        DemoContext context = taoDemoContextMacDinh();
        inTieuDe("Chuyen tien thanh cong");
        System.out.println(context.atmService.chuyenTien(context.taiKhoanNguon, "123456", "1000000", "TK002"));
        System.out.println("So du TK nguon: " + Utils.formatCurrency(context.taiKhoanNguon.getSoDu()));
        System.out.println("So du TK dich: " + Utils.formatCurrency(context.taiKhoanDich.getSoDu()));
    }

    private static void demoNapTienThanhCong() {
        DemoContext context = taoDemoContextMacDinh();
        inTieuDe("Nap tien thanh cong");
        System.out.println(context.atmService.napTien(context.taiKhoanNguon, "250000"));
        System.out.println("So du sau khi nap: " + Utils.formatCurrency(context.taiKhoanNguon.getSoDu()));
    }

    private static void demoDoiPinThanhCong() {
        DemoContext context = taoDemoContextMacDinh();
        inTieuDe("Doi PIN thanh cong");
        System.out.println(context.atmService.doiPIN(context.taiKhoanNguon, "123456", "111111", "111111"));
        System.out.println("PIN moi hien tai: " + context.taiKhoanNguon.getMaPin());
    }

    private static void demoRutTienAm() {
        DemoContext context = taoDemoContextMacDinh();
        inTieuDe("Loi rut tien am");
        System.out.println(context.atmService.rutTien(context.taiKhoanNguon, "123456", "-1000"));
    }

    private static void demoRutTienBangKhong() {
        DemoContext context = taoDemoContextMacDinh();
        inTieuDe("Loi rut tien bang 0");
        System.out.println(context.atmService.rutTien(context.taiKhoanNguon, "123456", "0"));
    }

    private static void demoNhapChuThayViSo() {
        DemoContext context = taoDemoContextMacDinh();
        inTieuDe("Loi nhap chu thay vi so");
        System.out.println(context.atmService.napTien(context.taiKhoanNguon, "mottrieu"));
    }

    private static void demoRutVuotSoDu() {
        DemoContext context = taoDemoContextMacDinh();
        context.taiKhoanNguon.setSoDu(2_000_000);
        inTieuDe("Loi rut vuot so du");
        System.out.println(context.atmService.rutTien(context.taiKhoanNguon, "123456", "5000000"));
    }

    private static void demoSaiPin3Lan() {
        DemoContext context = taoDemoContextMacDinh();
        inTieuDe("Loi sai PIN 3 lan");
        System.out.println("Lan 1: " + context.atmService.dangNhapATM(context.taiKhoanNguon, "000000"));
        System.out.println("Lan 2: " + context.atmService.dangNhapATM(context.taiKhoanNguon, "000000"));
        System.out.println("Lan 3: " + context.atmService.dangNhapATM(context.taiKhoanNguon, "000000"));
        System.out.println("Trang thai khoa: " + context.taiKhoanNguon.isBiKhoa());
    }

    private static void demoRutTienKhiTaiKhoanBiKhoa() {
        DemoContext context = taoDemoContextMacDinh();
        context.taiKhoanNguon.khoaTaiKhoan();
        inTieuDe("Loi rut tien khi tai khoan da bi khoa");
        System.out.println(context.atmService.rutTien(context.taiKhoanNguon, "123456", "100000"));
    }

    private static void demoChuyenKhoanDenTaiKhoanKhongTonTai() {
        DemoContext context = taoDemoContextMacDinh();
        inTieuDe("Loi chuyen khoan den tai khoan khong ton tai");
        System.out.println(context.atmService.chuyenTien(context.taiKhoanNguon, "123456", "1000000", "TK999"));
    }

    private static void demoRutVuotHanMuc() {
        DemoContext context = taoDemoContextMacDinh();
        context.taiKhoanNguon.setSoDu(30_000_000);
        inTieuDe("Loi rut vuot han muc");
        System.out.println(context.atmService.rutTien(context.taiKhoanNguon, "123456", "21000000"));
    }

    private static void demoDoiPinSaiDinhDang() {
        DemoContext context = taoDemoContextMacDinh();
        inTieuDe("Loi doi PIN sai dinh dang");
        System.out.println(context.atmService.doiPIN(context.taiKhoanNguon, "123456", "12ab", "12ab"));
    }

    private static void demoDoiPinXacNhanKhongKhop() {
        DemoContext context = taoDemoContextMacDinh();
        inTieuDe("Loi doi PIN xac nhan khong khop");
        System.out.println(context.atmService.doiPIN(context.taiKhoanNguon, "123456", "222222", "333333"));
    }

    private static void inTieuDe(String tieuDe) {
        System.out.println();
        System.out.println("---- " + tieuDe + " ----");
    }
}
