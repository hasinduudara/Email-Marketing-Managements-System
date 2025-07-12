module lk.ijse.groupproject.emms {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires lombok;
    requires java.mail;
    requires com.jfoenix;
    requires com.google.api.client;
    requires com.google.api.client.json.gson;
    requires google.api.client;
    requires com.google.api.client.auth;
    requires com.google.api.services.oauth2;
    requires java.desktop;

    requires com.google.api.client.extensions.jetty.auth;
    requires com.google.api.client.extensions.java6.auth;
    requires itextpdf;


    opens lk.ijse.groupproject.emms to javafx.fxml;
    exports lk.ijse.groupproject.emms;
    opens lk.ijse.groupproject.emms.controller to javafx.fxml;
    exports lk.ijse.groupproject.emms.controller;
    opens lk.ijse.groupproject.emms.dto.tm to javafx.base;
}