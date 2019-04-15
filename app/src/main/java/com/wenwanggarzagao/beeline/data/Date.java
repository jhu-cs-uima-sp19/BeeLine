package com.wenwanggarzagao.beeline.data;

/**
 * Represents a location in the world. Can either be a destination or a meetup point, etc.
 */
public class Date {

    public Date() {

    }

    public Date(int month, int day, int year) {
        this.month = month;
        this.day = day;
        this.year = year;
    }

    public Date(String mmddyyyy) {
        String ra[] = mmddyyyy.split("/");
        this.month = Integer.parseInt(ra[0]);
        this.day = Integer.parseInt(ra[1]);
        this.year = Integer.parseInt(ra[2]);
    }

    public int month;
    public int day;
    public int year;


    @Override
    public String toString() {
        return month + "/" + day + "/" + year;
    }

}
