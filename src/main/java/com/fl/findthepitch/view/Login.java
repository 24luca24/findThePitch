package com.fl.findthepitch.view;

import com.fl.findthepitch.model.UserData;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Login extends Application {

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

    public void start(Stage stage) throws IOException {

        //Load FXML file and set the scene
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Login.fxml"));
        AnchorPane root = loader.load();

        //Set up the scene with the loaded root element
        Scene scene = new Scene(root);
        stage.setTitle("Login Page");
        stage.setScene(scene);
        stage.show();
    }

}
