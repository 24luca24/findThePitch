package com.fl.findthepitch.controller;

import com.fl.findthepitch.model.PitchData;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class ServerConnection {

    private static Socket socket;
    private static ObjectOutputStream out;
    private static ObjectInputStream in;

    //Initialize the server connection (only called once)
    public static void connectToServer(InetAddress serverAddress, int serverPort) throws IOException {
        if (socket == null || socket.isClosed()) {
            socket = new Socket(serverAddress, serverPort);
            out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(socket.getInputStream());
            System.out.println("Connected to server.");
        }
    }

    //Method to send command to server
    public static String sendCommand(String command, Object data) throws IOException, ClassNotFoundException {
        if (socket == null || socket.isClosed()) {
            throw new IOException("Socket is not connected.");
        }

        System.out.println("Sending command: " + command);
        out.writeObject(command);
        out.writeObject(data);
        out.flush();

        try {
            Object response = in.readObject();
            if (response instanceof String) {
                System.out.println("Response received: " + response);
                return (String) response;
            } else {
                System.err.println("Unexpected response type: " + response.getClass().getName());
                return "ERROR";
            }
        } catch (EOFException e) {
            System.err.println("Server closed connection unexpectedly.");
            e.printStackTrace();
            return "ERROR";
        }
    }

    //Method to send command to server
    public static Object sendCommandObj(String command, Object data) throws IOException, ClassNotFoundException {
        if (socket == null || socket.isClosed()) {
            throw new IOException("Socket is not connected.");
        }

        System.out.println("Sending command: " + command);
        out.writeObject(command);
        out.writeObject(data);
        out.flush();

        try {
            Object response = in.readObject();
            if (response instanceof String) {
                System.out.println("Response received: " + response);
                return (String) response;
            } else {
                System.err.println("Unexpected response type: " + response.getClass().getName());
                return "ERROR";
            }
        } catch (EOFException e) {
            System.err.println("Server closed connection unexpectedly.");
            e.printStackTrace();
            return "ERROR";
        }
    }

    //Method to send command to server
    public static Object sendCommand(String command) throws IOException, ClassNotFoundException {
        if (socket == null || socket.isClosed()) {
            throw new IOException("Socket is not connected.");
        }

        System.out.println("Sending command: " + command);
        out.writeObject(command);
        out.flush();

        try {
            Object response = in.readObject();
            if (response instanceof String) {
                System.out.println("Response received: " + response);
                return (String) response;
            } else {
                System.err.println("Unexpected response type: " + response.getClass().getName());
                return "ERROR";
            }
        } catch (EOFException e) {
            System.err.println("Server closed connection unexpectedly.");
            e.printStackTrace();
            return "ERROR";
        }
    }

    //Method to close the connection when done
    public static void closeConnection() {
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null && !socket.isClosed()) socket.close();
            System.out.println("Connection closed.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
