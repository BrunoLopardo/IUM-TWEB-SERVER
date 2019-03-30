package dao.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {

    private final String DB_URL;
    private final String USER;
    private final String PWD;

    public ConnectionManager(String DB_URL, String USER, String PWD) {
        this.DB_URL = DB_URL;
        this.USER = USER;
        this.PWD = PWD;
        registerDriver();
    }

    public Connection getConnection() {
        Connection conn = null;
        
        try {
            conn = DriverManager.getConnection(DB_URL, USER, PWD);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        
        return conn;
    }
    
    public static void closeAll(AutoCloseable... closeables) {
        if (closeables != null) {
            for (AutoCloseable closeable : closeables) {
                try {
                    if (closeable != null) {
                        closeable.close();
                    }
                }catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }
        }
    }

    private static void registerDriver() {
        try {
            DriverManager.registerDriver(new org.postgresql.Driver());
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }
}
