package com.atm.service;

import com.atm.entity.TaiKhoan;
import com.atm.exception.ChuyenKhoanThatBaiException;
import com.atm.exception.KhongDuSoDuException;
import com.atm.exception.SaiPinException;
import com.atm.exception.SoTienKhongHopLeException;
import com.atm.exception.TaiKhoanBiKhoaException;
import com.atm.exception.VuotHanMucRutTienException;
import com.atm.util.Utils;

import java.util.HashMap;
import java.util.Map;

public class ATMService {
    public static final double HAN_MUC_RUT_TIEN_MOI_NGAY = 20_000_000;

    private final Map<String, TaiKhoan> danhSachTaiKhoan = new HashMap<>();
    private final Map<String, Double> tongTienRutTheoNgay = new HashMap<>();

    public void themTaiKhoan(TaiKhoan taiKhoan) {
        if (taiKhoan != null && taiKhoan.getSoTK() != null) {
            danhSachTaiKhoan.put(taiKhoan.getSoTK(), taiKhoan);
        }
    }

    public String dangNhapATM(TaiKhoan taiKhoan, String pinNhap) {
        try {
            // Dang nhap chi hop le khi tai khoan ton tai va PIN dung.
            kiemTraTaiKhoanTonTai(taiKhoan);
            taiKhoan.kiemTraPIN(pinNhap);
            return "Dang nhap ATM thanh cong.";
        } catch (SaiPinException | TaiKhoanBiKhoaException e) {
            return e.getMessage();
        }
    }

    public String rutTien(TaiKhoan taiKhoan, String pinNhap, String soTienNhap) {
        try {
            // Thu tu kiem tra rat quan trong: khoa tai khoan -> PIN -> so tien -> so du -> han muc.
            kiemTraTaiKhoanCoTheGiaoDich(taiKhoan);
            taiKhoan.kiemTraPIN(pinNhap);
            double soTien = chuyenDoiSoTien(soTienNhap, "rut");
            kiemTraSoDu(taiKhoan, soTien);
            kiemTraHanMucRutTien(taiKhoan, soTien);

            taiKhoan.setSoDu(taiKhoan.getSoDu() - soTien);
            ghiNhanRutTien(taiKhoan, soTien);
            return "Rut tien thanh cong: " + Utils.formatCurrency(soTien) + ".";
        } catch (TaiKhoanBiKhoaException | SaiPinException | SoTienKhongHopLeException
                 | KhongDuSoDuException | VuotHanMucRutTienException e) {
            return e.getMessage();
        }
    }

    public String chuyenTien(TaiKhoan taiKhoanNguon, String pinNhap, String soTienNhap, String soTaiKhoanDich) {
        try {
            // Chuyen khoan phai xac thuc tai khoan nguon truoc khi kiem tra tai khoan dich.
            kiemTraTaiKhoanCoTheGiaoDich(taiKhoanNguon);
            taiKhoanNguon.kiemTraPIN(pinNhap);
            double soTien = chuyenDoiSoTien(soTienNhap, "chuyen");
            kiemTraSoDu(taiKhoanNguon, soTien);

            TaiKhoan taiKhoanDich = danhSachTaiKhoan.get(soTaiKhoanDich);
            if (taiKhoanDich == null) {
                throw new ChuyenKhoanThatBaiException("Loi: Tai khoan dich khong ton tai.");
            }

            taiKhoanNguon.setSoDu(taiKhoanNguon.getSoDu() - soTien);
            taiKhoanDich.setSoDu(taiKhoanDich.getSoDu() + soTien);
            return "Chuyen tien thanh cong: " + Utils.formatCurrency(soTien) + ".";
        } catch (TaiKhoanBiKhoaException | SaiPinException | SoTienKhongHopLeException
                 | KhongDuSoDuException | ChuyenKhoanThatBaiException e) {
            return e.getMessage();
        }
    }

    public String napTien(TaiKhoan taiKhoan, String soTienNhap) {
        try {
            // Nap tien khong can PIN trong mo hinh nay, nhung van chan tai khoan da bi khoa.
            kiemTraTaiKhoanCoTheGiaoDich(taiKhoan);
            double soTien = chuyenDoiSoTien(soTienNhap, "nap");
            taiKhoan.setSoDu(taiKhoan.getSoDu() + soTien);
            return "Nap tien thanh cong: " + Utils.formatCurrency(soTien) + ".";
        } catch (TaiKhoanBiKhoaException | SoTienKhongHopLeException e) {
            return e.getMessage();
        }
    }

    public String doiPIN(TaiKhoan taiKhoan, String pinCu, String pinMoi, String xacNhanPinMoi) {
        try {
            // Doi PIN bat buoc kiem tra tai khoan, xac thuc PIN cu, roi moi validate PIN moi.
            kiemTraTaiKhoanCoTheGiaoDich(taiKhoan);
            taiKhoan.kiemTraPIN(pinCu);

            if (pinMoi == null || !pinMoi.matches("\\d{6}")) {
                throw new SaiPinException("Loi: Ma PIN moi phai gom dung 6 chu so.");
            }

            if (!pinMoi.equals(xacNhanPinMoi)) {
                throw new SaiPinException("Loi: Xac nhan ma PIN moi khong khop.");
            }

            taiKhoan.setMaPin(pinMoi);
            return "Doi ma PIN thanh cong.";
        } catch (TaiKhoanBiKhoaException | SaiPinException e) {
            return e.getMessage();
        }
    }

    /**
     * Ham nay chan ngay truong hop dang nhap vao mot tai khoan null.
     */
    private void kiemTraTaiKhoanTonTai(TaiKhoan taiKhoan) throws SaiPinException {
        if (taiKhoan == null) {
            throw new SaiPinException("Loi: Khong tim thay tai khoan de thuc hien dang nhap.");
        }
    }

    /**
     * Ham nay dam bao moi giao dich deu dung ngay neu tai khoan khong ton tai
     * hoac da bi khoa.
     */
    private void kiemTraTaiKhoanCoTheGiaoDich(TaiKhoan taiKhoan) throws TaiKhoanBiKhoaException {
        if (taiKhoan == null) {
            throw new TaiKhoanBiKhoaException("Loi: Khong tim thay tai khoan de thuc hien giao dich.");
        }

        if (taiKhoan.isBiKhoa() || !taiKhoan.isTrangThaiHoatDong()) {
            throw new TaiKhoanBiKhoaException("Loi: Tai khoan da bi khoa. Vui long lien he ngan hang de duoc ho tro.");
        }
    }

    /**
     * Ham nay xu ly ca hai loi pho bien:
     * nhap chu thay vi so, hoac nhap so <= 0.
     */
    private double chuyenDoiSoTien(String soTienNhap, String loaiGiaoDich) throws SoTienKhongHopLeException {
        try {
            double soTien = Double.parseDouble(soTienNhap);
            if (soTien <= 0) {
                throw new SoTienKhongHopLeException("Loi: So tien " + loaiGiaoDich + " phai lon hon 0.");
            }
            return soTien;
        } catch (NumberFormatException e) {
            throw new SoTienKhongHopLeException("Loi: So tien khong hop le. Vui long chi nhap chu so.");
        }
    }

    /**
     * Truoc khi tru tien, luon xac minh tai khoan con du so du.
     */
    private void kiemTraSoDu(TaiKhoan taiKhoan, double soTien) throws KhongDuSoDuException {
        if (taiKhoan.getSoDu() < soTien) {
            throw new KhongDuSoDuException("Loi: So du khong du de thuc hien giao dich.");
        }
    }

    /**
     * Ham nay giu tong tien rut trong ngay khong vuot qua gioi han ATM dat ra.
     */
    private void kiemTraHanMucRutTien(TaiKhoan taiKhoan, double soTien) throws VuotHanMucRutTienException {
        double daRut = tongTienRutTheoNgay.getOrDefault(taiKhoan.getSoTK(), 0.0);
        if (daRut + soTien > HAN_MUC_RUT_TIEN_MOI_NGAY) {
            throw new VuotHanMucRutTienException(
                    "Loi: Ban da vuot han muc rut tien toi da " + Utils.formatCurrency(HAN_MUC_RUT_TIEN_MOI_NGAY) + " trong ngay.");
        }
    }

    /**
     * Sau khi rut tien thanh cong, can cap nhat tong rut trong ngay de phuc vu kiem tra han muc.
     */
    private void ghiNhanRutTien(TaiKhoan taiKhoan, double soTien) {
        double daRut = tongTienRutTheoNgay.getOrDefault(taiKhoan.getSoTK(), 0.0);
        tongTienRutTheoNgay.put(taiKhoan.getSoTK(), daRut + soTien);
    }
}
