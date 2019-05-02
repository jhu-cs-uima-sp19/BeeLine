package com.wenwanggarzagao.beeline.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.google.firebase.database.Exclude;
import com.wenwanggarzagao.beeline.data.Date;
import com.wenwanggarzagao.beeline.io.ResponseHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

/**
 * Represents a Beeline, aka a trip.
 */
public class Beeline {

    private static Map<String, SavedUserData> userCache = new HashMap<>();

    public Beeline() {
        this.participantIds = new ArrayList<>();
    }

    // call this when you want to make a new beeline
    public static Builder builder() {
        return new Builder();
    }

    // private on purpose... would get complicated to handle w/o builder if
    // we end up making tons of different options
    private Beeline(User user, Location from, Location to, Date meet_date, Time meet_time, String details) {
        this.from = from;
        this.to = to;
        this.meet_date = meet_date;
        this.meet_time = meet_time;
        this.participants = new ArrayList<>();
        this.participantIds = new ArrayList<>();
        this.id = new Random().nextLong();
        this.join(user);
        this.details = details;
    }

    public Date meet_date;
    public Time meet_time;
    public Location from;
    public Location to;
    public long id;
    public List<String> participantIds;
    public String details;

    // list of participants. index 0, is the group leader.
    @Exclude
    public List<SavedUserData> participants;

    public boolean isLeader(User user) {
        return participants.size() > 0 && participants.get(0).equals(user);
    }

    public int timeValue() {
        return meet_date.value(meet_time);
    }

    public void load() {
        if (participants == null) {
            participants = new ArrayList<>();
            for (String id : participantIds) {
                SavedUserData sud = userCache.get(id);
                if (sud != null) {
                    participants.add(sud);
                    continue;
                }

                DatabaseUtils.queryUserData(id, new ResponseHandler<SavedUserData>() {
                    @Override
                    public void handle(SavedUserData savedUserData) {
                        participants.add(savedUserData);
                    }
                });
            }
        }
    }

    /**
     * Add a user to a Beeline. Also updates the user to include this Beeline.
     * @param user The user.
     */
    public void join(User user) {
        if (participantIds == null) {
            participantIds = new ArrayList<>();
        }
        if (participants == null) {
            participants = new ArrayList<>();
        }
        if (user == null) {
            return;
        }
            participantIds.add(user.getId());
            participants.add(user.saveData);
            user.saveData.addBeeline(this);
    }


    /**
     * Leaves Beeline. Removes from Beeline data structures AND user data structures.
     * @param user The user.
     */
    public void leave(User user) {
        if (user == null)
            return;

        participantIds.remove(user.getId());
        participants.remove(user.saveData);
        user.saveData.removeBeeline(this);

        if (participants.isEmpty()) {
            System.out.println("removing");
            DatabaseUtils.removeBeeline(this);
        }
        // check if users is now empty
    }

    public @Nullable
    SavedUserData getLeader() {
        return this.participants.size() > 0 ? this.participants.get(0) : null;
    }

    @Override
    public String toString() {
        return this.id + " " + this.to.zip;
    }

    @Override
    public int hashCode() {
        return (int) (this.id % 2147483647);
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Beeline) {
            Beeline bl = (Beeline) other;
            return bl.id == this.id;
        }
        return false;
    }

    /**
     * Builder for a Beeline, in case we have more complex options later.
     */
    public static class Builder {
        public Builder() {

        }
        Date meet_date;
        Time meet_time;
        Location from, to;
        long id;
        User user;
        String details;

        public Builder setOwner(User user) {
            this.user = user;
            return this;
        }

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

        public Builder setDetails(String details) {
            this.details = details;
            return this;
        }

        public Builder setTime(com.wenwanggarzagao.beeline.data.Time meet_time) {
            this.meet_time = meet_time;
            return this;

        }
        boolean valid() {
            return from != null && to != null;
        }

        public Beeline build() {
            if (!this.valid()) {
                throw new IllegalStateException("Tried to build Beeline before all fields were filled.");
            }

            Beeline beeline = new Beeline(user, from, to, meet_date, meet_time, details);
            return beeline;
        }
    }

}
