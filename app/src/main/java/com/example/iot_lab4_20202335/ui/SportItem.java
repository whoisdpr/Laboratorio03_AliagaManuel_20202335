package com.example.iot_lab4_20202335.ui;

public class SportItem {
    public final String match;
    public final String tournament;
    public final String countryRegion;
    public final String stadium;
    public final String start;

    public SportItem(String match, String tournament, String countryRegion, String stadium, String start) {
        this.match = match;
        this.tournament = tournament;
        this.countryRegion = countryRegion;
        this.stadium = stadium;
        this.start = start;
    }
}