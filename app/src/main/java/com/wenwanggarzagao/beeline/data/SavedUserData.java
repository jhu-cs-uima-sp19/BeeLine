package com.wenwanggarzagao.beeline.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SavedUserData {

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

}
