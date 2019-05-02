package com.wenwanggarzagao.beeline.settings;

public class NotifData {

    public NotifData(long id, long time, String header, String body) {
        this.id = id;
        this.time = time;
        this.header = header;
        this.body = body;
    }

    public long id, time;
    public String header, body;

    public String toString() {
        return id + " " + time + " " + header + " " + body;
    }

    public static NotifData fromString(String s) {
        String[] arr = s.split(" ");
        return new NotifData(
                Long.parseLong(arr[0]),
                Long.parseLong(arr[1]),
                arr[2],
                arr[3]
        );
    }
}
