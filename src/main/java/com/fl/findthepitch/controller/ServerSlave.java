package com.fl.findthepitch.controller;
import java.io.*;
import java.net.Socket;

public class ServerSlave extends Thread {

    private Socket clientSocket;

    public ServerSlave(Socket socket) {
        this.clientSocket = socket;
    }

    @Override
    public void run() {
        try (
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
        ) {
            writer.println("You are connected to the server");
            String message;
            while((message = reader.readLine()) != null) {
                System.out.println("Client says: " + message);
                writer.println("Server received: " + message);

                //Optionally break if the client sends "exit"
                if (message.equalsIgnoreCase("exit")) {
                    writer.println("Goodbye!");
                    break;
                }
            }
        } catch(IOException e) {
            System.err.println("Error due to: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
                System.out.println("Client disconnected");
            }catch (IOException e) {
                System.err.println("Error due to: " + e.getMessage());
            }
        }
    }

}
