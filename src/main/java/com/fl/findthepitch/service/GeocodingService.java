package com.fl.findthepitch.service;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.gluonhq.maps.MapPoint;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.concurrent.CompletableFuture;

public class GeocodingService {

    private static final String USER_AGENT = "FindThePitch/1.0 (luca.airaghi24@gmail.com)";
    private final HttpClient client;

    public GeocodingService() {
        this.client = HttpClient.newHttpClient();
    }

    /**
     * Geocodes the provided address using Nominatim's API.
     *
     * @param address the address to geocode.
     * @return a CompletableFuture that will provide a LatLong when the request completes.
     */
    public CompletableFuture<MapPoint> geocodeAddress(String address) {
        try {
            //Encode the address for safe URL usage.
            String encodedAddress = URLEncoder.encode(address, StandardCharsets.UTF_8);
            //Nominatim API URL – adjust parameters if needed.
            String url = "https://nominatim.openstreetmap.org/search?format=json&q=" + encodedAddress;

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    //Nominatim requires a valid User-Agent header.
                    .header("User-Agent", USER_AGENT)
                    .build();

            //Send the request asynchronously.
            return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(response -> {
                        JSONArray results = new JSONArray(response.body());
                        if (results.length() > 0) {
                            JSONObject firstResult = results.getJSONObject(0);
                            double lat = firstResult.getDouble("lat");
                            double lon = firstResult.getDouble("lon");
                            return new MapPoint(lat, lon);
                        } else {
                            throw new RuntimeException("No results found for: " + address);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
            return CompletableFuture.failedFuture(e);
        }
    }
}
