package com.fl.findthepitch.view;

import com.fl.findthepitch.controller.SceneManager;
import com.fl.findthepitch.controller.dbManager;
import com.fl.findthepitch.model.PitchData;
import com.fl.findthepitch.model.PitchSession;
import com.fl.findthepitch.model.UserData;
import com.fl.findthepitch.model.UserSession;
import com.fl.findthepitch.model.fieldTypeInformation.PitchType;
import com.fl.findthepitch.service.GeocodingService;
import com.gluonhq.maps.MapLayer;
import com.gluonhq.maps.MapPoint;
import com.gluonhq.maps.MapView;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.controlsfx.control.textfield.TextFields;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private ComboBox<PitchType> comboBox;

    @FXML
    private AnchorPane searchbar;

    @FXML
    private Button searchButton;

    @FXML
    private Button back;

    GeocodingService geocodingService = new GeocodingService();

    private MapView mapView;

    dbManager db = new dbManager();

    PitchSession ps;

    private static String city;

    private final List<MapLayer> activeMarkers = new ArrayList<>();

    public void initialize() {

        //Initialize the ListView with some items
        listView.getItems().addAll(getPitchInUserPosition());

        //Fill the comboBox with type of field
        fillComboBox();

        //Initialize the map
        initializeMap();

        autoCompletionCity();

    }

    private List<String> getPitchInUserPosition() {
        UserData currentUser = UserSession.getInstance().getUserData();
        if (currentUser != null) {
            city = currentUser.getCity();
            System.out.println("User's city: " + city);
        } else {
            System.out.println("No user is logged in.");
        }
        return db.retrievePitch(city);
    }

    private void initializeMap() {
        mapView = new MapView();

        //Load football field on the city of the user
        if (city == null || city.isEmpty()) {
            city = "Milano";
            addPitchMarkers(city); //to handle non-logged user we retrieve Milan pitch
        } else {
            addPitchMarkers(city);
        }

        //Set dimension to adapt to the container
        mapView.setPrefSize(mapContainer.getPrefWidth(), mapContainer.getPrefHeight());
        mapContainer.getChildren().add(mapView);

        //Anchor the mapView to all side of container
        AnchorPane.setTopAnchor(mapView, 0.0);
        AnchorPane.setBottomAnchor(mapView, 0.0);
        AnchorPane.setLeftAnchor(mapView, 0.0);
        AnchorPane.setRightAnchor(mapView, 0.0);

        //Update map dimension if dimension of the container change
        mapContainer.widthProperty().addListener((obs, oldWidth, newWidth) ->
                mapView.setPrefWidth(newWidth.doubleValue())
        );
        mapContainer.heightProperty().addListener((obs, oldHeight, newHeight) ->
                mapView.setPrefHeight(newHeight.doubleValue())
        );
    }

    //Method to set the value of comboBox
    private void fillComboBox() {
        comboBox.getItems().addAll(PitchType.values());

        //Handle ComboBox selection
        comboBox.setOnAction(e -> {
            PitchType selectedType = comboBox.getValue();
            System.out.println("Selected: " + selectedType);
        });

        comboBox.setValue(PitchType.FOOTBALL);
    }

    //Action of the search button near the textfield. Retrieve the pitch given a city
    @FXML
    private void searchField() {
        //run it on background thread to avoid UI lag
        Platform.runLater(() -> {
            listView.getItems().clear();
            listView.getItems().addAll(db.retrievePitch(textFieldInsert.getText()));
        });
    }

    //Action to open the description panel of a field and load the road to go there using google maps
    @FXML
    private void handleListClick(MouseEvent event) {
        String selectedItem = listView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            // Get the current stage from the event's source (the list view)
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            ps.getInstance().setPitchData(parsePitchData(selectedItem));
            // Now call the switchScene method with the currentStage as an argument
            SceneManager.switchScene(currentStage, "/descriptionOfThePitch.fxml", "Pitch Description");
        }
    }

    //Helper method to parse string result and save it into a pitchObject
    private PitchData parsePitchData(String selectedItem) {
        Map<String, String> values = new HashMap<>();
        String[] data = selectedItem.split("\n");
        for (String d : data) {
            String[] parts = d.split(":", 2);
            if (parts.length == 2) {
                values.put(parts[0].trim().toLowerCase(), parts[1].trim());
            }
        }
        // Build the PitchData object using the extracted values
        return new PitchData.Builder()
                .name(values.getOrDefault("name", ""))
                .city(values.getOrDefault("city", ""))
                .address(values.getOrDefault("address", ""))
                .pitchType(PitchType.valueOf(values.getOrDefault("pitch type", "FOOTBALL").toUpperCase())) // Assuming PitchType is an Enum
                .build();
    }

    //TODO: We can change it to make a more advanced research
    @FXML
    private void refreshList(ActionEvent actionEvent) {
        System.out.println("Refreshing map...");

        //Remove existing map view
        mapContainer.getChildren().clear();

        //Reinitialize the map
        initializeMap();
    }

    //Command to turn back in the previous scene
    public void backToMain() {
        SceneManager.switchScene("/DecisionView.fxml", "Main View", back);
    }

    //Command to autocomplete the textfield of city
    private void autoCompletionCity() {
        TextFields.bindAutoCompletion(textFieldInsert, request -> {
            String input = textFieldInsert.getText().trim();
            if (input.isEmpty()) {
                return new ArrayList<>(); //No suggestions if input is empty
            }
            return dbManager.getCitySuggestions(input); //Fetch suggestions from DB
        });
    }

    //Method for the automatic zoom on the map
    private void addPitchMarkers(String city) {
        //Removing all existing mark on the map
        for (MapLayer layer : activeMarkers) {
            mapView.removeLayer(layer);
        }
        activeMarkers.clear();

        //Return address + city, Italy String
        List<String> pitchAddresses = db.retrievePitchForLocation(city);
        if(pitchAddresses.isEmpty()) {
            Platform.runLater(this::warningNoPitchInTheCity);
            return;
        }

        List<MapPoint> pitchLocations = new ArrayList<>();

        for (String address : pitchAddresses) {
            geocodingService.geocodeAddress(address).thenAccept(coordinates -> {
                double latitude = coordinates.getLatitude();
                double longitude = coordinates.getLongitude();
                MapPoint pitchLocation = new MapPoint(latitude, longitude);
                pitchLocations.add(pitchLocation);

                //Adding MapPoint and keep it in the active MapPoint list
                javafx.application.Platform.runLater(() -> {
                    MapLayer markerLayer = createMarkerLayer(pitchLocation);
                    mapView.addLayer(markerLayer);
                    activeMarkers.add(markerLayer);

                    //After adding all mark we adapt the zoom
                    if (pitchLocations.size() == pitchAddresses.size()) {
                        fitMapToBounds(pitchLocations);
                    }
                });
            }).exceptionally(ex -> {
                ex.printStackTrace();
                return null;
            });
        }
    }

    //Method to adapt map to all marker
    private void fitMapToBounds(List<MapPoint> points) {
        if (points.isEmpty()) return;

        double minLat = points.stream().mapToDouble(MapPoint::getLatitude).min().orElse(0);
        double maxLat = points.stream().mapToDouble(MapPoint::getLatitude).max().orElse(0);
        double minLon = points.stream().mapToDouble(MapPoint::getLongitude).min().orElse(0);
        double maxLon = points.stream().mapToDouble(MapPoint::getLongitude).max().orElse(0);

        //We centered the map to guarantee that the map is centered around the cluster of markers rather than an arbitrary location
        MapPoint center = new MapPoint((minLat + maxLat) / 2, (minLon + maxLon) / 2);
        mapView.setCenter(center);

        double zoom = calculateZoomLevel(minLat, maxLat, minLon, maxLon);
        mapView.setZoom(zoom);
    }

    //Method to compute the level of zoom (approximately)
    private double calculateZoomLevel(double minLat, double maxLat, double minLon, double maxLon) {
        double latDiff = maxLat - minLat;
        double lonDiff = maxLon - minLon;
        double maxDiff = Math.max(latDiff, lonDiff);

        if (maxDiff < 0.01) return 15;  //nearest zoom
        if (maxDiff < 0.05) return 14;
        if (maxDiff < 0.1) return 13;
        if (maxDiff < 0.5) return 12;
        if (maxDiff < 1.0) return 11;
        return 10;  //most far zoom
    }

    //Create the point to the map (red point to spot a location like google maps)
    private MapLayer createMarkerLayer(MapPoint point) {
        //Create an ImageView for the marker
        ImageView redPing = new ImageView(new Image(getClass().getResourceAsStream("/images/redPing.png")));
        redPing.setFitWidth(20);
        redPing.setFitHeight(20);

        //Adjust position: center bottom of the image should align with the coordinates
        redPing.setTranslateX(-redPing.getFitWidth() / 2);
        redPing.setTranslateY(-redPing.getFitHeight());

        //Wrap marker in a StackPane
        StackPane markerPane = new StackPane(redPing);

        //Create a custom MapLayer
        return new MapLayer() {
            @Override
            protected void layoutLayer() {
                //Ensure the marker is updated in the correct UI thread
                javafx.application.Platform.runLater(() -> {
                    this.getChildren().setAll(markerPane);

                    //Convert lat/lon to pixel coordinates
                    Point2D point2D = getMapPoint(point.getLatitude(), point.getLongitude());
                    markerPane.setLayoutX(point2D.getX());
                    markerPane.setLayoutY(point2D.getY());

                    //Ensure correct layout update
                    requestLayout();
                });
            }
        };
    }

    //Displaying a warning if there are no field in a certain zone
    private void warningNoPitchInTheCity() {
        if(activeMarkers.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No pitch!");
            alert.setContentText("There aren't pitch in this city: " + city);
            alert.showAndWait();
        }
    }
}
