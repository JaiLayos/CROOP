package com.example.croop;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.croop.model.Notifications;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationAdapterViewHolder> {
    private List<Notifications> notifications;

    public NotificationAdapter(List<Notifications> notifications){
        this.notifications = notifications;
    }


    @NonNull
    @Override
    public NotificationAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.inventory_threshold_alert, parent, false); // Make sure this matches your XML
        return new NotificationAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.NotificationAdapterViewHolder holder, int position) {
        Notifications notification = notifications.get(position);
        holder.about.setText(notification.getAbout());
        holder.text.setText(notification.getMessage());
        holder.date.setText(notification.getDate().toString());
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public static class NotificationAdapterViewHolder extends RecyclerView.ViewHolder {
        TextView about, text, date;

        public NotificationAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            about = itemView.findViewById(R.id.notificationAbout);
            text = itemView.findViewById(R.id.notificationText);
            date = itemView.findViewById(R.id.notificationDate);
        }
    }
}
