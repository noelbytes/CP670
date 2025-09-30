package com.example.androidassignments;

import android.app.Activity;
import android.app.ComponentCaller;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private static final String ACTIVITY_NAME="MainActivity";
    private static final int LIST_ITEMS_REQUEST_CODE = 10; // Request code

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(ACTIVITY_NAME, "onCreate() called");
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button button = findViewById(R.id.button);

        button.setOnClickListener(view -> {
           Intent intent = new Intent(MainActivity.this, ListItemsActivity.class);
           startActivityForResult(intent, LIST_ITEMS_REQUEST_CODE);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data, @NonNull ComponentCaller caller) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LIST_ITEMS_REQUEST_CODE) {
            Log.i(ACTIVITY_NAME, "Returned to MainActivity.onActivityResult");
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    String messagePassed = data.getStringExtra("Response");
                    if (messagePassed != null) {
                        String text = "ListItemsActivity passed: " + messagePassed;

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

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        Log.i(ACTIVITY_NAME, "onSaveInstanceState() called");
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        Log.i(ACTIVITY_NAME, "onRestoreInstanceState() called");
        super.onRestoreInstanceState(savedInstanceState);
    }
}