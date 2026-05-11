package com.atm.util;

import com.atm.entity.TaiKhoan;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TransactionLogger {
    private static final String LOG_DIR = "logs";
    private static final String LOG_FILE = LOG_DIR + File.separator + "giao_dich.txt";
    private static final Object FILE_LOCK = new Object();

    private static void damBaoThuMuc() {
        File dir = new File(LOG_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    private static String taoDong(String loai, String soTK, String soTKDich, double soTien, String trangThai) {
        String thoiGian = LocalDateTime.now().toString();
        return thoiGian + "|" + loai + "|" + soTK + "|" + soTKDich + "|" + soTien + "|" + trangThai;
    }

    private static void ghiDong(String dong) {
        synchronized (FILE_LOCK) {
            damBaoThuMuc();
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE, true))) {
                writer.write(dong);
                writer.newLine();
            } catch (IOException e) {
                System.err.println("Khong the ghi lich su giao dich: " + e.getMessage());
            }
        }
    }

    public static void logRutTien(TaiKhoan taiKhoan, double soTien, String trangThai) {
        String soTK = taiKhoan != null && taiKhoan.getSoTK() != null ? taiKhoan.getSoTK() : "N/A";
        String dong = taoDong("RUT_TIEN", soTK, "-", soTien, trangThai);
        ghiDong(dong);
    }

    public static void logChuyenTien(TaiKhoan taiKhoanNguon, TaiKhoan taiKhoanDich, double soTien, String trangThai) {
        String soTKNguon = taiKhoanNguon != null && taiKhoanNguon.getSoTK() != null ? taiKhoanNguon.getSoTK() : "N/A";
        String soTKDich = taiKhoanDich != null && taiKhoanDich.getSoTK() != null ? taiKhoanDich.getSoTK() : "N/A";
        String dong = taoDong("CHUYEN_TIEN", soTKNguon, soTKDich, soTien, trangThai);
        ghiDong(dong);
    }

    public static List<String> docLichSu() {
        synchronized (FILE_LOCK) {
            damBaoThuMuc();
            File file = new File(LOG_FILE);
            if (!file.exists()) {
                return new ArrayList<>();
            }

            List<String> ketQua = new ArrayList<>();
            try (BufferedReader reader = new BufferedReader(new FileReader(LOG_FILE))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    ketQua.add(line);
                }
            } catch (IOException e) {
                System.err.println("Khong the doc lich su giao dich: " + e.getMessage());
            }
            return ketQua;
        }
    }
}
