module com.fl.findthepitch {
    requires javafx.controls;
    requires javafx.fxml;
    requires firebase.admin;
    requires com.google.auth.oauth2;
    requires com.google.auth;
    requires google.cloud.firestore;


    opens com.fl.findthepitch to javafx.fxml;
    exports com.fl.findthepitch;
}