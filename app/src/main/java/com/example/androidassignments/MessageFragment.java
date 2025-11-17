package com.example.androidassignments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class MessageFragment extends Fragment {

    private ChatWindow parentActivity;

    // Default constructor is required by the framework
    public MessageFragment() {}

    // Custom constructor to receive the parent activity on tablets
    public MessageFragment(ChatWindow parent) {
        this.parentActivity = parent;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_message_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            String message = args.getString("message", "");
            long id = args.getLong("id", -1);

            TextView messageTextView = view.findViewById(R.id.message_textview);
            TextView idTextView = view.findViewById(R.id.id_textview);
            Button deleteButton = view.findViewById(R.id.delete_button);

            messageTextView.setText("Message: " + message);
            idTextView.setText("ID: " + id);

            deleteButton.setOnClickListener(v -> {
                // If parentActivity is not null, we are on a tablet
                if (parentActivity != null) {
                    parentActivity.deleteMessageById(id);
                    // Remove fragment from the screen
                    parentActivity.getSupportFragmentManager().beginTransaction().remove(this).commit();
                } else { // We are on a phone
                    if (getActivity() != null) {
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("delete_id", id);
                        getActivity().setResult(Activity.RESULT_OK, resultIntent);
                        getActivity().finish();
                    }
                }
            });
        }
    }
}
