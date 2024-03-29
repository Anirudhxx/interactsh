package org.example;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@SpringBootApplication
@RestController
public class Main {
    private static Map<String, List<Pair<String, String>>> interactionsMap = new HashMap<>();
    public static ArrayList<String> dynamicURL = new ArrayList<>();
    public static void main(String[] args)throws IOException {
        SpringApplication.run(Main.class, args);
        Process process = Runtime.getRuntime().exec(" interactsh-client -n 3");

        // threads for standard output and error stream processing
        Thread stdoutThread = new Thread(() -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println("it's printing stdout "+line); // Print standard output
                    extractAndPrintCallerInfo(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        Thread stderrThread = new Thread(() -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.err.println("it's printing "+line); // Print stderr directly

                    // Finding the URL from error stream with "oast"
                    extractAndPrintOastUrls(line);

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        // Starting both threads and wait for them to finish
        stdoutThread.start();
        stderrThread.start();

        try {
            stdoutThread.join();
            stderrThread.join();

            int exitCode = process.waitFor();

            if (exitCode != 0) {
                System.err.println("Command exited with code: " + exitCode);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    @GetMapping("/status")
    public String getStatus() {
        return "Server is running!";
    }
    @GetMapping("/getURL")
    public ResponseEntity<String> getURL() {
        JSONArray jsonArray = new JSONArray();
        for (String url : dynamicURL) {
            JSONObject json = new JSONObject();
            json.put("url", url);
            jsonArray.put(json);
        }

        String jsonString = jsonArray.toString();
        return ResponseEntity.ok(jsonString);
    }

    @GetMapping("/getInteractions")
    public ResponseEntity<String> getInteractions(
            @RequestParam String serverUrl,
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime
    ) {
        List<String> callerIpTimestampPairs = new ArrayList<>();

        // Traversing the interactionsMap
        for (Map.Entry<String, List<Pair<String, String>>> entry : interactionsMap.entrySet()) {
            String requestId = entry.getKey();
            List<Pair<String, String>> pairs = entry.getValue();

            // Check if the requestId matches the provided serverUrl
            if (requestId.equalsIgnoreCase(serverUrl)) {
                // Check timestamp limits if provided
                if (isValidTimestamp(pairs, startTime, endTime)) {
                    // Add caller IP and timestamp pairs to the list
                    for (Pair<String, String> pair : pairs) {
                        callerIpTimestampPairs.add("Caller IP: " + pair.first + ", Timestamp: " + pair.second);
                    }
                }
            }
        }

        JSONArray jsonArray = convertListToJSONArray(callerIpTimestampPairs);

        String jsonString = jsonArray.toString();
        return ResponseEntity.ok(jsonString);
    }

    private JSONArray convertListToJSONArray(List<String> list) {
        JSONArray jsonArray = new JSONArray();
        for (String entry : list) {
            jsonArray.put(entry);
        }
        return jsonArray;
    }

    private boolean isValidTimestamp(List<Pair<String, String>> pairs, String startTime, String endTime) {
        // If start or end time is not provided, return true
        if (startTime == null && endTime == null) {
            return true;
        }

        // If both start and end time are provided, check if timestamp falls within the range
        if (startTime != null && endTime != null) {
            for (Pair<String, String> pair : pairs) {
                if (pair.second.compareTo(startTime) >= 0 && pair.second.compareTo(endTime) <= 0) {
                    return true;
                }
            }
            return false;
        }

        // If only start time is provided, check if timestamp is after or equal to start time
        if (startTime != null) {
            for (Pair<String, String> pair : pairs) {
                if (pair.second.compareTo(startTime) >= 0) {
                    return true;
                }
            }
            return false;
        }

        // If only end time is provided, check if timestamp is before or equal to end time
        if (endTime != null) {
            for (Pair<String, String> pair : pairs) {
                if (pair.second.compareTo(endTime) <= 0) {
                    return true;
                }
            }
            return false;
        }

        return true; // Default case
    }

    public static void extractAndPrintOastUrls(String line) {
        String regex = "[^\\s]+oast[^\\s]+";

        Pattern pattern = Pattern.compile(regex);

        // Matcher to find occurrences in the line
        Matcher matcher = pattern.matcher(line);

        while (matcher.find()) {
            // Extract and print the matched URL
            String url = matcher.group();
            dynamicURL.add(url);
            System.out.println("Oast URL found: " + url);
        }
    }
    public static void extractAndPrintCallerInfo(String logLine) {
        String regex = "\\[(.*?)\\] Received (?:DNS|HTTP) interaction.*? from (\\d+\\.\\d+\\.\\d+\\.\\d+) at (\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2})";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(logLine);
        if (matcher.find()) {
            String requestId = matcher.group(1);
            String callerIp = matcher.group(2);
            String timestamp = matcher.group(3);
            Pair<String, String> pair = new Pair<>(callerIp, timestamp);
            interactionsMap.computeIfAbsent(requestId, k -> new ArrayList<>()).add(pair);
        }

    }
    static class Pair<X, Y> {
        public final X first;
        public final Y second;

        public Pair(X first, Y second) {
            this.first = first;
            this.second = second;
        }
    }
}
