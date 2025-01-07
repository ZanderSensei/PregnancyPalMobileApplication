package com.example.pregnancypalapp2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.os.Bundle;
import android.widget.RadioGroup;

public class SettingsActivity extends AppCompatActivity {

    private RadioGroup themeOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Initialize RadioGroup from the layout
        themeOptions = findViewById(R.id.themeOptions);
        themeOptions.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.systemDefault) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
            } else if (checkedId == R.id.lightTheme) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            } else if (checkedId == R.id.darkTheme) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }
        });

        // Set the initial state based on the current theme
        setCurrentThemeSelection();
    }

    private void setCurrentThemeSelection() {
        int currentNightMode = AppCompatDelegate.getDefaultNightMode();
        if (currentNightMode == AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM) {
            themeOptions.check(R.id.systemDefault);
        } else if (currentNightMode == AppCompatDelegate.MODE_NIGHT_NO) {
            themeOptions.check(R.id.lightTheme);
        } else if (currentNightMode == AppCompatDelegate.MODE_NIGHT_YES) {
            themeOptions.check(R.id.darkTheme);
        }
    }
}
