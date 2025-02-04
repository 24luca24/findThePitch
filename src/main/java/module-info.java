module com.fl.findthepitch {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.auth.oauth2;
    requires com.google.auth;
    requires jbcrypt;
    requires java.sql;

    opens com.fl.findthepitch to javafx.fxml;
    exports com.fl.findthepitch;
    exports com.fl.findthepitch.view;
    exports com.fl.findthepitch.model;
    exports com.fl.findthepitch.controller;
}