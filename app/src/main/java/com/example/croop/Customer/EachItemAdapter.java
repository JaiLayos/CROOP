package com.example.croop.Customer;

import android.content.Context;
import android.text.InputType;
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
import com.example.croop.model.CartDTO;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class EachItemAdapter extends RecyclerView.Adapter<EachItemAdapter.EachItemAdapterHolder> {
    private Context context;
    private List<CartDTO> cartDTOList;

    public EachItemAdapter(Context context, List<CartDTO> cartDTOList){
        this.context = context;
        this.cartDTOList = cartDTOList;
    }

    @NonNull
    @Override
    public EachItemAdapter.EachItemAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.customer_cart_per_item, parent, false);
        return new EachItemAdapterHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EachItemAdapter.EachItemAdapterHolder holder, int position) {
        CartDTO cartDTO = cartDTOList.get(position);
        holder.cropName.setText(cartDTO.getCropName());
        holder.quantity.setInputType(InputType.TYPE_CLASS_NUMBER);
        holder.quantity.setText(String.valueOf(cartDTO.getQuantity()));
        holder.priceLabel.setText("₱" + String.valueOf(cartDTO.getPrice()));

        StorageReference storageRef = FirebaseStorage.getInstance().getReference()
                .child("Products")
                .child(cartDTO.getFirebaseID())
                .child(cartDTO.getCropName());

        loadImage(storageRef, holder);

    }

    private void loadImage(StorageReference storageRef, EachItemAdapterHolder holder) {
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
        return cartDTOList.size();
    }

    public class EachItemAdapterHolder extends RecyclerView.ViewHolder{
        TextView cropName, priceLabel;
        TextView quantity;
        ImageView cropProfile;
        public EachItemAdapterHolder(@NonNull View itemView) {
            super(itemView);
            cropName = itemView.findViewById(R.id.cropNameLabel);
            priceLabel = itemView.findViewById(R.id.priceLabel);
            quantity = itemView.findViewById(R.id.quantityEditText);
            cropProfile = itemView.findViewById(R.id.cropProfile);
        }
    }
}
