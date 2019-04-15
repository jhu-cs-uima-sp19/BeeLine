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

    public int hour;
    public int min;


    @Override
    public String toString() {
        return hour + ":" + min;
    }

}
