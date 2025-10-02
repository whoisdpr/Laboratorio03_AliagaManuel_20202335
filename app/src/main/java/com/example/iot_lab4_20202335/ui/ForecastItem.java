package com.example.iot_lab4_20202335.ui;

public class ForecastItem {
    public final String date;
    public final double tMaxC;
    public final double tMinC;
    public final String condition;

    public ForecastItem(String date, double tMaxC, double tMinC, String condition) {
        this.date = date;
        this.tMaxC = tMaxC;
        this.tMinC = tMinC;
        this.condition = condition;
    }
}