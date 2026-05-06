package com.atm;

import com.atm.DAO.DatabaseConnection;

/**
 * Main entry point for the ATM project.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("====================================");
        System.out.println("  ATM SYSTEM - MAIN ENTRY");
        System.out.println("====================================");

        // Basic health check to confirm DB connectivity
        DatabaseConnection.getInstance().testConnection();
    }
}