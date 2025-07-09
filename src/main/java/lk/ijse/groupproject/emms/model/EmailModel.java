package lk.ijse.groupproject.emms.model;

import lk.ijse.groupproject.emms.dto.EmailDTO;
import lk.ijse.groupproject.emms.db.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmailModel {

    public static boolean saveEmail(EmailDTO emailDTO) {
        String sql = "INSERT INTO emails (email, clientName, gender, age, job) VALUES (?, ?, ?, ?, ?)";

        try {
            Connection conn = DBConnection.getInstance().getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            pstmt.setString(1, emailDTO.getEmail());
            pstmt.setString(2, emailDTO.getClientName());
            pstmt.setString(3, emailDTO.getGender());
            pstmt.setInt(4, emailDTO.getAge());
            pstmt.setString(5, emailDTO.getJob());

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                // Get the generated ID
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    emailDTO.setId(generatedKeys.getInt(1));
                }
                generatedKeys.close();
                pstmt.close();
                return true;
            }

            pstmt.close();
            return false;

        } catch (SQLException e) {
            System.err.println("Error saving email: " + e.getMessage());
            return false;
        }
    }

    public static List<EmailDTO> getAllEmails() {
        List<EmailDTO> emailList = new ArrayList<>();
        String sql = "SELECT id, email, clientName, gender, age, job FROM emails ORDER BY id DESC";

        try {
            Connection conn = DBConnection.getInstance().getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                EmailDTO email = new EmailDTO(
                        rs.getInt("id"),
                        rs.getString("email"),
                        rs.getString("clientName"),
                        rs.getString("gender"),
                        rs.getInt("age"),
                        rs.getString("job")
                );
                emailList.add(email);
            }

            rs.close();
            pstmt.close();

        } catch (SQLException e) {
            System.err.println("Error retrieving emails: " + e.getMessage());
        }

        return emailList;
    }

    public static boolean updateEmail(EmailDTO emailDTO) {
        String sql = "UPDATE emails SET email = ?, clientName = ?, gender = ?, age = ?, job = ? WHERE id = ?";

        try {
            Connection conn = DBConnection.getInstance().getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, emailDTO.getEmail());
            pstmt.setString(2, emailDTO.getClientName());
            pstmt.setString(3, emailDTO.getGender());
            pstmt.setInt(4, emailDTO.getAge());
            pstmt.setString(5, emailDTO.getJob());
            pstmt.setInt(6, emailDTO.getId());

            int rowsAffected = pstmt.executeUpdate();
            pstmt.close();

            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error updating email: " + e.getMessage());
            return false;
        }
    }

    public static boolean deleteEmail(int id) {
        String sql = "DELETE FROM emails WHERE id = ?";

        try {
            Connection conn = DBConnection.getInstance().getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
            pstmt.close();

            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error deleting email: " + e.getMessage());
            return false;
        }
    }

    public static EmailDTO searchEmail(int id) {
        String sql = "SELECT id, email, clientName, gender, age, job FROM emails WHERE id = ?";

        try {
            Connection conn = DBConnection.getInstance().getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                EmailDTO email = new EmailDTO(
                        rs.getInt("id"),
                        rs.getString("email"),
                        rs.getString("clientName"),
                        rs.getString("gender"),
                        rs.getInt("age"),
                        rs.getString("job")
                );
                rs.close();
                pstmt.close();
                return email;
            }

            rs.close();
            pstmt.close();

        } catch (SQLException e) {
            System.err.println("Error searching email by ID: " + e.getMessage());
        }

        return null;
    }

    public static EmailDTO searchEmailByAddress(String emailAddress) {
        String sql = "SELECT id, email, clientName, gender, age, job FROM emails WHERE email = ?";

        try {
            Connection conn = DBConnection.getInstance().getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, emailAddress);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                EmailDTO email = new EmailDTO(
                        rs.getInt("id"),
                        rs.getString("email"),
                        rs.getString("clientName"),
                        rs.getString("gender"),
                        rs.getInt("age"),
                        rs.getString("job")
                );
                rs.close();
                pstmt.close();
                return email;
            }

            rs.close();
            pstmt.close();

        } catch (SQLException e) {
            System.err.println("Error searching email by address: " + e.getMessage());
        }

        return null;
    }

    public static boolean emailExists(String emailAddress) {
        return searchEmailByAddress(emailAddress) != null;
    }

    public static int getEmailCount() {
        String sql = "SELECT COUNT(*) as count FROM emails";

        try {
            Connection conn = DBConnection.getInstance().getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int count = rs.getInt("count");
                rs.close();
                pstmt.close();
                return count;
            }

            rs.close();
            pstmt.close();

        } catch (SQLException e) {
            System.err.println("Error getting email count: " + e.getMessage());
        }

        return 0;
    }

    public static List<EmailDTO> getEmailsByGender(String gender) {
        List<EmailDTO> emailList = new ArrayList<>();
        String sql = "SELECT id, email, clientName, gender, age, job FROM emails WHERE gender = ? ORDER BY clientName";

        try {
            Connection conn = DBConnection.getInstance().getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, gender);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                EmailDTO email = new EmailDTO(
                        rs.getInt("id"),
                        rs.getString("email"),
                        rs.getString("clientName"),
                        rs.getString("gender"),
                        rs.getInt("age"),
                        rs.getString("job")
                );
                emailList.add(email);
            }

            rs.close();
            pstmt.close();

        } catch (SQLException e) {
            System.err.println("Error getting emails by gender: " + e.getMessage());
        }

        return emailList;
    }

    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }


}