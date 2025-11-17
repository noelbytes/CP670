package com.example.androidassignments;

import android.content.Context;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ChatWindow extends AppCompatActivity {

    private static final String ACTIVITY_NAME = "ChatWindow";
    private static final int DELETE_REQUEST_CODE = 1;

    ListView listView;
    EditText textInput;
    Button sendButton;
    ArrayList<String> chatMessages;
    ChatAdapter messageAdapter;
    boolean isTablet;

    // Database fields
    ChatDatabaseHelper dbHelper;
    SQLiteDatabase db;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);

        FrameLayout frameLayout = findViewById(R.id.fragment_container);
        isTablet = (frameLayout != null);

        listView = findViewById(R.id.listView);
        textInput = findViewById(R.id.text_input);
        sendButton = findViewById(R.id.send_button);

        chatMessages = new ArrayList<>();

        dbHelper = new ChatDatabaseHelper(this);
        db = dbHelper.getWritableDatabase();

        loadMessages();

        messageAdapter = new ChatAdapter(this);
        listView.setAdapter(messageAdapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            Bundle bundle = new Bundle();
            bundle.putString("message", chatMessages.get(position));
            bundle.putLong("id", id);

            if (isTablet) {
                MessageFragment fragment = new MessageFragment(this); // Pass activity reference
                fragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .commit();
            } else {
                Intent intent = new Intent(ChatWindow.this, MessageDetails.class);
                intent.putExtras(bundle);
                startActivityForResult(intent, DELETE_REQUEST_CODE);
            }
        });

        sendButton.setOnClickListener(v -> {
            String message = textInput.getText().toString().trim();
            if (!message.isEmpty()) {
                ContentValues cv = new ContentValues();
                cv.put(ChatDatabaseHelper.KEY_MESSAGE, message);
                db.insert(ChatDatabaseHelper.TABLE_NAME, null, cv);
                textInput.setText("");
                loadMessages(); // Reload and refresh
                messageAdapter.notifyDataSetChanged();
            }
        });
    }

    public void deleteMessageById(long id) {
        db.delete(ChatDatabaseHelper.TABLE_NAME, ChatDatabaseHelper.KEY_ID + "=?", new String[]{Long.toString(id)});
        loadMessages();
        messageAdapter.notifyDataSetChanged();
    }

    private void loadMessages() {
        if (cursor != null) {
            cursor.close();
        }
        cursor = db.rawQuery("SELECT * FROM " + ChatDatabaseHelper.TABLE_NAME, null);
        chatMessages.clear();
        if (cursor.moveToFirst()) {
            int msgColIndex = cursor.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE);
            if (msgColIndex != -1) {
                do {
                    chatMessages.add(cursor.getString(msgColIndex));
                } while (cursor.moveToNext());
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == DELETE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            long idToDelete = data.getLongExtra("delete_id", -1);
            if (idToDelete != -1) {
                deleteMessageById(idToDelete);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cursor != null) {
            cursor.close();
        }
        if (db != null) {
            db.close();
        }
        if (dbHelper != null) {
            dbHelper.close();
        }
    }

    public class ChatAdapter extends ArrayAdapter<String> {
        public ChatAdapter(Context ctx) {
            super(ctx, 0, chatMessages);
        }
        
        @Override
        public long getItemId(int position) {
            if (cursor != null && cursor.moveToPosition(position)) {
                int idColIndex = cursor.getColumnIndex(ChatDatabaseHelper.KEY_ID);
                if (idColIndex != -1) {
                    return cursor.getLong(idColIndex);
                }
            }
            return -1;
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
