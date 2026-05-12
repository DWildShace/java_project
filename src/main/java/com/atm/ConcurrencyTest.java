package com.atm;

import com.atm.DAO.DatabaseConnection;
import com.atm.dto.request.NapTienRequest;
import com.atm.dto.request.RutTienRequest;
import com.atm.service.ATMService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * ConcurrencyTest - Demo kiểm tra tính đa luồng của ATM System.
 *
 * Kịch bản: 10 thread cùng rút 100,000 VND từ tài khoản có 5,000,000 VND.
 * Nếu KHÔNG có lock → race condition → số dư sai.
 * Nếu CÓ lock đúng  → số dư chính xác = 5,000,000 - (10 × 100,000).
 *
 * Chạy: mvn compile exec:java -Dexec.mainClass="com.atm.ConcurrencyTest"
 */
public class ConcurrencyTest {

    private static final String SO_TK        = "TK_TT001";   // tài khoản test trong DB
    private static final String SO_THE       = "1234567890123456";
    private static final String PIN          = "1234";
    private static final double SO_DU_BAN_DAU = 5_000_000.0;
    private static final double SO_TIEN_MOI_THREAD = 100_000.0;
    private static final int    SO_THREAD    = 10;

    public static void main(String[] args) throws Exception {
        System.out.println("══════════════════════════════════════════════");
        System.out.println("  CONCURRENCY TEST — ATM SYSTEM");
        System.out.println("══════════════════════════════════════════════");

        // Bước 1: Reset số dư về giá trị ban đầu
        resetSoDu(SO_TK, SO_DU_BAN_DAU);
        System.out.printf("✅ Số dư ban đầu: %,.0f VND%n", SO_DU_BAN_DAU);
        System.out.printf("🧵 Số thread: %d   |   Mỗi thread rút: %,.0f VND%n",
                SO_THREAD, SO_TIEN_MOI_THREAD);
        System.out.printf("📊 Số dư kỳ vọng sau test: %,.0f VND%n",
                SO_DU_BAN_DAU - SO_THREAD * SO_TIEN_MOI_THREAD);
        System.out.println("──────────────────────────────────────────────");

        // Bước 2: Chạy N thread đồng thời
        ATMService service = new ATMService();
        ExecutorService pool = Executors.newFixedThreadPool(SO_THREAD);
        CountDownLatch startGun = new CountDownLatch(1);  // cho tất cả thread chờ để bắt đầu cùng lúc
        CountDownLatch doneLatch = new CountDownLatch(SO_THREAD);
        List<String> results = new ArrayList<>();

        for (int i = 1; i <= SO_THREAD; i++) {
            final int threadId = i;
            pool.submit(() -> {
                try {
                    startGun.await(); // chờ tín hiệu bắt đầu
                    RutTienRequest req = new RutTienRequest(
                            SO_TK, PIN, String.valueOf((long) SO_TIEN_MOI_THREAD));
                    var ketQua = service.rutTienDTO(req);
                    String msg = String.format("Thread-%02d: %s", threadId, ketQua.thongBao());
                    synchronized (results) { results.add(msg); }
                    System.out.println(msg);
                } catch (Exception e) {
                    System.err.println("Thread-" + threadId + " lỗi: " + e.getMessage());
                } finally {
                    doneLatch.countDown();
                }
            });
        }

        long startTime = System.currentTimeMillis();
        startGun.countDown(); // BẮT ĐẦU! tất cả thread chạy cùng lúc
        doneLatch.await(30, TimeUnit.SECONDS);
        pool.shutdown();
        long elapsed = System.currentTimeMillis() - startTime;

        // Bước 3: Kiểm tra kết quả
        System.out.println("──────────────────────────────────────────────");
        double soDuThucTe = laySoDuHienTai(SO_TK);
        double soDuKyVong = SO_DU_BAN_DAU - SO_THREAD * SO_TIEN_MOI_THREAD;
        long soLanThanhCong = results.stream()
                .filter(r -> r.contains("thanh cong")).count();

        System.out.printf("⏱️  Thời gian chạy: %d ms%n", elapsed);
        System.out.printf("✔️  Giao dịch thành công: %d/%d%n", soLanThanhCong, SO_THREAD);
        System.out.printf("📊 Số dư kỳ vọng : %,.0f VND%n", soDuKyVong);
        System.out.printf("📊 Số dư thực tế : %,.0f VND%n", soDuThucTe);
        System.out.println("──────────────────────────────────────────────");

        if (Math.abs(soDuThucTe - soDuKyVong) < 1) {
            System.out.println("✅ PASS — Đa luồng xử lý đúng! Không có race condition.");
        } else {
            System.out.printf("❌ FAIL — Race condition! Chênh lệch: %,.0f VND%n",
                    Math.abs(soDuThucTe - soDuKyVong));
        }
        System.out.println("══════════════════════════════════════════════");
    }

    /** Reset số dư tài khoản về giá trị test. */
    private static void resetSoDu(String soTK, double soDu) throws Exception {
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "UPDATE taikhoan SET SoDu = ? WHERE SoTK = ?")) {
            ps.setDouble(1, soDu);
            ps.setString(2, soTK);
            ps.executeUpdate();
        }
    }

    /** Đọc số dư hiện tại trực tiếp từ DB. */
    private static double laySoDuHienTai(String soTK) throws Exception {
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT SoDu FROM taikhoan WHERE SoTK = ?")) {
            ps.setString(1, soTK);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getDouble("SoDu");
            }
        }
        return -1;
    }
}
