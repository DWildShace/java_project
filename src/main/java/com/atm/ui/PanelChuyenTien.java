package com.atm.ui;

import com.atm.dto.request.ChuyenTienRequest;
import com.atm.dto.response.KetQuaResponse;

import javax.swing.*;
import java.awt.*;

/** PanelChuyenTien - dùng ChuyenTienRequest DTO. */
public class PanelChuyenTien extends JPanel {

    private final MainFrame frame;

    public PanelChuyenTien(MainFrame frame) {
        this.frame = frame;
        setBackground(UITheme.BG_PRIMARY);
        setLayout(new GridBagLayout());
        add(xayDungForm());
    }

    private JPanel xayDungForm() {
        JPanel card = UIComponents.createCard();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setPreferredSize(new Dimension(360, 440));

        JLabel lblTitle = UIComponents.createTitle("🔄  Chuyển tiền");
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPasswordField pfPin    = UIComponents.createPasswordField();
        JTextField tfSoTKDich   = UIComponents.createTextField();
        JTextField tfSoTien     = UIComponents.createTextField();
        pfPin.setMaximumSize(new Dimension(Integer.MAX_VALUE, UITheme.INPUT_HEIGHT));
        tfSoTKDich.setMaximumSize(new Dimension(Integer.MAX_VALUE, UITheme.INPUT_HEIGHT));
        tfSoTien.setMaximumSize(new Dimension(Integer.MAX_VALUE, UITheme.INPUT_HEIGHT));

        JLabel lblResult = UIComponents.createResultLabel();
        lblResult.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        lblResult.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnChuyen  = UIComponents.createPrimaryButton("Xác nhận chuyển tiền");
        JButton btnQuayLai = UIComponents.createSecondaryButton("← Quay lại");
        btnChuyen.setMaximumSize(new Dimension(Integer.MAX_VALUE, UITheme.BTN_HEIGHT));
        btnQuayLai.setMaximumSize(new Dimension(Integer.MAX_VALUE, UITheme.BTN_HEIGHT));
        btnChuyen.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnQuayLai.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnChuyen.addActionListener(e -> {
            String pin      = new String(pfPin.getPassword());
            String soTKDich = tfSoTKDich.getText().trim();
            String soTien   = tfSoTien.getText().trim();

            if (pin.isEmpty() || soTKDich.isEmpty() || soTien.isEmpty()) {
                UIComponents.showError(lblResult, "Vui lòng nhập đầy đủ thông tin.");
                return;
            }

            ChuyenTienRequest request = new ChuyenTienRequest(
                    frame.getTaiKhoanDangNhap().soTK(), pin, soTien, soTKDich);

            KetQuaResponse ketQua = frame.getAtmService().chuyenTienDTO(request);

            if (ketQua.thanhCong()) {
                UIComponents.showSuccess(lblResult,
                        "Chuyển thành công → " + soTKDich);
                pfPin.setText("");
                tfSoTKDich.setText("");
                tfSoTien.setText("");
                frame.refreshTaiKhoan();
            } else {
                UIComponents.showError(lblResult, ketQua.thongBao());
            }
        });

        btnQuayLai.addActionListener(e -> {
            pfPin.setText("");
            tfSoTKDich.setText("");
            tfSoTien.setText("");
            UIComponents.clearResult(lblResult);
            frame.hienThiManHinh(MainFrame.SCREEN_MENU);
        });

        card.add(lblTitle);
        card.add(Box.createVerticalStrut(20));
        card.add(UIComponents.createLabel("Mã PIN của bạn"));
        card.add(Box.createVerticalStrut(6));
        card.add(pfPin);
        card.add(Box.createVerticalStrut(14));
        card.add(UIComponents.createLabel("Số tài khoản đích"));
        card.add(Box.createVerticalStrut(6));
        card.add(tfSoTKDich);
        card.add(Box.createVerticalStrut(14));
        card.add(UIComponents.createLabel("Số tiền chuyển (VND)"));
        card.add(Box.createVerticalStrut(6));
        card.add(tfSoTien);
        card.add(Box.createVerticalStrut(20));
        card.add(btnChuyen);
        card.add(Box.createVerticalStrut(10));
        card.add(lblResult);
        card.add(Box.createVerticalStrut(10));
        card.add(btnQuayLai);

        return card;
    }
}
