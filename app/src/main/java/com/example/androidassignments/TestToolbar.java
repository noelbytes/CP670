package com.example.androidassignments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;

import com.example.androidassignments.databinding.ActivityTestToolbarBinding;

public class TestToolbar extends AppCompatActivity {

    private static final String ACTIVITY_NAME = "TestToolbar";
    private ActivityTestToolbarBinding binding;
    private String customMessage = "You selected item 1"; // Default message

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(ACTIVITY_NAME, "onCreate started");

        try {
            binding = ActivityTestToolbarBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());

            if (binding.toolbar != null) {
                setSupportActionBar(binding.toolbar);
                Log.d(ACTIVITY_NAME, "Toolbar set successfully");
            } else {
                Log.e(ACTIVITY_NAME, "Toolbar is null!");
            }

            if (binding.fab != null) {
                binding.fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Snackbar.make(view, getString(R.string.welcome_testtoolbar), Snackbar.LENGTH_LONG)
                                .setAnchorView(R.id.fab)
                                .setAction("Action", null).show();
                    }
                });
            } else {
                Log.e(ACTIVITY_NAME, "FAB is null!");
            }

            Log.d(ACTIVITY_NAME, "onCreate completed successfully");
        } catch (Exception e) {
            Log.e(ACTIVITY_NAME, "Error in onCreate: " + e.getMessage(), e);
            Toast.makeText(this, "Error starting TestToolbar: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int id = menuItem.getItemId();

        if (id == R.id.action_one) {
            Log.d("Toolbar", "Option 1 selected");
            Snackbar.make(findViewById(android.R.id.content), customMessage, Snackbar.LENGTH_SHORT).show();
        } else if (id == R.id.action_two) {
            Log.d("Toolbar", "Option 2 selected");
            // Create AlertDialog
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.dialog_go_back_title);

            // Add the buttons
            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User clicked OK button - finish the activity
                    finish();
                }
            });

            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User cancelled the dialog - do nothing
                    Log.d("Toolbar", "User cancelled the dialog");
                }
            });

            // Create and show the AlertDialog
            AlertDialog dialog = builder.create();
            dialog.show();
        } else if (id == R.id.action_three) {
            Log.d("Toolbar", "Option 3 selected");
            // Inflate the custom layout/view
            LayoutInflater inflater = this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.dialog_custom_message, null);

            // Create the AlertDialog.Builder
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(dialogView);

            // Find the EditText in the custom layout
            final EditText editText = dialogView.findViewById(R.id.new_message);

            // Add the buttons
            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User clicked OK button - get the custom message
                    String newMessage = editText.getText().toString();
                    if (!newMessage.isEmpty()) {
                        customMessage = newMessage;
                        Snackbar.make(findViewById(android.R.id.content), "Message updated!", Snackbar.LENGTH_SHORT).show();
                    }
                }
            });

            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User cancelled the dialog - do nothing
                    Log.d("Toolbar", "User cancelled custom message dialog");
                }
            });

            // Create and show the AlertDialog
            AlertDialog dialog = builder.create();
            dialog.show();
        } else if (id == R.id.action_about) {
            Log.d("Toolbar", "About selected");
            Toast.makeText(this, "Version 1.0, by Allan Noel D'Souza", Toast.LENGTH_SHORT).show();
        }
        return true;
    }
}