package com.example.androidassignments;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.matcher.ViewMatchers.isChecked;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isNotChecked;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.app.Activity;
import android.app.Instrumentation;
import android.provider.MediaStore;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ListItemsActivityInstrumentedTest {

    @Rule
    public ActivityScenarioRule<ListItemsActivity> activityRule =
            new ActivityScenarioRule<>(ListItemsActivity.class);

    @Before
    public void setUp() {
        Intents.init();
        // Stub camera intent to avoid launching external camera app
        intending(hasAction(MediaStore.ACTION_IMAGE_CAPTURE))
                .respondWith(new Instrumentation.ActivityResult(Activity.RESULT_CANCELED, null));
    }

    @After
    public void tearDown() {
        Intents.release();
    }

    @Test
    public void testUIElementsDisplayed() {
        // Test that all UI elements are visible
        onView(withId(R.id.imageButton)).check(matches(isDisplayed()));
        onView(withId(R.id.switchToggle)).check(matches(isDisplayed()));
        onView(withId(R.id.checkBox)).check(matches(isDisplayed()));
    }

    @Test
    public void testSwitchToggleOn() throws InterruptedException {
        // Test turning switch on
        onView(withId(R.id.switchToggle)).perform(click());

        Thread.sleep(200);

        // Verify switch is checked
        onView(withId(R.id.switchToggle)).check(matches(isChecked()));
    }

    @Test
    public void testSwitchToggleOff() throws InterruptedException {
        // Turn switch on first
        onView(withId(R.id.switchToggle)).perform(click());
        Thread.sleep(150);

        // Turn switch off
        onView(withId(R.id.switchToggle)).perform(click());
        Thread.sleep(150);

        // Verify switch is not checked
        onView(withId(R.id.switchToggle)).check(matches(isNotChecked()));
    }

    @Test
    public void testSwitchMultipleToggles() throws InterruptedException {
        // Toggle switch multiple times
        for (int i = 0; i < 3; i++) {
            onView(withId(R.id.switchToggle)).perform(click());
            Thread.sleep(120);
        }

        // After odd number of toggles, should be checked
        onView(withId(R.id.switchToggle)).check(matches(isChecked()));
    }

    @Test
    public void testCheckBoxClick() {
        // Click checkbox - this shows a confirmation dialog
        onView(withId(R.id.checkBox)).perform(click());

        // Assert the dialog is shown (title, message, and buttons visible)
        onView(withText(R.string.dialog_title)).inRoot(isDialog()).check(matches(isDisplayed()));
        onView(withText(R.string.dialog_message)).inRoot(isDialog()).check(matches(isDisplayed()));
        onView(withText(R.string.ok)).inRoot(isDialog()).check(matches(isDisplayed()));
        onView(withText(R.string.cancel)).inRoot(isDialog()).check(matches(isDisplayed()));
    }

    @Test
    public void testCheckBoxDialogPositiveButton() throws InterruptedException {
        // Click checkbox to show dialog
        onView(withId(R.id.checkBox)).perform(click());

        Thread.sleep(150);

        // Click OK button on dialog (finishes activity)
        onView(withText(R.string.ok)).inRoot(isDialog()).perform(click());

        Thread.sleep(150);
    }

    @Test
    public void testCheckBoxDialogNegativeButton() throws InterruptedException {
        // Click checkbox to show dialog
        onView(withId(R.id.checkBox)).perform(click());

        Thread.sleep(150);

        // Click Cancel button on dialog (unchecks checkbox)
        onView(withText(R.string.cancel)).inRoot(isDialog()).perform(click());

        Thread.sleep(150);

        // Activity should still be displayed and checkbox should be unchecked
        onView(withId(R.id.checkBox)).check(matches(isDisplayed()));
        onView(withId(R.id.checkBox)).check(matches(isNotChecked()));
    }

    @Test
    public void testImageButtonClick() {
        // Click image button; intent should be sent
        onView(withId(R.id.imageButton)).perform(click());
        intended(hasAction(MediaStore.ACTION_IMAGE_CAPTURE));

        // UI should still be functional
        onView(withId(R.id.imageButton)).check(matches(isDisplayed()));
    }

    @Test
    public void testAllUIInteractionsSequentially() throws InterruptedException {
        // Toggle switch
        onView(withId(R.id.switchToggle)).perform(click());
        Thread.sleep(120);
        onView(withId(R.id.switchToggle)).check(matches(isChecked()));

        // Click image button (camera intent is stubbed)
        onView(withId(R.id.imageButton)).perform(click());
        intended(hasAction(MediaStore.ACTION_IMAGE_CAPTURE));
        Thread.sleep(120);

        // Click checkbox and cancel dialog
        onView(withId(R.id.checkBox)).perform(click());
        Thread.sleep(150);
        onView(withText(R.string.cancel)).inRoot(isDialog()).perform(click());
        Thread.sleep(120);

        // Verify all elements still displayed
        onView(withId(R.id.imageButton)).check(matches(isDisplayed()));
        onView(withId(R.id.switchToggle)).check(matches(isDisplayed()));
        onView(withId(R.id.checkBox)).check(matches(isDisplayed()));
    }

    @Test
    public void testInitialState() {
        // Test that UI elements are in correct initial state
        onView(withId(R.id.switchToggle)).check(matches(isNotChecked()));
        onView(withId(R.id.checkBox)).check(matches(isNotChecked()));
        onView(withId(R.id.imageButton)).check(matches(isDisplayed()));
    }
}
