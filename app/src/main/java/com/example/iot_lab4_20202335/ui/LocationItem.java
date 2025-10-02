package com.example.iot_lab4_20202335.ui;

public class LocationItem {
    public final int id;
    public final String name;
    public final String region;
    public final String country;
    public final double lat;
    public final double lon;

    public LocationItem(int id, String name, String region, String country, double lat, double lon) {
        this.id = id;
        this.name = name;
        this.region = region;
        this.country = country;
        this.lat = lat;
        this.lon = lon;
    }

    public String displayTitle() { return name == null ? "" : name; }

    public String displaySubtitle() {
        String r = (region == null || region.isEmpty()) ? "" : (region + ", ");
        return r + (country == null ? "" : country);
    }
}