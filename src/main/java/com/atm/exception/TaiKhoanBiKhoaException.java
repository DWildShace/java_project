package com.atm.exception;

/**
 * Ngoai le nay duoc nem ra khi tai khoan khong con duoc phep giao dich
 * do da bi khoa thu cong hoac bi khoa sau nhieu lan sai PIN.
 */
public class TaiKhoanBiKhoaException extends Exception {
    public TaiKhoanBiKhoaException(String message) {
        super(message);
    }
}
