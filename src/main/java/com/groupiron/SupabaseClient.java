package com.groupiron;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SupabaseClient {
    private final String baseUrl;
    private final String apiKey;
    //String baseUrl = "https://ebmbwhlajxbbziopwyto.supabase.co";
    //String apiKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImVibWJ3aGxhanhiYnppb3B3eXRvIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDc4NTU1NTgsImV4cCI6MjA2MzQzMTU1OH0.TVBFLk1mWCsnh9CHrI5bo98xK5zeHJP-ljmRC8smS1s";

    // Constructor takes base URL + API key
    public SupabaseClient(String baseUrl, String apiKey) {
        this.baseUrl = baseUrl;
        this.apiKey = apiKey;
    }

    // Method 1: sendTile(TileData tile)
    // Sends a POST request with tile info
    public void sendTile(TileData tile) throws IOException, InterruptedException {
        String json = String.format(
                "{\"x\": %d, \"y\": %d, \"plane\": %d, \"player\": \"%s\"}",
                tile.getX(), tile.getY(), tile.getPlane(), tile.getPlayer());

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/rest/v1/tiles"))
                .header("apikey", apiKey)
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json)).build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("Supabase response code: " + response.statusCode());
        System.out.println("Response body: " + response.body());
    }

    // Method 2: fetchAllTiles()
    // Sends a GET request, parses response, returns List<TileData>
    public List<TileData> fetchAllTiles() throws IOException, InterruptedException, JSONException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/rest/v1/tiles"))
                .header("apikey", apiKey)
                .header("Authorization", "Bearer " + apiKey)
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        List<TileData> tiles = new ArrayList<>();

        if (response.statusCode() == 200) {
            JSONArray jsonArray = new JSONArray(response.body());

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                int x = obj.getInt("x");
                int y = obj.getInt("y");
                int plane = obj.getInt("plane");
                String player = obj.getString("player");

                tiles.add(new TileData(x, y, plane, player));
            }
        } else {
            System.err.println("Failed to fetch tiles: " + response.statusCode());
            System.err.println(response.body());
        }

        return tiles;
    }
}