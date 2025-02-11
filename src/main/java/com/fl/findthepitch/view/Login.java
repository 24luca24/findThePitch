package com.fl.findthepitch.view;

import com.fl.findthepitch.controller.SceneManager;
import com.fl.findthepitch.controller.ServerConnection;
import com.fl.findthepitch.controller.dbManager;
import com.fl.findthepitch.model.UserData;
import javafx.concurrent.Task;
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

                Task<String> loginTask = new Task<String>() {
                    @Override
                    protected String call() throws Exception {
                        UserData userData = new UserData(
                                enteredUsername,
                                enteredPassword
                        );
                        return new ServerConnection().sendCommand("LOGIN", userData);
                    }
                };

                //When the task completes successfully, process the server response on the UI thread
                loginTask.setOnSucceeded(event -> {
                    String response = loginTask.getValue();
                    if ("SUCCESS".equals(response)) {
                        System.out.println("User registered successfully.");
                        //Clear all the text fields
                        username.clear();
                        password.clear();
                        try {
                            navigateToMainView();
                        } catch (IOException ex) {
                            ex.printStackTrace();

                        }
                    } else {
                        System.out.println("User login failed");
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Login Error");
                        alert.setHeaderText(null);
                        alert.setContentText("User login failed. Please try again.");
                        alert.showAndWait();
                    }
                });

                //Handle any exceptions that occur during the network call
                loginTask.setOnFailed(event -> {
                    Throwable ex = loginTask.getException();
                    ex.printStackTrace();
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Login Error");
                    alert.setHeaderText("An error occurred during login.");
                    alert.setContentText(ex.getMessage());
                    alert.showAndWait();
                });

                //Start the Task in a new thread
                Thread registrationThread = new Thread(loginTask);
                registrationThread.setDaemon(true); // Optional: allow the application to exit if this is the only thread running
                registrationThread.start();


            } else {
                System.out.println("Invalid username or password.");
                new Alert(Alert.AlertType.ERROR, "Invalid username or password.").show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error during login.");
        }
    }

    private void navigateToMainView() throws IOException {
        SceneManager.switchScene("/DecisionView.fxml", "DecisionPanel", login);
    }

    public void backToMain() {
        SceneManager.switchScene("/mainView.fxml", "MainView", back);
    }
}
