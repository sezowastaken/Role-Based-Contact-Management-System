package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/oop_rbcm_db?useUnicode=true&characterEncoding=UTF-8&useSSL=false";
    private static final String USER = "myuser";
    private static final String PASSWORD = "1234";

    private static Connection connection;

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            // Ensure session uses utf8mb4 to avoid character loss for multibyte characters (Turkish, emojis, etc.)
            try (Statement s = connection.createStatement()) {
                s.execute("SET NAMES utf8mb4");
                s.execute("SET character_set_results = 'utf8mb4'");
                s.execute("SET character_set_client = 'utf8mb4'");
                s.execute("SET character_set_connection = 'utf8mb4'");
            } catch (SQLException ignored) {
                // If the server/driver for some reason doesn't accept these, keep going — caller will see failures.
            }
        }
        return connection;
    }
}
