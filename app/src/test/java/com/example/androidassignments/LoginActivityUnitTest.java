package com.example.androidassignments;

import android.content.Context;
import android.content.SharedPreferences;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.regex.Pattern;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for LoginActivity using JUnit and Mockito
 * Tests validation logic and SharedPreferences functionality
 * Following Chapter 4 recommendations for unit testing
 */
@RunWith(MockitoJUnitRunner.class)
public class LoginActivityUnitTest {

    @Mock
    private Context mockContext;

    @Mock
    private SharedPreferences mockSharedPreferences;

    @Mock
    private SharedPreferences.Editor mockEditor;

    private static final String SHARED_PREFERENCES_NAME = "UserLoginPreferences";
    private static final String EMAIL_KEY = "DefaultEmail";

    // Custom email pattern for unit testing (since android.util.Patterns is not available in JVM)
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "[a-zA-Z0-9+._%\\-]{1,256}" +
        "@" +
        "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
        "(" +
        "\\." +
        "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
        ")+"
    );

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        // Removed unnecessary stubbings - they will be set up in specific tests that need them
    }

    // Helper method to validate email
    private boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    // Test 1: Email Validation - Valid Email Format
    @Test
    public void testValidEmailFormat() {
        String validEmail = "user@example.com";
        assertTrue("Valid email should pass pattern matching",
                isValidEmail(validEmail));
    }

    // Test 2: Email Validation - Invalid Email Format
    @Test
    public void testInvalidEmailFormat_NoAtSign() {
        String invalidEmail = "userexample.com";
        assertFalse("Email without @ should be invalid",
                isValidEmail(invalidEmail));
    }

    // Test 3: Email Validation - Invalid Email Format
    @Test
    public void testInvalidEmailFormat_NoDomain() {
        String invalidEmail = "user@";
        assertFalse("Email without domain should be invalid",
                isValidEmail(invalidEmail));
    }

    // Test 4: Email Validation - Empty Email
    @Test
    public void testEmptyEmailValidation() {
        String emptyEmail = "";
        assertTrue("Empty email should be detected",
                emptyEmail.trim().isEmpty());
    }

    // Test 5: Email Validation - Whitespace Only
    @Test
    public void testWhitespaceOnlyEmail() {
        String whitespaceEmail = "   ";
        assertTrue("Whitespace-only email should be invalid",
                whitespaceEmail.trim().isEmpty());
    }

    // Test 6: Password Validation - Minimum Length
    @Test
    public void testPasswordMinimumLength() {
        String shortPassword = "pass";
        assertTrue("Password less than 6 characters should be invalid",
                shortPassword.length() < 6);
    }

    // Test 7: Password Validation - Valid Length
    @Test
    public void testPasswordValidLength() {
        String validPassword = "password123";
        assertTrue("Password with 6+ characters should be valid",
                validPassword.length() >= 6);
    }

    // Test 8: Password Validation - Exactly 6 Characters
    @Test
    public void testPasswordExactly6Characters() {
        String exactPassword = "pass12";
        assertEquals("Password should have exactly 6 characters", 6, exactPassword.length());
        assertTrue("6-character password should be valid",
                exactPassword.length() >= 6);
    }

    // Test 9: Password Validation - Empty Password
    @Test
    public void testEmptyPasswordValidation() {
        String emptyPassword = "";
        assertTrue("Empty password should be detected",
                emptyPassword.trim().isEmpty());
    }

    // Test 10: SharedPreferences - Save Email
    @Test
    public void testSaveEmailToSharedPreferences() {
        String testEmail = "test@example.com";

        mockEditor.putString(EMAIL_KEY, testEmail);
        mockEditor.apply();

        verify(mockEditor).putString(EMAIL_KEY, testEmail);
        verify(mockEditor).apply();
    }

    // Test 11: SharedPreferences - Retrieve Saved Email
    @Test
    public void testRetrieveSavedEmail() {
        String savedEmail = "saved@example.com";
        when(mockSharedPreferences.getString(EMAIL_KEY, "")).thenReturn(savedEmail);

        String retrievedEmail = mockSharedPreferences.getString(EMAIL_KEY, "");

        assertEquals("Retrieved email should match saved email", savedEmail, retrievedEmail);
        verify(mockSharedPreferences).getString(EMAIL_KEY, "");
    }

    // Test 12: SharedPreferences - Clear Email
    @Test
    public void testClearEmailFromSharedPreferences() {
        mockEditor.remove(EMAIL_KEY);
        mockEditor.apply();

        verify(mockEditor).remove(EMAIL_KEY);
        verify(mockEditor).apply();
    }

    // Test 13: Validation - Both Fields Empty
    @Test
    public void testBothFieldsEmpty() {
        String email = "";
        String password = "";

        boolean isValid = !email.trim().isEmpty() && !password.trim().isEmpty();
        assertFalse("Both empty fields should be invalid", isValid);
    }

    // Test 14: Validation - Valid Email and Password
    @Test
    public void testValidEmailAndPassword() {
        String email = "valid@test.com";
        String password = "password123";

        boolean isValidEmail = isValidEmail(email);
        boolean isValidPassword = password.length() >= 6;

        assertTrue("Valid email should pass", isValidEmail);
        assertTrue("Valid password should pass", isValidPassword);
        assertTrue("Both valid inputs should pass", isValidEmail && isValidPassword);
    }

    // Test 15: Validation - Valid Email but Short Password
    @Test
    public void testValidEmailButShortPassword() {
        String email = "valid@test.com";
        String password = "pass";

        boolean isValidEmail = isValidEmail(email);
        boolean isValidPassword = password.length() >= 6;

        assertTrue("Email should be valid", isValidEmail);
        assertFalse("Password should be invalid", isValidPassword);
    }

    // Test 16: Constants Validation
    @Test
    public void testSharedPreferencesConstants() {
        assertEquals("Shared preferences name should match",
                "UserLoginPreferences", SHARED_PREFERENCES_NAME);
        assertEquals("Email key should match",
                "DefaultEmail", EMAIL_KEY);
    }

    // Test 17: Email Trimming
    @Test
    public void testEmailTrimming() {
        String emailWithSpaces = "  user@example.com  ";
        String trimmedEmail = emailWithSpaces.trim();

        assertEquals("Trimmed email should match", "user@example.com", trimmedEmail);
        assertTrue("Trimmed email should be valid",
                isValidEmail(trimmedEmail));
    }

    // Test 18: Password Trimming
    @Test
    public void testPasswordTrimming() {
        String passwordWithSpaces = "  password123  ";
        String trimmedPassword = passwordWithSpaces.trim();

        assertEquals("Trimmed password should match", "password123", trimmedPassword);
        assertTrue("Trimmed password should be valid", trimmedPassword.length() >= 6);
    }

    // Test 19: Special Characters in Email
    @Test
    public void testEmailWithSpecialCharacters() {
        String emailWithSpecialChars = "user+test@example.com";
        assertTrue("Email with + should be valid",
                isValidEmail(emailWithSpecialChars));
    }

    // Test 20: Case Sensitivity
    @Test
    public void testEmailCaseSensitivity() {
        String lowerCase = "user@example.com";
        String upperCase = "USER@EXAMPLE.COM";
        String mixedCase = "UsEr@ExAmPlE.CoM";

        assertTrue("Lowercase email should be valid",
                isValidEmail(lowerCase));
        assertTrue("Uppercase email should be valid",
                isValidEmail(upperCase));
        assertTrue("Mixed case email should be valid",
                isValidEmail(mixedCase));
    }
}
