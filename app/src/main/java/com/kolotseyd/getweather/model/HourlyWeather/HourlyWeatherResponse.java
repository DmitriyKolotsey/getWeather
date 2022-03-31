package com.kolotseyd.getweather.model.HourlyWeather;

import java.util.List;

public class HourlyWeatherResponse {
    private double lat;
    private double lon;
    private String timezone;
    private List<HourlyWeather> hourly;

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

    public List<HourlyWeather> getHourly() {
        return hourly;
    }

    public void setHourly(List<HourlyWeather> hourly) {
        this.hourly = hourly;
    }
}
