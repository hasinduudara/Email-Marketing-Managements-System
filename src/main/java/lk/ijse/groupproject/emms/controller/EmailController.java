package lk.ijse.groupproject.emms.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.groupproject.emms.dto.EmailDTO;
import lk.ijse.groupproject.emms.dto.tm.EmailTM;
import lk.ijse.groupproject.emms.model.EmailModel;

import java.net.URL;
import java.util.*;

public class EmailController implements Initializable {

    @FXML
    private TextField emailField;

    @FXML
    private TextField clientNameField;

    @FXML
    private ComboBox<String> genderComboBox;

    @FXML
    private TextField ageField;

    @FXML
    private TextField jobField;

    @FXML
    private Button saveButton;

    @FXML
    private Button updateButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button sendMailButton;

    @FXML
    private Button clearButton;

    @FXML
    private TableView<EmailTM> emailTableView;

    @FXML
    private TableColumn<EmailTM, Integer> idColumn;

    @FXML
    private TableColumn<EmailTM, String> emailColumn;

    @FXML
    private TableColumn<EmailTM, String> clientNameColumn;

    @FXML
    private TableColumn<EmailTM, String> genderColumn;

    @FXML
    private TableColumn<EmailTM, Integer> ageColumn;

    @FXML
    private TableColumn<EmailTM, String> jobColumn;

    private ObservableList<EmailTM> emailObservableList;
    private EmailTM selectedEmail;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupTableColumns();
        loadEmailData();
        setupTableSelectionListener();

        idColumn.setSortType(TableColumn.SortType.ASCENDING);
        emailTableView.getSortOrder().add(idColumn);

        updateButton.setDisable(true);
    }

    private void setupTableColumns() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        clientNameColumn.setCellValueFactory(new PropertyValueFactory<>("clientName"));
        genderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));
        ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));
        jobColumn.setCellValueFactory(new PropertyValueFactory<>("job"));
    }

    private void setupTableSelectionListener() {
        emailTableView.setRowFactory(tv -> {
            TableRow<EmailTM> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1 && (!row.isEmpty())) {
                    selectedEmail = row.getItem();
                    populateFields(selectedEmail);
                }
            });
            return row;
        });
    }

    private void populateFields(EmailTM emailTM) {
        if (emailTM != null) {
            emailField.setText(emailTM.getEmail());
            clientNameField.setText(emailTM.getClientName());
            genderComboBox.setValue(emailTM.getGender());
            ageField.setText(String.valueOf(emailTM.getAge()));
            jobField.setText(emailTM.getJob());

            updateButton.setDisable(false);
        }
    }

    private void loadEmailData() {
        try {
            List<EmailDTO> emailDTOList = EmailModel.getAllEmails();

            emailDTOList.sort(Comparator.comparingInt(EmailDTO::getId));

            if (emailObservableList == null) {
                emailObservableList = FXCollections.observableArrayList();
            } else {
                emailObservableList.clear();
            }

            for (EmailDTO dto : emailDTOList) {
                EmailTM tm = new EmailTM(
                        dto.getId(),
                        dto.getEmail(),
                        dto.getClientName(),
                        dto.getGender(),
                        dto.getAge(),
                        dto.getJob()
                );
                emailObservableList.add(tm);
            }

            emailTableView.setItems(emailObservableList);
            emailTableView.refresh();

        } catch (Exception e) {
            System.err.println("Error loading email data: " + e.getMessage());
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load email data: " + e.getMessage());
        }
    }

    @FXML
    private void handleSave() {
        if (validateFields()) {
            try {
                String email = emailField.getText().trim();
                String clientName = clientNameField.getText().trim();
                String gender = genderComboBox.getValue();
                int age = Integer.parseInt(ageField.getText().trim());
                String job = jobField.getText().trim();

                if (EmailModel.emailExists(email)) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Email already exists!");
                    return;
                }

                EmailDTO emailDTO = new EmailDTO(email, clientName, gender, age, job);

                if (EmailModel.saveEmail(emailDTO)) {
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Email saved successfully!");
                    loadEmailData();
                    handleClear();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to save email!");
                }
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Please enter a valid age!");
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error", "An error occurred: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleUpdate() {
        if (selectedEmail != null && validateFields()) {
            try {
                String email = emailField.getText().trim();
                String clientName = clientNameField.getText().trim();
                String gender = genderComboBox.getValue();
                int age = Integer.parseInt(ageField.getText().trim());
                String job = jobField.getText().trim();

                EmailDTO existingEmail = EmailModel.searchEmailByAddress(email);
                if (existingEmail != null && existingEmail.getId() != selectedEmail.getId()) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Email already exists for another client!");
                    return;
                }

                EmailDTO emailDTO = new EmailDTO(selectedEmail.getId(), email, clientName, gender, age, job);

                if (EmailModel.updateEmail(emailDTO)) {
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Email updated successfully!");
                    loadEmailData();
                    handleClear();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to update email!");
                }
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Please enter a valid age!");
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error", "An error occurred: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please select a record to update!");
        }
    }

    @FXML
    private void handleDelete() {
        if (selectedEmail != null) {
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Confirm Delete");
            confirmAlert.setHeaderText("Delete Email Record");
            confirmAlert.setContentText("Are you sure you want to delete this email record?");

            Optional<ButtonType> result = confirmAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                if (EmailModel.deleteEmail(selectedEmail.getId())) {
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Email deleted successfully!");
                    loadEmailData();
                    handleClear();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete email!");
                }
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please select a record to delete!");
        }
    }

    @FXML
    private void handleSendMail() {
        // Implementation placeholder
    }

    @FXML
    private void handleClear() {
        emailField.clear();
        clientNameField.clear();
        genderComboBox.setValue(null);
        ageField.clear();
        jobField.clear();

        selectedEmail = null;
        updateButton.setDisable(true);
        emailTableView.getSelectionModel().clearSelection();
    }

    private boolean validateFields() {
        StringBuilder errorMessage = new StringBuilder();

        if (emailField.getText().trim().isEmpty()) {
            errorMessage.append("Email is required!\n");
        } else if (!EmailModel.isValidEmail(emailField.getText().trim())) {
            errorMessage.append("Please enter a valid email address!\n");
        }

        if (clientNameField.getText().trim().isEmpty()) {
            errorMessage.append("Client name is required!\n");
        }

        if (genderComboBox.getValue() == null) {
            errorMessage.append("Gender is required!\n");
        }

        if (ageField.getText().trim().isEmpty()) {
            errorMessage.append("Age is required!\n");
        } else {
            try {
                int age = Integer.parseInt(ageField.getText().trim());
                if (age < 0 || age > 150) {
                    errorMessage.append("Please enter a valid age (0-150)!\n");
                }
            } catch (NumberFormatException e) {
                errorMessage.append("Please enter a valid age!\n");
            }
        }

        if (jobField.getText().trim().isEmpty()) {
            errorMessage.append("Job is required!\n");
        }

        if (errorMessage.length() > 0) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", errorMessage.toString());
            return false;
        }

        return true;
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

