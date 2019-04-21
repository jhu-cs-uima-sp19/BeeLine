package com.wenwanggarzagao.beeline.data;

public class Notification {

    public Notification() {

    }

    public Notification withText(String str) {
        this.text = str;
        return this;
    }

    public Notification withBeeline(long id) {
        this.assocBeeline = id;
        return this;
    }

    public Notification withBeeline(Beeline b) {
        return this.withBeeline(b.id);
    }

    public Notification withId(int id) {
        this.id = id;
        return this;
    }

    public Notification randomId() {
        this.id = (int) (Math.random() * 2147483647);
        return this;
    }

    public Notification setTimeNow() {
        this.timestamp = System.currentTimeMillis();
        return this;
    }

    public long assocBeeline;
    public String text;
    public long timestamp;
    public int id;

    public String getTimeSince() {
        long now = System.currentTimeMillis();
        long diff = now - timestamp;

        long seconds = diff / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;

        hours = hours % 24;
        minutes = minutes % 60;
        seconds = seconds % 60;

        if (days > 0) {
            return days == 1 ? "A day ago" : days + " days ago";
        }

        if (hours > 0) {
            return hours == 1 ? "An hour ago" : hours + " hours ago";
        }

        if (minutes > 0) {
            return minutes == 1 ? "A minute ago" : minutes + " minutes ago";
        }

        return "A few seconds ago";
    }

}
