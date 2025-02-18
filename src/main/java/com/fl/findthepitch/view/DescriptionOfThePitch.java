package com.fl.findthepitch.view;

import com.fl.findthepitch.controller.SceneManager;
import com.fl.findthepitch.controller.ServerConnection;
import com.fl.findthepitch.controller.dbManager;
import com.fl.findthepitch.model.PitchData;
import com.fl.findthepitch.model.PitchSession;
import javafx.application.HostServices;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;

public class DescriptionOfThePitch {

    @FXML
    Button backButton;

    @FXML
    Button googleMapsButton;

    @FXML
    ImageView image;

    @FXML
    TextArea textArea;

    private HostServices hostServices;

    @FXML
    Label labelName;

    @FXML
    Label labelAddress;

    @FXML
    Label labelCity;

    @FXML
    Label labelAreaType;

    @FXML
    Label labelPrice;

    @FXML
    Label labelHasParking;

    @FXML
    Label labelHasLight;

    @FXML
    Label labelOpenTime;

    @FXML
    Label labelLunchBrake;

    @FXML
    Label labelLunchEnd;

    @FXML
    Label labelClosingTIme;

    @FXML
    Label labelPhone;

    @FXML
    Label labelWebsite;

    @FXML
    Label labelEmail;

    @FXML
    Label labelPitch;

    @FXML
    Label labelSurface;

    PitchSession ps;
    dbManager dbManager = new dbManager();

    //Setter that will be called by the Application class
    public void setHostServices(HostServices hostServices) {
        this.hostServices = hostServices;
    }

    public void initialize() {
        ps = PitchSession.getInstance(); // Ensure ps is initialized
        if (ps == null) {
            System.err.println("Error: PitchSession instance is null!");
        } else {
            retrievePitchValues();
        }
    }

    @FXML
    private void goToGoogleMaps() {
        if (hostServices != null) {
            hostServices.showDocument("https://www.google.com/maps");
        } else {
            System.err.println("HostServices not set in the controller!");
        }
    }

    @FXML
    private void goBack() {
        SceneManager.switchScene("/newMap.fxml", "Search Field", backButton);
    }

    private void retrievePitchValues() {
        // Check if PitchSession and PitchData exist
        if (ps == null || ps.getPitchData() == null) {
            System.err.println("Error: No pitch data available.");
            showErrorAlert("Pitch data is unavailable. Please try again.");
            return;
        }

        // Create a new Task to fetch pitch data asynchronously
        Task<PitchData> returnPitchTask = new Task<>() {
            @Override
            protected PitchData call() throws Exception {
                PitchData dataToSend = new PitchData(
                        ps.getPitchData().getName(),
                        ps.getPitchData().getAddress(),
                        ps.getPitchData().getCity(),
                        ps.getPitchData().getPitchType()
                );

                System.out.println("Sending request to retrieve pitch data: " + dataToSend.getName());
                Object serverResponse = ServerConnection.sendCommandObj("RETRIEVEPITCH", dataToSend);

                // Ensure the response is of the correct type
                if (serverResponse instanceof PitchData) {
                    return (PitchData) serverResponse;
                } else {
                    System.err.println("Invalid response from server: " + serverResponse);
                    return null;
                }
            }
        };

        //Handle task success
        returnPitchTask.setOnSucceeded(event -> {
            PitchData response = returnPitchTask.getValue();
            if (response != null) {
                System.out.println("Pitch retrieved successfully.");
                updateUIWithPitchData(response);
            } else {
                System.err.println("Pitch retrieval failed.");
                showErrorAlert("Pitch retrieval failed. Please try again.");
            }
        });

        // Handle task failure
        returnPitchTask.setOnFailed(event -> {
            Throwable ex = returnPitchTask.getException();
            ex.printStackTrace();
            showErrorAlert("An error occurred while loading the pitch description: " + ex.getMessage());
        });

        // Start the task in a new background thread
        Thread retrievePitchThread = new Thread(returnPitchTask);
        retrievePitchThread.setDaemon(true);
        retrievePitchThread.start();
    }

    // Helper method to show error alerts
    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void updateUIWithPitchData(PitchData pitchData) {
        labelName.setText(pitchData.getName());
        labelAddress.setText(pitchData.getAddress());
        labelCity.setText(pitchData.getCity());
        labelAreaType.setText((pitchData.areaType()).toString());
        labelPrice.setText((pitchData.getPrice()).toString());
        labelOpenTime.setText(pitchData.getOpeningTime().toString());
        labelLunchBrake.setText(pitchData.getLunchBrakeStart().toString());
        labelLunchEnd.setText(pitchData.getLunchBrakeEnd().toString());
        labelLunchEnd.setText(pitchData.getLunchBrakeEnd().toString());
        labelClosingTIme.setText(pitchData.getClosingTime().toString());
        labelPhone.setText(pitchData.getPhoneNumber());
        labelWebsite.setText(pitchData.getWebsite());
        labelEmail.setText(pitchData.getEmail());
        labelPitch.setText(pitchData.getPitchType().toString());

        if(pitchData.isHasParking()) {
            labelHasParking.setText("yes");
        } else {
            labelHasParking.setText("no");
        }

        if(pitchData.isCanShower()) {
            labelHasLight.setText("yes");
        } else {
            labelHasLight.setText("no");
        }

        if(pitchData.isHasLighting()) {
            labelHasLight.setText("yes");
        } else {
            labelHasLight.setText("no");
        }
    }
}