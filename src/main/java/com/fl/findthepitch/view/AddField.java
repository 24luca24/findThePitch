package com.fl.findthepitch.view;

import com.fl.findthepitch.model.fieldTypeInformation.AreaType;
import com.fl.findthepitch.model.fieldTypeInformation.PitchType;
import com.fl.findthepitch.model.fieldTypeInformation.Price;
import com.fl.findthepitch.model.fieldTypeInformation.SurfaceType;
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
    ComboBox<Price> priceComboBox;

    @FXML
    ComboBox<AreaType> areaTypeComboBox;

    @FXML
    ComboBox<PitchType> pitchTypeComboBox;

    @FXML
    ComboBox<SurfaceType> surfaceTypeComboBox;

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
        initializeComboBox();
    }

    //Initialize map
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

    //initialize combobox
    private void initializeComboBox() {

        //AREA COMBOBOX
        this.areaTypeComboBox.getItems().addAll(AreaType.values());
        //Handle ComboBox selection
        this.areaTypeComboBox.setOnAction(e -> {
            AreaType selectedType = this.areaTypeComboBox.getValue();
            System.out.println("Selected: " + selectedType);
        });
        this.areaTypeComboBox.setValue(AreaType.INDOOR);

        //PRICE COMBOBOX
        this.priceComboBox.getItems().addAll(Price.values());
        this.priceComboBox.setOnAction(e -> {
            Price selectedPrice = this.priceComboBox.getValue();
            System.out.println("Selected: " + selectedPrice);
        });
        this.priceComboBox.setValue(Price.FREE);

        //PITCH TYPE COMBOBOX
        this.pitchTypeComboBox.getItems().addAll(PitchType.values());
        this.pitchTypeComboBox.setOnAction(e -> {
            PitchType selectedPitch = this.pitchTypeComboBox.getValue();
            System.out.println("Selected: " + selectedPitch);
        });
        this.pitchTypeComboBox.setValue(PitchType.FOOTBALL);

        //SURFACE TYPE COMBOBOX
        this.surfaceTypeComboBox.getItems().addAll(SurfaceType.values());
        this.surfaceTypeComboBox.setOnAction(e -> {
            SurfaceType selectedSurface = this.surfaceTypeComboBox.getValue();
            System.out.println("Selected: " + selectedSurface);
        });
        this.surfaceTypeComboBox.setValue(SurfaceType.GRASS);
    }

    //cleaning all field
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
        this.showerCheckBox.setSelected(false);
        this.parkingCheckBox.setSelected(false);
        this.lightingCheckBox.setSelected(false);
    }


}
