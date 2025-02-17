package com.fl.findthepitch.view;

import com.fl.findthepitch.controller.SceneManager;
import com.fl.findthepitch.controller.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
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

    @FXML
    private VBox settingsMenu;

    private boolean isSettingsMenuVisible = false;

    @FXML
    public void toggleSettingsMenu(ActionEvent event) {
        isSettingsMenuVisible = !isSettingsMenuVisible;
        settingsMenu.setVisible(isSettingsMenuVisible);

        // Rotate the settings button when toggled
        if (isSettingsMenuVisible) {
            settings.setStyle("-fx-rotate: 90;");
        } else {
            settings.setStyle("-fx-rotate: 0;");
        }
    }

    @FXML
    public void goToAccount(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/accountView.fxml"));
        AnchorPane accountRoot = loader.load();
        Scene accountScene = new Scene(accountRoot);

        // Push current scene to stack
        SceneManager.pushScene(search.getScene());

        Stage stage = (Stage) root.getScene().getWindow();
        stage.setScene(accountScene);
    }

    @FXML
    public void logout(ActionEvent event) {
        System.out.println("Logging out...");
        // Clear the current username
        SessionManager.clearUsername();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/MainView.fxml"));
            AnchorPane mainRoot = loader.load();
            Scene mainScene = new Scene(mainRoot);

            Stage stage = (Stage) root.getScene().getWindow();
            stage.setScene(mainScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void goTonewMap(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/newMap.fxml"));
        AnchorPane newRoot = loader.load();
        Scene newScene = new Scene(newRoot);

        // Push the current scene to the stack
        SceneManager.pushScene(search.getScene());

        Stage stage = (Stage) root.getScene().getWindow();
        stage.setScene(newScene);
    }

    @FXML
    public void loadAddFieldPage(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/addField.fxml"));
        AnchorPane newRoot = loader.load();
        Scene newScene = new Scene(newRoot);

        // Push the current scene to the stack
        SceneManager.pushScene(search.getScene());

        Stage stage = (Stage) root.getScene().getWindow();
        stage.setScene(newScene);
    }
}
