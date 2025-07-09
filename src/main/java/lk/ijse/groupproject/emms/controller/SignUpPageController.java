package lk.ijse.groupproject.emms.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import lk.ijse.groupproject.emms.dto.UserDTO;
import lk.ijse.groupproject.emms.model.UserModel;
import lk.ijse.groupproject.emms.service.GoogleAuthService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SignUpPageController {
    private static final Logger logger = Logger.getLogger(SignUpPageController.class.getName());

    @FXML private Label lblMessage;
    @FXML private AnchorPane signupPage;
    @FXML private TextField txtUsername;
    @FXML private TextField txtemail;
    @FXML private PasswordField txtPassword;
    @FXML private PasswordField txtConfirmPassword;
    @FXML private Button btnGoogleSignup;

    @FXML
    void btnSignUpOnAction(ActionEvent event) {
        try {
            String username = txtUsername.getText().trim();
            String email = txtemail.getText().trim();
            String password = txtPassword.getText().trim();
            String confirmPassword = txtConfirmPassword.getText().trim();

            if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                showErrorMessage("All fields are required!");
                return;
            }

            if (!password.equals(confirmPassword)) {
                showErrorMessage("Passwords do not match!");
                return;
            }

            if (!isValidEmail(email)) {
                showErrorMessage("Please enter a valid email address!");
                return;
            }

            UserDTO user = new UserDTO();
            user.setUsername(username);
            user.setName(username);
            user.setEmail(email);
            user.setPassword(password);

            if (UserModel.saveUser(user)) {
                showSuccessMessage("Account created successfully!");
                redirectToSignIn();
            } else {
                showErrorMessage("Failed to create account!");
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Database error during signup", e);
            showErrorMessage("Username or Email already exists!");
        }
    }

    @FXML
    void handleGoogleSignup(ActionEvent event) {
        showInfoMessage("Connecting to Google...");
        btnGoogleSignup.setDisable(true);

        Task<GoogleAuthService.GoogleUserInfo> task = new Task<>() {
            @Override
            protected GoogleAuthService.GoogleUserInfo call() throws Exception {
                return GoogleAuthService.authenticateUser();
            }
        };

        task.setOnSucceeded(e -> {
            GoogleAuthService.GoogleUserInfo userInfo = task.getValue();
            logger.info("Google auth succeeded for: " + userInfo.getEmail());
            javafx.application.Platform.runLater(() -> {
                btnGoogleSignup.setDisable(false);
                handleGoogleSignupSuccess(userInfo);
            });
        });

        task.setOnFailed(e -> {
            Throwable exception = task.getException();
            logger.log(Level.SEVERE, "Google auth failed", exception);
            javafx.application.Platform.runLater(() -> {
                btnGoogleSignup.setDisable(false);
                showErrorMessage("Google Sign Up failed. Please try again.");
            });
        });

        new Thread(task).start();
    }

    private void handleGoogleSignupSuccess(GoogleAuthService.GoogleUserInfo userInfo) {
        try {
            if (isExistingUser(userInfo.getEmail())) {
                showErrorMessage("Account already exists. Please sign in.");
                redirectToSignIn();
                return;
            }

            UserDTO newUser = new UserDTO();
            newUser.setUsername(generateUsernameFromEmail(userInfo.getEmail()));
            newUser.setName(userInfo.getName());
            newUser.setEmail(userInfo.getEmail());
            newUser.setPassword("GOOGLE_AUTH");

            if (UserModel.saveUser(newUser)) {
                showSuccessMessage("Account created successfully!");
                redirectToSignIn();
            } else {
                showErrorMessage("Failed to create account. Please try again.");
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Database error during Google signup", ex);
            showErrorMessage("Database error. Please try again later.");
        }
    }

    private boolean isExistingUser(String email) throws SQLException {
        List<UserDTO> users = UserModel.getAllUsers();
        return users.stream().anyMatch(user -> email.equals(user.getEmail()));
    }

    private void redirectToSignIn() {
        Timeline timeline = new Timeline(new KeyFrame(
            Duration.seconds(2),
            ae -> loadUI("/view/SignInPage.fxml")
        ));
        timeline.play();
    }

    @FXML
    void goToLogin(MouseEvent event) {
        loadUI("/view/SignInPage.fxml");
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    private String generateUsernameFromEmail(String email) {
        return email.split("@")[0] + "_google";
    }

    private void loadUI(String resource) {
        signupPage.getChildren().clear();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(resource));
            if (loader.getLocation() == null) {
                logger.severe("Resource not found: " + resource);
                showErrorMessage("Could not find page to load!");
                return;
            }
            signupPage.getChildren().add(loader.load());
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error loading UI", e);
            showErrorMessage("Failed to load page!");
        }
    }

    private void showErrorMessage(String message) {
        lblMessage.setText(message);
        lblMessage.setStyle("-fx-text-fill: red; -fx-font-size: 14px;");
        lblMessage.setVisible(true);

        Timeline timeline = new Timeline(new KeyFrame(
            Duration.seconds(3),
            ae -> {
                lblMessage.setText("");
                lblMessage.setVisible(false);
            }
        ));
        timeline.play();
    }

    private void showSuccessMessage(String message) {
        lblMessage.setText(message);
        lblMessage.setStyle("-fx-text-fill: green; -fx-font-size: 14px;");
        lblMessage.setVisible(true);
    }

    private void showInfoMessage(String message) {
        lblMessage.setText(message);
        lblMessage.setStyle("-fx-text-fill: blue; -fx-font-size: 14px;");
        lblMessage.setVisible(true);
    }
}
