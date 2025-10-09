package com.example.androidassignments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ComponentCaller;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ListItemsActivity extends AppCompatActivity {

    private static final String ACTIVITY_NAME="ListItemsActivity";
    private static final int REQUEST_IMAGE_CAPTURE = 1; // Request code for camera
    private ImageButton imageButton;
    private Switch switchToggle;
    private CheckBox checkBox;
    private int duration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(ACTIVITY_NAME, "onCreate() called");
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_list_items);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Enable the up button in the action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        imageButton = findViewById(R.id.imageButton); // Store a reference to imageButton
        switchToggle = findViewById(R.id.switchToggle); // Store a reference to switchToggle
        checkBox = findViewById(R.id.checkBox); // Store a reference to checkBox

        imageButton.setOnClickListener(view -> {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            // Checks to ensure that there's a camera activity to handle the intent
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            } else {
                Log.e(ACTIVITY_NAME, "No camera activity found!");
                // Show a toast or dialog to the user
                Toast toast = Toast.makeText(this, getString(R.string.no_camera_activity), Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        switchToggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
            String text;

            if (isChecked) {
                text = getString(R.string.switch_on);
                duration = Toast.LENGTH_SHORT;
            } else {
                text = getString(R.string.switch_off);
                duration = Toast.LENGTH_LONG;
            }

            print(text); // Display the toast object with the relevant message
        });

        // Set onCheckedChangedListener for the Checkbox
        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) { // Show the dialog box if the checkbox is checked
                AlertDialog.Builder builder = new AlertDialog.Builder(ListItemsActivity.this);
                builder.setMessage(R.string.dialog_message)
                        .setTitle(R.string.dialog_title)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // User clicked the OK button
                                Log.d(ACTIVITY_NAME, "User clicked the OK button on the dialog box. Finishing activity");
                                Intent resultIntent = new Intent();
                                resultIntent.putExtra("Response", getString(R.string.my_information_to_share));
                                setResult(Activity.RESULT_OK, resultIntent);
                                finish(); // Finish the current activity and return to the MainActivity
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // User cancelled the dialog
                                Log.d(ACTIVITY_NAME, "User clicked the cancel button on the dialog box");
                                // Uncheck checkbox since the action was cancelled
                                checkBox.setChecked(false);
                            }
                        })
                        .setOnCancelListener(dialog -> {
                            // Handle if the dialog is cancelled by the back button or touching outside
                            Log.d(ACTIVITY_NAME, "Dialog was cancelled. User clicked on the back button or touched outside the dialog");
                            // Uncheck checkbox since the action was cancelled
                            checkBox.setChecked(false);
                        })
                        .show();
            }
        });
    }

    /***
     * Method to display a Toast message
     * @param message - The message to display
     */
    public void print(String message) {
        Log.i(ACTIVITY_NAME, "Inside the print method");
        Toast toast = Toast.makeText(this, message, duration);
        toast.show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data, @NonNull ComponentCaller caller) {
        super.onActivityResult(requestCode, resultCode, data, caller);
        Log.i(ACTIVITY_NAME, "onActivityResult() called. Request code: " + requestCode + ", Result code: " + resultCode);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            if (data != null && data.getExtras() != null) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                if (imageBitmap != null) {
                    imageButton.setImageBitmap(imageBitmap);
                    Log.i(ACTIVITY_NAME, "Image set to ImageButton");
                } else {
                    Log.e(ACTIVITY_NAME, "Bitmap data is null");
                }
            } else {
                Log.e(ACTIVITY_NAME, "Intent data or extras are null");
            }
        } else {
            Log.e(ACTIVITY_NAME, "Image capture failed or was cancelled. Result code: " + resultCode);
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