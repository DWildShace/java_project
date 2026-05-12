package com.atm;

import com.atm.DAO.DatabaseConnection;
import com.atm.ui.MainFrame;

import javax.swing.*;

/**
 * Main entry point for the ATM project.
 *
 * Luồng khởi động:
 *  1. Kiểm tra kết nối DB (không ném exception nếu thất bại — UI vẫn mở).
 *  2. Đặt Look & Feel theo hệ thống.
 *  3. Khởi tạo và hiển thị MainFrame trên Event Dispatch Thread (EDT).
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("====================================");
        System.out.println("   ATM SYSTEM - KHỞI ĐỘNG");
        System.out.println("====================================");

        // Kiểm tra kết nối DB (tùy chọn, không làm crash UI)
        try {
            DatabaseConnection.getInstance().testConnection();
        } catch (Exception e) {
            System.out.println("⚠️  Không kết nối được DB: " + e.getMessage());
            System.out.println("   Chương trình vẫn chạy với dữ liệu mẫu.");
        }

        // Khởi động Swing UI trên Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
                // Fallback về Look & Feel mặc định nếu không lấy được
            }
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}