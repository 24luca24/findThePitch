package com.fl.findthepitch.view;

import com.fl.findthepitch.model.fieldTypeInformation.FieldType;
import com.gluonhq.maps.MapView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

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

    @FXML
    private Button back;

    MapView mapView;

    public void initialize() {

        //Initialize the ListView with some items
        listView.getItems().addAll("Item 1", "Item 2", "Item 3");

        //Fill the comboBox with type of field
        fillExpansionPanelContainer();

        //Initialize the map
        setMap();

        //Handle search button click
        //searchButton.setOnAction(e -> searchField());
    }

    private void setMap() {
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

    private void getUserPosition() {

    }

    @FXML
    private void refreshList(ActionEvent actionEvent) {
        System.out.println("Refreshing map...");

        // Remove existing map view
        mapContainer.getChildren().clear();

        // Reinitialize the map
        setMap();
    }

    public void backToMain() {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/DecisionView.fxml"));
            AnchorPane newRoot = loader.load();
            Scene newScene = new Scene(newRoot);

            Stage currentStage = (Stage) back.getScene().getWindow();
            currentStage.setScene(newScene);
            currentStage.setTitle("Main View");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error during switching scenes.");
        }
    }
}
