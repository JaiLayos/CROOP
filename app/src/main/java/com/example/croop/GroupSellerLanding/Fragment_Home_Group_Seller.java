package com.example.croop.GroupSellerLanding;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.croop.R;
import com.example.croop.model.GroupSellers;
import com.example.croop.model.SellerOrdersDTO;
import com.example.croop.retrofit.RetrofitService;
import com.example.croop.retrofit.UserAPI;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Home_Group_Seller extends Fragment {
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private RetrofitService RetrofitClient;
    private UserAPI userAPI;
    private TextView name, bio;
    private int userID;
    private int initialRevenue = 0, initialCount = 0;
    private TextView totalRevenue, countProducts, meanRevenue, modeRevenue, meanCount, modeCount;
    private List<Entry> chartEntries = new ArrayList<>();
    private List<String> dateLabels = new ArrayList<>();
    private LineChart lineChart;
    private List<Entry> entries;

    public Fragment_Home_Group_Seller() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.home_seller, container, false);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        name = view.findViewById(R.id.userNameDisplay);
        bio = view.findViewById(R.id.userBioDisplay);

        countProducts = view.findViewById(R.id.countProductsText);
        totalRevenue = view.findViewById(R.id.totalRevenueText);

        meanRevenue = view.findViewById(R.id.meanRevenueText);
        modeRevenue = view.findViewById(R.id.modeRevenueText);

        meanCount = view.findViewById(R.id.meanCountText);
        modeCount = view.findViewById(R.id.modeCountText);

        lineChart = view.findViewById(R.id.lineChart);

        userAPI = RetrofitClient.getClient().create(UserAPI.class);

        // Get the user role from SharedPreferences
        SharedPreferences prefs = getActivity().getSharedPreferences("user_prefs", getContext().MODE_PRIVATE);
        String role = prefs.getString("user_role", null);

        String collection = getCollection(role);
        DocumentReference docRef = db.collection(collection).document(user.getUid());

        // Fetch user data from Firestore
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Toast.makeText(getActivity(), "Mabuhay! " + role + " " + documentSnapshot.getString("Name"), Toast.LENGTH_SHORT).show();
            }
        }).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String name_user = document.getString("Name");
                        name.setText(name_user);
                        String bio_user = document.getString("Bio");
                        bio.setText(bio_user);
                    }
                }
            }
        });
        CardView order, inventory, discount;
        order = view.findViewById(R.id.ordersContainer);
        order.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), Activity_Orders_Group.class);
            startActivity(intent);
        });
        inventory = view.findViewById(R.id.inventoryContainer);
        inventory.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), Activity_Inventory_Category.class);
            startActivity(intent);
        });
        discount = view.findViewById(R.id.discountsContainer);
        discount.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), Activity_Discount_Inventory.class);
            startActivity(intent);
        });
        Call<GroupSellers> groupSellersCall = userAPI.getGroupSellersbyFirebaseID(user.getUid());
        groupSellersCall.enqueue(new Callback<GroupSellers>() {
            @Override
            public void onResponse(Call<GroupSellers> call, Response<GroupSellers> response) {
                GroupSellers groupSellers = response.body();
                userID = groupSellers.getId();
                getSumOfAllPrice(userID);
            }

            @Override
            public void onFailure(Call<GroupSellers> call, Throwable t) {
                Log.e("Getting Group Seller ID Error: ", t.getMessage());
            }
        });

        return view;
    }

    private void getSumOfAllPrice(int userID) {
        Map<String, Integer> dailySales = new HashMap<>();
        Map<String, Integer> dailyCounts = new HashMap<>();
        List<Integer> storePrice = new ArrayList<>();
        List<Integer> storeCount = new ArrayList<>();
        Call<List<SellerOrdersDTO>> call = userAPI.getGroupOrderByGroupSellerId(userID);
        call.enqueue(new Callback<List<SellerOrdersDTO>>() {
            @Override
            public void onResponse(Call<List<SellerOrdersDTO>> call, Response<List<SellerOrdersDTO>> response) {
                List<SellerOrdersDTO> ordersDTOS = response.body();
                for(SellerOrdersDTO ordersDTO : ordersDTOS){
                    if(ordersDTO.getOrderStatus().equals("Completed")){
                        initialRevenue = initialRevenue + ordersDTO.getOrderPrice();
                        Map<String, Integer> orderList = ordersDTO.getOrderList();
                        storePrice.add(ordersDTO.getOrderPrice());
                        for(Map.Entry<String, Integer> order : orderList.entrySet()){
                            initialCount = initialCount + order.getValue();
                        }
                        storeCount.add(initialCount);
                        String date = formatDate(ordersDTO.getOrderDate());
                        int orderPrice = ordersDTO.getOrderPrice();
                        if (dailySales.containsKey(date)) {
                            dailySales.put(date, dailySales.get(date) + orderPrice);
                        } else {
                            dailySales.put(date, orderPrice);
                        }
                        int itemCount = calculateItemCount(orderList);
                        if (dailyCounts.containsKey(date)) {
                            dailyCounts.put(date, dailyCounts.get(date) + itemCount);
                        } else {
                            dailyCounts.put(date, itemCount);
                        }
                    }
                }
                totalRevenue.setText("₱"+ String.valueOf(initialRevenue));
                countProducts.setText(String.valueOf(initialCount));

                double meanOfRevenue = initialRevenue / storePrice.size();
                meanRevenue.setText("₱"+String.valueOf(meanOfRevenue));

                double meanOfCount = initialCount / storeCount.size();
                meanCount.setText(String.valueOf(meanOfCount));

                int rangeRevenue = calculateRange(storePrice);
                modeRevenue.setText("₱" + String.valueOf(rangeRevenue));

                int rangeCount = calculateRange(storeCount);
                modeCount.setText(String.valueOf(rangeCount));

                List<String> sortedDates = new ArrayList<>(dailySales.keySet());
                Collections.sort(sortedDates);

                for (int i = 0; i < sortedDates.size(); i++) {
                    String date = sortedDates.get(i);
                    int totalSalesForDay = dailySales.get(date);
                    chartEntries.add(new Entry(i, totalSalesForDay));
                    dateLabels.add(date);
                }

                List<BarEntry> barEntries = new ArrayList<>();
                List<String> sortedDatesFromDaily = new ArrayList<>(dailyCounts.keySet());
                Collections.sort(sortedDates); // Sort dates chronologically

                for (int i = 0; i < sortedDatesFromDaily.size(); i++) {
                    String date = sortedDatesFromDaily.get(i);
                    int itemCountForDay = dailyCounts.get(date);
                    barEntries.add(new BarEntry(i, itemCountForDay)); // Add BarEntry
                }

                // Update UI
                totalRevenue.setText("P" + String.valueOf(initialRevenue));
                countProducts.setText(String.valueOf(initialCount));

                // Plot the LineChart
                layoutLineChart(chartEntries, dateLabels);
                layoutBarChart(barEntries, dateLabels);
            }

            @Override
            public void onFailure(Call<List<SellerOrdersDTO>> call, Throwable t) {
                Log.e("Getting Group Seller Sum of Sell Error: ", t.getMessage());
            }
        });
    }

    private int calculateRange(List<Integer> values) {
        if (values == null || values.isEmpty()) {
            return 0; // Handle empty list
        }

        int max = Collections.max(values);
        int min = Collections.min(values);
        return max - min;
    }

    private void layoutBarChart(List<BarEntry> barEntries, List<String> dateLabels) {
        BarDataSet barDataSet = new BarDataSet(barEntries, "Products Sold");
        barDataSet.setValueTextColor(ContextCompat.getColor(getActivity(), R.color.beige_brackground));
        barDataSet.setColor(ContextCompat.getColor(getActivity(), R.color.highlight_green)); // Set line color
        barDataSet.setValueTextColor(ContextCompat.getColor(getActivity(), R.color.secondary_color));

        BarData barData = new BarData(barDataSet);
        barData.setBarWidth(0.5f); // Set bar width

        BarChart barChart = getActivity().findViewById(R.id.barChart);
        barChart.setData(barData);
        barChart.setNoDataText("No data available");

        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setGridColor(ContextCompat.getColor(getActivity(), R.color.beige_brackground));
        xAxis.setTextColor(ContextCompat.getColor(getActivity(), R.color.beige_brackground));
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                int index = (int) value;
                if (index >= 0 && index < dateLabels.size()) {
                    return dateLabels.get(index); // Display the date label
                }
                return "";
            }
        });

        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setAxisMinimum(0f);
        leftAxis.setTextColor(ContextCompat.getColor(getActivity(), R.color.beige_brackground));

        barChart.getAxisRight().setEnabled(false);

        barChart.invalidate();
    }

    private int calculateItemCount(Map<String, Integer> orderList) {
        int totalCount = 0;
        for (Integer count : orderList.values()) {
            totalCount += count;
        }
        return totalCount;
    }

    private void layoutLineChart(List<Entry> entries, List<String> dateLabels) {
        LineDataSet dataSet = new LineDataSet(entries, "Daily Sales");
        dataSet.setValueTextColor(ContextCompat.getColor(getActivity(), R.color.beige_brackground));
        dataSet.setColor(ContextCompat.getColor(getActivity(), R.color.highlight_green)); // Set line color
        dataSet.setValueTextColor(ContextCompat.getColor(getActivity(), R.color.secondary_color));
        dataSet.setLineWidth(2f);

        Legend legend = lineChart.getLegend();
        legend.setTextColor(ContextCompat.getColor(getActivity(), R.color.beige_brackground));

        LineData lineData = new LineData(dataSet);

        lineChart.setData(lineData);
        lineChart.setNoDataText("No data available");

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setGridColor(ContextCompat.getColor(getActivity(), R.color.beige_brackground));
        xAxis.setTextColor(ContextCompat.getColor(getActivity(), R.color.beige_brackground));
        xAxis.setYOffset(8f);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                int index = (int) value;
                if (index >= 0 && index < dateLabels.size()) {
                    return dateLabels.get(index); // Display the date label
                }
                return "";
            }
        });

        YAxis yAxis = lineChart.getAxisLeft();
        yAxis.setTextColor(ContextCompat.getColor(getActivity(), R.color.beige_brackground));
        yAxis.setAxisMinimum(0f);
        yAxis.setXOffset(10f);
        lineChart.getAxisRight().setEnabled(false);
        lineChart.invalidate();
    }


    private String getCollection(String role) {
        SharedPreferences prefs = getActivity().getSharedPreferences("user_prefs", getContext().MODE_PRIVATE);
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
            case "Individual Customer User":
                collection = "Customers";
                break;
            case "Group Customer User":
                collection = "Group Customers";
                break;
            default:
                collection = "Unknown";
                break;
        }

        editor.putString("user_collection", collection).apply();
        return collection;
    }
    private String formatDate(Date date) {
        if (date == null) return "N/A";
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
        return sdf.format(date);
    }
}
