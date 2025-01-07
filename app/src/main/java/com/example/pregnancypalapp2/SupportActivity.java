package com.example.pregnancypalapp2;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SupportActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);

        EditText nameEditText = findViewById(R.id.nameEditText);
        EditText emailEditText = findViewById(R.id.emailEditText);
        EditText messageEditText = findViewById(R.id.messageEditText);
        Button sendButton = findViewById(R.id.sendButton);

        // Initialize Firebase Database
        databaseReference = FirebaseDatabase.getInstance().getReference("SupportMessages");

        sendButton.setOnClickListener(v -> {
            String name = nameEditText.getText().toString().trim();
            String email = emailEditText.getText().toString().trim();
            String message = messageEditText.getText().toString().trim();

            if (!name.isEmpty() && !email.isEmpty() && !message.isEmpty()) {
                sendSupportMessage(name, email, message);
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendSupportMessage(String name, String email, String message) {
        // Generate a unique key for each message
        String key = databaseReference.push().getKey();

        if (key != null) {
            SupportMessage supportMessage = new SupportMessage(name, email, message);
            databaseReference.child(key).setValue(supportMessage)
                    .addOnSuccessListener(aVoid -> Toast.makeText(SupportActivity.this, "Message sent successfully!", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(SupportActivity.this, "Failed to send message: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        }
    }

    // Define a SupportMessage class to structure the data for Firebase
    public static class SupportMessage {
        public String name;
        public String email;
        public String message;

        public SupportMessage() {
            // Default constructor required for DataSnapshot.getValue(SupportMessage.class)
        }

        public SupportMessage(String name, String email, String message) {
            this.name = name;
            this.email = email;
            this.message = message;
        }
    }
}
