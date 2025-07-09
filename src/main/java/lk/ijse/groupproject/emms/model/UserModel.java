package lk.ijse.groupproject.emms.model;

import lk.ijse.groupproject.emms.db.DBConnection;
import lk.ijse.groupproject.emms.dto.UserDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserModel {

    public static boolean saveUser(UserDTO user) throws SQLException {
        String sql = "INSERT INTO users (name, username, email, password) VALUES (?, ?, ?, ?)";
        Connection conn = DBConnection.getInstance().getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, user.getName());
        ps.setString(2, user.getUsername());
        ps.setString(3, user.getEmail());
        ps.setString(4, user.getPassword()); // Should be hashed if using security
        return ps.executeUpdate() > 0;
    }

    public static boolean validateUser(String username, String password) throws SQLException {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        Connection conn = DBConnection.getInstance().getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, username);
        ps.setString(2, password); // Match hash if hashed
        ResultSet rs = ps.executeQuery();
        return rs.next();
    }
}
