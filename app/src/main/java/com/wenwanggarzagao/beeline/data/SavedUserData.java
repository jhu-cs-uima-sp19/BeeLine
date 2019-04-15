package com.wenwanggarzagao.beeline.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SavedUserData {

    public String email;
    public String username;
    public String userId;
    public String bio;
    // zip -> list of beelines
    public Map<Integer, List<Long>> myBeelines;

    public void addBeeline(Beeline b) {
        if (myBeelines == null)
            myBeelines = new HashMap<>();

        List<Long> list = myBeelines.get(b.to.zip);
        if (list == null)
            myBeelines.put(b.to.zip, list = new ArrayList<>());
        list.add(b.id);
    }

    public void removeBeeline(Beeline b) {
        if (myBeelines == null)
            return;

        List<Long> list = myBeelines.get(b.to.zip);
        if (list == null)
            return;
        list.remove(b.id);
        if (list.isEmpty())
            myBeelines.remove(b.to.zip);
    }

}
