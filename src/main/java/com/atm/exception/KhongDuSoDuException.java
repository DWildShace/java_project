package com.atm.exception;

/**
 * Ngoai le nay duoc dung khi so du hien tai nho hon so tien can giao dich.
 */
public class KhongDuSoDuException extends Exception {
    public KhongDuSoDuException(String message) {
        super(message);
    }
}
