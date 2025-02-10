package com.fl.findthepitch.view;

import com.fl.findthepitch.controller.SceneManager;
import com.fl.findthepitch.controller.ServerConnection;
import com.fl.findthepitch.controller.dbManager;
import com.fl.findthepitch.model.UserData;
import org.controlsfx.control.tableview2.filter.filtereditor.SouthFilter;
import org.controlsfx.control.textfield.TextFields;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class Registration {

    @FXML
    private AnchorPane root;

    @FXML
    private TextField name;

    @FXML
    private TextField surname;

    @FXML
    private TextField email;

    @FXML
    private TextField city;

    @FXML
    private TextField username;

    @FXML
    private PasswordField password;

    @FXML
    private Button register;

    @FXML
    private Button back;

    dbManager db = new dbManager();

    @FXML
    public void initialize() {
        //Enable autocomplete for the city field
        autoCompletionCity();

        //Detect swipe right to go back
        root.setOnSwipeRight(event -> goBack());
    }

    private void autoCompletionCity() {
        TextFields.bindAutoCompletion(city, request -> {
            String input = city.getText().trim();
            if (input.isEmpty()) {
                return new ArrayList<>(); //No suggestions if input is empty
            }
            return db.getCitySuggestions(input); //Fetch suggestions from DB
        });
    }

    private void goBack() {
        Scene previousScene = SceneManager.popScene();
        if (previousScene != null) {
            Stage stage = (Stage) root.getScene().getWindow();
            stage.setScene(previousScene);
        }
    }

    @FXML
    private void sendRegisteredData() {
        List<String> errors = checkFields();
        if (errors.isEmpty()) { //No errors, proceed with registration
            try {
                UserData userData = new UserData(
                        name.getText(),
                        surname.getText(),
                        username.getText(),
                        email.getText(),
                        password.getText(),
                        city.getText()
                );

                //Send registration data to the server
                String response = ServerConnection.sendCommand("REGISTER", userData);
                if("SUCCESS".equals(response)) {
                    System.out.println("User registered successfully.");
                    clearFields();
                    navigateToMainView();
                } else {
                    System.out.println("User registration failed");
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("IO Exception during registration: " + e.getMessage());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                System.out.println("ClassNotFoundException during registration: " + e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Unexpected error during registration: " + e.getMessage());
            }

        } else {
            showAlert(errors);
        }
    }

    private void clearFields() {
        name.clear();
        surname.clear();
        email.clear();
        city.clear();
        username.clear();
        password.clear();
    }

    private void navigateToMainView() throws IOException {
        Stage currentStage = (Stage) register.getScene().getWindow();
        SceneManager.pushScene(currentStage.getScene()); //Store current scene before switching

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/MainView.fxml"));
        AnchorPane newRoot = loader.load();
        Scene newScene = new Scene(newRoot);

        currentStage.setScene(newScene);
        currentStage.setTitle("Main View");
    }

    private List<String> checkFields() {
        List<String> errors = new ArrayList<>();

        if (this.name.getText().isEmpty()) {
            errors.add("Name field is empty");
        }
        if (this.surname.getText().isEmpty()) {
            errors.add("Surname field is empty");
        }
        if (this.city.getText().isEmpty()) {
            errors.add("City field is empty");
        } else if (!checkCityExist()) {
            errors.add("City does not exist");
        }
        if (this.email.getText().isEmpty()) {
            errors.add("Email field is empty");
        } else if (!checkEmailIsValid()) {
            errors.add("Invalid email format or domain does not exist");
        }
        if (this.username.getText().isEmpty()) {
            errors.add("Username field is empty");
        } else if (checkUsernameExist()) { // Check if the username already exists
            errors.add("Username already exists");
        }
        if (this.password.getText().isEmpty()) {
            errors.add("Password field is empty");
        } else if (!checkPasswordIsValid()) {
            errors.add("Password must have 1 uppercase letter, 1 special character, and be at least 8 characters long");
        }

        return errors;
    }

    //if true age exist
    private boolean checkCityExist() {
        return db.checkCity(this.city.getText().trim());
    }

    //if email is correct return true
    private boolean checkEmailIsValid() {
        String email = this.email.getText();
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

        if (!email.matches(emailRegex)) {
            return false; //Invalid email format
        }

        //Extract domain from email
        String domain = email.substring(email.indexOf("@") + 1);

        //Check if domain exists
        return isDomainValid(domain);
    }

    //Method to check if a domain is reachable
    private boolean isDomainValid(String domain) {
        try {
            InetAddress.getByName(domain); // Try resolving the domain
            return true; // If successful, the domain exists
        } catch (UnknownHostException e) {
            return false; // Domain does not exist
        }
    }

    //If username already exist return true
    private boolean checkUsernameExist() {
        return db.checkUsername(this.username.getText()); // Returns true if username already exists
    }


    //If password is valid return true
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

        //Join errors into a single string separated by line breaks
        StringBuilder errorMessages = new StringBuilder();
        for (String error : errors) {
            errorMessages.append(error).append("\n");
        }

        alert.setContentText(errorMessages.toString());
        alert.showAndWait();
    }

    public void backToMain() {
        try {
            Stage currentStage = (Stage) back.getScene().getWindow();
            SceneManager.pushScene(currentStage.getScene()); //Store current scene before switching

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/MainView.fxml"));
            AnchorPane newRoot = loader.load();
            Scene newScene = new Scene(newRoot);

            currentStage.setScene(newScene);
            currentStage.setTitle("Main View");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error during switching scenes.");
        }
    }
}

