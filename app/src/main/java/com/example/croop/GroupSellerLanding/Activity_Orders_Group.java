package com.example.croop.GroupSellerLanding;

import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.croop.R;
import com.example.croop.model.GroupSellersOrders;
import com.example.croop.model.OrderItem;
import com.example.croop.retrofit.RetrofitService;
import com.example.croop.retrofit.UserAPI;
import com.google.common.reflect.TypeToken;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

import java.lang.reflect.Type;
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
        Call<List<GroupSellersOrders>> call = apiService.getGroupSellersOrders(firebaseID);
        call.enqueue(new Callback<List<GroupSellersOrders>>() {
            @Override
            public void onResponse(Call<List<GroupSellersOrders>> call, Response<List<GroupSellersOrders>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<GroupSellersOrders> orders = response.body();
                    Toast.makeText(Activity_Orders_Group.this, "Number of orders fetched: " + orders.size(), Toast.LENGTH_SHORT).show();
                    populateTable(orders);
                }else{
                    Toast.makeText(Activity_Orders_Group.this, "API_ERROR: Response not successful or body is null", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<GroupSellersOrders>> call, Throwable t) {
                Toast.makeText(Activity_Orders_Group.this, "API_ERROR"+ t.toString(), Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

    private void initializeComponents() {
        table = findViewById(R.id.tableLayout);
    }

    private void populateTable(List<GroupSellersOrders> orders) {
        // Clear existing rows (except the header)
        table.removeViews(1, table.getChildCount() - 1);

        for (GroupSellersOrders order : orders) {
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
            int customerId = Integer.parseInt(order.getCustomer().getId());
            Call<String> customerName = apiService.getCustomerName(customerId);
            customerName.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        String customerName = response.body();
                        customerTextView.setText(customerName); // Update the TextView with the customer name
                    } else {
                        customerTextView.setText("Unknown Customer"); // Handle API error
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {

                }
            });
            customerTextView.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
            customerTextView.setLayoutParams(params);
            row.addView(customerTextView);

            TextView orderListTextView = new TextView(this);
            Map<String, Integer> orderListJson = order.getOrderList(); // Assuming this is a JSON string
            String formattedOrderList = formatOrderList(orderListJson.toString()); // Format the JSON
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

    private String formatOrderList(String orderListJson) {
        try {
            Gson gson = new Gson();
            Type listType = new TypeToken<List<OrderItem>>() {}.getType();
            List<OrderItem> orderItems = gson.fromJson(orderListJson, listType);

            StringBuilder formattedList = new StringBuilder();
            for (OrderItem item : orderItems) {
                formattedList.append(item.getItem())
                        .append(" x ")
                        .append(item.getQuantity())
                        .append("\n");
            }
            return formattedList.toString().trim(); // Remove trailing newline
        } catch (Exception e) {
            e.printStackTrace();
            return "Error parsing order list";
        }
    }
}
