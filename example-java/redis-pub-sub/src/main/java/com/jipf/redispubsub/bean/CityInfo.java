package com.jipf.redispubsub.bean;

import lombok.Data;

import java.io.Serializable;

@Data
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
}