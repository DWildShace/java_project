package com.atm.dto.response;

/**
 * TaiKhoanResponse - DTO thông tin tài khoản trả về cho UI.
 *
 * QUAN TRỌNG: Không bao giờ chứa `maPin`.
 * Entity TaiKhoan (có maPin) chỉ tồn tại bên trong Service layer.
 * UI chỉ được biết những thông tin an toàn này.
 */
public record TaiKhoanResponse(
        String soTK,
        String maKH,
        double soDu,
        String trangThai,
        boolean biKhoa
) {}
