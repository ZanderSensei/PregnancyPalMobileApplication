package com.example.pregnancypalapp2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {

    private EditText emailField;
    private EditText passwordField;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        emailField = findViewById(R.id.signup_email);
        passwordField = findViewById(R.id.signup_password);
        Button signUpButton = findViewById(R.id.signup_button);
        TextView loginLink = findViewById(R.id.link_login);
        TextView partnerSignupLink = findViewById(R.id.link_partner_signup);

        signUpButton.setOnClickListener(view -> registerUser(emailField.getText().toString().trim(), passwordField.getText().toString().trim()));
        loginLink.setOnClickListener(view -> startActivity(new Intent(SignupActivity.this, LoginActivity.class)));
        partnerSignupLink.setOnClickListener(view -> startActivity(new Intent(SignupActivity.this, PartnerLoginActivity.class)));
    }

    private void registerUser(String email, String password) {
        if (!email.isEmpty() && !password.isEmpty()) {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                writeNewUser(user.getUid(), email);
                                updateUI(user);
                            }
                        } else {
                            updateUI(null);
                            // Providing feedback to the user about the failure
                            Toast.makeText(SignupActivity.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        } else {
            Toast.makeText(this, "Email and password fields cannot be empty.", Toast.LENGTH_SHORT).show();
        }
    }

    private void writeNewUser(String userId, String email) {
        User user = new User(email);
        mDatabase.child("users").child(userId).setValue(user);
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            Intent intent = new Intent(SignupActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            // You can provide more feedback here if needed
        }
    }

    static class User {
        public String email;

        public User(String email) {
            this.email = email;
        }
    }
}
