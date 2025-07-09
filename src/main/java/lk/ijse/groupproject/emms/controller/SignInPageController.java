// File: src/main/java/lk/ijse/groupproject/emms/controller/SignInPageController.java
package lk.ijse.groupproject.emms.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import lk.ijse.groupproject.emms.model.UserModel;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

public class SignInPageController {

    @FXML
    private Button btnSignIn;

    @FXML
    private ImageView imgConfirmPasswordView;

    @FXML
    private Label lblCreateAcc;

    @FXML
    private Label lblForgotPassword;

    @FXML
    private Label lblNotAcc;

    @FXML
    private AnchorPane loginPage;

    @FXML
    private PasswordField txtPassword1;

    @FXML
    private TextField txtPasswordVisible;

    @FXML
    private TextField txtUsername;

    @FXML
    private AnchorPane whiteap;

    private boolean isPasswordVisible = false;

    public static SignInPageController instance;
    public static String username = "";

    @FXML
    void btnSignInOnAction(ActionEvent event) {
        username = txtUsername.getText().trim();
        String password = isPasswordVisible ? txtPasswordVisible.getText().trim() : txtPassword1.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Username and password required!", Alert.AlertType.ERROR);
            return;
        }

        try {
            boolean valid = UserModel.validateUser(username, password);
            if (valid) {
                showAlert("Success", "Login successful!", Alert.AlertType.INFORMATION);
                loadUI("/view/testDashboard.fxml");
            } else {
                showAlert("Error", "Invalid username or password!", Alert.AlertType.ERROR);
            }
        } catch (SQLException e) {
            showAlert("Error", "Database error!", Alert.AlertType.ERROR);
        }

    }

    @FXML
    void imgConfirmPasswordViewOnAction(MouseEvent event) {
        if (isPasswordVisible) {
            txtPassword1.setText(txtPasswordVisible.getText());
            txtPasswordVisible.setVisible(false);
            txtPassword1.setVisible(true);
        } else {
            txtPasswordVisible.setText(txtPassword1.getText());
            txtPasswordVisible.setVisible(true);
            txtPassword1.setVisible(false);
        }
        isPasswordVisible = !isPasswordVisible;
    }

    @FXML
    void lblCreateAccOnClick(MouseEvent event) {
        loadUI("/view/SignUpPage.fxml");
    }

    private void loadUI(String resource) {
        loginPage.getChildren().clear();
        try {
            loginPage.getChildren().add(FXMLLoader.load(Objects.requireNonNull(getClass().getResource(resource))));
        } catch (IOException e) {
            showAlert("Error", "Failed to load dashboard!", Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void lblForgotPasswordOnAction(MouseEvent event) {
        // loadUI("/view/ForgetPassword.fxml");
    }
}