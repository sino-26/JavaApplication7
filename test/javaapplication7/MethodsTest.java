package javaapplication7;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class MethodsTest {

    // Clear the messages list before each test to avoid contamination
    @BeforeEach
    public void clearMessages() {
        try {
            Field field = Methods.class.getDeclaredField("messages");
            field.setAccessible(true);
            ((ArrayList<?>) field.get(null)).clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 1. Message length test
    @Test
    public void testMessageLengthSuccess() {
        String message = "This is a short message.";
        assertTrue(message.length() <= 250);
    }

    @Test
    public void testMessageLengthFailure() {
        String message = "a".repeat(260);
        assertFalse(message.length() <= 250);
    }

    // 2. Recipient cell format tests
    @Test
    public void testValidRecipientCell() {
        assertTrue(Methods.checkRecipientCell("0821234567"));
    }

    @Test
    public void testInvalidRecipientCell() {
        assertFalse(Methods.checkRecipientCell("123456789"));
    }

    // 3. Message hash generation
    @Test
    public void testCreateMessageHash() {
        String hash = Methods.createMessageHash("0012345678", 0, "Hi tonight");
        assertEquals("00:0:HITONIGHT", hash);
    }

    // 4. Message ID format checks
    @Test
    public void testValidMessageID() {
        assertTrue(Methods.checkMessageID("1234567890"));
    }

    @Test
    public void testInvalidMessageID() {
        assertFalse(Methods.checkMessageID("1234"));
    }

    // 5. Message sent counter
    @Test
    public void testMessageSentCounter() {
        int before = Methods.returnTotalMessages();
        Methods.incrementMessageCount();
        int after = Methods.returnTotalMessages();
        assertEquals(before + 1, after);
    }

    // 6. Password validation
    @Test
    public void testValidPassword() {
        Object result = Methods.validatePassword("Password1!");
        assertTrue(result instanceof Boolean && (Boolean) result);
    }

    @Test
    public void testInvalidPassword() {
        Object result = Methods.validatePassword("pass");
        assertEquals("Password is not correctly formatted.", result);
    }

    // 7. Cellphone validation with +27
    @Test
    public void testValidCellphoneWithCode() {
        Object result = Methods.validateCellphone("+27821234567");
        assertTrue(result instanceof Boolean && (Boolean) result);
    }

    @Test
    public void testInvalidCellphoneWithCode() {
        Object result = Methods.validateCellphone("0821234567");
        assertEquals("Cellphone number incorrectly formatted or does not contain international code.", result);
    }

    // === PART 3 TESTS START HERE ===

    // 8. Populate sent messages and verify content
    @Test
    public void testSentMessagesPopulated() {
        Methods.populateTestMessages();
        String report = Methods.displaySentReport();
        assertTrue(report.contains("Did you get the cake?"));
        assertTrue(report.contains("It is dinner time!"));
    }

    // 9. Longest message
    @Test
    public void testLongestMessage() {
        Methods.populateTestMessages();
        String longest = Methods.displayLongestMessage();
        assertEquals("Where are you? You are late! I have asked you to be on time.", longest);
    }

    // 10. Search by Message ID
    @Test
    public void testSearchByMessageID() {
        Methods.populateTestMessages();
        String result = Methods.searchByMessageID("0838884567");
        assertEquals("It is dinner time!", result);
    }

    // 11. Search messages by recipient (+27838884567)
    @Test
    public void testSearchByRecipient() {
        Methods.populateTestMessages();
        var results = Methods.searchByRecipient("+27838884567");
        assertTrue(results.contains("Where are you? You are late! I have asked you to be on time."));
        assertTrue(results.contains("Ok, I am leaving without you"));
    }

    // 12. Delete a message by hash
    @Test
    public void testDeleteByHash() {
        Methods.populateTestMessages();
        String hash = Methods.createMessageHash("0000000002", 2, "Where are you? You are late! I have asked you to be on time.");
        String result = Methods.deleteByHash(hash);
        assertTrue(result.contains("successfully deleted"));
    }

    // 13. Sent message report
    @Test
    public void testSentMessageReport() {
        Methods.populateTestMessages();
        String report = Methods.displaySentReport();
        assertTrue(report.contains("Hash:"));
        assertTrue(report.contains("To:"));
        assertTrue(report.contains("Message:"));
    }
}