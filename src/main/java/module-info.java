module lk.ijse.groupproject.emms {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires lombok;
    requires java.mail;
    requires itextpdf;


    opens lk.ijse.groupproject.emms to javafx.fxml;
    exports lk.ijse.groupproject.emms;
    opens lk.ijse.groupproject.emms.controller to javafx.fxml;
    exports lk.ijse.groupproject.emms.controller;
}