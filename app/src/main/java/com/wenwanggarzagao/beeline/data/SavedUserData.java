package com.wenwanggarzagao.beeline.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SavedUserData {

    public SavedUserData() {
        myBeelines = new HashMap<>();
    }

    public String email;
    public String username;
    public String userId;
    public String bio;
    // zip -> list of beelines
    public Map<String, List<Long>> myBeelines;

    public int getBeelineCount() {
        int c = 0;
        for (List<Long> list : myBeelines.values()) {
            c += list.size();
        }
        System.out.println("saveddata count " + c);
        return c;
    }

    public void addBeeline(Beeline b) {
        if (myBeelines == null)
            myBeelines = new HashMap<>();

        List<Long> list = myBeelines.get("" + b.from.zip);
        if (list == null)
            myBeelines.put("" + b.from.zip, list = new ArrayList<>());

        if (!list.contains(b.id)) {
            list.add(b.id);
            DatabaseUtils.saveUser();
        }
    }

    public void removeBeeline(Beeline b) {
        if (myBeelines == null)
            return;

        List<Long> list = myBeelines.get("" + b.from.zip);
        if (list == null)
            return;

        for (Iterator<Long> it = list.iterator(); it.hasNext();) {
            if (it.next() == b.id) {
                it.remove();
            }
        }

        if (list.isEmpty())
            myBeelines.remove("" + b.from.zip);
        DatabaseUtils.saveUser();
    }

    @Override
    public int hashCode() {
        return this.userId.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof SavedUserData) {
            SavedUserData sud = (SavedUserData) other;
            return sud.userId.equals(this.userId);
        }

        if (other instanceof String) {
            return ((String) other).equals(this.userId);
        }

        return false;
    }

}
