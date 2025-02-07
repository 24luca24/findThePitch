package com.fl.findthepitch.controller;

import com.fl.findthepitch.model.fieldTypeInformation.FieldType;
import com.gluonhq.maps.MapView;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

public class NewMapController {

    @FXML
    private AnchorPane mapContainer;

    @FXML
    private ListView<String> listView;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private TextField textFieldInsert;

    @FXML
    private ComboBox<FieldType> comboBox;

    @FXML
    private AnchorPane root;

    @FXML
    private AnchorPane searchbar;

    @FXML
    private Button searchButton;

    public void initialize() {

        //Initialize the ListView with some items
        listView.getItems().addAll("Item 1", "Item 2", "Item 3");

        //Fill the comboBox with type of field
        fillExpansionPanelContainer();

        //Initialize the map
        setMap();

        //Handle search button click
//        searchButton.setOnAction(e -> searchField());
    }

    private void setMap() {
        //Initialize MapView and add it to the mapContainer.
        MapView mapView = new MapView();
        mapView.setCenter(37.7749, -122.4194); // Example: San Francisco
        mapView.setZoom(10);

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
    private void fillExpansionPanelContainer() {
        comboBox.getItems().addAll(FieldType.values());

        //Handle ComboBox selection
        comboBox.setOnAction(e -> {
            FieldType selectedType = comboBox.getValue();
            System.out.println("Selected: " + selectedType);
        });

        comboBox.setValue(FieldType.FOOTBALL);
    }

    @FXML
    private void searchField() {
        System.out.println("Search button clicked!");
        // Example: add a new item to the ListView on search
        //Platform.runLater(() -> listView.getItems().add("New Search Result"));
    }
}
