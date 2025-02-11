package com.fl.findthepitch.view;

import com.fl.findthepitch.controller.dbManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class AccountView {

    @FXML
    private Label nameLabel;
    @FXML
    private Label surnameLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private Label usernameLabel;

    public void initialize() {
        // Fetch user info from database
        String username = "currentUser";  // Replace with actual session username
        String[] userInfo = dbManager.getUserInfo(username);

        if (userInfo != null) {
            nameLabel.setText("Name: " + userInfo[0]);
            surnameLabel.setText("Surname: " + userInfo[1]);
            emailLabel.setText("Email: " + userInfo[2]);
            usernameLabel.setText("Username: " + userInfo[3]);
        }
    }

    @FXML
    public void goBack() {
        Stage stage = (Stage) nameLabel.getScene().getWindow();
        stage.close(); // Close account view
    }
}
