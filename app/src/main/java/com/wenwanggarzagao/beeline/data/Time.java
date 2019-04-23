package com.wenwanggarzagao.beeline.data;

/**
 * Represents a location in the world. Can either be a destination or a meetup point, etc.
 */
public class Time {

    public Time() {

    }

    public Time(int hour, int min) {
        this.hour = hour;
        this.min = min;
    }

    public Time(String hhmm) {
        String ra[] = hhmm.split(":");
        this.hour = Integer.parseInt(ra[0]);
        this.min = Integer.parseInt(ra[1]);
    }

    public String convertMinToString(int min) {
        String minMod = "";
        if (this.min == 0) {
            minMod = "00";
        } else {
            minMod = Integer.toString(this.min);
        }
        return minMod;
    }

    public int hour;
    public int min;

    public int value() {
        return hour * 60 + min;
    }

    @Override
    public String toString() {
        return hour + ":" + convertMinToString(min);
    }

}
