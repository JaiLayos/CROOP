package com.example.croop.Customer;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.croop.R;
import com.example.croop.model.Customer;
import com.example.croop.model.GroupSellers;
import com.example.croop.model.GroupSellersProductsInventory;
import com.example.croop.model.SellerOrdersDTO;
import com.example.croop.retrofit.RetrofitService;
import com.example.croop.retrofit.UserAPI;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Orders_Completed extends Fragment {
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private RetrofitService RetrofitClient;
    private UserAPI userAPI;
    private FirebaseUser user;
    private List<SellerOrdersDTO> orders;
    private TableLayout tableLayout;

    private ProgressBar progressBar; // Reference to the ProgressBar
    private TextView loadingText;   // Reference to the loading text

    private int customerID, openProductID;
    private String openFirebaseID, returnTheRole;

    public Fragment_Orders_Completed() {}

    @Override
    public void onResume() {
        super.onResume();
        refreshOrders(); // Reload data every time the fragment is shown
    }

    private void refreshOrders() {
        orders = new ArrayList<>();
        showLoading(true); // Show loading indicator
        loadTable();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.customer_order_list, container, false);

        SharedPreferences prefs = getActivity().getSharedPreferences("user_prefs", getContext().MODE_PRIVATE);
        String collection = prefs.getString("user_collection", null);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        user = mAuth.getCurrentUser();

        userAPI = RetrofitClient.getClient().create(UserAPI.class);

        // Initialize views
        tableLayout = rootView.findViewById(R.id.orderTable);
        progressBar = rootView.findViewById(R.id.progressBar);
        loadingText = rootView.findViewById(R.id.loadingText);

        return rootView;
    }

    private void loadTable() {
        Call<Customer> customerIDcall = userAPI.getCustomerByFirebaseID(user.getUid());
        customerIDcall.enqueue(new Callback<Customer>() {
            @Override
            public void onResponse(Call<Customer> call, Response<Customer> response) {
                Customer customer = response.body();
                if (customer != null) {
                    customerID = customer.getId();
                    Call<List<SellerOrdersDTO>> getGroupOrderByCustomer = userAPI.getGroupOrderByCustomer(customerID);
                    getGroupOrderByCustomer.enqueue(new Callback<List<SellerOrdersDTO>>() {
                        @Override
                        public void onResponse(Call<List<SellerOrdersDTO>> call, Response<List<SellerOrdersDTO>> response) {
                            orders = response.body();
                            getIndividualOrders(customerID, tableLayout, orders);
                        }

                        @Override
                        public void onFailure(Call<List<SellerOrdersDTO>> call, Throwable t) {
                            showLoading(false); // Hide loading indicator on failure
                        }
                    });
                } else {
                    showLoading(false); // Hide loading indicator if customer ID is null
                }
            }

            @Override
            public void onFailure(Call<Customer> call, Throwable t) {
                showLoading(false); // Hide loading indicator on failure
            }
        });
    }

    private void getIndividualOrders(int customerID, TableLayout tableLayout, List<SellerOrdersDTO> orders) {
        Call<List<SellerOrdersDTO>> individualOrdersCall = userAPI.getIndividualOrderByCustomer(customerID);
        individualOrdersCall.enqueue(new Callback<List<SellerOrdersDTO>>() {
            @Override
            public void onResponse(Call<List<SellerOrdersDTO>> call, Response<List<SellerOrdersDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    for (SellerOrdersDTO indivOrders : response.body()) {
                        orders.add(indivOrders);
                    }
                    populateTable(tableLayout, orders);
                }
                showLoading(false); // Hide loading indicator after data is loaded
            }

            @Override
            public void onFailure(Call<List<SellerOrdersDTO>> call, Throwable t) {
                showLoading(false); // Hide loading indicator on failure
            }
        });
    }

    private void populateTable(TableLayout tableLayout, List<SellerOrdersDTO> orders) {
        tableLayout.post(() -> {
            tableLayout.removeViews(1, tableLayout.getChildCount() - 1);

            if (!orders.isEmpty()) {
                for (SellerOrdersDTO order : orders) {
                    if(order.getOrderStatus().equals("Completed")){
                        TableRow row = createTableRow(order);
                        tableLayout.addView(row);
                    }
                }
            } else {
                showNoDataMessage(tableLayout);
            }
            showLoading(false); // Hide loading indicator after populating the table
        });
    }

    private TableRow createTableRow(SellerOrdersDTO order) {
        TableRow row = new TableRow(getContext());
        TableRow.LayoutParams params = new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT
        );
        int marginInPixels = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                8,
                getResources().getDisplayMetrics()
        );
        params.setMargins(0, 0, 0, marginInPixels);
        row.setLayoutParams(params);

        String formattedDate = formatDate(order.getOrderDate());
        addTextViewToRow(row, formattedDate, 1f);

        Map<String, Integer> orderList = order.getOrderList();
        if (orderList == null || orderList.isEmpty()) {
            addTextViewToRow(row, "No items", 1f);
        } else {
            LinearLayout orderListContainer = new LinearLayout(getContext());
            orderListContainer.setOrientation(LinearLayout.VERTICAL);
            orderListContainer.setLayoutParams(new TableRow.LayoutParams(
                    0,
                    TableRow.LayoutParams.WRAP_CONTENT,
                    1f
            ));

            for (Map.Entry<String, Integer> entry : orderList.entrySet()) {
                String cropName = entry.getKey();
                int quantity = entry.getValue();

                TextView cropTextView = new TextView(getContext());
                cropTextView.setText(String.format("%s (%d)", cropName, quantity));
                cropTextView.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                ));
                cropTextView.setPadding(4, 0, 4, 0);

                if ("Completed".equals(order.getOrderStatus())) {
                    SpannableString spannableString = new SpannableString(cropTextView.getText());
                    spannableString.setSpan(
                            new ForegroundColorSpan(getResources().getColor(R.color.highlight_green)),
                            0, cropName.length(), // Apply green color only to the crop name
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    );
                    cropTextView.setText(spannableString);

                    cropTextView.setOnClickListener(v -> {
                        if(order.getSellerType().equals("group")){
                            int sellerID = order.getSellerID();
                            Call<List<GroupSellersProductsInventory>> groupSellersProductsInventoryCall = userAPI.getItemByNameByGroupID(cropName, sellerID);
                            groupSellersProductsInventoryCall.enqueue(new Callback<List<GroupSellersProductsInventory>>() {
                                @Override
                                public void onResponse(Call<List<GroupSellersProductsInventory>> call, Response<List<GroupSellersProductsInventory>> response) {
                                    List<GroupSellersProductsInventory> groupSellersProductsInventories = response.body();
                                    for(GroupSellersProductsInventory groupSellersProductsInventory : groupSellersProductsInventories){
                                        openProductID = groupSellersProductsInventory.getId();
                                        Call<GroupSellers> groupSellersCall = userAPI.getGroupSellers(sellerID);
                                        groupSellersCall.enqueue(new Callback<GroupSellers>() {
                                            @Override
                                            public void onResponse(Call<GroupSellers> call, Response<GroupSellers> response) {
                                                List<String> collectionsToSearch = Arrays.asList("Farming Association", "Farming Cooperatives");
                                                GroupSellers groupSellers = response.body();
                                                openFirebaseID = groupSellers.getFirebaseID();
                                                for (String collection : collectionsToSearch) {
                                                    db.collection(collection).document(openFirebaseID).get()
                                                            .addOnSuccessListener(documentSnapshot -> {
                                                                if (documentSnapshot.exists()) {
                                                                    returnTheRole = collection;
                                                                    String seller_role = getCollection(returnTheRole);

                                                                    Intent intent = new Intent(getContext(), Activity_Product_Profile.class);
                                                                    intent.putExtra("product_id", openProductID);
                                                                    intent.putExtra("firebase_id",openFirebaseID);
                                                                    intent.putExtra("seller", seller_role);
                                                                    startActivity(intent);

                                                                } else {
                                                                    Log.d("Firestore Search", "Document not found in collection: " + collection);
                                                                }
                                                            })
                                                            .addOnFailureListener(e -> {
                                                                Log.e("Firestore Search Error", "Error searching in collection " + collection + ": " + e.getMessage());
                                                            });
                                                }
                                                /**

                                                 */
                                            }

                                            @Override
                                            public void onFailure(Call<GroupSellers> call, Throwable t) {
                                                Log.e("Group Seller Error: ", t.getMessage());
                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onFailure(Call<List<GroupSellersProductsInventory>> call, Throwable t) {
                                    Log.e("Group Seller Products Error: ", t.getMessage());
                                }
                            });
                        }

                    });
                }
                orderListContainer.addView(cropTextView);
            }

            row.addView(orderListContainer);
        }

        addTextViewToRow(row, String.valueOf(order.getOrderPrice()), 1f);

        TextView statusTextView = new TextView(getContext());
        statusTextView.setText(order.getOrderStatus());
        statusTextView.setGravity(Gravity.CENTER); // Center-align the text
        statusTextView.setLayoutParams(new TableRow.LayoutParams(
                0,
                TableRow.LayoutParams.WRAP_CONTENT,
                1f
        ));

        if ("In Transit".equals(order.getOrderStatus())) {
            statusTextView.setTextColor(getResources().getColor(R.color.highlight_green));
            statusTextView.setOnClickListener(v -> {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity());
                View bottomSheetView = getLayoutInflater().inflate(R.layout.delivery_details, null);
                TextView delivery;
                delivery = bottomSheetView.findViewById(R.id.deliveryLink);
                delivery.setText(order.getDeliveryDetails());
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(delivery.getText().toString()));
                startActivity(intent); // Open the link in a browser
                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();
            });

        }

        if ("Completed".equals(order.getOrderStatus())) {
            statusTextView.setTextColor(getResources().getColor(R.color.highlight_green));
            statusTextView.setOnClickListener(v -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("RATINGS");
                builder.setMessage("It would be appreciated if you rate the products!");
                builder.setPositiveButton("See Receipt", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getActivity(), Activity_Orders_Receipt.class);
                        intent.putExtra("order_id", order.getId());
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            });
        }

        row.addView(statusTextView);
        return row;
    }

    private String getCollection(String role) {
        String collection;
        switch (role) {
            case "Farming Association":
                collection = "Group Business User (Association)";
                break;
            case "Farming Cooperatives":
                collection = "Group Business User (Cooperative)";
                break;
            case "Individual Business User":
                collection = "Individual Sellers";
                break;
            case "Individual Customer User":
                collection = "Customers";
                break;
            default:
                collection = "Unknown";
                break;
        }
        return collection;
    }

    private String formatDate(Date date) {
        if (date == null) return "N/A";
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
        return sdf.format(date);
    }

    private void addTextViewToRow(TableRow row, String text, float weight) {
        TextView tv = new TextView(getContext());
        tv.setText(text);
        tv.setGravity(Gravity.CENTER);
        tv.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, weight));
        row.addView(tv);
    }

    private void showNoDataMessage(TableLayout tableLayout) {
        TableRow row = new TableRow(getContext());
        TextView tv = new TextView(getContext());
        tv.setText("No orders found");
        tv.setGravity(Gravity.CENTER);

        TableRow.LayoutParams params = new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT
        );

        int marginInPixels = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                8,
                getResources().getDisplayMetrics()
        );
        params.setMargins(marginInPixels, marginInPixels, marginInPixels, marginInPixels);
        tv.setLayoutParams(params);
        row.addView(tv);
        tableLayout.addView(row);
    }

    private void showLoading(boolean isLoading) {
        if (isLoading) {
            progressBar.setVisibility(View.VISIBLE);
            loadingText.setVisibility(View.VISIBLE);
            tableLayout.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            loadingText.setVisibility(View.GONE);
            tableLayout.setVisibility(View.VISIBLE);
        }
    }
}

