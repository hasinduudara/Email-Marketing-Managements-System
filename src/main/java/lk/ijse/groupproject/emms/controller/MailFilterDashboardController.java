package lk.ijse.groupproject.emms.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import lk.ijse.groupproject.emms.db.DBConnection;
import lk.ijse.groupproject.emms.model.EmailModel;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.sql.*;
import java.util.List;
import java.util.Properties;
import java.util.ArrayList;

public class MailFilterDashboardController {

    @FXML private AnchorPane filterMailDashboard;
    @FXML private ComboBox<String> cmbGender;
    @FXML private TextField txtMinAge;
    @FXML private TextField txtMaxAge;
    @FXML private TextField txtJob;
    @FXML private TextField txtTitle;
    @FXML private TextArea txtBody;
    @FXML private TextField txtName;
    @FXML private Button btnFilter;
    @FXML private Button btnSend;
    @FXML private ListView<EmailModel> lstFilteredEmails;

    private ObservableList<EmailModel> filteredList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        cmbGender.getItems().addAll("Male", "Female");
    }

    @FXML
    void btnFilterOnaction(ActionEvent event) {
        String namePart = txtName.getText();
        String gender = cmbGender.getValue();
        String job = txtJob.getText();
        int minAge = Integer.parseInt(txtMinAge.getText());
        int maxAge = Integer.parseInt(txtMaxAge.getText());

        filteredList.clear();
        try (Connection con = DBConnection.getInstance().getConnection()) {
            String sql = "SELECT * FROM emails WHERE clientName LIKE ? AND gender=? AND age BETWEEN ? AND ? AND job=?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, namePart + "%"); // e.g., 'Ha%' will match Hasindu, Harry
            pst.setString(2, gender);
            pst.setInt(3, minAge);
            pst.setInt(4, maxAge);
            pst.setString(5, job);

            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                EmailModel email = new EmailModel(
                        rs.getString("email"),
                        rs.getString("clientName"),
                        rs.getString("gender"),
                        rs.getInt("age"),
                        rs.getString("job")
                );
                filteredList.add(email);
            }
            lstFilteredEmails.setItems(filteredList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    void btnSendOnAction(ActionEvent event) {
        String title = txtTitle.getText();
        String body = txtBody.getText();

        for (EmailModel emailModel : filteredList) {
            sendEmail(emailModel.getEmail(), title, body);
        }

        new Alert(Alert.AlertType.INFORMATION, "Emails sent successfully!").show();
    }

    private void sendEmail(String toEmail, String subject, String body) {
        final String username = "yourgmail@gmail.com";
        final String password = "your-app-password";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);
            message.setText(body);

            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
