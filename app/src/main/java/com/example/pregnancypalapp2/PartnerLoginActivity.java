package com.example.pregnancypalapp2;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PartnerLoginActivity extends AppCompatActivity {

    private EditText editTextPartnerCode;
    private Button buttonLogin;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partner_login);

        editTextPartnerCode = findViewById(R.id.editTextPartnerCode);
        buttonLogin = findViewById(R.id.buttonLoginPartner);
        databaseReference = FirebaseDatabase.getInstance().getReference("PartnerLinks");

        buttonLogin.setOnClickListener(view -> {
            String partnerCode = editTextPartnerCode.getText().toString().trim();
            if (!TextUtils.isEmpty(partnerCode)) {
                authenticatePartner(partnerCode);
            } else {
                Toast.makeText(PartnerLoginActivity.this, "Please enter the partner code", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void authenticatePartner(String partnerCode) {
        databaseReference.child(partnerCode).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String partnerUid = dataSnapshot.getValue(String.class);
                    if (partnerUid != null) {
                        Intent intent = new Intent(PartnerLoginActivity.this, MainActivity.class);
                        intent.putExtra("partnerUid", partnerUid); // Ensure the key is "partnerUid"
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(PartnerLoginActivity.this, "Invalid partner code", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(PartnerLoginActivity.this, "Partner code not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(PartnerLoginActivity.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
