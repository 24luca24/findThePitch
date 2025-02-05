package com.fl.findthepitch.view;

import com.fl.findthepitch.controller.dbManager;
import com.fl.findthepitch.model.UserData;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class Login {

    @FXML
    private TextField username;

    @FXML
    private PasswordField password;

    @FXML
    private Button login;

    @FXML
    private void sendLoginData() {
        try {
            dbManager db = new dbManager();
            String enteredUsername = username.getText();
            String enteredPassword = password.getText();

            if (db.validateLogin(enteredUsername, enteredPassword)) {
                System.out.println("Login successful!");
                //TODO: pass to the main panel (for registered user)
            } else {
                System.out.println("Invalid username or password.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error during login.");
        }
    }
}
