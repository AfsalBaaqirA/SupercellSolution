package org.supercell;

import org.json.simple.JSONObject;

import java.util.*;

public class TimeMap {
    Map<String, List<UserData>> map;
    boolean noChange = false;

    public TimeMap() {
            map = new HashMap<>();
    }

    public void set(String user, String key, String value, Long timestamp) {
        List<UserData> curr_data = new LinkedList<>();
        UserData newUserData;
        for (String a : map.keySet()) {
            if (a.equals(user)) {
                curr_data = map.get(a);
                if (curr_data.isEmpty()) {
                    newUserData = new UserData(key, value, timestamp);
                    curr_data.add(newUserData);
                    map.replace(a, curr_data);
                    return;
                } else {
                    for (UserData b : curr_data) {
                        if (b.key.equals(key)) {
                            if (b.timeStamp < timestamp) {
                                if (b.value.equals(value)) {
                                    noChange = true;
                                }
                                else{
                                    b.value = value;
                                    noChange = false;
                                }
                            } else {
                                noChange = true;
                                return;
                            }
                        }
                    }
                    newUserData = new UserData(key, value, timestamp);
                    curr_data.add(newUserData);
                    map.replace(a, curr_data);
                    noChange = false;
                    return;
                }
            }
        }
        newUserData = new UserData(key, value, timestamp);
        curr_data.add(newUserData);
        map.put(user, curr_data);
        noChange = false;
    }
    public JSONObject get(String user, Long timestamp) {
        JSONObject jsonObject = new JSONObject();
        if(noChange){
            return jsonObject;
        }
        for (String a : map.keySet()) {
            if(a.equals(user)) {
                List<UserData> curr_data = map.get(a);
                for (UserData b : curr_data) {
                    if(b.timeStamp == timestamp) {
                        jsonObject.put(b.key, b.value);
                    }
                }
            }
        }
        return jsonObject;
    }
    public class UserData {
        String key;
        String value;
        Long timeStamp;

        public UserData(String key, String value, Long timeStamp) {
            this.key = key;
            this.value = value;
            this.timeStamp = timeStamp;
        }
        public void setKey(String key) {
            this.key = key;
        }
        public void setValue(String value) {
            this.value = value;
        }
        public void setTimeStamp(Long timeStamp) {
            this.timeStamp = timeStamp;
        }
    }
}
