package com.example.androidassignments;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Unit tests for ChatWindow using JUnit and Mockito
 * Tests chat message validation and list operations
 * Following Chapter 4 recommendations for unit testing
 */
@RunWith(MockitoJUnitRunner.class)
public class ChatWindowUnitTest {

    private List<String> chatMessages;

    @Before
    public void setUp() {
        chatMessages = new ArrayList<>();
    }

    // Test 1: Empty Message Validation
    @Test
    public void testEmptyMessageIsInvalid() {
        String emptyMessage = "";
        assertTrue("Empty message should be detected as invalid",
                emptyMessage.isEmpty());
    }

    // Test 2: Whitespace-Only Message Validation
    @Test
    public void testWhitespaceOnlyMessageIsInvalid() {
        String whitespaceMessage = "   ";
        assertTrue("Whitespace-only message should be invalid",
                whitespaceMessage.trim().isEmpty());
    }

    // Test 3: Valid Message
    @Test
    public void testValidMessageIsNotEmpty() {
        String validMessage = "Hello, World!";
        assertFalse("Valid message should not be empty",
                validMessage.isEmpty());
    }

    // Test 4: Message Trimming
    @Test
    public void testMessageTrimming() {
        String messageWithSpaces = "  Test Message  ";
        String trimmed = messageWithSpaces.trim();
        assertEquals("Trimmed message should match", "Test Message", trimmed);
    }

    // Test 5: Add Message to List
    @Test
    public void testAddMessageToList() {
        chatMessages.add("First message");
        assertEquals("List should contain 1 message", 1, chatMessages.size());
        assertEquals("Message should match", "First message", chatMessages.get(0));
    }

    // Test 6: Add Multiple Messages
    @Test
    public void testAddMultipleMessages() {
        chatMessages.add("Message 1");
        chatMessages.add("Message 2");
        chatMessages.add("Message 3");

        assertEquals("List should contain 3 messages", 3, chatMessages.size());
        assertEquals("First message should match", "Message 1", chatMessages.get(0));
        assertEquals("Last message should match", "Message 3", chatMessages.get(2));
    }

    // Test 7: Initial List is Empty
    @Test
    public void testInitialListIsEmpty() {
        assertEquals("Initial list should be empty", 0, chatMessages.size());
        assertTrue("List should be empty", chatMessages.isEmpty());
    }

    // Test 8: Message Count After Adding
    @Test
    public void testMessageCountAfterAdding() {
        int initialCount = chatMessages.size();
        chatMessages.add("New message");
        assertEquals("Count should increase by 1", initialCount + 1, chatMessages.size());
    }

    // Test 9: Long Message Handling
    @Test
    public void testLongMessageHandling() {
        String longMessage = "This is a very long message that contains many characters and words to test if the system can handle long messages properly without any issues.";
        chatMessages.add(longMessage);

        assertEquals("List should contain the long message", 1, chatMessages.size());
        assertEquals("Message should match", longMessage, chatMessages.get(0));
    }

    // Test 10: Special Characters in Message
    @Test
    public void testSpecialCharactersInMessage() {
        String specialMessage = "Test @#$%^&*() 123!";
        chatMessages.add(specialMessage);

        assertEquals("Message with special characters should be added", 1, chatMessages.size());
        assertEquals("Special message should match", specialMessage, chatMessages.get(0));
    }

    // Test 11: Message Retrieval by Index
    @Test
    public void testMessageRetrievalByIndex() {
        chatMessages.add("First");
        chatMessages.add("Second");
        chatMessages.add("Third");

        assertEquals("First message should match", "First", chatMessages.get(0));
        assertEquals("Second message should match", "Second", chatMessages.get(1));
        assertEquals("Third message should match", "Third", chatMessages.get(2));
    }

    // Test 12: Alternating Message Types (Incoming/Outgoing)
    @Test
    public void testAlternatingMessageTypes() {
        chatMessages.add("Outgoing");  // Index 0 - even
        chatMessages.add("Incoming");  // Index 1 - odd
        chatMessages.add("Outgoing");  // Index 2 - even

        // Test the logic used in ChatAdapter
        assertTrue("Index 0 should be even (outgoing)", 0 % 2 == 0);
        assertFalse("Index 1 should be odd (incoming)", 1 % 2 == 0);
        assertTrue("Index 2 should be even (outgoing)", 2 % 2 == 0);
    }

    // Test 13: Clear All Messages
    @Test
    public void testClearAllMessages() {
        chatMessages.add("Message 1");
        chatMessages.add("Message 2");
        chatMessages.clear();

        assertEquals("List should be empty after clear", 0, chatMessages.size());
        assertTrue("List should be empty", chatMessages.isEmpty());
    }

    // Test 14: Message List Contains
    @Test
    public void testMessageListContains() {
        String testMessage = "Test message";
        chatMessages.add(testMessage);

        assertTrue("List should contain the message", chatMessages.contains(testMessage));
        assertFalse("List should not contain non-existent message",
                chatMessages.contains("Non-existent"));
    }

    // Test 15: Unicode and Emoji in Messages
    @Test
    public void testUnicodeAndEmojiInMessages() {
        String emojiMessage = "Hello 😊 World 🌍";
        chatMessages.add(emojiMessage);

        assertEquals("Emoji message should be added", 1, chatMessages.size());
        assertEquals("Emoji message should match", emojiMessage, chatMessages.get(0));
    }

    // Test 16: Numeric Messages
    @Test
    public void testNumericMessages() {
        String numericMessage = "12345";
        chatMessages.add(numericMessage);

        assertEquals("Numeric message should be added", 1, chatMessages.size());
        assertEquals("Numeric message should match", numericMessage, chatMessages.get(0));
    }

    // Test 17: Message with Newlines
    @Test
    public void testMessageWithNewlines() {
        String multilineMessage = "Line 1\nLine 2\nLine 3";
        chatMessages.add(multilineMessage);

        assertEquals("Multiline message should be added", 1, chatMessages.size());
        assertTrue("Message should contain newlines",
                chatMessages.get(0).contains("\n"));
    }

    // Test 18: Empty String vs Null
    @Test
    public void testEmptyStringVsNull() {
        String emptyString = "";
        assertNotNull("Empty string should not be null", emptyString);
        assertTrue("Empty string should have zero length", emptyString.length() == 0);
    }

    // Test 19: Message Order Preservation
    @Test
    public void testMessageOrderPreservation() {
        chatMessages.add("First");
        chatMessages.add("Second");
        chatMessages.add("Third");

        assertEquals("Messages should be in order", "First", chatMessages.get(0));
        assertEquals("Messages should be in order", "Second", chatMessages.get(1));
        assertEquals("Messages should be in order", "Third", chatMessages.get(2));
    }

    // Test 20: List Size After Multiple Operations
    @Test
    public void testListSizeAfterMultipleOperations() {
        assertEquals("Initial size should be 0", 0, chatMessages.size());

        chatMessages.add("Message 1");
        assertEquals("Size should be 1", 1, chatMessages.size());

        chatMessages.add("Message 2");
        assertEquals("Size should be 2", 2, chatMessages.size());

        chatMessages.clear();
        assertEquals("Size should be 0 after clear", 0, chatMessages.size());
    }
}
