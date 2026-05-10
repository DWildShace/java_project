package com.atm.entity;

import com.atm.exception.SaiPinException;
import com.atm.exception.TaiKhoanBiKhoaException;

import java.time.LocalDateTime;

public class TaiKhoan {
    private String soTK;
    private double soDu;
    private String trangThai;
    private String maKH;
    private String maPin;
    private LocalDateTime ngayMo;
    private int soLanNhapSaiPin;
    private boolean biKhoa = false;

    public TaiKhoan() {}

    public TaiKhoan(String soTK, double soDu, String trangThai, String maKH, LocalDateTime ngayMo) {
        this.soTK = soTK;
        this.soDu = soDu;
        this.trangThai = trangThai;
        this.maKH = maKH;
        this.ngayMo = ngayMo;
        this.soLanNhapSaiPin = 0;
    }

    public TaiKhoan(String soTK, double soDu, String trangThai, String maKH, String maPin, LocalDateTime ngayMo) {
        this(soTK, soDu, trangThai, maKH, ngayMo);
        this.maPin = maPin;
    }

    public String getSoTK() { return soTK; }
    public void setSoTK(String soTK) { this.soTK = soTK; }
    public double getSoDu() { return soDu; }
    public void setSoDu(double soDu) { this.soDu = soDu; }
    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
    public String getMaKH() { return maKH; }
    public void setMaKH(String maKH) { this.maKH = maKH; }
    public String getMaPin() { return maPin; }
    public void setMaPin(String maPin) { this.maPin = maPin; }
    public LocalDateTime getNgayMo() { return ngayMo; }
    public void setNgayMo(LocalDateTime ngayMo) { this.ngayMo = ngayMo; }
    public int getSoLanNhapSaiPin() { return soLanNhapSaiPin; }
    public boolean isBiKhoa() { return biKhoa; }

    /**
     * Kiem tra PIN theo dung thu tu bao mat:
     * 1. Neu tai khoan da bi khoa thi dung ngay.
     * 2. Neu PIN dung thi reset so lan sai.
     * 3. Neu PIN sai thi tang bo dem va khoa tai khoan khi du 3 lan.
     */
    public boolean kiemTraPIN(String pinNhap) throws SaiPinException, TaiKhoanBiKhoaException {
        if (biKhoa || soLanNhapSaiPin >= 3) {
            khoaTaiKhoan();
            throw new TaiKhoanBiKhoaException("Loi: Tai khoan da bi khoa. Vui long lien he ngan hang de duoc ho tro.");
        }

        if (maPin != null && maPin.equals(pinNhap)) {
            soLanNhapSaiPin = 0;
            return true;
        }

        soLanNhapSaiPin++;
        if (soLanNhapSaiPin >= 3) {
            khoaTaiKhoan();
            throw new TaiKhoanBiKhoaException("Loi: Ban da nhap sai ma PIN 3 lan. Tai khoan da bi khoa.");
        }

        throw new SaiPinException("Loi: Ma PIN khong chinh xac. Ban con " + (3 - soLanNhapSaiPin) + " lan thu.");
    }

    /**
     * Khoa tai khoan va dong bo trang thai de cac giao dich sau bi chan.
     */
    public void khoaTaiKhoan() {
        this.biKhoa = true;
        this.trangThai = "Bi khoa";
    }

    /**
     * Mo khoa tai khoan dong thoi xoa bo dem sai PIN de cho phep dang nhap lai.
     */
    public void moKhoaTaiKhoan() {
        this.biKhoa = false;
        this.soLanNhapSaiPin = 0;
        this.trangThai = "Hoat dong";
    }

    public boolean isTrangThaiHoatDong() {
        return !biKhoa;
    }

    public void khoa() {
        khoaTaiKhoan();
    }

    public void moKhoa() {
        moKhoaTaiKhoan();
    }
}
