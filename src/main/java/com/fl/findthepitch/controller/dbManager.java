package com.fl.findthepitch.controller;

import com.fl.findthepitch.model.DatabaseConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static com.fl.findthepitch.model.DatabaseConnection.*;

/**
 * This class is responsible for managing the database.So we casn add queries here.
 */
public class dbManager {

    private Connection connection;

    public dbManager() {
        try {
            // Establish the connection to the database
            DatabaseConnection.createDatabaseIfNotExists();
            this.connection = DatabaseConnection.getConnection();
            System.out.println("Database connection established successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to connect to the database.");
        }
    }
}
