package com.atm.exception;

/**
 * Ngoai le nay bao hieu giao dich chuyen khoan khong the hoan tat,
 * vi du tai khoan dich khong ton tai.
 */
public class ChuyenKhoanThatBaiException extends Exception {
    public ChuyenKhoanThatBaiException(String message) {
        super(message);
    }
}
