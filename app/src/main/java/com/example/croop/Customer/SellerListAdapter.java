package com.example.croop.Customer;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.croop.R;
import com.example.croop.model.FeaturedSellersDTO;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class SellerListAdapter extends RecyclerView.Adapter<SellerListAdapter.SellerListAdapterHolder> {
    private Context context;
    private List<FeaturedSellersDTO> inventory;
    private FeaturedSellerAdapter.OnItemClickListener listener;
    private StorageReference storageRef;
    private FirebaseAuth mAuth;
    public interface OnItemClickListener{
        void onViewProfileClick(FeaturedSellersDTO featuredSellersDTO);
        void onViewProductsClick(FeaturedSellersDTO featuredSellersDTO);
    }
    public SellerListAdapter(Context context, List<FeaturedSellersDTO> inventory, FeaturedSellerAdapter.OnItemClickListener listener){
        this.context = context;
        this.inventory = inventory;
        this.listener = listener;
    }
    @NonNull
    @Override
    public SellerListAdapter.SellerListAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_home_customer_featured_sellers,parent,false);
        return new SellerListAdapter.SellerListAdapterHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SellerListAdapter.SellerListAdapterHolder holder, int position) {
        FeaturedSellersDTO featuredSellersDTO = inventory.get(position);

        String userID = featuredSellersDTO.getFirebaseID();
        holder.name.setText(featuredSellersDTO.getName());
        holder.role.setText(featuredSellersDTO.getRole());
        holder.profile.setOnClickListener(v1 -> {
            if (listener != null) {
                listener.onViewProfileClick(featuredSellersDTO);
            }
        });

        // Handle "View Products" button click
        holder.products.setOnClickListener(v2 -> {
            if (listener != null) {
                listener.onViewProductsClick(featuredSellersDTO);
            }
        });
        storageRef = FirebaseStorage.getInstance().getReference()
                .child("Profile Picture")
                .child(userID)
                .child("Display");

        storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
            Glide.with(holder.itemView.getContext())
                    .load(uri.toString())
                    .placeholder(R.drawable.logo)
                    .error(R.drawable.sun)
                    .into(holder.display);
        }).addOnFailureListener(e -> {
            Log.e("FirebaseImageError", "Failed to get download URL: " + e.getMessage());
            holder.display.setImageResource(R.drawable.logo);
        });
    }

    @Override
    public int getItemCount() {
        return inventory.size();
    }

    public static class SellerListAdapterHolder extends RecyclerView.ViewHolder{
        TextView name, role;
        ImageView display;
        Button profile, products;
        public SellerListAdapterHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.sellerNameText);
            role = itemView.findViewById(R.id.sellerRoleText);
            display = itemView.findViewById(R.id.sellerProfile);
            profile = itemView.findViewById(R.id.profileButton);
            products = itemView.findViewById(R.id.productsButton);
        }
    }
}
