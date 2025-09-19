package vn.edu.hcmute.fit.bt8;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionPool {

    private static ConnectionPool pool = null;

    // 🔑 Render PostgreSQL connection
    private static final String URL =
            "jdbc:postgresql://dpg-d36bh2ogjchc73c608v0-a.oregon-postgres.render.com:5432/dbvinhan";
    private static final String USER = "vinhan";
    private static final String PASSWORD = "aggX9mEugEiBK0r8fiDc1JxVxxh41E8Q";

    private ConnectionPool() {
        try {
            // Load PostgreSQL driver
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static synchronized ConnectionPool getInstance() {
        if (pool == null) {
            pool = new ConnectionPool();
        }
        return pool;
    }

    public Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void freeConnection(Connection c) {
        try {
            if (c != null && !c.isClosed()) {
                c.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
