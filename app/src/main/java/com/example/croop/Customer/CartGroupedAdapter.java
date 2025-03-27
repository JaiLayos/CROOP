package com.example.croop.Customer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.croop.R;
import com.example.croop.model.CartGroupedResponseDTO;
import com.example.croop.model.GroupSellerCartDTO;
import com.example.croop.model.IndividualSellerCartDTO;

import java.util.ArrayList;
import java.util.List;

public class CartGroupedAdapter extends RecyclerView.Adapter<CartGroupedAdapter.CartGroupedAdapterHolder> {
    private static Context context;
    private List<GroupSellerCartDTO> groupSellers = new ArrayList<>();
    private List<IndividualSellerCartDTO> individualSellers = new ArrayList<>();
    private static FragmentActivity fragmentActivity;
    private OnItemClickListener listener;


    // Listener now receives seller ID and type
    public interface OnItemClickListener {
        void onCheckout(int sellerId, boolean isGroupSeller);
    }

    public CartGroupedAdapter(Context context, CartGroupedResponseDTO dto, FragmentActivity fragmentActivity, OnItemClickListener listener) {
        this.context = context;
        this.listener = listener;
        this.fragmentActivity = fragmentActivity;
        this.groupSellers = dto.getGroupSellers() != null
                ? dto.getGroupSellers()
                : new ArrayList<>();
        this.individualSellers = dto.getIndividualSellers() != null
                ? dto.getIndividualSellers()
                : new ArrayList<>();
    }

    @NonNull
    @Override
    public CartGroupedAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.customer_cart_grouped_by_seller, parent, false);
        return new CartGroupedAdapterHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartGroupedAdapterHolder holder, int position) {
        if (position < groupSellers.size()) {
            GroupSellerCartDTO group = groupSellers.get(position);
            holder.bindGroup(group, listener);
        } else {
            int adjustedPosition = position - groupSellers.size();
            IndividualSellerCartDTO individual = individualSellers.get(adjustedPosition);
            holder.bindIndividual(individual, listener);
        }
    }

    @Override
    public int getItemCount() {
        return groupSellers.size() + individualSellers.size();
    }

    // ViewHolder with proper click handling
    public static class CartGroupedAdapterHolder extends RecyclerView.ViewHolder {
        TextView sellerName;
        RecyclerView recyclerView;
        Button checkout;

        public CartGroupedAdapterHolder(@NonNull View itemView) {
            super(itemView);
            sellerName = itemView.findViewById(R.id.sellerNameText);
            recyclerView = itemView.findViewById(R.id.eachItemRecycler);
            checkout = itemView.findViewById(R.id.checkoutButton);
        }

        public void bindGroup(GroupSellerCartDTO group, OnItemClickListener listener) {
            sellerName.setText(group.getGroupName());

            // Setup child RecyclerView
            EachItemAdapter adapter = new EachItemAdapter(context, group.getCartItems(), fragmentActivity);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(adapter);

            checkout.setOnClickListener(v ->
                    listener.onCheckout(group.getId(), true)
            ); // true = group seller

        }

        public void bindIndividual(IndividualSellerCartDTO individual, OnItemClickListener listener) {
            sellerName.setText(individual.getName());

            // Setup child RecyclerView
            EachItemAdapter adapter = new EachItemAdapter(context, individual.getCartItems(), fragmentActivity);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(adapter);

            // Set click listener
            checkout.setOnClickListener(v ->
                    listener.onCheckout(individual.getId(), false)); // false = individual seller
        }
    }
}
