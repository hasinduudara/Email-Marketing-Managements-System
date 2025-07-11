package lk.ijse.groupproject.emms.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static DBConnection dbConnection;

    private final String url = "jdbc:mysql://localhost:3306/emms_db";
    private final String user = "root";
    private final String password = "hasindu12345";

    private DBConnection() {
        // private constructor
    }

    public static DBConnection getInstance() {
        if (dbConnection == null) {
            dbConnection = new DBConnection();
        }
        return dbConnection;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password); // ✅ always new, fresh connection
    }
}
