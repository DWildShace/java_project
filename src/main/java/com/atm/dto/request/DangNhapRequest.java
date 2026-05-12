package com.atm.dto.request;

/**
 * DangNhapRequest - DTO mang thông tin đăng nhập từ UI xuống Service.
 *
 * Luồng ATM đúng: khách nhập SỐ THẺ trước, sau đó nhập PIN.
 * Service dùng soThe → tra theatm → lấy SoTK_LienKet → load TaiKhoan.
 */
public record DangNhapRequest(String soThe, String pin) {}
