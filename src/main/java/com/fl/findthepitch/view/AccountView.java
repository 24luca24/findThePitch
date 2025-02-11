package com.fl.findthepitch.view;

import com.fl.findthepitch.controller.SceneManager;
import com.fl.findthepitch.controller.SessionManager;
import com.fl.findthepitch.controller.dbManager;
import javafx.fxml.FXML;
import javafx.scene.Scene;
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
        String username = SessionManager.getCurrentUsername();
        String[] userInfo = dbManager.getUserInfo(username);

        if (userInfo != null) {
            nameLabel.setText(userInfo[0]);
            surnameLabel.setText(userInfo[1]);
            emailLabel.setText(userInfo[2]);
            usernameLabel.setText(userInfo[3]);
        }
    }

    @FXML
    public void goBack() {
            Scene previousScene = SceneManager.popScene();
            if (previousScene != null) {
                Stage stage = (Stage) nameLabel.getScene().getWindow();
                stage.setScene(previousScene);
        }
    }
}
