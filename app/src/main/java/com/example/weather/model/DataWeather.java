package com.example.weather.model;


import java.io.Serializable;

public class DataWeather implements Serializable {
    private String mVisibility;
    private int mTemperature;
    private int mHumidity;
    private int mTemp_min;
    private int mTemp_max;
    private int mPressure;
    private int mClouds;
    private long mSunrise;
    private long mSunset;
    private String mDescription;
    private int mIcon;
    private double mSpeed;

    public void setSpeed(double speed) {
        this.mSpeed = speed;
    }

    public void setVisibility(String mVisibility) {
        this.mVisibility = mVisibility;
    }

    public void setTemperature(int temperature) {
        this.mTemperature = temperature;
    }

    public void setHumidity(int humidity) {
        this.mHumidity = humidity;
    }

    public void setTemp_min(int temp_min) {
        this.mTemp_min = temp_min;
    }

    public void setTemp_max(int temp_max) {
        this.mTemp_max = temp_max;
    }

    public void setPressure(int pressure) {
        this.mPressure = pressure;
    }

    public void setClouds(int clouds) {
        this.mClouds = clouds;
    }

    public void setSunrise(long sunrise) {
        this.mSunrise = sunrise;
    }

    public void setSunset(long sunset) {
        this.mSunset = sunset;
    }

    public void setDescription(String description) {
        this.mDescription = description;
    }

    public void setIcon(int icon) {
        this.mIcon = icon;
    }

    public String getVisibility() {
        return mVisibility;
    }

    public int getTemperature() {
        return mTemperature;
    }

    public int getHumidity() {
        return mHumidity;
    }

    public int getTemp_min() {
        return mTemp_min;
    }

    public int getTemp_max() {
        return mTemp_max;
    }

    public int getPressure() {
        return mPressure;
    }

    public int getClouds() {
        return mClouds;
    }

    public long getSunrise() {
        return mSunrise;
    }

    public long getSunset() {
        return mSunset;
    }

    public String getDescription() {
        return mDescription;
    }

    public int getIcon() {
        return mIcon;
    }

    public double getSpeed() {
        return mSpeed;
    }
}
