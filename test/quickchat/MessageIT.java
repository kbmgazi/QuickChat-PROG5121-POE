/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package quickchat;

// JUnit 4 Imports
import org.junit.BeforeClass;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * JUnit 4 Integration Test Suite for the Message class (Part 2).
 * Verifies validation rules, hash generation, sent actions, and session history.
 */
public class MessageIT {

    public MessageIT() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    // =========================================================================
    // 1. Message Length Validation Tests (Max 250 characters)
    // =========================================================================
    @Test
    public void testCheckMessageLengthStatus_Success() {
        System.out.println("checkMessageLengthStatus - Success");
        String text = "Hi Mike, can you join us for dinner tonight?";
        Message instance = new Message("+27718693002", text);

        assertTrue("Message length should be <= 250 characters", text.length() <= 250);
        String expected = "Message ready to send.";
        String actual = instance.checkMessageLengthStatus(text);
        assertEquals(expected, actual);
    }

    @Test
    public void testCheckMessageLengthStatus_FailureExceedsLimit() {
        System.out.println("checkMessageLengthStatus - Failure (> 250 characters)");
        
        // Construct a string with 260 characters (exceeds by 10)
        StringBuilder longText = new StringBuilder();
        for (int i = 0; i < 260; i++) {
            longText.append("a");
        }

        Message instance = new Message("+27718693002", longText.toString());
        assertTrue("Message length should exceed 250 characters", longText.length() > 250);

        int excess = longText.length() - 250;
        String expected = "Message exceeds 250 characters by " + excess + ", please reduce size.";
        String actual = instance.checkMessageLengthStatus(longText.toString());
        assertEquals(expected, actual);
    }

    // =========================================================================
    // 2. Recipient Cell Phone Validation Tests
    // =========================================================================
    @Test
    public void testCheckRecipientCell_ValidSuccess() {
        System.out.println("checkRecipientCell - Valid (+ prefixed, <= 10 chars)");
        String cellNumber = "+27718693002";
        Message instance = new Message();

        boolean result = instance.checkRecipientCell(cellNumber);
        assertTrue("Recipient cell starting with + and <= 10 chars should be valid", result);
    }

    @Test
    public void testCheckRecipientCell_InvalidNoInternationalCode() {
        System.out.println("checkRecipientCell - Invalid (Missing + prefix)");
        String cellNumber = "08575975889";
        Message instance = new Message();

        boolean result = instance.checkRecipientCell(cellNumber);
        assertFalse("Recipient cell without '+' prefix should be invalid", result);
    }

    @Test
    public void testCheckRecipientCell_InvalidTooLong() {
        System.out.println("checkRecipientCell - Invalid (> 10 chars)");
        String cellNumber = "+2771869300211";
        Message instance = new Message();

        boolean result = instance.checkRecipientCell(cellNumber);
        assertFalse("Recipient cell longer than 10 characters should be invalid", result);
    }

    // =========================================================================
    // 3. Message ID Validation Tests
    // =========================================================================
    @Test
    public void testCheckMessageID_ValidSuccess() {
        System.out.println("checkMessageID - Valid (<= 10 chars)");
        String id = "0012345678";
        Message instance = new Message();

        boolean result = instance.checkMessageID(id);
        assertTrue("Message ID with <= 10 characters should be valid", result);
    }

    @Test
    public void testCheckMessageID_InvalidTooLong() {
        System.out.println("checkMessageID - Invalid (> 10 chars)");
        String id = "0012345678999";
        Message instance = new Message();

        boolean result = instance.checkMessageID(id);
        assertFalse("Message ID longer than 10 characters should be invalid", result);
    }

    // =========================================================================
    // 4. Message Hash Generation Tests (Test Case 1 Data)
    // Format: FIRST_2_DIGITS_ID:MSG_NUM:FIRST_WORD+LAST_WORD (ALL CAPS)
    // =========================================================================
    @Test
    public void testCreateMessageHash_TestCase1() {
        System.out.println("createMessageHash - Test Case 1");
        String id = "0012345678";
        int msgNum = 0;
        String text = "Hi Mike, can you join us for dinner tonight?";
        Message instance = new Message("+27718693002", text);

        String expectedHash = "00:0:HITONIGHT";
        String actualHash = instance.createMessageHash(id, msgNum, text);

        assertEquals("Message Hash should match expected format 00:0:HITONIGHT", expectedHash, actualHash);
    }

    // =========================================================================
    // 5. SentMessage Action Choice Tests
    // =========================================================================
    @Test
    public void testSentMessage_Option1Send() {
        System.out.println("sentMessage - Choice 1 (Send)");
        Message instance = new Message("+27718693002", "Hi Mike");

        String expected = "Message successfully sent.";
        String actual = instance.sentMessage(1);
        assertEquals(expected, actual);
    }

    @Test
    public void testSentMessage_Option2Discard() {
        System.out.println("sentMessage - Choice 2 (Discard)");
        Message instance = new Message("08575975889", "Hi Keegan");

        String expected = "Press 0 to delete the message.";
        String actual = instance.sentMessage(2);
        assertEquals(expected, actual);
    }

    @Test
    public void testSentMessage_Option3Store() {
        System.out.println("sentMessage - Choice 3 (Store)");
        Message instance = new Message("+27718693002", "Store this message");

        String expected = "Message successfully stored.";
        String actual = instance.sentMessage(3);
        assertEquals(expected, actual);
    }

    // =========================================================================
    // 6. JSON File Persistence Test (storeMessage)
    // =========================================================================
    @Test
    public void testStoreMessage_Success() {
        System.out.println("storeMessage");
        Message instance = new Message("+27718693002", "Test store message");
        instance.setMessageID("0012345678");
        instance.createMessageHash("0012345678", 0, "Test store message");

        boolean result = instance.storeMessage("messages.json");
        assertTrue("storeMessage should return true when successfully saving to file", result);
    }

    // =========================================================================
    // 7. Session Log & Running Totals Tests
    // =========================================================================
    @Test
    public void testAddSessionLogAndPrintMessages() {
        System.out.println("addSessionLog and printMessages");
        
        String summary = "Message ID: 0012345678\nRecipient: +27718693002\nMessage: Hi Mike";
        Message.addSessionLog(summary);

        String sessionLog = Message.printMessages();
        assertNotNull("Session log string should not be null", sessionLog);
        assertTrue("Session log should contain the summary text", sessionLog.contains("Message ID: 0012345678"));
    }

    @Test
    public void testReturnTotalMessages() {
        System.out.println("returnTotalMessages");
        int total = Message.returnTotalMessages();
        assertTrue("Total messages sent counter should be >= 0", total >= 0);
    }
}