package com.atm.ui;

import com.atm.dto.request.NapTienRequest;
import com.atm.dto.response.KetQuaResponse;

import javax.swing.*;
import java.awt.*;

/** PanelNapTien - dùng NapTienRequest DTO. */
public class PanelNapTien extends JPanel {

    private final MainFrame frame;

    public PanelNapTien(MainFrame frame) {
        this.frame = frame;
        setBackground(UITheme.BG_PRIMARY);
        setLayout(new GridBagLayout());
        add(xayDungForm());
    }

    private JPanel xayDungForm() {
        JPanel card = UIComponents.createCard();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setPreferredSize(new Dimension(360, 290));

        JLabel lblTitle = UIComponents.createTitle("💰  Nạp tiền");
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField tfSoTien = UIComponents.createTextField();
        tfSoTien.setMaximumSize(new Dimension(Integer.MAX_VALUE, UITheme.INPUT_HEIGHT));

        JLabel lblResult = UIComponents.createResultLabel();
        lblResult.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        lblResult.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnNap     = UIComponents.createPrimaryButton("Xác nhận nạp tiền");
        JButton btnQuayLai = UIComponents.createSecondaryButton("← Quay lại");
        btnNap.setMaximumSize(new Dimension(Integer.MAX_VALUE, UITheme.BTN_HEIGHT));
        btnQuayLai.setMaximumSize(new Dimension(Integer.MAX_VALUE, UITheme.BTN_HEIGHT));
        btnNap.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnQuayLai.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnNap.addActionListener(e -> {
            String soTien = tfSoTien.getText().trim();
            if (soTien.isEmpty()) {
                UIComponents.showError(lblResult, "Vui lòng nhập số tiền.");
                return;
            }

            NapTienRequest request = new NapTienRequest(
                    frame.getTaiKhoanDangNhap().soTK(), soTien);

            KetQuaResponse ketQua = frame.getAtmService().napTienDTO(request);

            if (ketQua.thanhCong()) {
                UIComponents.showSuccess(lblResult, "Nạp tiền thành công!");
                tfSoTien.setText("");
                frame.refreshTaiKhoan();
            } else {
                UIComponents.showError(lblResult, ketQua.thongBao());
            }
        });

        btnQuayLai.addActionListener(e -> {
            tfSoTien.setText("");
            UIComponents.clearResult(lblResult);
            frame.hienThiManHinh(MainFrame.SCREEN_MENU);
        });

        card.add(lblTitle);
        card.add(Box.createVerticalStrut(20));
        card.add(UIComponents.createLabel("Số tiền muốn nạp (VND)"));
        card.add(Box.createVerticalStrut(6));
        card.add(tfSoTien);
        card.add(Box.createVerticalStrut(20));
        card.add(btnNap);
        card.add(Box.createVerticalStrut(10));
        card.add(lblResult);
        card.add(Box.createVerticalStrut(10));
        card.add(btnQuayLai);

        return card;
    }
}
