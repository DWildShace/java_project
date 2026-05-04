package com.atm.entity;

/**
 * Interface định nghĩa các hành động chung cho tài khoản
 */
public interface IAccount {
    /**
     * Khóa tài khoản
     */
    void khoa();

    /**
     * Mở khóa tài khoản
     */
    void moKhoa();

    /**
     * Kiểm tra trạng thái tài khoản có hoạt động không
     * @return true nếu tài khoản hoạt động, false nếu bị khóa
     */
    boolean isTrangThaiHoatDong();
}
