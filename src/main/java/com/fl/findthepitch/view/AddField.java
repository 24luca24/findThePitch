package com.fl.findthepitch.view;

import com.gluonhq.maps.MapView;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

public class AddField {

    @FXML
    AnchorPane mapContainer;

    @FXML
    ScrollPane scrollPane;

    @FXML
    TextField nameField;

    @FXML
    TextField cityField;

    @FXML
    TextField addressField;

    @FXML
    TextField phoneField;

    @FXML
    TextField websiteField;

    @FXML
    TextField emailField;

    @FXML
    CheckBox indoorCheckBox;

    @FXML
    CheckBox showerCheckBox;

    @FXML
    CheckBox parkingCheckBox;

    @FXML
    CheckBox lightingCheckBox;

    @FXML
    TextField openTimeField;

    @FXML
    TextField lunchStartField;

    @FXML
    TextField lunchEndField;

    @FXML
    TextField closeTimeField;

    @FXML
    ComboBox priceComboBox;

    @FXML
    ComboBox pitchTypeComboBox;

    @FXML
    ComboBox surfaceTypeComboBox;

    @FXML
    TextArea descriptionField;

    @FXML
    Button imageButton;

    @FXML
    Button clearButton;

    @FXML
    Button submitButton;

    @FXML
    Button goBackButton;

    @FXML
    Button goToSearchButton;

    MapView mapView;

    public void initialize() {
        initializeMap();
    }

    private void initializeMap() {
        mapView = new MapView();
        mapView.setCenter(45.53333,  9.2); // Example: Gorla Minore
        mapView.setZoom(15);

        //Set the map's size to match its container
        mapView.setPrefSize(mapContainer.getPrefWidth(), mapContainer.getPrefHeight());
        mapContainer.getChildren().add(mapView);

        //Adjust MapView size if the container size changes
        mapContainer.widthProperty().addListener((obs, oldWidth, newWidth) ->
                mapView.setPrefWidth(newWidth.doubleValue())
        );
        mapContainer.heightProperty().addListener((obs, oldHeight, newHeight) ->
                mapView.setPrefHeight(newHeight.doubleValue())
        );
    }

    private void clearField() {
        this.nameField.clear();
        this.cityField.clear();
        this.addressField.clear();
        this.phoneField.clear();
        this.websiteField.clear();
        this.emailField.clear();
        this.openTimeField.clear();
        this.lunchStartField.clear();
        this.lunchEndField.clear();
        this.closeTimeField.clear();
        this.descriptionField.clear();
        this.indoorCheckBox.setSelected(false); //!WRONG should be a select
        this.showerCheckBox.setSelected(false);
        this.parkingCheckBox.setSelected(false);
        this.lightingCheckBox.setSelected(false);
    }
}
