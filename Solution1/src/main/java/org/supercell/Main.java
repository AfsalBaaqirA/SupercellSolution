package org.supercell;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Scanner;

import static java.lang.System.exit;

public class Main {

    private static Graph graph = new Graph();
    private static TimeMap timeMap = new TimeMap();

    public static void main(String[] args) {
        Long startTime = System.nanoTime();
        String inputFile = args[0];
        Scanner sc = new Scanner(System.in);
        String line;
        FileReader fr;
        BufferedReader br;
        JSONObject json;
        JSONParser parser = new JSONParser();
        try {
            fr = new FileReader(inputFile);
            br = new BufferedReader(fr);

            while(!((line = br.readLine()) == null)) {
                json = (JSONObject) parser.parse(line);
                process(json);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            exit(1);
        }
        Long duration = System.nanoTime() - startTime;
        System.out.println("Time taken: " + duration / 1000000000d + " seconds");
    }
    public static void process(JSONObject json) {
        String type = (String) json.get("type");
        String user;
        JSONArray friendList;

        switch (type) {
            case "make_friends":
                graph.addNewEdge((String) json.get("user1"), (String) json.get("user2"));
                break;
            case "del_friends":
                graph.removeEdge((String) json.get("user1"),(String) json.get("user2"));
                break;
            case "update":
                user = (String) json.get("user");
                friendList = graph.getAdjacentVertices(user);
                graph.containsVertex(user);
                JSONObject valuesJSON = update(json);
                if (!friendList.isEmpty()) {
                    if(!valuesJSON.isEmpty()) {
                        broadcast(friendList, user, json.get("timestamp"), valuesJSON);
                    }
                }
                break;
        }
    }

    public static void broadcast (JSONArray broadcast, String user, Object timestamp, JSONObject values) {
        JSONObject jsonData = new JSONObject();
        jsonData.put("broadcast", broadcast);
        jsonData.put("user", user);
        jsonData.put("timestamp", timestamp);
        jsonData.put("values", values);
        System.out.println(jsonData);
    }
    public static JSONObject update(JSONObject json) {
        JSONObject jsonObject = (JSONObject) json.get("values");
        for ( Object a : jsonObject.keySet()) {
            timeMap.set((String) json.get("user"), (String) a, (String) jsonObject.get(a), (Long) json.get("timestamp"));
        }
        return timeMap.get((String) json.get("user"), (Long) json.get("timestamp"));
    }
}