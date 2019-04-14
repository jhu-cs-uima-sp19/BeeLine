package com.wenwanggarzagao.beeline.data;

import com.wenwanggarzagao.beeline.data.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Represents a Beeline, aka a trip.
 */
public class Beeline {

    // call this when you want to make a new beeline
    public static Builder builder() {
        return new Builder();
    }

    // private on purpose... would get complicated to handle w/o builder if
    // we end up making tons of different options
    private Beeline(Location from, Location to, Date meet_date) {
        this.from = from;
        this.to = to;
        this.meet_date = meet_date;
        this.participants = new LinkedList<>();
        this.id = new Random().nextLong();
    }

    public Date meet_date;
    public Location from;
    public Location to;
    public long id;

    // linked list of participants. HEAD, aka index 0, is the group leader.
    private List<User> participants;

    public void loadUsers() {
        // TODO load users from a db
    }

    public void join(User user) {

    }

    public void leave(User user) {

        // check if users is now empty
    }

    public User getLeader() {
        //return this.participants.get(0);
        return null;
    }

    /**
     * Builder for a Beeline, in case we have more complex options later.
     */
    public static class Builder {
        public Builder() {

        }
        Date meet_date;
        Location from, to;
        long id;

        public Builder setId(long id) {
            this.id = id;
            return this;
        }

        public Builder setFrom(Location from) {
            this.from = from;
            return this;
        }

        public Builder setTo(Location to) {
            this.to = to;
            return this;
        }

        public Builder setFromTo(Location from, Location to) {
            this.from = from;
            this.to = to;
            return this;
        }

        public Builder setDate(com.wenwanggarzagao.beeline.data.Date meet_date) {
            this.meet_date = meet_date;
            return this;

        }
        boolean valid() {
            return from != null && to != null;
        }

        public Beeline build() {
            if (!this.valid()) {
                throw new IllegalStateException("Tried to build Beeline before all fields were filled.");
            }

            Beeline beeline = new Beeline(from, to, meet_date);
            beeline.loadUsers();
            return beeline;
        }
    }

}
