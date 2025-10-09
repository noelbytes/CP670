package com.example.androidassignments;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ChatWindowInstrumentedTest {

    @Rule
    public ActivityScenarioRule<ChatWindow> activityRule =
            new ActivityScenarioRule<>(ChatWindow.class);

    @Test
    public void testUIElementsDisplayed() {
        // Test that all UI elements are visible
        onView(withId(R.id.listView)).check(matches(isDisplayed()));
        onView(withId(R.id.text_input)).check(matches(isDisplayed()));
        onView(withId(R.id.send_button)).check(matches(isDisplayed()));
    }

    @Test
    public void testSendMessage() throws InterruptedException {
        String testMessage = "Hello, this is a test message!";

        // Type a message
        onView(withId(R.id.text_input))
                .perform(typeText(testMessage), closeSoftKeyboard());

        Thread.sleep(300);

        // Click send button
        onView(withId(R.id.send_button)).perform(click());

        Thread.sleep(300);

        // Verify the input field is cleared after sending
        onView(withId(R.id.text_input)).check(matches(withText("")));
    }

    @Test
    public void testSendMultipleMessages() throws InterruptedException {
        // Send first message
        onView(withId(R.id.text_input))
                .perform(typeText("First message"), closeSoftKeyboard());
        Thread.sleep(200);
        onView(withId(R.id.send_button)).perform(click());
        Thread.sleep(300);

        // Send second message
        onView(withId(R.id.text_input))
                .perform(typeText("Second message"), closeSoftKeyboard());
        Thread.sleep(200);
        onView(withId(R.id.send_button)).perform(click());
        Thread.sleep(300);

        // Send third message
        onView(withId(R.id.text_input))
                .perform(typeText("Third message"), closeSoftKeyboard());
        Thread.sleep(200);
        onView(withId(R.id.send_button)).perform(click());
        Thread.sleep(300);

        // Verify input is cleared
        onView(withId(R.id.text_input)).check(matches(withText("")));
    }

    @Test
    public void testSendEmptyMessage() {
        // Try to send empty message - should not crash
        onView(withId(R.id.send_button)).perform(click());

        // UI should still be displayed
        onView(withId(R.id.listView)).check(matches(isDisplayed()));
        onView(withId(R.id.text_input)).check(matches(isDisplayed()));
        onView(withId(R.id.send_button)).check(matches(isDisplayed()));
    }

    @Test
    public void testSendMessageWithSpecialCharacters() throws InterruptedException {
        String specialMessage = "Test @#$%^&*() message!";

        onView(withId(R.id.text_input))
                .perform(typeText(specialMessage), closeSoftKeyboard());

        Thread.sleep(300);

        onView(withId(R.id.send_button)).perform(click());

        Thread.sleep(300);

        // Verify input field is cleared
        onView(withId(R.id.text_input)).check(matches(withText("")));
    }

    @Test
    public void testSendLongMessage() throws InterruptedException {
        String longMessage = "This is a very long message that should test the chat functionality with a lot of text to see how it handles longer content in the chat window.";

        onView(withId(R.id.text_input))
                .perform(typeText(longMessage), closeSoftKeyboard());

        Thread.sleep(300);

        onView(withId(R.id.send_button)).perform(click());

        Thread.sleep(300);

        // Verify input field is cleared
        onView(withId(R.id.text_input)).check(matches(withText("")));
    }

    @Test
    public void testListViewIsScrollable() throws InterruptedException {
        // Send multiple messages to test scrolling
        for (int i = 1; i <= 5; i++) {
            onView(withId(R.id.text_input))
                    .perform(typeText("Message " + i), closeSoftKeyboard());
            Thread.sleep(200);
            onView(withId(R.id.send_button)).perform(click());
            Thread.sleep(200);
        }

        // Verify ListView is still displayed
        onView(withId(R.id.listView)).check(matches(isDisplayed()));
    }

    @Test
    public void testInputFieldAcceptsText() throws InterruptedException {
        String testText = "Testing input";

        onView(withId(R.id.text_input))
                .perform(typeText(testText), closeSoftKeyboard());

        Thread.sleep(300);

        // Verify the text is in the input field
        onView(withId(R.id.text_input)).check(matches(withText(testText)));
    }
}
