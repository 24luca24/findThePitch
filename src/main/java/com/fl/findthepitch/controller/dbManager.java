package com.fl.findthepitch.controller;



import com.fl.findthepitch.model.Database;
import com.fl.findthepitch.model.UserData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

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
        //TODO: query oper cercare utente se esiste true altrimenti false
        String result = "";//Risultato query
        if (result == username) {
            return true;
        }
        return false;
    }

    public boolean registerUser(UserData userData) {
        String insertUserSQL = "INSERT INTO users (name, surname, username, email, password_hash, age, google_id) VALUES (?, ?, ?, ?, ?, ?, ?);";

        try {
            //Begin transaction (Disable auto-commit )
            connection.setAutoCommit(false);

            //Prepare the query (compile once and perform with different values)
            try (PreparedStatement pstmt = connection.prepareStatement(insertUserSQL)) {
                pstmt.setString(1, userData.getName());
                pstmt.setString(2, userData.getSurname());
                pstmt.setString(3, userData.getUsername());
                pstmt.setString(4, userData.getMail());
                pstmt.setString(5, userData.getHashPassword());
                pstmt.setInt(6, userData.getAge());
                pstmt.setString(7, userData.getGoogleID());

                int affectedRows = pstmt.executeUpdate();

                if (affectedRows == 0) {
                    throw new SQLException("User registration failed, no rows affected.");
                }
            }
            //If good commit the transaction
            connection.commit();
            return true;
        } catch (SQLException e) {
            try {
                //Rollback if an error occur
                connection.rollback();
                System.err.println("Transaction rolled back: " + e.getMessage());
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            return false;
        } finally {
            try {
                //Restore the autocommit
                connection.setAutoCommit(true);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}