package com.example.croop.GroupSellerLanding;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.croop.R;
import com.example.croop.model.SellerOrdersDTO;
import com.example.croop.retrofit.RetrofitService;
import com.example.croop.retrofit.UserAPI;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Activity_Orders_Group extends AppCompatActivity {
    private TableLayout table;
    FirebaseAuth mAuth;
    RetrofitService RetrofitClient;
    UserAPI apiService = RetrofitClient.getClient().create(UserAPI.class);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orders_group_seller);
        initializeComponents();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        // Fetch data from the API
        String firebaseID = user.getUid(); // Replace with the actual Firebase
        Call<List<SellerOrdersDTO>> call = apiService.getGroupSellersOrders(firebaseID);
        call.enqueue(new Callback<List<SellerOrdersDTO>>() {
            @Override
            public void onResponse(Call<List<SellerOrdersDTO>> call, Response<List<SellerOrdersDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<SellerOrdersDTO> orders = response.body();
                    Toast.makeText(Activity_Orders_Group.this, "Number of orders fetched: " + orders.size(), Toast.LENGTH_SHORT).show();
                    populateTable(orders);
                }else{
                    Toast.makeText(Activity_Orders_Group.this, "API_ERROR: Response not successful or body is null", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<SellerOrdersDTO>> call, Throwable t) {
                Toast.makeText(Activity_Orders_Group.this, "API_ERROR"+ t.toString(), Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });

        FloatingActionButton back = findViewById(R.id.backFloat);
        back.setOnClickListener(v -> {
            Intent intent = new Intent(this, Sign_In_Success_Group_Seller.class);
            startActivity(intent);
        });
    }

    private void initializeComponents() {
        table = findViewById(R.id.tableLayout);
    }

    private void populateTable(List<SellerOrdersDTO> orders) {
        // Clear existing rows (except the header)
        table.removeViews(1, table.getChildCount() - 1);

        for (SellerOrdersDTO order : orders) {
            TableRow row = new TableRow(this);
            TableRow.LayoutParams params = new TableRow.LayoutParams(
                    0, // Width: 0 means the width will be determined by the weight
                    TableRow.LayoutParams.WRAP_CONTENT, // Height: Wrap content
                    1.0f // Weight: 1 means equal distribution of space
            );

            TextView idTextView = new TextView(this);
            idTextView.setText(String.valueOf(order.getId()));
            idTextView.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
            idTextView.setLayoutParams(params);
            idTextView.setOnClickListener(view -> {

            });
            row.addView(idTextView);

            TextView dateTextView = new TextView(this);
            dateTextView.setText(order.getOrderDate().toString());
            dateTextView.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
            dateTextView.setLayoutParams(params);
            row.addView(dateTextView);

            TextView customerTextView = new TextView(this);
            customerTextView.setText(order.getCustomerName());
            customerTextView.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
            customerTextView.setLayoutParams(params);
            row.addView(customerTextView);

            TextView orderListTextView = new TextView(this);
            Map<String, Integer> orderList = order.getOrderList(); // Get the orderList Map
            String formattedOrderList = formatOrderList(orderList); // Format the Map into a readable string
            orderListTextView.setText(formattedOrderList);
            orderListTextView.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
            orderListTextView.setLayoutParams(params);
            row.addView(orderListTextView);

            TextView priceTextView = new TextView(this);
            priceTextView.setText(String.valueOf(order.getOrderPrice()));
            priceTextView.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
            priceTextView.setLayoutParams(params);
            row.addView(priceTextView);

            TextView statusTextView = new TextView(this);
            statusTextView.setText(order.getOrderStatus());
            statusTextView.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
            statusTextView.setLayoutParams(params);
            row.addView(statusTextView);

            table.addView(row);
        }
        // Add a new row for each order
    }

    private String formatOrderList(Map<String, Integer> orderList) {
        try {
            StringBuilder formattedList = new StringBuilder();
            for (Map.Entry<String, Integer> entry : orderList.entrySet()) {
                formattedList.append(entry.getKey()) // Item name
                        .append(" - P")
                        .append(entry.getValue()) // Quantity
                        .append("\n"); // Add a newline for readability
            }
            return formattedList.toString().trim(); // Remove trailing newline
        } catch (Exception e) {
            e.printStackTrace();
            return "Error parsing order list";
        }
    }
}
