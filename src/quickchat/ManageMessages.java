/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package quickchat;

/**
 *
 * @author Kea
 */
public class ManageMessages {
    
    private static final int MAX_MESSAGES = 100;
 
    private static final String STATUS_SENT = "Sent";
    private static final String STATUS_DISREGARDED = "Disregarded";
    private static final String STATUS_STORED = "Stored";
 
    private final String senderName;
    
    //parallel arrays
    private final String[] messageIDs = new String[MAX_MESSAGES];
    private final String[] messageHashes = new String[MAX_MESSAGES];
    private final String[] recipients = new String[MAX_MESSAGES];
    private final String[] messageTexts = new String[MAX_MESSAGES];
    private final String[] messageStatuses = new String[MAX_MESSAGES];
    private int totalCount = 0;
    
    // the three arrays the brief asks for
    private final String[] sentMessages = new String[MAX_MESSAGES];
    private final String[] disregardedMessages = new String[MAX_MESSAGES];
    private final String[] storedMessages = new String[MAX_MESSAGES];
    private int sentCount = 0;
    private int disregardedCount = 0;
    private int storedCount = 0;
    
    // constructor method
    public ManageMessages(String senderName) {
        this.senderName = senderName;
    }
    
    // Filling the arrays
    public boolean addMessage(String id, String hash, String recipient, String text, String status) {
        boolean added = false;
        String cleanStatus = cleanStatus(status);
 
        if (totalCount < MAX_MESSAGES && !cleanStatus.isEmpty()) {
            messageIDs[totalCount] = id;
            messageHashes[totalCount] = hash;
            recipients[totalCount] = recipient;
            messageTexts[totalCount] = text;
            messageStatuses[totalCount] = cleanStatus;
            totalCount++;
 
            if (cleanStatus.equals(STATUS_SENT)) {
                sentMessages[sentCount] = text;
                sentCount++;
            } else if (cleanStatus.equals(STATUS_DISREGARDED)) {
                disregardedMessages[disregardedCount] = text;
                disregardedCount++;
            } else {
                storedMessages[storedCount] = text;
                storedCount++;
            }
            added = true;
        }
 
        return added;
    }
    
    // Gives back how many messages were loaded.
    public int loadStoredMessages(String filePath) {
        String[][] rows = Jsonstorage.readMessages(filePath);
        int loaded = 0;
 
        for (int i = 0; i < rows.length; i++) {
            if (addMessage(rows[i][0], rows[i][2], rows[i][1], rows[i][3], STATUS_STORED)) {
                loaded++;
            }
        }
 
        return loaded;
    }
    
    
    // Rewrites the JSON file with whatever is currently stored in the arrays.
    // Called after a delete so the message doesn't reappear next time the app runs.
    public boolean saveStoredMessages(String filePath) {
        String[] ids = new String[totalCount];
        String[] recipientList = new String[totalCount];
        String[] hashes = new String[totalCount];
        String[] texts = new String[totalCount];
        int count = 0;
 
        for (int i = 0; i < totalCount; i++) {
            if (hasStatus(i, STATUS_STORED)) {
                ids[count] = messageIDs[i];
                recipientList[count] = recipients[i];
                hashes[count] = messageHashes[i];
                texts[count] = messageTexts[i];
                count++;
            }
        }
 
        return Jsonstorage.writeMessages(filePath, ids, recipientList, hashes, texts, count);
    }
    
    
    // Stored messages features
    
    // a) sender and recipient of every stored message
    public String displaySenderAndRecipient() {
        StringBuilder result = new StringBuilder();
        int found = 0;
 
        for (int i = 0; i < totalCount; i++) {
            if (hasStatus(i, STATUS_STORED)) {
                found++;
                result.append("Sender: ").append(senderName)
                      .append(" | Recipient: ").append(recipients[i]).append("\n");
            }
        }
 
        String output;
        if (found == 0) {
            output = "No stored messages.";
        } else {
            output = result.toString().trim();
        }
        return output;
    }
    
    // b) the longest stored message
    public String findLongestStoredMessage() {
        int longestIndex = -1;
 
        // keep hold of the position of the longest one seen so far
        for (int i = 0; i < totalCount; i++) {
            if (hasStatus(i, STATUS_STORED)) {
                if (longestIndex == -1 || messageTexts[i].length() > messageTexts[longestIndex].length()) {
                    longestIndex = i;
                }
            }
        }
 
        String output;
        if (longestIndex == -1) {
            output = "No stored messages.";
        } else {
            output = messageTexts[longestIndex];
        }
        return output;
    }
    
    // Position of a message ID in the master arrays, or -1 if it isn't there
    public int findIndexByID(String id) {
        int position = -1;
 
        for (int i = 0; i < totalCount && position == -1; i++) {
            if (messageIDs[i].equals(id)) {
                position = i;
            }
        }
 
        return position;
    }
    
    // Just the message text for an ID ("" if not found). The unit test uses this one.
    public String getMessageTextByID(String id) {
        int index = findIndexByID(id);
        String text = "";
 
        if (index != -1) {
            text = messageTexts[index];
        }
        return text;
    }
    
    // c) search by message ID and show the recipient and message
    public String searchByMessageID(String id) {
        int index = findIndexByID(id);
        String output;
 
        if (index == -1) {
            output = "No message found with ID: " + id;
        } else {
            output = "Recipient: " + recipients[index] + "\nMessage: " + messageTexts[index];
        }
        return output;
    }
    
    // Every sent or stored message for one recipient, as an array. The unit test uses this one.
    public String[] getMessagesForRecipient(String recipient) {
        // first pass counts the matches so the array is the right size
        int matches = 0;
        for (int i = 0; i < totalCount; i++) {
            if (recipients[i].equals(recipient) && !hasStatus(i, STATUS_DISREGARDED)) {
                matches++;
            }
        }
 
        // second pass fills it
        String[] found = new String[matches];
        int position = 0;
        for (int i = 0; i < totalCount; i++) {
            if (recipients[i].equals(recipient) && !hasStatus(i, STATUS_DISREGARDED)) {
                found[position] = messageTexts[i];
                position++;
            }
        }
 
        return found;
    }
 
    // d) show all the messages for one recipient
    public String searchByRecipient(String recipient) {
        String[] found = getMessagesForRecipient(recipient);
        String output;
 
        if (found.length == 0) {
            output = "No messages found for recipient: " + recipient;
        } else {
            StringBuilder result = new StringBuilder();
            for (int i = 0; i < found.length; i++) {
                result.append(i + 1).append(") ").append(found[i]).append("\n");
            }
            output = result.toString().trim();
        }
        return output;
    }
 
    // e) delete a message using its hash. It is removed from every array.
    public String deleteByHash(String hash) {
        int index = -1;
 
        for (int i = 0; i < totalCount && index == -1; i++) {
            if (messageHashes[i].equalsIgnoreCase(hash)) {
                index = i;
            }
        }
 
        String output;
        if (index == -1) {
            output = "No message found with hash: " + hash;
        } else {
            String deletedText = messageTexts[index];
            String deletedStatus = messageStatuses[index];
 
            // take it out of the matching category array first
            if (deletedStatus.equals(STATUS_SENT)) {
                sentCount = removeText(sentMessages, sentCount, deletedText);
            } else if (deletedStatus.equals(STATUS_DISREGARDED)) {
                disregardedCount = removeText(disregardedMessages, disregardedCount, deletedText);
            } else {
                storedCount = removeText(storedMessages, storedCount, deletedText);
            }
 
            // then shift everything after it one place to the left in the master arrays
            for (int i = index; i < totalCount - 1; i++) {
                messageIDs[i] = messageIDs[i + 1];
                messageHashes[i] = messageHashes[i + 1];
                recipients[i] = recipients[i + 1];
                messageTexts[i] = messageTexts[i + 1];
                messageStatuses[i] = messageStatuses[i + 1];
            }
            totalCount--;
            messageIDs[totalCount] = null;
            messageHashes[totalCount] = null;
            recipients[totalCount] = null;
            messageTexts[totalCount] = null;
            messageStatuses[totalCount] = null;
 
            String shownText = deletedText;
            if (shownText.endsWith(".")) {
                    shownText = shownText.substring(0, shownText.length() - 1);
             }
            
            output = "Message: \"" + shownText + "\" successfully deleted.";
        }
        return output;
    }
    
    // f) full report.
    public String buildReport(String status) {
        String wanted = cleanStatus(status);
        StringBuilder report = new StringBuilder();
        int found = 0;
 
        report.append("===== ").append(wanted.toUpperCase()).append(" MESSAGES REPORT =====\n");
 
        for (int i = 0; i < totalCount; i++) {
            if (hasStatus(i, wanted)) {
                found++;
                report.append("Message ID: ").append(messageIDs[i]).append("\n")
                      .append("Message Hash: ").append(messageHashes[i]).append("\n")
                      .append("Recipient: ").append(recipients[i]).append("\n")
                      .append("Message: ").append(messageTexts[i]).append("\n")
                      .append("---------------------\n");
            }
        }
 
        if (found == 0) {
            report.append("No messages found.\n");
        }
        return report.toString().trim();
    }
 
    // One-line count of each type, shown when the user quits
    public String buildSummary() {
        return "Sent: " + sentCount + " | Disregarded: " + disregardedCount + " | Stored: " + storedCount;
    }
    
   
    // Getters
    public String[] getSentMessages() { return trimmedCopy(sentMessages, sentCount); }
    public String[] getDisregardedMessages() { return trimmedCopy(disregardedMessages, disregardedCount); }
    public String[] getStoredMessages() { return trimmedCopy(storedMessages, storedCount); }
    public String[] getMessageHashes() { return trimmedCopy(messageHashes, totalCount); }
    public String[] getMessageIDs() { return trimmedCopy(messageIDs, totalCount); }
    public int getTotalCount() { return totalCount; }
    
    // Private helpers
    private boolean hasStatus(int index, String status) {
        return messageStatuses[index].equals(status);
    }
 
    private String cleanStatus(String status) {
        String result = "";
 
        if (status != null) {
            String lower = status.trim().toLowerCase();
            if (lower.equals("sent")) {
                result = STATUS_SENT;
            } else if (lower.startsWith("disregard")) {
                result = STATUS_DISREGARDED;
            } else if (lower.equals("stored")) {
                result = STATUS_STORED;
            }
        }
        return result;
    }
    
    // Removes the first matching text from a category array and gives back the new count
    private int removeText(String[] array, int count, String text) {
        int position = -1;
        int newCount = count;
 
        for (int i = 0; i < count && position == -1; i++) {
            if (array[i].equals(text)) {
                position = i;
            }
        }
 
        if (position != -1) {
            for (int i = position; i < count - 1; i++) {
                array[i] = array[i + 1];
            }
            array[count - 1] = null;
            newCount = count - 1;
        }
        return newCount;
    }
 
    private String[] trimmedCopy(String[] source, int count) {
        String[] copy = new String[count];
        for (int i = 0; i < count; i++) {
            copy[i] = source[i];
        }
        return copy;
    }
    
}
