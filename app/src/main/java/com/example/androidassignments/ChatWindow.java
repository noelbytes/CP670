package com.example.androidassignments;

import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ChatWindow extends AppCompatActivity {

    private static final String ACTIVITY_NAME = "ChatWindow";

    ListView listView;
    EditText textInput;
    Button sendButton;
    ArrayList<String> chatMessages;
    ChatAdapter messageAdapter;

    // Database fields
    ChatDatabaseHelper dbHelper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);

        listView = findViewById(R.id.listView);
        textInput = findViewById(R.id.text_input);
        sendButton = findViewById(R.id.send_button);
        chatMessages = new ArrayList<>();

        // Open the database using a temporary helper and keep references
        dbHelper = new ChatDatabaseHelper(this);
        db = dbHelper.getWritableDatabase();

        // Query existing messages using try-with-resources
        try (Cursor cursor = db.rawQuery("SELECT * FROM " + ChatDatabaseHelper.TABLE_NAME, null)) {

            Log.i(ACTIVITY_NAME, "Cursor's column count = " + cursor.getColumnCount());
            // Print each column name
            for (int i = 0; i < cursor.getColumnCount(); i++) {
                Log.i(ACTIVITY_NAME, "Column " + i + " name = " + cursor.getColumnName(i));
            }

            if (cursor.moveToFirst()) {
                int msgColIndex = cursor.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE);
                if (msgColIndex >= 0) {
                    do {
                        String msg = cursor.getString(msgColIndex);
                        Log.i(ACTIVITY_NAME, "SQL MESSAGE: " + msg);
                        if (msg != null) {
                            chatMessages.add(msg);
                        }
                    } while (cursor.moveToNext());
                } else {
                    Log.i(ACTIVITY_NAME, "Message column not found in cursor");
                }
            } else {
                Log.i(ACTIVITY_NAME, "Cursor returned no rows");
            }
        } catch (Exception e) {
            Log.e(ACTIVITY_NAME, "Error querying messages: " + e.getMessage());
        }

        // Set up adapter after loading messages
        messageAdapter = new ChatAdapter(this);
        listView.setAdapter(messageAdapter);

        sendButton.setOnClickListener(v -> {
            String message = textInput.getText().toString().trim();
            if (!message.isEmpty()) {
                // Add to in-memory list and update UI
                chatMessages.add(message);
                messageAdapter.notifyDataSetChanged();
                textInput.setText("");

                // Also insert the message into the SQLite database
                if (db != null && db.isOpen()) {
                    try {
                        ContentValues cv = new ContentValues();
                        cv.put(ChatDatabaseHelper.KEY_MESSAGE, message);
                        long newRowId = db.insert(ChatDatabaseHelper.TABLE_NAME, null, cv);
                        Log.i(ACTIVITY_NAME, "Inserted message row id: " + newRowId);
                    } catch (Exception e) {
                        Log.e(ACTIVITY_NAME, "Error inserting message: " + e.getMessage());
                    }
                } else {
                    Log.w(ACTIVITY_NAME, "Database is not open; message not saved to DB");
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (db != null && db.isOpen()) {
            db.close();
            db = null;
        }
        if (dbHelper != null) {
            // SQLiteOpenHelper does not have an explicit close for helper, but close() will close DB
            dbHelper.close();
            dbHelper = null;
        }
    }

    public class ChatAdapter extends ArrayAdapter<String> {
        public ChatAdapter(Context ctx) {
            super(ctx, 0);
        }

        @Override
        public int getCount() {
            return chatMessages.size();
        }

        @Override
        public String getItem(int position) {
            return chatMessages.get(position);
        }

        @Override
        @NonNull
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = ChatWindow.this.getLayoutInflater();
            View result;
            if (position % 2 == 0) {
                result = inflater.inflate(R.layout.chat_row_incoming, parent, false);
            } else {
                result = inflater.inflate(R.layout.chat_row_outgoing, parent, false);
            }

            TextView message = result.findViewById(R.id.message_text);
            message.setText(getItem(position));
            return result;
        }
    }
}
