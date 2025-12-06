package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {

    /** JDBC connection URL for MySQL database.
     * Username: myuser
     * Password: 1234 
     * For authentication.
     */
    private static final String URL = "jdbc:mysql://localhost:3306/oop_rbcm_db?useUnicode=true&characterEncoding=UTF-8&useSSL=false";
    private static final String USER = "myuser";
    private static final String PASSWORD = "1234";

    private static Connection connection;

    /**
     * Returns a database connection instance. Uses single pattern to reuse the same connection.
     * If no connection exists or the existing connection is closed, creates a new one.
     * Automatically configures UTF-8 for Turkish characters.
     * @return Connection object to the MySQL database.
     * @throws SQLException if database connection fails or SQL error occurs.
     */
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            
            try (Statement s = connection.createStatement()) {
                s.execute("SET NAMES utf8mb4");
                s.execute("SET character_set_results = 'utf8mb4'");
                s.execute("SET character_set_client = 'utf8mb4'");
                s.execute("SET character_set_connection = 'utf8mb4'");
            } catch (SQLException ignored) {
                
            }
        }
        return connection;
    }
}
