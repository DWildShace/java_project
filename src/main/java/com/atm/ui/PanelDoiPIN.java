package com.atm.ui;

import com.atm.dto.request.DoiPINRequest;
import com.atm.dto.response.KetQuaResponse;

import javax.swing.*;
import java.awt.*;

/** PanelDoiPIN - dùng DoiPINRequest DTO. */
public class PanelDoiPIN extends JPanel {

    private final MainFrame frame;

    public PanelDoiPIN(MainFrame frame) {
        this.frame = frame;
        setBackground(UITheme.BG_PRIMARY);
        setLayout(new GridBagLayout());
        add(xayDungForm());
    }

    private JPanel xayDungForm() {
        JPanel card = UIComponents.createCard();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setPreferredSize(new Dimension(360, 450));

        JLabel lblTitle = UIComponents.createTitle("🔑  Đổi mã PIN");
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPasswordField pfPinCu      = UIComponents.createPasswordField();
        JPasswordField pfPinMoi     = UIComponents.createPasswordField();
        JPasswordField pfXacNhanPin = UIComponents.createPasswordField();
        pfPinCu.setMaximumSize(new Dimension(Integer.MAX_VALUE, UITheme.INPUT_HEIGHT));
        pfPinMoi.setMaximumSize(new Dimension(Integer.MAX_VALUE, UITheme.INPUT_HEIGHT));
        pfXacNhanPin.setMaximumSize(new Dimension(Integer.MAX_VALUE, UITheme.INPUT_HEIGHT));

        JLabel lblResult = UIComponents.createResultLabel();
        lblResult.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        lblResult.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblHint = UIComponents.createLabel("PIN gồm đúng 6 chữ số.");
        lblHint.setForeground(UITheme.TEXT_MUTED);
        lblHint.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnDoi     = UIComponents.createPrimaryButton("Xác nhận đổi PIN");
        JButton btnQuayLai = UIComponents.createSecondaryButton("← Quay lại");
        btnDoi.setMaximumSize(new Dimension(Integer.MAX_VALUE, UITheme.BTN_HEIGHT));
        btnQuayLai.setMaximumSize(new Dimension(Integer.MAX_VALUE, UITheme.BTN_HEIGHT));
        btnDoi.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnQuayLai.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnDoi.addActionListener(e -> {
            String pinCu      = new String(pfPinCu.getPassword());
            String pinMoi     = new String(pfPinMoi.getPassword());
            String xacNhanPin = new String(pfXacNhanPin.getPassword());

            if (pinCu.isEmpty() || pinMoi.isEmpty() || xacNhanPin.isEmpty()) {
                UIComponents.showError(lblResult, "Vui lòng nhập đầy đủ thông tin.");
                return;
            }

            DoiPINRequest request = new DoiPINRequest(
                    frame.getTaiKhoanDangNhap().soTK(), pinCu, pinMoi, xacNhanPin);

            KetQuaResponse ketQua = frame.getAtmService().doiPINDTO(request);

            if (ketQua.thanhCong()) {
                UIComponents.showSuccess(lblResult, "Đổi mã PIN thành công!");
                pfPinCu.setText("");
                pfPinMoi.setText("");
                pfXacNhanPin.setText("");
            } else {
                UIComponents.showError(lblResult, ketQua.thongBao());
            }
        });

        btnQuayLai.addActionListener(e -> {
            pfPinCu.setText("");
            pfPinMoi.setText("");
            pfXacNhanPin.setText("");
            UIComponents.clearResult(lblResult);
            frame.hienThiManHinh(MainFrame.SCREEN_MENU);
        });

        card.add(lblTitle);
        card.add(Box.createVerticalStrut(20));
        card.add(UIComponents.createLabel("Mã PIN hiện tại"));
        card.add(Box.createVerticalStrut(6));
        card.add(pfPinCu);
        card.add(Box.createVerticalStrut(14));
        card.add(UIComponents.createLabel("Mã PIN mới"));
        card.add(Box.createVerticalStrut(6));
        card.add(pfPinMoi);
        card.add(Box.createVerticalStrut(14));
        card.add(UIComponents.createLabel("Xác nhận mã PIN mới"));
        card.add(Box.createVerticalStrut(6));
        card.add(pfXacNhanPin);
        card.add(Box.createVerticalStrut(6));
        card.add(lblHint);
        card.add(Box.createVerticalStrut(18));
        card.add(btnDoi);
        card.add(Box.createVerticalStrut(10));
        card.add(lblResult);
        card.add(Box.createVerticalStrut(10));
        card.add(btnQuayLai);

        return card;
    }
}
