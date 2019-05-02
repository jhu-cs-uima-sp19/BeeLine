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

    public Date(long millis) {
        java.util.Date date = new java.util.Date(millis);
        this.year = date.getYear() + 1900;
        this.day = date.getDate();
        this.month = date.getMonth() + 1;
    }

    public int month;
    public int day;
    public int year;

    private static final int hour = 24 * 60;

    public int value(Time time) {
        return (int) new java.util.Date(year - 1900, month - 1, day).getTime() + time.value();
    }

    @Override
    public String toString() {
        return month + "/" + day + "/" + year;
    }

}
