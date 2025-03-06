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

public class InSeasonAdapter extends RecyclerView.Adapter<InSeasonAdapter.InSeasonViewHolder> {
    private Context context;
    private List<ProductDTO> products;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(ProductDTO productDTO);
    }

    public InSeasonAdapter(Context context, List<ProductDTO> products, OnItemClickListener listener){
        this.context = context;
        this.products = products;
        this.listener = listener;
    }

    @NonNull
    @Override
    public InSeasonAdapter.InSeasonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_home_customer_inseason, parent, false);
        return new InSeasonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InSeasonAdapter.InSeasonViewHolder holder, int position) {
        ProductDTO product = products.get(position);
        holder.productName.setText(product.getProductName());

        String firebaseID = product.getFirebaseID();
        String fileName = product.getProductName();

        StorageReference storageRef = FirebaseStorage.getInstance().getReference()
                .child("Products")
                .child(firebaseID)
                .child(fileName);

        loadImage(storageRef, holder);
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(product);
            }
        });
    }

    private void loadImage(StorageReference storageRef, InSeasonViewHolder holder) {
        storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
            Glide.with(holder.itemView.getContext())
                    .load(uri.toString())
                    .placeholder(R.drawable.logo)
                    .error(R.drawable.sun)
                    .into(holder.productPhoto);
        }).addOnFailureListener(e -> {
            Log.e("FirebaseImageError", "Failed to get download URL: " + e.getMessage());
            holder.productPhoto.setImageResource(R.drawable.logo);
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public static class InSeasonViewHolder extends RecyclerView.ViewHolder {
        TextView productName;
        ImageView productPhoto;
        public InSeasonViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productNameText);
            productPhoto = itemView.findViewById(R.id.productProfile);
        }
    }
}
