package com.fl.findthepitch.controller;

import com.fl.findthepitch.model.fieldTypeInformation.FieldType;
import com.gluonhq.maps.MapView;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;

public class MapViewController {

    @FXML
    private AnchorPane mapContainer;

    @FXML
    private ListView<String> listView;

    @FXML
    private Button searchButton;

    @FXML
    private TextField capField;

    @FXML
    private ComboBox<FieldType> fieldTypeComboBox;

    @FXML
    private HBox hbox;

    public void initialize() {

        listView.getItems().addAll("Item 1", "Item 2", "Item 3");

        fillExpansionPanelContainer();

        //initialize map and add it to the mapContainer
        MapView mapView = new MapView();
        mapView.setCenter(37.7749, -122.4194); // Example coordinates (San Francisco)
        mapView.setZoom(10);
        mapView.setPrefSize(mapContainer.getWidth(), mapView.getHeight());
        mapContainer.getChildren().add(mapView);

        //Make the map glue to the anchor pane
        AnchorPane.setTopAnchor(mapView, 0.0);
        AnchorPane.setLeftAnchor(mapView, 0.0);
        AnchorPane.setBottomAnchor(mapView, 0.0);
        AnchorPane.setRightAnchor(mapView, 0.0);

        // Make sure the map adjusts its size when the container size changes
        mapContainer.widthProperty().addListener((obs, oldWidth, newWidth) -> {
            mapView.setPrefWidth(newWidth.doubleValue());
        });
        mapContainer.heightProperty().addListener((obs, oldHeight, newHeight) -> {
            mapView.setPrefHeight(newHeight.doubleValue());
        });

        searchButton.setOnAction(e -> searchField());

    }

    private void fillExpansionPanelContainer() {
        fieldTypeComboBox.getItems().addAll(FieldType.values());

        //handle selection
        fieldTypeComboBox.setOnAction(e -> {
            FieldType selectedType = (FieldType) fieldTypeComboBox.getValue();
            System.out.println("Selected: " + selectedType);
        });
    }

    @FXML
    private void searchField() {
        System.out.println("Search button clicked!");
        Platform.runLater(() -> listView.getItems().add("New Search Result"));
    }


}
