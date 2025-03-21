package com.example.croop.Customer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.croop.R;
import com.example.croop.model.FeaturedSellersDTO;
import com.example.croop.retrofit.RetrofitService;
import com.example.croop.retrofit.UserAPI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Seller_Customer extends Fragment {
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private RecyclerView recyclerView;
    private List<FeaturedSellersDTO> featuredSellers, allSellers;
    private RetrofitService RetrofitClient;
    private UserAPI userAPI;

    public Fragment_Seller_Customer() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.customer_seller_list, container, false);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        SharedPreferences prefs = getActivity().getSharedPreferences("user_prefs", getContext().MODE_PRIVATE);
        String role = prefs.getString("user_role", null);

        recyclerView = view.findViewById(R.id.sellerList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        userAPI = RetrofitClient.getClient().create(UserAPI.class);
        showTable();

        EditText searchText;
        searchText = view.findViewById(R.id.searchText);

        Button search = view.findViewById(R.id.searchButton);
        search.setOnClickListener(v -> {
            featuredSellers = new ArrayList<>();
            String searched = searchText.getText().toString().trim();
            if(searched.isEmpty()){
                showTable();
            }else{
                featuredSellers = new ArrayList<>();
                Call<List<FeaturedSellersDTO>> group = userAPI.getGroupSellerByGroupName(searched);
                group.enqueue(new Callback<List<FeaturedSellersDTO>>() {
                    @Override
                    public void onResponse(Call<List<FeaturedSellersDTO>> call, Response<List<FeaturedSellersDTO>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            for(FeaturedSellersDTO sellers : response.body()){
                                FeaturedSellersDTO featuredSellersDTO = new FeaturedSellersDTO();
                                featuredSellersDTO = addFeaturedSellers(featuredSellersDTO, sellers);
                                featuredSellers.add(featuredSellersDTO);
                            }
                            findIndividualSellers(featuredSellers, searched);
                        } else {
                            addToList();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<FeaturedSellersDTO>> call, Throwable t) {
                        Log.e("RetrofitAPI", "Error fetching group discounts: " + t.getMessage());
                    }
                });
            }
        });

        return view;
    }

    private void showTable() {
        featuredSellers = new ArrayList<>();
        Call<List<FeaturedSellersDTO>> groupSellersCall = userAPI.getAllGroupSellers();
        groupSellersCall.enqueue(new Callback<List<FeaturedSellersDTO>>() {
            @Override
            public void onResponse(Call<List<FeaturedSellersDTO>> call, Response<List<FeaturedSellersDTO>> response) {
                if(response.isSuccessful()){
                    for(FeaturedSellersDTO sellers : response.body()){
                        FeaturedSellersDTO featuredSellersDTO = new FeaturedSellersDTO();
                        featuredSellersDTO = addFeaturedSellers(featuredSellersDTO, sellers);
                        featuredSellers.add(featuredSellersDTO);
                    }
                    featuredIndividualSellers(featuredSellers);
                }else{
                    Log.e("RetrofitAPI", "Error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<FeaturedSellersDTO>> call, Throwable t) {
                Log.e("RetrofitAPI", "Error: " + t.getMessage());
            }
        });
    }

    private void findIndividualSellers(List<FeaturedSellersDTO> featuredSellers, String searched) {
        Call<List<FeaturedSellersDTO>> featuredIndividuals = userAPI.findIndividualSellerByName(searched);
        featuredIndividuals.enqueue(new Callback<List<FeaturedSellersDTO>>() {
            @Override
            public void onResponse(Call<List<FeaturedSellersDTO>> call, Response<List<FeaturedSellersDTO>> response) {
                if(response.isSuccessful()){
                    for(FeaturedSellersDTO sellers : response.body()){
                        FeaturedSellersDTO featuredSellersDTO = new FeaturedSellersDTO();
                        featuredSellersDTO = addFeaturedSellers(featuredSellersDTO, sellers);
                        featuredSellers.add(featuredSellersDTO);
                    }
                    addToList();
                }
            }

            @Override
            public void onFailure(Call<List<FeaturedSellersDTO>> call, Throwable t) {
                Log.e("RetrofitAPI", "Error: " + t.getMessage());
            }
        });
    }

    private void addToList() {
        SellerListAdapter adapter = new SellerListAdapter(getContext(), featuredSellers, new FeaturedSellerAdapter.OnItemClickListener() {
            @Override
            public void onViewProfileClick(FeaturedSellersDTO featuredSellersDTO) {
                Intent intent = new Intent(getActivity(),Activity_Seller_Profile.class);
                String kindOfSeller = featuredSellersDTO.getRole();
                String firebaseID = featuredSellersDTO.getFirebaseID();
                int id = featuredSellersDTO.getId();
                intent.putExtra("seller", kindOfSeller);
                intent.putExtra("seller_id", id);
                intent.putExtra("firebase_id", firebaseID);
                startActivity(intent);
            }

            @Override
            public void onViewProductsClick(FeaturedSellersDTO featuredSellersDTO) {
                Toast.makeText(getContext(), "View Products: " + featuredSellersDTO.getName(), Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private void featuredIndividualSellers(List<FeaturedSellersDTO> featuredSellers) {
        Call<List<FeaturedSellersDTO>> featuredIndividuals = userAPI.getAllIndividualSellersDTO();
        featuredIndividuals.enqueue(new Callback<List<FeaturedSellersDTO>>() {
            @Override
            public void onResponse(Call<List<FeaturedSellersDTO>> call, Response<List<FeaturedSellersDTO>> response) {
                if(response.isSuccessful()){
                    for(FeaturedSellersDTO sellers : response.body()){
                        FeaturedSellersDTO featuredSellersDTO = new FeaturedSellersDTO();
                        featuredSellersDTO = addFeaturedSellers(featuredSellersDTO, sellers);
                        featuredSellers.add(featuredSellersDTO);
                    }
                    addToList();
                }
            }

            @Override
            public void onFailure(Call<List<FeaturedSellersDTO>> call, Throwable t) {
                Log.e("RetrofitAPI", "Error: " + t.getMessage());
            }
        });
    }

    private FeaturedSellersDTO addFeaturedSellers(FeaturedSellersDTO featuredSellersDTO, FeaturedSellersDTO sellers) {
        featuredSellersDTO.setId(sellers.getId());
        featuredSellersDTO.setName(sellers.getName());
        featuredSellersDTO.setRole(sellers.getRole());
        featuredSellersDTO.setFirebaseID(sellers.getFirebaseID());
        return featuredSellersDTO;
    }
}
