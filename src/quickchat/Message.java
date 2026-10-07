/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package quickchat;

/**
 *
 * @author Kea
 */
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Message {

    // Instance Fields
    private String messageID;
    private String recipientCell;
    private String messageText;
    private int messageNumber;
    private String messageHash;
    private String messageStatus;

    // Static Session Fields
    private static int totalMessagesSent = 0;
    private static String[] sessionMessageList = new String[100];
    private static int sessionMessageCount = 0;

    /**
     * Default Constructor
     */
    public Message() {
        this.messageID = "";
        this.recipientCell = "";
        this.messageText = "";
        this.messageNumber = 0;
        this.messageHash = "";
        this.messageStatus = "";
    }

    /**
     * Two-parameter Constructor
     * 
     * @param recipientCell International formatted cell number
     * @param messageText Text content of message
     */
    public Message(String recipientCell, String messageText) {
        this.recipientCell = recipientCell;
        this.messageText = messageText;
        this.messageID = "";
        this.messageNumber = 0;
        this.messageHash = "";
        this.messageStatus = "";
    }

    // =========================================================================
    // VALIDATION METHODS
    // =========================================================================

    /**
     * Validates if message ID is non-null and <= 10 characters.
     */
    public boolean checkMessageID(String id) {
        return id != null && id.length() <= 10;
    }

    public boolean checkMessageID() {
        return checkMessageID(this.messageID);
    }

    /**
     * Validates if cell number starts with '+' and is <= 10 characters long.
     */
    public boolean checkRecipientCell(String cellNumber) {
        return cellNumber != null && cellNumber.startsWith("+") && cellNumber.length() <= 12;
    }

    public boolean checkRecipientCell() {
        return checkRecipientCell(this.recipientCell);
    }

    /**
     * Validates text length against 250 characters limit and returns descriptive status.
     */
    public String checkMessageLengthStatus(String text) {
        if (text == null) {
            return "Message text cannot be null.";
        } else if (text.length() <= 250) {
            return "Message ready to send.";
        } else {
            int excess = text.length() - 250;
            return "Message exceeds 250 characters by " + excess + ", please reduce size.";
        }
    }

    // =========================================================================
    // HASHING & HELPER METHODS
    // =========================================================================

    public String getFirstWord(String text) {
        if (text != null && !text.trim().isEmpty()) {
            String cleanText = text.trim();
            int spaceIndex = cleanText.indexOf(" ");
            return spaceIndex == -1 ? cleanText : cleanText.substring(0, spaceIndex);
        }
        return "";
    }

    public String getLastWord(String text) {
        if (text != null && !text.trim().isEmpty()) {
            String cleanText = text.trim();
            int spaceIndex = cleanText.lastIndexOf(" ");
            return spaceIndex == -1 ? cleanText : cleanText.substring(spaceIndex + 1);
        }
        return "";
    }

    /**
     * Generates message hash formatted as FIRST_2_DIGITS:MSG_NUM:FIRST_WORD+LAST_WORD in UPPERCASE.
     * Example: 0012345678, count 0, "Hi Mike..." -> "00:0:HITONIGHT"
     */
    public String createMessageHash(String id, int msgNum, String text) {
        if (id != null && id.length() >= 2 && text != null && !text.trim().isEmpty()) {
            String firstTwoDigits = id.substring(0, 2);
            String firstWord = getFirstWord(text).replaceAll("[^a-zA-Z0-9]", "");
            String lastWord = getLastWord(text).replaceAll("[^a-zA-Z0-9]", "");

            String combined = firstTwoDigits + ":" + msgNum + ":" + firstWord + lastWord;
            this.messageHash = combined.toUpperCase();
            return this.messageHash;
        }
        return "";
    }

    public String createMessageHash() {
        return createMessageHash(this.messageID, this.messageNumber, this.messageText);
    }

    // =========================================================================
    // ACTION HANDLER
    // =========================================================================

    /**
     * Executes message action based on choice:
     * 1 = Send, 2 = Discard, 3 = Store
     */
    public String sentMessage(int userChoice) {
        String statusMessage;

        if (userChoice == 1) {
            this.messageStatus = "Sent";
            totalMessagesSent++;
            statusMessage = "Message successfully sent.";
        } else if (userChoice == 2) {
            this.messageStatus = "Disregarded";
            statusMessage = "Press 0 to delete the message.";
        } else if (userChoice == 3) {
            this.messageStatus = "Stored";
            storeMessage();
            statusMessage = "Message successfully stored.";
        } else {
            statusMessage = "Invalid action selected.";
        }

        return statusMessage;
    }

    // =========================================================================
    // PERSISTENCE (STORE MESSAGE JSON)
    // =========================================================================

    /*
     * Code Attribution:
     * File I/O and JSON Array Parsing logic researched and adapted from Oracle Java Documentation 
     * (java.io.File, java.io.FileWriter, java.util.Scanner) and Standard RFC 8259 JSON specifications.
     */
    public boolean storeMessage(String filePath) {
        boolean isSaved = false;
        File file = new File(filePath);

        String newJsonObject = "{\n" +
                "  \"messageID\": \"" + this.messageID + "\",\n" +
                "  \"recipientCell\": \"" + this.recipientCell + "\",\n" +
                "  \"messageNumber\": " + this.messageNumber + ",\n" +
                "  \"messageHash\": \"" + this.messageHash + "\",\n" +
                "  \"messageText\": \"" + escapeJson(this.messageText) + "\",\n" +
                "  \"messageStatus\": \"" + this.messageStatus + "\"\n" +
                "}";

        StringBuilder existingContent = new StringBuilder();

        if (file.exists()) {
            try (Scanner scanner = new Scanner(file)) {
                while (scanner.hasNextLine()) {
                    existingContent.append(scanner.nextLine());
                }
            } catch (IOException e) {
                System.err.println("Read Error: Unable to read " + filePath + " - " + e.getMessage());
            }
        }

        String finalJson;
        String contentStr = existingContent.toString().trim();

        if (contentStr.startsWith("[") && contentStr.endsWith("]")) {
            String innerContent = contentStr.substring(1, contentStr.length() - 1).trim();
            if (innerContent.isEmpty()) {
                finalJson = "[\n" + newJsonObject + "\n]";
            } else {
                finalJson = "[\n" + innerContent + ",\n" + newJsonObject + "\n]";
            }
        } else {
            finalJson = "[\n" + newJsonObject + "\n]";
        }

        try (FileWriter writer = new FileWriter(file, false)) {
            writer.write(finalJson);
            writer.flush();
            isSaved = true;
        } catch (IOException e) {
            System.err.println("Write Error: Could not write to " + filePath + " - " + e.getMessage());
            isSaved = false;
        }

        return isSaved;
    }

    public boolean storeMessage() {
        return storeMessage("messages.json");
    }

    private String escapeJson(String input) {
        if (input == null) return "";
        return input.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    // =========================================================================
    // STATIC SESSION LOGGING METHODS
    // =========================================================================

    public static void addSessionLog(String summary) {
        if (sessionMessageCount < sessionMessageList.length) {
            sessionMessageList[sessionMessageCount] = summary;
            sessionMessageCount++;
        }
    }

    public static String printMessages() {
        if (sessionMessageCount == 0) {
            return "No messages sent during this session.";
        }
        StringBuilder fullReport = new StringBuilder();
        for (int i = 0; i < sessionMessageCount; i++) {
            fullReport.append(sessionMessageList[i]).append("\n---------------------\n");
        }
        return fullReport.toString().trim();
    }

    public static int returnTotalMessages() {
        return totalMessagesSent;
    }

    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================

    public String getMessageID() { return messageID; }
    public void setMessageID(String messageID) { this.messageID = messageID; }

    public String getRecipientCell() { return recipientCell; }
    public void setRecipientCell(String recipientCell) { this.recipientCell = recipientCell; }

    public String getMessageText() { return messageText; }
    public void setMessageText(String messageText) { this.messageText = messageText; }

    public int getMessageNumber() { return messageNumber; }
    public void setMessageNumber(int messageNumber) { this.messageNumber = messageNumber; }

    public String getMessageHash() { return messageHash; }
    public void setMessageHash(String messageHash) { this.messageHash = messageHash; }

    public String getMessageStatus() { return messageStatus; }
    public void setMessageStatus(String messageStatus) { this.messageStatus = messageStatus; }
}