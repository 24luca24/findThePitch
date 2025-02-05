package com.fl.findthepitch.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {


    private static final String DBNAME = "findThePitch";
    private static final String URL = "jdbc:postgresql://localhost:5432/" + DBNAME;
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "postgres";

    //Connection instance
    private static Connection connection;

    private static Statement statement;

    //Private constructor to prevent instantiation
    private void Database() {
    }

    // Method to get the database connection
    public static Connection getDBConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }

    //Method to initialize the database connection
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                //Load the PostgreSQL JDBC driver
                Class.forName("org.postgresql.Driver");

                //Establish connection
                connection = DriverManager.getConnection(
                        "jdbc:postgresql://localhost:5432/findThePitch", USERNAME, PASSWORD);
                System.out.println("Connected to the database.");
            } catch (ClassNotFoundException e) {
                System.err.println("PostgreSQL JDBC Driver not found.");
                e.printStackTrace();
                throw new SQLException("PostgreSQL JDBC Driver not found.", e);
            } catch (SQLException e) {
                System.err.println("Connection failed!");
                e.printStackTrace();
                throw e;
            }
        }
        return connection;
    }

    // Method to create the database if it doesn't exist
    public static void createDatabaseIfNotExists() {
        String defaultURL = "jdbc:postgresql://localhost:5432/postgres"; // Connect to 'postgres' initially

        // Check if the database exists
        String checkDbExists = "SELECT 1 FROM pg_database WHERE datname = '" + DBNAME + "';";
        try (Connection conn = DriverManager.getConnection(defaultURL, USERNAME, PASSWORD);
             Statement stmt = conn.createStatement();
             var rs = stmt.executeQuery(checkDbExists)) {

            if (rs.next()) {
                System.out.println("Database already exists: " + DBNAME);
            } else {
                String createDb = "CREATE DATABASE \"" + DBNAME + "\";";
                stmt.executeUpdate(createDb);
                System.out.println("Database created successfully: " + DBNAME);
                Thread.sleep(2000);
            }
        } catch (SQLException e) {
            System.err.println("Error checking or creating the database: " + e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    //Method to close the connection
    public static void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
            System.out.println("Connection closed.");
        }
    }
}
