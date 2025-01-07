package com.example.pregnancypalapp2;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class BlogCarouselAdapter extends RecyclerView.Adapter<BlogCarouselAdapter.BlogViewHolder> {
    private List<BlogItem> blogItems;
    private Context context;

    public BlogCarouselAdapter(Context context, List<BlogItem> blogItems) {
        this.context = context;
        this.blogItems = blogItems;
    }

    @NonNull
    @Override
    public BlogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.blog_item_layout, parent, false);
        return new BlogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BlogViewHolder holder, int position) {
        BlogItem item = blogItems.get(position);
        holder.blogTitle.setText(item.getTitle());
        Glide.with(context).load(item.getImageResId()).into(holder.blogImage);

        // Handle item clicks to open the BlogDetailActivity with more details
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, BlogDetailActivity.class);
            intent.putExtra("imageResId", item.getImageResId());
            intent.putExtra("title", item.getTitle());
            intent.putExtra("content", item.getContent());  // Pass content to the detail activity
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return blogItems.size();
    }

    static class BlogViewHolder extends RecyclerView.ViewHolder {
        ImageView blogImage;
        TextView blogTitle;

        BlogViewHolder(View itemView) {
            super(itemView);
            blogImage = itemView.findViewById(R.id.blogImage);
            blogTitle = itemView.findViewById(R.id.blogTitle);
        }
    }
}
