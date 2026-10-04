/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package quickchat;

/**
 *
 * @author Kea
 */
import java.util.*;

public class Message {
    //declarations
    private String messageID;
    private String recipientCell;
    private String messageText;
    private int messageNumber;
    private String messageHash;
    private String messageStatus;
    private static int totalMessageSent = 0;
    private static List<String> sessionMessageList = new ArrayList<>();
    
    //method 1
    public boolean checkMessageID(String id){
        boolean isValid = false;
        
        if (id.length() <= 10) {
            isValid = true;
        }
        return isValid;
    }
    
    //method 2
    public boolean checkRecipientCell(String cellNumber){
        boolean isValid = false;
        
        if (cellNumber.length() <=10 && cellNumber.startsWith("+27")){
            isValid = true;
        }
        
        return isValid;
    }
    
    //method 3
    public String createMessageHash(String id, int msgNum, String text){
        String firstTwoDigits ;
        String firstWord;
        String lastWord;
        String combinedHash;
        String finalHash;
        
        firstTwoDigits = id.substring(0,2);
        
        firstWord ="";
        lastWord = "";
        
        combinedHash = firstTwoDigits + ":" + msgNum + ":" + firstWord + lastWord ;
        finalHash = combinedHash.toUpperCase();
        
        return finalHash;
        
    }
    
    //method 4
    public String sentMessage (int userChoice) {
        String statusMessage ;
        
        if (userChoice == 1) {
            messageStatus = "Sent";
            totalMessageSent = totalMessageSent + 1;
            
            statusMessage = "Message successfully sent.";
            
        }else if (userChoice == 2) {
            messageStatus = "Disregarded";
            
            statusMessage = "Message disregarded";
        }else if (userChoice == 3) {
            storeMessage();
            statusMessage = "Message successfully stored";
        }else {
            statusMessage = "Invalid action selected.";
        }
        
        return statusMessage;
    }
    
    //method 5 
    public String printMessage() {
        String allMessages;
        
        if (sessionMassageCount = 0) {
            allMessages = "No messages sent during this session ";
        }else {
            for (int i = 0; i < sessionMessageCount - 1; i++ ) {
                allMessages = allMessages + sessionMessageList[i] + "\n---------------------\n";
                
            }
        }
        return allMessages;
    }
    
    //method 6
    public int returnTotalMessage(){
        return totalMessageSent;
    }
    
    //method 7
    public boolean storeMessage(){
        boolean saved = false;
        // RESEARCH STEP REQUIRED:
        // In Java, import external library (e.g., org.json or Gson) to format and write JSON:
        // JSONObject msgJson = new JSONObject();
        // msgJson.put("ID", messageID); msgJson.put("Recipient", recipientCell);
        // Write msgJson to "messages.json" file.
        return saved;
    }
    
    
    
}
