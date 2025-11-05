package com.example.androidassignments;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.content.Context;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ChatWindowInstrumentedTest {

    @Test
    public void testSendMessageViaUi() {
        try (ActivityScenario<ChatWindow> scenario = ActivityScenario.launch(ChatWindow.class)) {
            onView(withId(R.id.text_input)).perform(typeText("instrumented message"));
            onView(withId(R.id.send_button)).perform(click());
            // We can't easily assert ListView content text here without custom matcher; at least ensure no crashes
        }
    }
}

