package com.kolotseyd.getweather.model.DailyWeather;

import java.util.ArrayList;
import java.util.List;

public class DailyWeatherResponse {
        private double lat;
        private double lon;
        private String timezone;
        private List<DailyWeather> daily;

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public List<DailyWeather> getDaily() {
        return daily;
    }

    public void setDaily(List<DailyWeather> daily) {
        this.daily = daily;
    }
}
