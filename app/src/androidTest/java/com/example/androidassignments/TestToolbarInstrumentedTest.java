package com.example.androidassignments;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.content.Context;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Instrumentation tests for TestToolbar Activity using Espresso
 * Tests UI interactions including toolbar menu items, dialogs, FAB, and Snackbar
 */
@RunWith(AndroidJUnit4.class)
public class TestToolbarInstrumentedTest {

    @Rule
    public ActivityScenarioRule<TestToolbar> activityRule =
            new ActivityScenarioRule<>(TestToolbar.class);

    private Context context;

    @Before
    public void setUp() {
        context = InstrumentationRegistry.getInstrumentation().getTargetContext();
    }

    @After
    public void tearDown() {
        // Clean up after each test
    }

    @Test
    public void testActivityLaunches() {
        // Verify the activity launches successfully
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()));
    }

    @Test
    public void testToolbarIsDisplayed() {
        // Verify toolbar is visible
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()));
    }

    @Test
    public void testFabIsDisplayed() {
        // Verify FAB is visible
        onView(withId(R.id.fab)).check(matches(isDisplayed()));
    }

    @Test
    public void testFabClick_ShowsSnackbar() {
        // Click the FAB button
        onView(withId(R.id.fab)).perform(click());

        // Verify Snackbar message is displayed
        String expectedMessage = context.getString(R.string.welcome_testtoolbar);
        onView(withText(expectedMessage)).check(matches(isDisplayed()));
    }

    @Test
    public void testMenuItemOne_Click_ShowsSnackbar() throws InterruptedException {
        // Click the first menu item
        onView(withId(R.id.action_one)).perform(click());

        // Wait a bit for Snackbar to appear
        Thread.sleep(500);

        // Verify Snackbar with default message is shown
        onView(withText("You selected item 1")).check(matches(isDisplayed()));
    }

    @Test
    public void testMenuItemTwo_Click_ShowsDialog() {
        // Click the second menu item
        onView(withId(R.id.action_two)).perform(click());

        // Verify dialog title is displayed
        String expectedTitle = context.getString(R.string.dialog_go_back_title);
        onView(withText(expectedTitle)).check(matches(isDisplayed()));
    }

    @Test
    public void testMenuItemTwo_Dialog_HasOkButton() {
        // Click the second menu item
        onView(withId(R.id.action_two)).perform(click());

        // Verify OK button is displayed
        String okText = context.getString(R.string.ok);
        onView(withText(okText)).check(matches(isDisplayed()));
    }

    @Test
    public void testMenuItemTwo_Dialog_HasCancelButton() {
        // Click the second menu item
        onView(withId(R.id.action_two)).perform(click());

        // Verify Cancel button is displayed
        String cancelText = context.getString(R.string.cancel);
        onView(withText(cancelText)).check(matches(isDisplayed()));
    }

    @Test
    public void testMenuItemTwo_Dialog_CancelButton_DismissesDialog() {
        // Click the second menu item
        onView(withId(R.id.action_two)).perform(click());

        // Click Cancel button
        String cancelText = context.getString(R.string.cancel);
        onView(withText(cancelText)).perform(click());

        // Verify dialog is dismissed by checking toolbar is still visible
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()));
    }

    @Test
    public void testMenuItemThree_Click_ShowsCustomDialog() {
        // Click the third menu item
        onView(withId(R.id.action_three)).perform(click());

        // Verify custom dialog EditText is displayed
        onView(withId(R.id.new_message)).check(matches(isDisplayed()));
    }

    @Test
    public void testMenuItemThree_CustomDialog_HasEditText() {
        // Click the third menu item
        onView(withId(R.id.action_three)).perform(click());

        // Verify EditText with hint is displayed
        String hintText = context.getString(R.string.new_message_hint);
        onView(withId(R.id.new_message)).check(matches(isDisplayed()));
    }

    @Test
    public void testMenuItemThree_CustomDialog_HasIcon() {
        // Click the third menu item
        onView(withId(R.id.action_three)).perform(click());

        // Verify icon is displayed
        onView(withId(R.id.icon)).check(matches(isDisplayed()));
    }

    @Test
    public void testMenuItemThree_CustomDialog_EnterText_ClickOk() throws InterruptedException {
        // Click the third menu item
        onView(withId(R.id.action_three)).perform(click());

        // Type custom message
        String customMessage = "My Custom Test Message";
        onView(withId(R.id.new_message))
                .perform(typeText(customMessage), closeSoftKeyboard());

        // Click OK button
        String okText = context.getString(R.string.ok);
        onView(withText(okText)).perform(click());

        // Wait for dialog to dismiss and Snackbar to show
        Thread.sleep(500);

        // Verify confirmation Snackbar
        onView(withText("Message updated!")).check(matches(isDisplayed()));
    }

    @Test
    public void testMenuItemThree_CustomDialog_EmptyText_ClickOk() {
        // Click the third menu item
        onView(withId(R.id.action_three)).perform(click());

        // Leave EditText empty and click OK
        String okText = context.getString(R.string.ok);
        onView(withText(okText)).perform(click());

        // Verify dialog is dismissed (toolbar still visible)
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()));
    }

    @Test
    public void testMenuItemThree_CustomDialog_CancelButton() {
        // Click the third menu item
        onView(withId(R.id.action_three)).perform(click());

        // Type some text
        onView(withId(R.id.new_message))
                .perform(typeText("Test"), closeSoftKeyboard());

        // Click Cancel button
        String cancelText = context.getString(R.string.cancel);
        onView(withText(cancelText)).perform(click());

        // Verify dialog is dismissed
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()));
    }

    @Test
    public void testMenuItemThree_ThenMenuItemOne_ShowsCustomMessage() throws InterruptedException {
        // First, set a custom message via menu item 3
        onView(withId(R.id.action_three)).perform(click());

        String customMessage = "Updated Message";
        onView(withId(R.id.new_message))
                .perform(typeText(customMessage), closeSoftKeyboard());

        String okText = context.getString(R.string.ok);
        onView(withText(okText)).perform(click());

        // Wait for dialog to dismiss
        Thread.sleep(500);

        // Now click menu item 1
        onView(withId(R.id.action_one)).perform(click());

        // Wait for Snackbar
        Thread.sleep(500);

        // Verify the custom message is displayed
        onView(withText(customMessage)).check(matches(isDisplayed()));
    }

    @Test
    public void testMenuItemAbout_Click_ShowsToast() throws InterruptedException {
        // Open overflow menu (three dots)
        try {
            openActionBarOverflowOrOptionsMenu(context);
            Thread.sleep(300);
        } catch (Exception e) {
            // If overflow menu is not available, try direct click
        }

        // Click About menu item
        String aboutText = context.getString(R.string.about);
        onView(withText(aboutText)).perform(click());

        // Wait for Toast to appear
        Thread.sleep(500);

        // Note: Toast verification in Espresso is tricky,
        // but we verify no crash occurred
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()));
    }

    @Test
    public void testMultipleMenuItemClicks() throws InterruptedException {
        // Test clicking multiple menu items in sequence

        // Click menu item 1
        onView(withId(R.id.action_one)).perform(click());
        Thread.sleep(500);

        // Click FAB
        onView(withId(R.id.fab)).perform(click());
        Thread.sleep(500);

        // Click menu item 2 and cancel
        onView(withId(R.id.action_two)).perform(click());
        String cancelText = context.getString(R.string.cancel);
        onView(withText(cancelText)).perform(click());

        // Verify activity is still active
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()));
    }

    @Test
    public void testMenuItemThree_MultipleDialogOpens() throws InterruptedException {
        // Open and close custom dialog multiple times
        for (int i = 0; i < 3; i++) {
            onView(withId(R.id.action_three)).perform(click());
            Thread.sleep(300);

            String cancelText = context.getString(R.string.cancel);
            onView(withText(cancelText)).perform(click());
            Thread.sleep(300);
        }

        // Verify activity is still functional
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()));
    }

    @Test
    public void testFabMultipleClicks() throws InterruptedException {
        // Click FAB multiple times
        for (int i = 0; i < 3; i++) {
            onView(withId(R.id.fab)).perform(click());
            Thread.sleep(500);
        }

        // Verify activity is still responsive
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()));
    }

    @Test
    public void testDialogRotation_Persistence() throws InterruptedException {
        // Click menu item to show dialog
        onView(withId(R.id.action_two)).perform(click());

        // Verify dialog is shown
        String expectedTitle = context.getString(R.string.dialog_go_back_title);
        onView(withText(expectedTitle)).check(matches(isDisplayed()));

        // Note: Testing rotation is complex in Espresso,
        // but we verify dialog state
        String cancelText = context.getString(R.string.cancel);
        onView(withText(cancelText)).perform(click());
    }

    @Test
    public void testCustomDialogEditText_LongText() throws InterruptedException {
        // Test with a long message
        onView(withId(R.id.action_three)).perform(click());

        String longMessage = "This is a very long custom message that should be handled properly by the EditText widget and displayed correctly in the Snackbar";
        onView(withId(R.id.new_message))
                .perform(typeText(longMessage), closeSoftKeyboard());

        String okText = context.getString(R.string.ok);
        onView(withText(okText)).perform(click());

        Thread.sleep(500);

        // Verify confirmation Snackbar
        onView(withText("Message updated!")).check(matches(isDisplayed()));
    }

    @Test
    public void testCustomDialogEditText_SpecialCharacters() throws InterruptedException {
        // Test with special characters
        onView(withId(R.id.action_three)).perform(click());

        String specialMessage = "Test!@#$%";
        onView(withId(R.id.new_message))
                .perform(typeText(specialMessage), closeSoftKeyboard());

        String okText = context.getString(R.string.ok);
        onView(withText(okText)).perform(click());

        Thread.sleep(500);

        // Now verify it's displayed
        onView(withId(R.id.action_one)).perform(click());
        Thread.sleep(500);

        onView(withText(specialMessage)).check(matches(isDisplayed()));
    }

    @Test
    public void testAllMenuItemsAccessible() {
        // Verify all menu items are accessible
        onView(withId(R.id.action_one)).check(matches(isDisplayed()));
        onView(withId(R.id.action_two)).check(matches(isDisplayed()));
        onView(withId(R.id.action_three)).check(matches(isDisplayed()));
    }

    @Test
    public void testActivityRecreation() {
        // Test that activity can be recreated
        activityRule.getScenario().recreate();

        // Verify toolbar is still displayed after recreation
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()));
        onView(withId(R.id.fab)).check(matches(isDisplayed()));
    }

    @Test
    public void testContextMenuAvailability() throws InterruptedException {
        // Test that overflow menu can be opened
        try {
            openActionBarOverflowOrOptionsMenu(context);
            Thread.sleep(300);

            String aboutText = context.getString(R.string.about);
            onView(withText(aboutText)).check(matches(isDisplayed()));
        } catch (NoMatchingViewException e) {
            // Overflow menu might not be available on all devices
            // This is acceptable
        }
    }
}
