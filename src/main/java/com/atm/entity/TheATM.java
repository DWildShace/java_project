package com.atm.entity;

import java.time.LocalDateTime;

public class TheATM {
    private String soThe;
    private String maPIN;
    private String trangThai;
    private String soTK_LienKet;
    private int soLanNhapSai = 0;
    private int hanNhapSaiToiDa = 3;
    private LocalDateTime ngayCapThe;
    private LocalDateTime ngayHetHan;
    

    public TheATM() {}

    public TheATM(String soThe, String maPIN, String trangThai, String soTK_LienKet, int soLanNhapSai, int hanNhapSaiToiDa, LocalDateTime ngayCapThe, LocalDateTime ngayHetHan) {
        this.soThe = soThe;
        this.maPIN = maPIN;
        this.trangThai = trangThai;
        this.soTK_LienKet = soTK_LienKet;
        this.soLanNhapSai = soLanNhapSai;
        this.hanNhapSaiToiDa = hanNhapSaiToiDa;
        this.ngayCapThe = ngayCapThe;
        this.ngayHetHan = ngayHetHan;
    }

    public String getSoThe() { return soThe; }
    public void setSoThe(String soThe) { this.soThe = soThe; }
    public String getMaPIN() { return maPIN; }
    public void setMaPIN(String maPIN) { this.maPIN = maPIN; }
    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
    public String getSoTK_LienKet() { return soTK_LienKet; }
    public void setSoTK_LienKet(String soTK_LienKet) { this.soTK_LienKet = soTK_LienKet; }
    public int getSoLanNhapSai() { return soLanNhapSai; }
    public void setSoLanNhapSai(int soLanNhapSai) { this.soLanNhapSai = soLanNhapSai; }
    public int getHanNhapSaiToiDa() { return hanNhapSaiToiDa; }
    public void setHanNhapSaiToiDa(int hanNhapSaiToiDa) { this.hanNhapSaiToiDa = hanNhapSaiToiDa; }
    public LocalDateTime getNgayCapThe() { return ngayCapThe; }
    public void setNgayCapThe(LocalDateTime ngayCapThe) { this.ngayCapThe = ngayCapThe; }
    public LocalDateTime getNgayHetHan() { return ngayHetHan; }
    public void setNgayHetHan(LocalDateTime ngayHetHan) { this.ngayHetHan = ngayHetHan; }
  
    public boolean xacThucPIN(String pinNhap) {
        if (!isTheHoatDong()) {
            System.out.println("Thẻ ATM hiện không hoạt động.");
            return false;
        }
        

        if (this.maPIN.equals(pinNhap)) {
            soLanNhapSai = 0;
            return true;
        }

        soLanNhapSai++;
        System.out.println("Sai mã PIN. Lần sai thứ: " + soLanNhapSai);

        if (soLanNhapSai >= hanNhapSaiToiDa) {
            this.trangThai = "Khoa";
            System.out.println("Bạn đã nhập sai PIN 3 lần. Thẻ đã bị khóa.");
        }

        return false;
    }
    public void moKhoaThe() {
        this.trangThai = "Hoat_Dong";
        this.soLanNhapSai = 0;
        System.out.println("Thẻ ATM đã được mở khóa.");
    }
    public boolean isTheHoatDong() {
        return "Hoat_Dong".equalsIgnoreCase(this.trangThai);
    }
}
