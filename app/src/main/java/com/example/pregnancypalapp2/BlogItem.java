package com.example.pregnancypalapp2;

public class BlogItem {
    private int imageResId;  // Resource ID for the image
    private String title;    // Title of the blog post
    private String content;  // Content of the blog post

    public BlogItem(int imageResId, String title, String content) {
        this.imageResId = imageResId;
        this.title = title;
        this.content = content;
    }

    public int getImageResId() {
        return imageResId;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }
}
