package com.example.pregnancypalapp2;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class FAQActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FAQAdapter adapter;
    private List<FAQItem> faqList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);

        recyclerView = findViewById(R.id.rvFAQ);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        faqList = getFAQs();
        adapter = new FAQAdapter(faqList);
        recyclerView.setAdapter(adapter);

        Button backButton = findViewById(R.id.btnBackFAQ);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Close this activity and return to the previous one
            }
        });
    }

    // Method to create a list of FAQs
    private List<FAQItem> getFAQs() {
        List<FAQItem> faqItems = new ArrayList<>();
        faqItems.add(new FAQItem("What is Pregnancy Pal?", "Pregnancy Pal is an app designed to support women through their pregnancy with tools and resources."));
        faqItems.add(new FAQItem("How do I track my appointments?", "You can use the calendar feature to add and view upcoming doctor's appointments."));
        faqItems.add(new FAQItem("What should I eat during pregnancy?", "The app provides nutritional tips and tracks your meals to ensure a healthy diet."));
        // Add more FAQs as needed
        return faqItems;
    }
}
