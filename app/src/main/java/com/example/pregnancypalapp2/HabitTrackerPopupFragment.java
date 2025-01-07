// HabitTrackerPopupFragment.java

package com.example.pregnancypalapp2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HabitTrackerPopupFragment extends DialogFragment {

    private static final String ARG_PARTNER_USER_ID = "partnerUserId";
    private String partnerUserId;

    public static HabitTrackerPopupFragment newInstance(String partnerUserId) {
        HabitTrackerPopupFragment fragment = new HabitTrackerPopupFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARTNER_USER_ID, partnerUserId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            partnerUserId = getArguments().getString(ARG_PARTNER_USER_ID);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.popup_habit_tracker, container, false);

        TextView textViewHabitSummary = view.findViewById(R.id.textViewHabitSummary);
        Button buttonClosePopup = view.findViewById(R.id.buttonClosePopup);

        buttonClosePopup.setOnClickListener(v -> dismiss());

        loadPartnerHabitData(partnerUserId, textViewHabitSummary);

        return view;
    }

    private void loadPartnerHabitData(String partnerUserId, TextView textViewHabitSummary) {
        DatabaseReference habitTrackingRef = FirebaseDatabase.getInstance().getReference("HabitTracking").child(partnerUserId);
        habitTrackingRef.limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                StringBuilder summary = new StringBuilder();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    summary.append("Date: ").append(snapshot.getKey()).append("\n");
                    for (DataSnapshot habitSnapshot : snapshot.getChildren()) {
                        summary.append(habitSnapshot.getKey()).append(": ").append(habitSnapshot.getValue().toString()).append("\n");
                    }
                }
                textViewHabitSummary.setText(summary.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                textViewHabitSummary.setText("Failed to load data.");
            }
        });
    }
}
