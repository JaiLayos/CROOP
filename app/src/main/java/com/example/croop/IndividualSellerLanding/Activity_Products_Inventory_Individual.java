package com.example.croop.IndividualSellerLanding;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
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
import com.example.croop.model.IndividualSellers;
import com.example.croop.model.IndividualSellersDiscount;
import com.example.croop.model.IndividualSellersProductsInventory;
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

public class Activity_Products_Inventory_Individual extends AppCompatActivity {
    private TableLayout table;
    FirebaseAuth mAuth;
    RetrofitService RetrofitClient;
    UserAPI apiService = RetrofitClient.getClient().create(UserAPI.class);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.products_individual_seller);
        initializeComponents();
    }

    private void initializeComponents() {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        table = findViewById(R.id.tableLayout);

        Call<List<IndividualSellersProductsInventory>> call = apiService.getIndividualProductsByFirebaseID(user.getUid());
        call.enqueue(new Callback<List<IndividualSellersProductsInventory>>() {
            @Override
            public void onResponse(Call<List<IndividualSellersProductsInventory>> call, Response<List<IndividualSellersProductsInventory>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<IndividualSellersProductsInventory> products = response.body();
                    Toast.makeText(Activity_Products_Inventory_Individual.this, "Bilang ng mga order na nakuha: " + products.size(), Toast.LENGTH_SHORT).show();
                    populateTableDefault(products,table);
                }else{
                    try {
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "No error body";
                        Log.e("API_ERROR", "Code: " + response.code() + ", Message: " + errorBody);
                        Toast.makeText(Activity_Products_Inventory_Individual.this, "API_ERROR: Code " + response.code(), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<IndividualSellersProductsInventory>> call, Throwable t) {
                Toast.makeText(Activity_Products_Inventory_Individual.this, "API_ERROR"+ t.toString(), Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
        Button find, add;
        find = findViewById(R.id.findItemButton);
        find.setOnClickListener(v -> {
            EditText itemNameFind = findViewById(R.id.productNameFindText);
            String itemName = itemNameFind.getText().toString().trim();
            Call<List<IndividualSellersProductsInventory>> searchItem = apiService.getIndividualProductsByName(itemName);
            searchItem.enqueue(new Callback<List<IndividualSellersProductsInventory>>() {
                @Override
                public void onResponse(Call<List<IndividualSellersProductsInventory>> call, Response<List<IndividualSellersProductsInventory>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<IndividualSellersProductsInventory> items = response.body();
                        Toast.makeText(Activity_Products_Inventory_Individual.this, "Bilang ng mga order na natagpuan: " + items.size(), Toast.LENGTH_SHORT).show();
                        populateTableDefault(items,table);
                    }else{
                        try {
                            String errorBody = response.errorBody() != null ? response.errorBody().string() : "No error body";
                            Log.e("API_ERROR", "Code: " + response.code() + ", Message: " + errorBody);
                            Toast.makeText(Activity_Products_Inventory_Individual.this, "API_ERROR: Code " + response.code(), Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<IndividualSellersProductsInventory>> call, Throwable t) {
                    Toast.makeText(Activity_Products_Inventory_Individual.this, "API_ERROR"+ t.toString(), Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                }
            });
        });
        add = findViewById(R.id.addItemButton);
        add.setOnClickListener(v -> {
            Intent intent = new Intent(this, Activity_Add_Products_Individual.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });
        FloatingActionButton back = findViewById(R.id.backFloat);
        back.setOnClickListener(v -> {
            Intent intent = new Intent(this, Activity_Inventory_Category_Individual.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });
    }

    private void populateTableDefault(List<IndividualSellersProductsInventory> products, TableLayout table) {
        table.removeViews(1, table.getChildCount() - 1);

        for (IndividualSellersProductsInventory product : products) {
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
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(Activity_Products_Inventory_Individual.this);
                View bottomSheetView = getLayoutInflater().inflate(R.layout.update_delete_products_individual_sellers, null);

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
                        Toast.makeText(Activity_Products_Inventory_Individual.this, "Dapat may pangalan ang produkto", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (price.isEmpty()) {
                        Toast.makeText(Activity_Products_Inventory_Individual.this, "Dapat lagyan ng presyo ang produkto", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    try {
                        int priceValue = Integer.parseInt(price);
                        if (priceValue < 0) {
                            Toast.makeText(Activity_Products_Inventory_Individual.this, "Kailangan ang presyo ng produkto ay hindi negatibo", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        IndividualSellersProductsInventory individualSellersProductsInventory = new IndividualSellersProductsInventory();
                        individualSellersProductsInventory.setItemName(itemNameText);
                        individualSellersProductsInventory.setItemStart(product.getItemStart());
                        individualSellersProductsInventory.setPrice(priceValue);
                        individualSellersProductsInventory.setItemUsed(Integer.parseInt(order));

                        Call<IndividualSellersProductsInventory> updateItem = apiService.updateIndividualProducts(product.getId(), individualSellersProductsInventory);
                        updateItem.enqueue(new Callback<IndividualSellersProductsInventory>() {
                            @Override
                            public void onResponse(Call<IndividualSellersProductsInventory> call, Response<IndividualSellersProductsInventory> response) {
                                if (response.isSuccessful() && response.body() != null) {
                                    Toast.makeText(Activity_Products_Inventory_Individual.this, "Na-update na ang produkto", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(Activity_Products_Inventory_Individual.this, Activity_Inventory_Category_Individual.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                    finish();
                                } else {
                                    try {
                                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "No error body";
                                        Log.e("API_ERROR", "Code: " + response.code() + ", Message: " + errorBody);
                                        Toast.makeText(Activity_Products_Inventory_Individual.this, "API_ERROR: Code " + response.code(), Toast.LENGTH_SHORT).show();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<IndividualSellersProductsInventory> call, Throwable t) {
                                Toast.makeText(Activity_Products_Inventory_Individual.this, "API_ERROR: " + t.toString(), Toast.LENGTH_SHORT).show();
                                t.printStackTrace();
                            }
                        });

                    } catch (NumberFormatException e) {
                        Toast.makeText(Activity_Products_Inventory_Individual.this, "Hindi valid na numero ang para sa ginamit na item", Toast.LENGTH_SHORT).show();
                    }
                });
                delete.setOnClickListener(v -> {
                    Call<IndividualSellersProductsInventory> deleteItem = apiService.deleteIndividualProducts(product.getId());
                    deleteItem.enqueue(new Callback<IndividualSellersProductsInventory>() {
                        @Override
                        public void onResponse(Call<IndividualSellersProductsInventory> call, Response<IndividualSellersProductsInventory> response) {
                            Toast.makeText(Activity_Products_Inventory_Individual.this, "Nabura na ang produkto", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Activity_Products_Inventory_Individual.this, Activity_Products_Inventory_Individual.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            finish();
                        }

                        @Override
                        public void onFailure(Call<IndividualSellersProductsInventory> call, Throwable t) {
                            Toast.makeText(Activity_Products_Inventory_Individual.this, "API_ERROR"+ t.toString(), Toast.LENGTH_SHORT).show();
                            t.printStackTrace();
                        }
                    });
                });

                check.setOnClickListener(v->{
                    recreate();
                });

                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();
            });
            row.addView(itemTextView);

            TextView remainingTextView = new TextView(this);
            remainingTextView.setText(String.valueOf(remaining));
            remainingTextView.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
            remainingTextView.setLayoutParams(params);
            row.addView(remainingTextView);

            TextView promoView = new TextView(this);
            String discount = "0"; // Default value
            if (product.getIndividualSellersDiscount() != null) {
                discount = String.valueOf(product.getIndividualSellersDiscount().getDiscountPercent());
            }
            promoView.setText(discount);
            promoView.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
            promoView.setLayoutParams(params);
            promoView.setTextColor(getResources().getColor(R.color.highlight_green));
            promoView.setTypeface(null, Typeface.ITALIC);
            promoView.setOnClickListener(v -> {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(Activity_Products_Inventory_Individual.this);

                View bottomSheetView = LayoutInflater.from(Activity_Products_Inventory_Individual.this)
                        .inflate(R.layout.add_discount_individual, null);

                EditText discountText = bottomSheetView.findViewById(R.id.discountText);
                Button save = bottomSheetView.findViewById(R.id.saveButton);

                save.setOnClickListener(v1 -> {
                    IndividualSellersDiscount individualSellersDiscount = new IndividualSellersDiscount();
                    FirebaseUser user = mAuth.getCurrentUser();
                    Call<IndividualSellers> getISellers = apiService.getIndividualSellersbyFirebaseID(user.getUid());
                    getISellers.enqueue(new Callback<IndividualSellers>() {
                        @Override
                        public void onResponse(Call<IndividualSellers> call, Response<IndividualSellers> response) {
                            individualSellersDiscount.setIndividualSellers(response.body());
                        }
                        @Override
                        public void onFailure(Call<IndividualSellers> call, Throwable t) {
                            Toast.makeText(Activity_Products_Inventory_Individual.this, "wala", Toast.LENGTH_SHORT).show();
                        }
                    });

                    individualSellersDiscount.setIndividualSellersProductsInventory(product);
                    individualSellersDiscount.setDiscountPercent(Double.parseDouble(discountText.getText().toString()));
                    Call<IndividualSellersDiscount> updateDiscountCall = apiService.updateIndividualDiscount(
                            product.getIndividualSellersDiscount().getId(),
                            individualSellersDiscount
                    );
                    updateDiscountCall.enqueue(new Callback<IndividualSellersDiscount>() {
                        @Override
                        public void onResponse(Call<IndividualSellersDiscount> call, Response<IndividualSellersDiscount> response) {
                            Intent intent = new Intent(Activity_Products_Inventory_Individual.this, Activity_Discount_Inventory_Individual.class);
                            startActivity(intent);
                            finish();
                        }
                        @Override
                        public void onFailure(Call<IndividualSellersDiscount> call, Throwable t) {
                            Toast.makeText(Activity_Products_Inventory_Individual.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                });
                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();
            });
            row.addView(promoView);
            table.addView(row);
        }
    }
}
