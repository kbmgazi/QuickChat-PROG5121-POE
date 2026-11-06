/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package quickchat;

/**
 *
 * @author Kea
 */
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/*
 * Code Attribution:
 * File reading/writing is based on the Oracle Java Documentation
 * (java.io.File, java.io.FileWriter, java.util.Scanner and the String methods
 * indexOf/substring). The JSON layout follows RFC 8259.
 */
public class Jsonstorage {
    
    // Reads the whole file into one String. Gives back "" if the file isn't there yet.
    public static String readFileContent(String filePath) {
        StringBuilder content = new StringBuilder();
        File file = new File(filePath);
 
        if (file.exists()) {
            try (Scanner scanner = new Scanner(file)) {
                while (scanner.hasNextLine()) {
                    content.append(scanner.nextLine()).append("\n");
                }
            } catch (IOException e) {
                System.err.println("Read Error: Unable to read " + filePath + " - " + e.getMessage());
            }
        }
 
        return content.toString();
    }
    
    // Reads every message in the file.
    public static String[][] readMessages(String filePath) {
        String json = readFileContent(filePath);
        int total = countOccurrences(json, "\"messageID\"");
        String[][] rows = new String[total][5];
        int searchFrom = 0;
 
        for (int row = 0; row < total; row++) {
            
            int idStart = findValueStart(json, "messageID", searchFrom);
            rows[row][0] = extractValue(json, idStart);
 
            int recipientStart = findValueStart(json, "recipientCell", idStart);
            rows[row][1] = extractValue(json, recipientStart);
 
            int hashStart = findValueStart(json, "messageHash", recipientStart);
            rows[row][2] = extractValue(json, hashStart);
 
            int textStart = findValueStart(json, "messageText", hashStart);
            rows[row][3] = extractValue(json, textStart);
 
            // look for the status after the END of the text
            int textEnd = findValueEnd(json, textStart);
            int statusStart = findValueStart(json, "messageStatus", textEnd);
            rows[row][4] = extractValue(json, statusStart);
 
            if (statusStart != -1) {
                searchFrom = statusStart;
            } else {
                searchFrom = json.length();
            }
        }
 
        return rows;
    }
    
    // Overwrites the file with the given messages (they are all written as "Stored").
    public static boolean writeMessages(String filePath, String[] ids, String[] recipients,
            String[] hashes, String[] texts, int count) {
        boolean isSaved = false;
        StringBuilder json = new StringBuilder("[\n");
 
        for (int i = 0; i < count; i++) {
            json.append("{\n")
                .append("  \"messageID\": \"").append(escapeJson(ids[i])).append("\",\n")
                .append("  \"recipientCell\": \"").append(escapeJson(recipients[i])).append("\",\n")
                .append("  \"messageNumber\": ").append(numberFromHash(hashes[i])).append(",\n")
                .append("  \"messageHash\": \"").append(escapeJson(hashes[i])).append("\",\n")
                .append("  \"messageText\": \"").append(escapeJson(texts[i])).append("\",\n")
                .append("  \"messageStatus\": \"Stored\"\n")
                .append("}");
 
            // no comma after the last object
            if (i < count - 1) {
                json.append(",");
            }
            json.append("\n");
        }
        json.append("]");
 
        try (FileWriter writer = new FileWriter(new File(filePath), false)) {
            writer.write(json.toString());
            writer.flush();
            isSaved = true;
        } catch (IOException e) {
            System.err.println("Write Error: Could not write to " + filePath + " - " + e.getMessage());
            isSaved = false;
        }
 
        return isSaved;
    }
    
    // How many times does this text appear in the JSON? Used to size the array.
    private static int countOccurrences(String json, String target) {
        int count = 0;
        int index = json.indexOf(target);
 
        while (index != -1) {
            count++;
            index = json.indexOf(target, index + target.length());
        }
 
        return count;
    }
    
    // Finds the key, then the colon after it, then the opening quote of the value
    private static int findValueStart(String json, String key, int fromIndex) {
        int result = -1;
        int keyIndex = json.indexOf("\"" + key + "\"", fromIndex);
 
        if (keyIndex != -1) {
            int colonIndex = json.indexOf(":", keyIndex);
            if (colonIndex != -1) {
                int quoteIndex = json.indexOf("\"", colonIndex);
                if (quoteIndex != -1) {
                    result = quoteIndex + 1;
                }
            }
        }
 
        return result;
    }
    
    // Finds the closing quote of a value
    private static int findValueEnd(String json, int startIndex) {
        int result = -1;
        int index = startIndex;
        boolean found = false;
 
        if (startIndex != -1) {
            while (index < json.length() && !found) {
                char current = json.charAt(index);
                if (current == '\\') {
                    index = index + 2;
                } else if (current == '"') {
                    result = index;
                    found = true;
                } else {
                    index = index + 1;
                }
            }
        }
 
        return result;
    }
    
    // Pulls out the value that starts at startIndex (or "" if something is missing).
    private static String extractValue(String json, int startIndex) {
        String result = "";
        int endIndex = findValueEnd(json, startIndex);
 
        if (startIndex != -1 && endIndex != -1) {
            result = unescapeJson(json.substring(startIndex, endIndex));
        }
 
        return result;
    }
    
    // Undoes escapeJson: \\ goes back to \ and \" goes back to "
    private static String unescapeJson(String input) {
        StringBuilder result = new StringBuilder();
        int index = 0;
 
        while (index < input.length()) {
            char current = input.charAt(index);
            if (current == '\\' && index + 1 < input.length()) {
                char next = input.charAt(index + 1);
                if (next == 'n') {
                    result.append('\n');
                } else {
                    result.append(next);
                }
                index = index + 2;
            } else {
                result.append(current);
                index = index + 1;
            }
        }
 
        return result.toString();
    }
    
    // Same escaping as Message.escapeJson so the two classes agree with each other.
    private static String escapeJson(String input) {
        String result = "";
        if (input != null) {
            result = input.replace("\\", "\\\\").replace("\"", "\\\"");
        }
        return result;
    }
    
    // The hash looks like 00:0:HITONIGHT, so the message number is between the two colons.
    private static String numberFromHash(String hash) {
        String result = "0";
        int firstColon = hash.indexOf(":");
        int secondColon = hash.indexOf(":", firstColon + 1);
 
        if (firstColon != -1 && secondColon != -1) {
            result = hash.substring(firstColon + 1, secondColon);
        }
 
        return result;
    }
    
    
}