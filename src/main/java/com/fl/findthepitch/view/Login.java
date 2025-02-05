package com.fl.findthepitch.view;

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
        String username = this.username.getText();
        String password = this.password.getText();

        //Packing data into a UserData object
        UserData userData = new UserData(username, password);

        // Make a query to the database to check if the user exists
        System.out.println("Data sent to the database");
    }
}
