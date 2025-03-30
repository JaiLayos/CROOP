package com.example.croop.GroupSellerLanding;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.croop.R;
import com.example.croop.model.CustomerOrdersForGroupSellers;
import com.example.croop.model.GroupSellers;
import com.example.croop.model.SellerOrdersDTO;
import com.example.croop.retrofit.RetrofitService;
import com.example.croop.retrofit.UserAPI;
import com.google.android.material.bottomsheet.BottomSheetDialog;
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
    private FirebaseAuth mAuth;
    private RetrofitService RetrofitClient;
    private UserAPI apiService = RetrofitClient.getClient().create(UserAPI.class);
    private int groupID;
    private CustomerOrdersForGroupSellers customerOrdersForGroupSellers;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orders_group_seller);
        initializeComponents();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        String firebaseID = user.getUid();
        Call<GroupSellers> groupSellersCall = apiService.getGroupSellersbyFirebaseID(user.getUid());
        groupSellersCall.enqueue(new Callback<GroupSellers>() {
            @Override
            public void onResponse(Call<GroupSellers> call, Response<GroupSellers> response) {
                GroupSellers groupSellers = response.body();
                groupID = groupSellers.getId();
                getGroupOrdersBySellerID(groupID);
            }

            @Override
            public void onFailure(Call<GroupSellers> call, Throwable t) {
                Toast.makeText(Activity_Orders_Group.this, "Getting Group Seller ID Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        FloatingActionButton back = findViewById(R.id.backFloat);
        back.setOnClickListener(v -> {
            onBackPressed();
        });
    }

    private void getGroupOrdersBySellerID(int id) {
        Call<List<SellerOrdersDTO>> call = apiService.getGroupOrderByGroupSellerId(id);
        call.enqueue(new Callback<List<SellerOrdersDTO>>() {
            @Override
            public void onResponse(Call<List<SellerOrdersDTO>> call, Response<List<SellerOrdersDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<SellerOrdersDTO> orders = response.body();
                    Toast.makeText(Activity_Orders_Group.this, "Bilang ng mga order na nakuha: " + orders.size(), Toast.LENGTH_SHORT).show();
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
    }

    private void initializeComponents() {
        table = findViewById(R.id.tableLayout);
    }

    private void populateTable(List<SellerOrdersDTO> orders) {
        // Clear existing rows (except the header)
        table.removeViews(1, table.getChildCount() - 1);
        CustomerOrdersForGroupSellers customerOrders;
        for (SellerOrdersDTO order : orders) {
            TableRow row = new TableRow(this);
            TableRow.LayoutParams params = new TableRow.LayoutParams(
                    0,
                    TableRow.LayoutParams.WRAP_CONTENT,
                    1.0f
            );

            TextView orderListTextView = new TextView(this);
            Map<String, Integer> orderList = order.getOrderList(); // Get the orderList Map
            String formattedOrderList = formatOrderList(orderList); // Format the Map into a readable string
            orderListTextView.setText(formattedOrderList);
            orderListTextView.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
            orderListTextView.setGravity(Gravity.CENTER);
            orderListTextView.setLayoutParams(params);
            orderListTextView.setTextColor(getResources().getColor(R.color.highlight_green));
            orderListTextView.setOnClickListener(v -> {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
                View bottomSheetView = getLayoutInflater().inflate(R.layout.delivery_edit, null);
                TextView delivery;
                delivery = bottomSheetView.findViewById(R.id.deliveryLink);

                Button update;
                update = bottomSheetView.findViewById(R.id.updateButton);
                update.setOnClickListener(view -> {
                    Call<SellerOrdersDTO> getOrder = apiService.getGroupOrder(order.getId());
                    String deliveryDetails = delivery.getText().toString();
                    getOrder.enqueue(new Callback<SellerOrdersDTO>() {
                        @Override
                        public void onResponse(Call<SellerOrdersDTO> call, Response<SellerOrdersDTO> response) {
                            SellerOrdersDTO customerOrders = response.body();
                            customerOrders.setOrderList(order.getOrderList());
                            customerOrders.setOrderPrice(order.getOrderPrice());
                            customerOrders.setDeliveryDetails(deliveryDetails);
                            Call<SellerOrdersDTO> updateCall = apiService.updateGroupOrder(order.getId(),customerOrders);
                            updateCall.enqueue(new Callback<SellerOrdersDTO>() {
                                @Override
                                public void onResponse(Call<SellerOrdersDTO> call, Response<SellerOrdersDTO> response) {
                                    Toast.makeText(Activity_Orders_Group.this, "Delivery: "+deliveryDetails, Toast.LENGTH_SHORT).show();
                                    bottomSheetDialog.dismiss();
                                    Intent intent = new Intent(Activity_Orders_Group.this, Activity_Orders_Group.class);
                                    startActivity(intent);
                                    finish();
                                }

                                @Override
                                public void onFailure(Call<SellerOrdersDTO> call, Throwable t) {
                                    Log.e("Updating order error: ", t.getMessage());
                                }
                            });
                        }

                        @Override
                        public void onFailure(Call<SellerOrdersDTO> call, Throwable t) {
                            Log.e("Getting order error: ", t.getMessage());
                        }
                    });
                });

                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();
            });
            row.addView(orderListTextView);

            TextView customerTextView = new TextView(this);
            customerTextView.setText(order.getCustomerName());
            customerTextView.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
            customerTextView.setGravity(Gravity.CENTER);
            customerTextView.setLayoutParams(params);
            row.addView(customerTextView);

            TextView priceTextView = new TextView(this);
            priceTextView.setText(String.valueOf(order.getOrderPrice()));
            priceTextView.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
            priceTextView.setGravity(Gravity.CENTER);
            priceTextView.setLayoutParams(params);
            row.addView(priceTextView);

            TextView statusTextView = new TextView(this);
            statusTextView.setText(order.getOrderStatus());
            statusTextView.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
            statusTextView.setGravity(Gravity.CENTER);
            statusTextView.setLayoutParams(params);
            statusTextView.setTextColor(getResources().getColor(R.color.highlight_green));
            statusTextView.setOnClickListener(v -> {
                if (order.getDeliveryDetails() == null || order.getDeliveryDetails().isEmpty()) {
                    Toast.makeText(Activity_Orders_Group.this,
                            "Mangyaring ayusin muna ang delivery ng mga produkto bago ma-update ang status ng order.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                radioButton(order);
            });
            row.addView(statusTextView);

            TextView seeMoreTextView = new TextView(this);
            seeMoreTextView.setText("More...");
            seeMoreTextView.setTextColor(getResources().getColor(R.color.highlight_green));
            seeMoreTextView.setGravity(Gravity.CENTER);
            seeMoreTextView.setLayoutParams(params);
            seeMoreTextView.setOnClickListener(v -> {
                Intent intent = new Intent(this, Activity_Orders_Details.class);
                intent.putExtra("order_id", order.getId());
                startActivity(intent);
            });
            row.addView(seeMoreTextView);

            table.addView(row);
        }
    }

    private void radioButton(SellerOrdersDTO order) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Update Order Status");

        // Inflate custom layout with RadioGroup
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_status_options, null);
        RadioGroup radioGroup = dialogView.findViewById(R.id.statusRadioGroup);

        // Set current status as checked
        String currentStatus = order.getOrderStatus();
        if ("Pending".equals(currentStatus)) {
            radioGroup.check(R.id.pendingRadio);
        } else if ("In Transit".equals(currentStatus)) {
            radioGroup.check(R.id.processingRadio);
        } else if ("Completed".equals(currentStatus)) {
            radioGroup.check(R.id.completedRadio);
        }

        // Set view and buttons
        builder.setView(dialogView)
                .setPositiveButton("Save", (dialog, which) -> {
                    int selectedId = radioGroup.getCheckedRadioButtonId();
                    String newStatus = "Pending"; // Default

                    if (selectedId == R.id.pendingRadio) {
                        newStatus = "Pending";
                    } else if (selectedId == R.id.processingRadio) {
                        newStatus = "In Transit";
                    } else if (selectedId == R.id.completedRadio) {
                        newStatus = "Completed";
                    }

                    // Update the order status
                    updateOrderStatus(order, newStatus);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void updateOrderStatus(SellerOrdersDTO order, String newStatus) {
        order.setOrderStatus(newStatus);
        Call<SellerOrdersDTO> getOrder = apiService.getGroupOrder(order.getId());
        getOrder.enqueue(new Callback<SellerOrdersDTO>() {
            @Override
            public void onResponse(Call<SellerOrdersDTO> call, Response<SellerOrdersDTO> response) {
                SellerOrdersDTO customerOrders = response.body();
                customerOrders.setOrderList(order.getOrderList());
                customerOrders.setOrderPrice(order.getOrderPrice());
                customerOrders.setOrderStatus(newStatus);
                Call<SellerOrdersDTO> updateCall = apiService.updateGroupOrder(order.getId(),customerOrders);
                updateCall.enqueue(new Callback<SellerOrdersDTO>() {
                    @Override
                    public void onResponse(Call<SellerOrdersDTO> call, Response<SellerOrdersDTO> response) {
                        Toast.makeText(Activity_Orders_Group.this, "Updated", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Activity_Orders_Group.this, Sign_In_Success_Group_Seller.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onFailure(Call<SellerOrdersDTO> call, Throwable t) {
                        Log.e("Updating order error: ", t.getMessage());
                    }
                });
            }

            @Override
            public void onFailure(Call<SellerOrdersDTO> call, Throwable t) {
                Log.e("Getting order error: ", t.getMessage());
            }
        });
    }

    private String formatOrderList(Map<String, Integer> orderList) {
        try {
            StringBuilder formattedList = new StringBuilder();
            for (Map.Entry<String, Integer> entry : orderList.entrySet()) {
                formattedList.append(entry.getKey()) // Item name
                        .append(":")
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
