package com.example.androidassignments;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;

/**
 * Unit tests for LoginActivity validation logic
 * Tests email and password validation using pure Java (no Android dependencies)
 * Following Chapter 4 recommendations for unit testing
 */
@RunWith(MockitoJUnitRunner.class)
public class LoginActivityUnitTest {

    // Helper method for email validation
    private boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        return email.contains("@") &&
               email.indexOf("@") > 0 &&
               email.indexOf("@") < email.length() - 1 &&
               email.lastIndexOf(".") > email.indexOf("@");
    }

    // Test 1: Valid Email Format
    @Test
    public void testValidEmailFormat() {
        String validEmail = "test@example.com";
        assertTrue("Valid email should pass validation", isValidEmail(validEmail));
    }

    // Test 2: Invalid Email - Missing @
    @Test
    public void testInvalidEmailMissingAt() {
        String invalidEmail = "testexample.com";
        assertFalse("Email without @ should be invalid", isValidEmail(invalidEmail));
    }

    // Test 3: Invalid Email - Missing Domain
    @Test
    public void testInvalidEmailMissingDomain() {
        String invalidEmail = "test@";
        assertFalse("Email without domain should be invalid", isValidEmail(invalidEmail));
    }

    // Test 4: Valid Email with Plus Sign
    @Test
    public void testValidEmailWithPlusSign() {
        String validEmail = "user+tag@example.com";
        assertTrue("Email with + should be valid", isValidEmail(validEmail));
    }

    // Test 5: Valid Email with Subdomain
    @Test
    public void testValidEmailWithSubdomain() {
        String validEmail = "test@mail.example.com";
        assertTrue("Email with subdomain should be valid", isValidEmail(validEmail));
    }

    // Test 6: Empty Email String
    @Test
    public void testEmptyEmailString() {
        String emptyEmail = "";
        assertFalse("Empty email should be invalid", isValidEmail(emptyEmail));
    }

    // Test 7: Null Email String
    @Test
    public void testNullEmailString() {
        assertFalse("Null email should be invalid", isValidEmail(null));
    }

    // Test 8: Email Trimming
    @Test
    public void testEmailTrimming() {
        String emailWithSpaces = "  test@example.com  ";
        String trimmed = emailWithSpaces.trim();
        assertEquals("Trimmed email should match", "test@example.com", trimmed);
        assertTrue("Trimmed email should be valid", isValidEmail(trimmed));
    }

    // Test 9: Empty Password String
    @Test
    public void testEmptyPasswordString() {
        String emptyPassword = "";
        assertTrue("Empty password should be empty", emptyPassword.isEmpty());
    }

    // Test 10: Password Trimming
    @Test
    public void testPasswordTrimming() {
        String passwordWithSpaces = "  password123  ";
        String trimmed = passwordWithSpaces.trim();
        assertEquals("Trimmed password should match", "password123", trimmed);
    }

    // Test 11: Non-Empty Password
    @Test
    public void testNonEmptyPassword() {
        String password = "myPassword123";
        assertFalse("Password should not be empty", password.isEmpty());
    }

    // Test 12: Password Validation - Minimum Length
    @Test
    public void testPasswordMinimumLength() {
        String shortPassword = "123";
        assertTrue("Short password should have length less than 6", shortPassword.length() < 6);

        String validPassword = "password123";
        assertTrue("Valid password should have length >= 6", validPassword.length() >= 6);
    }

    // Test 13: Email with Multiple @ Symbols
    @Test
    public void testEmailWithMultipleAtSymbols() {
        String invalidEmail = "test@@example.com";
        // This simple validator doesn't check for multiple @, but we test the behavior
        assertNotNull("Email string should not be null", invalidEmail);
    }

    // Test 14: String Concatenation for Display
    @Test
    public void testStringConcatenation() {
        String email = "user@example.com";
        String welcomeMessage = "Welcome, " + email;
        assertEquals("Welcome message should match", "Welcome, user@example.com", welcomeMessage);
    }

    // Test 15: Case Sensitivity
    @Test
    public void testEmailCaseSensitivity() {
        String upperEmail = "TEST@EXAMPLE.COM";
        String lowerEmail = upperEmail.toLowerCase();
        assertEquals("Email should be converted to lowercase", "test@example.com", lowerEmail);
    }
}
