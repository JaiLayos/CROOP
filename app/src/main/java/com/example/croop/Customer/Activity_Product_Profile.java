package com.example.croop.Customer;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.croop.R;
import com.example.croop.model.Cart;
import com.example.croop.model.Customer;
import com.example.croop.model.GroupSellers;
import com.example.croop.model.GroupSellersProductsInventory;
import com.example.croop.model.IndividualSellers;
import com.example.croop.model.IndividualSellersProductsInventory;
import com.example.croop.model.ProductDTO;
import com.example.croop.retrofit.RetrofitService;
import com.example.croop.retrofit.UserAPI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        sellerNameText, sellerRoleText, comments;
    private FloatingActionButton back;
    private Button addToCart, postComment;
    private Cart cart;
    private ProductDTO store;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private int count;
    private BottomSheetDialog bottomSheetDialog;
    private FirebaseFirestore db;
    private RecyclerView forComment;
    private String name_user, collection;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_profile);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        initializeComponents();
    }

    private void initializeComponents() {
        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String role = prefs.getString("user_role", null);
        collection = getCollection(role);
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

        forComment = findViewById(R.id.commentList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        forComment.setLayoutManager(layoutManager);

        productProfile = findViewById(R.id.productProfile);
        sellerProfile = findViewById(R.id.sellerProfile);
        comments = findViewById(R.id.commentTextBox);
        postComment = findViewById(R.id.postButton);

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
        int productID = productDTO.getProductID();
        String kindOfSeller = productDTO.getSellerRole();
        String firebase_id = productDTO.getFirebaseID();
        Intent intent = new Intent(this, Activity_Product_Profile.class);
        intent.putExtra("product_id", productID);
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

    private String getCollection(String role) {
        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
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
            case "Customer User":
                collection = "Customers";
                break;
            default:
                collection = "Unknown";
                break;
        }

        editor.putString("user_collection", collection).apply();
        return collection;
    }

    private void ifSuccess(Response<ProductDTO> response) {
        ProductDTO products = response.body();
        store = products;
        priceText.setText("₱" + products.getProductPrice());
        discountText.setText(String.valueOf(products.getProductDiscount() * 100)+"%");
        cropNameText.setText(products.getProductName()+ " per " + products.getUnit());
        sellerNameText.setText(products.getProductSeller());
        if (user != null) {
            DocumentReference docRef = db.collection(collection).document(user.getUid());
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            name_user = document.getString("Name");
                        } else {
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });
        }
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
            Intent openThruName = new Intent(this,Activity_Seller_Profile.class);
            String kindOfSeller = products.getSellerRole();
            String firebaseID = products.getFirebaseID();
            int id = products.getSellerID();
            openThruName.putExtra("seller", kindOfSeller);
            openThruName.putExtra("seller_id", id);
            openThruName.putExtra("firebase_id", firebaseID);
            startActivity(openThruName);
        });
        postComment.setOnClickListener(v -> {
            String comment = comments.getText().toString();
            db.collection("Comments on Products")
                    .document(products.getSellerRole() + products.getProductID())
                    .update(name_user, comment)  // Append the comment
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(Activity_Product_Profile.this, "Done!", Toast.LENGTH_SHORT).show();
                        showComments(products);  // Refresh comments after posting
                    })
                    .addOnFailureListener(e -> {
                        Map<String, Object> newComment = new HashMap<>();
                        newComment.put(name_user, comment);
                        db.collection("Comments on Products")
                                .document(products.getSellerRole() + products.getProductID())
                                .set(newComment)  // Create a new document
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(Activity_Product_Profile.this, "Done!", Toast.LENGTH_SHORT).show();
                                    showComments(products);
                                })
                                .addOnFailureListener(err -> {
                                    Toast.makeText(Activity_Product_Profile.this, "Error! " + err, Toast.LENGTH_SHORT).show();
                                });
                    });
        });
        if(products!=null){
            showComments(products);
        }
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
            openBottomView(products);
        });
    }

    private void showComments(ProductDTO products) {
        db.collection("Comments on Products")
                .document(products.getSellerRole() + products.getProductID())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Map<String, Object> commentsMap = documentSnapshot.getData();
                        if (commentsMap != null && !commentsMap.isEmpty()) {
                            List<Map<String, String>> commentsList = new ArrayList<>();

                            // Convert Firestore Map into a list of key-value pairs (userID -> comment)
                            for (Map.Entry<String, Object> entry : commentsMap.entrySet()) {
                                Map<String, String> comment = new HashMap<>();
                                comment.put(entry.getKey(), entry.getValue().toString());  // Use "comment" as key
                                commentsList.add(comment);
                            }

                            CommentsAdapter commentsAdapter = new CommentsAdapter(commentsList, this);
                            forComment.setAdapter(commentsAdapter);
                        } else {
                            Log.e("FirestoreError", "No comments found");
                        }
                    } else {
                        Log.e("FirestoreError", "Document does not exist");
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("FirestoreError", "Error fetching comments: " + e.getMessage());
                });
    }


    private void openBottomView(ProductDTO products) {
        bottomSheetDialog = new BottomSheetDialog(this);
        View bottomSheetView = getLayoutInflater().inflate(R.layout.customer_products_quantity, null);

        EditText quantity = bottomSheetView.findViewById(R.id.quantityText);
        quantity.setInputType(InputType.TYPE_CLASS_NUMBER);
        Button finalize = bottomSheetView.findViewById(R.id.cartButton);
        finalize.setOnClickListener(v -> {
            count = Integer.parseInt(quantity.getText().toString());
            cart = new Cart();
            cart.setCropID(products.getProductID());
            cart.setCropName(products.getProductName());
            cart.setQuantity(count);
            cart.setPrice(cart.getQuantity() * products.getProductPrice());
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


        });
        // Show the bottom sheet dialog
        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
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
                if (response.isSuccessful()) {
                    Toast.makeText(Activity_Product_Profile.this,
                            "Added to Cart!",
                            Toast.LENGTH_SHORT).show();
                    bottomSheetDialog.dismiss();
                } else {
                    try {
                        String errorBody = response.errorBody().string();
                        if (response.code() == 409) {
                            Toast.makeText(Activity_Product_Profile.this,
                                    "Item already in cart",
                                    Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(Activity_Product_Profile.this,
                                    "Error: " + errorBody,
                                    Toast.LENGTH_LONG).show();
                        }
                    } catch (IOException e) {
                        Toast.makeText(Activity_Product_Profile.this,
                                "Failed to read server response",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Cart> call, Throwable t) {
                Toast.makeText(Activity_Product_Profile.this,
                        "Network error: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void findIndividualSellerByID(Integer body) {
        Call<IndividualSellers> individualSellersCall = userAPI.getIndividualSellers(body);
        individualSellersCall.enqueue(new Callback<IndividualSellers>() {
            @Override
            public void onResponse(Call<IndividualSellers> call, Response<IndividualSellers> response) {
                if(response.isSuccessful() && response != null){
                    IndividualSellers individualSellers = response.body();
                    Call<List<IndividualSellersProductsInventory>> productsInventoryCall = userAPI.getIndividualProductsByName(cart.getCropName());
                    productsInventoryCall.enqueue(new Callback<List<IndividualSellersProductsInventory>>() {
                        @Override
                        public void onResponse(Call<List<IndividualSellersProductsInventory>> call, Response<List<IndividualSellersProductsInventory>> response) {
                            if(response.isSuccessful()){
                                List<IndividualSellersProductsInventory> productsInventories = response.body();
                                for(IndividualSellersProductsInventory productsInventory : productsInventories){
                                    if(cart.getQuantity()< productsInventory.getItemRemaining()-(productsInventory.getItemRemaining()*0.25)){
                                        cart.setIndividualSellers(individualSellers);
                                        findCustomer();
                                    }else{
                                        Toast.makeText(Activity_Product_Profile.this, "Quantity exceeded the threshold", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<List<IndividualSellersProductsInventory>> call, Throwable t) {
                            Log.e("Finding Group Seller Error for Quantity Compatison: ", t.getMessage());
                        }
                    });
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
                    GroupSellers groupSellers = response.body();
                    Call<List<GroupSellersProductsInventory>> productsInventoryCall = userAPI.getProductsByName(cart.getCropName());
                    productsInventoryCall.enqueue(new Callback<List<GroupSellersProductsInventory>>() {
                        @Override
                        public void onResponse(Call<List<GroupSellersProductsInventory>> call, Response<List<GroupSellersProductsInventory>> response) {
                            if(response.isSuccessful()){
                                List<GroupSellersProductsInventory> productsInventories = response.body();
                                for(GroupSellersProductsInventory productsInventory : productsInventories){
                                    if(cart.getQuantity()< productsInventory.getItemRemaining()-(productsInventory.getItemRemaining()*0.25)){
                                        cart.setGroupSellers(groupSellers);
                                        findCustomer();
                                    }else{
                                        Toast.makeText(Activity_Product_Profile.this, "Quantity exceeded the threshold", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<List<GroupSellersProductsInventory>> call, Throwable t) {
                            Log.e("Finding Group Seller Error for Quantity Compatison: ", t.getMessage());
                        }
                    });
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
