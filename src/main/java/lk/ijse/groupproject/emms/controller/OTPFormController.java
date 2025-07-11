package lk.ijse.groupproject.emms.controller;

import com.jfoenix.controls.JFXButton;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Objects;

public class OTPFormController {

    @FXML
    private JFXButton btnVerify;

    @FXML
    private ImageView girl;

    @FXML
    private ImageView imgBack;

    @FXML
    private Label lblError;

    @FXML
    private Label lblResend;

    @FXML
    private Label lblResendTimer;

    @FXML
    private AnchorPane rootPane;

    @FXML
    private TextField txtOtp1;

    @FXML
    private TextField txtOtp2;

    @FXML
    private TextField txtOtp3;

    @FXML
    private TextField txtOtp4;

    private int secondsRemaining = 120;

    private static final String CORRECT_OTP = ForgetPasswordController.otpGenerated;

    @FXML
    public void initialize() {
        txtOtp1.requestFocus();
        lblResend.setDisable(true);
        btnVerify.setDisable(true);
        startTimer();

        txtOtp1.addEventFilter(KeyEvent.KEY_TYPED, this::handleKeyTyped);
        txtOtp2.addEventFilter(KeyEvent.KEY_TYPED, this::handleKeyTyped);
        txtOtp3.addEventFilter(KeyEvent.KEY_TYPED, this::handleKeyTyped);
        txtOtp4.addEventFilter(KeyEvent.KEY_TYPED, this::handleKeyTyped);

        txtOtp1.textProperty().addListener((observable, oldValue, newValue) -> moveToNextField());
        txtOtp2.textProperty().addListener((observable, oldValue, newValue) -> moveToNextField());
        txtOtp3.textProperty().addListener((observable, oldValue, newValue) -> moveToNextField());
        txtOtp4.textProperty().addListener((observable, oldValue, newValue) -> moveToNextField());
    }

    private void handleKeyTyped(KeyEvent event) {
        if (!event.getCharacter().matches("\\d") || ((TextField) event.getSource()).getText().length() >= 1) {
            event.consume();
        }
    }

    @FXML
    void btnVerifyOnAction(ActionEvent event) {
        submitOtp();
    }

    @FXML
    void imgBackOnAction(MouseEvent event) {
        loadUI("/view/ForgetPassword.fxml");
    }

    @FXML
    void moveToNextField() {
        if (!txtOtp1.getText().isEmpty()) txtOtp2.requestFocus();
        if (!txtOtp2.getText().isEmpty()) txtOtp3.requestFocus();
        if (!txtOtp3.getText().isEmpty()) txtOtp4.requestFocus();
        checkFieldsFilled();
    }

    @FXML
    void resendOtp(MouseEvent event) {
        lblResend.setDisable(true);
        secondsRemaining = 120;
        startTimer();
    }

    @FXML
    void submitOtpOnEnter(KeyEvent event) {
        if (!txtOtp4.getText().isEmpty()) {
            submitOtp();
        }
    }

    @FXML
    void submitOtp() {
        System.out.println(CORRECT_OTP);
        System.out.println((txtOtp1.getText() + txtOtp2.getText() + txtOtp3.getText() + txtOtp4.getText()));
        if ((txtOtp1.getText() + txtOtp2.getText() + txtOtp3.getText() + txtOtp4.getText()).equals(CORRECT_OTP)) {

            loadUI("/view/OTPVerifiedForm.fxml");
        } else {
            showErrorMessage("Incorrect OTP. Please try again.");
        }
    }

    private void startTimer() {
        Timeline timer = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            secondsRemaining--;
            lblResendTimer.setText(String.format("Resend in %02d:%02d", secondsRemaining / 60, secondsRemaining % 60));

            if (secondsRemaining <= 0) {
                lblResend.setDisable(false);
            }
        }));
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();
    }

    private void checkFieldsFilled() {
        if (!txtOtp1.getText().isEmpty() &&
                !txtOtp2.getText().isEmpty() &&
                !txtOtp3.getText().isEmpty() &&
                !txtOtp4.getText().isEmpty()) {
            btnVerify.setDisable(false);
        } else {
            btnVerify.setDisable(true);
        }
    }

    private void showErrorMessage(String message) {
        lblError.setText(message);
        lblError.setStyle("-fx-text-fill: red; -fx-font-size: 14px; -fx-alignment: center");

        Timeline timeline = new Timeline(new KeyFrame(
                Duration.seconds(2),
                ae -> lblError.setText("")
        ));
        timeline.play();
    }

    private void loadUI(String resource) {
        rootPane.getChildren().clear();
        try {
            rootPane.getChildren().add(FXMLLoader.load(Objects.requireNonNull(getClass().getResource(resource))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
