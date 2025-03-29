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
import com.example.croop.model.FeaturedSellersDTO;
import com.example.croop.model.GroupSellerCartDTO;
import com.example.croop.model.GroupSellers;
import com.example.croop.model.IndividualSellerCartDTO;
import com.example.croop.model.IndividualSellers;
import com.example.croop.retrofit.RetrofitService;
import com.example.croop.retrofit.UserAPI;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartGroupedAdapter extends RecyclerView.Adapter<CartGroupedAdapter.CartGroupedAdapterHolder> {
    private static Context context;
    private List<GroupSellerCartDTO> groupSellers = new ArrayList<>();
    private List<IndividualSellerCartDTO> individualSellers = new ArrayList<>();
    private static FragmentActivity fragmentActivity;
    private OnItemClickListener listener;
    private RetrofitService RetrofitClient;
    private UserAPI userAPI;
    private static EachItemAdapter.OnUpdateClickListener updateListener;

    public interface OnItemClickListener {
        void onCheckout(int sellerId, boolean isGroupSeller);
    }

    public void setUpdateListener(EachItemAdapter.OnUpdateClickListener updateListener) {
        this.updateListener = updateListener;
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
    public class CartGroupedAdapterHolder extends RecyclerView.ViewHolder {
        TextView sellerName, sellerLocation;
        RecyclerView recyclerView;
        Button checkout;


        public CartGroupedAdapterHolder(@NonNull View itemView) {
            super(itemView);
            sellerName = itemView.findViewById(R.id.sellerNameText);
            sellerLocation = itemView.findViewById(R.id.sellerLocationText);
            recyclerView = itemView.findViewById(R.id.eachItemRecycler);
            checkout = itemView.findViewById(R.id.checkoutButton);
            userAPI = RetrofitClient.getClient().create(UserAPI.class);
        }

        public void bindGroup(GroupSellerCartDTO group, OnItemClickListener listener) {
            sellerName.setText(group.getGroupName());
            Call<List<FeaturedSellersDTO>> featuredSellersDTOS = userAPI.getGroupSellerByGroupName(group.getGroupName());
            featuredSellersDTOS.enqueue(new Callback<List<FeaturedSellersDTO>>() {
                @Override
                public void onResponse(Call<List<FeaturedSellersDTO>> call, Response<List<FeaturedSellersDTO>> response) {
                    List<FeaturedSellersDTO> featuredSellersDTOs = response.body();
                    for(FeaturedSellersDTO featuredSellersDTO : featuredSellersDTOs){
                        int id = featuredSellersDTO.getId();
                        Call<GroupSellers> groupSellersCall = userAPI.getGroupSellers(id);
                        groupSellersCall.enqueue(new Callback<GroupSellers>() {
                            @Override
                            public void onResponse(Call<GroupSellers> call, Response<GroupSellers> response) {
                                GroupSellers groupSellers = response.body();
                                String address = formatAddress(groupSellers.getAddress());
                                sellerLocation.setText("Seller Location: " + address);
                            }

                            @Override
                            public void onFailure(Call<GroupSellers> call, Throwable t) {

                            }
                        });
                    }
                }

                @Override
                public void onFailure(Call<List<FeaturedSellersDTO>> call, Throwable t) {

                }
            });
            EachItemAdapter adapter = new EachItemAdapter(context, group.getCartItems(), fragmentActivity, updateListener);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(adapter);

            checkout.setOnClickListener(v ->
                    listener.onCheckout(group.getId(), true)
            );
        }

        public void bindIndividual(IndividualSellerCartDTO individual, OnItemClickListener listener) {
            sellerName.setText(individual.getName());
            Call<List<FeaturedSellersDTO>> featuredSellersDTOS = userAPI.findIndividualSellerByName(individual.getName());
            featuredSellersDTOS.enqueue(new Callback<List<FeaturedSellersDTO>>() {
                @Override
                public void onResponse(Call<List<FeaturedSellersDTO>> call, Response<List<FeaturedSellersDTO>> response) {
                    List<FeaturedSellersDTO> featuredSellersDTOs = response.body();
                    for(FeaturedSellersDTO featuredSellersDTO : featuredSellersDTOs){
                        int id = featuredSellersDTO.getId();
                        Call<IndividualSellers> groupSellersCall = userAPI.getIndividualSellers(id);
                        groupSellersCall.enqueue(new Callback<IndividualSellers>() {
                            @Override
                            public void onResponse(Call<IndividualSellers> call, Response<IndividualSellers> response) {
                                IndividualSellers groupSellers = response.body();
                                String address = formatAddress(groupSellers.getAddress());
                                sellerLocation.setText("Seller Location: " +address);
                            }

                            @Override
                            public void onFailure(Call<IndividualSellers> call, Throwable t) {

                            }
                        });
                    }
                }

                @Override
                public void onFailure(Call<List<FeaturedSellersDTO>> call, Throwable t) {

                }
            });
            EachItemAdapter adapter = new EachItemAdapter(context, individual.getCartItems(), fragmentActivity, updateListener);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(adapter);

            checkout.setOnClickListener(v ->
                    listener.onCheckout(individual.getId(), false)
            );
        }

        private String formatAddress(Map<String, String> address) {
            try {
                StringBuilder formattedList = new StringBuilder();
                for (Map.Entry<String, String> entry : address.entrySet()) {
                    formattedList.append(entry.getValue())
                            .append(", ");
                }
                return formattedList.toString().trim();
            } catch (Exception e) {
                e.printStackTrace();
                return "Error parsing order list";
            }
        }
    }
}
