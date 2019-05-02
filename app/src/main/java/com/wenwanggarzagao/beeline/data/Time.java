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
        if (ra[1].contains(" ")) {
            String ampm = ra[1].split(" ")[1];
            this.hour = Integer.parseInt(ra[0]);
            this.min = Integer.parseInt(ra[1]);
            if (ampm.equalsIgnoreCase("pm")) {
                if (hour != 12) {
                    hour += 12;
                }
            } else {
                if (hour == 12) {
                    hour = 0;
                }
            }
            return;
        }
        this.hour = Integer.parseInt(ra[0]);
        this.min = Integer.parseInt(ra[1]);
    }

    public Time(long millis) {
        java.util.Date date = new java.util.Date(millis);
        this.hour = date.getHours();
        this.min = date.getMinutes();
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
        return (hour * 60 + min) * 60 * 1000;
    }

    @Override
    public String toString() {
        if (hour >= 12) {
            return (hour == 12 ? "12:" : (hour - 12) + ":") + convertMinToString(min) + " PM";
        }
        return (hour == 0 ? "12" : hour) + ":" + convertMinToString(min) + " AM";
    }

}
