package com.example.croop.Customer;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.croop.R;

import java.util.List;
import java.util.Map;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentsAdapterViewHolder> {
    private Context context;
    private List<Map<String, String>> comments;
    public CommentsAdapter(List<Map<String, String>> comments, Context context){
        this.comments = comments;
        this.context = context;
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
            Log.d("DEBUG", "Key: " + entry.getKey() + ", Value: " + entry.getValue());

            holder.userName.setText(entry.getKey());  // This should be user ID
            holder.comments.setText(entry.getValue());  // This should be the comment text
        }
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public static class CommentsAdapterViewHolder extends RecyclerView.ViewHolder {
        TextView userName, comments;
        public CommentsAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.userNameLabel);
            comments = itemView.findViewById(R.id.commentText);
        }
    }
}
