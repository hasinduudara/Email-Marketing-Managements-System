package lk.ijse.groupproject.emms.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class MailFilterDashboardController {

    @FXML
    private Button btnFilter;

    @FXML
    private Button btnSend;

    @FXML
    private ComboBox<?> cmbGender;

    @FXML
    private AnchorPane filterMailDashboard;

    @FXML
    private ListView<?> lstFilteredEmails;

    @FXML
    private TextArea txtBody;

    @FXML
    private TextField txtJob;

    @FXML
    private TextField txtMaxAge;

    @FXML
    private TextField txtMinAge;

    @FXML
    private TextField txtTitle;

    @FXML
    void btnFilterOnaction(ActionEvent event) {

    }

    @FXML
    void btnSendOnAction(ActionEvent event) {

    }

}
