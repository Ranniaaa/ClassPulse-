package analytics;

import java.util.*;
import java.io.*;

public class FileManager {

    public void saveToFile(String data, String filename) {
        try {
            FileWriter writer = new FileWriter(filename);
            writer.write(data);
            writer.close();
            System.out.println("Data saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving file.");
        }
    }

    public String loadFromFile(String filename) {
        StringBuilder data = new StringBuilder();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line;

            while ((line = reader.readLine()) != null) {
                data.append(line).append("\n");
            }

            reader.close();
        } catch (IOException e) {
            System.out.println("Error loading file.");
        }

        return data.toString();
    }

    public Map<String, Integer> loadScores(String filename) {
        Map<String, Integer> scores = new HashMap<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String name = parts[0];
                int score = Integer.parseInt(parts[1]);

                scores.put(name, score);
            }

            reader.close();
        } catch (Exception e) {
            System.out.println("Error loading scores.");
        }

        return scores;
    }
}