package com.atm.util;

import com.atm.entity.KhachHang;
import com.atm.entity.TheATM;

/**
 * SessionManager - lưu trạng thái đăng nhập hiện tại (thẻ, khách hàng liên quan)
 */
public class SessionManager {
    private static final SessionManager INSTANCE = new SessionManager();

    private TheATM currentCard;
    private KhachHang currentCustomer;

    private SessionManager() {
    }

    public static SessionManager getInstance() {
        return INSTANCE;
    }

    public void login(TheATM card, KhachHang customer) {
        this.currentCard = card;
        this.currentCustomer = customer;
    }

    public void logout() {
        this.currentCard = null;
        this.currentCustomer = null;
    }

    public boolean isLoggedIn() {
        return currentCard != null && currentCustomer != null;
    }

    public TheATM getCurrentCard() {
        return currentCard;
    }

    public KhachHang getCurrentCustomer() {
        return currentCustomer;
    }
}
