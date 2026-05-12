package com.atm;

import com.atm.entity.TaiKhoan;
import com.atm.service.ATMService;
import com.atm.util.TransactionLogger;
import com.atm.util.Utils;

import java.time.LocalDateTime;
import java.util.List;

public class DemoThreadSafe {
    public static void main(String[] args) {
        ATMService atmService = new ATMService();
        TaiKhoan taiKhoan = new TaiKhoan("TK_THREAD", 1_500_000, "Hoat dong", "KH_THREAD", "123456", LocalDateTime.now());
        atmService.themTaiKhoan(taiKhoan);

        Runnable rutTien = () -> {
            String ketQua = atmService.rutTien(taiKhoan, "123456", "1000000");
            System.out.println(Thread.currentThread().getName() + ": " + ketQua);
        };

        Thread threadA = new Thread(rutTien, "Thread-A");
        Thread threadB = new Thread(rutTien, "Thread-B");

        threadA.start();
        threadB.start();

        try {
            threadA.join();
            threadB.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("So du cuoi cung: " + Utils.formatCurrency(taiKhoan.getSoDu()));

        List<String> lichSu = TransactionLogger.docLichSu();
        if (!lichSu.isEmpty()) {
            System.out.println("--- Lich su giao dich ---");
            for (String dong : lichSu) {
                System.out.println(dong);
            }
        }
    }
}
