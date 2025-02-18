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

import java.io.IOException;
import java.util.List;

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

    PitchSession pc;

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

    dbManager dbManager = new dbManager();

    //Setter that will be called by the Application class
    public void setHostServices(HostServices hostServices) {
        this.hostServices = hostServices;
    }

    public void initialize() {
        updateUIWithPitchData(retrievePitchValues());
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
        SceneManager.switchScene("/NewMapController.fxml", "Search Field", backButton);
    }

    private PitchData retrievePitchValues() {
        if(pc == null) {
            System.err.println("error no data");
        }

        Task<PitchData> returnPitchTask = new Task<PitchData>() {
            @Override
            protected PitchData call() throws Exception {
                PitchData dataToSend = new PitchData(pc.getPitchData().getName(), pc.getPitchData().getCity(), pc.getPitchData().getCity(), pc.getPitchData().getSurfaceType());
                return (PitchData) ServerConnection.sendCommandObj("RETRIEVEPITCH", dataToSend);
                }
            };

            //When the task completes successfully, process the server response on the UI thread
            returnPitchTask.setOnSucceeded(event -> {
                PitchData response = returnPitchTask.getValue();
                if (response != null) {
                    System.out.println("Pitch retrieved successfully.");
                    updateUIWithPitchData(response);
                } else {
                    System.out.println("Pitch retrieval fail");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Pitch Retrieval Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Pitch Retrieval failed. Please try again.");
                    alert.showAndWait();
                }
            });
            return null; //TO FIX
        }

    private void showAlert(List<String> strings) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Pitch Retrieving");
        alert.setContentText("No pitch retrieved");
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