package com.example.croop.IndividualSellerLanding;

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
import com.example.croop.model.IndividualSellersItemInventory;
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

public class Activity_Item_Inventory_Individual extends AppCompatActivity {
    private TableLayout table;
    FirebaseAuth mAuth;
    RetrofitService RetrofitClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_group_inventory);
        initializeComponents();
    }

    private void initializeComponents() {
        UserAPI apiService = RetrofitClient.getClient().create(UserAPI.class);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        table = findViewById(R.id.tableLayout);

        Call<List<IndividualSellersItemInventory>> call = apiService.getIndividualItemsByFirebaseID(user.getUid());
        call.enqueue(new Callback<List<IndividualSellersItemInventory>>() {
            @Override
            public void onResponse(Call<List<IndividualSellersItemInventory>> call, Response<List<IndividualSellersItemInventory>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<IndividualSellersItemInventory> items = response.body();
                    Toast.makeText(Activity_Item_Inventory_Individual.this, "Bilang ng mga order na nakuha: " + items.size(), Toast.LENGTH_SHORT).show();
                    populateTableDefault(items,table);
                }else{
                    try {
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "No error body";
                        Log.e("API_ERROR", "Code: " + response.code() + ", Message: " + errorBody);
                        Toast.makeText(Activity_Item_Inventory_Individual.this, "API_ERROR: Code " + response.code(), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<IndividualSellersItemInventory>> call, Throwable t) {
                Toast.makeText(Activity_Item_Inventory_Individual.this, "API_ERROR"+ t.toString(), Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
        Button find, add;
        find = findViewById(R.id.findItemButton);
        find.setOnClickListener(v -> {
            EditText itemNameFind = findViewById(R.id.itemNameFindText);
            String itemName = itemNameFind.getText().toString();
            Call<List<IndividualSellersItemInventory>> searchItem = apiService.getIndividualItemByName(itemName);
            searchItem.enqueue(new Callback<List<IndividualSellersItemInventory>>() {
                @Override
                public void onResponse(Call<List<IndividualSellersItemInventory>> call, Response<List<IndividualSellersItemInventory>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<IndividualSellersItemInventory> items = response.body();
                        Toast.makeText(Activity_Item_Inventory_Individual.this, "Bilang ng mga order na natagpuan: " + items.size(), Toast.LENGTH_SHORT).show();
                        populateTableDefault(items,table);
                    }else{
                        try {
                            String errorBody = response.errorBody() != null ? response.errorBody().string() : "No error body";
                            Log.e("API_ERROR", "Code: " + response.code() + ", Message: " + errorBody);
                            Toast.makeText(Activity_Item_Inventory_Individual.this, "API_ERROR: Code " + response.code(), Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<IndividualSellersItemInventory>> call, Throwable t) {
                    Toast.makeText(Activity_Item_Inventory_Individual.this, "API_ERROR"+ t.toString(), Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                }
            });
        });
        add = findViewById(R.id.addItemButton);
        add.setOnClickListener(v -> {
            Intent intent = new Intent(this, Activity_Add_Item_Individual.class);
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

    private void populateTableDefault(List<IndividualSellersItemInventory> items, TableLayout table){
        UserAPI apiService = RetrofitClient.getClient().create(UserAPI.class);
        table.removeViews(1, table.getChildCount() - 1);

        for (IndividualSellersItemInventory item : items) {
            TableRow row = new TableRow(this);
            TableRow.LayoutParams params = new TableRow.LayoutParams(
                    0, // Width: 0 means the width will be determined by the weight
                    TableRow.LayoutParams.WRAP_CONTENT, // Height: Wrap content
                    1.0f // Weight: 1 means equal distribution of space
            );

            TextView itemTextView = new TextView(this);
            SpannableString underlinedText = new SpannableString(String.valueOf(item.getItemName()));
            underlinedText.setSpan(new UnderlineSpan(), 0, underlinedText.length(), 0);
            itemTextView.setText(underlinedText);
            itemTextView.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
            itemTextView.setLayoutParams(params);
            itemTextView.setTextColor(getResources().getColor(R.color.highlight_green));
            itemTextView.setOnClickListener(view -> {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(Activity_Item_Inventory_Individual.this);
                View bottomSheetView = getLayoutInflater().inflate(R.layout.update_delete_group_item_sheet, null);

                // Populate the bottom sheet with data
                EditText itemName = bottomSheetView.findViewById(R.id.nameText);
                itemName.setText(item.getItemName());
                EditText itemUsed = bottomSheetView.findViewById(R.id.usedText);
                Button update = bottomSheetView.findViewById(R.id.updateButton);
                Button check = bottomSheetView.findViewById(R.id.checkButton);
                Button delete = bottomSheetView.findViewById(R.id.deleteButton);
                delete.setOnClickListener(v -> {
                    Call<IndividualSellersItemInventory> deleteItem = apiService.deleteIndividualItem(item.getId());
                    deleteItem.enqueue(new Callback<IndividualSellersItemInventory>() {
                        @Override
                        public void onResponse(Call<IndividualSellersItemInventory> call, Response<IndividualSellersItemInventory> response) {
                            Toast.makeText(Activity_Item_Inventory_Individual.this, "Nabura na ang item", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Activity_Item_Inventory_Individual.this, Activity_Item_Inventory_Individual.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            finish();
                        }

                        @Override
                        public void onFailure(Call<IndividualSellersItemInventory> call, Throwable t) {
                            Toast.makeText(Activity_Item_Inventory_Individual.this, "API_ERROR"+ t.toString(), Toast.LENGTH_SHORT).show();
                            t.printStackTrace();
                        }
                    });
                });

                update.setOnClickListener(v -> {
                    String itemNameText = itemName.getText().toString().trim();
                    String itemUsedText = itemUsed.getText().toString().trim();

                    if (itemNameText.isEmpty()) {
                        Toast.makeText(Activity_Item_Inventory_Individual.this, "Dapat may pangalan ang item", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (itemUsedText.isEmpty()) {
                        Toast.makeText(Activity_Item_Inventory_Individual.this, "Pakilagay ang item na ginamit", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    try {
                        int itemUsedValue = Integer.parseInt(itemUsedText);
                        if (itemUsedValue < 0) {
                            Toast.makeText(Activity_Item_Inventory_Individual.this, "Kailangan ang numero ng item na ginamit at hindi negatibo", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        IndividualSellersItemInventory individualSellersItemInventory = new IndividualSellersItemInventory();
                        individualSellersItemInventory.setItemName(itemNameText);
                        individualSellersItemInventory.setItemStart(item.getItemStart());
                        individualSellersItemInventory.setItemUsed(itemUsedValue);

                        Call<IndividualSellersItemInventory> updateItem = apiService.updateIndividualItem(item.getId(), individualSellersItemInventory);
                        updateItem.enqueue(new Callback<IndividualSellersItemInventory>() {
                            @Override
                            public void onResponse(Call<IndividualSellersItemInventory> call, Response<IndividualSellersItemInventory> response) {
                                if (response.isSuccessful() && response.body() != null) {
                                    Toast.makeText(Activity_Item_Inventory_Individual.this, "Na-update na gamit", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(Activity_Item_Inventory_Individual.this, Activity_Inventory_Category_Individual.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                    finish();
                                } else {
                                    try {
                                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "No error body";
                                        Log.e("API_ERROR", "Code: " + response.code() + ", Message: " + errorBody);
                                        Toast.makeText(Activity_Item_Inventory_Individual.this, "API_ERROR: Code " + response.code(), Toast.LENGTH_SHORT).show();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<IndividualSellersItemInventory> call, Throwable t) {
                                Toast.makeText(Activity_Item_Inventory_Individual.this, "API_ERROR: " + t.toString(), Toast.LENGTH_SHORT).show();
                                t.printStackTrace();
                            }
                        });

                    } catch (NumberFormatException e) {
                        Toast.makeText(Activity_Item_Inventory_Individual.this, "Hindi valid na numero ang para sa ginamit na item", Toast.LENGTH_SHORT).show();
                    }
                });
                check.setOnClickListener(v->{
                    Intent intent = new Intent(Activity_Item_Inventory_Individual.this, Activity_Item_Inventory_Individual.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    finish();
                });

                // Set the view and show the dialog
                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();
            });
            row.addView(itemTextView);

            TextView initialTextView = new TextView(this);
            initialTextView.setText(String.valueOf(item.getItemStart()));
            initialTextView.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
            initialTextView.setLayoutParams(params);
            row.addView(initialTextView);

            TextView usedTextView = new TextView(this);
            usedTextView.setText(String.valueOf(item.getItemUsed())); // Assuming Customer has a name field
            usedTextView.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
            usedTextView.setLayoutParams(params);
            row.addView(usedTextView);

            TextView remainingTextView = new TextView(this);// Assuming this is a JSON string
            int initial = item.getItemStart();
            int used = item.getItemUsed();
            int remaining = initial - used;
            remainingTextView.setText(String.valueOf(remaining));
            remainingTextView.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
            remainingTextView.setLayoutParams(params);
            row.addView(remainingTextView);

            table.addView(row);
        }
    }
}
