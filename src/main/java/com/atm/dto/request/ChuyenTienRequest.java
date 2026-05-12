package com.atm.dto.request;

/**
 * ChuyenTienRequest - DTO mang thông tin chuyển tiền từ UI xuống Service.
 */
public record ChuyenTienRequest(String soTKNguon, String pin,
                                 String soTienNhap, String soTKDich) {}
