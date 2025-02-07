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

public class Login {

    @FXML
    private AnchorPane root;

    @FXML
    private TextField username;

    @FXML
    private PasswordField password;

    @FXML
    private Button login;

    @FXML
    private Button back;

    @FXML
    public void initialize() {
        // Detect swipe right to go back
        root.setOnSwipeRight(event -> goBack());
    }

    private void goBack() {
        Scene previousScene = SceneManager.popScene();
        if (previousScene != null) {
            Stage stage = (Stage) root.getScene().getWindow();
            stage.setScene(previousScene);
        }
    }

    @FXML
    private void sendLoginData() {
        try {
            dbManager db = new dbManager();
            String enteredUsername = username.getText();
            String enteredPassword = password.getText();

            if (db.validateLogin(enteredUsername, enteredPassword)) {
                System.out.println("Login successful!");

                //Clear all the text fields
                username.clear();
                password.clear();

                Stage currentStage = (Stage) login.getScene().getWindow();
                SceneManager.pushScene(currentStage.getScene()); //Store current scene before switching

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/newMap.fxml"));
                AnchorPane root = loader.load();
                Scene newScene = new Scene(root);

                currentStage.setScene(newScene);
                currentStage.setTitle("Map View");
            } else {
                System.out.println("Invalid username or password.");
                new Alert(Alert.AlertType.ERROR, "Invalid username or password.").show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error during login.");
        }
    }

    public void backToMain() {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/MainView.fxml"));
            AnchorPane newRoot = loader.load();
            Scene newScene = new Scene(newRoot);

            Stage currentStage = (Stage) back.getScene().getWindow();
            currentStage.setScene(newScene);
            currentStage.setTitle("Main View");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error during switching scenes.");
        }
    }
}
