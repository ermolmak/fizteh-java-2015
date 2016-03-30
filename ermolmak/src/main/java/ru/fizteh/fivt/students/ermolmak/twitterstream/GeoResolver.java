package ru.fizteh.fivt.students.ermolmak.twitterstream;

import twitter4j.GeoLocation;
import twitter4j.JSONException;
import twitter4j.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;

class GeoResolver {
    private String googleKey;

    GeoResolver() throws IOException {
        Properties properties = new Properties();
        InputStream inputStream = this.getClass().getResourceAsStream("/GoogleMapsKey.properties");
        properties.load(inputStream);
        googleKey = properties.getProperty("key");
    }

    private String googleMaps(String place) throws IOException {
        URL url = new URL("https://maps.googleapis.com/maps/api/geocode/json?address="
                + place + "&key=" + googleKey);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.connect();

//        if (connection.getResponseCode() >= 400) {
//            // TODO
//        }

        InputStream googleAnswer = connection.getInputStream();
        String strGoogleAnswer;

        try (BufferedReader in = new BufferedReader(new InputStreamReader(googleAnswer))) {
            String current;
            StringBuilder answer = new StringBuilder();
            while ((current = in.readLine()) != null) {
                answer.append(current);
            }
            strGoogleAnswer = answer.toString();
        }

        googleAnswer.close();
        connection.disconnect();
        return strGoogleAnswer;
    }

    private GeoLocation locationFromJSON(JSONObject location) throws JSONException {
        return new GeoLocation(
                Double.parseDouble(location.getString("lat")),
                Double.parseDouble(location.getString("lng")));
    }

    static double distance(GeoLocation loc1, GeoLocation loc2) {
        final double radius = 6370;
        double cosAng = Math.cos(loc1.getLatitude() * Math.PI / 180)
                * Math.cos(loc2.getLatitude() * Math.PI / 180)
                * Math.cos((loc1.getLongitude() - loc2.getLongitude()) * Math.PI / 180)

                + Math.sin(loc1.getLongitude() * Math.PI / 180)
                * Math.sin(loc2.getLongitude() * Math.PI / 180);

        return radius * Math.acos(cosAng);
    }

    Area findPlace(String place) throws IOException, JSONException {
        JSONObject json = new JSONObject(googleMaps(place));

//        if (!json.getString("status").equals("OK")) {
//            // TODO
//        }

        JSONObject geometry = json
                .getJSONArray("results")
                .getJSONObject(0)
                .getJSONObject("geometry");

        JSONObject northeast = geometry.getJSONObject("bounds").getJSONObject("northeast");
        JSONObject southwest = geometry.getJSONObject("bounds").getJSONObject("southwest");
        JSONObject location = geometry.getJSONObject("location");

        double radius = distance(locationFromJSON(northeast), locationFromJSON(southwest)) / 2;

        return new Area(locationFromJSON(location), radius);
    }
}
