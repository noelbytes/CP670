package com.example.androidassignments;

import static org.junit.Assert.*;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.test.core.app.ApplicationProvider;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 28)
public class ChatDatabaseHelperTest {
    private Context context;
    private ChatDatabaseHelper helper;
    private SQLiteDatabase db;

    @Before
    public void setUp() {
        context = ApplicationProvider.getApplicationContext();
        helper = new ChatDatabaseHelper(context);
        db = helper.getWritableDatabase();
    }

    @After
    public void tearDown() {
        if (db != null && db.isOpen()) db.close();
        if (helper != null) helper.close();
    }

    @Test
    public void testInsertAndQueryMessage() {
        ContentValues cv = new ContentValues();
        cv.put(ChatDatabaseHelper.KEY_MESSAGE, "unit test message");
        long rowId = db.insert(ChatDatabaseHelper.TABLE_NAME, null, cv);
        assertTrue("Insert should return valid row id", rowId != -1);

        Cursor cursor = db.rawQuery("SELECT * FROM " + ChatDatabaseHelper.TABLE_NAME + " WHERE " + ChatDatabaseHelper.KEY_MESSAGE + "=?",
                new String[]{"unit test message"});
        try {
            assertNotNull(cursor);
            assertTrue(cursor.getCount() > 0);
            cursor.moveToFirst();
            int msgIdx = cursor.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE);
            assertTrue(msgIdx >= 0);
            String msg = cursor.getString(msgIdx);
            assertEquals("unit test message", msg);
        } finally {
            cursor.close();
        }
    }
}

