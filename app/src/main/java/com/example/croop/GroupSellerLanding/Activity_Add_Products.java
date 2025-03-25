package com.example.croop.GroupSellerLanding;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.croop.R;
import com.example.croop.model.GroupSellers;
import com.example.croop.model.GroupSellersProductsInventory;
import com.example.croop.retrofit.RetrofitService;
import com.example.croop.retrofit.UserAPI;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.time.LocalDate;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Activity_Add_Products extends AppCompatActivity {
    private static final int RC_IMAGE_PICKER = 100;
    private Uri imageUri;
    private FirebaseAuth mAuth;
    private RetrofitService RetrofitClient;
    private ImageView product;
    private Spinner unit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_products);
        mAuth = FirebaseAuth.getInstance();
        initializeComponents();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        product = findViewById(R.id.addProductProfile);
        if (requestCode == RC_IMAGE_PICKER && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            if (imageUri == null) {
                Toast.makeText(this, "Failed to retrieve image URI!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Image selected: " + imageUri.toString(), Toast.LENGTH_SHORT).show();
                Glide.with(this)
                        .load(imageUri.toString())
                        .placeholder(R.drawable.logo)
                        .error(R.drawable.sun)
                        .into(product);
            }
        } else {
            Toast.makeText(this, "No image selected!", Toast.LENGTH_SHORT).show();
        }
    }
    private void initializeComponents() {
        Button picture = findViewById(R.id.uploadPicButton);
        picture.setOnClickListener(v1 -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, RC_IMAGE_PICKER);
        });

        unit = findViewById(R.id.unitOptions);
        String[] units = {"Kilo", "Sako", "Tumpok", "Piraso"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, units);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        unit.setAdapter(adapter);

        Button add = findViewById(R.id.addButton);
        add.setOnClickListener(v -> {
            UserAPI userAPI = RetrofitClient.getClient().create(UserAPI.class);
            FirebaseUser user = mAuth.getCurrentUser();
            Call<Integer> call = userAPI.getGroupSellersID(user.getUid());
            call.enqueue(new Callback<Integer>() {
                @Override
                public void onResponse(Call<Integer> call, Response<Integer> response) {
                    if(response.isSuccessful() && response.body()!=null){
                        int id = response.body();
                        addItemProcess(id);
                    }
                }

                @Override
                public void onFailure(Call<Integer> call, Throwable t) {
                    Toast.makeText(Activity_Add_Products.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        FloatingActionButton back = findViewById(R.id.backFloat);
        back.setOnClickListener(v -> {
            onBackPressed();
        });
    }

    private void addPictureProduct(Uri imageUri, String fileName) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            Toast.makeText(this, "User not signed in!", Toast.LENGTH_SHORT).show();
            return;
        }

        product = findViewById(R.id.addProductProfile);

        String userId = user.getUid();

        StorageReference storageRef = FirebaseStorage.getInstance().getReference()
                .child("Products")
                .child(userId)
                .child(fileName);

        storageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        Toast.makeText(this, "Picture successfully uploaded", Toast.LENGTH_SHORT).show();
                    }).addOnFailureListener(e -> {
                        Toast.makeText(this, "Failed to get download URL: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to upload image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void addItemProcess(int id) {
        UserAPI userAPI = RetrofitClient.getClient().create(UserAPI.class);
        GroupSellers groupSellers = new GroupSellers();
        groupSellers.setID(id);
        EditText name, quantity, price, freshness;



        name = findViewById(R.id.nameText);
        quantity = findViewById(R.id.initialText);
        price = findViewById(R.id.priceText);
        freshness = findViewById(R.id.freshnessText);

        quantity.setInputType(InputType.TYPE_CLASS_NUMBER);
        price.setInputType(InputType.TYPE_CLASS_NUMBER);
        freshness.setInputType(InputType.TYPE_CLASS_NUMBER);

        GroupSellersProductsInventory groupSellersProductsInventory = new GroupSellersProductsInventory();
        groupSellersProductsInventory.setItemName(name.getText().toString());
        groupSellersProductsInventory.setItemStart(Integer.parseInt(quantity.getText().toString()));
        groupSellersProductsInventory.setPrice(Integer.parseInt(price.getText().toString()));
        groupSellersProductsInventory.setGroupSellers(groupSellers);
        groupSellersProductsInventory.setUnit(unit.getSelectedItem().toString());
        groupSellersProductsInventory.setShelfLifeDays(Integer.parseInt(freshness.getText().toString()));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            groupSellersProductsInventory.setLocalDate(LocalDate.now());
        }
        Call<GroupSellersProductsInventory> call = userAPI.addProduct(groupSellersProductsInventory);
        call.enqueue(new Callback<GroupSellersProductsInventory>() {
            @Override
            public void onResponse(Call<GroupSellersProductsInventory> call, Response<GroupSellersProductsInventory> response) {
                addPictureProduct(imageUri,name.getText().toString());
                Toast.makeText(Activity_Add_Products.this, name.getText().toString() + " is added.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Activity_Add_Products.this, Activity_Products_Inventory.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(Call<GroupSellersProductsInventory> call, Throwable t) {
                Toast.makeText(Activity_Add_Products.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
