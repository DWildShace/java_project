package com.atm.exception;

/**
 * Ngoai le nay duoc su dung khi tong tien rut trong ngay
 * vuot qua han muc ATM cho phep.
 */
public class VuotHanMucRutTienException extends Exception {
    public VuotHanMucRutTienException(String message) {
        super(message);
    }
}
