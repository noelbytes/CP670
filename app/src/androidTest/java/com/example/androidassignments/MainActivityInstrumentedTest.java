package com.example.androidassignments;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class MainActivityInstrumentedTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void setUp() {
        Intents.init();
    }

    @After
    public void tearDown() {
        Intents.release();
    }

    @Test
    public void testUIElementsDisplayed() {
        // Test that all UI elements are visible
        onView(withId(R.id.button)).check(matches(isDisplayed()));
        onView(withId(R.id.startChatButton)).check(matches(isDisplayed()));
    }

    @Test
    public void testStartChatButtonNavigatesToChatWindow() {
        // Click the start chat button
        onView(withId(R.id.startChatButton)).perform(click());

        // Verify that ChatWindow activity was launched
        intended(hasComponent(ChatWindow.class.getName()));
    }

    @Test
    public void testButtonNavigatesToListItemsActivity() {
        // Click the button to navigate to ListItemsActivity
        onView(withId(R.id.button)).perform(click());

        // Verify that ListItemsActivity was launched
        intended(hasComponent(ListItemsActivity.class.getName()));
    }

    @Test
    public void testMultipleNavigations() throws InterruptedException {
        // Test navigating to ChatWindow
        onView(withId(R.id.startChatButton)).perform(click());
        intended(hasComponent(ChatWindow.class.getName()));

        Thread.sleep(500);

        // Go back
        androidx.test.espresso.Espresso.pressBack();

        Thread.sleep(500);

        // Test navigating to ListItemsActivity
        onView(withId(R.id.button)).check(matches(isDisplayed()));
    }

    @Test
    public void testActivityLifecycle() throws InterruptedException {
        // Test that activity handles lifecycle correctly
        onView(withId(R.id.button)).check(matches(isDisplayed()));

        // Navigate away and back
        onView(withId(R.id.startChatButton)).perform(click());
        Thread.sleep(500);

        androidx.test.espresso.Espresso.pressBack();
        Thread.sleep(500);

        // Verify we're back on MainActivity
        onView(withId(R.id.button)).check(matches(isDisplayed()));
        onView(withId(R.id.startChatButton)).check(matches(isDisplayed()));
    }
}
