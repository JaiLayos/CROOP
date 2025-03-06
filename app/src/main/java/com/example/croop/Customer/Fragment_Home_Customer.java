package com.example.croop.Customer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.croop.R;
import com.example.croop.model.DiscountDTO;
import com.example.croop.model.FeaturedSellersDTO;
import com.example.croop.model.ProductDTO;
import com.example.croop.retrofit.RetrofitService;
import com.example.croop.retrofit.UserAPI;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Home_Customer extends Fragment {
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private RetrofitService RetrofitClient;
    private AtomicInteger apiCounter;
    private RecyclerView recyclerViewForPromos,recyclerViewForInSeason, recyclerViewForInDemand,
        recyclerViewForFeaturedSellers;
    private UserAPI userAPI;
    private List<DiscountDTO> forCustomers;
    private List<ProductDTO> productProfile, inSeason, inDemand;
    private List<FeaturedSellersDTO> featuredSellers;

    public Fragment_Home_Customer() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_customer, container, false);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        SharedPreferences prefs = getActivity().getSharedPreferences("user_prefs", getContext().MODE_PRIVATE);
        String role = prefs.getString("user_role", null);

        FirebaseUser user = mAuth.getCurrentUser();
        String collection = getCollection(role);

        recyclerViewForPromos = view.findViewById(R.id.promoList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewForPromos.setLayoutManager(layoutManager);

        recyclerViewForInSeason = view.findViewById(R.id.inSeasonList);
        LinearLayoutManager layoutManager_1 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewForInSeason.setLayoutManager(layoutManager_1);

        recyclerViewForInDemand = view.findViewById(R.id.inDemandList);
        LinearLayoutManager layoutManager_2 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewForInDemand.setLayoutManager(layoutManager_2);

        recyclerViewForFeaturedSellers = view.findViewById(R.id.featuredList);
        LinearLayoutManager layoutManager_3 = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerViewForFeaturedSellers.setLayoutManager(layoutManager_3);

        userAPI = RetrofitClient.getClient().create(UserAPI.class);
        forCustomers = new ArrayList<>();
        productProfile = new ArrayList<>();
        apiCounter = new AtomicInteger(0);

        Call<List<DiscountDTO>> individualDiscounts = userAPI.getAllIndividualDiscounts();
        individualDiscounts.enqueue(new Callback<List<DiscountDTO>>() {
            @Override
            public void onResponse(Call<List<DiscountDTO>> call, Response<List<DiscountDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    for (DiscountDTO item : response.body()) {
                        if (item.getDiscountPercent() > 0.0) {
                            forCustomers.add(item);
                            ProductDTO productDTO = new ProductDTO();
                            productDTO = translateaToProductDTO(productDTO, item);
                            productProfile.add(productDTO);
                        }
                    }
                    addGroupSellersDiscount(forCustomers);
                } else {
                    Log.e("RetrofitAPI", "Error fetching individual discounts: " + response.code());
                }

            }

            @Override
            public void onFailure(Call<List<DiscountDTO>> call, Throwable t) {
                Log.e("RetrofitAPI", "Error fetching individual discounts: " + t.getMessage());
            }
        });

        inSeason = new ArrayList<>();
        Call<List<ProductDTO>> inSeasonGroup = userAPI.getInSeasonGroupProducts();
        inSeasonGroup.enqueue(new Callback<List<ProductDTO>>() {
            @Override
            public void onResponse(Call<List<ProductDTO>> call, Response<List<ProductDTO>> response) {
                if(response.isSuccessful()){
                    for(ProductDTO product: response.body()){
                        inSeason.add(product);
                    }
                    addIndividualInSeason(inSeason);
                }else{
                    Log.e("RetrofitAPI", "Error fetching individual discounts: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<ProductDTO>> call, Throwable t) {
                Log.e("RetrofitAPI", "Error: " + t.getMessage());
            }
        });

        inDemand = new ArrayList<>();
        Call<List<ProductDTO>> inDemandGroup = userAPI.getInDemandGroupProducts();
        inDemandGroup.enqueue(new Callback<List<ProductDTO>>() {
            @Override
            public void onResponse(Call<List<ProductDTO>> call, Response<List<ProductDTO>> response) {
                if(response.isSuccessful()){
                    for(ProductDTO productDTO : response.body()){
                        inDemand.add(productDTO);
                    }
                    addIndividualInDemand(inDemand);
                }else{
                    Log.e("RetrofitAPI", "Error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<ProductDTO>> call, Throwable t) {
                Log.e("RetrofitAPI", "Error: " + t.getMessage());
            }
        });

        featuredSellers = new ArrayList<>();
        Call<List<FeaturedSellersDTO>> groupSellersCall = userAPI.getFeaturedGroup();
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

        TextView da, cda;

        da = view.findViewById(R.id.daText);
        da.setOnClickListener(v -> {
            String userId = "521426187938826"; // Replace with the actual user ID
            openMessenger(userId);
        });

        cda = view.findViewById(R.id.cdaText);
        cda.setOnClickListener(v -> {
            String userId = "406419702548229"; // Replace with the actual user ID
            openMessenger(userId);
        });

        return view;
    }

    private FeaturedSellersDTO addFeaturedSellers(FeaturedSellersDTO featuredSellersDTO, FeaturedSellersDTO sellers) {
        featuredSellersDTO.setId(sellers.getId());
        featuredSellersDTO.setName(sellers.getName());
        featuredSellersDTO.setRole(sellers.getRole());
        return featuredSellersDTO;
    }

    private void featuredIndividualSellers(List<FeaturedSellersDTO> featuredSellers) {
        Call<List<FeaturedSellersDTO>> featuredIndividuals = userAPI.getFeaturedIndividual();
        featuredIndividuals.enqueue(new Callback<List<FeaturedSellersDTO>>() {
            @Override
            public void onResponse(Call<List<FeaturedSellersDTO>> call, Response<List<FeaturedSellersDTO>> response) {
                if(response.isSuccessful()){
                    for(FeaturedSellersDTO sellers : response.body()){
                        FeaturedSellersDTO featuredSellersDTO = new FeaturedSellersDTO();
                        featuredSellersDTO = addFeaturedSellers(featuredSellersDTO, sellers);
                        featuredSellers.add(featuredSellersDTO);
                    }
                    FeaturedSellerAdapter adapter = new FeaturedSellerAdapter(getContext(), featuredSellers, new FeaturedSellerAdapter.OnItemClickListener() {
                        @Override
                        public void onViewProfileClick(FeaturedSellersDTO featuredSellersDTO) {
                            Toast.makeText(getContext(), "View Profile: " + featuredSellersDTO.getName(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onViewProductsClick(FeaturedSellersDTO featuredSellersDTO) {
                            Toast.makeText(getContext(), "View Products: " + featuredSellersDTO.getName(), Toast.LENGTH_SHORT).show();
                        }
                    });
                    recyclerViewForFeaturedSellers.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<FeaturedSellersDTO>> call, Throwable t) {
                Log.e("RetrofitAPI", "Error: " + t.getMessage());
            }
        });

    }

    private ProductDTO translateaToProductDTO(ProductDTO productDTO, DiscountDTO item) {
        productDTO.setProductID(item.getProductID());
        productDTO.setProductName(item.getItemName());
        productDTO.setProductPrice(item.getOriginalPrice());
        productDTO.setProductSeller(item.getSellerName());
        productDTO.setSellerRole(item.getSellerRole());
        productDTO.setProductDiscount(item.getDiscountPercent());
        productDTO.setProductFinalPrice(item.getSalePrice());
        return productDTO;
    }

    private void addIndividualInDemand(List<ProductDTO> inDemand) {
        Call<List<ProductDTO>> inDemandIndividual = userAPI.getInDemandIndividualProducts();
        inDemandIndividual.enqueue(new Callback<List<ProductDTO>>() {
            @Override
            public void onResponse(Call<List<ProductDTO>> call, Response<List<ProductDTO>> response) {
                if(response.isSuccessful()){
                    for(ProductDTO productDTO : response.body()){
                        inDemand.add(productDTO);
                    }
                    InDemandAdapter adapter = new InDemandAdapter(getContext(),inDemand, productDTO -> {
                        showProduct(productDTO);
                    });
                    recyclerViewForInDemand.setAdapter(adapter);
                }else{
                    Log.e("RetrofitAPI", "Error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<ProductDTO>> call, Throwable t) {
                Log.e("RetrofitAPI", "Error fetching group discounts: " + t.getMessage());
            }
        });
    }

    private void addIndividualInSeason(List<ProductDTO> inSeason) {
        Call<List<ProductDTO>> products = userAPI.getInSeasonIndividualProducts();
        products.enqueue(new Callback<List<ProductDTO>>() {
            @Override
            public void onResponse(Call<List<ProductDTO>> call, Response<List<ProductDTO>> response) {
                if(response.isSuccessful()){
                    for(ProductDTO products : response.body()){
                        inSeason.add(products);
                    }
                    InSeasonAdapter adapter = new InSeasonAdapter(getContext(), inSeason, productDTO -> {
                        showProduct(productDTO);
                    });
                    recyclerViewForInSeason.setAdapter(adapter);
                }else{
                    Log.e("RetrofitAPI", "Error fetching individual discounts: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<ProductDTO>> call, Throwable t) {
                Toast.makeText(getActivity(), "Error: "+ t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addGroupSellersDiscount(List<DiscountDTO> forCustomers) {
        Call<List<DiscountDTO>> groupDiscounts = userAPI.getAllDiscounts();
        groupDiscounts.enqueue(new Callback<List<DiscountDTO>>() {
            @Override
            public void onResponse(Call<List<DiscountDTO>> call, Response<List<DiscountDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    for (DiscountDTO item : response.body()) {
                        if (item.getDiscountPercent() > 0.0) {
                            forCustomers.add(item);
                            ProductDTO productDTO = new ProductDTO();
                            productDTO = translateaToProductDTO(productDTO,item);
                            productProfile.add(productDTO);
                        }
                    }
                    PromoAdapter adapter = new PromoAdapter(getContext(), forCustomers, productProfile, productDTO -> {
                        showBottomSheetDialog(productDTO);
                    });
                    recyclerViewForPromos.setAdapter(adapter);
                } else {
                    Log.e("RetrofitAPI", "Error fetching group discounts: " + response.code());
                }

            }

            @Override
            public void onFailure(Call<List<DiscountDTO>> call, Throwable t) {
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

    private void showBottomSheetDialog(ProductDTO productDTO) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
        View bottomSheetView = getLayoutInflater().inflate(R.layout.home_customer_preview_discount, null);

        TextView itemName = bottomSheetView.findViewById(R.id.nameText);
        TextView originalPrice = bottomSheetView.findViewById(R.id.priceText);
        TextView discountPercent = bottomSheetView.findViewById(R.id.discountText);
        TextView finalPrice = bottomSheetView.findViewById(R.id.saleText);
        TextView seller = bottomSheetView.findViewById(R.id.sellerText);

        Button seeProduct = bottomSheetView.findViewById(R.id.seeProductButton);
        seeProduct.setOnClickListener(v -> {
            showProduct(productDTO);
        });

        // Set data to views
        itemName.setText(productDTO.getProductName());
        originalPrice.setText(String.valueOf(productDTO.getProductPrice()));
        discountPercent.setText(String.valueOf(productDTO.getProductDiscount()));
        finalPrice.setText(String.valueOf(productDTO.getProductFinalPrice()));
        seller.setText(productDTO.getProductSeller());

        // Show the bottom sheet dialog
        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }

    private void showProduct(ProductDTO productDTO) {
        int id = productDTO.getProductID();
        String kindOfSeller = productDTO.getSellerRole();
        Intent intent = new Intent(getActivity(), Activity_Product_Profile.class);
        intent.putExtra("product_id", id);
        intent.putExtra("seller", kindOfSeller);
        startActivity(intent);
    }

    private void openMessenger(String userId) {
        try {
            // Try to open Messenger app using its URI scheme
            String messengerUri = "fb-messenger://user-thread/" + userId;
            Intent intent = new Intent(Intent.ACTION_VIEW, android.net.Uri.parse(messengerUri));
            startActivity(intent);
        } catch (Exception e) {
            // Fallback to web URL if Messenger app is not installed
            String fallbackUrl = "https://www.facebook.com/messages/t/" + userId;
            Intent intent = new Intent(Intent.ACTION_VIEW, android.net.Uri.parse(fallbackUrl));
            startActivity(intent);
        }
    }
}
