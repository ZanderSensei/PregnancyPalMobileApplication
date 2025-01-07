package com.example.pregnancypalapp2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class BlogDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_detail);

        ImageView imageView = findViewById(R.id.blogDetailImage);
        TextView titleView = findViewById(R.id.blogDetailTitle);
        TextView contentView = findViewById(R.id.blogDetailContent);

        // Retrieve data from intent
        int imageResId = getIntent().getIntExtra("imageResId", 0);
        String title = getIntent().getStringExtra("title");
        String content = getIntent().getStringExtra("content");

        // Set the retrieved data
        imageView.setImageResource(imageResId);
        titleView.setText(title);
        contentView.setText(content);
    }
}
