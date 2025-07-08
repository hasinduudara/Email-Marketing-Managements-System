package lk.ijse.groupproject.emms.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

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

    }

    @FXML
    void goToLogin(MouseEvent event) {

    }

}
