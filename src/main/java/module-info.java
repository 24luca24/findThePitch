module com.fl.findthepitch {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.fl.findthepitch to javafx.fxml;
    exports com.fl.findthepitch;
}