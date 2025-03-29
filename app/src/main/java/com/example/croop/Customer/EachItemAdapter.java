package com.example.croop.Customer;

import android.content.Context;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.croop.R;
import com.example.croop.model.CartDTO;
import com.example.croop.retrofit.RetrofitService;
import com.example.croop.retrofit.UserAPI;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EachItemAdapter extends RecyclerView.Adapter<EachItemAdapter.EachItemAdapterHolder> {
    private Context context;
    private List<CartDTO> cartDTOList;
    private RetrofitService RetrofitClient;
    private UserAPI userAPI;
    private FragmentActivity fragmentActivity;
    private OnUpdateClickListener updateListener;

    public interface OnUpdateClickListener {
        void onUpdateClicked(CartDTO cartItem);
    }

    public EachItemAdapter(Context context, List<CartDTO> cartDTOList, FragmentActivity fragmentActivity, OnUpdateClickListener updateListener) {
        this.context = context;
        this.cartDTOList = cartDTOList;
        this.fragmentActivity = fragmentActivity;
        this.updateListener = updateListener;
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
        holder.priceLabel.setText("₱" + String.valueOf(cartDTO.getPrice()) + " + " + "\n" + String.format("%.0f", cartDTO.getPrice() * 0.01));
        StorageReference storageRef = FirebaseStorage.getInstance().getReference()
                .child("Products")
                .child(cartDTO.getFirebaseID())
                .child(cartDTO.getCropName());

        loadImage(storageRef, holder);
        holder.update.setOnClickListener(v -> {
            if (updateListener != null) {
                updateListener.onUpdateClicked(cartDTO);
            }
        });

        holder.delete.setOnClickListener(v -> {
            Call<Void> cartCall = userAPI.deleteCart(cartDTO.getId());
            cartCall.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    Toast.makeText(v.getContext(), "Item Deleted", Toast.LENGTH_SHORT).show();
                    Fragment parentFragment = fragmentActivity.getSupportFragmentManager()
                            .findFragmentById(R.id.viewPagerContainer);
                    if (parentFragment instanceof Fragment_Cart_Customer) {
                        ((Fragment_Cart_Customer) parentFragment).refreshCartData();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(v.getContext(), "Item Deletion Error" + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

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
        Button update, delete;
        public EachItemAdapterHolder(@NonNull View itemView) {
            super(itemView);
            cropName = itemView.findViewById(R.id.cropNameLabel);
            priceLabel = itemView.findViewById(R.id.priceLabel);
            quantity = itemView.findViewById(R.id.quantityEditText);
            cropProfile = itemView.findViewById(R.id.cropProfile);
            userAPI = RetrofitClient.getClient().create(UserAPI.class);
            update = itemView.findViewById(R.id.updateButton);
            delete = itemView.findViewById(R.id.deleteButton);
        }
    }
}
