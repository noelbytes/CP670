package com.example.androidassignments;

import android.os.Build;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
@Config(sdk = {Build.VERSION_CODES.P})
public class ChatWindowTest {

    @Test
    public void view_Initialization_on_First_Create() {
        try (ActivityScenario<ChatWindow> scenario = ActivityScenario.launch(ChatWindow.class)) {
            scenario.onActivity(activity -> {
                assertNotNull("ListView should not be null", activity.listView);
                assertNotNull("EditText should not be null", activity.textInput);
                assertNotNull("Button should not be null", activity.sendButton);
            });
        }
    }

    @Test
    public void adapter_Setup_Verification() {
        try (ActivityScenario<ChatWindow> scenario = ActivityScenario.launch(ChatWindow.class)) {
            scenario.onActivity(activity -> {
                assertNotNull("Adapter should not be null", activity.listView.getAdapter());
                assertTrue("Adapter should be an instance of ChatAdapter", activity.listView.getAdapter() instanceof ChatWindow.ChatAdapter);
            });
        }
    }

    @Test
    public void initial_Chat_Message_List_State() {
        try (ActivityScenario<ChatWindow> scenario = ActivityScenario.launch(ChatWindow.class)) {
            scenario.onActivity(activity -> {
                assertNotNull("chatMessages should not be null", activity.chatMessages);
                assertTrue("chatMessages should be empty on creation", activity.chatMessages.isEmpty());
            });
        }
    }

    @Test
    public void send_Button_Click_Listener_Registration() {
        try (ActivityScenario<ChatWindow> scenario = ActivityScenario.launch(ChatWindow.class)) {
            scenario.onActivity(activity -> {
                assertTrue("Send button should have a click listener", activity.sendButton.hasOnClickListeners());
            });
        }
    }

    @Test
    public void send_Empty_Message() {
        try (ActivityScenario<ChatWindow> scenario = ActivityScenario.launch(ChatWindow.class)) {
            scenario.onActivity(activity -> {
                activity.textInput.setText("");
                activity.sendButton.performClick();
                assertTrue("chatMessages should be empty", activity.chatMessages.isEmpty());
            });
        }
    }

    @Test
    public void send_Non_Empty_Message() {
        try (ActivityScenario<ChatWindow> scenario = ActivityScenario.launch(ChatWindow.class)) {
            scenario.onActivity(activity -> {
                activity.textInput.setText("Hello, world!");
                activity.sendButton.performClick();
                assertEquals("chatMessages should have one message", 1, activity.chatMessages.size());
                assertEquals("Message should be 'Hello, world!'", "Hello, world!", activity.chatMessages.get(0));
                assertEquals("EditText should be cleared after sending", "", activity.textInput.getText().toString());
            });
        }
    }

    @Test
    public void send_Message_with_Whitespace_Only() {
        try (ActivityScenario<ChatWindow> scenario = ActivityScenario.launch(ChatWindow.class)) {
            scenario.onActivity(activity -> {
                activity.textInput.setText("   ");
                activity.sendButton.performClick();
                // Assuming the desired behavior is to not send messages with only whitespace
                assertTrue("chatMessages should be empty", activity.chatMessages.isEmpty());
            });
        }
    }

    @Test
    public void send_Multiple_Consecutive_Messages() {
        try (ActivityScenario<ChatWindow> scenario = ActivityScenario.launch(ChatWindow.class)) {
            scenario.onActivity(activity -> {
                activity.textInput.setText("Message 1");
                activity.sendButton.performClick();
                activity.textInput.setText("Message 2");
                activity.sendButton.performClick();
                assertEquals("chatMessages should have two messages", 2, activity.chatMessages.size());
                assertEquals("First message should be 'Message 1'", "Message 1", activity.chatMessages.get(0));
                assertEquals("Second message should be 'Message 2'", "Message 2", activity.chatMessages.get(1));
            });
        }
    }

    @Test
    public void layout_Inflation_Verification() {
        try (ActivityScenario<ChatWindow> scenario = ActivityScenario.launch(ChatWindow.class)) {
            scenario.onActivity(activity -> {
                assertNotNull("Content view should be inflated", activity.findViewById(R.id.main));
            });
        }
    }

    @Test
    public void chatAdapter_getCount_Accuracy() {
        try (ActivityScenario<ChatWindow> scenario = ActivityScenario.launch(ChatWindow.class)) {
            scenario.onActivity(activity -> {
                activity.chatMessages.add("Msg 1");
                activity.chatMessages.add("Msg 2");
                activity.messageAdapter.notifyDataSetChanged();
                assertEquals("Adapter count should match chatMessages size", 2, activity.messageAdapter.getCount());
            });
        }
    }

    @Test
    public void chatAdapter_getItem_Accuracy() {
        try (ActivityScenario<ChatWindow> scenario = ActivityScenario.launch(ChatWindow.class)) {
            scenario.onActivity(activity -> {
                activity.chatMessages.add("Msg 1");
                activity.chatMessages.add("Msg 2");
                activity.messageAdapter.notifyDataSetChanged();
                assertEquals("Item at position 0 should be 'Msg 1'", "Msg 1", activity.messageAdapter.getItem(0));
                assertEquals("Item at position 1 should be 'Msg 2'", "Msg 2", activity.messageAdapter.getItem(1));
            });
        }
    }
}
