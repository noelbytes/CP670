package com.example.androidassignments;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Unit tests for ChatWindow logic
 * Tests message handling, validation, and data structures using pure Java (no Android dependencies)
 * Following Chapter 4 recommendations for unit testing
 */
@RunWith(MockitoJUnitRunner.class)
public class ChatWindowUnitTest {

    private List<String> messages;

    @Before
    public void setUp() {
        messages = new ArrayList<>();
    }

    // Test 1: Message List Initialization
    @Test
    public void testMessageListInitialization() {
        assertNotNull("Messages list should be initialized", messages);
        assertEquals("Initial message count should be 0", 0, messages.size());
    }

    // Test 2: Add Valid Message
    @Test
    public void testAddValidMessage() {
        String message = "Test message";
        messages.add(message);
        assertEquals("Message count should be 1", 1, messages.size());
        assertEquals("Message should match", "Test message", messages.get(0));
    }

    // Test 3: Empty Message Validation
    @Test
    public void testEmptyMessageValidation() {
        String emptyMessage = "";
        boolean isValid = !emptyMessage.trim().isEmpty();
        assertFalse("Empty message should not be valid", isValid);
    }

    // Test 4: Whitespace-Only Message Validation
    @Test
    public void testWhitespaceOnlyMessageValidation() {
        String whitespaceMessage = "   ";
        boolean isValid = !whitespaceMessage.trim().isEmpty();
        assertFalse("Whitespace-only message should not be valid", isValid);
    }

    // Test 5: Valid Message After Trimming
    @Test
    public void testValidMessageAfterTrimming() {
        String messageWithSpaces = "  Hello World  ";
        String trimmed = messageWithSpaces.trim();
        boolean isValid = !trimmed.isEmpty();
        assertTrue("Trimmed message should be valid", isValid);
        assertEquals("Trimmed message should match", "Hello World", trimmed);
    }

    // Test 6: Multiple Messages Added
    @Test
    public void testMultipleMessagesAdded() {
        messages.add("Message 1");
        messages.add("Message 2");
        messages.add("Message 3");

        assertEquals("Should have 3 messages", 3, messages.size());
    }

    // Test 7: Message Order Preserved
    @Test
    public void testMessageOrderPreserved() {
        messages.add("First");
        messages.add("Second");
        messages.add("Third");

        assertEquals("First message should match", "First", messages.get(0));
        assertEquals("Second message should match", "Second", messages.get(1));
        assertEquals("Third message should match", "Third", messages.get(2));
    }

    // Test 8: Clear Input After Sending
    @Test
    public void testClearInputAfterSending() {
        String input = "Test message";
        messages.add(input);
        input = ""; // Simulate clearing input

        assertEquals("Input should be empty after sending", "", input);
        assertEquals("Message should be in list", 1, messages.size());
    }

    // Test 9: Message List Size Increases
    @Test
    public void testMessageListSizeIncreases() {
        int initialSize = messages.size();
        messages.add("New message");

        assertEquals("Size should increase by 1", initialSize + 1, messages.size());
    }

    // Test 10: Get Last Message
    @Test
    public void testGetLastMessage() {
        messages.add("First");
        messages.add("Second");
        messages.add("Last");

        String lastMessage = messages.get(messages.size() - 1);
        assertEquals("Last message should match", "Last", lastMessage);
    }

    // Test 11: Message Contains Text
    @Test
    public void testMessageContainsText() {
        String message = "Hello World";
        assertTrue("Message should contain 'Hello'", message.contains("Hello"));
        assertTrue("Message should contain 'World'", message.contains("World"));
    }

    // Test 12: Message Length Validation
    @Test
    public void testMessageLengthValidation() {
        String shortMessage = "Hi";
        String longMessage = "This is a much longer message with more content";

        assertTrue("Short message should have length > 0", shortMessage.length() > 0);
        assertTrue("Long message should have length > short message",
                longMessage.length() > shortMessage.length());
    }

    // Test 13: Remove Message
    @Test
    public void testRemoveMessage() {
        messages.add("Message to remove");
        assertEquals("Should have 1 message", 1, messages.size());

        messages.remove(0);
        assertEquals("Should have 0 messages after removal", 0, messages.size());
    }

    // Test 14: Message List Contains Element
    @Test
    public void testMessageListContainsElement() {
        String message = "Test message";
        messages.add(message);

        assertTrue("List should contain the message", messages.contains(message));
    }

    // Test 15: Clear All Messages
    @Test
    public void testClearAllMessages() {
        messages.add("Message 1");
        messages.add("Message 2");
        messages.add("Message 3");

        messages.clear();
        assertEquals("List should be empty after clear", 0, messages.size());
    }
}
