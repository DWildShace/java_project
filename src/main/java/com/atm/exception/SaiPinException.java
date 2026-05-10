package com.atm.exception;

/**
 * Ngoai le nay duoc dung khi nguoi dung nhap sai ma PIN
 * nhung chua den muc khoa tai khoan.
 */
public class SaiPinException extends Exception {
    public SaiPinException(String message) {
        super(message);
    }
}
