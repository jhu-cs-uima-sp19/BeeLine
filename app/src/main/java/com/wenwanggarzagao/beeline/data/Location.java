package com.wenwanggarzagao.beeline.data;

/**
 * Represents a location in the world. Can either be a destination or a meetup point, etc.
 */
public class Location {

    // TODO what makes sense as args to Location?
    public Location(String address, String city, String state, short zip) {
        this.address = address;
        this.city = city;
        this.state = state;
        this.zip = zip;
    }

    private String address;
    private String city;

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public short getZip() {
        return zip;
    }

    private String state;
    private short zip;


    @Override
    public String toString() {
        return address + " | " + city + ", " + state + " | " + zip;
    }

}
