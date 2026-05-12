package com.atm.ui;

import com.atm.dto.request.RutTienRequest;
import com.atm.dto.response.KetQuaResponse;

import javax.swing.*;
import java.awt.*;

/** PanelRutTien - dùng RutTienRequest DTO, nhận KetQuaResponse. */
public class PanelRutTien extends JPanel {

    private final MainFrame frame;

    public PanelRutTien(MainFrame frame) {
        this.frame = frame;
        setBackground(UITheme.BG_PRIMARY);
        setLayout(new GridBagLayout());
        add(xayDungForm());
    }

    private JPanel xayDungForm() {
        JPanel card = UIComponents.createCard();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setPreferredSize(new Dimension(360, 370));

        JLabel lblTitle = UIComponents.createTitle("💵  Rút tiền");
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPasswordField pfPin   = UIComponents.createPasswordField();
        JTextField   tfSoTien  = UIComponents.createTextField();
        pfPin.setMaximumSize(new Dimension(Integer.MAX_VALUE, UITheme.INPUT_HEIGHT));
        tfSoTien.setMaximumSize(new Dimension(Integer.MAX_VALUE, UITheme.INPUT_HEIGHT));

        JLabel lblResult = UIComponents.createResultLabel();
        lblResult.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        lblResult.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnRut     = UIComponents.createPrimaryButton("Xác nhận rút tiền");
        JButton btnQuayLai = UIComponents.createSecondaryButton("← Quay lại");
        btnRut.setMaximumSize(new Dimension(Integer.MAX_VALUE, UITheme.BTN_HEIGHT));
        btnQuayLai.setMaximumSize(new Dimension(Integer.MAX_VALUE, UITheme.BTN_HEIGHT));
        btnRut.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnQuayLai.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnRut.addActionListener(e -> {
            String pin    = new String(pfPin.getPassword());
            String soTien = tfSoTien.getText().trim();

            if (pin.isEmpty() || soTien.isEmpty()) {
                UIComponents.showError(lblResult, "Vui lòng nhập đầy đủ thông tin.");
                return;
            }

            // ① Tạo Request DTO - gom đủ dữ liệu, không xử lý gì thêm ở UI
            RutTienRequest request = new RutTienRequest(
                    frame.getTaiKhoanDangNhap().soTK(), pin, soTien);

            // ② Gọi service DTO-path
            KetQuaResponse ketQua = frame.getAtmService().rutTienDTO(request);

            // ③ Đọc từ DTO kết quả
            if (ketQua.thanhCong()) {
                UIComponents.showSuccess(lblResult, "Rút tiền thành công!");
                pfPin.setText("");
                tfSoTien.setText("");
                frame.refreshTaiKhoan(); // cập nhật số dư session
            } else {
                UIComponents.showError(lblResult, ketQua.thongBao());
            }
        });

        btnQuayLai.addActionListener(e -> {
            pfPin.setText("");
            tfSoTien.setText("");
            UIComponents.clearResult(lblResult);
            frame.hienThiManHinh(MainFrame.SCREEN_MENU);
        });

        card.add(lblTitle);
        card.add(Box.createVerticalStrut(20));
        card.add(UIComponents.createLabel("Mã PIN"));
        card.add(Box.createVerticalStrut(6));
        card.add(pfPin);
        card.add(Box.createVerticalStrut(14));
        card.add(UIComponents.createLabel("Số tiền muốn rút (VND)"));
        card.add(Box.createVerticalStrut(6));
        card.add(tfSoTien);
        card.add(Box.createVerticalStrut(20));
        card.add(btnRut);
        card.add(Box.createVerticalStrut(10));
        card.add(lblResult);
        card.add(Box.createVerticalStrut(10));
        card.add(btnQuayLai);

        return card;
    }
}
