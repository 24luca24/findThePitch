package com.fl.findthepitch.view;

import com.fl.findthepitch.controller.SceneManager;
import com.fl.findthepitch.controller.dbManager;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class MainView extends Application {

    @FXML
    private Button register;

    @FXML
    private Button login;

    @FXML
    private Button loginAsGuest;

    @Override
    public void start(Stage primaryStage) throws IOException {

        //Initialize the database //TODO: checkare se non ricrea le tabelle ogni volta

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/MainView.fxml"));
        AnchorPane root = loader.load();

        Scene scene = new Scene(root);
        primaryStage.setTitle("Find The Pitch");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    //Centralized method for switching scenes
    public void switchScene(String fxmlFile, String title, Button button) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
        AnchorPane newRoot = loader.load();
        Scene newScene = new Scene(newRoot);

        //Push the current scene to the stack
        SceneManager.pushScene(button.getScene());

        //Get the Stage from the button clicked
        Stage currentStage = (Stage) button.getScene().getWindow();
        currentStage.setScene(newScene);
        currentStage.setTitle(title);
    }

    @FXML
    private void goToRegisterPage() throws IOException{
        switchScene("/Registration.fxml", "Registration", register);
    }

    @FXML
    private void goToLoginPage() throws IOException {
        switchScene("/Login.fxml", "Login", login);
    }

    @FXML
    private void enterApp() {
        System.out.println("Login As Guest");
        // Add your logic for guest login
    }

    //Close the connection with the server when the application end
//    @Override
//    public void stop() throws Exception {
//        if (socket != null) socket.close();
//        if (out != null) out.close();
//        if (in != null) in.close();
//        super.stop();
//    }
}
