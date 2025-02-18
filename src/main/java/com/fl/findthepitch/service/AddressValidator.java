package com.fl.findthepitch.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class AddressValidator {
    private static final String NOMINATIM_URL = "https://nominatim.openstreetmap.org/search?q=";

    public static boolean isAddressValid(String address) {
        try {
            //Format the address for the API request
            String formattedAddress = address.replace(" ", "%20");
            String requestUrl = NOMINATIM_URL + formattedAddress + "&format=json";

            //Open connection
            URL url = new URL(requestUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0"); // Avoid blocking

            //Read response
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            //Check if response contains data
            return response.toString().length() > 2; //Empty JSON array `[]` means no results

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
