package com.atm.exception;

/**
 * Ngoai le nay duoc nem ra khi so tien nhap vao khong dung dinh dang
 * hoac khong thoa dieu kien lon hon 0.
 */
public class SoTienKhongHopLeException extends Exception {
    public SoTienKhongHopLeException(String message) {
        super(message);
    }
}
