package com.atm.dto.request;

/**
 * DoiPINRequest - DTO mang thông tin đổi PIN từ UI xuống Service.
 */
public record DoiPINRequest(String soTK, String pinCu,
                             String pinMoi, String xacNhanPinMoi) {}
