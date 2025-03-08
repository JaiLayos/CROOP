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

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ProductListViewHolder> {
    private Context context;
    private List<ProductDTO> productDTOList;
    private OnItemClickListener listener;

    public interface OnItemClickListener{
        void onItemClick(ProductDTO productDTO);
    }

    public ProductListAdapter(Context context, List<ProductDTO> productDTOList,
                              OnItemClickListener listener) {
        this.context = context;
        this.productDTOList = productDTOList;
        this.listener = listener;
    }


    @NonNull
    @Override
    public ProductListAdapter.ProductListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_products_customer_list, parent, false);
        return new ProductListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductListAdapter.ProductListViewHolder holder, int position) {
        ProductDTO productDTO = productDTOList.get(position);
        holder.cropName.setText(productDTO.getProductName());
        holder.sellerName.setText(productDTO.getProductSeller());
        holder.sellerRole.setText(productDTO.getSellerRole());

        String firebaseID = productDTO.getFirebaseID();
        String fileName = productDTO.getProductName();

        StorageReference storageRef = FirebaseStorage.getInstance().getReference()
                .child("Products")
                .child(firebaseID)
                .child(fileName);

        holder.itemView.setOnClickListener(v -> {
            if(listener != null){
                listener.onItemClick(productDTO);
            }
        });

        loadImage(storageRef, holder);
    }



    private void loadImage(StorageReference storageRef, ProductListViewHolder holder) {
        Context appContext = holder.itemView.getContext().getApplicationContext();
        storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
            if (holder.itemView.isAttachedToWindow()) {
                Glide.with(appContext)
                        .load(uri.toString())
                        .placeholder(R.drawable.logo)
                        .error(R.drawable.sun)
                        .into(holder.cropProfile);
            }
        }).addOnFailureListener(e -> {
            Log.e("FirebaseImageError", "Failed to get download URL: " + e.getMessage());
            holder.cropProfile.setImageResource(R.drawable.logo);
        });
    }

    @Override
    public int getItemCount() {
        return productDTOList.size();
    }

    public static class ProductListViewHolder extends RecyclerView.ViewHolder{
        TextView cropName, sellerName, sellerRole;
        ImageView cropProfile;
        public ProductListViewHolder(@NonNull View itemView) {
            super(itemView);
            cropName = itemView.findViewById(R.id.cropNameText);
            sellerName = itemView.findViewById(R.id.sellerNameText);
            sellerRole = itemView.findViewById(R.id.sellerRoleText);
            cropProfile = itemView.findViewById(R.id.cropProfile);
        }

    }
}
