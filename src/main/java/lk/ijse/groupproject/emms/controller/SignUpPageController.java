package lk.ijse.groupproject.emms.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import lk.ijse.groupproject.emms.dto.UserDTO;
import lk.ijse.groupproject.emms.model.UserModel;

import java.io.IOException;
import java.sql.SQLException;

public class SignUpPageController {

    @FXML
    private Button btnSignUp;

    @FXML
    private AnchorPane signupPage;

    @FXML
    private PasswordField txtConfirmPassword;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private TextField txtUsername;

    @FXML
    private TextField txtemail;

    @FXML
    private AnchorPane whiteap;

    @FXML
    void btnSignUpOnAction(ActionEvent event) {
        String username = txtUsername.getText().trim();
        String email = txtemail.getText().trim();
        String password = txtPassword.getText().trim();
        String confirmPassword = txtConfirmPassword.getText().trim();

        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showAlert("Error", "All fields are required!", Alert.AlertType.ERROR);
            return;
        }

        if (!password.equals(confirmPassword)) {
            showAlert("Error", "Passwords do not match!", Alert.AlertType.ERROR);
            return;
        }

        // Optional: Use hashing for password
        // String hashedPassword = PasswordUtil.hash(password);

        UserDTO user = new UserDTO(null, username, username, email, password); // password could be hashed
        try {
            boolean saved = UserModel.saveUser(user);
            if (saved) {
                showAlert("Success", "Account created successfully!", Alert.AlertType.INFORMATION);
                loadUI("/view/SignInPage.fxml");
            } else {
                showAlert("Error", "Failed to create account!", Alert.AlertType.ERROR);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Show actual error in console
            showAlert("Error", "Username or Email already exists or database error!", Alert.AlertType.ERROR);
        }
    }

    @FXML
    void goToLogin(MouseEvent event) {
        loadUI("/view/SignInPage.fxml");
    }

    private void loadUI(String resource) {
        signupPage.getChildren().clear();
        try {
            signupPage.getChildren().add(FXMLLoader.load(getClass().getResource(resource)));
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load page!", Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
