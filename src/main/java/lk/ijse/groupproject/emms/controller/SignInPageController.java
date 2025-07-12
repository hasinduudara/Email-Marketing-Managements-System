package lk.ijse.groupproject.emms.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import lk.ijse.groupproject.emms.dto.UserDTO;
import lk.ijse.groupproject.emms.model.UserModel;
import lk.ijse.groupproject.emms.service.GoogleAuthService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public class SignInPageController {

    @FXML
    private Button btnSignIn;
    @FXML
    private Label lblMessege;


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

    @FXML
    private Button btnGoogleSignin;


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
                loadUI("/view/Dashboard.fxml");
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
        loadUI("/view/ForgetPassword.fxml");
    }
    @FXML
    void handleGoogleSignin(ActionEvent event) {
        // Show loading message
        showInfoMessage("Connecting to Google...");

        // Disable the button to prevent multiple clicks
        btnGoogleSignin.setDisable(true);

        // Create background task for Google authentication
        Task<GoogleAuthService.GoogleUserInfo> googleAuthTask = new Task<GoogleAuthService.GoogleUserInfo>() {
            @Override
            protected GoogleAuthService.GoogleUserInfo call() throws Exception {
                System.out.println("Starting Google authentication...");
                return GoogleAuthService.authenticateUser();
            }
        };

        googleAuthTask.setOnSucceeded(e -> {
            GoogleAuthService.GoogleUserInfo userInfo = googleAuthTask.getValue();
            System.out.println("Google auth succeeded: " + userInfo);
            // Ensure UI updates happen on JavaFX Application Thread
            javafx.application.Platform.runLater(() -> {
                btnGoogleSignin.setDisable(false);
                handleGoogleAuthSuccess(userInfo);
            });
        });

        googleAuthTask.setOnFailed(e -> {
            Throwable exception = googleAuthTask.getException();
            System.err.println("Google auth failed: " + exception.getMessage());
            exception.printStackTrace();
            // Ensure UI updates happen on JavaFX Application Thread
            javafx.application.Platform.runLater(() -> {
                btnGoogleSignin.setDisable(false);
                showErrorMessage("Google Sign In failed: " + exception.getMessage());
            });
        });

        // Run the task in a background thread
        Thread thread = new Thread(googleAuthTask);
        thread.setDaemon(true);
        thread.start();
    }

    private void handleGoogleAuthSuccess(GoogleAuthService.GoogleUserInfo userInfo) {
        try {
            // Check if user exists in database
            List<UserDTO> allUsers = UserModel.getAllUsers();
            boolean userExists = allUsers.stream()
                    .anyMatch(user -> user.getEmail().equals(userInfo.getEmail()));

            if (userExists) {
                // User exists, proceed to dashboard
                showSuccessMessage("Welcome back, " + userInfo.getName() + "!");
                Timeline timeline = new Timeline(new KeyFrame(
                        Duration.seconds(1),
                        ae -> loadUI("/view/Dashboard.fxml")
                ));
                timeline.play();
            } else {
                // User doesn't exist, create account automatically
                UserDTO newUser = new UserDTO();
                newUser.setName(userInfo.getName());
                newUser.setEmail(userInfo.getEmail());
                newUser.setUsername(generateUsernameFromEmail(userInfo.getEmail()));
                newUser.setPassword("GOOGLE_AUTH"); // Special marker for Google auth users

                if (UserModel.saveUser(newUser)) {
                    showSuccessMessage("Account created! Welcome, " + userInfo.getName() + "!");
                    Timeline timeline = new Timeline(new KeyFrame(
                            Duration.seconds(1),
                            ae -> loadUI("/view/Dashboard.fxml")
                    ));
                    timeline.play();
                } else {
                    showErrorMessage("Failed to create account. Please try again.");
                }
            }
        } catch (SQLException ex) {
            showErrorMessage("Database error: " + ex.getMessage());
        }
    }

    private String generateUsernameFromEmail(String email) {
        // Extract username part from email and make it unique
        String username = email.split("@")[0];
        return username + "_google";
    }
    private void showErrorMessage(String message) {
        lblMessege.setText(message);
        lblMessege.setStyle("-fx-text-fill: red; -fx-font-size: 14px;");
        lblMessege.setVisible(true);

        Timeline timeline = new Timeline(new KeyFrame(
                Duration.seconds(3),
                ae -> {
                    lblMessege.setText("");
                    lblMessege.setVisible(false);
                }
        ));
        timeline.play();
    }

    private void showSuccessMessage(String message) {
        lblMessege.setText(message);
        lblMessege.setStyle("-fx-text-fill: green; -fx-font-size: 14px;");
        lblMessege.setVisible(true);
    }

    private void showInfoMessage(String message) {
        lblMessege.setText(message);
        lblMessege.setStyle("-fx-text-fill: blue; -fx-font-size: 14px;");
        lblMessege.setVisible(true);
    }
}