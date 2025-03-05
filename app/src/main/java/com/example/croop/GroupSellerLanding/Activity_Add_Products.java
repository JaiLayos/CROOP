package com.example.croop.GroupSellerLanding;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Activity_Add_Products extends AppCompatActivity {
    private static final int RC_IMAGE_PICKER = 100;
    private Uri imageUri;
    FirebaseAuth mAuth;
    RetrofitService RetrofitClient;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_products_group);
        mAuth = FirebaseAuth.getInstance();
        initializeComponents();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_IMAGE_PICKER && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            if (imageUri == null) {
                Toast.makeText(this, "Failed to retrieve image URI!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Image selected: " + imageUri.toString(), Toast.LENGTH_SHORT).show();
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
            Intent intent = new Intent(this, Activity_Products_Inventory.class);
            startActivity(intent);
            recreate();
        });
    }

    private void addPictureProduct(Uri imageUri, String fileName) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            Toast.makeText(this, "User not signed in!", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = user.getUid();

        StorageReference storageRef = FirebaseStorage.getInstance().getReference()
                .child("Products")
                .child(userId)
                .child(fileName);

        storageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        Toast.makeText(this, "Uploaded!", Toast.LENGTH_SHORT).show();
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
        EditText name, quantity, price;
        name = findViewById(R.id.nameText);
        quantity = findViewById(R.id.initialText);
        price = findViewById(R.id.priceText);
        GroupSellersProductsInventory groupSellersProductsInventory = new GroupSellersProductsInventory();
        groupSellersProductsInventory.setItemName(name.getText().toString());
        groupSellersProductsInventory.setItemStart(Integer.parseInt(quantity.getText().toString()));
        groupSellersProductsInventory.setPrice(Integer.parseInt(price.getText().toString()));
        groupSellersProductsInventory.setGroupSellers(groupSellers);
        Call<GroupSellersProductsInventory> call = userAPI.addProduct(groupSellersProductsInventory);
        call.enqueue(new Callback<GroupSellersProductsInventory>() {
            @Override
            public void onResponse(Call<GroupSellersProductsInventory> call, Response<GroupSellersProductsInventory> response) {
                Toast.makeText(Activity_Add_Products.this, name.getText().toString() + " is added.", Toast.LENGTH_SHORT).show();
                addPictureProduct(imageUri, name.getText().toString().trim());
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
