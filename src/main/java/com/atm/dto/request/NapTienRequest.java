package com.atm.dto.request;

/**
 * NapTienRequest - DTO mang thông tin nạp tiền từ UI xuống Service.
 * Nạp tiền không cần PIN (theo thiết kế nghiệp vụ hiện tại).
 */
public record NapTienRequest(String soTK, String soTienNhap) {}
