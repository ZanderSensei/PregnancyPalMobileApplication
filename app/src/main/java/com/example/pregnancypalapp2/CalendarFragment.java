package com.example.pregnancypalapp2;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.viewpager2.widget.ViewPager2;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass that represents the Calendar view in the app.
 */
public class CalendarFragment extends Fragment {

    private ViewPager2 blogCarouselViewPager;
    private List<BlogItem> blogItems;
    private TextView daysOfWeekTextView;
    private TextView pregnancyWeekTextView; // TextView for displaying the week of pregnancy

    public CalendarFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        // Initialize the ViewPager2 and BlogCarouselAdapter
        blogCarouselViewPager = view.findViewById(R.id.blogCarouselViewPager);
        initializeBlogItems();
        blogCarouselViewPager.setAdapter(new BlogCarouselAdapter(getContext(), blogItems));

        daysOfWeekTextView = view.findViewById(R.id.daysOfWeekTextView);
        pregnancyWeekTextView = view.findViewById(R.id.pregnancyWeekTextView);

        updateDate(daysOfWeekTextView);
        updatePregnancyWeek();

        return view;
    }

    private void updateDate(TextView textView) {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        SimpleDateFormat sdf = new SimpleDateFormat("d'" + getDaySuffix(day) + "' EEEE", Locale.getDefault());
        String formattedDate = sdf.format(calendar.getTime());
        textView.setText(formattedDate);
    }

    private void updatePregnancyWeek() {
        Date dueDate = getExpectedDueDate(); // You need to implement this method
        if (dueDate != null) {
            Calendar dueCalendar = Calendar.getInstance();
            dueCalendar.setTime(dueDate);
            Calendar today = Calendar.getInstance();
            int weeks = (int) ((dueCalendar.getTimeInMillis() - today.getTimeInMillis()) / (1000 * 60 * 60 * 24 * 7));
            pregnancyWeekTextView.setText(String.format(Locale.getDefault(), "Week %d of 40", 40 - weeks));
        } else {
            pregnancyWeekTextView.setText("Week data not available");
        }
    }

    private Date getExpectedDueDate() {
        // This method should retrieve the actual due date, possibly from SharedPreferences or Firebase
        // Placeholder for the expected due date
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            return sdf.parse("2024-12-25"); // example due date
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getDaySuffix(int day) {
        switch (day) {
            case 1:
            case 21:
            case 31:
                return "st";
            case 2:
            case 22:
                return "nd";
            case 3:
            case 23:
                return "rd";
            default:
                return "th";
        }
    }

    private void initializeBlogItems() {
        blogItems = new ArrayList<>();
        // Example items - replace with real data as needed
        blogItems.add(new BlogItem(R.drawable.blog1, "The First Trimester: What to Expect and How to Cope",
                "Introduction to the First Trimester: Brief overview of what the first trimester entails, including common physical and emotional changes.\n" +
                        "Common Symptoms: Detailed look at nausea, fatigue, mood swings, and other early pregnancy symptoms.\n" +
                        "Health and Nutrition: Key nutrients needed during the first trimester, foods to favor, and foods to avoid.\n" +
                        "Doctor Visits and Tests: What to expect during prenatal visits, important tests, and screenings in the first trimester.\n" +
                        "Tips for Managing Symptoms: Practical advice on coping with morning sickness, fatigue, and emotional fluctuations.\n" +
                        "Conclusion: Encouragement and support for expectant mothers during this challenging yet exciting time."));

        blogItems.add(new BlogItem(R.drawable.blog2, "Planning Your Maternity Leave: A Step-by-Step Guide",
                "Understanding Your Rights and Benefits: Overview of maternity leave laws and rights in different regions/countries.\n" +
                        "When to Start Your Leave: Advice on deciding the best time to start maternity leave based on health, job requirements, and personal preferences.\n" +
                        "Preparing at Work: How to prepare for your absence at work, including handover strategies and communication plans.\n" +
                        "Budgeting for Maternity Leave: Financial planning tips for the unpaid portion of maternity leave, if applicable.\n" +
                        "Making the Most of Maternity Leave: Suggestions for balancing rest, baby preparation, and personal time.\n" +
                        "Conclusion: Strategies to ensure a smooth transition into and out of maternity leave."));

        blogItems.add(new BlogItem(R.drawable.blog3, "Understanding and Preparing for Labor and Delivery",
                "Types of Deliveries: Explanation of natural, assisted, and cesarean deliveries.\n" +
                        "Signs of Labor: How to recognize the onset of labor and when to go to the hospital.\n" +
                        "Pain Management Options: Overview of natural and medical pain relief methods available during labor.\n" +
                        "What to Pack for the Hospital: Essentials for your hospital bag for both mother and baby.\n" +
                        "Creating a Birth Plan: Tips for drafting a birth plan that reflects your wishes and preferences.\n" +
                        "Conclusion: Preparing mentally and physically for the transformative experience of childbirth."));
    }
}
