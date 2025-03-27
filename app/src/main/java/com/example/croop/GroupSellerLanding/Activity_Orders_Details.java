package com.example.croop.GroupSellerLanding;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.croop.R;
import com.example.croop.model.SellerOrdersDTO;
import com.example.croop.retrofit.RetrofitService;
import com.example.croop.retrofit.UserAPI;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Activity_Orders_Details extends AppCompatActivity {
    private TextView customer, orderList, orderPrice, customerLocation, orderIDDisplay;
    private FloatingActionButton back;
    private Intent intent;
    private int orderID;
    private RetrofitService RetrofitClient;
    private UserAPI userAPI;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_details);
        intent = getIntent();
        orderID = intent.getIntExtra("order_id", 0);
        initializeComponents();
    }

    private void initializeComponents() {
        orderIDDisplay = findViewById(R.id.orderIDText);
        customer = findViewById(R.id.orderCustomerText);
        orderList = findViewById(R.id.orderListText);
        orderPrice = findViewById(R.id.orderPriceText);
        customerLocation = findViewById(R.id.orderCustomerLocation);

        userAPI = RetrofitClient.getClient().create(UserAPI.class);
        Call<SellerOrdersDTO> orderCall = userAPI.getGroupOrder(orderID);
        orderCall.enqueue(new Callback<SellerOrdersDTO>() {
            @Override
            public void onResponse(Call<SellerOrdersDTO> call, Response<SellerOrdersDTO> response) {
                SellerOrdersDTO customerOrdersForGroupSellers = response.body();
                orderIDDisplay.setText("ORDER: #" + String.valueOf(customerOrdersForGroupSellers.getId()));
                customer.setText(customerOrdersForGroupSellers.getCustomerName());
                Map<String, Integer> orders = customerOrdersForGroupSellers.getOrderList();
                String formattedOrderList = formatOrderList(orders);
                orderList.setText(formattedOrderList);
                orderPrice.setText("₱" + customerOrdersForGroupSellers.getOrderPrice());
                Map<String, String> address = customerOrdersForGroupSellers.getAddress();
                String formatAddress = formatAddress(address);
                customerLocation.setText(formatAddress);
            }

            @Override
            public void onFailure(Call<SellerOrdersDTO> call, Throwable t) {

            }
        });

        back = findViewById(R.id.backFloat);
        back.setOnClickListener(v -> {
            onBackPressed();
        });
    }

    private String formatOrderList(Map<String, Integer> orderList) {
        try {
            StringBuilder formattedList = new StringBuilder();
            for (Map.Entry<String, Integer> entry : orderList.entrySet()) {
                formattedList.append(entry.getKey()) // Item name
                        .append(" - ")
                        .append(entry.getValue()) // Quantity
                        .append("\n"); // Add a newline for readability
            }
            return formattedList.toString().trim(); // Remove trailing newline
        } catch (Exception e) {
            e.printStackTrace();
            return "Error parsing order list";
        }
    }

    private String formatAddress(Map<String, String> address) {
        try {
            StringBuilder formattedList = new StringBuilder();
            for (Map.Entry<String, String> entry : address.entrySet()) {
                formattedList.append(entry.getValue())
                        .append(", ");
            }
            return formattedList.toString().trim();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error parsing order list";
        }
    }
}
