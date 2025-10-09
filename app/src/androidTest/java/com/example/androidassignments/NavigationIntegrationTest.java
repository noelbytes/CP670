package com.example.androidassignments;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class NavigationIntegrationTest {

    @Rule
    public ActivityScenarioRule<LoginActivity> activityRule =
            new ActivityScenarioRule<>(LoginActivity.class);

    private SharedPreferences sharedPreferences;

    @Before
    public void setUp() {
        // Ensure no pre-filled email interferes with input
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        sharedPreferences = context.getSharedPreferences(LoginActivity.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().commit();
    }

    @After
    public void tearDown() {
        if (sharedPreferences != null) {
            sharedPreferences.edit().clear().commit();
        }
    }

    @Test
    public void testCompleteLoginToMainActivityFlow() throws InterruptedException {
        // Complete login flow
        onView(withId(R.id.emailEditText))
                .perform(replaceText("test@example.com"), closeSoftKeyboard());
        onView(withId(R.id.passwordEditText))
                .perform(replaceText("Password123#"), closeSoftKeyboard());

        Thread.sleep(500);

        onView(withId(R.id.loginButton)).perform(click());

        Thread.sleep(800);

        // Verify we reached MainActivity by checking its views
        onView(withId(R.id.button)).check(matches(isDisplayed()));
        onView(withId(R.id.startChatButton)).check(matches(isDisplayed()));
    }

    @Test
    public void testLoginToMainActivityToChatWindow() throws InterruptedException {
        // Login first
        onView(withId(R.id.emailEditText))
                .perform(replaceText("test@example.com"), closeSoftKeyboard());
        onView(withId(R.id.passwordEditText))
                .perform(replaceText("Password123#"), closeSoftKeyboard());

        Thread.sleep(500);

        onView(withId(R.id.loginButton)).perform(click());

        Thread.sleep(800);

        // Navigate to ChatWindow
        onView(withId(R.id.startChatButton)).perform(click());

        Thread.sleep(400);

        // Verify ChatWindow UI elements
        onView(withId(R.id.listView)).check(matches(isDisplayed()));
        onView(withId(R.id.text_input)).check(matches(isDisplayed()));
        onView(withId(R.id.send_button)).check(matches(isDisplayed()));
    }

    @Test
    public void testLoginToMainActivityToListItems() throws InterruptedException {
        // Login first
        onView(withId(R.id.emailEditText))
                .perform(replaceText("test@example.com"), closeSoftKeyboard());
        onView(withId(R.id.passwordEditText))
                .perform(replaceText("Password123#"), closeSoftKeyboard());

        Thread.sleep(500);

        onView(withId(R.id.loginButton)).perform(click());

        Thread.sleep(800);

        // Navigate to ListItemsActivity
        onView(withId(R.id.button)).perform(click());

        Thread.sleep(400);

        // Verify ListItemsActivity UI elements
        onView(withId(R.id.imageButton)).check(matches(isDisplayed()));
        onView(withId(R.id.switchToggle)).check(matches(isDisplayed()));
        onView(withId(R.id.checkBox)).check(matches(isDisplayed()));
    }

    @Test
    public void testNavigationWithBackButton() throws InterruptedException {
        // Login
        onView(withId(R.id.emailEditText))
                .perform(replaceText("test@example.com"), closeSoftKeyboard());
        onView(withId(R.id.passwordEditText))
                .perform(replaceText("Password123#"), closeSoftKeyboard());

        Thread.sleep(500);

        onView(withId(R.id.loginButton)).perform(click());

        Thread.sleep(800);

        // Navigate to ChatWindow
        onView(withId(R.id.startChatButton)).perform(click());

        Thread.sleep(400);

        // Press back button
        pressBack();

        Thread.sleep(400);

        // Should be back on MainActivity
        onView(withId(R.id.button)).check(matches(isDisplayed()));
        onView(withId(R.id.startChatButton)).check(matches(isDisplayed()));
    }

    @Test
    public void testFullUserJourney() throws InterruptedException {
        // 1. Login
        onView(withId(R.id.emailEditText))
                .perform(replaceText("journey@test.com"), closeSoftKeyboard());
        onView(withId(R.id.passwordEditText))
                .perform(replaceText("Journey123#"), closeSoftKeyboard());

        Thread.sleep(500);

        onView(withId(R.id.loginButton)).perform(click());

        Thread.sleep(800);

        // 2. Go to ChatWindow
        onView(withId(R.id.startChatButton)).perform(click());

        Thread.sleep(400);

        // 3. Send a message
        onView(withId(R.id.text_input))
                .perform(replaceText("Test message"), closeSoftKeyboard());
        Thread.sleep(200);
        onView(withId(R.id.send_button)).perform(click());

        Thread.sleep(400);

        // 4. Go back to MainActivity
        pressBack();

        Thread.sleep(400);

        // 5. Navigate to ListItemsActivity
        onView(withId(R.id.button)).perform(click());

        Thread.sleep(400);

        // 6. Toggle the switch
        onView(withId(R.id.switchToggle)).perform(click());

        Thread.sleep(300);

        // Verify we're still on ListItemsActivity
        onView(withId(R.id.imageButton)).check(matches(isDisplayed()));
    }
}
