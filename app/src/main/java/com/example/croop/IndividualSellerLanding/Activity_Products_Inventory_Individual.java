package com.example.croop.IndividualSellerLanding;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.croop.R;
import com.example.croop.model.IndividualSellers;
import com.example.croop.model.IndividualSellersDiscount;
import com.example.croop.model.IndividualSellersProductsInventory;
import com.example.croop.retrofit.RetrofitService;
import com.example.croop.retrofit.UserAPI;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Activity_Products_Inventory_Individual extends AppCompatActivity {
    private TableLayout table;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private RetrofitService RetrofitClient;
    private UserAPI apiService = RetrofitClient.getClient().create(UserAPI.class);
    private StorageReference storageRef;
    private FirebaseFirestore db;

    private static final int REQUEST_CODE_READ_EXTERNAL_STORAGE = 1;
    private static final int RC_IMAGE_PICKER = 100;
    private Uri imageUri;



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
        setContentView(R.layout.products_individual_seller);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
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
        add = findViewById(R.id.addProductButton);
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
                View bottomSheetView = getLayoutInflater().inflate(R.layout.update_delete_products, null);

                ImageView productPic;
                EditText itemName = bottomSheetView.findViewById(R.id.nameText);
                itemName.setText(product.getItemName());
                EditText priceTag = bottomSheetView.findViewById(R.id.priceText);
                priceTag.setText(String.valueOf(product.getPrice()));
                TextView orderText = bottomSheetView.findViewById(R.id.boughtText);
                orderText.setText(String.valueOf(used));
                TextView leftText = bottomSheetView.findViewById(R.id.remainingText);
                leftText.setText(String.valueOf(remaining));

                String itemNameText = itemName.getText().toString().trim();

                ChipGroup chipGroup = bottomSheetView.findViewById(R.id.tagsChipGroup);
                EditText tagInput = bottomSheetView.findViewById(R.id.tagInput);
                Button addTagButton = bottomSheetView.findViewById(R.id.addTagButton);

                productPic = bottomSheetView.findViewById(R.id.addProductProfile);
                storageRef = FirebaseStorage.getInstance().getReference()
                        .child("Products")
                        .child(user.getUid())
                        .child(itemNameText);
                storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    Glide.with(this)
                            .load(uri.toString())
                            .placeholder(R.drawable.logo)
                            .error(R.drawable.sun)
                            .into(productPic);
                }).addOnFailureListener(exception -> {
                    Log.e("FirebaseStorage", "Error getting download URL: " + exception.getMessage());
                    Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
                });

                Spinner unit = bottomSheetView.findViewById(R.id.unitOptions);
                String[] units = {"Kilo", "Sako", "Tumpok", "Piraso"};
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, units) {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        View view = super.getView(position, convertView, parent);
                        TextView textView = view.findViewById(android.R.id.text1);
                        textView.setTextColor(ContextCompat.getColor(getContext(), R.color.beige_brackground));
                        return view;
                    }

                    @Override
                    public View getDropDownView(int position, View convertView, ViewGroup parent) {
                        View view = super.getDropDownView(position, convertView, parent);
                        TextView textView = view.findViewById(android.R.id.text1);
                        textView.setTextColor(ContextCompat.getColor(getContext(), R.color.secondary_color));
                        return view;
                    }
                };
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                unit.setAdapter(adapter);
                if (product.getUnit() != null) {
                    int position = adapter.getPosition(product.getUnit());
                    if (position >= 0) {
                        unit.setSelection(position);
                    } else {
                        Log.e("SpinnerError", "Unit not found in adapter: " + product.getUnit());
                        unit.setSelection(0);
                    }
                } else {
                    unit.setSelection(0);
                }

                db.collection("Product Tags")
                        .document(user.getUid())
                        .get()
                        .addOnSuccessListener(documentSnapshot -> {
                            if (documentSnapshot.exists()) {
                                Map<String, Object> productTags = documentSnapshot.getData();
                                if (productTags != null && productTags.containsKey(itemNameText)) {
                                    List<String> tags = (List<String>) productTags.get(itemNameText);
                                    for (String tag : tags) {
                                        addTag(tag, chipGroup);
                                    }
                                }
                            }
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(Activity_Products_Inventory_Individual.this, "Failed to load tags: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });

                addTagButton.setOnClickListener(v -> {
                    String tagName = tagInput.getText().toString().trim();
                    if (!tagName.isEmpty()) {
                        addTag(tagName, chipGroup); // Add the tag to the ChipGroup
                        tagInput.setText(""); // Clear the input field
                    } else {
                        Toast.makeText(this, "Tag cannot be empty!", Toast.LENGTH_SHORT).show();
                    }
                });

                Button update = bottomSheetView.findViewById(R.id.updateButton);
                update.setOnClickListener(v -> {
                    String itemNameTextUpdate = itemName.getText().toString().trim();
                    String price = priceTag.getText().toString().trim();
                    String order = orderText.getText().toString().trim();

                    if (itemNameTextUpdate.isEmpty()) {
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

                        List<String> updatedTags = new ArrayList<>();
                        for (int i = 0; i < chipGroup.getChildCount(); i++) {
                            View child = chipGroup.getChildAt(i);
                            if (child instanceof Chip) {
                                Chip chip = (Chip) child;
                                updatedTags.add(chip.getText().toString());
                            }
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
                                    Map<String, Object> productTags = new HashMap<>();
                                    productTags.put(itemNameText, updatedTags);

                                    db.collection("Product Tags")
                                            .document(user.getUid())
                                            .set(productTags, SetOptions.merge())
                                            .addOnSuccessListener(aVoid -> {
                                                Toast.makeText(Activity_Products_Inventory_Individual.this, "Tags updated successfully!", Toast.LENGTH_SHORT).show();
                                            })
                                            .addOnFailureListener(e -> {
                                                Toast.makeText(Activity_Products_Inventory_Individual.this, "Error updating tags: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                            });

                                    statusPicture(imageUri, itemNameText);

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

                Button delete = bottomSheetView.findViewById(R.id.deleteButton);
                delete.setOnClickListener(v -> {
                    if (user == null) {
                        Toast.makeText(this, "User not signed in!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String userId = user.getUid();
                    String fileName = itemNameText.trim();
                    storageRef = FirebaseStorage.getInstance().getReference()
                            .child("Products")
                            .child(userId)
                            .child(fileName);
                    Call<IndividualSellersProductsInventory> deleteItem = apiService.deleteIndividualProducts(product.getId());
                    deleteItem.enqueue(new Callback<IndividualSellersProductsInventory>() {
                        @Override
                        public void onResponse(Call<IndividualSellersProductsInventory> call, Response<IndividualSellersProductsInventory> response) {
                            Toast.makeText(Activity_Products_Inventory_Individual.this, "Nabura na ang produkto", Toast.LENGTH_SHORT).show();

                            storageRef.delete()
                                    .addOnSuccessListener(aVoid -> {
                                        System.out.println("File deleted successfully!");
                                        Toast.makeText(Activity_Products_Inventory_Individual.this, "Picture deleted successfully!", Toast.LENGTH_SHORT).show();
                                    })
                                    .addOnFailureListener(e -> {
                                        if (e instanceof StorageException && ((StorageException) e).getErrorCode() == StorageException.ERROR_OBJECT_NOT_FOUND) {
                                            System.out.println("File does not exist. Cannot delete.");
                                            Toast.makeText(Activity_Products_Inventory_Individual.this, "File does not exist. Nothing to delete.", Toast.LENGTH_SHORT).show();
                                        } else {
                                            System.err.println("Failed to delete file: " + e.getMessage());
                                            Toast.makeText(Activity_Products_Inventory_Individual.this, "Failed to delete file: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });

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

                Button check = bottomSheetView.findViewById(R.id.checkButton);
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
                    user = mAuth.getCurrentUser();
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

    private void statusPicture(Uri imageUri, String itemNameText) {
        if (user == null) {
            Toast.makeText(this, "User not signed in!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (imageUri == null) {
            Toast.makeText(this, "Image is processed!", Toast.LENGTH_SHORT).show();
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

    private void addTag(String tagName, ChipGroup chipGroup) {
        Chip chip = new Chip(this);
        chip.setText(tagName);
        chip.setCloseIconVisible(true);

        chip.setOnCloseIconClickListener(v -> {
            chipGroup.removeView(chip); // Remove the chip from the ChipGroup
        });

        chipGroup.addView(chip); // Add the chip to the ChipGroup
    }
}
