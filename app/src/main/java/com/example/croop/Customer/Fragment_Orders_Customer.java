package com.example.croop.Customer;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.croop.R;
import com.example.croop.model.Customer;
import com.example.croop.model.SellerOrdersDTO;
import com.example.croop.retrofit.RetrofitService;
import com.example.croop.retrofit.UserAPI;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Orders_Customer extends Fragment {
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private RetrofitService RetrofitClient;
    private UserAPI userAPI;
    private FirebaseUser user;
    private List<SellerOrdersDTO> orders;
    private TableLayout tableLayout;

    private ProgressBar progressBar; // Reference to the ProgressBar
    private TextView loadingText;   // Reference to the loading text

    int customerID;

    public Fragment_Orders_Customer() {}

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
                    TableRow row = createTableRow(order);
                    tableLayout.addView(row);
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
        String formattedOrderList = formatOrderList(order.getOrderList());
        addTextViewToRow(row, formattedDate, 1f);
        addTextViewToRow(row, formattedOrderList, 1f);
        addTextViewToRow(row, String.valueOf(order.getOrderPrice()), 1f);

        // Add the status TextView and handle its background color
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
                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();
            });
        }

        row.addView(statusTextView);
        return row;
    }

    private String formatDate(Date date) {
        if (date == null) return "N/A";
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
        return sdf.format(date);
    }

    private String formatOrderList(Map<String, Integer> orderList) {
        if (orderList == null || orderList.isEmpty()) {
            return "No items";
        }

        List<String> items = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : orderList.entrySet()) {
            items.add(String.format("%s (%d)", entry.getKey(), entry.getValue()));
        }
        return TextUtils.join(", ", items);
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