module com.fl.findthepitch {
    requires javafx.fxml;
    requires javafx.controls;
    requires javafx.base;       // ✅ Ensure JavaFX base is explicitly required
    requires javafx.graphics;   // ✅ Ensure JavaFX graphics is explicitly required

    requires com.google.auth.oauth2;
    requires com.google.auth;
    requires jbcrypt;
    requires java.sql;
    requires com.gluonhq.maps;
    requires com.gluonhq.charm.glisten; // Required for Gluon UI controls
    requires com.gluonhq.attach.util;
    requires transitive org.controlsfx.controls;

    // Open packages for JavaFX reflection
    opens com.fl.findthepitch.view to javafx.fxml;
    opens com.fl.findthepitch.controller to javafx.fxml;
    opens com.fl.findthepitch.model to javafx.fxml;
    opens com.fl.findthepitch to javafx.fxml;

    // Export packages for external use
    exports com.fl.findthepitch;
    exports com.fl.findthepitch.view;
    exports com.fl.findthepitch.model;
    exports com.fl.findthepitch.controller;
    exports com.fl.findthepitch.model.fieldTypeInformation;
}
