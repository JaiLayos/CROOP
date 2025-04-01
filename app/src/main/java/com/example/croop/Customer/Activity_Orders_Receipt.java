package com.example.croop.Customer;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.croop.R;
import com.example.croop.model.GroupSellers;
import com.example.croop.model.SellerOrdersDTO;
import com.example.croop.retrofit.RetrofitService;
import com.example.croop.retrofit.UserAPI;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Activity_Orders_Receipt extends AppCompatActivity {
    private TextView customer, seller, orderList, orderPrice, customerLocation, orderIDDisplay;
    private FloatingActionButton back;
    private Intent intent;
    private int orderID;
    private RetrofitService RetrofitClient;
    private UserAPI userAPI;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_receipt);
        intent = getIntent();
        orderID = intent.getIntExtra("order_id", 0);
        initializeComponents();
    }

    private void initializeComponents() {
        orderIDDisplay = findViewById(R.id.orderIDText);
        seller = findViewById(R.id.orderSellerText);
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
                Call<GroupSellers> groupSellersCall = userAPI.getGroupSellers(customerOrdersForGroupSellers.getSellerID());
                groupSellersCall.enqueue(new Callback<GroupSellers>() {
                    @Override
                    public void onResponse(Call<GroupSellers> call, Response<GroupSellers> response) {
                        GroupSellers groupSellers = response.body();
                        orderIDDisplay.setText("ORDER: #" + String.valueOf(customerOrdersForGroupSellers.getId()));
                        customer.setText(customerOrdersForGroupSellers.getCustomerName());
                        Map<String, Integer> orders = customerOrdersForGroupSellers.getOrderList();
                        String formattedOrderList = formatOrderList(orders);
                        orderList.setText(formattedOrderList);
                        orderPrice.setText("₱" + customerOrdersForGroupSellers.getOrderPrice());
                        seller.setText(groupSellers.getGroupName());
                        Map<String, String> address = groupSellers.getAddress();
                        String formatAddress = formatAddress(address);
                        customerLocation.setText(formatAddress);
                    }

                    @Override
                    public void onFailure(Call<GroupSellers> call, Throwable t) {

                    }
                });
            }

            @Override
            public void onFailure(Call<SellerOrdersDTO> call, Throwable t) {

            }
        });

        TextView download = findViewById(R.id.downloadPdfButton2);
        download.setOnClickListener(v -> {
            generatePdf();
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
    private void generatePdf() {
        // Create a new PdfDocument
        PdfDocument document = new PdfDocument();

        // Define page dimensions (A4 size: 595 x 842 points)
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(595, 842, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);

        // Get canvas for drawing
        Canvas canvas = page.getCanvas();

        // Draw the table content onto the PDF
        drawTableContent(canvas);

        // Finish the page
        document.finishPage(page);

        // Save the PDF to a file
        String filePath = savePdfToFile(document);

        // Notify the user
        if (filePath != null) {
            Toast.makeText(this, "PDF saved at: " + filePath, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Failed to save PDF", Toast.LENGTH_SHORT).show();
        }

        // Close the document
        document.close();
    }

    private void drawTableContent(Canvas canvas) {
        Paint paint = new Paint();
        paint.setTextSize(12); // Set font size

        int yPosition = 50; // Starting Y position
        int rowHeight = 30; // Height of each row
        int marginStart = 50; // Left margin
        int columnWidth = 200; // Width of each column

        // Draw Order ID
        paint.setColor(getColor(android.R.color.black));
        canvas.drawText("ORDER: ########", marginStart, yPosition, paint);
        yPosition += rowHeight;

        // Draw Customer
        paint.setColor(getColor(R.color.secondary_color));
        canvas.drawText("Customer", marginStart, yPosition, paint);
        paint.setColor(getColor(android.R.color.black));
        canvas.drawText(customer.getText().toString(), marginStart + columnWidth, yPosition, paint);
        yPosition += rowHeight;

        // Draw Seller
        paint.setColor(getColor(R.color.secondary_color));
        canvas.drawText("Seller", marginStart, yPosition, paint);
        paint.setColor(getColor(android.R.color.black));
        canvas.drawText(seller.getText().toString(), marginStart + columnWidth, yPosition, paint);
        yPosition += rowHeight;

        // Draw Order List
        paint.setColor(getColor(R.color.secondary_color));
        canvas.drawText("Order List", marginStart, yPosition, paint);
        paint.setColor(getColor(android.R.color.black));
        canvas.drawText(orderList.getText().toString(), marginStart + columnWidth, yPosition, paint);
        yPosition += rowHeight;

        // Draw Order Price
        paint.setColor(getColor(R.color.secondary_color));
        canvas.drawText("Order Price", marginStart, yPosition, paint);
        paint.setColor(getColor(android.R.color.black));
        canvas.drawText(orderPrice.getText().toString(), marginStart + columnWidth, yPosition, paint);
        yPosition += rowHeight;

        // Draw Seller Location
        paint.setColor(getColor(R.color.secondary_color));
        canvas.drawText("Seller Location", marginStart, yPosition, paint);
        paint.setColor(getColor(android.R.color.black));
        canvas.drawText(customerLocation.getText().toString(), marginStart + columnWidth, yPosition, paint);
        yPosition += rowHeight;
    }

    private String savePdfToFile(PdfDocument document) {
        // Define the file path
        File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File file = new File(downloadsDir, "order_receipt.pdf");

        try (FileOutputStream fos = new FileOutputStream(file)) {
            document.writeTo(fos);
            return file.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
