package com.example.croop.Customer;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.croop.R;

import java.util.List;
import java.util.Map;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentsAdapterViewHolder> {
    private Context context;
    private List<Map<String, String>> comments;
    private OnItemClickListener listener;
    private String userName, commentText, stars, key, value;

    public interface OnItemClickListener {
        void onDeleteClick(String commentKey, String commentUsername);
    }

    public CommentsAdapter(List<Map<String, String>> comments, Context context, OnItemClickListener listener){
        this.comments = comments;
        this.context = context;
        this.listener = listener;
    }
    @NonNull
    @Override
    public CommentsAdapter.CommentsAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.comments_each, parent, false);
        return new CommentsAdapter.CommentsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentsAdapter.CommentsAdapterViewHolder holder, int position) {
        Map<String, String> comment = comments.get(position);
        Log.d("DEBUG", "Comment at position " + position + ": " + comment.toString());

        if (!comment.isEmpty()) {
            Map.Entry<String, String> entry = comment.entrySet().iterator().next();
            key = entry.getKey();
            value = entry.getValue();

            String[] parts = value.split(": ", 3);
            if (parts.length == 3) {
                userName = parts[0];
                commentText = parts[1];
                stars = parts[2];

                holder.userName.setText(userName);
                holder.comments.setText(commentText);
                holder.score.setText(stars + "/5 STARS");
            } else {
                holder.userName.setText("Unknown User");
                holder.comments.setText(value);
            }
        }
        holder.itemView.setOnLongClickListener(v -> {
            if (listener != null) {
                holder.wholeCard.setCardBackgroundColor(context.getResources().getColor(R.color.secondary_color));
                listener.onDeleteClick(key, userName);
                v.postDelayed(() -> holder.wholeCard.setCardBackgroundColor(
                        context.getResources().getColor(android.R.color.white)), 500);
            }
            return true; // Indicate that the long press was handled
        });
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public static class CommentsAdapterViewHolder extends RecyclerView.ViewHolder {
        TextView userName, comments, score;
        CardView wholeCard;
        public CommentsAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.userNameLabel);
            comments = itemView.findViewById(R.id.commentText);
            score = itemView.findViewById(R.id.scoreText);
            wholeCard = itemView.findViewById(R.id.wholeCard);
        }
    }


}
