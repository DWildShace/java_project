package com.atm.DAO;

/**
 * DatabaseTest - kiem tra ket noi MySQL
 */
public class DatabaseTest {
    public static void main(String[] args) {
        System.out.println("====================================");
        System.out.println("  TEST KET NOI DATABASE ATM");
        System.out.println("====================================");

        DatabaseConnection.getInstance().testConnection();
    }
}