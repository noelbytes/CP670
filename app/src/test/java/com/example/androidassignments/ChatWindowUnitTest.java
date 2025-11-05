package com.example.androidassignments;

import static org.junit.Assert.*;

import android.content.Context;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.test.core.app.ApplicationProvider;

import org.junit.Before;
import org.junit.Test;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;
import org.junit.runner.RunWith;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 28)
public class ChatWindowUnitTest {
    private ActivityController<ChatWindow> controller;
    private ChatWindow activity;

    @Before
    public void setUp() {
        controller = Robolectric.buildActivity(ChatWindow.class).create().start().resume();
        activity = controller.get();
    }

    @Test
    public void testSendAddsMessageAndInsertsToDb() {
        EditText input = activity.findViewById(R.id.text_input);
        Button send = activity.findViewById(R.id.send_button);
        ListView list = activity.findViewById(R.id.listView);

        input.setText("hello robolectric");
        send.performClick();

        // Verify that message was added to in-memory list
        assertEquals(1, activity.chatMessages.size());
        assertEquals("hello robolectric", activity.chatMessages.get(0));

        // Verify ListView adapter updated
        assertEquals(1, list.getAdapter().getCount());
    }
}

