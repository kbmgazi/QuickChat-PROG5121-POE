package quickchat;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Kea
 */
public class ManageMessagesIT {
    private final String[] ids = {"1000000001", "2000000002", "3000000003", "0838884567", "5000000005"};
    private final String[] recipients = {"+27834557896", "+27838884567", "+27834484567", "0838884567", "+27838884567"};
    private final String[] texts = {
        "Did you get the cake?",
        "Where are you? You are late! I have asked you to be on time.",
        "Yohoooo, I am at your gate.",
        "It is dinner time!",
        "Ok, I am leaving without you."
    };
    private final String[] statuses = {"Sent", "Stored", "Disregard", "Sent", "Stored"};
    private final String[] hashes = new String[5];

    private ManageMessages manager;

    /**
     * Loads the five test messages before each test.
     */
    @Before
    public void setUp() {
        Message helper = new Message();
        for (int i = 0; i < 5; i++) {
            hashes[i] = helper.createMessageHash(ids[i], i, texts[i]);
        }
        manager = buildManager(5);
    }

    /**
     * Loads the first howMany test messages into a new ManageMessages.
     */
    private ManageMessages buildManager(int howMany) {
        ManageMessages result = new ManageMessages("Kyle Smith");
        for (int i = 0; i < howMany; i++) {
            result.addMessage(ids[i], hashes[i], recipients[i], texts[i], statuses[i]);
        }
        return result;
    }

    /**
     * Test of addMessage method, of class ManageMessages.
     */
    @Test
    public void testSentMessagesArrayCorrectlyPopulated() {
        System.out.println("Sent Messages array correctly populated (messages 1-4)");
        ManageMessages firstFour = buildManager(4);
        String[] expected = {"Did you get the cake?", "It is dinner time!"};
        assertArrayEquals(expected, firstFour.getSentMessages());
    }

    /**
     * Test of findLongestStoredMessage method, of class ManageMessages.
     */
    @Test
    public void testDisplayLongestMessage() {
        System.out.println("Display the longest message (messages 1-4)");
        ManageMessages firstFour = buildManager(4);
        assertEquals("Where are you? You are late! I have asked you to be on time.",
                firstFour.findLongestStoredMessage());
    }

    /**
     * Test of searchByMessageID method, of class ManageMessages.
     */
    @Test
    public void testSearchForMessageID() {
        System.out.println("Search for message ID 0838884567 (message 4)");
        ManageMessages firstFour = buildManager(4);
        assertEquals("It is dinner time!", firstFour.getMessageTextByID("0838884567"));
        assertEquals("Recipient: 0838884567\nMessage: It is dinner time!",
                firstFour.searchByMessageID("0838884567"));
    }

    /**
     * Test of getMessagesForRecipient method, of class ManageMessages.
     */
    @Test
    public void testSearchMessagesForRecipient() {
        System.out.println("Search all sent or stored messages for +27838884567");
        String[] expected = {
            "Where are you? You are late! I have asked you to be on time.",
            "Ok, I am leaving without you."
        };
        assertArrayEquals(expected, manager.getMessagesForRecipient("+27838884567"));
    }

    /**
     * Test of deleteByHash method, of class ManageMessages.
     */
    @Test
    public void testDeleteMessageUsingHash() {
        System.out.println("Delete test message 2 using its hash");

        String expected = "Message: \"Where are you? You are late! I have asked you to be on time\" successfully deleted.";
        assertEquals(expected, manager.deleteByHash(hashes[1]));
    }

    /**
     * Test of buildReport method, of class ManageMessages.
     */
    @Test
    public void testDisplayReport() {
        System.out.println("Display report of sent messages (hash, recipient, message)");
        String report = manager.buildReport("Sent");

        assertTrue(report.contains(hashes[0]));
        assertTrue(report.contains("+27834557896"));
        assertTrue(report.contains("Did you get the cake?"));

        assertTrue(report.contains(hashes[3]));
        assertTrue(report.contains("0838884567"));
        assertTrue(report.contains("It is dinner time!"));

        assertFalse(report.contains("Where are you?"));
        assertFalse(report.contains("Yohoooo"));
    }

    /**
     * Test of addMessage method, of class ManageMessages.
     */
    @Test
    public void testDisregardedAndStoredArraysPopulated() {
        System.out.println("Disregarded and Stored arrays populated");
        assertArrayEquals(new String[]{"Yohoooo, I am at your gate."}, manager.getDisregardedMessages());
        assertArrayEquals(new String[]{texts[1], texts[4]}, manager.getStoredMessages());
        assertEquals(5, manager.getMessageHashes().length);
        assertEquals(5, manager.getMessageIDs().length);
    }

    /**
     * Test of addMessage method, of class ManageMessages.
     */
    @Test
    public void testAddMessageRejectsUnknownStatus() {
        System.out.println("Unknown status is rejected");
        assertFalse(manager.addMessage("6000000006", "60:5:HIBYE", "+27830000000", "Hi bye", "Pending"));
        assertEquals(5, manager.getTotalCount());
    }

    /**
     * Test of searchByMessageID method, of class ManageMessages.
     */
    @Test
    public void testSearchForMessageIDNotFound() {
        System.out.println("Search for an ID that does not exist");
        assertEquals("No message found with ID: 9999999999", manager.searchByMessageID("9999999999"));
        assertEquals("", manager.getMessageTextByID("9999999999"));
    }

    /**
     * Test of searchByRecipient method, of class ManageMessages.
     */
    @Test
    public void testSearchRecipientWithNoMessages() {
        System.out.println("Search for a recipient with no messages");
        assertEquals("No messages found for recipient: +27000000000", manager.searchByRecipient("+27000000000"));

        assertEquals(0, manager.getMessagesForRecipient("+27834484567").length);
    }

    /**
     * Test of deleteByHash method, of class ManageMessages.
     */
    @Test
    public void testDeleteRemovesMessageFromEveryArray() {
        System.out.println("Delete removes the message from every array");
        manager.deleteByHash(hashes[1]);

        assertEquals(4, manager.getTotalCount());
        assertEquals(4, manager.getMessageHashes().length);
        assertArrayEquals(new String[]{texts[4]}, manager.getStoredMessages());
        assertEquals("", manager.getMessageTextByID(ids[1]));
    }

    /**
     * Test of deleteByHash method, of class ManageMessages.
     */
    @Test
    public void testDeleteMessageWithUnknownHash() {
        System.out.println("Delete with a hash that does not exist");
        assertEquals("No message found with hash: 00:0:NOTHING", manager.deleteByHash("00:0:NOTHING"));
        assertEquals(5, manager.getTotalCount());
    }

    /**
     * Test of buildReport method, of class ManageMessages.
     */
    @Test
    public void testStoredReportAndSenderList() {
        System.out.println("Stored report and sender/recipient list");
        String report = manager.buildReport("Stored");
        assertTrue(report.contains(ids[1]));
        assertTrue(report.contains(ids[4]));
        assertFalse(report.contains("Did you get the cake?"));

        String list = manager.displaySenderAndRecipient();
        assertTrue(list.contains("Sender: Kyle Smith | Recipient: +27838884567"));
    }

    /**
     * Test of buildSummary method, of class ManageMessages.
     */
    @Test
    public void testBuildSummary() {
        System.out.println("Summary counts");
        assertEquals("Sent: 2 | Disregarded: 1 | Stored: 2", manager.buildSummary());
    }
}