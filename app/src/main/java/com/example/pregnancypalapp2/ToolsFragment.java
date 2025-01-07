package com.example.pregnancypalapp2;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

public class ToolsFragment extends Fragment {

    private String partnerUid;

    public ToolsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            partnerUid = getArguments().getString("partnerUid");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tools, container, false);

        CardView cardViewHabitTracker = view.findViewById(R.id.cardViewHabitTracker);
        cardViewHabitTracker.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), HabitTrackerActivity.class);
            if (partnerUid != null) {
                intent.putExtra("partnerMode", true);
                intent.putExtra("partnerUserId", partnerUid);
            } else {
                intent.putExtra("partnerMode", false);
            }
            startActivity(intent);
        });

        CardView cardViewMoodTracker = view.findViewById(R.id.cardViewMoodTracker);
        cardViewMoodTracker.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), MoodTrackerActivity.class);
            if (partnerUid != null) {
                intent.putExtra("partnerMode", true);
                intent.putExtra("partnerUserId", partnerUid);
            } else {
                intent.putExtra("partnerMode", false);
            }
            startActivity(intent);
        });

        return view;
    }
}
