package com.atm.ui;

import com.atm.dto.response.TaiKhoanResponse;
import com.atm.util.Utils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * PanelMenu - Đọc TaiKhoanResponse (DTO) từ session của MainFrame.
 * Không bao giờ tiếp xúc trực tiếp với Entity TaiKhoan.
 */
public class PanelMenu extends JPanel {

    private final MainFrame frame;
    private JLabel lblSoTK;
    private JLabel lblSoDu;
    private JLabel lblTrangThai;

    public PanelMenu(MainFrame frame) {
        this.frame = frame;
        setBackground(UITheme.BG_PRIMARY);
        setLayout(new GridBagLayout());
        xayDungUI();
    }

    /**
     * CardLayout gọi setVisible(true) mỗi lần chuyển sang panel này.
     * Đây là thời điểm đúng để refresh số dư từ DB.
     * (addNotify() chỉ chạy 1 lần khi component được thêm vào container → không dùng được)
     */
    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible) {
            frame.refreshTaiKhoan();
            capNhatThongTin();
        }
    }

    private void xayDungUI() {
        JPanel wrapper = new JPanel();
        wrapper.setBackground(UITheme.BG_PRIMARY);
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
        wrapper.setBorder(new EmptyBorder(30, 30, 30, 30));
        wrapper.setPreferredSize(new Dimension(420, 560));

        wrapper.add(xayDungCardThongTin());
        wrapper.add(Box.createVerticalStrut(20));
        wrapper.add(xayDungLuoiNut());
        wrapper.add(Box.createVerticalStrut(16));

        JButton btnDangXuat = UIComponents.createDangerButton("🔓  Đăng xuất");
        btnDangXuat.setMaximumSize(new Dimension(Integer.MAX_VALUE, UITheme.BTN_HEIGHT));
        btnDangXuat.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnDangXuat.addActionListener(e -> frame.dangXuat());
        wrapper.add(btnDangXuat);

        add(wrapper);
    }

    private JPanel xayDungCardThongTin() {
        JPanel card = UIComponents.createCard();
        card.setLayout(new BorderLayout(0, 8));
        card.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblTitle = UIComponents.createHeading("💳  Tài khoản của bạn");

        lblSoTK = UIComponents.createLabel("...");
        lblSoTK.setFont(UITheme.FONT_BODY);
        lblSoTK.setForeground(UITheme.TEXT_MUTED);

        lblTrangThai = UIComponents.createLabel("");
        lblTrangThai.setFont(UITheme.FONT_BODY);

        lblSoDu = new JLabel("0 VND", SwingConstants.RIGHT);
        lblSoDu.setFont(UITheme.FONT_MONO);
        lblSoDu.setForeground(UITheme.ACCENT_GREEN);

        JPanel topRow = new JPanel(new BorderLayout());
        topRow.setOpaque(false);
        topRow.add(lblTitle, BorderLayout.WEST);
        topRow.add(lblSoDu,  BorderLayout.EAST);

        JPanel bottomRow = new JPanel(new BorderLayout());
        bottomRow.setOpaque(false);
        bottomRow.add(lblSoTK,      BorderLayout.WEST);
        bottomRow.add(lblTrangThai, BorderLayout.EAST);

        JSeparator sep = new JSeparator();
        sep.setForeground(UITheme.BG_HOVER);

        card.add(topRow,    BorderLayout.NORTH);
        card.add(sep,       BorderLayout.CENTER);
        card.add(bottomRow, BorderLayout.SOUTH);
        return card;
    }

    private JPanel xayDungLuoiNut() {
        JPanel grid = new JPanel(new GridLayout(2, 2, 12, 12));
        grid.setOpaque(false);

        grid.add(taoNut("💵  Rút tiền",    MainFrame.SCREEN_RUT_TIEN));
        grid.add(taoNut("💰  Nạp tiền",    MainFrame.SCREEN_NAP_TIEN));
        grid.add(taoNut("🔄  Chuyển tiền", MainFrame.SCREEN_CHUYEN_TIEN));
        grid.add(taoNut("🔑  Đổi PIN",     MainFrame.SCREEN_DOI_PIN));
        return grid;
    }

    private JButton taoNut(String nhan, String manHinh) {
        JButton btn = UIComponents.createPrimaryButton(nhan);
        btn.setPreferredSize(new Dimension(0, 80));
        btn.addActionListener(e -> frame.hienThiManHinh(manHinh));
        return btn;
    }

    private void capNhatThongTin() {
        // Đọc từ DTO session - không có PIN
        TaiKhoanResponse dto = frame.getTaiKhoanDangNhap();
        if (dto == null) return;

        lblSoTK.setText("TK: " + dto.soTK() + "   |   KH: " + dto.maKH());
        lblSoDu.setText(Utils.formatCurrency(dto.soDu()) + " VND");

        if (dto.biKhoa()) {
            lblTrangThai.setText("🔒 Bị khóa");
            lblTrangThai.setForeground(UITheme.ACCENT_RED);
        } else {
            lblTrangThai.setText("✅ Hoạt động");
            lblTrangThai.setForeground(UITheme.ACCENT_GREEN);
        }
    }
}
