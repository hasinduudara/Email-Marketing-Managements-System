package lk.ijse.groupproject.emms.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBConnection {
    private static final Logger logger = Logger.getLogger(DBConnection.class.getName());
    private static DBConnection dbConnection;
    private Connection connection;

    private final String url = "jdbc:mysql://localhost:3306/emms_db";
    private final String user = "root";
    private final String password = "hasindu12345";

    private DBConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, user, password);
            logger.info("✅ Database connection established successfully.");
        } catch (ClassNotFoundException | SQLException e) {
            logger.log(Level.SEVERE, "❌ Error establishing database connection", e);
            throw new RuntimeException("Database connection failed", e);
        }
    }

    public static synchronized DBConnection getInstance() {
        if (dbConnection == null) {
            dbConnection = new DBConnection();
        }
        return dbConnection;
    }

    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            logger.warning("🔄 Reconnecting to the database...");
            connection = DriverManager.getConnection(url, user, password);
        }
        return connection;
    }

    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                logger.info("🔒 Database connection closed.");
            } catch (SQLException e) {
                logger.log(Level.WARNING, "⚠️ Error closing database connection", e);
            }
        }
    }
}
