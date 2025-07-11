package lk.ijse.groupproject.emms.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static DBConnection dbConnection;

    private final Connection connection;
    private final String url = "jdbc:mysql://localhost:3306/emms_db";
    private final String user = "root";
    private final String password = "avishka12345";

    private DBConnection() throws SQLException {
        connection = DriverManager.getConnection(url, user, password);
    }

    public static DBConnection getInstance() throws SQLException {

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password); // ✅ always new, fresh connection
    }
}

