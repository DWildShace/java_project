package com.atm.entity;

public class TaiKhoanThanhToan extends TaiKhoan {
    private double phiBaoHanh;
    private int soLanRutTrongThang;
    private int hanSoLanRutToiDa;

    public TaiKhoanThanhToan() { super(); }

    public TaiKhoanThanhToan(String soTK, double soDu, String trangThai, String maKH, java.time.LocalDateTime ngayMo, double phiBaoHanh, int soLanRutTrongThang, int hanSoLanRutToiDa) {
        super(soTK, soDu, trangThai, maKH, ngayMo);
        this.phiBaoHanh = phiBaoHanh;
        this.soLanRutTrongThang = soLanRutTrongThang;
        this.hanSoLanRutToiDa = hanSoLanRutToiDa;
    }

    public double getPhiBaoHanh() { return phiBaoHanh; }
    public void setPhiBaoHanh(double phiBaoHanh) { this.phiBaoHanh = phiBaoHanh; }
    public int getSoLanRutTrongThang() { return soLanRutTrongThang; }
    public void setSoLanRutTrongThang(int soLanRutTrongThang) { this.soLanRutTrongThang = soLanRutTrongThang; }
    public int getHanSoLanRutToiDa() { return hanSoLanRutToiDa; }
    public void setHanSoLanRutToiDa(int hanSoLanRutToiDa) { this.hanSoLanRutToiDa = hanSoLanRutToiDa; }
}
