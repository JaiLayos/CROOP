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

public class SuggestionsAdapter extends RecyclerView.Adapter<SuggestionsAdapter.SuggestionsViewHolder> {
    private Context context;
    private List<ProductDTO> suggestionList;
    private OnItemClickListener listener;

    public interface OnItemClickListener{
        void onItemClick(ProductDTO productDTO);
    }

    public SuggestionsAdapter(Context context, List<ProductDTO> productDTOList, OnItemClickListener listener){
        this.context = context;
        this.suggestionList = productDTOList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SuggestionsAdapter.SuggestionsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_home_customer_indemand, parent, false);
        return new SuggestionsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SuggestionsAdapter.SuggestionsViewHolder holder, int position) {
        ProductDTO productDTO = suggestionList.get(position);
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

    private void loadImage(StorageReference storageRef, SuggestionsViewHolder holder) {
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
            Log.e("FirebaseImageError", "Failed to get Product Profile for suggestions: " + e.getMessage());
            holder.productProfile.setImageResource(R.drawable.logo);
        });
    }

    @Override
    public int getItemCount() {
        return suggestionList.size();
    }

    public class SuggestionsViewHolder extends RecyclerView.ViewHolder{
        ImageView productProfile;
        TextView productName;
        public SuggestionsViewHolder(@NonNull View itemView) {
            super(itemView);
            productProfile = itemView.findViewById(R.id.productProfile);
            productName = itemView.findViewById(R.id.productNameText);
        }
    }
}
