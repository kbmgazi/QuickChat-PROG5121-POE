package quickchat;

import java.io.File;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Kea
 */
public class JsonstorageIT {
    private static final String TEST_FILE = "test_messages.json";

    /**
     * Makes sure no test file is left over before each test.
     */
    @Before
    public void setUp() {
        deleteTestFile();
    }

    /**
     * Deletes the test file after each test.
     */
    @After
    public void tearDown() {
        deleteTestFile();
    }

    /**
     * Deletes the test JSON file if it exists.
     */
    private void deleteTestFile() {
        File file = new File(TEST_FILE);
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * Builds a message the way Main does and stores it with storeMessage.
     */
    private Message storeTestMessage(String id, int number, String recipient, String text) {
        Message msg = new Message(recipient, text);
        msg.setMessageID(id);
        msg.setMessageNumber(number);
        msg.createMessageHash();
        msg.setMessageStatus("Stored");
        msg.storeMessage(TEST_FILE);
        return msg;
    }

    /**
     * Test of readMessages method, of class Jsonstorage.
     */
    @Test
    public void testReadMessagesWrittenByStoreMessage() {
        System.out.println("Read back messages written by storeMessage");
        Message first = storeTestMessage("2000000002", 1, "+27838884567",
                "Where are you? You are late! I have asked you to be on time.");

        Message second = storeTestMessage("5000000005", 2, "+27838884567",
                "He said \"hi\" and left.");

        String[][] rows = Jsonstorage.readMessages(TEST_FILE);

        assertEquals(2, rows.length);
        assertEquals("2000000002", rows[0][0]);
        assertEquals("+27838884567", rows[0][1]);
        assertEquals(first.getMessageHash(), rows[0][2]);
        assertEquals("Where are you? You are late! I have asked you to be on time.", rows[0][3]);
        assertEquals("Stored", rows[0][4]);
        assertEquals(second.getMessageHash(), rows[1][2]);
        assertEquals("He said \"hi\" and left.", rows[1][3]);
    }

    /**
     * Test of readMessages method, of class Jsonstorage.
     */
    @Test
    public void testReadMissingFileGivesEmptyArray() {
        System.out.println("Reading a file that does not exist");
        assertEquals(0, Jsonstorage.readMessages("no_such_file.json").length);
        assertEquals("", Jsonstorage.readFileContent("no_such_file.json"));
    }

    /**
     * Test of writeMessages method, of class Jsonstorage.
     */
    @Test
    public void testWriteThenReadRoundTrip() {
        System.out.println("Write then read back");
        String[] ids = {"1000000001", "2000000002"};
        String[] recipients = {"+27834557896", "+27838884567"};
        String[] hashes = {"10:0:DIDCAKE", "20:1:WHERETIME"};
        String[] texts = {"Did you get the cake?", "Where are you?"};

        assertTrue(Jsonstorage.writeMessages(TEST_FILE, ids, recipients, hashes, texts, 2));

        String[][] rows = Jsonstorage.readMessages(TEST_FILE);
        assertEquals(2, rows.length);
        assertEquals("20:1:WHERETIME", rows[1][2]);
        assertEquals("Where are you?", rows[1][3]);
    }

    /**
     * Test of loadStoredMessages method, of class ManageMessages.
     */
    @Test
    public void testLoadStoredMessagesIntoManager() {
        System.out.println("Load the JSON file into the arrays");
        storeTestMessage("2000000002", 1, "+27838884567", "Where are you?");
        storeTestMessage("5000000005", 2, "+27838884567", "Ok, I am leaving without you.");

        ManageMessages manager = new ManageMessages("Kyle Smith");
        assertEquals(2, manager.loadStoredMessages(TEST_FILE));
        assertArrayEquals(new String[]{"Where are you?", "Ok, I am leaving without you."},
                manager.getStoredMessages());
    }

    /**
     * Test of saveStoredMessages method, of class ManageMessages.
     */
    @Test
    public void testDeleteSurvivesReload() {
        System.out.println("A deleted message stays deleted after saving and reloading");
        Message first = storeTestMessage("2000000002", 1, "+27838884567", "Where are you?");
        storeTestMessage("5000000005", 2, "+27838884567", "Ok, I am leaving without you.");

        ManageMessages manager = new ManageMessages("Kyle Smith");
        manager.loadStoredMessages(TEST_FILE);
        manager.deleteByHash(first.getMessageHash());
        assertTrue(manager.saveStoredMessages(TEST_FILE));

        ManageMessages reloaded = new ManageMessages("Kyle Smith");
        assertEquals(1, reloaded.loadStoredMessages(TEST_FILE));
        assertArrayEquals(new String[]{"Ok, I am leaving without you."}, reloaded.getStoredMessages());
    }
}