package com.example.croop.GroupSellerLanding;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.croop.R;
import com.example.croop.model.GroupSellersProductsInventory;
import com.example.croop.retrofit.RetrofitService;
import com.example.croop.retrofit.UserAPI;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Activity_Products_Inventory extends AppCompatActivity {
    private TableLayout table;
    FirebaseAuth mAuth;
    RetrofitService RetrofitClient;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.products_group_seller);
        initializeComponents();
    }

    private void initializeComponents() {
        UserAPI apiService = RetrofitClient.getClient().create(UserAPI.class);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        table = findViewById(R.id.tableLayout);

        Call<List<GroupSellersProductsInventory>> call = apiService.getProductsByFirebaseID(user.getUid());
        call.enqueue(new Callback<List<GroupSellersProductsInventory>>() {
            @Override
            public void onResponse(Call<List<GroupSellersProductsInventory>> call, Response<List<GroupSellersProductsInventory>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<GroupSellersProductsInventory> products = response.body();
                    Toast.makeText(Activity_Products_Inventory.this, "Number of products fetched: " + products.size(), Toast.LENGTH_SHORT).show();
                    populateTableDefault(products,table);
                }else{
                    try {
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "No error body";
                        Log.e("API_ERROR", "Code: " + response.code() + ", Message: " + errorBody);
                        Toast.makeText(Activity_Products_Inventory.this, "API_ERROR: Code " + response.code(), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<GroupSellersProductsInventory>> call, Throwable t) {
                Toast.makeText(Activity_Products_Inventory.this, "API_ERROR"+ t.toString(), Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
        Button find, add;
        find = findViewById(R.id.findItemButton);
        find.setOnClickListener(v -> {
            EditText itemNameFind = findViewById(R.id.productNameFindText);
            String itemName = itemNameFind.getText().toString().trim();
            Call<List<GroupSellersProductsInventory>> searchItem = apiService.getProductsByName(itemName);
            searchItem.enqueue(new Callback<List<GroupSellersProductsInventory>>() {
                @Override
                public void onResponse(Call<List<GroupSellersProductsInventory>> call, Response<List<GroupSellersProductsInventory>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<GroupSellersProductsInventory> items = response.body();
                        Toast.makeText(Activity_Products_Inventory.this, "Number of products found: " + items.size(), Toast.LENGTH_SHORT).show();
                        populateTableDefault(items,table);
                    }else{
                        try {
                            String errorBody = response.errorBody() != null ? response.errorBody().string() : "No error body";
                            Log.e("API_ERROR", "Code: " + response.code() + ", Message: " + errorBody);
                            Toast.makeText(Activity_Products_Inventory.this, "API_ERROR: Code " + response.code(), Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<GroupSellersProductsInventory>> call, Throwable t) {
                    Toast.makeText(Activity_Products_Inventory.this, "API_ERROR"+ t.toString(), Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                }
            });
        });
        add = findViewById(R.id.addItemButton);
        add.setOnClickListener(v -> {
            Intent intent = new Intent(this, Activity_Add_Products.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });
        FloatingActionButton back = findViewById(R.id.backFloat);
        back.setOnClickListener(v -> {
            Intent intent = new Intent(this, Activity_Inventory_Category.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });
    }

    private void populateTableDefault(List<GroupSellersProductsInventory> products, TableLayout table) {
        UserAPI apiService = RetrofitClient.getClient().create(UserAPI.class);
        table.removeViews(1, table.getChildCount() - 1);

        for (GroupSellersProductsInventory product : products) {
            TableRow row = new TableRow(this);
            TableRow.LayoutParams params = new TableRow.LayoutParams(
                    0, // Width: 0 means the width will be determined by the weight
                    TableRow.LayoutParams.WRAP_CONTENT, // Height: Wrap content
                    1.0f // Weight: 1 means equal distribution of space
            );
            int initial = product.getItemStart();
            int used = product.getItemUsed();
            int remaining = initial - used;

            TextView itemTextView = new TextView(this);
            SpannableString underlinedText = new SpannableString(String.valueOf(product.getItemName()));
            underlinedText.setSpan(new UnderlineSpan(), 0, underlinedText.length(), 0);
            itemTextView.setText(underlinedText);
            itemTextView.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
            itemTextView.setLayoutParams(params);
            itemTextView.setTextColor(getResources().getColor(R.color.highlight_green));
            itemTextView.setOnClickListener(view -> {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(Activity_Products_Inventory.this);
                View bottomSheetView = getLayoutInflater().inflate(R.layout.update_delete_products_group_sellers, null);

                // Populate the bottom sheet with data
                EditText itemName = bottomSheetView.findViewById(R.id.nameText);
                itemName.setText(product.getItemName());
                EditText priceTag = bottomSheetView.findViewById(R.id.priceText);
                priceTag.setText(String.valueOf(product.getPrice()));
                TextView orderText = bottomSheetView.findViewById(R.id.boughtText);
                orderText.setText(String.valueOf(used));
                TextView leftText = bottomSheetView.findViewById(R.id.remainingText);
                leftText.setText(String.valueOf(remaining));
                Button update = bottomSheetView.findViewById(R.id.updateButton);
                Button check = bottomSheetView.findViewById(R.id.checkButton);
                Button delete = bottomSheetView.findViewById(R.id.deleteButton);
                update.setOnClickListener(v -> {
                    String itemNameText = itemName.getText().toString().trim();
                    String price = priceTag.getText().toString().trim();
                    String order = orderText.getText().toString().trim();
                    String left = leftText.getText().toString().trim();

                    if (itemNameText.isEmpty()) {
                        Toast.makeText(Activity_Products_Inventory.this, "Product name cannot be empty", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (price.isEmpty()) {
                        Toast.makeText(Activity_Products_Inventory.this, "Product price cannot be empty", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    try {
                        int priceValue = Integer.parseInt(price);
                        if (priceValue < 0) {
                            Toast.makeText(Activity_Products_Inventory.this, "Product price must be a non-negative number", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        GroupSellersProductsInventory groupSellersProductsInventory = new GroupSellersProductsInventory();
                        groupSellersProductsInventory.setItemName(itemNameText);
                        groupSellersProductsInventory.setItemStart(product.getItemStart());
                        groupSellersProductsInventory.setPrice(priceValue);
                        groupSellersProductsInventory.setItemUsed(Integer.parseInt(order));

                        Call<GroupSellersProductsInventory> updateItem = apiService.updateProducts(product.getId(), groupSellersProductsInventory);
                        updateItem.enqueue(new Callback<GroupSellersProductsInventory>() {
                            @Override
                            public void onResponse(Call<GroupSellersProductsInventory> call, Response<GroupSellersProductsInventory> response) {
                                if (response.isSuccessful() && response.body() != null) {
                                    Toast.makeText(Activity_Products_Inventory.this, "Product Updated", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(Activity_Products_Inventory.this, Activity_Inventory_Category.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                    finish();
                                } else {
                                    try {
                                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "No error body";
                                        Log.e("API_ERROR", "Code: " + response.code() + ", Message: " + errorBody);
                                        Toast.makeText(Activity_Products_Inventory.this, "API_ERROR: Code " + response.code(), Toast.LENGTH_SHORT).show();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<GroupSellersProductsInventory> call, Throwable t) {
                                Toast.makeText(Activity_Products_Inventory.this, "API_ERROR: " + t.toString(), Toast.LENGTH_SHORT).show();
                                t.printStackTrace();
                            }
                        });

                    } catch (NumberFormatException e) {
                        Toast.makeText(Activity_Products_Inventory.this, "Invalid number for item used", Toast.LENGTH_SHORT).show();
                    }
                });
                delete.setOnClickListener(v -> {
                    Call<GroupSellersProductsInventory> deleteItem = apiService.deleteProducts(product.getId());
                    deleteItem.enqueue(new Callback<GroupSellersProductsInventory>() {
                        @Override
                        public void onResponse(Call<GroupSellersProductsInventory> call, Response<GroupSellersProductsInventory> response) {
                            Toast.makeText(Activity_Products_Inventory.this, "Products Deleted", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Activity_Products_Inventory.this, Activity_Products_Inventory.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            finish();
                        }

                        @Override
                        public void onFailure(Call<GroupSellersProductsInventory> call, Throwable t) {
                            Toast.makeText(Activity_Products_Inventory.this, "API_ERROR"+ t.toString(), Toast.LENGTH_SHORT).show();
                            t.printStackTrace();
                        }
                    });
                });

                check.setOnClickListener(v->{
                    recreate();
                });

                // Set the view and show the dialog
                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();
            });
            row.addView(itemTextView);

            TextView remainingTextView = new TextView(this);// Assuming this is a JSON string

            remainingTextView.setText(String.valueOf(remaining));
            remainingTextView.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
            remainingTextView.setLayoutParams(params);
            row.addView(remainingTextView);

            table.addView(row);
        }
    }
}
