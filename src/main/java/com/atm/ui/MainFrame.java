package com.atm.ui;

import com.atm.dto.response.TaiKhoanResponse;
import com.atm.service.ATMService;

import javax.swing.*;
import java.awt.*;

/**
 * MainFrame - JFrame chính của ứng dụng.
 *
 * Session lưu TaiKhoanResponse (DTO an toàn), KHÔNG lưu Entity TaiKhoan.
 * Entity TaiKhoan chỉ tồn tại bên trong ATMService.
 */
public class MainFrame extends JFrame {

    public static final String SCREEN_DANG_NHAP   = "DANG_NHAP";
    public static final String SCREEN_MENU        = "MENU";
    public static final String SCREEN_RUT_TIEN    = "RUT_TIEN";
    public static final String SCREEN_NAP_TIEN    = "NAP_TIEN";
    public static final String SCREEN_CHUYEN_TIEN = "CHUYEN_TIEN";
    public static final String SCREEN_DOI_PIN     = "DOI_PIN";

    private final ATMService atmService;

    /**
     * Session: lưu DTO thay vì Entity.
     * UI chỉ đọc thông tin hiển thị từ đây, không có PIN.
     */
    private TaiKhoanResponse taiKhoanDangNhap;

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel     mainPanel  = new JPanel(cardLayout);

    public MainFrame() {
        this.atmService = new ATMService();
        khoiTaoUI();
        hienThiManHinh(SCREEN_DANG_NHAP);
    }

    private void khoiTaoUI() {
        setTitle("ATM Simulation System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(480, 600);
        setMinimumSize(new Dimension(400, 520));
        setLocationRelativeTo(null);
        setResizable(false);
        mainPanel.setBackground(UITheme.BG_PRIMARY);
        dangKyTatCaManHinh();
        add(mainPanel);
    }

    private void dangKyTatCaManHinh() {
        mainPanel.add(new PanelDangNhap(this),   SCREEN_DANG_NHAP);
        mainPanel.add(new PanelMenu(this),        SCREEN_MENU);
        mainPanel.add(new PanelRutTien(this),     SCREEN_RUT_TIEN);
        mainPanel.add(new PanelNapTien(this),     SCREEN_NAP_TIEN);
        mainPanel.add(new PanelChuyenTien(this),  SCREEN_CHUYEN_TIEN);
        mainPanel.add(new PanelDoiPIN(this),      SCREEN_DOI_PIN);
    }

    public void hienThiManHinh(String tenManHinh) {
        cardLayout.show(mainPanel, tenManHinh);
    }

    public void dangXuat() {
        taiKhoanDangNhap = null;
        hienThiManHinh(SCREEN_DANG_NHAP);
    }

    public ATMService          getAtmService()       { return atmService; }
    public TaiKhoanResponse    getTaiKhoanDangNhap() { return taiKhoanDangNhap; }

    /**
     * Sau khi đăng nhập thành công, service trả về TaiKhoanResponse (không PIN).
     * MainFrame lưu DTO này làm session - UI các panel đọc từ đây.
     */
    public void setTaiKhoanDangNhap(TaiKhoanResponse dto) {
        this.taiKhoanDangNhap = dto;
    }

    /**
     * Refresh session từ service sau mỗi giao dịch làm thay đổi số dư.
     * Panels gọi method này để số dư trên PanelMenu luôn đúng.
     */
    public void refreshTaiKhoan() {
        if (taiKhoanDangNhap == null) return;
        TaiKhoanResponse moi = atmService.layThongTinTaiKhoan(taiKhoanDangNhap.soTK());
        if (moi != null) taiKhoanDangNhap = moi;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
            catch (Exception ignored) {}
            new MainFrame().setVisible(true);
        });
    }
}
