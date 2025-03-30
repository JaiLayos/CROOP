package com.example.croop;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.croop.Customer.Sign_In_Success_Customer;
import com.example.croop.GroupSellerLanding.Sign_In_Success_Group_Seller;
import com.example.croop.IndividualSellerLanding.Sign_In_Success_Individual_Seller;
import com.example.croop.model.CurrentUsage;
import com.example.croop.singleton.CurrentUsageSingleton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Landing_Activity extends AppCompatActivity {
    FirebaseAuth mAuth;


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.landing_page);
        initializeComponent();

    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
            String role = prefs.getString("user_role", null);

            if (role == null) {
                Toast.makeText(this, "Walang nakalog in mangyaring mag-sign in muli.", Toast.LENGTH_SHORT).show();
                FirebaseAuth.getInstance().signOut();
                return;
            }
            switch (role) {
                case "Group Business User (Association)":
                case "Group Business User (Cooperative)":{
                    Intent intent = new Intent(Landing_Activity.this, Sign_In_Success_Group_Seller.class);
                    startActivity(intent);
                    finish();
                    break;
                }
                case "Individual Business User": {
                    Intent intent = new Intent(Landing_Activity.this, Sign_In_Success_Individual_Seller.class);
                    startActivity(intent);
                    finish();
                    break;
                }
                case "Customer User": {
                    Intent intent = new Intent(Landing_Activity.this, Sign_In_Success_Customer.class);
                    startActivity(intent);
                    finish();
                    break;
                }
                default:
                    Toast.makeText(Landing_Activity.this, "Invalid role: " + role, Toast.LENGTH_SHORT).show();
                    break;
            }
        }else{
            Toast.makeText(Landing_Activity.this, "Mag-login po ulit.", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    private void initializeComponent() {
        Button signIn = findViewById(R.id.signInButton);
        Button signUp = findViewById(R.id.signUpButton);
        signIn.setOnClickListener( view -> {
            CurrentUsage currentUsage = new CurrentUsage();
            currentUsage.setCurrentUsage("Sign In");
            CurrentUsageSingleton.getInstance().setCurrentUsageSingleton(currentUsage);
            Intent intent = new Intent(Landing_Activity.this, Roles_Activity.class);
            startActivity(intent);
        });
        signUp.setOnClickListener(view -> {
            CurrentUsage currentUsage = new CurrentUsage();
            currentUsage.setCurrentUsage("Sign Up");
            CurrentUsageSingleton.getInstance().setCurrentUsageSingleton(currentUsage);
            showTermsAndConditionsDialog();
        });
    }

    private void showTermsAndConditionsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Create a TextView to display the Terms and Conditions
        TextView textView = new TextView(this);
        textView.setText("Bago mag-sign up para sa isang account sa CRO-OP, mangyaring basahin nang mabuti ang sumusunod na Mga Tuntunin at Kundisyon. Sa paggawa ng account, sumasang-ayon kang sumunod sa mga tuntuning ito:\n\n" +
                "• Dapat ikaw ay 18 taong gulang pataas upang magamit ang aming platform.\n" +
                "• Ang pagbibigay ng tumpak at kumpletong impormasyon sa panahon ng pagpaparehistro ay sapilitan, at ikaw lamang ang responsable sa pagpapanatili ng pagiging kumpidensyal ng iyong mga kredensyal ng account.\n" +
                "• Sumasang-ayon ka na hindi gagamitin ang platform para sa anumang ilegal o hindi awtorisadong layunin, kabilang ngunit hindi limitado sa mga mapanlinlang na aktibidad, pag-i-spam, o panliligalig sa ibang mga gumagamit.\n" +
                "• Ang anumang nilalaman na iyong ina-upload o ibinabahagi ay hindi dapat lumalabag sa mga karapatan ng iba, kabilang ang mga karapatan sa intelektwal na pag-aari.\n" +
                "• Inilalaan ng CRO-OP ang karapatang suspindihin o wakasan ang iyong account kung lalabag ka sa mga tuntuning ito.\n" +
                "• Maaari naming i-update ang Mga Tuntunin at Kundisyon na ito paminsan-minsan, at responsibilidad mong suriin ang mga ito nang pana-panahon.\n" +
                "• Sa patuloy na paggamit ng platform pagkatapos ng anumang mga update, sumasang-ayon ka sa mga binagong tuntunin.\n" +
                "• Ang iyong paggamit ng CRO-OP ay nasa iyong sariling peligro, at hindi kami mananagot para sa anumang mga pinsalang nagmumula sa iyong paggamit ng platform.\n" +
                "• Ang mga tuntuning ito ay pinamamahalaan ng mga batas ng Pilipinas, at ang anumang mga hindi pagkakasundo ay lulutasin alinsunod sa mga batas na ito.\n\n" +
                "Sa pagpapatuloy sa proseso ng pag-sign up, kinikilala mo na nabasa mo, naunawaan, at sinang-ayunan mo ang Mga Tuntunin at Kundisyon na ito.");
        textView.setPadding(80, 40, 80, 40); // Add padding for better readability
        textView.setTextSize(16); // Set text size

        // Wrap the TextView in a ScrollView to make it scrollable
        ScrollView scrollView = new ScrollView(this);
        scrollView.addView(textView);

        // Set the title and view for the dialog
        builder.setTitle("Mga Tuntunin at Kundisyon");
        builder.setView(scrollView);

        // Add "Agree" button
        builder.setPositiveButton("Sumang-ayon", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Landing_Activity.this, Roles_Activity.class);
                startActivity(intent);
            }
        });

        // Add "Disagree" button
        builder.setNegativeButton("Hindi Sumang-ayon", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss(); // Close the dialog
                onBackPressed(); // Go back or exit
                finish(); // End the activity
            }
        });

        // Make the dialog non-cancelable (user must explicitly choose Agree or Disagree)
        builder.setCancelable(false);

        // Create and show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
