package com.fl.findthepitch.view;

import com.fl.findthepitch.controller.SceneManager;
import com.fl.findthepitch.controller.Server;
import com.fl.findthepitch.controller.dbManager;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.LoadException;
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

    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;


    @Override
    public void start(Stage primaryStage) throws IOException {

        //Initialize the database //TODO: checkare se non ricrea le tabelle ogni volta
        dbManager db = new dbManager();
        db.createUserTable();
        db.createPitchTable();

        db.createMunicipalityTable();
        //insert path to gi_comuni_cap.csv
        String filePath = getClass().getResource("/CitiesCoordinates/gi_comuni_cap.csv").getPath();
        db.uploadDataFromCSV(filePath);

        //Load FXML file and set the scene
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/MainView.fxml"));
        AnchorPane root = loader.load();

        //Set up the scene with the loaded root element
        Scene scene = new Scene(root);
        primaryStage.setTitle("Find The Pitch");
        primaryStage.setScene(scene);
        primaryStage.show();

        connectToServer();
    }

    //TODO: see if works
    //Centralized method for switching scenes
    private void switchScene(String fxmlFile, String title, Button button) throws IOException {
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

    //Method for Register Button
    @FXML
    private void goToRegisterPage() throws IOException{
        switchScene("/Registration.fxml", "Registration", register);
    }

    //Method for Login Button
    @FXML
    private void goToLoginPage() throws IOException {
        switchScene("/Login.fxml", "Login", login);
    }

    //Method for Login As Guest Button
    @FXML
    private void enterApp() {
        System.out.println("Login As Guest");
        // Add your logic for guest login
    }

    private void connectToServer() {
        try {
            socket = new Socket("localhost", Server.PORT);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            System.out.println("Connected to server.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
