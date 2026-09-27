/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package quickchat;

/**
 *
 * @author Kea
 */
public class Message {
    //declarations
    private String messageID;
    private String recipientCell;
    private String messageText;
    private int messageNumber;
    private String messageHash;
    private String messageStatus;
    private static int totalMessagesSent = 0;
    private static list sessionMessageList;
    
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
        
        if (cellNumber.length() <=10 && cellNumber.startsWith(+27)){
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
    }
}
