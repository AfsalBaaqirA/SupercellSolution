package org.supercell;

import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
    public String print() {
        JSONObject jsonObject = new JSONObject();
        for (String a : map.keySet()) {
            List<UserData> curr_data = map.get(a);
            JSONObject userData = new JSONObject();
            for (UserData b : curr_data) {
                userData.put(b.key, b.value);
            }
            jsonObject.put(a, userData);
        }
        System.out.println(jsonObject.toString());
        return jsonObject.toString();
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
