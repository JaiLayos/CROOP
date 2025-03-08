package com.example.croop.Customer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.croop.R;
import com.example.croop.model.Cart;
import com.example.croop.model.Customer;
import com.example.croop.model.GroupSellers;
import com.example.croop.model.IndividualSellers;
import com.example.croop.model.ProductDTO;
import com.example.croop.retrofit.RetrofitService;
import com.example.croop.retrofit.UserAPI;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Activity_Product_Profile extends AppCompatActivity {

    private String seller, firebase_id;
    private int productID;
    private UserAPI userAPI;
    private RetrofitService retrofitService;
    private ImageView productProfile, sellerProfile;
    private TextView priceText, discountText, cropNameText,
        sellerNameText, sellerRoleText;
    private FloatingActionButton back;
    private Button addToCart;
    private Cart cart;
    private ProductDTO store;
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_profile);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        initializeComponents();
    }

    private void initializeComponents() {
        Intent intent = getIntent();
        if (intent != null) {
            productID = intent.getIntExtra("product_id", 1);
            seller = intent.getStringExtra("seller");
            firebase_id = intent.getStringExtra("firebase_id");
        }
        priceText = findViewById(R.id.priceText);
        discountText = findViewById(R.id.discountText);
        cropNameText = findViewById(R.id.cropNameText);
        sellerNameText = findViewById(R.id.sellerNameText);
        sellerRoleText = findViewById(R.id.sellerRoleText);

        productProfile = findViewById(R.id.productProfile);
        sellerProfile = findViewById(R.id.sellerProfile);

        back = findViewById(R.id.backButton);
        back.setOnClickListener(v -> {
            onBackPressed();
        });

        userAPI = retrofitService.getClient().create(UserAPI.class);
        layoutProfile(seller);
        layoutSuggestions(firebase_id);
    }

    private void layoutSuggestions(String firebase_id) {
        RecyclerView recyclerView = findViewById(R.id.suggestionList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        List<ProductDTO> suggestions = new ArrayList<>();
        if(seller != null){
            switch(seller){
                case "Group Business User (Association)":
                case "Group Business User (Cooperative)":
                    if(firebase_id != null){
                        Call<List<ProductDTO>> productsCall = userAPI.getGroupProductsDTOsByFirebaseID(firebase_id);
                        productsCall.enqueue(new Callback<List<ProductDTO>>() {
                            @Override
                            public void onResponse(Call<List<ProductDTO>> call, Response<List<ProductDTO>> response) {
                                for(ProductDTO productDTO : response.body()){
                                    suggestions.add(productDTO);
                                }
                                SuggestionsAdapter suggestionsAdapter = new SuggestionsAdapter(Activity_Product_Profile.this, suggestions, new SuggestionsAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(ProductDTO productDTO) {
                                        showProduct(productDTO);
                                    }
                                });
                                recyclerView.setAdapter(suggestionsAdapter);
                            }
                            @Override
                            public void onFailure(Call<List<ProductDTO>> call, Throwable t) {
                                Log.e("Suggestions Error: ", t.getMessage());
                            }
                        });
                    }else{
                        Log.e("Firebase", "Firebase is null");
                    }
                    break;
                case "Individual Business User":
                    if(firebase_id != null){
                        Call<List<ProductDTO>> productsCall = userAPI.getIndividualProductDTOsByFirebaseID(firebase_id);
                        productsCall.enqueue(new Callback<List<ProductDTO>>() {
                            @Override
                            public void onResponse(Call<List<ProductDTO>> call, Response<List<ProductDTO>> response) {
                                for(ProductDTO productDTO : response.body()){
                                    suggestions.add(productDTO);
                                }
                                SuggestionsAdapter suggestionsAdapter = new SuggestionsAdapter(Activity_Product_Profile.this, suggestions, new SuggestionsAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(ProductDTO productDTO) {
                                        showProduct(productDTO);
                                    }
                                });
                                recyclerView.setAdapter(suggestionsAdapter);
                            }
                            @Override
                            public void onFailure(Call<List<ProductDTO>> call, Throwable t) {
                                Log.e("Suggestions Error: ", t.getMessage());
                            }
                        });
                    }else{
                        Log.e("Firebase", "Firebase is null");
                    }
                    break;
            }
        }else{
            Log.e("RetrofitAPI", "Seller doesn't exist");
        }


    }

    private void showProduct(ProductDTO productDTO) {
        int id = productDTO.getProductID();
        String kindOfSeller = productDTO.getSellerRole();
        String firebase_id = productDTO.getFirebaseID();
        Intent intent = new Intent(this, Activity_Product_Profile.class);
        intent.putExtra("product_id", id);
        intent.putExtra("firebase_id",firebase_id);
        intent.putExtra("seller", kindOfSeller);
        startActivity(intent);
    }

    private void layoutProfile(String seller) {
        if(seller != null){
            switch(seller){
                case "Group Business User (Association)":
                case "Group Business User (Cooperative)":
                    Call<ProductDTO> productsInventoryCall = userAPI.getGroupProductDTO(productID);
                    productsInventoryCall.enqueue(new Callback<ProductDTO>() {
                        @Override
                        public void onResponse(Call<ProductDTO> call, Response<ProductDTO> response) {
                            if(response.isSuccessful() && response.body()!=null){
                                ifSuccess(response);
                            }else{
                                Log.e("RetrofitAPI", "Error fetching group discounts: " + response.code());
                            }
                        }

                        @Override
                        public void onFailure(Call<ProductDTO> call, Throwable t) {
                            Log.e("RetrofitAPI", "Error fetching group discounts: " + t.getMessage());
                        }
                    });
                    break;
                case "Individual Business User":
                    Call<ProductDTO> productsInventoryCall_1 = userAPI.getIndividualProductDTO(productID);
                    productsInventoryCall_1.enqueue(new Callback<ProductDTO>() {
                        @Override
                        public void onResponse(Call<ProductDTO> call, Response<ProductDTO> response) {
                            if(response.isSuccessful() && response.body()!=null){
                                ifSuccess(response);
                            }else{
                                Log.e("RetrofitAPI", "Error fetching group discounts: " + response.code());
                            }
                        }

                        @Override
                        public void onFailure(Call<ProductDTO> call, Throwable t) {
                            Log.e("RetrofitAPI", "Error fetching group discounts: " + t.getMessage());
                        }
                    });
                    break;
            }
        }else{
            Log.e("RetrofitAPI", "Seller doesn't exist");
        }
    }

    private void ifSuccess(Response<ProductDTO> response) {
        ProductDTO products = response.body();
        store = products;
        priceText.setText("₱" + products.getProductPrice());
        discountText.setText(String.valueOf(products.getProductDiscount() * 100)+"%");
        cropNameText.setText(products.getProductName());
        sellerNameText.setText(products.getProductSeller());
        sellerNameText.setOnClickListener(v ->{
            Intent openThruProfile = new Intent(this,Activity_Seller_Profile.class);
            String kindOfSeller = products.getSellerRole();
            String firebaseID = products.getFirebaseID();
            int id = products.getSellerID();
            openThruProfile.putExtra("seller", kindOfSeller);
            openThruProfile.putExtra("seller_id", id);
            openThruProfile.putExtra("firebase_id", firebaseID);
            startActivity(openThruProfile);
        });
        sellerRoleText.setText(products.getSellerRole());
        sellerProfile.setOnClickListener(v -> {
            Intent openThruProfile = new Intent(this,Activity_Seller_Profile.class);
            String kindOfSeller = products.getSellerRole();
            String firebaseID = products.getFirebaseID();
            int id = products.getSellerID();
            openThruProfile.putExtra("seller", kindOfSeller);
            openThruProfile.putExtra("seller_id", id);
            openThruProfile.putExtra("firebase_id", firebaseID);
            startActivity(openThruProfile);
        });
        StorageReference storageRef = FirebaseStorage.getInstance().getReference()
                .child("Products")
                .child(products.getFirebaseID())
                .child(products.getProductName());
        loadImage(storageRef);

        StorageReference storageRef_1 = FirebaseStorage.getInstance().getReference()
                .child("Profile Picture")
                .child(firebase_id)
                .child("Display");
        storageRef_1.getDownloadUrl().addOnSuccessListener(uri -> {
            Glide.with(this)
                    .load(uri.toString())
                    .placeholder(R.drawable.logo)
                    .error(R.drawable.sun)
                    .into(sellerProfile);
        }).addOnFailureListener(e -> {
            Log.e("FirebaseImageError", "Failed to get download URL: " + e.getMessage());
            sellerProfile.setImageResource(R.drawable.logo);
        });

        addToCart = findViewById(R.id.cartButton);
        addToCart.setOnClickListener(v -> {
            cart = new Cart();
            if(seller != null){
                switch(seller){
                    case "Group Business User (Association)":
                    case "Group Business User (Cooperative)":
                        Call<Integer> sellerIDCall = userAPI.getGroupSellersID(products.getFirebaseID());
                        sellerIDCall.enqueue(new Callback<Integer>() {
                            @Override
                            public void onResponse(Call<Integer> call, Response<Integer> response) {
                                if(response.isSuccessful() && response != null) {
                                    findGroupSellerByID(response.body());
                                }
                            }

                            @Override
                            public void onFailure(Call<Integer> call, Throwable t) {
                                Log.e("Finding Group Seller by Firebase Error: ", t.getMessage());
                            }
                        });
                        break;
                    case "Individual Business User":
                        Call<Integer> indivSellerIDCall = userAPI.getIndividualSellersID(products.getFirebaseID());
                        indivSellerIDCall.enqueue(new Callback<Integer>() {
                            @Override
                            public void onResponse(Call<Integer> call, Response<Integer> response) {
                                if(response.isSuccessful() && response != null) {
                                    findIndividualSellerByID(response.body());
                                }
                            }

                            @Override
                            public void onFailure(Call<Integer> call, Throwable t) {
                                Log.e("Finding Individual Seller by Firebase Error: ", t.getMessage());
                            }
                        });
                        break;
                }
            }else{
                Log.e("RetrofitAPI", "Seller doesn't exist");
            }

            cart.setCropID(products.getProductID());
            cart.setCropName(products.getProductName());
            cart.setQuantity(1);
            cart.setPrice(cart.getQuantity() * products.getProductPrice());
        });
    }

    private void findCustomer() {
        Call<Customer> customerCall = userAPI.getCustomerByFirebaseID(user.getUid());
        customerCall.enqueue(new Callback<Customer>() {
            @Override
            public void onResponse(Call<Customer> call, Response<Customer> response) {
                cart.setCustomer(response.body());
                addToCartMethod();
            }

            @Override
            public void onFailure(Call<Customer> call, Throwable t) {
                Log.e("RetrofitAPI", "Customer doesn't exist");
            }
        });
    }

    private void addToCartMethod() {
        Call<Cart> cartCall = userAPI.addCart(cart);
        cartCall.enqueue(new Callback<Cart>() {
            @Override
            public void onResponse(Call<Cart> call, Response<Cart> response) {
                Toast.makeText(Activity_Product_Profile.this, "Added to Cart!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Cart> call, Throwable t) {

            }
        });
    }

    private void findIndividualSellerByID(Integer body) {
        Call<IndividualSellers> individualSellersCall = userAPI.getIndividualSellers(body);
        individualSellersCall.enqueue(new Callback<IndividualSellers>() {
            @Override
            public void onResponse(Call<IndividualSellers> call, Response<IndividualSellers> response) {
                if(response.isSuccessful() && response != null){
                    cart.setIndividualSellers(response.body());
                    findCustomer();
                }
            }

            @Override
            public void onFailure(Call<IndividualSellers> call, Throwable t) {
                Log.e("Finding Group Seller Error: ", t.getMessage());
            }
        });
    }

    private void findGroupSellerByID(Integer body) {
        Call<GroupSellers> groupSellersCall = userAPI.getGroupSellers(body);
        groupSellersCall.enqueue(new Callback<GroupSellers>() {
            @Override
            public void onResponse(Call<GroupSellers> call, Response<GroupSellers> response) {
                if(response.isSuccessful() && response != null){
                    cart.setGroupSellers(response.body());
                    findCustomer();
                }
            }

            @Override
            public void onFailure(Call<GroupSellers> call, Throwable t) {
                Log.e("Finding Group Seller Error: ", t.getMessage());
            }
        });
    }

    private void loadImage(StorageReference storageRef) {
        storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
            Glide.with(this)
                    .load(uri.toString())
                    .placeholder(R.drawable.logo)
                    .error(R.drawable.sun)
                    .into(productProfile);
        }).addOnFailureListener(e -> {
            Log.e("FirebaseImageError", "Failed to get download URL: " + e.getMessage());
            productProfile.setImageResource(R.drawable.logo);
        });
    }
}
