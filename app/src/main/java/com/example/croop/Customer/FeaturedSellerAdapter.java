package com.example.croop.Customer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.croop.R;
import com.example.croop.model.FeaturedSellersDTO;

import java.util.List;

public class FeaturedSellerAdapter extends RecyclerView.Adapter<FeaturedSellerAdapter.FeaturedSellerAdapterHolder> {
    private Context context;
    private List<FeaturedSellersDTO> inventory;
    private OnItemClickListener listener;

    public interface OnItemClickListener{
        void onViewProfileClick(FeaturedSellersDTO featuredSellersDTO);
        void onViewProductsClick(FeaturedSellersDTO featuredSellersDTO);
    }

    public FeaturedSellerAdapter(Context context, List<FeaturedSellersDTO> inventory, OnItemClickListener listener){
        this.context = context;
        this.inventory = inventory;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FeaturedSellerAdapter.FeaturedSellerAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_home_customer_featured_sellers,parent,false);
        return new FeaturedSellerAdapterHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeaturedSellerAdapter.FeaturedSellerAdapterHolder holder, int position) {
        FeaturedSellersDTO featuredSellersDTO = inventory.get(position);

        holder.name.setText(featuredSellersDTO.getName());
        holder.role.setText(featuredSellersDTO.getRole());
        holder.profile.setOnClickListener(v -> {
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
        });
    }

    @Override
    public int getItemCount() {
        return inventory.size();
    }

    public static class FeaturedSellerAdapterHolder extends RecyclerView.ViewHolder{
        TextView name, role;
        ImageView display;
        Button profile, products;
        public FeaturedSellerAdapterHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.sellerNameText);
            role = itemView.findViewById(R.id.sellerRoleText);
            display = itemView.findViewById(R.id.sellerProfile);
            profile = itemView.findViewById(R.id.profileButton);
            products = itemView.findViewById(R.id.productsButton);
        }
    }
}
