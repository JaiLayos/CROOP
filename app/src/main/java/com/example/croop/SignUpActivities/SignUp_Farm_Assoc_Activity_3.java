package com.example.croop.SignUpActivities;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType; // import statement for password
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.croop.R;
import com.example.croop.SignIn_Activity;
import com.example.croop.model.GroupSellers;
import com.example.croop.singleton.GroupSellersSingleton;

public class SignUp_Farm_Assoc_Activity_3 extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_farm_assoc_3);
        initializeComponents();
        TextView signInHyperlink = findViewById(R.id.signInHyperlink6);
        signInHyperlink.setOnClickListener(view -> {
            Intent intent = new Intent(SignUp_Farm_Assoc_Activity_3.this, SignIn_Activity.class);
            startActivity(intent);
        });
    }

    private void initializeComponents() {
        EditText passwordText = findViewById(R.id.assocPasswordText);
        EditText rePasswordText = findViewById(R.id.assocRePasswordText);

        passwordText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        rePasswordText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        Button next = findViewById(R.id.nextButton_SUF_3);
        next.setOnClickListener(view -> {
            String password = passwordText.getText().toString();
            String rePassword = rePasswordText.getText().toString();
            if (!isValidPassword(password)) {
                Toast.makeText(this, "Password does not meet the requirements.", Toast.LENGTH_LONG).show();
                return;
            }
            if(password.matches(rePassword)){
                GroupSellers groupSellers = GroupSellersSingleton.getInstance().getGroupSellers();
                groupSellers.setPassword(password);
                Intent intent = new Intent(this, SignUp_Farm_Assoc_Activity_4.class);
                startActivity(intent);
            }else{
                Toast.makeText(this, "Password doesn't match", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isValidPassword(String password) {
        if (password.length() < 8 || password.length() > 32) {
            return false;
        }
        if (!password.matches(".*[a-z].*")) {
            return false;
        }
        if (!password.matches(".*[A-Z].*")) {
            return false;
        }
        if (!password.matches(".*\\d.*")) {
            return false;
        }
        if (!password.matches(".*[!@#$%^&*_].*")) {
            return false;
        }
        return true;
    }
}
