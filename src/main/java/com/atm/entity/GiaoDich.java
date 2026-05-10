package com.atm.entity;

import java.time.LocalDateTime;

public class GiaoDich {
    private String maGD;
    private String loaiGiaoDich;
    private double soTien;
    private LocalDateTime thoiGian;
    private String trangThai;
    private String soTK_LienKet;

    public GiaoDich() {}

    public GiaoDich(String maGD, String loaiGiaoDich, double soTien, LocalDateTime thoiGian, String trangThai, String soTK_LienKet) {
        this.maGD = maGD;
        this.loaiGiaoDich = loaiGiaoDich;
        this.soTien = soTien;
        this.thoiGian = thoiGian;
        this.trangThai = trangThai;
        this.soTK_LienKet = soTK_LienKet;
    }

    public String getMaGD() { return maGD; }
    public void setMaGD(String maGD) { this.maGD = maGD; }
    public String getLoaiGiaoDich() { return loaiGiaoDich; }    
    public void setLoaiGiaoDich(String loaiGiaoDich) { this.loaiGiaoDich = loaiGiaoDich; }
    public double getSoTien() { return soTien; }
    public void setSoTien(double soTien) { this.soTien = soTien; }
    public LocalDateTime getThoiGian() { return thoiGian; }
    public void setThoiGian(LocalDateTime thoiGian) { this.thoiGian = thoiGian; }
    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
    public String getSoTK_LienKet() { return soTK_LienKet; }
    public void setSoTK_LienKet(String soTK_LienKet) { this.soTK_LienKet = soTK_LienKet; }
}
