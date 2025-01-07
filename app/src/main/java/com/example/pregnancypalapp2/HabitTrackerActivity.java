package com.example.pregnancypalapp2;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class HabitTrackerActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private RadioGroup radioGroupWaterIntake, radioGroupSleepHours;
    private CheckBox checkBoxBreakfast, checkBoxLunch, checkBoxDinner, checkBoxExerciseDone;
    private boolean partnerMode;
    private String partnerUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_tracker);

        partnerMode = getIntent().getBooleanExtra("partnerMode", false);
        partnerUserId = getIntent().getStringExtra("partnerUserId");

        if (partnerMode) {
            showHabitTrackerPopup(partnerUserId);
        } else {
            initializeUI();
            initializeDatabase();
        }
    }

    private void showHabitTrackerPopup(String partnerUserId) {
        DialogFragment habitTrackerPopupFragment = HabitTrackerPopupFragment.newInstance(partnerUserId);
        habitTrackerPopupFragment.show(getSupportFragmentManager(), "HabitTrackerPopupFragment");
    }

    private void initializeUI() {
        radioGroupWaterIntake = findViewById(R.id.radioGroupWaterIntake);
        radioGroupSleepHours = findViewById(R.id.radioGroupSleepHours);
        checkBoxBreakfast = findViewById(R.id.checkBoxBreakfast);
        checkBoxLunch = findViewById(R.id.checkBoxLunch);
        checkBoxDinner = findViewById(R.id.checkBoxDinner);
        checkBoxExerciseDone = findViewById(R.id.checkBoxExerciseDone);

        Button submitButton = findViewById(R.id.submitButton);
        submitButton.setOnClickListener(v -> submitData());
    }

    private void initializeDatabase() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() != null) {
            databaseReference = FirebaseDatabase.getInstance().getReference("HabitTracking").child(firebaseAuth.getCurrentUser().getUid());
        }
    }

    private void submitData() {
        Map<String, Object> updates = new HashMap<>();
        updates.put("waterIntake", getSelectedRadioText(radioGroupWaterIntake));
        updates.put("sleepHours", getSelectedRadioText(radioGroupSleepHours));
        updates.put("breakfast", checkBoxBreakfast.isChecked());
        updates.put("lunch", checkBoxLunch.isChecked());
        updates.put("dinner", checkBoxDinner.isChecked());
        updates.put("exerciseDone", checkBoxExerciseDone.isChecked());
        // Collect additional data
        updates.put("weight", ((EditText)findViewById(R.id.editTextWeight)).getText().toString());
        updates.put("medication", ((EditText)findViewById(R.id.editTextMedication)).getText().toString());
        updates.put("doctorAppointments", ((EditText)findViewById(R.id.editTextDoctorAppointment)).getText().toString());
        updates.put("kickCounts", ((EditText)findViewById(R.id.editTextKickCounts)).getText().toString());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String currentDate = sdf.format(new Date());
        databaseReference.child(currentDate).setValue(updates)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(HabitTrackerActivity.this, "Data submitted successfully!", Toast.LENGTH_SHORT).show();
                    provideFeedback(updates);
                })
                .addOnFailureListener(e -> Toast.makeText(HabitTrackerActivity.this, "Failed to submit data: " + e.getMessage(), Toast.LENGTH_LONG).show());
    }

    private String getSelectedRadioText(RadioGroup radioGroup) {
        int selectedId = radioGroup.getCheckedRadioButtonId();
        if (selectedId != -1) {
            RadioButton selectedRadioButton = findViewById(selectedId);
            return selectedRadioButton.getText().toString();
        }
        return "";
    }

    private void provideFeedback(Map<String, Object> data) {
        StringBuilder feedback = new StringBuilder();
        if (Boolean.TRUE.equals(data.get("exerciseDone"))) {
            feedback.append("Great job on completing your exercise today!\n");
        }
        if (Boolean.TRUE.equals(data.get("breakfast")) && Boolean.TRUE.equals(data.get("lunch")) && Boolean.TRUE.equals(data.get("dinner"))) {
            feedback.append("Fantastic! You've eaten all three meals today!\n");
        }
        if ("3 Liters".equals(data.get("waterIntake"))) {
            feedback.append("Excellent hydration!\n");
        }
        if ("8 hrs".equals(data.get("sleepHours")) || "10 hrs".equals(data.get("sleepHours"))) {
            feedback.append("You're getting plenty of rest!\n");
        }

        if (feedback.length() > 0) {
            showAlertDialog("Daily Feedback", feedback.toString());
        } else {
            showAlertDialog("Feedback", "Keep up your good habits!");
        }
    }

    private void showAlertDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }
}
