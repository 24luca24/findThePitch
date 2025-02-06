module com.fl.findthepitch {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.auth.oauth2;
    requires com.google.auth;
    requires jbcrypt;
    requires java.sql;
    requires java.desktop;
    requires com.gluonhq.maps;
    requires com.gluonhq.charm.glisten; // Required for Gluon UI controls
    requires com.gluonhq.attach.util;

    opens com.fl.findthepitch.view to javafx.fxml;
    opens com.fl.findthepitch.controller to javafx.fxml;
    opens com.fl.findthepitch.model to javafx.fxml;
    opens com.fl.findthepitch to javafx.fxml;
    exports com.fl.findthepitch;
    exports com.fl.findthepitch.view;
    exports com.fl.findthepitch.model;
    exports com.fl.findthepitch.controller;
}
