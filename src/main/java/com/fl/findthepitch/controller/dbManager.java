package com.fl.findthepitch.controller;



import com.fl.findthepitch.model.Database;
import com.fl.findthepitch.model.PasswordUtils;
import com.fl.findthepitch.model.UserData;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This class is responsible for managing the database.So we can add queries here.
 */
public class dbManager {

    private static Connection connection;

    public dbManager() {
        try {
            // Establish the connection to the database
            Database.createDatabaseIfNotExists();
            this.connection = Database.getConnection();
            System.out.println("Database connection established successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to connect to the database.");
        }
    }

    //Method to create the User table
    public void createUserTable() {
        String createTableSQL = """
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

        executeUpdate(createTableSQL, "User Table");
    }

    //Method to create the Pitch table
    public void createPitchTable() {
        String createTableSQL = """
                CREATE TABLE IF NOT EXISTS pitch (
                    id SERIAL PRIMARY KEY,
                    name VARCHAR(255) NOT NULL,
                    address VARCHAR(255) NOT NULL,
                    zip_code INTEGER,
                    city VARCHAR(100),
                    is_indoor BOOLEAN,
                    is_free BOOLEAN,
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
                    description TEXT,
                    imageURL TEXT,
                    pitch_type VARCHAR(100),
                    surface_type VARCHAR(100)
                );
                """;

        executeUpdate(createTableSQL, "Pitch Table");
    }

    public void createMunicipalityTable() {
        String createTableSQL = """
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
        executeUpdate(createTableSQL, "Municipality Table");
    }


    private double parseDouble(String value) {
        if (value == null || value.trim().isEmpty()) return 0.0;
        return Double.parseDouble(value.replace(",", "."));
    }


    //Helper method to execute table creation
    private void executeUpdate(String query, String tableName) {
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(query);
            System.out.println(tableName + " created or already exists.");
        } catch (SQLException e) {
            System.err.println("Error creating " + tableName + ": " + e.getMessage());
        }
    }

    public boolean checkUsername(String username) {
        String query = "SELECT username FROM users WHERE username = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            return rs.next(); // Returns true if there is a matching username in the DB

        } catch (SQLException e) {
            e.printStackTrace();
            return false; // In case of an error, assume username does not exist
        }
    }

    public static boolean registerUser(UserData userData) {
        // Store hashed password
        String hashedPassword = PasswordUtils.hashPassword(userData.getHashPassword()); // Ensure hashing before storing

        String insertUserSQL = "INSERT INTO users (name, surname, username, email, password_hash, city, google_id) VALUES (?, ?, ?, ?, ?, ?, ?);";

        try {
            connection.setAutoCommit(false);

            try (PreparedStatement pstmt = connection.prepareStatement(insertUserSQL)) {
                pstmt.setString(1, userData.getName());
                pstmt.setString(2, userData.getSurname());
                pstmt.setString(3, userData.getUsername());
                pstmt.setString(4, userData.getMail());
                pstmt.setString(5, hashedPassword);
                pstmt.setString(6, userData.getCity());
                pstmt.setString(7, userData.getGoogleID());

                int affectedRows = pstmt.executeUpdate();

                if (affectedRows == 0) {
                    throw new SQLException("User registration failed, no rows affected.");
                }
            }
            connection.commit();
            return true;
        } catch (SQLException e) {
            try {
                connection.rollback();
                System.err.println("Transaction rolled back: " + e.getMessage());
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            return false;
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    //TODO: If the user registers via Google, their password is null since they don’t need one
    public boolean validateLogin(String username, String enteredPassword) {
        String query = "SELECT password_hash FROM users WHERE username = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                // Get the stored password and check
                String storedHashedPassword = rs.getString("password_hash");
                return PasswordUtils.checkPassword(enteredPassword, storedHashedPassword);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //If not found
        return false;
    }

    public boolean checkCity(String text) {
        String query = "SELECT 1 FROM municipalities WHERE denominazione_ita = ? LIMIT 1"; // More efficient

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, text);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next(); // Returns true if city exists
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Assume city does not exist in case of an error
        }
    }

    public List<String> getCitySuggestions(String input) {
        List<String> cities = new ArrayList<>();
        String query = "SELECT denominazione_ita FROM municipalities WHERE denominazione_ita ILIKE ? LIMIT 10"; // Case-insensitive search

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, input + "%"); // Search for cities starting with input
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    cities.add(rs.getString("denominazione_ita"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cities;
    }



    private String removeQuotes(String value) {
        if (value != null && value.startsWith("\"") && value.endsWith("\"")) {
            return value.substring(1, value.length() - 1);
        }
        return value;
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

    public List<String> getCityNames() {
        String query = "SELECT denominazione_ita FROM municipalities WHERE denominazione_ita IS NOT NULL";
        List<String> cityNames = new ArrayList<>();

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            // Iterate over the result set and add city names to the list
            while (rs.next()) {
                String cityName = rs.getString("denominazione_ita");
                cityNames.add(cityName);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cityNames;
    }

}
