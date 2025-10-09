package com.example.androidassignments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final String ACTIVITY_NAME="MainActivity";
    private static final int LIST_ITEMS_REQUEST_CODE = 10; // Request code

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(ACTIVITY_NAME, "onCreate() called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Enable the up button in the action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Button button = findViewById(R.id.button);
        Button startChatButton = findViewById(R.id.startChatButton);
        
        startChatButton.setOnClickListener(v -> {
            Log.i("MainActivity", "User clicked Start Chat");
            Intent intent = new Intent(MainActivity.this, ChatWindow.class);
            startActivity(intent);
        });

        button.setOnClickListener(view -> {
           Intent intent = new Intent(MainActivity.this, ListItemsActivity.class);
           startActivityForResult(intent, LIST_ITEMS_REQUEST_CODE);
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LIST_ITEMS_REQUEST_CODE) {
            Log.i(ACTIVITY_NAME, "Returned to MainActivity.onActivityResult");
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    String messagePassed = data.getStringExtra("Response");
                    if (messagePassed != null) {
                        String text = getString(R.string.list_items_passed, messagePassed);

                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(this, text, duration);
                        toast.show();
                    }
                }
            }
        }
    }

    @Override
    protected void onResume() {
        Log.i(ACTIVITY_NAME, "onResume() called");
        super.onResume();
    }

    @Override
    protected void onStart() {
        Log.i(ACTIVITY_NAME, "onStart() called");
        super.onStart();
    }

    @Override
    protected void onPause() {
        Log.i(ACTIVITY_NAME, "onPause() called");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.i(ACTIVITY_NAME, "onStop() called");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.i(ACTIVITY_NAME, "onDestroy() called");
        super.onDestroy();
    }
}