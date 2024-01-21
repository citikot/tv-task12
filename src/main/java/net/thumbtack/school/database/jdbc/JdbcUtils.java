package net.thumbtack.school.database.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JdbcUtils {

    private static Connection connection;
    private static final String USER = "test";
    private static final String PASSWORD = "test";
    private static final String URL = "jdbc:mysql://localhost:3306/ttschool?useUnicode=yes&characterEncoding=UTF8&useSSL=false&serverTimezone=Asia/Omsk&allowPublicKeyRetrieval=true";

    public static boolean createConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            return true;
        } catch (ClassNotFoundException | SQLException e) {
            return false;
        }
    }

    public static Connection getConnection() {
        return connection;
    }

    public static void closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            // can only log
        }
    }
}
