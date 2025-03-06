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

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.croop.R;
import com.example.croop.model.ProductDTO;
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

public class Fragment_Products_Customer extends Fragment {
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private RecyclerView recyclerView;
    private List<ProductDTO> productDTOList, searchList;
    private RetrofitService RetrofitClient;
    private UserAPI userAPI;
    private int id;
    private String seller;


    public Fragment_Products_Customer() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.products_customer_list, container, false);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        SharedPreferences prefs = getActivity().getSharedPreferences("user_prefs", getContext().MODE_PRIVATE);
        String role = prefs.getString("user_role", null);

        recyclerView = view.findViewById(R.id.productList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        productDTOList = new ArrayList<>();

        userAPI = RetrofitClient.getClient().create(UserAPI.class);
        Call<List<ProductDTO>> groupProductCall = userAPI.getAllProducts();
        groupProductCall.enqueue(new Callback<List<ProductDTO>>() {
            @Override
            public void onResponse(Call<List<ProductDTO>> call, Response<List<ProductDTO>> response) {
                if(response.isSuccessful()){
                    for(ProductDTO products : response.body()){
                        if(products.getRemaining()>0){
                            productDTOList.add(products);
                        }
                    }
                    addIndividualProducts(productDTOList, role);
                }else{
                    Log.e("RetrofitAPI", "Error fetching group discounts: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<ProductDTO>> call, Throwable t) {
                Log.e("RetrofitAPI", "Error fetching group discounts: " + t.getMessage());
            }
        });

        EditText searchText;
        searchText = view.findViewById(R.id.searchText);

        searchList = new ArrayList<>();

        Button search = view.findViewById(R.id.searchButton);
        search.setOnClickListener(v -> {
            String searched = searchText.getText().toString();
            if(searchText == null){
                addToList();
            }else{
                Call<List<ProductDTO>> group = userAPI.getGroupProductDTOByName(searched);
                group.enqueue(new Callback<List<ProductDTO>>() {
                    @Override
                    public void onResponse(Call<List<ProductDTO>> call, Response<List<ProductDTO>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            searchList.addAll(response.body()); // Use addAll() instead of loop
                            checkIndividualSellers(searchList, searched);
                        } else {
                            addToList();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<ProductDTO>> call, Throwable t) {
                        Log.e("RetrofitAPI", "Error fetching group discounts: " + t.getMessage());
                    }
                });
            }
        });

        return view;
    }

    private void addIndividualProducts(List<ProductDTO> productDTOList, String role) {
        Call<List<ProductDTO>> individualProductsCall = userAPI.getAllIndividualProducts();
        individualProductsCall.enqueue(new Callback<List<ProductDTO>>() {
            @Override
            public void onResponse(Call<List<ProductDTO>> call, Response<List<ProductDTO>> response) {
                for(ProductDTO individualSellersProducts : response.body()){
                    if(individualSellersProducts.getRemaining()>0){
                        productDTOList.add(individualSellersProducts);
                    }
                    addToList();
                }
            }

            @Override
            public void onFailure(Call<List<ProductDTO>> call, Throwable t) {
                Log.e("RetrofitAPI", "Error fetching group discounts: " + t.getMessage());
            }
        });
    }

    private void addToList() {
        ProductListAdapter adapter = new ProductListAdapter(getContext(), productDTOList, new ProductListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ProductDTO productDTO) {
                showProduct(productDTO);
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private void showProduct(ProductDTO productDTO) {
        int id = productDTO.getProductID();
        String kindOfSeller = productDTO.getSellerRole();
        Intent intent = new Intent(getActivity(), Activity_Product_Profile.class);
        intent.putExtra("product_id", id);
        intent.putExtra("seller", kindOfSeller);
        startActivity(intent);
    }

    private void checkIndividualSellers(List<ProductDTO> searchList, String searched) {
        Call<List<ProductDTO>> individualProducts = userAPI.getIndividualProductDTOByName(searched);
        individualProducts.enqueue(new Callback<List<ProductDTO>>() {
            @Override
            public void onResponse(Call<List<ProductDTO>> call, Response<List<ProductDTO>> response) {
                for(ProductDTO productDTO : response.body()){
                    searchList.add(productDTO);
                }
                ProductListAdapter adapter = new ProductListAdapter(getContext(), searchList, new ProductListAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(ProductDTO productDTO) {
                        int id = productDTO.getProductID();
                        String kindOfSeller = productDTO.getSellerRole();
                        Intent intent = new Intent(getActivity(), Activity_Product_Profile.class);
                        intent.putExtra("product_id", id);
                        intent.putExtra("seller", kindOfSeller);
                        startActivity(intent);
                    }
                });
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<ProductDTO>> call, Throwable t) {
                Log.e("RetrofitAPI", "Error fetching group discounts: " + t.getMessage());
            }
        });
    }



    private String getCollection(String role) {
        SharedPreferences prefs = getActivity().getSharedPreferences("user_prefs", getContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        String collection;

        switch (role) {
            case "Group Business User (Association)":
                collection = "Farming Association";
                break;
            case "Group Business User (Cooperative)":
                collection = "Farming Cooperatives";
                break;
            case "Individual Business User":
                collection = "Individual Sellers";
                break;
            case "Individual Customer User":
                collection = "Customers";
                break;
            case "Group Customer User":
                collection = "Group Customers";
                break;
            default:
                collection = "Unknown";
                break;
        }

        editor.putString("user_collection", collection).apply();
        return collection;
    }
}
