package com.example.croop.Customer;

import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.croop.R;
import com.example.croop.model.CartDTO;
import com.example.croop.retrofit.RetrofitService;
import com.example.croop.retrofit.UserAPI;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateCartItemBottomSheet extends BottomSheetDialogFragment {
    private CartDTO cartDTO;
    private RetrofitService RetrofitClient;
    private int id;

    public UpdateCartItemBottomSheet(CartDTO cartDTO, int id) {
        this.cartDTO = cartDTO;
        this.id = id;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.customer_cart_update, container, false);

        EditText quantityEditText = view.findViewById(R.id.quantityText);
        quantityEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
        Button updateButton = view.findViewById(R.id.updateButton);

        updateButton.setOnClickListener(v -> {
            int newQuantity = Integer.parseInt(quantityEditText.getText().toString());
            UserAPI userAPI = RetrofitClient.getClient().create(UserAPI.class);
            cartDTO.setQuantity(Integer.parseInt(quantityEditText.getText().toString()));
            cartDTO.setCustomerID(id);
            Call<CartDTO> updateCall = userAPI.updateCart(cartDTO.getId(), cartDTO);
            updateCall.enqueue(new Callback<CartDTO>() {
                @Override
                public void onResponse(Call<CartDTO> call, Response<CartDTO> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(getContext(), "Cart Item Updated", Toast.LENGTH_SHORT).show();
                        dismiss();
                    } else {
                        Toast.makeText(getContext(), "Failed to Update Cart Item", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<CartDTO> call, Throwable t) {
                    Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
        return view;
    }
}
