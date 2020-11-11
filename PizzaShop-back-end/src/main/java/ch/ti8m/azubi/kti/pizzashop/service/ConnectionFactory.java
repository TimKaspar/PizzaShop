package ch.ti8m.azubi.kti.pizzashop.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

    public static Connection testConnection() throws SQLException {
        // load the driver - this will also register it
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("H2 Driver not found");
        }
        String connectionURL = "jdbc:h2:file:./db/h2-database;DB_CLOSE_ON_EXIT=FALSE";
        return DriverManager.getConnection(connectionURL);

    }
    public static Connection createDBConnection() {
        Connection con = null;
        try {
            return createDBConnection("localhost", 3306, "pizzashop", "root", "root");
        } catch (SQLException ex) {
            throw new RuntimeException("DB connection unavailable", ex);
        }
    }

    private static Connection createDBConnection(String host, int port, String dbName, String user, String password) throws SQLException {

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL JDBC Driver not found");
        }
        String connectionURL = String.format("jdbc:mysql://%s:%d/%s", host, port, dbName);
        return DriverManager.getConnection(connectionURL, user, password);
    }
}
