package com.example.pregnancypalapp2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MoodTrackerPopupFragment extends DialogFragment {

    private static final String ARG_PARTNER_USER_ID = "partnerUserId";
    private String partnerUserId;
    private TextView textViewMoodSummary;

    public static MoodTrackerPopupFragment newInstance(String partnerUserId) {
        MoodTrackerPopupFragment fragment = new MoodTrackerPopupFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARTNER_USER_ID, partnerUserId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            partnerUserId = getArguments().getString(ARG_PARTNER_USER_ID);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mood_tracker_popup, container, false);
        textViewMoodSummary = view.findViewById(R.id.textViewMoodSummary);
        loadMoodData();
        return view;
    }

    private void loadMoodData() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("MoodTracking").child(partnerUserId);
        databaseReference.limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String mood = snapshot.child("mood").getValue(String.class);
                        String notes = snapshot.child("notes").getValue(String.class);
                        String timestamp = snapshot.child("timestamp").getValue(String.class);

                        textViewMoodSummary.setText(String.format("Mood: %s\nNotes: %s\nDate: %s", mood, notes, timestamp));
                    }
                } else {
                    textViewMoodSummary.setText("No mood data available.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                textViewMoodSummary.setText("Failed to load mood data.");
            }
        });
    }
}
