package com.example.pregnancypalapp2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class FAQAdapter extends RecyclerView.Adapter<FAQAdapter.FAQViewHolder> {
    private List<FAQItem> faqItems;

    public FAQAdapter(List<FAQItem> faqItems) {
        this.faqItems = faqItems;
    }

    @Override
    public FAQViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.faq_item_layout, parent, false);
        return new FAQViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FAQViewHolder holder, int position) {
        FAQItem item = faqItems.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return faqItems.size();
    }

    class FAQViewHolder extends RecyclerView.ViewHolder {
        TextView questionView;
        TextView answerView;

        FAQViewHolder(View itemView) {
            super(itemView);
            questionView = itemView.findViewById(R.id.questionText);
            answerView = itemView.findViewById(R.id.answerText);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FAQItem item = faqItems.get(getAdapterPosition());
                    item.setExpanded(!item.isExpanded()); // Toggle the expanded state
                    notifyItemChanged(getAdapterPosition());
                }
            });
        }

        void bind(FAQItem item) {
            questionView.setText(item.getQuestion());
            answerView.setText(item.getAnswer());
            answerView.setVisibility(item.isExpanded() ? View.VISIBLE : View.GONE); // Show or hide the answer
        }
    }
}


