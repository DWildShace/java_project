package com.atm.entity;

import java.time.LocalDateTime;

public class TaiKhoan {
    private String soTK;
    private double soDu;
    private String trangThai;
    private String maKH;
    private LocalDateTime ngayMo;

    public TaiKhoan() {}

    public TaiKhoan(String soTK, double soDu, String trangThai, String maKH, LocalDateTime ngayMo) {
        this.soTK = soTK;
        this.soDu = soDu;
        this.trangThai = trangThai;
        this.maKH = maKH;
        this.ngayMo = ngayMo;
    }

    public String getSoTK() { return soTK; }
    public void setSoTK(String soTK) { this.soTK = soTK; }
    public double getSoDu() { return soDu; }
    public void setSoDu(double soDu) { this.soDu = soDu; }
    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
    public String getMaKH() { return maKH; }
    public void setMaKH(String maKH) { this.maKH = maKH; }
    public LocalDateTime getNgayMo() { return ngayMo; }
    public void setNgayMo(LocalDateTime ngayMo) { this.ngayMo = ngayMo; }
}
