package com.jipf.redispubsub.bean;

import java.io.Serializable;

public class CityInfo implements Serializable {

    public CityInfo(String city, Double longitude, Double latitude) {

        this.city = city;
        this.longitude = longitude;
        this.latitude = latitude;

    }

    /**
     * 城市
     */
    private String city;

    /**
     * 经度
     */
    private Double longitude;

    /**
     * 纬度
     */
    private Double latitude;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
}