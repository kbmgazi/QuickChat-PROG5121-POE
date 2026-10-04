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

public class Message {
    //declarations
    private String messageID;
    private String recipientCell;
    private String messageText;
    private int messageNumber;
    private String messageHash;
    private String messageStatus;
    private static int totalMessagesSent = 0;
    private static String[] sessionMessageList = new String[7];
    private static int sessionMessageCount = 0;
    
    //constructor
    public Message() {
        this.messageID = "";
        this.recipientCell = "";
        this.messageText = "";
        this.messageNumber = 0;
        this.messageHash = "";
        this.messageStatus = "";
    }
    
    
    //method 1
    public boolean checkMessageID(String id){
        boolean isValid = false;
        
        if (id != null && id.length() <= 10) {
            isValid = true;
        }
        return isValid;   
    }
    // Overloaded parameterless method
    public boolean checkMessageID() {
            return checkMessageID(this.messageID);
    }
    
    //method 2
    public boolean checkRecipientCell(String cellNumber){
        boolean isValid = false;
        
        if (cellNumber != null && cellNumber.length() <=10 && cellNumber.startsWith("+27")){
            isValid = true;
        }
        
        return isValid;
    }
    
    public boolean checkRecipientCell(){
        return checkRecipientCell(this.recipientCell);
    }
    
    //method 3
    public String createMessageHash(String id, int msgNum, String text){
        String finalHash = "";
        String firstTwoDigits;
        String firstWord;
        String lastWord;
        String combinedHash;
        
        if (id != null && id.length()>=2 && text != null && !text.trim().isEmpty()) {
            firstTwoDigits = id.substring(0, 2);
            firstWord = getFirstWord(text).replaceAll("[^a-zA-Z0-9]", "");
            lastWord = getLastWord(text).replaceAll("[^a-zA-Z0-9]", "");
            combinedHash = firstTwoDigits + ":" + msgNum + ":" + firstWord + lastWord;
            finalHash = combinedHash.toUpperCase();
            this.messageHash = finalHash;
        }
        
        return finalHash;
        
    }
    
    //method 4
    public String sentMessage (int userChoice) {
        String statusMessage = "";
        
         if (userChoice == 1) {
            this.messageStatus = "Sent";
            totalMessagesSent = totalMessagesSent + 1;
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
    public String createMessageHash() {
        return createMessageHash(this.messageID, this.messageNumber, this.messageText);
    }
    
    //method 5 
    public String printMessage() {
        String fullReport = "";
        
        if (sessionMessageCount == 0) {
            fullReport = "No messages sent during this session.";
        }else {
            for (int i = 0; i < sessionMessageCount; i = i + 1) {
                fullReport = fullReport + sessionMessageList[i] + "\n---------------------\n";
                
            }
        }
        return fullReport;
    }
    
    //method 6
    public static int returnTotalMessages() {
        return totalMessagesSent;
    }
    
    //method 7
    public boolean storeMessage(){
        boolean saved = false;
        String jsonFormat = "{\n" +
                "  \"messageID\": \"" + this.messageID + "\",\n" +
                "  \"recipientCell\": \"" + this.recipientCell + "\",\n" +
                "  \"messageNumber\": " + this.messageNumber + ",\n" +
                "  \"messageHash\": \"" + this.messageHash + "\",\n" +
                "  \"messageText\": \"" + this.messageText + "\",\n" +
                "  \"messageStatus\": \"" + this.messageStatus + "\"\n" +
                "}";
        
        try {
            FileWriter writer = new FileWriter("messages.json", true);
            writer.write(jsonFormat + "\n");
            writer.close();
            saved = true;
        }catch (IOException e) {
            System.out.println("Error saving message to file: " + e.getMessage());
            saved = false;
            
        }
        // RESEARCH STEP REQUIRED:
        // In Java, import external library (e.g., org.json or Gson) to format and write JSON:
        // JSONObject msgJson = new JSONObject();
        // msgJson.put("ID", messageID); msgJson.put("Recipient", recipientCell);
        // Write msgJson to "messages.json" file.
        return saved;
    }
    
    //getter and setters method
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
