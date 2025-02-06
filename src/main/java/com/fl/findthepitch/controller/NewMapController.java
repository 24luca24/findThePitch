package com.fl.findthepitch.controller;

import com.fl.findthepitch.model.fieldTypeInformation.FieldType;
import com.gluonhq.maps.MapView;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class NewMapController {

    @FXML
    private AnchorPane mapContainer;

    @FXML
    private ListView<String> list;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private TextField textField;

    @FXML
    private ComboBox<FieldType> fieldTypeComboBox;

    @FXML
    private AnchorPane root;

    @FXML
    private AnchorPane fixedBar;

    @FXML
    private Button buttonSearch;

    public void initialize() {
        //Initialize the ListView with items
        list.getItems().addAll("Item 1", "Item 2", "Item 3");

        fillExpansionPanelContainer();

        //Initialize MapView and add it to the mapContainer
        MapView mapView = new MapView();
        mapView.setCenter(37.7749, -122.4194); // Example coordinates (San Francisco)
        mapView.setZoom(10);
        mapView.setPrefSize(mapContainer.getWidth(), 380.0); // Set map height to match container height
        mapContainer.getChildren().add(mapView);

        //Adjust MapView size based on container size changes
        mapContainer.widthProperty().addListener((obs, oldWidth, newWidth) -> {
            mapView.setPrefWidth(newWidth.doubleValue());
        });
        mapContainer.heightProperty().addListener((obs, oldHeight, newHeight) -> {
            mapView.setPrefHeight(newHeight.doubleValue());
        });

        //Platform.runLater(() -> mapContainer.getChildren().add(mapView));
        //Handle search button click
        buttonSearch.setOnAction(e -> searchField());
    }

    private void fillExpansionPanelContainer() {
        fieldTypeComboBox.getItems().addAll(FieldType.values());

        //Handle ComboBox selection
        fieldTypeComboBox.setOnAction(e -> {
            FieldType selectedType = (FieldType) fieldTypeComboBox.getValue();
            System.out.println("Selected: " + selectedType);
        });
    }

    @FXML
    private void searchField() {
        System.out.println("Search button clicked!");
        Platform.runLater(() -> list.getItems().add("New Search Result"));
    }
}
