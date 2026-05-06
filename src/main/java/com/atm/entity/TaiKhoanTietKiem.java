package com.atm.entity;

import java.time.LocalDateTime;

public class TaiKhoanTietKiem extends TaiKhoan {
    private double laiSuat;
    private int hanRutToiThieu;
    private LocalDateTime ngayRutCuoi;

    public TaiKhoanTietKiem() { super(); }

    public TaiKhoanTietKiem(String soTK, double soDu, String trangThai, String maKH, LocalDateTime ngayMo, double laiSuat, int hanRutToiThieu, LocalDateTime ngayRutCuoi) {
        super(soTK, soDu, trangThai, maKH, ngayMo);
        this.laiSuat = laiSuat;
        this.hanRutToiThieu = hanRutToiThieu;
        this.ngayRutCuoi = ngayRutCuoi;
    }

    public double getLaiSuat() { return laiSuat; }
    public void setLaiSuat(double laiSuat) { this.laiSuat = laiSuat; }
    public int getHanRutToiThieu() { return hanRutToiThieu; }
    public void setHanRutToiThieu(int hanRutToiThieu) { this.hanRutToiThieu = hanRutToiThieu; }
    public LocalDateTime getNgayRutCuoi() { return ngayRutCuoi; }
    public void setNgayRutCuoi(LocalDateTime ngayRutCuoi) { this.ngayRutCuoi = ngayRutCuoi; }
}
