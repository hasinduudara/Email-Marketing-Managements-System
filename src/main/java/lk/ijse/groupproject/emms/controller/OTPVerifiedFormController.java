package lk.ijse.groupproject.emms.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OTPVerifiedFormController {
    private static final Logger logger = Logger.getLogger(OTPVerifiedFormController.class.getName());

    @FXML private AnchorPane whiteap;
    @FXML private Button btnReset;
    @FXML private Label lblError;
    @FXML private TextField txtPasswordVisible;
    @FXML private PasswordField txtPassword;
    @FXML private TextField txtConfirmPasswordVisible;
    @FXML private PasswordField txtConfirmPassword;
    @FXML private ImageView imgPasswordView;
    @FXML private ImageView imgConfirmPasswordView;
    @FXML private ImageView imgBack;

    @FXML
    void btnResetOnAction(ActionEvent event) {
        String password = txtPassword.getText();
        String confirmPassword = txtConfirmPassword.getText();

        if (password.isEmpty() || confirmPassword.isEmpty()) {
            showError("Please fill in all fields");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showError("Passwords do not match");
            return;
        }

        // TODO: Update password in database
        try {
            // For now, just navigate back to sign in
            loadUI("/view/SignInPage.fxml");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error loading sign in page", e);
            showError("Error resetting password");
        }
    }

    @FXML
    void imgBackOnAction(MouseEvent event) {
        try {
            loadUI("/view/ForgetPassword.fxml");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error loading forget password page", e);
            showError("Error navigating back");
        }
    }

    @FXML
    void imgPasswordViewOnAction(MouseEvent event) {
        if (txtPassword.isVisible()) {
            txtPasswordVisible.setText(txtPassword.getText());
            txtPasswordVisible.setVisible(true);
            txtPassword.setVisible(false);
        } else {
            txtPassword.setText(txtPasswordVisible.getText());
            txtPassword.setVisible(true);
            txtPasswordVisible.setVisible(false);
        }
    }

    @FXML
    void imgConfirmPasswordViewOnAction(MouseEvent event) {
        if (txtConfirmPassword.isVisible()) {
            txtConfirmPasswordVisible.setText(txtConfirmPassword.getText());
            txtConfirmPasswordVisible.setVisible(true);
            txtConfirmPassword.setVisible(false);
        } else {
            txtConfirmPassword.setText(txtConfirmPasswordVisible.getText());
            txtConfirmPassword.setVisible(true);
            txtConfirmPasswordVisible.setVisible(false);
        }
    }

    private void showError(String message) {
        lblError.setText(message);
        lblError.setStyle("-fx-text-fill: red;");
    }

    private void loadUI(String location) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(location));
            AnchorPane pane = loader.load();
            whiteap.getChildren().setAll(pane);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error loading UI: " + location, e);
            throw e;
        }
    }

    @FXML
    public void initialize() {
        // Initialize password fields visibility
        txtPasswordVisible.setVisible(false);
        txtConfirmPasswordVisible.setVisible(false);
    }
}
