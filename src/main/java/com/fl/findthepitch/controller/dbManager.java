package com.fl.findthepitch.controller;



import com.fl.findthepitch.model.Database;
import com.fl.findthepitch.model.PasswordUtils;
import com.fl.findthepitch.model.UserData;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.*;

/**
 * This class is responsible for managing the database.So we can add queries here.
 */
public class dbManager {

    private Connection connection;

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
                    age INTEGER NOT NULL,
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
                codice_istat INTEGER PRIMARY KEY,
                denominazione_ita VARCHAR(255) NOT NULL,
                denominazione_altra VARCHAR(255),
                cap VARCHAR(10),
                sigla_provincia VARCHAR(10),
                denominazione_provincia VARCHAR(255),
                tipologia_provincia VARCHAR(255),
                codice_regione INTEGER,
                denominazione_regione VARCHAR(255),
                tipologia_regione VARCHAR(255),
                ripartizione_geografica VARCHAR(255),
                flag_capoluogo VARCHAR(10),
                codice_belfiore VARCHAR(10),
                lat DECIMAL(10,7),
                lon DECIMAL(10,7),
                superficie_kmq DECIMAL(10,4)
            );
        """;
        executeUpdate(createTableSQL, "Municipality Table");

    }


    public void uploadDataFromCSV(String filePath) {


        String insertSQL = """
            INSERT INTO municipalities (
                codice_istat, denominazione_ita, denominazione_altra, cap, sigla_provincia,
                denominazione_provincia, tipologia_provincia, codice_regione, denominazione_regione,
                tipologia_regione, ripartizione_geografica, flag_capoluogo, codice_belfiore, lat, lon, superficie_kmq
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (PreparedStatement pstmt = connection.prepareStatement(insertSQL);
             BufferedReader br = new BufferedReader(new FileReader(filePath))) {

            String line;
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] values = line.split(";");
                pstmt.setInt(1, Integer.parseInt(values[0]));
                pstmt.setString(2, values[1]);
                pstmt.setString(3, values[2].isEmpty() ? null : values[2]);
                pstmt.setString(4, values[3]);
                pstmt.setString(5, values[4]);
                pstmt.setString(6, values[5]);
                pstmt.setString(7, values[6]);
                pstmt.setInt(8, Integer.parseInt(values[7]));
                pstmt.setString(9, values[8]);
                pstmt.setString(10, values[9]);
                pstmt.setString(11, values[10]);
                pstmt.setString(12, values[11]);
                pstmt.setString(13, values[12]);
                pstmt.setDouble(14, Double.parseDouble(values[13].replace(",", ".")));
                pstmt.setDouble(15, Double.parseDouble(values[14].replace(",", ".")));
                pstmt.setDouble(16, Double.parseDouble(values[15].replace(",", ".")));
                pstmt.executeUpdate();
            }
            System.out.println("Data uploaded successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }

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


    public boolean registerUser(UserData userData) {
        // Store hashed password
        String hashedPassword = PasswordUtils.hashPassword(userData.getHashPassword()); // Ensure hashing before storing

        String insertUserSQL = "INSERT INTO users (name, surname, username, email, password_hash, age, google_id) VALUES (?, ?, ?, ?, ?, ?, ?);";

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
        String query = "SELECT city FROM pitch WHERE city = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, text);
            ResultSet rs = stmt.executeQuery();

            return rs.next(); // Returns true if there is a matching city in the DB

        } catch (SQLException e) {
            e.printStackTrace();
            return false; // In case of an error, assume city does not exist
        }
    }
}