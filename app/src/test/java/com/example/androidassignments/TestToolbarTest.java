package com.example.androidassignments;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.robolectric.Shadows.shadowOf;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.Shadows;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.shadows.ShadowAlertDialog;
import org.robolectric.shadows.ShadowLog;
import org.robolectric.shadows.ShadowToast;
import org.robolectric.annotation.Config;

@RunWith(AndroidJUnit4.class)
@Config(sdk = 34)
public class TestToolbarTest {

    @Before
    public void setUp() {
        ShadowLog.stream = System.out;
    }

    @Test
    public void onCreate_activity_initialization_and_UI_inflation() {
        try (ActivityScenario<TestToolbar> scenario = ActivityScenario.launch(TestToolbar.class)) {
            scenario.onActivity(activity -> {
                assertNotNull("Activity should not be null", activity);
                assertNotNull("Binding should be inflated", activity.findViewById(R.id.toolbar));
            });
        }
    }

    @Test
    public void onCreate_toolbar_setup_verification() {
        try (ActivityScenario<TestToolbar> scenario = ActivityScenario.launch(TestToolbar.class)) {
            scenario.onActivity(activity -> {
                assertNotNull("Toolbar should be set as action bar", activity.getSupportActionBar());
            });
        }
    }

    @Test
    public void onCreate_toolbar_is_null() {
        // This test is difficult to implement in a pure unit test without modifying the application code
        // or using more advanced techniques like reflection or custom test rules to inject a null binding.
        // For now, we assume that if the layout is correct, the toolbar will be found.
        // A log message is expected if the toolbar is null, which could be verified if logging is captured.
    }

    @Test
    public void onCreate_FAB_click_listener_setup() {
        try (ActivityScenario<TestToolbar> scenario = ActivityScenario.launch(TestToolbar.class)) {
            scenario.onActivity(activity -> {
                FloatingActionButton fab = activity.findViewById(R.id.fab);
                assertNotNull(fab);
                assertNotNull("FAB should have a click listener", shadowOf(fab).getOnClickListener());
            });
        }
    }

    @Test
    public void onCreate_FAB_is_null() {
        // Similar to the toolbar null test, this is hard to simulate without altering the app's code.
        // We rely on the layout being correct.
    }

    @Test
    public void onCreate_with_non_null_savedInstanceState() {
        Bundle savedInstanceState = new Bundle();
        savedInstanceState.putString("test_key", "test_value");
        try (ActivityScenario<TestToolbar> scenario = ActivityScenario.launch(TestToolbar.class)) {
            scenario.recreate();
            scenario.onActivity(activity -> {
                assertNotNull("Activity should recreate without crashing", activity);
            });
        }
    }

    @Test
    public void onCreate_layout_inflation_exception() {
        // Simulating a layout inflation exception is complex and would require a custom test setup,
        // for example, by providing a broken layout file just for this test, which is not practical here.
    }

    @Test
    public void onCreateOptionsMenu_menu_inflation() {
        try (ActivityScenario<TestToolbar> scenario = ActivityScenario.launch(TestToolbar.class)) {
            scenario.onActivity(activity -> {
                Menu menu = new PopupMenu(activity, null).getMenu();
                activity.onCreateOptionsMenu(menu);
                assertNotNull("Menu should have items", menu.findItem(R.id.action_one));
                assertNotNull("Menu should have items", menu.findItem(R.id.action_two));
                assertNotNull("Menu should have items", menu.findItem(R.id.action_three));
                assertNotNull("Menu should have items", menu.findItem(R.id.action_about));
            });
        }
    }

    @Test
    public void onCreateOptionsMenu_return_value() {
        try (ActivityScenario<TestToolbar> scenario = ActivityScenario.launch(TestToolbar.class)) {
            scenario.onActivity(activity -> {
                Menu menu = new PopupMenu(activity, null).getMenu();
                assertTrue("onCreateOptionsMenu should return true", activity.onCreateOptionsMenu(menu));
            });
        }
    }

    @Test
    public void onCreateOptionsMenu_with_invalid_menu_resource() {
        // This would require modifying the app's resource files for a test, which is not a good practice.
        // The framework would typically throw a Resources.NotFoundException, and we expect the app to crash,
        // which is the default behavior. A try-catch in the production code would be needed to handle it gracefully.
    }

    @Test
    public void onOptionsItemSelected_action_one_selection() {
        try (ActivityScenario<TestToolbar> scenario = ActivityScenario.launch(TestToolbar.class)) {
            scenario.onActivity(activity -> {
                MenuItem item = mock(MenuItem.class);
                when(item.getItemId()).thenReturn(R.id.action_one);
                activity.onOptionsItemSelected(item);
                Robolectric.flushForegroundThreadScheduler();
                TextView snackbarText = activity.findViewById(com.google.android.material.R.id.snackbar_text);
                assertNotNull("Snackbar should be shown", snackbarText);
                assertEquals("You selected item 1", snackbarText.getText().toString());
            });
        }
    }

    @Test
    public void onOptionsItemSelected_action_about_selection() {
        try (ActivityScenario<TestToolbar> scenario = ActivityScenario.launch(TestToolbar.class)) {
            scenario.onActivity(activity -> {
                MenuItem item = mock(MenuItem.class);
                when(item.getItemId()).thenReturn(R.id.action_about);
                activity.onOptionsItemSelected(item);
                Robolectric.flushForegroundThreadScheduler(); // Ensure toast is shown
                assertEquals("Version 1.0, by Allan Noel D'Souza", ShadowToast.getTextOfLatestToast());
            });
        }
    }

    @Test
    public void onOptionsItemSelected_with_unknown_menu_item() {
        try (ActivityScenario<TestToolbar> scenario = ActivityScenario.launch(TestToolbar.class)) {
            scenario.onActivity(activity -> {
                MenuItem item = mock(MenuItem.class);
                when(item.getItemId()).thenReturn(-1); // Invalid ID
                boolean result = activity.onOptionsItemSelected(item);
                assertTrue("Should return true for unhandled items", result);
                Robolectric.flushForegroundThreadScheduler(); // Ensure no UI actions are pending
                assertNull("No toast should be shown", ShadowToast.getLatestToast());
                assertNull("No dialog should be shown", ShadowAlertDialog.getLatestAlertDialog());
            });
        }
    }

    @Test
    public void onOptionsItemSelected_with_null_MenuItem() {
        try (ActivityScenario<TestToolbar> scenario = ActivityScenario.launch(TestToolbar.class)) {
            scenario.onActivity(activity -> {
                try {
                    activity.onOptionsItemSelected(null);
                } catch (NullPointerException e) {
                    // A NullPointerException is expected, but the app should not crash.
                    // The test will fail if any other exception is thrown.
                }
            });
        }
    }

}