package com.fl.findthepitch.view;

import com.fl.findthepitch.controller.dbManager;
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
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.controlsfx.control.textfield.TextFields;

import java.util.ArrayList;
import java.util.List;

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
    private AnchorPane root;

    @FXML
    private AnchorPane searchbar;

    @FXML
    private Button searchButton;

    @FXML
    private Button back;

    GeocodingService geocodingService = new GeocodingService();

    private MapView mapView;

    dbManager db = new dbManager();

    private static String city;

    private List<MapLayer> activeMarkers = new ArrayList<>();

    public void initialize() {

        //Initialize the ListView with some items
        listView.getItems().addAll(getPitchInUserPosition());

        //Fill the comboBox with type of field
        fillExpansionPanelContainer();

        //Initialize the map
        setMap();

        //Let auto-binding on city available
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

    private void setMap() {
        mapView = new MapView();

        // Imposta le coordinate iniziali su Milano
        mapView.setCenter(new MapPoint(45.4642, 9.1900));
        mapView.setZoom(10);

        if (city == null || city.isEmpty()) {
            System.out.println("City is null or empty, cannot retrieve pitches");
        } else {
            addPitchMarkers(city);
        }

        // Setta la dimensione della mappa per adattarsi al container
        mapView.setPrefSize(mapContainer.getPrefWidth(), mapContainer.getPrefHeight());
        mapContainer.getChildren().add(mapView);

        // Anchor the mapView to tutti i lati del container
        AnchorPane.setTopAnchor(mapView, 0.0);
        AnchorPane.setBottomAnchor(mapView, 0.0);
        AnchorPane.setLeftAnchor(mapView, 0.0);
        AnchorPane.setRightAnchor(mapView, 0.0);

        // Aggiorna la dimensione della mappa se il container cambia dimensione
        mapContainer.widthProperty().addListener((obs, oldWidth, newWidth) ->
                mapView.setPrefWidth(newWidth.doubleValue())
        );
        mapContainer.heightProperty().addListener((obs, oldHeight, newHeight) ->
                mapView.setPrefHeight(newHeight.doubleValue())
        );
    }

    private void fillExpansionPanelContainer() {
        comboBox.getItems().addAll(PitchType.values());

        //Handle ComboBox selection
        comboBox.setOnAction(e -> {
            PitchType selectedType = comboBox.getValue();
            System.out.println("Selected: " + selectedType);
        });

        comboBox.setValue(PitchType.FOOTBALL);
    }

    @FXML
    private void searchField() {
        //run it on background thread to avoid UI lag
        Platform.runLater(() -> {
            listView.getItems().clear();
            listView.getItems().addAll(db.retrievePitch(textFieldInsert.getText()));
        });
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

    private void autoCompletionCity() {
        TextFields.bindAutoCompletion(textFieldInsert, request -> {
            String input = textFieldInsert.getText().trim();
            if (input.isEmpty()) {
                return new ArrayList<>(); //No suggestions if input is empty
            }
            return db.getCitySuggestions(input); //Fetch suggestions from DB
        });
    }

    /**
     * Retrieve pitches for the city and add a red marker for each.
     *
     * @param city the city to load pitches for.
     */
    // Metodo modificato per gestire lo zoom automatico
    private void addPitchMarkers(String city) {
        // Rimuovi i marker esistenti
        for (MapLayer layer : activeMarkers) {
            mapView.removeLayer(layer);
        }
        activeMarkers.clear();

        List<String> pitchAddresses = db.retrievePitchForLocation(city);

        if (pitchAddresses.isEmpty()) {
            System.out.println("No pitches found for city: " + city);
            return;
        }

        List<MapPoint> pitchLocations = new ArrayList<>();

        for (String address : pitchAddresses) {
            geocodingService.geocodeAddress(address).thenAccept(coordinates -> {
                double latitude = coordinates.getLatitude();
                double longitude = coordinates.getLongitude();
                MapPoint pitchLocation = new MapPoint(latitude, longitude);
                pitchLocations.add(pitchLocation);

                // Aggiungi il marker e tienilo nella lista attiva
                javafx.application.Platform.runLater(() -> {
                    MapLayer markerLayer = createMarkerLayer(pitchLocation);
                    mapView.addLayer(markerLayer);
                    activeMarkers.add(markerLayer);

                    // Dopo aver aggiunto tutti i marker, adatta lo zoom
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

        if (maxDiff < 0.01) return 15;  //most near zoom
        if (maxDiff < 0.05) return 14;
        if (maxDiff < 0.1) return 13;
        if (maxDiff < 0.5) return 12;
        if (maxDiff < 1.0) return 11;
        return 10;  //most far zoom
    }

    private MapLayer createMarkerLayer(MapPoint point) {
        // Create an ImageView for the marker
        ImageView redPing = new ImageView(new Image(getClass().getResourceAsStream("/images/redPing.png")));
        redPing.setFitWidth(20);
        redPing.setFitHeight(20);

        // Adjust position: center bottom of the image should align with the coordinates
        redPing.setTranslateX(-redPing.getFitWidth() / 2);
        redPing.setTranslateY(-redPing.getFitHeight());

        // Wrap marker in a StackPane
        StackPane markerPane = new StackPane(redPing);

        // Create a custom MapLayer
        return new MapLayer() {
            @Override
            protected void layoutLayer() {
                // Ensure the marker is updated in the correct UI thread
                javafx.application.Platform.runLater(() -> {
                    this.getChildren().setAll(markerPane);

                    // Convert lat/lon to pixel coordinates
                    Point2D point2D = getMapPoint(point.getLatitude(), point.getLongitude());
                    markerPane.setLayoutX(point2D.getX());
                    markerPane.setLayoutY(point2D.getY());

                    // Ensure correct layout update
                    requestLayout();
                });
            }
        };
    }

}
