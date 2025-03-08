package com.example.croop.Customer;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.croop.R;
import com.example.croop.model.ProductDTO;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class InDemandAdapter extends RecyclerView.Adapter<InDemandAdapter.InDemandViewHolder > {
    private Context context;
    private List<ProductDTO> products;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(ProductDTO productDTO);
    }

    public InDemandAdapter(Context context, List<ProductDTO> products, OnItemClickListener listener){
        this.context = context;
        this.products = products;
        this.listener = listener;
    }

    @NonNull
    @Override
    public InDemandAdapter.InDemandViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_home_customer_indemand, parent, false);
        return new InDemandViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InDemandAdapter.InDemandViewHolder holder, int position) {
        ProductDTO productDTO = products.get(position);
        holder.productName.setText(productDTO.getProductName());

        String firebaseID = productDTO.getFirebaseID();
        String fileName = productDTO.getProductName();

        StorageReference storageRef = FirebaseStorage.getInstance().getReference()
                .child("Products")
                .child(firebaseID)
                .child(fileName);

        loadImage(storageRef, holder);
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(productDTO);
            }
        });
    }

    private void loadImage(StorageReference storageRef, InDemandViewHolder holder) {
        Context appContext = holder.itemView.getContext().getApplicationContext();
        storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
            if (holder.itemView.isAttachedToWindow()) {
                Glide.with(appContext)
                        .load(uri.toString())
                        .placeholder(R.drawable.logo)
                        .error(R.drawable.sun)
                        .into(holder.productProfile);
            }
        }).addOnFailureListener(e -> {
            Log.e("FirebaseImageError", "Failed to get download URL: " + e.getMessage());
            holder.productProfile.setImageResource(R.drawable.logo);
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public static class InDemandViewHolder extends RecyclerView.ViewHolder{
        TextView productName;
        ImageView productProfile;
        public InDemandViewHolder(@NonNull View itemView) {
            super(itemView);

            productName = itemView.findViewById(R.id.productNameText);
            productProfile = itemView.findViewById(R.id.productProfile);
        }
    }
}
