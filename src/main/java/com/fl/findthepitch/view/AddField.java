package com.fl.findthepitch.view;

import com.fl.findthepitch.controller.SceneManager;
import com.fl.findthepitch.controller.ServerConnection;
import com.fl.findthepitch.controller.dbManager;
import com.fl.findthepitch.model.PitchData;
import com.fl.findthepitch.model.fieldTypeInformation.AreaType;
import com.fl.findthepitch.model.fieldTypeInformation.PitchType;
import com.fl.findthepitch.model.fieldTypeInformation.Price;
import com.fl.findthepitch.model.fieldTypeInformation.SurfaceType;
import com.fl.findthepitch.service.AddressValidator;
import com.gluonhq.maps.MapView;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import org.controlsfx.control.textfield.TextFields;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class AddField {

    @FXML
    AnchorPane mapContainer;

    @FXML
    ScrollPane scrollPane;

    @FXML
    TextField nameField;

    private String name;

    @FXML
    TextField cityField;

    private String city;

    @FXML
    TextField addressField;

    private String address;

    @FXML
    TextField phoneField;

    private String phone;

    @FXML
    TextField websiteField;

    private String website;

    @FXML
    TextField emailField;

    private String email;

    @FXML
    CheckBox showerCheckBox;

    private String shower;

    @FXML
    CheckBox parkingCheckBox;

    private String parking;

    @FXML
    CheckBox lightingCheckBox;

    private String light;

    @FXML
    TextField openTimeField;

    private LocalTime openTime;

    @FXML
    TextField lunchStartField;

    private LocalTime lunchStart;

    @FXML
    TextField lunchEndField;

    private LocalTime lunchEnd;

    @FXML
    TextField closeTimeField;

    private LocalTime closeTime;

    @FXML
    ComboBox<Price> priceComboBox;

    private Price price;

    @FXML
    ComboBox<AreaType> areaTypeComboBox;

    private AreaType area;

    @FXML
    ComboBox<PitchType> pitchTypeComboBox;

    private PitchType pitch;

    @FXML
    ComboBox<SurfaceType> surfaceTypeComboBox;

    private SurfaceType surface;

    @FXML
    TextArea descriptionField;

    private String description;

    private String image;

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

    dbManager db = new dbManager();

    //Method that runs when we load the window and instantiate objects by callind methods
    public void initialize() {
        initializeMap();
        initializeComboBox();
        autoCompletionCity();
    }

    //Initialize map
    private void initializeMap() {
        mapView = new MapView();
        mapView.setCenter(45.53333, 9.2);
        mapView.setZoom(15);

        //Set the map's size to match its container
        mapView.setPrefSize(mapContainer.getPrefWidth(), mapContainer.getPrefHeight());
        mapContainer.getChildren().add(mapView);

        //Anchor the mapView to all sides of the container
        AnchorPane.setTopAnchor(mapView, 0.0);
        AnchorPane.setBottomAnchor(mapView, 0.0);
        AnchorPane.setLeftAnchor(mapView, 0.0);
        AnchorPane.setRightAnchor(mapView, 0.0);

        //Adjust MapView size if the container size changes
        mapContainer.widthProperty().addListener((obs, oldWidth, newWidth) ->
                mapView.setPrefWidth(newWidth.doubleValue())
        );
        mapContainer.heightProperty().addListener((obs, oldHeight, newHeight) ->
                mapView.setPrefHeight(newHeight.doubleValue())
        );
    }

    //Initialize combobox
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

    //Cleaning all field
    private void clearFields() {
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

    //Action Go back button
    @FXML
    private void goBackDecision() {
        SceneManager.switchScene("/DecisionView.fxml", "Search Or Add Field", goBackButton);
    }

    //Action Go to search page button
    @FXML
    private void goToSearch() {
        SceneManager.switchScene("/newMap.fxml", "Search Field", goToSearchButton);
    }

    //Action clear button
    @FXML
    private void clear() {
        clearFields();
    }

    //Method to send data to the server slave
    @FXML
    private void sendPitchData() {
        getData();
        List<String> errors = checkConstraint();
        if (!(errors.isEmpty())) {
            showAlert(errors);
        } else {
            //Create a Task to handle the network operation off the UI thread
            Task<String> createPitchTask = new Task<>() {
                @Override
                protected String call() throws Exception {
                    //Create a PitchData instance with the entered information
                    PitchData pitchData = new PitchData.Builder()
                            .name(name)
                            .city(city)
                            .address(address)
                            .areaType(area)
                            .price(price)
                            .canShower(showerCheckBox.isSelected())
                            .hasParking(parkingCheckBox.isSelected())
                            .hasLighting(lightingCheckBox.isSelected())
                            .openingTime(openTime)
                            .lunchBrakeStart(lunchStart)
                            .lunchBrakeEnd(lunchEnd)
                            .closingTime(closeTime)
                            .phoneNumber(phone)
                            .website(website)
                            .email(email)
                            .description(description)
                            .image(image)
                            .pitchType(pitch)
                            .surfaceType(surface)
                            .build();
                    //This call blocks, so it's important to run it off the UI thread
                    return (ServerConnection.sendCommand("CREATEPITCH", pitchData));
                }
            };

            //When the task completes successfully, process the server response on the UI thread
            createPitchTask.setOnSucceeded(event -> {
                String response = createPitchTask.getValue();
                if ("SUCCESS".equals(response)) {
                    System.out.println("Pitch created successfully.");
                    clearFields();
                    //new alert saying creation of the field done correctly
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Pitch creation done correctly");
                    alert.setHeaderText("The pitch was created correctly.");
                    alert.showAndWait();
                    //TODO: METHODS TO SHOW THE FIELD IN THE MAP
                    showFieldInMap();
                } else {
                    System.out.println("Pitch creation failed");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Pitch creation Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Pitch creation failed. Please try again.");
                    alert.showAndWait();
                }
            });

            //Handle any exceptions that occur during the network call
            createPitchTask.setOnFailed(event -> {
                Throwable ex = createPitchTask.getException();
                ex.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Pitch creation Error");
                alert.setHeaderText("An error occurred during pitch creation.");
                alert.setContentText(ex.getMessage());
                alert.showAndWait();
            });

            //Start the Task in a new thread
            Thread registrationThread = new Thread(createPitchTask);
            registrationThread.setDaemon(true);
            registrationThread.start();
        }

    }

    private void showFieldInMap() {
        System.out.println("new field created");
    }

    //Method to get data from the form
    private void getData() {
        this.name = this.nameField.getText();
        this.city = this.cityField.getText();
        this.address = this.addressField.getText();
        this.phone = this.phoneField.getText();
        this.website = this.websiteField.getText();
        this.email = this.emailField.getText();
        this.openTime = parseTime(this.openTimeField.getText());
        this.lunchStart = parseTime(this.lunchStartField.getText());
        this.lunchEnd = parseTime(this.lunchEndField.getText());
        this.closeTime = parseTime(this.closeTimeField.getText());
        this.shower = this.showerCheckBox.getText();
        this.light = this.lightingCheckBox.getText();
        this.parking = this.parkingCheckBox.getText();
        this.surface = this.surfaceTypeComboBox.getValue();
        this.pitch = this.pitchTypeComboBox.getValue();
        this.price = this.priceComboBox.getValue();
        this.area = this.areaTypeComboBox.getValue();
        this.description = this.descriptionField.getText();
    }

    //Method to validate field taken by the getData method
    private List<String> checkConstraint() {
        List<String> errors = new ArrayList<>();

        //Add error messages for each validation
        addErrorIfNotEmpty(errors, checkName());
        addErrorIfNotEmpty(errors, checkAddress());
        addErrorIfNotEmpty(errors, checkPhone());
        addErrorIfNotEmpty(errors, checkWebsite());
        addErrorIfNotEmpty(errors, checkEmail());
        addErrorIfNotEmpty(errors, checkTimes());
        addErrorIfNotEmpty(errors, checkExistenceOfComboBoxValue());
        addErrorIfNotEmpty(errors, checkDescription());

        return errors;
    }

    //CONSTRAINT METHOD
        private void addErrorIfNotEmpty(List<String> errors, String error) {
            if (error != null && !error.isEmpty()) {
                errors.add(error);
            }
        }

        private String checkName() {
            if (name == null || name.trim().isEmpty()) {
                return "Name cannot be null or empty";
            }
            return ""; // No error
        }

        private String checkAddress() {
            if (address == null || address.trim().isEmpty()) {
                return "Address cannot be null or empty";
            }

            // Regular expression to check if the address follows "Street Name, Number" format
            String addressPattern = "^[\\p{L}0-9 .'-]+, \\d+$";
            if (!address.matches(addressPattern)) {
                return "Invalid address format. Use: 'Street Name, Number' (e.g., 'Via Roma, 10')";
            }

            if(!AddressValidator.isAddressValid(address)) {
                return "Address doesn't exist!";
            }

            return ""; // Address is valid
        }

    private String checkPhone() {
            if (phone == null || phone.trim().isEmpty()) {
                return "Phone number cannot be null or empty";
            }
            //Check if the phone number contains only digits or starts with a '+' followed by digits
            if (!phone.matches("^\\+?\\d+$")) {  //Allow an optional '+' followed by digits
                return "Phone number must contain only digits, and can optionally start with a '+'";
            }
            return ""; // No error
        }

        private String checkWebsite() {
            if (website == null || website.trim().isEmpty()) {
                return ""; // No error if the website is null or empty (optional field)
            }
            //Check if the website is a valid URL format
            String urlRegex = "^(https?|ftp)://[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)+(/[^\\s]*)?$";
            if (!website.matches(urlRegex)) {
                return "Invalid website URL format";
            }
            return ""; //No error if the website is valid
        }

        private String checkEmail() {
            if (email == null || email.trim().isEmpty()) {
                return ""; // No error if the email is null or empty (optional field)
            }

            // Split the email into local part and domain part
            String[] parts = email.split("@");
            if (parts.length != 2) {
                return "Invalid email format: Missing '@' symbol.";
            }

            String localPart = parts[0];
            String domain = parts[1];

            //Validate the local part
            String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*$"; // Local part check
            if (!localPart.matches(emailRegex)) {
                return "Invalid local part of email: Contains invalid characters.";
            }

            //Now validate the dot placement in the local part
            if (!isValidLocalPart(localPart)) {
                return "Invalid dot placement in the email's local part.";
            }

            //Validate the domain part (after '@')
            if (!isValidDomainPart(domain)) {
                return "Invalid domain part of email: Contains invalid characters or consecutive dots.";
            }

            //Check if the domain is valid (reachable)
            if (!isDomainValid(domain)) {
                return "Invalid email domain: Domain is not reachable.";
            }

            return ""; //No error if the email is valid
        }

        private boolean isValidLocalPart(String localPart) {
            //If there's no dot, it's valid
            if (!localPart.contains(".")) {
                return true;
            }

            //Check if the first or last character is a dot
            if (localPart.startsWith(".") || localPart.endsWith(".")) {
                return false;
            }

            //Check if there are consecutive dots
            if (localPart.contains("..")) {
                return false;
            }

            return true;  //If dot is placed correctly, it's valid
        }

        private boolean isValidDomainPart(String domain) {
            //Check if the domain is empty
            if (domain == null || domain.trim().isEmpty()) {
                return false;
            }

            //Check for consecutive dots in the domain
            if (domain.contains("..")) {
                return false;
            }

            //Ensure the domain doesn't start or end with a dot
            if (domain.startsWith(".") || domain.endsWith(".")) {
                return false;
            }

            //Check if the domain contains at least one dot separating the domain name and extension
            return domain.contains(".");
        }

        private boolean isDomainValid(String domain) {
            try {
                InetAddress.getByName(domain); //Try resolving the domain
                return true; //If successful, the domain exists
            } catch (UnknownHostException e) {
                return false; //Domain does not exist
            }
        }

        private String checkTimes() {
            boolean openFilled = openTimeField.getText() != null && !openTimeField.getText().isEmpty();
            boolean closeFilled = closeTimeField.getText() != null && !closeTimeField.getText().isEmpty();
            boolean lunchStartFilled = lunchStartField.getText() != null && !lunchStartField.getText().isEmpty();
            boolean lunchEndFilled = lunchEndField.getText() != null && !lunchEndField.getText().isEmpty();

            //If opening time is filled, closing time must also be filled
            if (openFilled && !closeFilled) {
                return "Closing time must be provided if opening time is set.";
            }
            if (closeFilled && !openFilled) {
                return "Opening time must be provided if closing time is set.";
            }

            //If lunch start is filled, lunch end must also be filled
            if (lunchStartFilled && !lunchEndFilled) {
                return "Lunch end time must be provided if lunch start time is set.";
            }
            if (lunchEndFilled && !lunchStartFilled) {
                return "Lunch start time must be provided if lunch end time is set.";
            }

            //If all fields are empty, return no error (optional fields)
            if (!openFilled && !closeFilled && !lunchStartFilled && !lunchEndFilled) {
                return "";
            }

            //Validate time format before logic checks
            if (openFilled && !isValidTimeFormat(openTimeField.getText())) {
                return "Opening time must be in the correct format (HH:mm, e.g., 13:00).";
            }
            if (closeFilled && !isValidTimeFormat(closeTimeField.getText())) {
                return "Closing time must be in the correct format (HH:mm, e.g., 13:00).";
            }
            if (lunchStartFilled && !isValidTimeFormat(lunchStartField.getText())) {
                return "Lunch start time must be in the correct format (HH:mm, e.g., 13:00).";
            }
            if (lunchEndFilled && !isValidTimeFormat(lunchEndField.getText())) {
                return "Lunch end time must be in the correct format (HH:mm, e.g., 13:00).";
            }

            //Ensure logical order of times
            if (openFilled && closeFilled && openTime.isAfter(closeTime)) {
                return "Opening time must be before closing time.";
            }
            if (lunchStartFilled && lunchEndFilled && lunchStart.isAfter(lunchEnd)) {
                return "Lunch start time must be before lunch end time.";
            }
            if (openFilled && lunchStartFilled && openTime.isAfter(lunchStart)) {
                return "Opening time must be before lunch start time.";
            }
            if (lunchEndFilled && closeFilled && lunchEnd.isAfter(closeTime)) {
                return "Lunch end time must be before closing time.";
            }

            return ""; // No errors
        }

        //Helper method to check if time is in correct HH:mm format
        private boolean isValidTimeFormat(String time) {
            return time != null && time.matches("^([01]\\d|2[0-3]):[0-5]\\d$"); // Matches HH:mm format (24-hour)
        }

        private String checkExistenceOfComboBoxValue() {
                //Check if each ComboBox selection is null and return an error if necessary
                if (surface == null) {
                    return "Surface type must be selected.";
                }
                if (pitch == null) {
                    return "Pitch type must be selected.";
                }
                if (price == null) {
                    return "Price must be selected.";
                }
                if (area == null) {
                    return "Area type must be selected.";
                }

                //If all checks passed, return an empty string indicating no errors
                return "";
            }

        private String checkDescription() {
            if (description == null || description.isEmpty()) {
                return "Description cannot be empty";
            }
            return "";
        }

    //Show an alert with the error messages
    private void showAlert(List<String> errors) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Add pitch errors");
        alert.setHeaderText("There were some errors with the adding process:");

        //Join errors into a single string separated by line breaks
        StringBuilder errorMessages = new StringBuilder();
        for (String error : errors) {
            errorMessages.append(error).append("\n");
        }

        alert.setContentText(errorMessages.toString());
        alert.showAndWait();
    }

    //Helper method to pad time string and parse it
    private LocalTime parseTime(String timeStr) {
        // Pad single-digit hour with a leading zero if necessary
        if (timeStr != null && timeStr.length() == 4) {
            timeStr = "0" + timeStr; // Add leading zero if hour is single digit
        }
        try {
            return LocalTime.parse(timeStr);
        } catch (DateTimeParseException e) {
            // Handle invalid time format
            System.out.println("Invalid time format: " + timeStr);
            return null; // Return null if parsing fails
        }
    }

    private void autoCompletionCity() {
        TextFields.bindAutoCompletion(cityField, request -> {
            String input = cityField.getText().trim();
            if (input.isEmpty()) {
                return new ArrayList<>(); //No suggestions if input is empty
            }
            return db.getCitySuggestions(input); //Fetch suggestions from DB
        });
    }

}
