package com.atm.dto.request;

/**
 * RutTienRequest - DTO mang thông tin rút tiền từ UI xuống Service.
 */
public record RutTienRequest(String soTK, String pin, String soTienNhap) {}
