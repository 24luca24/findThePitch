package com.fl.findthepitch.view;

import com.fl.findthepitch.controller.dbManager;
import com.fl.findthepitch.model.UserData;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Registration {

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

    dbManager db = new dbManager();;

    @FXML
    private void sendRegisteredData() {
        List<String> errors = checkFields(); // Check fields for errors
        if (errors.isEmpty()) { // No errors, proceed with registration
            try {
                UserData userData = new UserData(
                        name.getText(),
                        surname.getText(),
                        username.getText(),
                        email.getText(),
                        password.getText(),
                        Integer.parseInt(age.getText())
                );

                if (db.registerUser(userData)) {
                    System.out.println("User registered successfully.");

                    //Clear all the text fields
                    name.clear();
                    surname.clear();
                    email.clear();
                    age.clear();
                    username.clear();
                    password.clear();

                    //Go back to the main view after successful registration
                    switchScene("/MainView.fxml", "Main View", register); // This is assuming the path to your main view is /MainView.fxml

                } else {
                    System.out.println("User registration failed.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error during registration.");
            }
        } else {
            // Show errors in an alert if there are any
            showAlert(errors);
        }
    }

    private List<String> checkFields() {
        List<String> errors = new ArrayList<>();
        if (this.name.getText().isEmpty()) {
            errors.add("Name field is empty");
        }
        if (this.surname.getText().isEmpty()) {
            errors.add("Surname field is empty");
        }
        if(this.age.getText().isEmpty() || !checkAgeIsInteger()){ //age not an int
            errors.add("Age field is empty");
        }
        if (this.email.getText().isEmpty() || !checkEmailIsValid()){ //email not valid)
            errors.add("Email field is empty OR not valid");
        }
        if (this.username.getText().isEmpty() || checkUsernameExist()){ //username not valid
            errors.add("Username field is empty OR already exists");
        }
        if (this.password.getText().isEmpty() || !checkPasswordIsValid()) {
            errors.add("Password field is empty OR not valid (1 Uppercase letter, 1 special character, 8 characters long)");
        }

        return errors;
    }

    //SE TRUE AGE E'INTERO
    private boolean checkAgeIsInteger() {
        try {
            Integer.parseInt(this.age.getText());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    //IF EMAIL MATCHES RETURN TRUE
    private boolean checkEmailIsValid() {
        String email = this.email.getText();
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }

    //SE TRUE USERNAME GIA ESISTENTE
    private boolean checkUsernameExist (){
        String username = this.username.getText();
        boolean result = db.checkUsername(username);
        if(result){
            return true;
        }
        return false;
    }

    //IF PASSWORD MATCHES RETURN TRUE
    private boolean checkPasswordIsValid() {
        String password = this.password.getText();
        String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*(),.?\":{}|<>]).{8,}$";
        return password.matches(passwordRegex);
    }

    //Show an alert with the error messages
    private void showAlert(List<String> errors) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Registration Errors");
        alert.setHeaderText("There were some errors with your registration:");

        // Join errors into a single string separated by line breaks
        StringBuilder errorMessages = new StringBuilder();
        for (String error : errors) {
            errorMessages.append(error).append("\n");
        }

        alert.setContentText(errorMessages.toString());
        alert.showAndWait();
    }

    //Centralized method for switching scenes
    private void switchScene(String fxmlFile, String title, Button button) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
        AnchorPane newRoot = loader.load();
        Scene newScene = new Scene(newRoot);

        // Get the Stage from the button clicked
        Stage currentStage = (Stage) button.getScene().getWindow();
        currentStage.setScene(newScene);
        currentStage.setTitle(title);
    }
}

