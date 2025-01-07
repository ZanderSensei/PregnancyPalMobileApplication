package com.example.pregnancypalapp2;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MoodTrackerActivity extends AppCompatActivity {

    private GridLayout gridLayoutMoods;
    private EditText editTextMoodNotes;
    private Button buttonSubmitMood;
    private boolean partnerMode;
    private String partnerUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_tracker);

        partnerMode = getIntent().getBooleanExtra("partnerMode", false);
        partnerUserId = getIntent().getStringExtra("partnerUserId");

        if (partnerMode) {
            showMoodTrackerPopup(partnerUserId);
        } else {
            initializeUI();
            setupSubmitButton();
        }
    }

    private void showMoodTrackerPopup(String partnerUserId) {
        DialogFragment moodTrackerPopupFragment = MoodTrackerPopupFragment.newInstance(partnerUserId);
        moodTrackerPopupFragment.show(getSupportFragmentManager(), "MoodTrackerPopupFragment");
    }

    private void initializeUI() {
        gridLayoutMoods = findViewById(R.id.gridLayoutMoods);
        editTextMoodNotes = findViewById(R.id.editTextMoodNotes);
        buttonSubmitMood = findViewById(R.id.buttonSubmitMood);
        initializeMoodSelection();
    }

    private void initializeMoodSelection() {
        int childCount = gridLayoutMoods.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = gridLayoutMoods.getChildAt(i);
            if (child instanceof CardView) {
                CardView cardView = (CardView) child;
                cardView.setOnClickListener(this::onMoodSelected);
                TextView moodTextView = cardView.findViewById(R.id.textViewMood);
                ImageView moodImageView = cardView.findViewById(R.id.imageMood);
                switch (i) {
                    case 0:
                        moodTextView.setText("Happy");
                        moodImageView.setImageResource(R.drawable.happy_icon);
                        break;
                    case 1:
                        moodTextView.setText("Sad");
                        moodImageView.setImageResource(R.drawable.sad_icon);
                        break;
                    case 2:
                        moodTextView.setText("Angry");
                        moodImageView.setImageResource(R.drawable.angry_icon);
                        break;
                    case 3:
                        moodTextView.setText("Excited");
                        moodImageView.setImageResource(R.drawable.excited_icon);
                        break;
                    case 4:
                        moodTextView.setText("Tired");
                        moodImageView.setImageResource(R.drawable.tired_icon);
                        break;
                    case 5:
                        moodTextView.setText("Stressed");
                        moodImageView.setImageResource(R.drawable.stressed_icon);
                        break;
                    case 6:
                        moodTextView.setText("Calm");
                        moodImageView.setImageResource(R.drawable.calm_icon);
                        break;
                }
            }
        }
    }

    private void onMoodSelected(View view) {
        resetMoodSelections();
        view.setSelected(true);
        TextView moodTextView = view.findViewById(R.id.textViewMood);
        String selectedMood = moodTextView.getText().toString();
        editTextMoodNotes.setHint("Notes about your " + selectedMood + " mood");
    }

    private void resetMoodSelections() {
        int childCount = gridLayoutMoods.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = gridLayoutMoods.getChildAt(i);
            if (child instanceof CardView) {
                child.setSelected(false);
            }
        }
    }

    private void setupSubmitButton() {
        buttonSubmitMood.setOnClickListener(v -> submitMoodData());
    }

    private void submitMoodData() {
        String moodNotes = editTextMoodNotes.getText().toString();
        String selectedMood = getSelectedMood();

        Map<String, Object> moodData = new HashMap<>();
        moodData.put("mood", selectedMood);
        moodData.put("notes", moodNotes);
        moodData.put("timestamp", getCurrentDate());

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("MoodTracking");
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            databaseReference.child(auth.getCurrentUser().getUid())
                    .push()
                    .setValue(moodData)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            showToast("Mood data submitted successfully!");
                        } else {
                            showToast("Failed to submit mood data.");
                        }
                    });
        }
    }

    private String getSelectedMood() {
        int childCount = gridLayoutMoods.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = gridLayoutMoods.getChildAt(i);
            if (child instanceof CardView && child.isSelected()) {
                TextView moodTextView = child.findViewById(R.id.textViewMood);
                return moodTextView.getText().toString();
            }
        }
        return "Not set"; // Default if no mood is selected
    }

    private String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return sdf.format(new Date());
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
