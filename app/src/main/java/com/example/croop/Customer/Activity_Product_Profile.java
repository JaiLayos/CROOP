package com.example.croop.Customer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.croop.R;
import com.example.croop.model.ProductDTO;
import com.example.croop.retrofit.RetrofitService;
import com.example.croop.retrofit.UserAPI;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Activity_Product_Profile extends AppCompatActivity {

    private String seller;
    private int productID;
    private UserAPI userAPI;
    private RetrofitService retrofitService;
    private ImageView productProfile, sellerProfile;
    private TextView priceText, discountText, cropNameText,
        sellerNameText, sellerRoleText;
    private FloatingActionButton back;
    private ProductDTO store;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_profile);

        initializeComponents();
    }

    private void initializeComponents() {
        Intent intent = getIntent();
        if (intent != null) {
            productID = intent.getIntExtra("product_id", 1);
            seller = intent.getStringExtra("seller");
        }
        //Texts
        priceText = findViewById(R.id.priceText);
        discountText = findViewById(R.id.discountText);
        cropNameText = findViewById(R.id.cropNameText);
        sellerNameText = findViewById(R.id.sellerNameText);
        sellerRoleText = findViewById(R.id.sellerRoleText);

        //Images
        productProfile = findViewById(R.id.productProfile);
        sellerProfile = findViewById(R.id.sellerProfile);

        back = findViewById(R.id.backButton);
        back.setOnClickListener(v -> {
            onBackPressed();
        });

        userAPI = retrofitService.getClient().create(UserAPI.class);
        layoutProfile(seller);

    }

    private void layoutProfile(String seller) {
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
    }

    private void ifSuccess(Response<ProductDTO> response) {
        ProductDTO products = response.body();
        store = products;
        priceText.setText("P" + products.getProductPrice());
        discountText.setText(String.valueOf(products.getProductDiscount() * 100)+"%");
        cropNameText.setText(products.getProductName());
        sellerNameText.setText(products.getProductSeller());
        sellerRoleText.setText(products.getSellerRole());
        StorageReference storageRef = FirebaseStorage.getInstance().getReference()
                .child("Products")
                .child(products.getFirebaseID())
                .child(products.getProductName());
        loadImage(storageRef);
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
