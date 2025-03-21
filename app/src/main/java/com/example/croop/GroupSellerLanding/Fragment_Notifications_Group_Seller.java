package com.example.croop.GroupSellerLanding;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.croop.NotificationAdapter;
import com.example.croop.R;
import com.example.croop.model.GroupSellers;
import com.example.croop.model.GroupSellersProductsInventory;
import com.example.croop.model.Notifications;
import com.example.croop.retrofit.RetrofitService;
import com.example.croop.retrofit.UserAPI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Notifications_Group_Seller extends Fragment {
    private NotificationAdapter adapter;
    private RetrofitService RetrofitClient;
    private UserAPI userAPI;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private RecyclerView recyclerView;

    public Fragment_Notifications_Group_Seller(){

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.notification_group_seller, container, false);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        String firebaseID = user.getUid();

        recyclerView = view.findViewById(R.id.notificationList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        userAPI = RetrofitClient.getClient().create(UserAPI.class);
        Call<List<GroupSellersProductsInventory>> productsInventoryCall = userAPI.getProductsByFirebaseID(firebaseID);
        productsInventoryCall.enqueue(new Callback<List<GroupSellersProductsInventory>>() {
            @Override
            public void onResponse(Call<List<GroupSellersProductsInventory>> call, Response<List<GroupSellersProductsInventory>> response) {
                List<GroupSellersProductsInventory> products = response.body();
                for(GroupSellersProductsInventory product : products){
                    int productID = product.getId();
                    Call<String> shelfLifeCall = userAPI.checkShelfLife(productID);
                    shelfLifeCall.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            Log.e("Notification", response.body());
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Log.e("Notification", t.getMessage());
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<GroupSellersProductsInventory>> call, Throwable t) {
                Log.e("Notification", t.getMessage());
            }
        });

        Call<GroupSellers> groupSellersCall = userAPI.getGroupSellersbyFirebaseID(firebaseID);
        groupSellersCall.enqueue(new Callback<GroupSellers>() {
            @Override
            public void onResponse(Call<GroupSellers> call, Response<GroupSellers> response) {
                if(response.body() != null && response.isSuccessful()){
                    GroupSellers groupSellers = response.body();
                    int sellerID = groupSellers.getId();
                    Call<List<Notifications>>notificationCall = userAPI.getNotificationOfUser(sellerID, "Group Seller");
                    notificationCall.enqueue(new Callback<List<Notifications>>() {
                        @Override
                        public void onResponse(Call<List<Notifications>> call, Response<List<Notifications>> response) {
                            List<Notifications> notifications = response.body();
                            showNotifications(notifications);
                        }

                        @Override
                        public void onFailure(Call<List<Notifications>> call, Throwable t) {
                            Log.e("Notification", t.getMessage());
                        }
                    });
                }else{
                    Log.e("Getting Group Seller Error: ", response.message());
                }
            }

            @Override
            public void onFailure(Call<GroupSellers> call, Throwable t) {
                Log.e("Getting Group Seller Error: ", t.getMessage());
            }
        });

        return view;
    }

    private void showNotifications(List<Notifications> notifications) {
        adapter = new NotificationAdapter(notifications);
        recyclerView.setAdapter(adapter);
    }

}
