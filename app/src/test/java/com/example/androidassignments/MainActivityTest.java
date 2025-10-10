package com.example.androidassignments;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.robolectric.Shadows.shadowOf;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.ActionBar;
import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.ShadowToast;

@RunWith(AndroidJUnit4.class)
@Config(sdk = {Build.VERSION_CODES.P})
public class MainActivityTest {

    @Test
    public void onCreate__Activity_creation_and_view_inflation() {
        try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
            scenario.onActivity(activity -> {
                assertNotNull("Activity should be created", activity);
                View contentView = activity.findViewById(android.R.id.content);
                assertNotNull("Content view should be inflated", contentView);
            });
        }
    }

    @Test
    public void onCreate__Up_button_enabled_check() {
        try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
            scenario.onActivity(activity -> {
                ActionBar actionBar = activity.getSupportActionBar();
                assertNotNull("Action bar should not be null", actionBar);
                assertTrue("Up button should be enabled", (actionBar.getDisplayOptions() & ActionBar.DISPLAY_HOME_AS_UP) != 0);
            });
        }
    }

    @Test
    public void onCreate__UI_elements_initialization() {
        try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
            scenario.onActivity(activity -> {
                assertNotNull("Button for ListItemsActivity should not be null", activity.findViewById(R.id.button));
                assertNotNull("Button for ChatWindow should not be null", activity.findViewById(R.id.startChatButton));
                assertNotNull("Button for TestToolbar should not be null", activity.findViewById(R.id.testToolbarButton));
            });
        }
    }

    @Test
    public void onCreate___ListItemsActivity__button_click_listener() {
        try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
            scenario.onActivity(activity -> {
                activity.findViewById(R.id.button).performClick();
                ShadowActivity shadowActivity = shadowOf(activity);
                ShadowActivity.IntentForResult intentForResult = shadowActivity.getNextStartedActivityForResult();
                assertNotNull("Intent should be started for result", intentForResult);
                assertEquals(ListItemsActivity.class.getName(), intentForResult.intent.getComponent().getClassName());
                assertEquals(10, intentForResult.requestCode);
            });
        }
    }

    @Test
    public void onCreate___ChatWindow__button_click_listener() {
        try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
            scenario.onActivity(activity -> {
                activity.findViewById(R.id.startChatButton).performClick();
                Intent startedIntent = shadowOf((Application) ApplicationProvider.getApplicationContext()).getNextStartedActivity();
                assertNotNull("Intent should be started", startedIntent);
                assertEquals(ChatWindow.class.getName(), startedIntent.getComponent().getClassName());
            });
        }
    }

    @Test
    public void onCreate___TestToolbar__button_click_listener() {
        try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
            scenario.onActivity(activity -> {
                activity.findViewById(R.id.testToolbarButton).performClick();
                Intent startedIntent = shadowOf((Application) ApplicationProvider.getApplicationContext()).getNextStartedActivity();
                assertNotNull("Intent should be started", startedIntent);
                assertEquals(TestToolbar.class.getName(), startedIntent.getComponent().getClassName());
            });
        }
    }

    @Test
    public void onCreate__State_restoration_with_non_null_Bundle() {
        try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
            scenario.recreate();
            scenario.onActivity(activity -> {
                assertNotNull("Activity should be recreated", activity);
            });
        }
    }

    @Test
    public void onSupportNavigateUp__Activity_finish_action() {
        try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
            scenario.onActivity(activity -> {
                assertTrue("onSupportNavigateUp should return true", activity.onSupportNavigateUp());
                assertTrue("Activity should be finishing", activity.isFinishing());
            });
        }
    }

    @Test
    public void onActivityResult__Correct_request_and_OK_result_with_data() {
        try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
            scenario.onActivity(activity -> {
                Intent data = new Intent();
                data.putExtra("Response", "Test Message");
                activity.onActivityResult(10, Activity.RESULT_OK, data);
                assertEquals("ListItemsActivity passed: Test Message", ShadowToast.getTextOfLatestToast());
            });
        }
    }

    @Test
    public void onActivityResult__Correct_request_and_OK_result_with_null_data() {
        try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
            scenario.onActivity(activity -> {
                activity.onActivityResult(10, Activity.RESULT_OK, null);
                assertNull("No toast should be shown for null data", ShadowToast.getLatestToast());
            });
        }
    }

    @Test
    public void onActivityResult__Correct_request_and_OK_result_with_data_missing_extra() {
        try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
            scenario.onActivity(activity -> {
                Intent data = new Intent();
                activity.onActivityResult(10, Activity.RESULT_OK, data);
                assertNull("No toast should be shown if extra is missing", ShadowToast.getLatestToast());
            });
        }
    }

    @Test
    public void onActivityResult__Correct_request_with_CANCELED_result() {
        try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
            scenario.onActivity(activity -> {
                activity.onActivityResult(10, Activity.RESULT_CANCELED, new Intent());
                assertNull("No toast should be shown for canceled result", ShadowToast.getLatestToast());
            });
        }
    }

    @Test
    public void onActivityResult__Incorrect_request_code() {
        try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
            scenario.onActivity(activity -> {
                activity.onActivityResult(99, Activity.RESULT_OK, new Intent());
                assertNull("No toast should be shown for incorrect request code", ShadowToast.getLatestToast());
            });
        }
    }

    @Test
    public void lifecycle__onStart_call_verification() {
        try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
            scenario.moveToState(Lifecycle.State.STARTED);
            assertEquals(Lifecycle.State.STARTED, scenario.getState());
        }
    }

    @Test
    public void lifecycle__onResume_call_verification() {
        try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
            scenario.moveToState(Lifecycle.State.RESUMED);
            assertEquals(Lifecycle.State.RESUMED, scenario.getState());
        }
    }

    @Test
    public void lifecycle__onPause_call_verification() {
        try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
            scenario.moveToState(Lifecycle.State.STARTED); // equivalent to onPause
            assertEquals(Lifecycle.State.STARTED, scenario.getState());
        }
    }

    @Test
    public void lifecycle__onStop_call_verification() {
        try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
            scenario.moveToState(Lifecycle.State.CREATED); // equivalent to onStop
            assertEquals(Lifecycle.State.CREATED, scenario.getState());
        }
    }

    @Test
    public void lifecycle__onDestroy_call_verification() {
        try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
            scenario.moveToState(Lifecycle.State.DESTROYED);
            assertEquals(Lifecycle.State.DESTROYED, scenario.getState());
        }
    }

    @Test
    public void lifecycle__Full_sequence_on_activity_launch() {
        try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
            assertEquals(Lifecycle.State.RESUMED, scenario.getState());
        }
    }

    @Test
    public void lifecycle__Full_sequence_on_navigating_away_and_back() {
        try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
            scenario.onActivity(activity -> {
                activity.findViewById(R.id.button).performClick();
            });
            scenario.moveToState(Lifecycle.State.RESUMED);
            assertEquals(Lifecycle.State.RESUMED, scenario.getState());
        }
    }

    @Test
    public void lifecycle__Full_sequence_on_activity_finish() {
        try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
            scenario.onActivity(Activity::finish);
            scenario.moveToState(Lifecycle.State.DESTROYED);
            assertEquals(Lifecycle.State.DESTROYED, scenario.getState());
        }
    }

}