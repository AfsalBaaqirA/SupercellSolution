package org.supercell;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Main {
    private static TimeMap timeMap = new TimeMap();
    private static Thread producer, consumer;
    public static BlockingQueue<String> inputQueue = new ArrayBlockingQueue<>(100000);

    public static void main(String[] args) {
        final long startTime = System.nanoTime();
        String inputFile = args[0];

        producer = new Thread(new inputReader(inputFile));
        consumer = new Thread(new inputProcessor());

        producer.start();
        consumer.start();

        try {
            producer.join();
            consumer.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try{
            FileWriter fileWriter = new FileWriter("output.txt");
            String s = timeMap.print();
            fileWriter.write(s);
            fileWriter.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        final long duration = System.nanoTime() - startTime;
        System.out.println("Time taken: " + duration / 1000000000d + " seconds");
    }
    public static void update(JSONObject json) {
        JSONObject jsonObject = (JSONObject) json.get("values");
        for ( Object a : jsonObject.keySet()) {
            timeMap.set((String) json.get("user"), (String) a, (String) jsonObject.get(a), (Long) json.get("timestamp"));
        }
    }
    public static class inputReader implements Runnable {
        String inputFile;

        public inputReader(String inputFile) {
            this.inputFile = inputFile;
        }

        @Override
        public void run() {
            try{
                BufferedReader br = new BufferedReader(new FileReader(inputFile));
                String line;
                while(!((line = br.readLine()) == null)) {
                    inputQueue.put(line);
//                    System.out.println("Consumer: "+consumer.getState());
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public static class inputProcessor implements Runnable {
        @Override
        public void run() {
            JSONParser parser = new JSONParser();
            try{
                while(!inputQueue.isEmpty()) {
                    JSONObject json = (JSONObject) parser.parse(inputQueue.take());
                    update(json);
//                    System.out.println("Producer: "+producer.getState());
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
