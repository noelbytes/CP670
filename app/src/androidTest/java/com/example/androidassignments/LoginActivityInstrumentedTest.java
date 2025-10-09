package com.example.androidassignments;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class LoginActivityInstrumentedTest {

    @Rule
    public ActivityScenarioRule<LoginActivity> activityRule =
            new ActivityScenarioRule<>(LoginActivity.class);

    private Context context;
    private SharedPreferences sharedPreferences;

    @Before
    public void setUp() {
        context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        sharedPreferences = context.getSharedPreferences(
                LoginActivity.SHARED_PREFERENCES_NAME,
                Context.MODE_PRIVATE
        );
        // Clear any saved preferences before each test
        sharedPreferences.edit().clear().commit();
        Intents.init();
    }

    @After
    public void tearDown() {
        // Clean up after tests
        sharedPreferences.edit().clear().commit();
        Intents.release();
    }

    @Test
    public void testUIElementsDisplayed() {
        // Test that all UI elements are visible
        onView(withId(R.id.emailEditText)).check(matches(isDisplayed()));
        onView(withId(R.id.passwordEditText)).check(matches(isDisplayed()));
        onView(withId(R.id.loginButton)).check(matches(isDisplayed()));
    }

    @Test
    public void testValidLoginNavigation() throws InterruptedException {
        // Clear any existing text and enter valid credentials
        onView(withId(R.id.emailEditText))
                .perform(replaceText("test@example.com"), closeSoftKeyboard());
        onView(withId(R.id.passwordEditText))
                .perform(replaceText("Password123#"), closeSoftKeyboard());

        Thread.sleep(500); // Small delay to ensure UI is ready

        onView(withId(R.id.loginButton)).perform(click());

        // Give time for navigation
        Thread.sleep(800);

        intended(hasComponent(MainActivity.class.getName()));
    }

    @Test
    public void testInvalidEmailFormat() {
        // Test with invalid email format
        onView(withId(R.id.emailEditText))
                .perform(replaceText("invalidemail"), closeSoftKeyboard());
        onView(withId(R.id.passwordEditText))
                .perform(replaceText("Password123#"), closeSoftKeyboard());

        onView(withId(R.id.loginButton)).perform(click());

        // Activity should stay on login screen (not navigate)
        onView(withId(R.id.loginButton)).check(matches(isDisplayed()));
    }

    @Test
    public void testEmptyEmail() {
        // Clear email field and test with empty email
        onView(withId(R.id.emailEditText))
                .perform(replaceText(""), closeSoftKeyboard());
        onView(withId(R.id.passwordEditText))
                .perform(replaceText("Password123#"), closeSoftKeyboard());

        onView(withId(R.id.loginButton)).perform(click());

        // Should remain on login screen
        onView(withId(R.id.loginButton)).check(matches(isDisplayed()));
    }

    @Test
    public void testEmptyPassword() {
        // Test with empty password
        onView(withId(R.id.emailEditText))
                .perform(replaceText("test@example.com"), closeSoftKeyboard());
        onView(withId(R.id.passwordEditText))
                .perform(replaceText(""), closeSoftKeyboard());

        onView(withId(R.id.loginButton)).perform(click());

        // Should remain on login screen
        onView(withId(R.id.loginButton)).check(matches(isDisplayed()));
    }

    @Test
    public void testWeakPassword() throws InterruptedException {
        // Current app logic only checks for non-empty password; weak still passes
        onView(withId(R.id.emailEditText))
                .perform(replaceText("test@example.com"), closeSoftKeyboard());
        onView(withId(R.id.passwordEditText))
                .perform(replaceText("weak"), closeSoftKeyboard());

        onView(withId(R.id.loginButton)).perform(click());

        Thread.sleep(800);

        // Expect navigation to MainActivity
        intended(hasComponent(MainActivity.class.getName()));
    }

    @Test
    public void testEmailSavedToSharedPreferences() throws InterruptedException {
        String testEmail = "saved@example.com";

        // Enter valid credentials and login
        onView(withId(R.id.emailEditText))
                .perform(replaceText(testEmail), closeSoftKeyboard());
        onView(withId(R.id.passwordEditText))
                .perform(replaceText("Password123#"), closeSoftKeyboard());

        Thread.sleep(500);

        onView(withId(R.id.loginButton)).perform(click());

        Thread.sleep(500);

        // Verify email was saved to SharedPreferences
        String savedEmail = sharedPreferences.getString(LoginActivity.EMAIL_KEY, "");
        assertEquals(testEmail, savedEmail);
    }

    @Test
    public void testEmailLoadedFromSharedPreferences() {
        // Save email to SharedPreferences first
        String savedEmail = "preloaded@example.com";
        sharedPreferences.edit().putString(LoginActivity.EMAIL_KEY, savedEmail).commit();

        // Launch activity again to test loading
        ActivityScenario<LoginActivity> scenario = ActivityScenario.launch(LoginActivity.class);

        // Verify the email field contains the saved email
        onView(withId(R.id.emailEditText)).check(matches(withText(savedEmail)));

        scenario.close();
    }

    @Test
    public void testPasswordWithSpecialCharacters() throws InterruptedException {
        // Test with password containing special characters
        onView(withId(R.id.emailEditText))
                .perform(replaceText("test@example.com"), closeSoftKeyboard());
        onView(withId(R.id.passwordEditText))
                .perform(replaceText("Pass@123#$"), closeSoftKeyboard());

        Thread.sleep(500);

        onView(withId(R.id.loginButton)).perform(click());

        Thread.sleep(500);

        intended(hasComponent(MainActivity.class.getName()));
    }

    @Test
    public void testDefaultEmailValue() {
        // When no email is saved, check the default value
        onView(withId(R.id.emailEditText)).check(matches(withText("email@domain.com")));
    }
}
