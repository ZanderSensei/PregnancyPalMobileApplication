// src/main/java/com/example/pregnancypalapp2/PartnerLinkActivity.java
package com.example.pregnancypalapp2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.security.SecureRandom;

public class PartnerLinkActivity extends AppCompatActivity {

    private TextView textViewShowLink;
    private Button buttonGenerateLink;
    private DatabaseReference databaseReference;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partner_link);

        textViewShowLink = findViewById(R.id.textViewShowLink);
        buttonGenerateLink = findViewById(R.id.buttonGenerateLink);
        databaseReference = FirebaseDatabase.getInstance().getReference("PartnerLinks");
        auth = FirebaseAuth.getInstance();

        buttonGenerateLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateAndDisplayLink();
            }
        });
    }

    private void generateAndDisplayLink() {
        String uniqueCode = generateUniqueCode(); // Generate a unique 6-character code.
        saveLinkToDatabase(uniqueCode); // Save the generated link to Firebase.
        textViewShowLink.setText(uniqueCode); // Display the generated link in the TextView.
    }

    private void saveLinkToDatabase(String link) {
        if (auth.getCurrentUser() != null) {
            String userId = auth.getCurrentUser().getUid();
            databaseReference.child(link).setValue(userId);
        }
    }

    private String generateUniqueCode() {
        SecureRandom random = new SecureRandom();
        StringBuilder code = new StringBuilder(6);
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

        for (int i = 0; i < 6; i++) {
            code.append(characters.charAt(random.nextInt(characters.length())));
        }

        return code.toString();
    }
}
