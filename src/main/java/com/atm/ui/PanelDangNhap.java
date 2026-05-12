package com.atm.ui;

import com.atm.dto.request.DangNhapRequest;
import com.atm.dto.response.KetQuaResponse;
import com.atm.dto.response.TaiKhoanResponse;

import javax.swing.*;
import java.awt.*;

/**
 * PanelDangNhap - Luồng ATM đúng: nhập SỐ THẺ trước, sau đó nhập PIN.
 *
 * Luồng:
 *   UI nhập soThe + pin → DangNhapRequest → ATMService.dangNhap()
 *   Service xác thực thẻ → tra SoTK liên kết → trả session TaiKhoanResponse.
 */
public class PanelDangNhap extends JPanel {

    private final MainFrame frame;

    public PanelDangNhap(MainFrame frame) {
        this.frame = frame;
        setBackground(UITheme.BG_PRIMARY);
        setLayout(new GridBagLayout());
        add(xayDungCard());
    }

    private JPanel xayDungCard() {
        JPanel card = UIComponents.createCard();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setPreferredSize(new Dimension(360, 430));

        JLabel lblIcon = new JLabel("🏧", SwingConstants.CENTER);
        lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 52));
        lblIcon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblTitle = UIComponents.createTitle("ATM Simulation");
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSub = UIComponents.createLabel("Nhập số thẻ và mã PIN để tiếp tục");
        lblSub.setForeground(UITheme.TEXT_MUTED);
        lblSub.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Đổi từ SốTK → Số Thẻ
        JTextField     tfSoThe = UIComponents.createTextField();
        JPasswordField pfPin   = UIComponents.createPasswordField();
        tfSoThe.setMaximumSize(new Dimension(Integer.MAX_VALUE, UITheme.INPUT_HEIGHT));
        pfPin.setMaximumSize(new Dimension(Integer.MAX_VALUE, UITheme.INPUT_HEIGHT));

        JLabel lblResult = UIComponents.createResultLabel();
        lblResult.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        lblResult.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnDangNhap = UIComponents.createPrimaryButton("Đăng nhập");
        btnDangNhap.setMaximumSize(new Dimension(Integer.MAX_VALUE, UITheme.BTN_HEIGHT));
        btnDangNhap.setAlignmentX(Component.CENTER_ALIGNMENT);

        Runnable xuLy = () -> xuLyDangNhap(tfSoThe, pfPin, lblResult);
        btnDangNhap.addActionListener(e -> xuLy.run());
        pfPin.addActionListener(e -> xuLy.run());

        // Hint khớp với dữ liệu thực trong DB
        JLabel lblHint = UIComponents.createLabel("Demo: Thẻ 1234567890123456 / PIN: 1234");
        lblHint.setForeground(UITheme.TEXT_MUTED);
        lblHint.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(Box.createVerticalStrut(10));
        card.add(lblIcon);
        card.add(Box.createVerticalStrut(8));
        card.add(lblTitle);
        card.add(Box.createVerticalStrut(4));
        card.add(lblSub);
        card.add(Box.createVerticalStrut(24));
        card.add(UIComponents.createLabel("Số thẻ ATM"));
        card.add(Box.createVerticalStrut(6));
        card.add(tfSoThe);
        card.add(Box.createVerticalStrut(14));
        card.add(UIComponents.createLabel("Mã PIN"));
        card.add(Box.createVerticalStrut(6));
        card.add(pfPin);
        card.add(Box.createVerticalStrut(18));
        card.add(btnDangNhap);
        card.add(Box.createVerticalStrut(12));
        card.add(lblResult);
        card.add(Box.createVerticalStrut(16));
        card.add(lblHint);

        return card;
    }

    private void xuLyDangNhap(JTextField tfSoThe, JPasswordField pfPin, JLabel lblResult) {
        String soThe = tfSoThe.getText().trim();
        String pin   = new String(pfPin.getPassword());

        if (soThe.isEmpty() || pin.isEmpty()) {
            UIComponents.showError(lblResult, "Vui lòng nhập số thẻ và mã PIN.");
            return;
        }

        // ① Tạo Request DTO với số thẻ
        DangNhapRequest request = new DangNhapRequest(soThe, pin);

        // ② Gọi Service — service xác thực thẻ → PIN → trả soTK khi thành công
        KetQuaResponse ketQua = frame.getAtmService().dangNhap(request);

        // ③ Đọc kết quả
        if (ketQua.thanhCong()) {
            // thongBao() chứa soTK liên kết (do service trả về)
            String soTK = ketQua.thongBao();
            TaiKhoanResponse taiKhoanDTO = frame.getAtmService().layThongTinTaiKhoan(soTK);
            frame.setTaiKhoanDangNhap(taiKhoanDTO);
            tfSoThe.setText("");
            pfPin.setText("");
            UIComponents.clearResult(lblResult);
            frame.hienThiManHinh(MainFrame.SCREEN_MENU);
        } else {
            UIComponents.showError(lblResult, ketQua.thongBao());
        }
    }
}
