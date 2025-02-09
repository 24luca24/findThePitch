package com.fl.findthepitch.view;

import com.fl.findthepitch.controller.SceneManager;
import com.fl.findthepitch.controller.dbManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class UserView {
    @FXML
    private AnchorPane root;

    @FXML
    private Button search;

    @FXML
    private Button settings;

    @FXML
    private Button add;

    public void goTonewMap(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/newMap.fxml"));
        AnchorPane newRoot = loader.load();
        Scene newScene = new Scene(newRoot);

        //Push the current scene to the stack
        SceneManager.pushScene(search.getScene());

        Stage stage = (Stage) root.getScene().getWindow();
        stage.setScene(newScene);
    }
}
