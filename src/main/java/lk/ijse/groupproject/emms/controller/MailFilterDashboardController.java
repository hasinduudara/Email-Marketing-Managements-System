package lk.ijse.groupproject.emms.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import lk.ijse.groupproject.emms.db.DBConnection;
import lk.ijse.groupproject.emms.model.EmailModel;
import lk.ijse.groupproject.emms.util.PdfUtil;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.File;
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
    @FXML private Button btnClear;
    @FXML private ListView<EmailModel> lstFilteredEmails;

    private ObservableList<EmailModel> filteredList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        cmbGender.getItems().addAll("Male", "Female");
    }

    @FXML
    void btnFilterOnaction(ActionEvent event) {
        String namePart = txtName.getText().trim();
        String gender = cmbGender.getValue();
        String job = txtJob.getText().trim();
        String minAgeStr = txtMinAge.getText().trim();
        String maxAgeStr = txtMaxAge.getText().trim();

        List<String> conditions = new ArrayList<>();
        List<Object> parameters = new ArrayList<>();

        if (!namePart.isEmpty()) {
            conditions.add("clientName LIKE ?");
            parameters.add(namePart + "%");
        }

        if (gender != null && !gender.isEmpty()) {
            conditions.add("gender = ?");
            parameters.add(gender);
        }

        if (!minAgeStr.isEmpty()) {
            try {
                parameters.add(Integer.parseInt(minAgeStr));
                conditions.add("age >= ?");
            } catch (NumberFormatException e) {
                new Alert(Alert.AlertType.ERROR, "Invalid minimum age!").show();
                return;
            }
        }

        if (!maxAgeStr.isEmpty()) {
            try {
                parameters.add(Integer.parseInt(maxAgeStr));
                conditions.add("age <= ?");
            } catch (NumberFormatException e) {
                new Alert(Alert.AlertType.ERROR, "Invalid maximum age!").show();
                return;
            }
        }

        if (!job.isEmpty()) {
            conditions.add("job = ?");
            parameters.add(job);
        }

        String whereClause = String.join(" AND ", conditions);
        String sql = "SELECT * FROM emails";
        if (!whereClause.isEmpty()) {
            sql += " WHERE " + whereClause;
        }

        filteredList.clear();

        try (
                Connection con = DBConnection.getInstance().getConnection();
                PreparedStatement pst = con.prepareStatement(sql)
        ) {
            for (int i = 0; i < parameters.size(); i++) {
                pst.setObject(i + 1, parameters.get(i));
            }

            try (ResultSet rs = pst.executeQuery()) {
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
            }

            lstFilteredEmails.setItems(filteredList);

            if (filteredList.isEmpty()) {
                new Alert(Alert.AlertType.INFORMATION, "No matching emails found.").show();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Database error: " + e.getMessage()).show();
        }
    }

    @FXML
    void btnSendOnAction(ActionEvent event) {
        String title = txtTitle.getText();
        String body = txtBody.getText();

        if (title.isEmpty() || body.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Title and Body are required!").show();
            return;
        }

        for (EmailModel emailModel : filteredList) {
            sendEmail(emailModel.getEmail(), title, body);
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "Emails sent successfully!\nDo you want to save this email as a PDF?",
                ButtonType.YES, ButtonType.NO);
        alert.setTitle("Save as PDF?");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Save Email PDF");
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
                File file = fileChooser.showSaveDialog(filterMailDashboard.getScene().getWindow());

                if (file != null) {
                    try {
                        PdfUtil.saveEmailAsPDF(title, body, file.getAbsolutePath());
                        new Alert(Alert.AlertType.INFORMATION, "PDF saved successfully!").show();
                    } catch (Exception e) {
                        e.printStackTrace();
                        new Alert(Alert.AlertType.ERROR, "Failed to save PDF.").show();
                    }
                }
            }
        });
    }

    private void sendEmail(String toEmail, String subject, String body) {
        final String username = "hasiduudara@gmail.com";
        final String password = "wngszetdusifxysl"; // wngs zetd usif xysl

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

    public void btnClearOnAction(ActionEvent actionEvent) {
        txtName.clear();
        cmbGender.getSelectionModel().clearSelection();
        txtMinAge.clear();
        txtMaxAge.clear();
        txtJob.clear();
        txtTitle.clear();
        txtBody.clear();
        filteredList.clear();
        lstFilteredEmails.setItems(filteredList);
    }
}
