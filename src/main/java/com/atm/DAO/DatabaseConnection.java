package com.atm.DAO;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Singleton DatabaseConnection - đọc `database.properties` từ classpath và trả về Connection
 */
public class DatabaseConnection {
    private static volatile DatabaseConnection instance;
    private String url;
    private String username;
    private String password;

    private DatabaseConnection() {
        try (InputStream in = DatabaseConnection.class.getClassLoader().getResourceAsStream("database.properties")) {
            if (in == null) {
                throw new RuntimeException("database.properties not found on classpath");
            }
            Properties props = new Properties();
            props.load(in);
            this.url = props.getProperty("jdbc.url");
            this.username = props.getProperty("jdbc.username");
            this.password = props.getProperty("jdbc.password");

            // Optional: load driver explicitly (not required for recent drivers)
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                // driver not found on classpath
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to load database properties", e);
        }
    }

    public static DatabaseConnection getInstance() {
        if (instance == null) {
            synchronized (DatabaseConnection.class) {
                if (instance == null) {
                    instance = new DatabaseConnection();
                }
            }
        }
        return instance;
    }

    public Connection getConnection() {
        try {
            return DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            throw new RuntimeException("Unable to get DB connection", e);
        }
    }

    public void testConnection() {
        try (Connection c = getConnection()) {
            System.out.println("✅ Database connection OK: " + c.getMetaData().getURL());
        } catch (Exception e) {
            System.out.println("❌ Database connection failed: " + e.getMessage());
        }
    }
}
