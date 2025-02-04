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

public class Registration extends Application {

    @FXML
    private TextField name;

    @FXML
    private TextField surname;

    @FXML
    private TextField email;

    @FXML
    private TextField age;

    @FXML
    private TextField username;

    @FXML
    private PasswordField password;

    @FXML
    private Button register;

    @FXML
    private void sendRegisteredData() {
        String name = this.name.getText();
        String surname = this.surname.getText();
        String email = this.email.getText();
        int age = Integer.parseInt(this.age.getText());
        String username = this.username.getText();
        String password = this.password.getText();

        // Packing data into a UserData object
        UserData userData = new UserData(name, surname, username, email, password, age);

        // Make a query to save the object in the database
        System.out.println("Data sent to the database");
    }


    @Override
    public void start(Stage stage) throws Exception {

        //Load FXML file and set the scene
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Registration.fxml"));
        AnchorPane root = loader.load();

        //Set up the scene with the loaded root element
        Scene scene = new Scene(root);
        stage.setTitle("Find The Pitch");
        stage.setScene(scene);
        stage.show();
    }
}
