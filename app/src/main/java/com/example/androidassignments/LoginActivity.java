package com.example.androidassignments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LoginActivity extends AppCompatActivity {

    private Button loginButton;
    private EditText emailEditText;
    private EditText passwordEditText;
    private SharedPreferences sharedPreferences;

    private static final String ACTIVITY_NAME="LoginActivity";
    // Constants for SharedPreferences keys
    public static final String SHARED_PREFERENCES_NAME="UserLoginPreferences";
    public static final String EMAIL_KEY="DefaultEmail";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(ACTIVITY_NAME, "onCreate() called");
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        loginButton = findViewById(R.id.loginButton); // Reference to loginButton
        emailEditText = findViewById(R.id.emailEditText); // Reference to the email text field
        passwordEditText = findViewById(R.id.passwordEditText); // Reference to the password text field

        // Initialize sharedPreferences
        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);

        // Load and set the saved email
        String savedEmail = sharedPreferences.getString(EMAIL_KEY, "email@domain.com");
        emailEditText.setText(savedEmail);
        Log.d(ACTIVITY_NAME, "Loaded email: " + savedEmail); // Added a log for debugging. This will be displayed in debug mode, since it's not an info log

        loginButton.setOnClickListener(view -> {
            if (validateInput()) { // Call the validateInput method to check if the email and password is valid
                // method to saved the email that the user typed into sharedPreferences
                saveUserEmail(emailEditText, sharedPreferences);

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    /***
     * Saves the email that the user typed into sharedPreferences
     * @param emailEditText - The reference to the EditText field where the user entered thier email
     * @param sharedPreferences - The SharedPreferences object reference to save the email to
     */
    private void saveUserEmail(EditText emailEditText, SharedPreferences sharedPreferences) {
        String enteredEmail = emailEditText.getText().toString();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(EMAIL_KEY, enteredEmail);
        editor.apply();
        Log.d(ACTIVITY_NAME, "Saved email: " + enteredEmail); // Added a log for debugging. This will be displayed in debug mode, since it's not an info log
    }

    /***
     * Checks if the email and password are valid
     * @return true if the email and password is valid, false otherwise
     */
    private boolean validateInput() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (!isValidEmail(email) || !isValidPassword(password)) {
            return false;
        }

        return true; // All validations passed
    }

    /***
     * Checks if the email is valid
     * @param email - The email to check
     * @return true if the email is valid, false otherwise
     */
    private boolean isValidEmail(String email) {
        if (TextUtils.isEmpty(email)) {
            Log.e(ACTIVITY_NAME, "Email is empty"); // Added a log for logging the empty email error
            emailEditText.setError(getString(R.string.email_required));
            emailEditText.requestFocus();
            Toast.makeText(this, getString(R.string.email_cannot_be_empty), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Log.e(ACTIVITY_NAME, "Invalid email format"); // Added a log for logging the Invalid email format error
            emailEditText.setError(getString(R.string.enter_valid_email));
            emailEditText.requestFocus();
            Toast.makeText(this, getString(R.string.invalid_email_format), Toast.LENGTH_SHORT).show();
            return false;
        }
        emailEditText.setError(null); // Clear error if validation passes
        return true;
    }

    /***
     * Checks if the password is valid
     * @param password - The password to check
     * @return true if the password is valid, false otherwise
     */
    private boolean isValidPassword(String password) {
        if (TextUtils.isEmpty(password)) {
            Log.e(ACTIVITY_NAME, "Password is empty"); // Added a log for logging the empty password error
            passwordEditText.setError(getString(R.string.password_required));
            passwordEditText.requestFocus();
            Toast.makeText(this, getString(R.string.password_cannot_be_empty), Toast.LENGTH_SHORT).show();
            return false;
        }
        passwordEditText.setError(null); // Clear error if validation passes
        return true;
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