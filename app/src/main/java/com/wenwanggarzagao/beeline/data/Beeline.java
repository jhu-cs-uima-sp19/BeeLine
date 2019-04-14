package com.wenwanggarzagao.beeline.data;

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
    private Beeline(Location from, Location to) {
        this.from = from;
        this.to = to;
        this.participants = new LinkedList<>();
        this.id = new Random().nextLong();
    }

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
        return this.participants.get(0);
    }

    /**
     * Builder for a Beeline, in case we have more complex options later.
     */
    public static class Builder {
        Builder() {

        }

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

        boolean valid() {
            return from != null && to != null;
        }

        Beeline build() {
            if (!this.valid()) {
                throw new IllegalStateException("Tried to build Beeline before all fields were filled.");
            }

            Beeline beeline = new Beeline(from, to);
            beeline.loadUsers();
            return beeline;
        }
    }

}
