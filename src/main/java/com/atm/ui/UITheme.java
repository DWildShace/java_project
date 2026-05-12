package com.atm.ui;

import java.awt.Color;
import java.awt.Font;

/**
 * UITheme - Tập trung toàn bộ hằng số thiết kế (màu, font, kích thước).
 * Thay đổi giao diện toàn app chỉ cần sửa ở đây.
 */
public final class UITheme {

    private UITheme() {} // Không cho khởi tạo

    // ── Màu nền ──────────────────────────────────────────────
    public static final Color BG_PRIMARY   = new Color(15, 23, 42);   // Xanh đen tối
    public static final Color BG_CARD      = new Color(30, 41, 59);   // Card nổi
    public static final Color BG_INPUT     = new Color(51, 65, 85);   // Ô nhập
    public static final Color BG_HOVER     = new Color(71, 85, 105);  // Hover button

    // ── Màu chữ ──────────────────────────────────────────────
    public static final Color TEXT_PRIMARY  = new Color(248, 250, 252); // Trắng chính
    public static final Color TEXT_MUTED    = new Color(148, 163, 184); // Xám phụ
    public static final Color TEXT_LABEL    = new Color(203, 213, 225); // Label form

    // ── Màu nhấn (Accent) ────────────────────────────────────
    public static final Color ACCENT_BLUE   = new Color(59, 130, 246);  // Nút chính
    public static final Color ACCENT_GREEN  = new Color(34, 197, 94);   // Thành công
    public static final Color ACCENT_RED    = new Color(239, 68, 68);   // Lỗi
    public static final Color ACCENT_YELLOW = new Color(234, 179, 8);   // Cảnh báo

    // ── Font ─────────────────────────────────────────────────
    public static final Font FONT_TITLE   = new Font("Segoe UI", Font.BOLD,  22);
    public static final Font FONT_HEADING = new Font("Segoe UI", Font.BOLD,  15);
    public static final Font FONT_BODY    = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_LABEL   = new Font("Segoe UI", Font.BOLD,  12);
    public static final Font FONT_BUTTON  = new Font("Segoe UI", Font.BOLD,  13);
    public static final Font FONT_MONO    = new Font("Consolas",  Font.BOLD,  20);

    // ── Bo góc & khoảng cách ─────────────────────────────────
    public static final int PADDING     = 20;
    public static final int PADDING_SM  = 10;
    public static final int BTN_HEIGHT  = 42;
    public static final int INPUT_HEIGHT = 36;
}
