package com.fl.findthepitch.view;

import com.fl.findthepitch.controller.SceneManager;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.io.IOException;


public class MainView extends Application {

    @FXML
    private Button register;

    @FXML
    private Button login;

    @FXML
    private Button loginAsGuest;

    @Override
    public void start(Stage primaryStage) throws IOException {

        //Initialize the database
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/MainView.fxml"));
        AnchorPane root = loader.load();

        Scene scene = new Scene(root);
        primaryStage.setTitle("Find The Pitch");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @FXML
    private void goToRegisterPage() throws IOException{
        SceneManager.switchScene("/Registration.fxml", "Registration", register);
    }

    @FXML
    private void goToLoginPage() throws IOException {
        SceneManager.switchScene("/Login.fxml", "Login", login);
    }

    @FXML
    private void enterApp() throws IOException {
        System.out.println("Login As Guest");
        SceneManager.switchScene("/newMap.fxml", "SearchMap", loginAsGuest);
    }
}
