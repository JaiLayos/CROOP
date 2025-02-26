package com.example.croop.GroupSellerLanding;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.croop.R;
import com.example.croop.model.GroupSellers;
import com.example.croop.model.GroupSellersDiscount;
import com.example.croop.model.GroupSellersProductsInventory;
import com.example.croop.retrofit.RetrofitService;
import com.example.croop.retrofit.UserAPI;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Activity_Products_Inventory extends AppCompatActivity {
    private static final int RC_IMAGE_PICKER = 100;
    private Uri imageUri;
    private TableLayout table;
    FirebaseAuth mAuth;
    FirebaseUser user;
    RetrofitService RetrofitClient;
    UserAPI apiService = RetrofitClient.getClient().create(UserAPI.class);

    private static final int REQUEST_CODE_READ_EXTERNAL_STORAGE = 1;

    private void checkAndRequestPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_CODE_READ_EXTERNAL_STORAGE
            );
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_READ_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission denied. Cannot access images.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.products_group_seller);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        checkAndRequestPermissions();
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
                Button picture = bottomSheetView.findViewById(R.id.uploadPicButton);
                picture.setOnClickListener(v1 -> {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, RC_IMAGE_PICKER);
                });
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
                                    statusPicture(imageUri, itemNameText);
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
                    String itemNameText = itemName.getText().toString().trim();
                    if (user == null) {
                        Toast.makeText(this, "User not signed in!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String userId = user.getUid();
                    String fileName = itemNameText.trim();
                    StorageReference storageRef = FirebaseStorage.getInstance().getReference()
                            .child("Products")
                            .child(userId)
                            .child(fileName);

                    // Debug log
                    System.out.println("Deleting image at: " + storageRef.getPath());

                    // Delete the file
                    Call<GroupSellersProductsInventory> deleteItem = apiService.deleteProducts(product.getId());
                    deleteItem.enqueue(new Callback<GroupSellersProductsInventory>() {
                        @Override
                        public void onResponse(Call<GroupSellersProductsInventory> call, Response<GroupSellersProductsInventory> response) {
                            Toast.makeText(Activity_Products_Inventory.this, "Products Deleted", Toast.LENGTH_SHORT).show();
                            storageRef.delete()
                                    .addOnSuccessListener(aVoid -> {
                                        System.out.println("File deleted successfully!");
                                        Toast.makeText(Activity_Products_Inventory.this, "Picture deleted successfully!", Toast.LENGTH_SHORT).show();
                                    })
                                    .addOnFailureListener(e -> {
                                        if (e instanceof StorageException && ((StorageException) e).getErrorCode() == StorageException.ERROR_OBJECT_NOT_FOUND) {
                                            System.out.println("File does not exist. Cannot delete.");
                                            Toast.makeText(Activity_Products_Inventory.this, "File does not exist. Nothing to delete.", Toast.LENGTH_SHORT).show();
                                        } else {
                                            System.err.println("Failed to delete file: " + e.getMessage());
                                            Toast.makeText(Activity_Products_Inventory.this, "Failed to delete file: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
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
            if (product.getGroupSellerDiscounts() != null) {
                discount = String.valueOf(product.getGroupSellerDiscounts().getDiscountPercent());
            }
            promoView.setText(discount);
            promoView.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
            promoView.setLayoutParams(params);
            promoView.setTextColor(getResources().getColor(R.color.highlight_green));
            promoView.setTypeface(null, Typeface.ITALIC);
            promoView.setOnClickListener(v -> {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(Activity_Products_Inventory.this);

                View bottomSheetView = LayoutInflater.from(Activity_Products_Inventory.this)
                        .inflate(R.layout.add_discount_group, null);

                EditText discountText = bottomSheetView.findViewById(R.id.discountText);
                Button save = bottomSheetView.findViewById(R.id.saveButton);

                save.setOnClickListener(v1 -> {
                    GroupSellersDiscount groupSellersDiscount = new GroupSellersDiscount();
                    FirebaseUser user = mAuth.getCurrentUser();
                    Call<GroupSellers> getGSellers = apiService.getGroupSellersbyFirebaseID(user.getUid());
                    getGSellers.enqueue(new Callback<GroupSellers>() {
                        @Override
                        public void onResponse(Call<GroupSellers> call, Response<GroupSellers> response) {
                            groupSellersDiscount.setGroupSellers(response.body());
                        }

                        @Override
                        public void onFailure(Call<GroupSellers> call, Throwable t) {
                            Toast.makeText(Activity_Products_Inventory.this, "wala", Toast.LENGTH_SHORT).show();
                        }
                    });

                    groupSellersDiscount.setGroupSellersProductsInventory(product);
                    groupSellersDiscount.setDiscountPercent(Double.parseDouble(discountText.getText().toString()));
                    Call<GroupSellersDiscount> updateDiscountCall = apiService.updateDiscount(
                            product.getGroupSellerDiscounts().getId(),
                            groupSellersDiscount
                    );
                    updateDiscountCall.enqueue(new Callback<GroupSellersDiscount>() {
                        @Override
                        public void onResponse(Call<GroupSellersDiscount> call, Response<GroupSellersDiscount> response) {
                            Intent intent = new Intent(Activity_Products_Inventory.this, Activity_Discount_Inventory.class);
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void onFailure(Call<GroupSellersDiscount> call, Throwable t) {
                            Toast.makeText(Activity_Products_Inventory.this, t.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void statusPicture(Uri imageUri, String itemNameText) {
        if (user == null) {
            Toast.makeText(this, "User not signed in!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (imageUri == null) {
            Toast.makeText(this, "Image URI is null!", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = user.getUid();
        String fileName = itemNameText.trim();
        StorageReference storageRef = FirebaseStorage.getInstance().getReference()
                .child("Products")
                .child(userId)
                .child(fileName);
        storageRef.getMetadata()
                .addOnSuccessListener(storageMetadata -> {
                    // File exists, proceed with updating
                    System.out.println("File exists. Updating...");
                    storageRef.putFile(imageUri)
                            .addOnSuccessListener(taskSnapshot -> {
                                System.out.println("Update successful!");
                                storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                                    System.out.println("Updated Download URL: " + uri.toString());
                                    Toast.makeText(this, "Picture updated successfully!", Toast.LENGTH_SHORT).show();
                                }).addOnFailureListener(e -> {
                                    System.err.println("Failed to get download URL: " + e.getMessage());
                                    Toast.makeText(this, "Failed to get download URL: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                            })
                            .addOnFailureListener(e -> {
                                System.err.println("Failed to update image: " + e.getMessage());
                                Toast.makeText(this, "Failed to update image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                })
                .addOnFailureListener(e -> {
                    // File does not exist, notify the user
                    if (e instanceof StorageException && ((StorageException) e).getErrorCode() == StorageException.ERROR_OBJECT_NOT_FOUND) {
                        System.out.println("File does not exist. Cannot update.");
                        Toast.makeText(this, "File does not exist. Please upload a new picture.", Toast.LENGTH_SHORT).show();
                    } else {
                        // Handle other errors
                        System.err.println("Error checking file existence: " + e.getMessage());
                        Toast.makeText(this, "Error checking file existence: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


}
