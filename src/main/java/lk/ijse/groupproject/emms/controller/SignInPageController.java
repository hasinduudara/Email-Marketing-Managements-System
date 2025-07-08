package lk.ijse.groupproject.emms.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

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

    @FXML
    void btnSignInOnAction(ActionEvent event) {

    }

    @FXML
    void imgConfirmPasswordViewOnAction(MouseEvent event) {

    }

    @FXML
    void lblCreateAccOnClick(MouseEvent event) {

    }

    @FXML
    void lblForgotPasswordOnAction(MouseEvent event) {

    }

}
