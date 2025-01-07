package com.example.pregnancypalapp2;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawer;
    private NavigationView navigationView;
    private static final String CHANNEL_ID = "appointment_reminders";
    private static final String CHANNEL_NAME = "Appointment Reminders";
    private static final String CHANNEL_DESCRIPTION = "Notifications for upcoming doctor's appointments";
    private String partnerUid;
    private TextView partnerModeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createNotificationChannel();

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        partnerModeTextView = findViewById(R.id.partnerModeTextView);
        Button menuButton = findViewById(R.id.menuButton);

        // Check if in partner mode
        partnerUid = getIntent().getStringExtra("partnerUid");
        if (partnerUid != null) {
            partnerModeTextView.setVisibility(View.VISIBLE);
        }

        // Set up the navigation drawer header with user data
        loadUserDataFromFirebase();

        // Handling Navigation Item Selected Listener for the Drawer
        navigationView.setNavigationItemSelectedListener(this::onNavigationItemSelected);

        // Setting up the menu button to open the drawer
        menuButton.setOnClickListener(v -> drawer.openDrawer(GravityCompat.START));

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            int itemId = item.getItemId();
            if (itemId == R.id.navigation_calendar) {
                selectedFragment = new CalendarFragment();
            } else if (itemId == R.id.navigation_tools) {
                selectedFragment = new ToolsFragment();
                if (partnerUid != null) {
                    Bundle args = new Bundle();
                    args.putString("partnerUid", partnerUid);
                    selectedFragment.setArguments(args);
                }
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, selectedFragment)
                        .commit();
            }

            return true;
        });

        // Set default selection
        bottomNavigationView.setSelectedItemId(R.id.navigation_calendar);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance);
            channel.setDescription(CHANNEL_DESCRIPTION);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    private boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_notifications) {
            scheduleAppointmentNotifications();
            startActivity(new Intent(this, NotificationsActivity.class));
        } else if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
        } else if (id == R.id.action_support) {
            startActivity(new Intent(this, SupportActivity.class));
        } else if (id == R.id.action_faq) {
            startActivity(new Intent(this, FAQActivity.class));
        } else if (id == R.id.action_partner_link) {
            startActivity(new Intent(this, PartnerLinkActivity.class));
        } else {
            return false;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void loadUserDataFromFirebase() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("UserResponses");

        if (auth.getCurrentUser() != null) {
            String userEmail = auth.getCurrentUser().getEmail();
            if (userEmail != null) {
                String formattedEmailKey = userEmail.replace('.', ',');

                databaseReference.child(formattedEmailKey).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        if (user != null) {
                            TextView nameTextView = navigationView.getHeaderView(0).findViewById(R.id.textViewName);
                            TextView emailTextView = navigationView.getHeaderView(0).findViewById(R.id.textViewEmail);

                            nameTextView.setText(user.fullName);
                            emailTextView.setText(user.emailAddress);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.w("DBError", "loadUserData:onCancelled", databaseError.toException());
                    }
                });
            }
        }
    }

    private void scheduleAppointmentNotifications() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("HabitTracking")
                .child(auth.getCurrentUser().getUid()).child("doctorAppointment");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String appointmentDate = snapshot.getValue(String.class);
                    if (appointmentDate != null) {
                        try {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                            Date date = sdf.parse(appointmentDate);
                            if (date != null) {
                                scheduleNotification(date.getTime());
                            }
                        } catch (ParseException e) {
                            Log.e("ScheduleNotif", "Error parsing date", e);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("DBError", "loadAppointments:onCancelled", databaseError.toException());
            }
        });
    }

    private void scheduleNotification(long timeInMillis) {
        if (canScheduleExactAlarms()) {
            Intent intent = new Intent(this, AppointmentReminderReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            if (alarmManager != null) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent);
            }
        } else {
            requestExactAlarmPermission();
        }
    }

    private boolean canScheduleExactAlarms() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            return getSystemService(AlarmManager.class).canScheduleExactAlarms();
        }
        return true;
    }

    private void requestExactAlarmPermission() {
        Intent intent = new Intent(android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
        startActivity(intent);
    }

    // Define the User class according to your Firebase structure
    public static class User {
        public String fullName, emailAddress;

        public User() {
        }
    }
}
