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
import com.example.croop.model.DiscountDTO;
import com.example.croop.model.ProductDTO;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class PromoAdapter extends RecyclerView.Adapter<PromoAdapter.PromoViewHolder> {

    private Context context;
    private List<DiscountDTO> promoList;
    private List<ProductDTO> productList;
    private ProductDTO productDTO;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(ProductDTO productDTO);
    }

    public PromoAdapter(Context context, List<DiscountDTO> promoList,
                        List<ProductDTO> productList,
                        OnItemClickListener listener) {
        this.context = context;
        this.promoList = promoList;
        this.productList = productList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PromoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each item
        View view = LayoutInflater.from(context).inflate(R.layout.layout_home_customer_discount, parent, false);
        return new PromoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PromoViewHolder holder, int position) {
        // Bind data to the views
        DiscountDTO currentItem = promoList.get(position);
        holder.productName.setText(currentItem.getItemName());
        holder.productDiscount.setText(String.valueOf(currentItem.getDiscountPercent() * 100) + "%");
        holder.off.setText(currentItem.getSellerName());

        String firebaseID = currentItem.getFirebaseID();
        String fileName = currentItem.getItemName();

        StorageReference storageRef = FirebaseStorage.getInstance().getReference()
                .child("Products")
                .child(firebaseID)
                .child(fileName);

        loadImage(storageRef, holder);

        ProductDTO currentProduct = productList.get(position);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(currentProduct);
            }
        });
    }

    private void loadImage(StorageReference storageRef, PromoViewHolder holder) {
        storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
            Glide.with(holder.itemView.getContext())
                    .load(uri.toString())
                    .placeholder(R.drawable.logo)
                    .error(R.drawable.sun)
                    .into(holder.crop);
        }).addOnFailureListener(e -> {
            Log.e("FirebaseImageError", "Failed to get download URL: " + e.getMessage());
            holder.crop.setImageResource(R.drawable.logo);
        });
    }

    @Override
    public int getItemCount() {
        return promoList.size();
    }

    // ViewHolder class
    public static class PromoViewHolder extends RecyclerView.ViewHolder {
        TextView productName, productDiscount, off;
        ImageView crop;

        public PromoViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productNameText);
            productDiscount = itemView.findViewById(R.id.productDiscountText);
            off = itemView.findViewById(R.id.productSellerText);
            crop = itemView.findViewById(R.id.productImage);
        }
    }
}
