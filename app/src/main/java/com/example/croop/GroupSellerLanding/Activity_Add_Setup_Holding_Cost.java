package com.example.croop.GroupSellerLanding;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.croop.R;
import com.example.croop.model.GroupSellers;
import com.example.croop.retrofit.RetrofitService;
import com.example.croop.retrofit.UserAPI;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Activity_Add_Setup_Holding_Cost extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private UserAPI userAPI;
    private RetrofitService RetrofitClient;
    private int id;
    private EditText laborText, processText, packagingText,
    refText, storageText, insuranceText;
    private FloatingActionButton back;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_setup_maintenance_inventory);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        userAPI = RetrofitClient.getClient().create(UserAPI.class);
        laborText = findViewById(R.id.laborText);
        processText = findViewById(R.id.processText);
        packagingText = findViewById(R.id.packagingText);
        refText = findViewById(R.id.refText);
        storageText = findViewById(R.id.storageText);
        insuranceText = findViewById(R.id.insuranceText);

        initializeComponents();
    }

    private void initializeComponents() {
        String firebaseID = user.getUid();
        Button next = findViewById(R.id.nextButton);
        next.setOnClickListener(v -> {

            if (!validateInputs()) {
                Toast.makeText(this, "Mangyaring sagutan ang bawat kahon!", Toast.LENGTH_SHORT).show();
                return;
            }

            getIDofSeller(firebaseID);

        });

        back = findViewById(R.id.backFloat);
        back.setOnClickListener(v -> {
            onBackPressed();
        });

        TextView setupHelp = findViewById(R.id.setupHelpLink);
        setupHelp.setOnClickListener(v-> {
            showSetupCostExplanation();
        });

        TextView holdingHelp = findViewById(R.id.holdingHelpLink);
        holdingHelp.setOnClickListener(v-> {
            showHoldCostHelp();
        });
    }

    private void showHoldCostHelp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        TextView textView = new TextView(this);
        textView.setText("Ang holding cost ay tumutukoy sa gastos na nauugnay sa " +
                "pag-iimbak ng mga produkto o inventory sa loob ng isang tiyak na panahon. " +
                "Ito ay kasama sa mga operational costs ng isang negosyo, at mahalaga ito sa pag-compute ng " +
                "pinakamabuting dami ng produkto na dapat ipagbili o i-stock upang mapanatili ang kabuuang gastos " +
                "sa pinakamababang antas. Ito ay ginagamit bilang basehan para magdesisyon kung gaano karaming produkto " +
                "ang dapat i-stock bago dumating ang susunod na order cycle. Ang demand-based threshold ay isang paraan upang matantya ang \"critical point\" " +
                "kung saan mas mura na magbenta o mag-order ng bagong stock kaysa ipagpatuloy ang pag-iimbak ng produkto dahil sa mataas na gastos ng pag-iimbak.");
        textView.setPadding(80, 40, 80, 40); // Add padding for better readability
        textView.setTextSize(16);

        ScrollView scrollView = new ScrollView(this);
        scrollView.addView(textView);

        builder.setTitle("Ano ang holding cost?");
        builder.setView(scrollView);

        builder.setPositiveButton("Naintindihan ko", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void getIDofSeller(String firebaseID) {
        Call<GroupSellers> groupSellersCall = userAPI.getGroupSellersbyFirebaseID(firebaseID);
        groupSellersCall.enqueue(new Callback<GroupSellers>() {
            @Override
            public void onResponse(Call<GroupSellers> call, Response<GroupSellers> response) {
                GroupSellers groupSellers = response.body();
                int id = groupSellers.getId();
                getSeller(id, firebaseID);
            }

            @Override
            public void onFailure(Call<GroupSellers> call, Throwable t) {
                Log.e("Getting Group Seller ID Error: ", t.getMessage());
            }
        });
    }

    private void showSetupCostExplanation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Ano ang Setup Cost?");
        builder.setMessage("Ang setup cost ay tumutukoy sa fixed cost na idinadagdag tuwing mag-oorder o magpapagawa ng bagong batch ng produkto. " +
                "Hindi ito depende sa dami ng order o produksyon. Kasama dito ang:\n\n" +
                "• Paghahanda ng makinarya para sa produksyon.\n" +
                "• Admin costs para sa pag-order (hal. papeles, approval).\n" +
                "• Transportation at logistics costs.\n\n" +
                "Sa Wagner-Whitin Model, mahalaga ang setup cost sa pagtukoy ng tamang oras at dami ng order. " +
                "Kung mataas ang setup cost, mas mura na mag-order ng malaking dami pero bihira. " +
                "Ngunit kung mababa ang setup cost, mas mainam na mag-order ng maliit na dami pero madalas upang bawasan ang holding cost.");

        builder.setPositiveButton("Naintindihan ko", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss(); // Close the dialog
            }
        });

        builder.setCancelable(false);

        // Create and show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void getSeller(int id, String firebaseID) {
        int labor = Integer.parseInt(laborText.getText().toString());
        int process = Integer.parseInt(processText.getText().toString());
        int packaging = Integer.parseInt(packagingText.getText().toString());
        int ref = Integer.parseInt(refText.getText().toString());
        int storage = Integer.parseInt(storageText.getText().toString());
        int insurance = Integer.parseInt(insuranceText.getText().toString());
        Call<GroupSellers> groupSellersCall = userAPI.getGroupSellers(id);

        groupSellersCall.enqueue(new Callback<GroupSellers>() {
            @Override
            public void onResponse(Call<GroupSellers> call, Response<GroupSellers> response) {
                GroupSellers groupSellers = response.body();
                groupSellers.setProduct_inventory_SC(labor+process+packaging);
                groupSellers.setProduct_inventory_MC(ref+storage+insurance);
                setInventoryThreshold(firebaseID, groupSellers);
            }

            @Override
            public void onFailure(Call<GroupSellers> call, Throwable t) {
                Log.e("Getting Group Seller Error: ", t.getMessage());
            }
        });

    }

    private void setInventoryThreshold(String id, GroupSellers groupSellers) {
        Call<GroupSellers> groupSellersCall = userAPI.updateGroupSellersByFirebaseID(id, groupSellers);
        groupSellersCall.enqueue(new Callback<GroupSellers>() {
            @Override
            public void onResponse(Call<GroupSellers> call, Response<GroupSellers> response) {
                Toast.makeText(Activity_Add_Setup_Holding_Cost.this, "Matagumpay ang pagbigay ng impormasyon", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Activity_Add_Setup_Holding_Cost.this, Activity_Products_Inventory.class);
                finish();
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<GroupSellers> call, Throwable t) {
                Log.e("Updating Group Seller Error: ", t.getMessage());
            }
        });
    }

    private boolean validateInputs() {
        EditText[] fields = {laborText, processText, packagingText, refText, storageText, insuranceText};

        for (EditText field : fields) {
            if (field.getText().toString().trim().isEmpty()) {
                return false;
            }
            field.setInputType(InputType.TYPE_CLASS_NUMBER);
        }
        return true;
    }
}
