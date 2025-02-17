package com.fl.findthepitch.controller;

import com.fl.findthepitch.model.Database;
import com.fl.findthepitch.model.PasswordUtils;
import com.fl.findthepitch.model.PitchData;
import com.fl.findthepitch.model.UserData;
import com.fl.findthepitch.model.fieldTypeInformation.PitchType;
import com.fl.findthepitch.model.fieldTypeInformation.Price;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class dbManager {

    private static Connection connection;

    // Ensure connection is initialized at class load
    static {
        connectToDatabase();
    }

    // Establish the connection
    public static void connectToDatabase() {
        try {
            if (connection == null || connection.isClosed()) {
                Database.createDatabaseIfNotExists();
                connection = Database.getConnection();
                if (connection != null) {
                    System.out.println("Database connection established successfully.");
                } else {
                    System.err.println("Failed to get a valid database connection.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Failed to connect to the database: " + e.getMessage());
        }
    }

    // Ensures the connection is always valid before using it
    private static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                System.err.println("Connection lost. Reconnecting...");
                connectToDatabase();
            }
        } catch (SQLException e) {
            System.err.println("Error checking connection status: " + e.getMessage());
        }
        return connection;
    }

    // Execute table creation queries
    private static void executeUpdate(String query, String tableName) {
        try (Statement stmt = getConnection().createStatement()) {
            stmt.executeUpdate(query);
            System.out.println(tableName + " table created or already exists.");
        } catch (SQLException e) {
            System.err.println("Error creating " + tableName + ": " + e.getMessage());
        }
    }

    // Create Users Table
    public static void createUserTable() {
        String query = """
                CREATE TABLE IF NOT EXISTS users (
                    id SERIAL PRIMARY KEY,
                    name VARCHAR(50) NOT NULL,
                    surname VARCHAR(50) NOT NULL,
                    city VARCHAR(200) NOT NULL,
                    username VARCHAR(50) UNIQUE NOT NULL,
                    email VARCHAR(255) NOT NULL,
                    password_hash TEXT NOT NULL,
                    google_id TEXT,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                );
                """;
        executeUpdate(query, "Users Table");
    }

    // Create Pitch Table
    public static void createPitchTable() {
        String query = """
                CREATE TABLE IF NOT EXISTS pitch (
                      id SERIAL PRIMARY KEY,
                      name VARCHAR(255) NOT NULL,
                      city VARCHAR(100) NOT NULL,
                      address VARCHAR(255) NOT NULL,
                      areaType VARCHAR(100) NOT NULL,
                      price VARCHAR(100) NOT NULL,  -- Assuming Price is an ENUM (FREE/PAID), stored as BOOLEAN (true=FREE, false=PAID)
                      can_shower BOOLEAN,
                      has_parking BOOLEAN,
                      has_lighting BOOLEAN,
                      opening_time TIME,
                      lunch_break_start TIME,
                      lunch_break_end TIME,
                      closing_time TIME,
                      phone_number VARCHAR(20),
                      website VARCHAR(255),
                      email VARCHAR(255),
                      description TEXT NOT NULL,
                      imageURL TEXT,
                      pitch_type VARCHAR(100) NOT NULL,
                      surface_type VARCHAR(100) NOT NULL
                  );
                """;
        executeUpdate(query, "Pitch Table");
    }

    // Create Municipality Table
    public static void createMunicipalityTable() {
        String query = """
        CREATE TABLE IF NOT EXISTS municipalities (
            codice_istat VARCHAR(200) PRIMARY KEY,
            denominazione_ita VARCHAR(255) NOT NULL,
            denominazione_altra VARCHAR(255),
            cap VARCHAR(100),
            sigla_provincia VARCHAR(100),
            denominazione_provincia VARCHAR(255),
            tipologia_provincia VARCHAR(255),
            codice_regione VARCHAR(200),
            denominazione_regione VARCHAR(255),
            tipologia_regione VARCHAR(255),
            ripartizione_geografica VARCHAR(255),
            flag_capoluogo VARCHAR(100),
            codice_belfiore VARCHAR(100),
            lat VARCHAR(200),
            lon VARCHAR(200),
            superficie_kmq VARCHAR(200)
        );
        """;
        executeUpdate(query, "Municipality Table");
    }

    // Check if a username exists
    public static boolean checkUsername(String username) {
        String query = "SELECT username FROM users WHERE username = ?";

        try (PreparedStatement stmt = getConnection().prepareStatement(query)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            return rs.next(); // Returns true if username exists
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Register a new user
    public static synchronized boolean registerUser(UserData userData) {
        String hashedPassword = PasswordUtils.hashPassword(userData.getHashPassword());
        if (hashedPassword == null) {
            System.err.println("Password hashing failed.");
            return false;
        }

        String query = """
            INSERT INTO users (name, surname, username, email, password_hash, city, google_id)
            VALUES (?, ?, ?, ?, ?, ?, ?);
        """;

        try (PreparedStatement pstmt = getConnection().prepareStatement(query)) {
            pstmt.setString(1, userData.getName());
            pstmt.setString(2, userData.getSurname());
            pstmt.setString(3, userData.getUsername());
            pstmt.setString(4, userData.getMail());
            pstmt.setString(5, hashedPassword);
            pstmt.setString(6, userData.getCity());
            pstmt.setString(7, userData.getGoogleID());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Validate user login
    public static synchronized boolean validateLogin(String username, String enteredPassword) {
        String query = "SELECT password_hash FROM users WHERE username = ?";

        try (PreparedStatement pstmt = getConnection().prepareStatement(query)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String storedHashedPassword = rs.getString("password_hash");
                return PasswordUtils.checkPassword(enteredPassword, storedHashedPassword);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Check if a city exists
    public static boolean checkCity(String city) {
        String query = "SELECT 1 FROM municipalities WHERE denominazione_ita = ? LIMIT 1";

        try (PreparedStatement stmt = getConnection().prepareStatement(query)) {
            stmt.setString(1, city);
            return stmt.executeQuery().next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get city suggestions
    public static List<String> getCitySuggestions(String input) {
        List<String> cities = new ArrayList<>();
        String query = "SELECT denominazione_ita FROM municipalities WHERE denominazione_ita ILIKE ? LIMIT 10";

        try (PreparedStatement stmt = getConnection().prepareStatement(query)) {
            stmt.setString(1, input + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                cities.add(rs.getString("denominazione_ita"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cities;
    }

    // Get all city names
    public static List<String> getCityNames() {
        List<String> cityNames = new ArrayList<>();
        String query = "SELECT denominazione_ita FROM municipalities WHERE denominazione_ita IS NOT NULL";

        try (Statement stmt = getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                cityNames.add(rs.getString("denominazione_ita"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cityNames;
    }

    public static boolean createPitch(PitchData pitchData) {
        String query = """
        INSERT INTO pitch (name, address, city, areaType, price, can_shower, has_parking, 
        has_lighting, opening_time, lunch_break_start, lunch_break_end, closing_time, phone_number, 
        website, email, description, imageURL, pitch_type, surface_type)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);
    """;

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, pitchData.getName());
            stmt.setString(2, pitchData.getAddress());
            stmt.setString(3, pitchData.getCity());
            stmt.setString(4, pitchData.areaType().name());
            stmt.setString(5, pitchData.getPrice().name());  // Assuming Price Enum
            stmt.setBoolean(6, pitchData.isCanShower());
            stmt.setBoolean(7, pitchData.isHasParking());
            stmt.setBoolean(8, pitchData.isHasLighting());

            // Handle nullable time fields safely
            if (pitchData.getOpeningTime() != null) {
                stmt.setTime(9, Time.valueOf(pitchData.getOpeningTime()));
            } else {
                stmt.setNull(9, Types.TIME);
            }

            if (pitchData.getLunchBrakeStart() != null) {
                stmt.setTime(10, Time.valueOf(pitchData.getLunchBrakeStart()));
            } else {
                stmt.setNull(10, Types.TIME);
            }

            if (pitchData.getLunchBrakeEnd() != null) {
                stmt.setTime(11, Time.valueOf(pitchData.getLunchBrakeEnd()));
            } else {
                stmt.setNull(11, Types.TIME);
            }

            if (pitchData.getClosingTime() != null) {
                stmt.setTime(12, Time.valueOf(pitchData.getClosingTime()));
            } else {
                stmt.setNull(12, Types.TIME);
            }

            stmt.setString(13, pitchData.getPhoneNumber());
            stmt.setString(14, pitchData.getWebsite());
            stmt.setString(15, pitchData.getEmail());
            stmt.setString(16, pitchData.getDescription());
            stmt.setString(17, pitchData.getImage()); // Assuming this is a URL or Base64 string
            stmt.setString(18, pitchData.getPitchType().name()); // Enum to string
            stmt.setString(19, pitchData.getSurfaceType().name()); // Enum to string

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void uploadDataFromCSV(String filePath) {
        String insertSQL = """
            INSERT INTO municipalities (
                codice_istat, denominazione_ita, denominazione_altra, cap, sigla_provincia,
                denominazione_provincia, tipologia_provincia, codice_regione, denominazione_regione,
                tipologia_regione, ripartizione_geografica, flag_capoluogo, codice_belfiore, lat, lon, superficie_kmq
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            ON CONFLICT (codice_istat) DO NOTHING
        """;

        try (PreparedStatement pstmt = connection.prepareStatement(insertSQL);
             BufferedReader br = new BufferedReader(new FileReader(filePath))) {

            String line;
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] values = line.split(";", -1); // -1 to keep empty values

                // Safe assignment, keeping nulls for empty values
                String codiceIstat = values[0];  // codice_istat as string
                String denominazioneIta = removeQuotes(values[1]);
                String denominazioneAltra = values[2].isEmpty() ? null : removeQuotes(values[2]);
                String cap = removeQuotes(values[3]);
                String siglaProvincia = removeQuotes(values[4]);
                String denominazioneProvincia = removeQuotes(values[5]);
                String tipologiaProvincia = removeQuotes(values[6]);
                String codiceRegione = removeQuotes(values[7]);
                String denominazioneRegione = removeQuotes(values[8]);
                String tipologiaRegione = removeQuotes(values[9]);
                String ripartizioneGeografica = removeQuotes(values[10]);
                String flagCapoluogo = removeQuotes(values[11]);
                String codiceBelfiore = removeQuotes(values[12]);
                String lat = removeQuotes(values[13]);
                String lon = removeQuotes(values[14]);
                String superficieKmq = removeQuotes(values[15]);

                // Set prepared statement parameters
                pstmt.setString(1, codiceIstat);
                pstmt.setString(2, denominazioneIta);
                pstmt.setString(3, denominazioneAltra);
                pstmt.setString(4, cap);
                pstmt.setString(5, siglaProvincia);
                pstmt.setString(6, denominazioneProvincia);
                pstmt.setString(7, tipologiaProvincia);
                pstmt.setString(8, codiceRegione);
                pstmt.setString(9, denominazioneRegione);
                pstmt.setString(10, tipologiaRegione);
                pstmt.setString(11, ripartizioneGeografica);
                pstmt.setString(12, flagCapoluogo);
                pstmt.setString(13, codiceBelfiore);
                pstmt.setString(14, lat);
                pstmt.setString(15, lon);
                pstmt.setString(16, superficieKmq);

                pstmt.executeUpdate();
            }
            System.out.println("Data uploaded successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String removeQuotes(String value) {
        if (value != null && value.startsWith("\"") && value.endsWith("\"")) {
            return value.substring(1, value.length() - 1);
        }
        return value;
    }


    public List<String> retrievePitch(String city) {
        List<String> pitchList = new ArrayList<>();
        // Corrected SQL query: removed extra comma and fetched required fields
        String query = "SELECT name, city, address, pitch_type FROM PITCH WHERE city = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(query)) {
            stmt.setString(1, city);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                // Build the PitchData object (you could also build it fully if you need more info)
                PitchData pitch = new PitchData.Builder()
                        .name(rs.getString("name"))
                        .city(rs.getString("city"))
                        .address(rs.getString("address"))
                        // Assuming pitch_type stored as String that matches your PitchType enum
                        .pitchType(PitchType.valueOf(rs.getString("pitch_type")))
                        .build();

                // Create a formatted string with multiple fields on separate lines:
                String formattedPitch = "name: " + pitch.getName() + "\n" +
                        "city: " + pitch.getCity() + "\n" +
                        "address: " + pitch.getAddress() + "\n" +
                        "pitch type: " + pitch.getPitchType() + "\n";
                pitchList.add(formattedPitch);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return pitchList;
    }

    public List<String> retrievePitchForLocation(String city) {
        List<String> pitchList = new ArrayList<>();

        //Fixed SQL query: removed extra comma and corrected JOIN condition
        String query = "SELECT p.address, p.city FROM pitch p JOIN municipalities m ON p.city = m.denominazione_ita WHERE p.city = ?";

        try (PreparedStatement stmt = getConnection().prepareStatement(query)) {
            stmt.setString(1, city);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                //Retrieve fields
                String address = rs.getString("address");
                String cityName = rs.getString("city");

                //Format: "Street Name, Number, City, Italy"
                String formattedAddress = address + ", " + cityName + ", Italy";
                pitchList.add(formattedAddress);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving pitch locations", e);
        }

        return pitchList;
    }


    public UserData getLoggedUserData(String enteredUsername) {
        String query = "SELECT * FROM users WHERE username = ?";
        try(PreparedStatement stmt = getConnection().prepareStatement(query)) {
            stmt.setString(1, enteredUsername);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                return new UserData(rs.getString("name"),
                                    rs.getString("surname"),
                                    rs.getString("city"),
                                    rs.getString("username"),
                                    rs.getString("email"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
