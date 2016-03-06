package io.github.archbloom.greenenergy;

/**
 * Created by archbloom on 5/3/16.
 */
public class UniversalData {
    public static final String BASE_URL = "http://192.168.1.5:8888/";
    public static final String weather = "http://api.openweathermap.org/data/2.5/weather";

    public static String getBaseUrl() {
        return BASE_URL;
    }

    public static String getWeather() {
        return weather;
    }
}
