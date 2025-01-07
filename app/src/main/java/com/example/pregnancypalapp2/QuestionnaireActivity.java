package com.example.pregnancypalapp2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class QuestionnaireActivity extends AppCompatActivity {

    private EditText fullName, emailAddress, dateOfBirth, expectedDueDate, numberOfPregnancies, previousComplications,
            currentHealthConditions, medications, allergies, exerciseHabits, supportSystem, informationPreferences,
            pregnancyGoals, majorConcerns, learningPreferences, interestedFeatures;
    private Spinner currentWeekSpinner, dietaryPreferences;
    private RadioGroup genderPreference;
    private CheckBox interestedInClasses;
    private SeekBar stressLevel;
    private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionnaire);

        initializeViews();
        checkIfCompleted();
        setupSubmitButton();
    }

    private void initializeViews() {
        fullName = findViewById(R.id.fullName);
        emailAddress = findViewById(R.id.emailAddress);
        dateOfBirth = findViewById(R.id.dateOfBirth);
        expectedDueDate = findViewById(R.id.expectedDueDate);
        numberOfPregnancies = findViewById(R.id.numberOfPregnancies);
        previousComplications = findViewById(R.id.previousComplications);
        currentHealthConditions = findViewById(R.id.currentHealthConditions);
        medications = findViewById(R.id.medications);
        allergies = findViewById(R.id.allergies);
        dietaryPreferences = findViewById(R.id.dietaryPreferences);
        exerciseHabits = findViewById(R.id.exerciseHabits);
        supportSystem = findViewById(R.id.supportSystem);
        stressLevel = findViewById(R.id.stressLevel);
        informationPreferences = findViewById(R.id.informationPreferences);
        pregnancyGoals = findViewById(R.id.pregnancyGoals);
        majorConcerns = findViewById(R.id.majorConcerns);
        learningPreferences = findViewById(R.id.learningPreferences);
        genderPreference = findViewById(R.id.genderPreference);
        interestedInClasses = findViewById(R.id.interestedInClasses);
        interestedFeatures = findViewById(R.id.interestedFeatures);
        currentWeekSpinner = findViewById(R.id.currentWeekSpinner);
        submitButton = findViewById(R.id.submitQuestionnaire); // Make sure this ID matches the Button ID in your XML layout
    }

    private void checkIfCompleted() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Use the email address as the key, but Firebase keys cannot contain '.', so replace it
            String key = user.getEmail().replace(".", ",");
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("UserResponses").child(key);

            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // User has already completed the questionnaire
                        startActivity(new Intent(QuestionnaireActivity.this, MainActivity.class));
                        finish(); // Close the QuestionnaireActivity
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(QuestionnaireActivity.this, "Failed to check previous data: " + databaseError.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }


    private void setupSubmitButton() {
        if (submitButton != null) {
            submitButton.setOnClickListener(view -> {
                if (validateInputs()) {
                    submitData();
                } else {
                    Toast.makeText(this, "Please fill all required fields correctly.", Toast.LENGTH_LONG).show();
                }
            });
        } else {
            Toast.makeText(this, "Submit button is not initialized", Toast.LENGTH_LONG).show();
        }
    }

    private boolean validateInputs() {
        // Validate input fields to ensure they're not empty
        return true; // Simplified for brevity
    }

    private void submitData() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            // Use the email address as the key, replace '.' with ',' because Firebase keys can't contain '.'
            String emailKey = user.getEmail().replace(".", ",");
            DatabaseReference myRef = database.getReference("UserResponses").child(emailKey);

            try {
                // Use safe getters to handle potentially null values from spinners and radio groups
                String dietaryPreference = dietaryPreferences.getSelectedItem() != null ? dietaryPreferences.getSelectedItem().toString() : "";
                String genderPreferenceText = genderPreference.getCheckedRadioButtonId() != -1 ? ((RadioButton)findViewById(genderPreference.getCheckedRadioButtonId())).getText().toString() : "";

                UserResponse response = new UserResponse(
                        fullName.getText().toString().trim(),
                        emailAddress.getText().toString().trim(),
                        dateOfBirth.getText().toString().trim(),
                        expectedDueDate.getText().toString().trim(),
                        Integer.parseInt(numberOfPregnancies.getText().toString().trim()),
                        previousComplications.getText().toString().trim(),
                        currentHealthConditions.getText().toString().trim(),
                        medications.getText().toString().trim(),
                        allergies.getText().toString().trim(),
                        dietaryPreference,
                        exerciseHabits.getText().toString().trim(),
                        supportSystem.getText().toString().trim(),
                        stressLevel.getProgress(),
                        informationPreferences.getText().toString().trim(),
                        pregnancyGoals.getText().toString().trim(),
                        majorConcerns.getText().toString().trim(),
                        learningPreferences.getText().toString().trim(),
                        genderPreferenceText,
                        interestedInClasses.isChecked(),
                        interestedFeatures.getText().toString().trim()
                );

                myRef.setValue(response) // Using setValue to directly set the user's responses.
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(QuestionnaireActivity.this, "Data submitted successfully!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(QuestionnaireActivity.this, MainActivity.class));
                            finish(); // Close the QuestionnaireActivity
                        })
                        .addOnFailureListener(e -> Toast.makeText(QuestionnaireActivity.this, "Failed to submit data: " + e.getMessage(), Toast.LENGTH_LONG).show());
            } catch (Exception e) {
                Toast.makeText(this, "Error processing input: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "User is not logged in", Toast.LENGTH_SHORT).show();
        }
    }




}
