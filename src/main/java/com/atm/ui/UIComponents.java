package com.atm.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * UIComponents - Factory tạo các component Swing đã được style sẵn.
 * Đảm bảo tính nhất quán giao diện, tránh lặp code style ở từng Panel.
 */
public final class UIComponents {

    private UIComponents() {}

    /** Tạo JPanel nền tối làm card */
    public static JPanel createCard() {
        JPanel card = new JPanel();
        card.setBackground(UITheme.BG_CARD);
        card.setBorder(new EmptyBorder(UITheme.PADDING, UITheme.PADDING,
                                       UITheme.PADDING, UITheme.PADDING));
        return card;
    }

    /** Label tiêu đề lớn */
    public static JLabel createTitle(String text) {
        JLabel lbl = new JLabel(text, SwingConstants.CENTER);
        lbl.setFont(UITheme.FONT_TITLE);
        lbl.setForeground(UITheme.TEXT_PRIMARY);
        return lbl;
    }

    /** Label heading cho từng section */
    public static JLabel createHeading(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(UITheme.FONT_HEADING);
        lbl.setForeground(UITheme.ACCENT_BLUE);
        return lbl;
    }

    /** Label thường cho form */
    public static JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(UITheme.FONT_LABEL);
        lbl.setForeground(UITheme.TEXT_LABEL);
        return lbl;
    }

    /** JTextField đã style */
    public static JTextField createTextField() {
        JTextField tf = new JTextField();
        styleInput(tf);
        return tf;
    }

    /** JPasswordField đã style */
    public static JPasswordField createPasswordField() {
        JPasswordField pf = new JPasswordField();
        styleInput(pf);
        return pf;
    }

    /** Nút chính (màu xanh) */
    public static JButton createPrimaryButton(String text) {
        return createButton(text, UITheme.ACCENT_BLUE, UITheme.TEXT_PRIMARY);
    }

    /** Nút nguy hiểm (màu đỏ) */
    public static JButton createDangerButton(String text) {
        return createButton(text, UITheme.ACCENT_RED, UITheme.TEXT_PRIMARY);
    }

    /** Nút phụ/quay lại (màu xám) */
    public static JButton createSecondaryButton(String text) {
        return createButton(text, UITheme.BG_INPUT, UITheme.TEXT_MUTED);
    }

    /** Label hiển thị kết quả (thành công/lỗi) */
    public static JLabel createResultLabel() {
        JLabel lbl = new JLabel(" ", SwingConstants.CENTER);
        lbl.setFont(UITheme.FONT_BODY);
        lbl.setOpaque(true);
        lbl.setBackground(UITheme.BG_CARD);
        lbl.setBorder(new EmptyBorder(UITheme.PADDING_SM, UITheme.PADDING_SM,
                                      UITheme.PADDING_SM, UITheme.PADDING_SM));
        return lbl;
    }

    /** Hiển thị thông báo thành công trên result label */
    public static void showSuccess(JLabel lbl, String msg) {
        lbl.setText("✓  " + msg);
        lbl.setForeground(UITheme.ACCENT_GREEN);
        lbl.setBackground(new Color(20, 83, 45));
    }

    /** Hiển thị thông báo lỗi trên result label */
    public static void showError(JLabel lbl, String msg) {
        lbl.setText("✗  " + msg);
        lbl.setForeground(UITheme.ACCENT_RED);
        lbl.setBackground(new Color(127, 29, 29));
    }

    /** Reset result label về trạng thái rỗng */
    public static void clearResult(JLabel lbl) {
        lbl.setText(" ");
        lbl.setBackground(UITheme.BG_CARD);
    }

    // ── Private helpers ───────────────────────────────────────

    private static void styleInput(JTextField tf) {
        tf.setBackground(UITheme.BG_INPUT);
        tf.setForeground(UITheme.TEXT_PRIMARY);
        tf.setCaretColor(UITheme.TEXT_PRIMARY);
        tf.setFont(UITheme.FONT_BODY);
        tf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.BG_HOVER, 1),
                new EmptyBorder(6, 10, 6, 10)
        ));
        tf.setPreferredSize(new Dimension(0, UITheme.INPUT_HEIGHT));
    }

    private static JButton createButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(UITheme.FONT_BUTTON);
        btn.setForeground(fg);
        btn.setBackground(bg);
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(0, UITheme.BTN_HEIGHT));

        // Hiệu ứng hover
        Color hoverBg = bg.brighter();
        btn.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { btn.setBackground(hoverBg); }
            @Override public void mouseExited(MouseEvent e)  { btn.setBackground(bg); }
        });
        return btn;
    }
}
