package com.kolotseyd.getweather.model.HourlyWeather;

import com.kolotseyd.getweather.model.DailyWeather.Weather;

import java.util.List;

public class HourlyWeather {
    private int dt;
    private double temp;
    private List<Weather> weather;

    public int getDt() {
        return dt;
    }

    public void setDt(int dt) {
        this.dt = dt;
    }

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public List<Weather> getWeather() {
        return weather;
    }

    public void setWeather(List<Weather> weather) {
        this.weather = weather;
    }
}
