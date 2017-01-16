package com.sunanda.monitorial;

/**
 * Created by Ahsan on 12/31/2016.
 */

public class LocationMap {
    String id;
    Double latitude, longitude;
    String name, roll;

    public LocationMap() {
    }

    public LocationMap(Object latitude, Object longitude, String name, String roll) {
        this.id = id;
        this.latitude = (Double) latitude;
        this.longitude =(Double) longitude;
        this.roll = roll;
        this.name = name;
    }
    public String getId() {
        return id;
    }

    public Double getLng() {
        return longitude;
    }

    public Double getLat() {
        return latitude;
    }

    public String getRoll() {
        return roll;
    }

    public String getName() {
        return name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setLng(Double lng) {
        this.longitude = lng;
    }

    public void setLat(Double lat) {
        this.latitude = lat;
    }

    public void setRoll(String roll) {
        this.roll = roll;
    }

    public void setName(String name) {
        this.name = name;
    }
}
